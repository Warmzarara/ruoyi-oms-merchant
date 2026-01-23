package com.ruoyi.merchant.service;

import com.ruoyi.merchant.domain.Customer;
import com.ruoyi.merchant.domain.Product;
import com.ruoyi.merchant.domain.req.MerchantOrderCreateReq;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

public interface OrderService {
    void createOrder(MerchantOrderCreateReq merchantOrderCreateReq, Map<String, Product> dbProductIdMap, Customer customer, Date orderCreateTime, String orderId, BigDecimal totalPrice);
}
