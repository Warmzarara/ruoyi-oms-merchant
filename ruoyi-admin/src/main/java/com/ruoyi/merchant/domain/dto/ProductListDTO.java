package com.ruoyi.merchant.domain.dto;

import com.ruoyi.merchant.domain.ProductImage;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
@Data
public class ProductListDTO {
    /** 唯一键 */
    private String productId;
    /** 商品标题 */
    private String title;
    /** 商品描述 */
    private String description;
    /** 单价 */
    private BigDecimal price;
    /** 相同商品总价 */
    private BigDecimal onePrdTotalPrice;
    /** 库存数量 */
    private Integer stock;
    /** 下单数量 */
    private Integer num;
    /** 状态 0=已下架 1=上架 */
    private Integer status;
    /* 商品图片列表 */
    private List<ProductImage> productImages;
    /* 展示图片链接 */
    private String showImageUrl;
    /* 缩略图链接 */
    private String thumbnail;
}
