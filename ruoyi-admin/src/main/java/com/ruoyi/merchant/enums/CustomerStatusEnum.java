package com.ruoyi.merchant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CustomerStatusEnum {
    ERR_STATUS(0,"用户状态异常"),
    OK_STATUS(1,"用户状态正常");
    private final Integer code;
    private final String desc;
}
