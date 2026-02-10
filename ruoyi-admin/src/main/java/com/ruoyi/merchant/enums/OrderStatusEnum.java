package com.ruoyi.merchant.enums;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderStatusEnum {
    ORDER_CANCEL(0,"订单取消"),
    ORDER_CREATED(1,"订单创建"),
    ORDER_NOT_PAY(2, "订单待支付"),
    ORDER_PAY(3, "订单已支付"),
    ORDER_DELIVERY(4, "订单已发货");
    private final Integer code;
    private final String desc;

    public static String getDescByCode(Integer code) {
        for (OrderStatusEnum value : OrderStatusEnum.values()) {
            if (NumberUtil.equals(value.getCode(), code)) {
                return value.getDesc();
            }
        }
        return "未知订单状态";
    }
}
