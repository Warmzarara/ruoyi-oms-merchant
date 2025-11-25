package com.ruoyi.merchant.manager;

import com.github.yulichang.base.MPJBaseService;
import com.ruoyi.merchant.domain.Product;
import com.ruoyi.merchant.domain.ProductImage;

import java.util.List;


public interface ProductManager extends MPJBaseService<Product> {
    List<Product> findProductList(Product product);
    Product findProductOne(Product product);
    List<ProductImage> findProductImgList(ProductImage productImage);
    List<ProductImage> findProductImgListByPrdId(String productId);
    Product findProductById(String productId);
}
