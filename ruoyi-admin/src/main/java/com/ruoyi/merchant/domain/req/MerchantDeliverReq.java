package com.ruoyi.merchant.domain.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class MerchantDeliverReq {
    @NotBlank(message = "订单号不能为空")
    private String orderId;
    @NotBlank(message = "快递单号不能为空")
    private String logisticsNo;
    @NotBlank(message = "物流公司名称不能为空")
    private String logisticsCompany;
}
