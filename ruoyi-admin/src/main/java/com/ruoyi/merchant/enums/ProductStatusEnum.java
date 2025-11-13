package com.ruoyi.merchant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ProductStatusEnum {
    OFF_SHELF(0,"下架"),
    ON_SHELF(1,"上架");
    private final Integer code;
    private final String desc;
    
}
