package com.ruoyi.merchant.manager.impl;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.github.yulichang.base.MPJBaseServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.ruoyi.common.enums.YesNoEnum;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.merchant.domain.ProductImage;
import com.ruoyi.merchant.enums.ProductStatusEnum;
import com.ruoyi.merchant.manager.ProductImageManager;
import com.ruoyi.merchant.mapper.ProductMapper;
import com.ruoyi.merchant.domain.Product;
import com.ruoyi.merchant.manager.ProductManager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class ProductManagerImpl extends MPJBaseServiceImpl<ProductMapper, Product> implements ProductManager {
    
    @Resource
    private ProductImageManager productImageManager;
    
    @Override
    public List<Product> findProductList(Product product) {
        MPJLambdaWrapper<Product> wrapper = new MPJLambdaWrapper<>();
        wrapper.like(StrUtil.isNotBlank(product.getTitle()), Product::getTitle, product.getTitle());
        wrapper.eq(ObjUtil.isNotEmpty(product.getStatus()), Product::getStatus, product.getStatus());
        wrapper.eq(Product::getIsDeleted, YesNoEnum.NO.getCode());
        return this.selectJoinList(Product.class, wrapper);
    }

    @Override
    public Product findProductOne(Product product) {
        MPJLambdaWrapper<Product> wrapper = new MPJLambdaWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(product.getId()), Product::getId, product.getId());
        wrapper.eq(Product::getIsDeleted, YesNoEnum.NO.getCode());
        return this.selectJoinOne(Product.class, wrapper);
    }

    @Override
    public List<ProductImage> findProductImgList(ProductImage productImage) {
        MPJLambdaWrapper<ProductImage> wrapper = new MPJLambdaWrapper<>();
        wrapper.eq(ProductImage::getIsDeleted, YesNoEnum.NO.getCode());
        wrapper.eq(StrUtil.isNotBlank(productImage.getProductId()), ProductImage::getProductId, productImage.getProductId());
        return productImageManager.selectJoinList(ProductImage.class, wrapper);
    }

    @Override
    public List<ProductImage> findProductImgListByPrdId(String productId) {
        MPJLambdaWrapper<ProductImage> wrapper = new MPJLambdaWrapper<>();
        wrapper.eq(ProductImage::getIsDeleted, YesNoEnum.NO.getCode());
        wrapper.eq(StrUtil.isNotBlank(productId), ProductImage::getProductId, productId);
        return productImageManager.selectJoinList(ProductImage.class, wrapper);
    }

    @Override
    public Product findProductById(String productId) {
        MPJLambdaWrapper<Product> wrapper = new MPJLambdaWrapper<>();
        wrapper.eq(StrUtil.isNotBlank(productId), Product::getId, productId);
        wrapper.eq(Product::getIsDeleted, YesNoEnum.NO.getCode());
        return this.selectJoinOne(Product.class, wrapper);
    }

    @Override
    public List<Product> findProductByIds(List<String> productIds, YesNoEnum isCheckOnShelf) {
        MPJLambdaWrapper<Product> wrapper = new MPJLambdaWrapper<>();
        wrapper.eq(Product::getIsDeleted, YesNoEnum.NO.getCode());
        if (isCheckOnShelf == YesNoEnum.YES) {
            wrapper.eq(Product::getStatus, ProductStatusEnum.ON_SHELF.getCode());
        }
        wrapper.in(Product::getId, productIds);
        return this.selectJoinList(Product.class, wrapper);
    }

    @Override
    public Boolean decreasePrdStock(String productId, Integer decreaseNum) {
        LambdaUpdateWrapper<Product> wrapper = new LambdaUpdateWrapper<>();
        wrapper.setSql("stock = stock - " + decreaseNum);
        wrapper.set(Product::getUpdatedTime, new Date());
        wrapper.set(Product::getUpdatedUser, SecurityUtils.getUsername());
        wrapper.eq(Product::getId, productId);
        wrapper.gt(Product::getStock, decreaseNum);
        return this.update(null, wrapper);
    }

//    @Override
//    public List<ProductImage> getShowImageListByPrdId(String prdId) {
//        
//        return Collections.emptyList();
//    }


}


