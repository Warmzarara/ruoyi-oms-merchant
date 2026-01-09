package com.ruoyi.merchant.strategy.impl;

import cn.hutool.core.util.NumberUtil;
import com.ruoyi.merchant.strategy.PriceCalculatorStrategy;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * 合作客户价格计算实现
 */
@Component("coopStrategy")
public class CoopStrategy implements PriceCalculatorStrategy {
    /**
     * 合作客户 打9折 2026-01-04
     * @param originalPrice 原始价格
     * @return 折扣价格
     */
    @Override
    public BigDecimal calculate(BigDecimal originalPrice) {
        return NumberUtil.mul(originalPrice, new BigDecimal("0.9"));
    }
}
