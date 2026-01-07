package com.ruoyi.merchant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderStatusEnum {
    ORDER_CREATED(1,"订单创建"),
    ORDER_CANCEL(0,"订单取消");
    private final Integer code;
    private final String desc;
}
