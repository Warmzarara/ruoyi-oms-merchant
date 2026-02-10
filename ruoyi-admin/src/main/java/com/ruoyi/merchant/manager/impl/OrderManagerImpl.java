package com.ruoyi.merchant.manager.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.github.yulichang.base.MPJBaseServiceImpl;
import com.ruoyi.common.enums.YesNoEnum;
import com.ruoyi.merchant.domain.Order;
import com.ruoyi.merchant.manager.OrderManager;
import com.ruoyi.merchant.mapper.OrderMapper;
import org.springframework.stereotype.Service;

@Service
public class OrderManagerImpl extends MPJBaseServiceImpl<OrderMapper, Order> implements OrderManager {
    @Override
    public Boolean insertOrderOne(Order order) {
        UpdateWrapper<Order> wrapper = new UpdateWrapper<>();
        return null;
    }

    @Override
    public Order getOrderByOrderId(String orderId) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getId, orderId);
        wrapper.eq(Order::getIsDeleted, YesNoEnum.NO.getCode());
        return getOne(wrapper);
    }
}
