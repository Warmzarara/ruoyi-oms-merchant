package com.ruoyi.merchant.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ProductImgDTO {
    /** 图片链接 */
    @NotBlank(message = "商品图片url不能为空")
    private String imageUrl;
    
    /** 是否为列表展示图片 */
    private Integer isDisplay;
    
    /** 图片展示顺序 */
    @NotNull(message = "图片顺序不能为空")
    private Integer displayOrder;
}
