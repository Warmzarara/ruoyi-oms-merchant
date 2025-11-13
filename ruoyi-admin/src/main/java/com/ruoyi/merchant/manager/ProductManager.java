package com.ruoyi.merchant.manager;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.yulichang.base.MPJBaseService;
import com.ruoyi.merchant.domain.Product;


public interface ProductManager extends MPJBaseService<Product> {
    IPage<Product> findProductList(Product product, int pageNo, int pageSize);
}
