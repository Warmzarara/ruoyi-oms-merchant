package com.ruoyi.merchant.manager.impl;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.base.MPJBaseServiceImpl;
import com.github.yulichang.query.MPJLambdaQueryWrapper;
import com.ruoyi.common.enums.YesNoEnum;
import com.ruoyi.merchant.mapper.ProductMapper;
import com.ruoyi.merchant.domain.Product;
import com.ruoyi.merchant.manager.ProductManager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ProductManagerImpl extends MPJBaseServiceImpl<ProductMapper, Product> implements ProductManager {

    @Override
    public Page<Product> findProductList(Product product, int pageNo, int pageSize) {
        MPJLambdaQueryWrapper<Product> wrapper = new MPJLambdaQueryWrapper<>();
        wrapper.like(StrUtil.isNotBlank(product.getTitle()), Product::getTitle, product.getTitle());
        wrapper.eq(ObjUtil.isNotEmpty(product.getStatus()), Product::getStatus, product.getStatus());
        wrapper.eq(Product::getIsDeleted, YesNoEnum.NO.getCode());

        Page<Product> page = new Page<>(pageNo, pageSize);

        return this.selectJoinListPage(page, Product.class, wrapper);
    }
}


