package com.ruoyi.merchant.strategy;

import java.math.BigDecimal;

/**
 * 价格计算接口
 */
public interface PriceCalculatorStrategy {
    BigDecimal calculate(BigDecimal originalPrice);
}
