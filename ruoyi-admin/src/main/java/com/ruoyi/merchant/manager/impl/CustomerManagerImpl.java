package com.ruoyi.merchant.manager.impl;

import com.github.yulichang.base.MPJBaseServiceImpl;
import com.github.yulichang.query.MPJLambdaQueryWrapper;
import com.ruoyi.common.enums.YesNoEnum;
import com.ruoyi.merchant.domain.Customer;
import com.ruoyi.merchant.enums.CustomerStatusEnum;
import com.ruoyi.merchant.manager.CustomerManager;
import com.ruoyi.merchant.mapper.CustomerMapper;
import org.springframework.stereotype.Service;

@Service
public class CustomerManagerImpl extends MPJBaseServiceImpl<CustomerMapper, Customer> implements CustomerManager {
    @Override
    public Customer getByPhone(String phone) {
        MPJLambdaQueryWrapper<Customer> wrapper = new MPJLambdaQueryWrapper<>();
        wrapper.selectAll(Customer.class);
        wrapper.eq(Customer::getPhoneNumber, phone);
        wrapper.eq(Customer::getIsDeleted, YesNoEnum.NO.getCode());
        return selectJoinOne(Customer.class, wrapper);
    }
}
