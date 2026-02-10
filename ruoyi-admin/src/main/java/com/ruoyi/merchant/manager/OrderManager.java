package com.ruoyi.merchant.manager;

import com.github.yulichang.base.MPJBaseService;
import com.ruoyi.merchant.domain.Order;

import java.util.List;

public interface OrderManager extends MPJBaseService<Order> {
    Boolean insertOrderOne(Order order);
    Order getOrderByOrderId(String orderId);
}
