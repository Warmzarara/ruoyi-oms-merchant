package com.ruoyi.merchant.domain.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CalculatorDto {
    // 客户联系方式
    private String phone;
    // 原始总价
    private BigDecimal originalTotal;
}
