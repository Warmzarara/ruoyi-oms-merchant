package com.ruoyi.merchant.domain.req;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ProductStatusReq {
    @NotBlank(message = "商品Id不能为空")
    private String productId;
    
    @NotNull(message = "商品状态不能为空")
    @Range(max = 1, min = 0, message = "商品状态错误")
    private Integer status;
}
