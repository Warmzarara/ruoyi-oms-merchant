package com.ruoyi.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.yulichang.base.MPJBaseServiceImpl;
import com.ruoyi.system.domain.GoodsType;
import com.ruoyi.system.mapper.GoodsTypeMapper;
import com.ruoyi.system.service.GoodsTypeService;

import java.util.List;

public class GoodsTypeServiceImpl extends MPJBaseServiceImpl<GoodsTypeMapper, GoodsType> implements GoodsTypeService {
    private static final String NOT_DELETED = "NO";
    @Override
    public List<GoodsType> listAllNoDeleted() {
        LambdaQueryWrapper<GoodsType> goodsTypeWrapper = new LambdaQueryWrapper<>();
        goodsTypeWrapper.eq(GoodsType::getIsDeleted, NOT_DELETED);
        return this.list(goodsTypeWrapper);
    }
}
