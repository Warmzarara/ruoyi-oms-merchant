package com.ruoyi.merchant.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.yulichang.base.MPJBaseServiceImpl;
import com.github.yulichang.query.MPJLambdaQueryWrapper;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.ruoyi.common.enums.YesNoEnum;
import com.ruoyi.merchant.mapper.ProductImageMapper;
import com.ruoyi.merchant.domain.ProductImage;
import com.ruoyi.merchant.manager.ProductImageManager;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class ProductImageManagerImpl extends MPJBaseServiceImpl<ProductImageMapper, ProductImage> implements ProductImageManager {

    @Override
    public List<ProductImage> findProductImagesByProductIds(List<String> productIds) {
        MPJLambdaWrapper<ProductImage> productImageMPJLambdaWrapper = new MPJLambdaWrapper<>();
        productImageMPJLambdaWrapper
                .eq(ProductImage::getIsDeleted, YesNoEnum.NO.getCode())
                .in(ProductImage::getProductId, productIds);
        return this.list(productImageMPJLambdaWrapper);
    }

    @Override
    public List<ProductImage> getShowImageListByPrdId(String prdId) {
        MPJLambdaWrapper<ProductImage> productImageLambdaQueryWrapper = new MPJLambdaWrapper<>();
        productImageLambdaQueryWrapper
                .selectAll()
                .eq(ProductImage::getIsDeleted, YesNoEnum.NO.getCode())
                .eq(ProductImage::getIsDisplay, YesNoEnum.YES.getCode());
        return this.list(productImageLambdaQueryWrapper);
    }


}
