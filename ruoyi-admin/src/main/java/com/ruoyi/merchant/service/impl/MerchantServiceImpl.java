package com.ruoyi.merchant.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.ruoyi.common.enums.YesNoEnum;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.merchant.domain.*;
import com.ruoyi.merchant.domain.dto.OrderProductDTO;
import com.ruoyi.merchant.domain.dto.ProductImgDTO;
import com.ruoyi.merchant.domain.dto.ProductListDTO;
import com.ruoyi.merchant.domain.req.*;
import com.ruoyi.merchant.domain.vo.OrderCreateVO;
import com.ruoyi.merchant.domain.vo.ProductDetailVO;
import com.ruoyi.merchant.domain.vo.ProductListVO;
import com.ruoyi.merchant.enums.CheckOnShelfEnum;
import com.ruoyi.merchant.enums.CustomerStatusEnum;
import com.ruoyi.merchant.enums.OrderStatusEnum;
import com.ruoyi.merchant.factory.PriceStrategyFactory;
import com.ruoyi.merchant.manager.OrderManager;
import com.ruoyi.merchant.manager.ProductImageManager;
import com.ruoyi.merchant.manager.ProductManager;
import com.ruoyi.merchant.manager.CustomerManager;
import com.ruoyi.merchant.service.MerchantService;
import com.ruoyi.merchant.service.OrderService;
import com.ruoyi.merchant.strategy.PriceCalculatorStrategy;
import com.ruoyi.merchant.util.CollectUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.merchant.manager.OrderProductManager;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
public class MerchantServiceImpl implements MerchantService {
    
    @Resource
    OrderService orderService;
    
    @Resource
    ProductManager productManager;
    
    @Resource
    ProductImageManager productImageManager;
    
    @Resource
    CustomerManager customerManager;
    
    @Resource
    OrderManager orderManager;
    
    @Resource
    OrderProductManager orderProductManager;

    @Resource
    private PriceStrategyFactory priceStrategyFactory;

    @Resource
    private RedissonClient redissonClient;

    /**
     * 商家端-商品列表查询
     * @param req 请求对象
     * @return 商品列表
     */
    @Override
    public List<ProductListVO> findProductList(ProductListReq req) {
        // 查询商品列表
        List<Product> productList = getDbProducts(req);
        
        //  封装商品列表响应数据
        return buildProductListVOList(productList);
    }

    /**
     * 商家端-查询商品详情
     * @param productId 商品id
     * @return 商品详情
     */
    @Override
    public ProductDetailVO findProductDetail(String productId) {
        // 得到扁平的单个商品详情数据
        Product product = getDbProductById(productId);

        // 获取商品图片数据
        List<ProductImage> productImgList = getDbProductImagesByPrdId(productId);

        // 组装响应对象
        return assembleProductDetailVO(productId, product, productImgList);
    }

    /**
     * 商家端-添加商品
     * @param productAddReq 添加商品请求体
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addProduct(ProductAddReq productAddReq){
        // 校验并获取图片列表数据
        List<ProductImgDTO> prdImgDTOList = validAndGetImgList(productAddReq);

        // 商品主表主键id
        String savePrdId = IdUtil.fastSimpleUUID();

        // 插入商品主表
        saveDbProduct(productAddReq, savePrdId);

        // 处理图片数据
        resolveDisplayImg(prdImgDTOList);

        // 批量插入商品图片数据
        saveBatchDbProductImage(prdImgDTOList, savePrdId);
    }

    /**
     * 商家端-更新商品详情
     * todo 商品上架时不允许修改，必须先下架才能修改商品详情信息
     * @param productEditReq 更新商品详情请求体
     */
    @Transactional
    @Override
    public void updateProductDetail(ProductEditReq productEditReq) {
        // 开始更新主表数据
        updateDbProduct(productEditReq);

        // 开始更新商品图片数据，采用全量更新
        fullUpdateDbProductDetail(productEditReq);
    }

    /**
     * 商家端-下单
     * 需要返回库存不足的商品
     * 单笔订单中商品种类较少时使用该方法下单，大宗采购场景不可用该方法
     * @param merchantOrderCreateReq 商家端下单请求体
     * @return 商家端下单响应数据
     */
    @Override
    public OrderCreateVO merchantOrderCreate(MerchantOrderCreateReq merchantOrderCreateReq) {
        // 下单操作开始时间
        Date orderCreateTime = new Date();
        
        // 生成订单uuid
        String orderId = IdUtil.fastSimpleUUID();

        // 获取客户信息并校验
        Customer customer = validAndGetCustomer(merchantOrderCreateReq.getReceiverPhone());

        // 获取商品信息并校验（存在性，是否上架）
        Map<String, Product> dbProductIdMap = validProductAndGetMap(merchantOrderCreateReq);
        
        // 计算订单总金额
        BigDecimal totalPrice = calculateTotalPrice(merchantOrderCreateReq, dbProductIdMap, customer);
        
        // 库存扣减 订单创建
        List<RLock> successLocks = new ArrayList<>();
        try {
            // 将商品按productId进行排序，防止出现 线程1下单a商品、b商品，线程2下单b商品、a商品 导致的死锁问题
            List<String> dbProductIdList = dbProductIdMap.keySet().stream().sorted().collect(Collectors.toList());
            for (String dbProductId : dbProductIdList) {
                // 创建锁，粒度：ORDER_CREATE:productId
                RLock lock = redissonClient.getLock(StrUtil.format("{}:{}", "ORDER_CREATE", dbProductId));
                // 最大等待时间：3s，自动释放时间：10s
                boolean isLocked = lock.tryLock(3, 10, TimeUnit.SECONDS);
                // 未获取到锁，直接返回失败
                if (!isLocked) {
                    throw new ServiceException(StrUtil.format("系统繁忙，请重试"));
                }
                // 将锁加入列表
                successLocks.add(lock);
            }
            ThreadUtil.sleep(10, TimeUnit.SECONDS);
            
            // 进入下单逻辑
            orderService.createOrder(merchantOrderCreateReq, dbProductIdMap, customer, orderCreateTime, orderId, totalPrice);
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ServiceException("下单异常，请重试");
        } finally {
            // 无论请求结果，必须释放掉所有的锁
            for (RLock lock : successLocks) {
                if (lock.isHeldByCurrentThread()) {
                    lock.unlock();
                }
            }
        }

        // 组装响应数据并返回
        return assembleOrderCreateVO(merchantOrderCreateReq, dbProductIdMap, orderId, totalPrice, orderCreateTime);
    }

    @Override
    @Transactional
    public void merchantDeliver(MerchantDeliverReq merchantDeliverReq) {
        // 校验订单是否已经发货
        Order dbOrder = orderManager.getOrderByOrderId(merchantDeliverReq.getOrderId());
        if (!NumberUtil.equals(dbOrder.getStatus(), OrderStatusEnum.ORDER_PAY.getCode())) {
            throw new ServiceException(StrUtil.format("该订单状态为 {}，不能发货", dbOrder.getStatus()));
        }

        // 修改订单状态
        dbOrder.setStatus(OrderStatusEnum.ORDER_DELIVERY.getCode());
        if (!orderManager.updateById(dbOrder)) {
            throw new ServiceException("发货失败，系统繁忙，请联系管理员");
        }
        // 插入发货记录表
        Shipment dbShipment = new Shipment();
        dbShipment.setId(IdUtil.fastSimpleUUID());
        dbShipment.setOrderId(dbOrder.getId());
        
    }

    /**
     * 商家端-订单支付（暂未开发聚合支付功能，先使用mock的方式）
     * @param merchantPayRequest 订单支付请求体
     */
    @Override
    @Transactional
    public void mockPay(MerchantPayRequest merchantPayRequest) {
        // 校验
        Order dbOrder = getOrderAndValid(merchantPayRequest);

        // 数据处理
        updateOrder(dbOrder);
    }

    private void updateOrder(Order dbOrder) {
        String mockPayType = "mock_pay";
        assembleDbOrder(dbOrder, mockPayType);
        if (!orderManager.updateById(dbOrder)) {
            throw new ServiceException("支付失败，系统繁忙，请联系管理员");
        }
    }

    private static void assembleDbOrder(Order dbOrder, String mockPayType) {
        dbOrder.setPayTime(new Date());
        dbOrder.setPayType(mockPayType);
        dbOrder.setStatus(OrderStatusEnum.ORDER_PAY.getCode());
    }

    private Order getOrderAndValid(MerchantPayRequest merchantPayRequest) {
        // 订单是否存在
        Order dbOrder = orderManager.getOrderByOrderId(merchantPayRequest.getOrderId());
        if (ObjUtil.isNull(dbOrder)) {
            throw new ServiceException(StrUtil.format("订单 {} 不存在)", merchantPayRequest.getOrderId()));
        }
        // 订单状态是否为：订单已创建
        if (!NumberUtil.equals(dbOrder.getStatus(), OrderStatusEnum.ORDER_NOT_PAY.getCode())) {
            throw new ServiceException(StrUtil.format("该订单状态为 {} ,不能支付", OrderStatusEnum.getDescByCode(dbOrder.getStatus())));
        }
        return dbOrder;
    }

    private List<Product> getDbProducts(ProductListReq req) {
        Product product = new Product();
        BeanUtil.copyProperties(req, product);
        // 通过分页获取商品数据
        List<Product> productList = productManager.findProductList(product);
        if (CollUtil.isEmpty(productList)) {
            return new ArrayList<>();
        }
        return productList;
    }


    private ProductDetailVO assembleProductDetailVO(String productId, Product product, List<ProductImage> productImgList) {
        ProductDetailVO productDetailVO = new ProductDetailVO();
        BeanUtil.copyProperties(product, productDetailVO);
        productDetailVO.setProductId(productId);
        // 获取首页展示图片
        String displayImg = getDisplayImg(productImgList);
        productDetailVO.setShowImgUrl(displayImg);
        productDetailVO.setProductImageList(productImgList);
        return productDetailVO;
    }

    private List<ProductImage> getDbProductImagesByPrdId(String productId) {
        List<ProductImage> productImgList = productManager.findProductImgListByPrdId(productId);
        if (CollUtil.isEmpty(productImgList)) {
            throw new ServiceException("该商品不存在图片信息");
        }
        return productImgList;
    }

    private Product getDbProductById(String productId) {
        Product product = productManager.findProductById(productId);
        if (ObjUtil.isNull(product)) {
            throw new ServiceException("该商品不存在");
        }
        return product;
    }

    private void saveBatchDbProductImage(List<ProductImgDTO> prdImgDTOList, String savePrdId) {
        List<ProductImage> savePrdImgList = prdImgDTOList.stream()
                .map(img -> assembleDbProductImage(savePrdId, img))
                .collect(Collectors.toList());
        if (!productImageManager.saveBatch(savePrdImgList)) {
            throw new ServiceException("商品图片数据更新失败，商品新增失败");
        }
    }

    private static ProductImage assembleDbProductImage(String savePrdId, ProductImgDTO img) {
        ProductImage savePrdImg = new ProductImage();
        savePrdImg.setId(IdUtil.fastSimpleUUID());
        savePrdImg.setProductId(savePrdId);
        savePrdImg.setImageUrl(img.getImageUrl());
        savePrdImg.setDisplayOrder(img.getDisplayOrder());
        savePrdImg.setIsDisplay(ObjUtil.defaultIfNull(img.getIsDisplay(), YesNoEnum.NO.getCode()));
        savePrdImg.setCreatedTime(new Date());
        savePrdImg.setCreatedUser(SecurityUtils.getUsername());
        return savePrdImg;
    }

    private static void resolveDisplayImg(List<ProductImgDTO> prdImgDTOList) {
        boolean hasDisplayImg = prdImgDTOList.stream().anyMatch(img -> ObjUtil.equals(img.getIsDisplay(), YesNoEnum.YES.getCode()));
        if (!hasDisplayImg) {
            prdImgDTOList.get(0).setIsDisplay(YesNoEnum.YES.getCode());
        }
    }

    private void saveDbProduct(ProductAddReq productAddReq, String savePrdId) {
        Product product = new Product();
        BeanUtil.copyProperties(productAddReq, product);
        product.setId(savePrdId);
        product.setCreatedUser(SecurityUtils.getUsername());
        product.setCreatedTime(new Date());
        if (!productManager.save(product)) {
            throw new ServiceException("商品数据新增失败");
        }
    }

    private List<ProductImgDTO> validAndGetImgList(ProductAddReq productAddReq){
        List<ProductImgDTO> dtoList = productAddReq.getPrdImgList();
        if (CollUtil.isEmpty(dtoList)) {
            throw new ServiceException("图片数据为空，请至少上传一张图片");
        }
        return dtoList;
    }

    private List<ProductImgDTO> validAndGetImgList(ProductEditReq productEditReq){
        List<ProductImgDTO> dtoList = productEditReq.getPrdImgList();
        if (CollUtil.isEmpty(dtoList)) {
            throw new ServiceException("图片数据为空，请至少上传一张图片");
        }
        return dtoList;
    }
    
    private String getDisplayImg(List<ProductImage> productImgList){
        // 获取首页展示图片，如果未配置则提供图片列表中的第一张
        ProductImage prdImg = productImgList.stream()
                .filter(productImg -> 
                        ObjUtil.isNotNull(productImg.getIsDisplay()) && 
                        productImg.getIsDisplay().equals(YesNoEnum.YES.getCode()))
                .findFirst()
                .orElse(productImgList.get(0));
        if (ObjUtil.isNotNull(prdImg) && StrUtil.isNotBlank(prdImg.getImageUrl())) {
            return prdImg.getImageUrl();
        }
        throw new ServiceException("商品图片数据异常");
    }

    private List<ProductListVO> buildProductListVOList(List<Product> productList){
        //  获取商品id与展示图片链接的map
        Map<String, String> prdIdShowImgUrlMap = getPrdIdImgUrlMap(productList);
        // 封装商品列表响应数据
        return productList.stream().map(prd -> buildProductListVO(prd, prdIdShowImgUrlMap))
                .collect(Collectors.toList());
    }
    
    private Map<String,String> getPrdIdImgUrlMap(List<Product> productList) {
        Set<String> productIdSet = productList.stream().map(Product::getId).collect(Collectors.toSet());
        List<ProductImage> productImageList = productImageManager.findProductImagesByProductIds(new ArrayList<>(productIdSet));
        return productImageList.stream()
                .filter(prdImg -> YesNoEnum.NO.getCode().equals(prdImg.getIsDeleted())
                        && StrUtil.isNotBlank(prdImg.getImageUrl()))
                .collect(Collectors.toMap(ProductImage::getProductId, ProductImage::getImageUrl,
                        (existing, replacement) -> existing));  //处理重复的展示图片
    }
    
    private ProductListVO buildProductListVO(Product prd, Map<String, String> prdIdShowImgUrlMap){
        ProductListVO productListVO = new ProductListVO();
        productListVO.setProductId(prd.getId());
        productListVO.setTitle(prd.getTitle());
        productListVO.setPrice(prd.getPrice());
        //todo 后续可改为默认图片链接
        String showImgUrl = StrUtil.emptyIfNull(prdIdShowImgUrlMap.get(prd.getId()));
        productListVO.setImageUrl(showImgUrl);
        return productListVO;
    }
    
    @Override
    public void updateProductStatus(ProductStatusReq productStatusReq) {
        Product product = new Product();
        product.setStatus(productStatusReq.getStatus());
        product.setId(productStatusReq.getProductId());
        product.setUpdatedTime(new Date());
        product.setUpdatedUser(SecurityUtils.getUsername());
        if (!productManager.updateById(product)) {
            throw new ServiceException("商品状态修改失败");
        }
    }

    private void fullUpdateDbProductDetail(ProductEditReq productEditReq) {
        // 全量逻辑删除对应商品图片数据
        String prdId = productEditReq.getProductId();
        productImageManager.removeByPrdIds(CollUtil.newArrayList(prdId));
        // 校验并获取请求图片列表
        List<ProductImgDTO> productImgDTOList = validAndGetImgList(productEditReq);
        // 组装入库商品图片列表
        List<ProductImage> productImageList = productImgDTOList.stream()
                .map(imgDto -> assembleDbProductImage(imgDto, prdId))
                .collect(Collectors.toList());
        // 插入商品图片数据
        if (!productImageManager.saveBatch(productImageList)) {
            throw new ServiceException("商品信息修改失败，图片数据修改失败");
        }
    }

    private void updateDbProduct(ProductEditReq productEditReq) {
        Product product = assembleDbProduct(productEditReq);
        if (!productManager.updateById(product)) {
            throw new ServiceException("商品信息修改失败");
        }
    }

    private static Product assembleDbProduct(ProductEditReq productEditReq) {
        Product product = new Product();
        BeanUtil.copyProperties(productEditReq, product);
        product.setId(productEditReq.getProductId());
        product.setUpdatedUser(SecurityUtils.getUsername());
        product.setUpdatedTime(new Date());
        return product;
    }

    private static ProductImage assembleDbProductImage(ProductImgDTO imgDto, String prdId) {
        ProductImage productImage = new ProductImage();
        productImage.setId(IdUtil.fastSimpleUUID());
        productImage.setProductId(prdId);
        productImage.setCreatedUser(SecurityUtils.getUsername());
        productImage.setCreatedTime(new Date());
        productImage.setUpdatedTime(new Date());
        productImage.setUpdatedUser(SecurityUtils.getUsername());
        productImage.setImageUrl(imgDto.getImageUrl());
        productImage.setIsDisplay(imgDto.getIsDisplay());
        productImage.setDisplayOrder(imgDto.getDisplayOrder());
        return productImage;
    }
    
    private Customer validAndGetCustomer(String phone){
        Customer customer = customerManager.getByPhone(phone);
        if (ObjUtil.isNull(customer)) {
            throw new ServiceException("客户信息不存在");
        }
        if (ObjUtil.isNull(customer.getUserStatus()) || NumberUtil.equals(customer.getUserStatus(), CustomerStatusEnum.ERR_STATUS.getCode())) {
            throw new ServiceException("客户信息异常");
        }
        return customer;
    }

    private static List<String> getReqPrdIds(List<OrderProductDTO> orderProductDTOList) {
        return orderProductDTOList.stream()
                .map(OrderProductDTO::getProductId)
                .collect(Collectors.toList());
    }

    private static OrderCreateVO assembleOrderCreateVO(MerchantOrderCreateReq merchantOrderCreateReq, Map<String, Product> dbProductIdMap, String orderId, BigDecimal totalPrice, Date orderCreateTime) {
        // 计算各项商品总金额
        Map<String, BigDecimal> productTotalPriceMap = calculateProductPrice(merchantOrderCreateReq, dbProductIdMap);
        OrderCreateVO orderCreateVO = new OrderCreateVO();
        orderCreateVO.setOrderId(orderId);
        orderCreateVO.setTotalPrice(totalPrice);
        orderCreateVO.setOrderCreatedTime(new Date());
        orderCreateVO.setPrdDTOLIst(assembleProductListDTOList(merchantOrderCreateReq, dbProductIdMap, productTotalPriceMap));
        orderCreateVO.setReceiverName(merchantOrderCreateReq.getReceiverName());
        orderCreateVO.setReceiverAddress(merchantOrderCreateReq.getReceiverAddress());
        orderCreateVO.setReceiverPhone(merchantOrderCreateReq.getReceiverPhone());
        orderCreateVO.setOrderCreateTime(orderCreateTime);
        return orderCreateVO;
    }

    private static List<ProductListDTO> assembleProductListDTOList(MerchantOrderCreateReq merchantOrderCreateReq, Map<String, Product> dbProductIdMap, Map<String, BigDecimal> productTotalPriceMap) {
        return merchantOrderCreateReq.getOrderProductDTOList().stream().map(orderProductDTO -> {
            String productId = orderProductDTO.getProductId();
            Product dbProduct = dbProductIdMap.get(productId);
            ProductListDTO productListDTO = new ProductListDTO();
            productListDTO.setProductId(productId);
            productListDTO.setTitle(dbProduct.getTitle());
            productListDTO.setDescription(dbProduct.getDescription());
            productListDTO.setPrice(dbProduct.getPrice());
            productListDTO.setOnePrdTotalPrice(productTotalPriceMap.get(productId));
            productListDTO.setStock(dbProduct.getStock());
            productListDTO.setNum(orderProductDTO.getNum());
            // todo 暂时不给返回图片，之后考虑实现
            return productListDTO;
        }).collect(Collectors.toList());
    }

    /**
     * 计算各个商品总金额
     * @param merchantOrderCreateReq 订单创建请求对象
     * @param dbProductIdMap 商品id 商品对象map
     * @return key:商品Id value:该商品总金额
     */
    private static Map<String, BigDecimal> calculateProductPrice(MerchantOrderCreateReq merchantOrderCreateReq, Map<String, Product> dbProductIdMap) {
        return merchantOrderCreateReq.getOrderProductDTOList().stream()
                .collect(Collectors.toMap(OrderProductDTO::getProductId, dto -> {
                    Product dbProduct = dbProductIdMap.get(dto.getProductId());
                    return NumberUtil.mul(dto.getNum(), dbProduct.getPrice());
                }));
    }

    private BigDecimal calculateTotalPrice(MerchantOrderCreateReq merchantOrderCreateReq, Map<String, Product> dbProductIdMap, Customer customer) {
        BigDecimal originalTotalPrice = merchantOrderCreateReq.getOrderProductDTOList().stream()
                .map(orderProductDto -> NumberUtil.mul(orderProductDto.getNum(), dbProductIdMap.get(orderProductDto.getProductId()).getPrice()))
                .reduce(BigDecimal.ZERO, NumberUtil::add);
        PriceCalculatorStrategy priceCalculatorStrategy = priceStrategyFactory.getStrategyByCustomerType(customer.getCustomerType());
        return priceCalculatorStrategy.calculate(originalTotalPrice);
    }

    private Map<String, Product> validProductAndGetMap(MerchantOrderCreateReq merchantOrderCreateReq) {
        // 获取请求中商品列表 productId与product对象映射关系
        Map<String, OrderProductDTO> orderProductIdDtoMap = merchantOrderCreateReq.getOrderProductDTOList().stream()
                .collect(Collectors.toMap(OrderProductDTO::getProductId, Function.identity(), (v1, v2) -> v1));
        
        // 请求中的productId列表
        List<String> reqProductIdList = new ArrayList<>(orderProductIdDtoMap.keySet());
        
        // 根据productId列表查询商品数据，只查上架状态的
        List<Product> dbProductList = productManager.findProductByIds(reqProductIdList, CheckOnShelfEnum.ONLY_ON_SHELF);
        
        // 获取数据库中product表 id与实体的映射map
        Map<String, Product> dbIdProductMap = dbProductList.stream()
                .collect(Collectors.toMap(Product::getId, Function.identity(), (v1, v2) -> v1));
        
        // 获取不存在或未上架的商品title列表，直接返回
        if (CollUtil.size(orderProductIdDtoMap) != CollUtil.size(dbIdProductMap)) {
            List<String> invalidTitles = orderProductIdDtoMap.entrySet().stream()
                    .filter(entry -> !dbIdProductMap.containsKey(entry.getKey()))
                    .map(entry -> entry.getValue().getTitle()).collect(Collectors.toList());
            throw new ServiceException("以下商品不存在或未上架", invalidTitles);
        }

        return dbIdProductMap;
    }

    public <V> V doInLock(String orgId, Supplier<V> runnable) {
        String key = getKey(orgId);
        RLock lock = redissonClient.getLock(StrUtil.format("{}:{}", "MERCHANT:ORDER_CREATE_LOCK", key));
        lock.lock();
        try {
            return runnable.get();
        } finally {
            lock.unlock();
        }
    }

    private String getKey(String orgId) {
        return StrUtil.format("{}_{}", orgId, LocalDate.now().getYear());
    }
}
