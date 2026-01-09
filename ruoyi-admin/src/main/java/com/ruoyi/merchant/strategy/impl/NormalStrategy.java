package com.ruoyi.merchant.strategy.impl;

import com.ruoyi.merchant.strategy.PriceCalculatorStrategy;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * 普通客户价格计算实现
 */
@Component("normalStrategy")
public class NormalStrategy implements PriceCalculatorStrategy {
    /**
     * 散户-原价返回
     * @param originalPrice 原始价格
     * @return 计算价格
     */
    @Override
    public BigDecimal calculate(BigDecimal originalPrice) {
        return originalPrice;
    }
}
