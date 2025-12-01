package com.ruoyi.merchant.manager.impl;

import com.github.yulichang.base.MPJBaseServiceImpl;
import com.ruoyi.merchant.domain.User;
import com.ruoyi.merchant.manager.UserManager;
import com.ruoyi.merchant.mapper.UserMapper;
import org.springframework.stereotype.Service;

@Service
public class UserManagerImpl extends MPJBaseServiceImpl<UserMapper, User> implements UserManager {
}
