package com.ruoyi.merchant.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.ruoyi.common.enums.YesNoEnum;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.merchant.domain.Product;
import com.ruoyi.merchant.domain.ProductImage;
import com.ruoyi.merchant.domain.req.ProductListReq;
import com.ruoyi.merchant.domain.vo.ProductDetailVO;
import com.ruoyi.merchant.domain.vo.ProductListVO;
import com.ruoyi.merchant.manager.ProductImageManager;
import com.ruoyi.merchant.manager.ProductManager;
import com.ruoyi.merchant.service.MerchantService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MerchantServiceImpl implements MerchantService {
    
    @Resource
    ProductManager productManager;
    
    @Resource
    ProductImageManager productImageManager;

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
    public void addMerchant() {

    }

    @Override
    public void updateMerhcantStatus() {

    }
}
