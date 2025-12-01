package com.ruoyi.merchant.domain.req;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class MerchantOrderCreateReq {
    @NotBlank(message = "商品id不能为空")
    private String productId;

    @NotBlank(message = "下单件量不能为空")
    @Min(value = 0, message = "下单件数不能小于0")
    private String num;
    
    @NotBlank(message = "客户不能为空")
    private String userName;
    
    @NotBlank(message = "客户id不能为空")
    private String userId;
    
    @NotBlank(message = "收件人电话不能为空")
    private String receiverPhone;
    
    @NotBlank(message = "收件人名称不能为空")
    private String receiverName;
    
    @NotBlank(message = "收件人地址不能为空")
    private String receiverAddress;
}
