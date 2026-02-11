package com.ruoyi.merchant.domain.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class MerchantPayRequest {
    @NotBlank(message = "订单号不能为空")
    private String orderId;
}
