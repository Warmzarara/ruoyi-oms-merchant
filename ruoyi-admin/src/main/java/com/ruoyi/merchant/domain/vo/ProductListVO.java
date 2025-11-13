package com.ruoyi.merchant.domain.vo;

import com.ruoyi.common.model.BasePage;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductListVO extends BasePage {
    /** 商品id */
    String productId;
    /** 商品标题 */
    private String title;
    /** 单价 */
    private BigDecimal price;
    /** 展示图片 */
    private String imageUrl;
}
