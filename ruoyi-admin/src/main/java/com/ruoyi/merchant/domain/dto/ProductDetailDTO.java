package com.ruoyi.merchant.domain.dto;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class ProductDetailDTO {
    private String productId;
    private String title;
    private String description;
    private BigDecimal price;
    private Integer stock;
    private Short status;
    private String imageUrl;
    private Short isDisplay;
    private Integer displayOrder;
}
