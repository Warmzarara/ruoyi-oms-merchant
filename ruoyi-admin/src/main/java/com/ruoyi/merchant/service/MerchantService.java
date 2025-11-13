package com.ruoyi.merchant.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.merchant.domain.req.MerchantDetailReq;
import com.ruoyi.merchant.domain.req.ProductListReq;
import com.ruoyi.merchant.domain.vo.ProductListVO;

import java.util.List;

public interface MerchantService {
    IPage<ProductListVO> findProductList(ProductListReq merchantListReq);
    MerchantDetailReq findMerchantDetail();
    void addMerchant();
    void updateMerhcantStatus();
    
}
