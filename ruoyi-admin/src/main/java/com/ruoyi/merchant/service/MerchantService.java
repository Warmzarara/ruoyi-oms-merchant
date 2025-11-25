package com.ruoyi.merchant.service;

import com.ruoyi.merchant.domain.req.ProductListReq;
import com.ruoyi.merchant.domain.vo.ProductDetailVO;
import com.ruoyi.merchant.domain.vo.ProductListVO;

import java.util.List;

public interface MerchantService {
    List<ProductListVO> findProductList(ProductListReq merchantListReq);
    ProductDetailVO findProductDetail(String productId);
    void addMerchant();
    void updateMerhcantStatus();
}
