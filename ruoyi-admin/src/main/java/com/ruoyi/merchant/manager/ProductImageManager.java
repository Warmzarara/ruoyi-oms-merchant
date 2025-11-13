package com.ruoyi.merchant.manager;

import com.github.yulichang.base.MPJBaseService;
import com.ruoyi.merchant.domain.ProductImage;

import java.util.List;

public interface ProductImageManager extends MPJBaseService<ProductImage> {
    List<ProductImage> findProductImagesByProductIds(List<String> productIds);
//    List<String> findShowImgUrlListByProductIds(List<String> productIds);
}
