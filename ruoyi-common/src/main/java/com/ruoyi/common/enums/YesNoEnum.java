package com.ruoyi.common.enums;

import lombok.Getter;

/**
 * YES or NO
 */
@Getter
public enum YesNoEnum {
    //是
    YES(1, "是"),
    //否
    NO(0, "否"),
    ;

    private Integer code;
    private String desc;

    YesNoEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static String getDescByCode(Integer code) {
        for (YesNoEnum value : YesNoEnum.values()) {
            if (value.getCode().equals(code)) {
                return value.getDesc();
            }
        }
        return null;
    }

    public static Integer getCodeByDesc(String desc) {
        for (YesNoEnum value : YesNoEnum.values()) {
            if (value.getDesc().equals(desc)) {
                return value.getCode();
            }
        }
        return null;
    }

    public static Integer getCode(Boolean bl){
        if(bl){
            return 1;
        }else{
            return 0;
        }
    }
}
