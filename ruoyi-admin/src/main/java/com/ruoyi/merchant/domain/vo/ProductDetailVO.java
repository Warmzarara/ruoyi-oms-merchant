package com.ruoyi.merchant.domain.vo;

import com.ruoyi.merchant.domain.ProductImage;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductDetailVO {
    /** 唯一键 */
    private String productId;
    /** 商品标题 */
    private String title;
    /** 商品描述 */
    private String description;
    /** 单价 */
    private BigDecimal price;
    /** 库存数量 */
    private Integer stock;
    /** 图片列表 */
    private List<ProductImage> productImageList;
    /** 展示图片url */
    private String showImgUrl;
}
