package com.ruoyi.merchant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CustomerTypeEnum {
    RETAIL_CUSTOMER(0,"散户"),
    COOPERATIVE_CUSTOMER(1,"合作客户");
    private final Integer code;
    private final String desc;
}
