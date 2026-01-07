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
import com.ruoyi.merchant.enums.CustomerStatusEnum;
import com.ruoyi.merchant.enums.OrderStatusEnum;
import com.ruoyi.merchant.manager.OrderManager;
import com.ruoyi.merchant.manager.ProductImageManager;
import com.ruoyi.merchant.manager.ProductManager;
import com.ruoyi.merchant.manager.CustomerManager;
import com.ruoyi.merchant.service.MerchantService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.merchant.manager.OrderProdcutManager;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
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
    OrderProdcutManager orderProductManager;

    /**
     * 查询商品列表
     */
    @Override
    public List<ProductListVO> findProductList(ProductListReq req) {
        Product product = new Product();
        BeanUtil.copyProperties(req, product);
        // 通过分页获取商品数据
        List<Product> productList = productManager.findProductList(product);
        if (CollUtil.isEmpty(productList)) {
            return new ArrayList<>();
        }
        //  封装商品列表响应数据
        return buildProductListVOList(productList);
    }
    
    /**
     * 查询商品详情
     * @param productId
     * @return
     */
    @Override
    public ProductDetailVO findProductDetail(String productId) {
        // 得到扁平的单个商品详情数据
        Product product = productManager.findProductById(productId);
        if (ObjUtil.isNull(product)) {
            throw new ServiceException("该商品不存在");
        }
        List<ProductImage> productImgList = productManager.findProductImgListByPrdId(productId);
        if (CollUtil.isEmpty(productImgList)) {
            throw new ServiceException("该商品不存在图片信息");
        }
        // 获取首页展示图片
        String displayImg = getDisplayImg(productImgList);
        // 开始封装响应对象
        ProductDetailVO productDetailVO = new ProductDetailVO();
        BeanUtil.copyProperties(product, productDetailVO);
        productDetailVO.setProductId(productId);
        productDetailVO.setShowImgUrl(displayImg);
        productDetailVO.setProductImageList(productImgList);
        return productDetailVO;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addProduct(ProductAddReq productAddReq){
        // 校验并获取图片列表数据
        List<ProductImgDTO> prdImgDTOList = validAndGetImgList(productAddReq);
        Product product = new Product();
        BeanUtil.copyProperties(productAddReq, product);
        String savePrdId = IdUtil.fastSimpleUUID();
        product.setId(savePrdId);
        product.setCreatedUser(SecurityUtils.getUsername());
        product.setCreatedTime(new Date());
        if (!productManager.save(product)) {
            throw new ServiceException("商品数据新增失败");
        }
        // 处理图片数据
        Integer yesCode = YesNoEnum.YES.getCode();
        Integer noCode = YesNoEnum.NO.getCode();
        boolean hasDisplayImg = prdImgDTOList.stream().anyMatch(img -> ObjUtil.equals(img.getIsDisplay(), yesCode));
        if (!hasDisplayImg) {
            prdImgDTOList.get(0).setIsDisplay(yesCode);
        }

        List<ProductImage> savePrdImgList = prdImgDTOList.stream().map(img -> {
            ProductImage savePrdImg = new ProductImage();
            savePrdImg.setId(IdUtil.fastSimpleUUID());
            savePrdImg.setProductId(savePrdId);
            savePrdImg.setImageUrl(img.getImageUrl());
            savePrdImg.setDisplayOrder(img.getDisplayOrder());
            savePrdImg.setIsDisplay(img.getIsDisplay());
            savePrdImg.setIsDisplay(ObjUtil.defaultIfNull(img.getIsDisplay(), noCode));
            savePrdImg.setCreatedTime(new Date());
            savePrdImg.setCreatedUser(SecurityUtils.getUsername());
            return savePrdImg;
        }).collect(Collectors.toList());
        
        if (!productImageManager.saveBatch(savePrdImgList)) {
            throw new ServiceException("商品图片数据更新失败，商品新增失败");
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

    @Transactional
    @Override
    public void updateProductDetail(ProductEditReq productEditReq) {
        String prdId = productEditReq.getProductId();
        Product product = new Product();
        BeanUtil.copyProperties(productEditReq, product);
        product.setId(prdId);
        product.setUpdatedUser(SecurityUtils.getUsername());
        product.setUpdatedTime(new Date());
        if (!productManager.updateById(product)) {
            throw new ServiceException("商品信息修改失败");
        }
        List<ProductImgDTO> productImgDTOList = validAndGetImgList(productEditReq);
        List<ProductImage> prodductImageList = productImgDTOList.stream().map(imgDto -> {
            ProductImage productImage = new ProductImage();
            BeanUtil.copyProperties(imgDto, productImage);
            productImage.setProductId(prdId);
            productImage.setUpdatedTime(new Date());
            productImage.setUpdatedUser(SecurityUtils.getUsername());
            return productImage;
        }).collect(Collectors.toList());
        if (!productImageManager.updateBatchById(prodductImageList)) {
            throw new ServiceException("商品信息修改失败，图片数据修改失败");
        }
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public OrderCreateVO merchantOrderCreate(MerchantOrderCreateReq merchantOrderCreateReq) {
        // 校验用户状态是否正常
        Customer customer = customerManager.getByPhone(merchantOrderCreateReq.getReceiverPhone());
        if (ObjUtil.isNull(customer)) {
            throw new ServiceException("客户信息不存在");
        }
        Integer userStatus = customer.getUserStatus();
        if (ObjUtil.isNull(userStatus) || ObjUtil.equals(userStatus, CustomerStatusEnum.ERR_STATUS)) {
            throw new ServiceException("客户信息异常");
        }
        // 获取用户id
        String customerId = customer.getId();
        List<OrderProductDTO> orderProductDTOList = merchantOrderCreateReq.getOrderProductDTOList();
        if (CollUtil.isEmpty(orderProductDTOList)) {
            throw new ServiceException("请添加商品后再下单");
        }
        // 判断下单商品是否存在
        List<String> prdIdList = orderProductDTOList.stream()
                .map(OrderProductDTO::getProductId)
                .collect(Collectors.toList());
        List<Product> dbPrdList = productManager.findProductByIds(prdIdList, YesNoEnum.NO);
        Set<String> dbPrdIdSet = dbPrdList.stream().map(Product::getId).collect(Collectors.toSet());
        List<OrderProductDTO> notExistPrdList = orderProductDTOList.stream()
                .filter(orderProductDTO -> !dbPrdIdSet.contains(orderProductDTO.getProductId()))
                .collect(Collectors.toList());
        if (CollUtil.isNotEmpty(notExistPrdList)) {
            log.info("以下商品不存在：{}", notExistPrdList);
            throw new ServiceException("以下商品不存在", 3001, notExistPrdList);
        }
        // 判断下单商品是否上架
        Set<String> dbOnShelfPrdList = dbPrdList.stream()
                .filter(dbPrd -> ObjUtil.equals(dbPrd.getStatus(), YesNoEnum.YES.getCode()))
                .map(Product::getId).collect(Collectors.toSet());
        List<OrderProductDTO> notOnShelfPrdList = orderProductDTOList.stream()
                .filter(orderProductDTO -> !dbOnShelfPrdList.contains(orderProductDTO.getProductId()))
                .collect(Collectors.toList());
        if (CollUtil.isNotEmpty(notOnShelfPrdList)) {
            throw new ServiceException("以下商品未上架", 3002, notOnShelfPrdList);
        }
        // 更新商品库存并判断库存是否充足
        List<OrderProductDTO> decreaseFailPrdList = orderProductDTOList.stream()
                .filter(orderPrdDTO -> !productManager.decreasePrdStock(orderPrdDTO.getProductId(), orderPrdDTO.getNum()))
                .collect(Collectors.toList());
        if (CollUtil.isNotEmpty(decreaseFailPrdList)) {
            throw new ServiceException("一下商品库存不足", 3003, decreaseFailPrdList);
        }
        // 开始更新订单主表
        // 封装订单信息
        Order order = new Order();
        // 统一订单创建时间
        Date orderCreatedTime = new Date();
        // 订单表主键
        String orderId = IdUtil.fastSimpleUUID();
        // 总件数
        Integer totalQuantity = orderProductDTOList.stream().mapToInt(OrderProductDTO::getNum).sum();
        order.setTotalQuantity(totalQuantity);
        // 计算订单总金额
        Map<String, Product> dbPrdMap = dbPrdList.stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));
        BigDecimal orderTotalPrice = new BigDecimal(0);
        orderProductDTOList.forEach(orderPrdDTO -> {
            Integer num = orderPrdDTO.getNum();
            String productId = orderPrdDTO.getProductId();
            Product product = dbPrdMap.get(productId);
            BigDecimal price = product.getPrice();
            BigDecimal onePrdTotal = NumberUtil.mul(num, price);
            NumberUtil.add(orderTotalPrice, onePrdTotal);
        });
        order.setTotalPrice(orderTotalPrice);
        order.setId(orderId);
        order.setStatus(OrderStatusEnum.ORDER_CREATED.getCode());
        order.setRemark(merchantOrderCreateReq.getRemark());
        order.setCreatedTime(orderCreatedTime);
        order.setCreatedUser(SecurityUtils.getUsername());
        order.setCustomerId(customerId);
//        order.setReceiverPhone();
//        order.setReceiverName();
//        order.setReceiverAddress();
        if (!orderManager.save(order)) {
            throw new ServiceException("订单信息更新失败");
        }
        // 开始更新订单详情
        List<OrderProduct> orderProductList = orderProductDTOList.stream().map(dto -> {
            String productId = dto.getProductId();
            OrderProduct orderProduct = new OrderProduct();
            orderProduct.setId(IdUtil.fastSimpleUUID());
            orderProduct.setOrderId(orderId);
            orderProduct.setProductId(productId);
            Product dbPrd = dbPrdMap.get(productId);
            orderProduct.setProductNameSnapshot(dbPrd.getTitle());
            orderProduct.setProductQuantity(dto.getNum());
            orderProduct.setProductPriceSnapshot(dbPrd.getPrice());
            orderProduct.setCreatedTime(new Date());
            orderProduct.setCreatedUser(SecurityUtils.getUsername());
            return orderProduct;
        }).collect(Collectors.toList());
        if (!orderProductManager.saveBatch(orderProductList)) {
            throw new ServiceException("更新订单详情数据失败");
        }
        //开始封装订单创建 响应数据
        OrderCreateVO orderCreateVO = new OrderCreateVO();
        orderCreateVO.setOrderCreatedTime(orderCreatedTime);
        orderCreateVO.setOrderId(orderId);
        orderCreateVO.setReceiverName(merchantOrderCreateReq.getReceiverName());
        orderCreateVO.setReceiverPhone(merchantOrderCreateReq.getReceiverPhone());
        orderCreateVO.setReceiverAddress(merchantOrderCreateReq.getReceiverAddress());
        orderCreateVO.setTotalPrice(orderTotalPrice);

        List<ProductListDTO> productListDTOs = orderProductDTOList.stream().map(dto -> {
            ProductListDTO productListDTO = new ProductListDTO();
            String productId = dto.getProductId();
            Product dbPrd = dbPrdMap.get(productId);
            productListDTO.setProductId(productId);
            productListDTO.setTitle(dbPrd.getTitle());
            productListDTO.setDescription(dbPrd.getDescription());
            productListDTO.setStock(dbPrd.getStock());
            BigDecimal price = dbPrd.getPrice();
            productListDTO.setPrice(price);
            Integer num = dto.getNum();
            productListDTO.setNum(num);
            productListDTO.setOnePrdTotalPrice(NumberUtil.mul(num, price));
            productListDTO.setThumbnail(dbPrd.getThumbnail());
//            productListDTO.setShowImageUrl();
            return productListDTO;
        }).collect(Collectors.toList());
        orderCreateVO.setPrdDTOLIst(productListDTOs);
        return orderCreateVO;
        // todo 暂时没做支付时间，支付方式，客户类型，先空着，后面再加

    }
}
