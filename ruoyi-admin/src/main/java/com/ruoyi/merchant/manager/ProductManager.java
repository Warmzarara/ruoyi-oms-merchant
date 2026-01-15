package com.ruoyi.merchant.manager;

import com.github.yulichang.base.MPJBaseService;
import com.ruoyi.merchant.domain.Product;
import com.ruoyi.merchant.domain.ProductImage;
import com.ruoyi.merchant.domain.dto.OrderProductDTO;
import com.ruoyi.merchant.enums.CheckOnShelfEnum;

import java.util.List;


public interface ProductManager extends MPJBaseService<Product> {
    List<Product> findProductList(Product product);
    Product findProductOne(Product product);
    List<ProductImage> findProductImgList(ProductImage productImage);
    List<ProductImage> findProductImgListByPrdId(String productId);
    Product findProductById(String productId);
    List<Product> findProductByIds(List<String> productIds, CheckOnShelfEnum onlyOnShelf);
    Boolean decreaseProductStock(String productId, Integer decreaseNum);
    int batchDecreaseProductStock(List<OrderProductDTO> orderProductDTOList);
}
