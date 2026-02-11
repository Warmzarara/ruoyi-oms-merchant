package com.ruoyi.merchant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.Arrays;


@Getter
@AllArgsConstructor
@ToString

public enum ShippingStatus {

    // 枚举常量定义
    TO_BE_SHIPPED(0, "待发货"),
    SHIPPED(1, "已发货"),
    SIGNED(2, "已签收");

    // Getter 方法
    // 成员变量
    private final Integer code;
    private final String description;

    /**
     * 根据 code 查找对应的枚举对象 (常用于数据库值映射)
     * @param code 状态码
     * @return 对应的枚举，如果找不到则返回 null
     */
    public static ShippingStatus getByCode(Integer code) {
        return Arrays.stream(ShippingStatus.values())
                .filter(status -> status.getCode().equals(code))
                .findFirst()
                .orElse(null);
    }

}