package com.ruoyi.merchant.factory;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjUtil;
import com.ruoyi.merchant.strategy.PriceCalculatorStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

@Component
public class PriceStrategyFactory {
    @Resource
    private Map<String, PriceCalculatorStrategy> strategyMap;
    public PriceCalculatorStrategy getStrategyByCustomerType(Integer customerType) {
        // 散户策略
        if (ObjUtil.isNull(customerType) || ObjUtil.equals(customerType, "0")) {
            return strategyMap.get("normalStrategy");
        }
        // 合作客户策略
        if (ObjUtil.equals(customerType, "1")) {
            return strategyMap.get("CoopStrategy");
        }
        return strategyMap.get("normalStrategy");
    }
}
