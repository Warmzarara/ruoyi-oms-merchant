package com.ruoyi.merchant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CheckOnShelfEnum {
    ONLY_ON_SHELF("1"),
    DONT_CHECK("0");
    private final String code;
}
