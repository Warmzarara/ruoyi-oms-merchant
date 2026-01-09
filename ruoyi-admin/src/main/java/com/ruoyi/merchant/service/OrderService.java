package com.ruoyi.merchant.service;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.merchant.domain.Customer;
import com.ruoyi.merchant.domain.dto.CalculatorDto;
import com.ruoyi.merchant.factory.PriceStrategyFactory;
import com.ruoyi.merchant.manager.CustomerManager;
import com.ruoyi.merchant.strategy.PriceCalculatorStrategy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;

@Service
public class OrderService {
    @Resource
    private PriceStrategyFactory priceStrategyFactory;
    
    @Resource
    private CustomerManager customerManager;
    
    private void placeOrder(CalculatorDto dto){
        // 1.获取客户信息
        Customer customer = customerManager.getByPhone(dto.getPhone());
        if (ObjUtil.isNull(customer)) {
            throw new ServiceException(StrUtil.format("客户信息不存在{}", dto.getPhone()));
        }
        // 2.计算总价 或者 直接用计算后的总价
        BigDecimal originalTotal = dto.getOriginalTotal();
        // 3.获取策略实现
        PriceCalculatorStrategy calcStrategy = priceStrategyFactory.getStrategyByCustomerType(customer.getCustomerType());
        // 4.调用价格计算方法
        BigDecimal calculatedTotal = calcStrategy.calculate(originalTotal);
        // 5.落库
    }
}
