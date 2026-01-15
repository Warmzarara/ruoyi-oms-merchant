package com.ruoyi.merchant.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class OrderProductDTO {
    /** 商品名称 */
    private String title;
    
    /** 商品id */
    @NotBlank(message = "商品id不能为空")
    private String productId;
    
    /** 下单数量 */
    @NotNull(message = "下单数量不能为空")
    private Integer num;
    
    /** 页面商品单价 */
    private BigDecimal price;
    
    /** 实际单价 */
    private BigDecimal realPrice;
}
