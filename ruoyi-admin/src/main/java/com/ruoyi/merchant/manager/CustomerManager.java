package com.ruoyi.merchant.manager;

import com.github.yulichang.base.MPJBaseService;
import com.ruoyi.merchant.domain.Customer;

public interface CustomerManager extends MPJBaseService<Customer> {
    Customer getByPhone(String phone);
}
