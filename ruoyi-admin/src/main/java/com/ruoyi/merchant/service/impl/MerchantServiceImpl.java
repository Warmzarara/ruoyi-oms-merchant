package com.ruoyi.merchant.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
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
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MerchantServiceImpl implements MerchantService {
    
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
    @Transactional(rollbackFor = Exception.class)
    @Override
    public OrderCreateVO merchantOrderCreate(MerchantOrderCreateReq merchantOrderCreateReq) {
        // 下单操作开始时间
        Date orderCreateTime = new Date();

        // 获取客户信息并校验
        Customer customer = validAndGetCustomer(merchantOrderCreateReq.getReceiverPhone());

        // 获取商品信息并校验（存在性，是否上架，获取商品Map）
        Map<String, Product> dbProductIdMap = validProductAndGetMap(merchantOrderCreateReq);

        // 检查库存是否充足
        checkProductStock(merchantOrderCreateReq, dbProductIdMap);

        // 批量扣减商品库存
        batchDecreaseStock(merchantOrderCreateReq);

        // 计算订单总金额
        BigDecimal totalPrice = calculateTotalPrice(merchantOrderCreateReq, dbProductIdMap, customer);

        // 插入订单主表
        String orderId = saveOrder(merchantOrderCreateReq, customer, totalPrice, orderCreateTime);

        // 插入订单详情表
        saveOrderProduct(merchantOrderCreateReq, dbProductIdMap, orderId, orderCreateTime);

        // 组装响应数据并返回
        return assembleOrderCreateVO(merchantOrderCreateReq, dbProductIdMap, orderId, totalPrice, orderCreateTime);
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

    private void saveOrderProduct(MerchantOrderCreateReq merchantOrderCreateReq, Map<String, Product> dbProductIdMap, String orderId, Date orderCreateTime) {
        // 组装订单列表
        List<OrderProduct> orderProductList = merchantOrderCreateReq.getOrderProductDTOList().stream()
                .map(orderProductDTO -> 
                        assembleOrderProduct(orderProductDTO, dbProductIdMap, orderId, orderCreateTime))
                .collect(Collectors.toList());
        // 插入订单详情信息
        if (!orderProductManager.saveBatch(orderProductList)) {
            throw new ServiceException("下单失败，商品详情插入失败");
        }
    }

    private static OrderProduct assembleOrderProduct(OrderProductDTO orderProductDTO, Map<String, Product> dbProductIdMap, String orderId, Date orderCreateTime) {
        Product dbProduct = dbProductIdMap.get(orderProductDTO.getProductId());
        OrderProduct orderProduct = new OrderProduct();
        orderProduct.setId(IdUtil.fastSimpleUUID());
        orderProduct.setProductId(orderProductDTO.getProductId());
        orderProduct.setProductNameSnapshot(dbProduct.getTitle());
        orderProduct.setOrderId(orderId);
        orderProduct.setProductQuantity(orderProductDTO.getNum());
        orderProduct.setProductPriceSnapshot(dbProduct.getPrice());
        orderProduct.setCreatedUser(SecurityUtils.getUsername());
        orderProduct.setCreatedTime(orderCreateTime);
        return orderProduct;
    }

    private String saveOrder(MerchantOrderCreateReq merchantOrderCreateReq, Customer customer, BigDecimal totalPrice, Date orderCreateTime) {
        // 组装订单数据
        Order order = assembleOrder(merchantOrderCreateReq, customer, totalPrice, orderCreateTime);
        
        // 更新订单
        if (!orderManager.save(order)) {
            throw new ServiceException("下单失败，订单数据插入失败");
        }
        return order.getId();
    }

    private Order assembleOrder(MerchantOrderCreateReq merchantOrderCreateReq, Customer customer, BigDecimal totalPrice, Date orderCreateTime) {
        Order order = new Order();
        order.setId(IdUtil.fastSimpleUUID());
        order.setCustomerId(customer.getId());
        order.setStatus(OrderStatusEnum.ORDER_CREATED.getCode());
        order.setTotalPrice(totalPrice);
        order.setTotalQuantity(getTotalNum(merchantOrderCreateReq));
        order.setRemark(merchantOrderCreateReq.getRemark());
        order.setCreatedUser(SecurityUtils.getUsername());
        order.setCreatedTime(orderCreateTime);
        return order;
    }

    private static int getTotalNum(MerchantOrderCreateReq merchantOrderCreateReq) {
        return merchantOrderCreateReq.getOrderProductDTOList().stream().mapToInt(OrderProductDTO::getNum).sum();
    }

    private BigDecimal calculateTotalPrice(MerchantOrderCreateReq merchantOrderCreateReq, Map<String, Product> dbProductIdMap, Customer customer) {
        BigDecimal originalTotalPrice = merchantOrderCreateReq.getOrderProductDTOList().stream()
                .map(orderProductDto -> NumberUtil.mul(orderProductDto.getNum(), dbProductIdMap.get(orderProductDto.getProductId()).getPrice()))
                .reduce(BigDecimal.ZERO, NumberUtil::add);
        PriceCalculatorStrategy priceCalculatorStrategy = priceStrategyFactory.getStrategyByCustomerType(customer.getCustomerType());
        return priceCalculatorStrategy.calculate(originalTotalPrice);
    }

    private void batchDecreaseStock(MerchantOrderCreateReq merchantOrderCreateReq) {
        List<OrderProductDTO> orderProductDTOList = merchantOrderCreateReq.getOrderProductDTOList();
        for (OrderProductDTO orderProductDTO : orderProductDTOList) {
            if (!productManager.decreaseProductStock(orderProductDTO.getProductId(), orderProductDTO.getNum())) {
                throw new ServiceException("下单失败，下单期间库存有变动，请重试");
            }
        }
    }

    private static void checkProductStock(MerchantOrderCreateReq merchantOrderCreateReq, Map<String, Product> dbProductIdMap) {
        List<OrderProductDTO> insufficientProductList = merchantOrderCreateReq.getOrderProductDTOList()
                .stream().filter(dto -> 
                        NumberUtil.compare(dto.getNum(), dbProductIdMap.get(dto.getProductId()).getStock()) > 0)
                .collect(Collectors.toList());
        if (CollUtil.isNotEmpty(insufficientProductList)) {
            throw new ServiceException("商品库存不足", insufficientProductList);
        }
    }

    private Map<String, Product> validProductAndGetMap(MerchantOrderCreateReq merchantOrderCreateReq) {
        List<OrderProductDTO> orderProductDTOList = merchantOrderCreateReq.getOrderProductDTOList();
        List<String> reqProductIdList = CollectUtil.toList(orderProductDTOList, OrderProductDTO::getProductId);
        Set<String> reqProductIdSet = new HashSet<>(reqProductIdList);
        List<Product> dbProductList = productManager.findProductByIds(reqProductIdList, CheckOnShelfEnum.ONLY_ON_SHELF);
        Set<String> dbProductIdSet = CollectUtil.toSet(dbProductList, Product::getId);
        if (! CollUtil.containsAll(dbProductIdSet, reqProductIdSet)) {
            Collection<String> invalidProductIdList = CollUtil.subtract(reqProductIdSet, dbProductIdSet);
            throw new ServiceException("下单的商品不存在", invalidProductIdList);
        }
        return dbProductList.stream().collect(Collectors.toMap(Product::getId, Function.identity()));
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
