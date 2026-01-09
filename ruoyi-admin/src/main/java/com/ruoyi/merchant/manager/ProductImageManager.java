package com.ruoyi.merchant.manager;

import com.github.yulichang.base.MPJBaseService;
import com.ruoyi.merchant.domain.ProductImage;

import java.util.List;

public interface ProductImageManager extends MPJBaseService<ProductImage> {
    List<ProductImage> findProductImagesByProductIds(List<String> productIds);
    List<ProductImage> getShowImageListByPrdId(String prdId);
//    List<String> findShowImgUrlListByProductIds(List<String> productIds);

    /**
     * 批量根据productId删除商品图片数据
     * @param prdIds 商品id集合
     * @return 删除结果
     */
    boolean removeByPrdIds(List<String> prdIds);
}
