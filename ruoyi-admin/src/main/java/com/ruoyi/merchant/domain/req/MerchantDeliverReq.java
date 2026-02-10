package com.ruoyi.merchant.domain.req;

import lombok.Data;

@Data
public class MerchantDeliverReq {
    private String orderId;
    private String logisticsNo;
    private String logisticsCompany;
}
