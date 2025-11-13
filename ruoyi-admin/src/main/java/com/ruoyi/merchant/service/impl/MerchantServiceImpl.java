package com.ruoyi.merchant.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.enums.YesNoEnum;
import com.ruoyi.merchant.domain.Product;
import com.ruoyi.merchant.domain.ProductImage;
import com.ruoyi.merchant.domain.req.MerchantDetailReq;
import com.ruoyi.merchant.domain.req.ProductListReq;
import com.ruoyi.merchant.domain.vo.ProductListVO;
import com.ruoyi.merchant.manager.ProductImageManager;
import com.ruoyi.merchant.manager.ProductManager;
import com.ruoyi.merchant.service.MerchantService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MerchantServiceImpl implements MerchantService {
    
    @Resource
    ProductManager productManager;
    
    @Resource
    ProductImageManager productImageManager;

    /**
     * 查询商品列表
     */
    @Override
    public Page<ProductListVO> findProductList(ProductListReq req) {
        Product product = new Product();
        BeanUtil.copyProperties(req, product);
        //  分页参数
        int pageNo = req.getPageNo();
        int pageSize = req.getPageSize();
        // 通过分页获取商品数据
        IPage<Product> productListPage = productManager.findProductList(product, pageNo, pageSize);
        List<Product> productList = productListPage.getRecords();
        if (CollUtil.isEmpty(productList)) {
            return new Page<>(pageNo, pageSize);
        }
        //  封装商品列表响应数据
        List<ProductListVO> productListVOList = buildProductListVOList(productList);
        return new Page<ProductListVO>(pageNo, pageSize, productListPage.getTotal()).setRecords(productListVOList);
    }

    private List<ProductListVO> buildProductListVOList(List<Product> productList){
        //  获取商品id与展示图片链接的map
        Map<String, String> prdIdShowImgUrlMap = getPrdIdImgUrlMap(productList);
        // 封装商品列表响应数据
        return productList.stream().map(prd -> buildProductListVO(prd, prdIdShowImgUrlMap))
                .collect(Collectors.toList());
    }
    
    private Map<String,String> getPrdIdImgUrlMap(List<Product> productList) {
        Set<String> productIdSet = productList.stream().map(Product::getId).collect(Collectors.toSet());
        List<ProductImage> productImageList = productImageManager.findProductImagesByProductIds(new ArrayList<>(productIdSet));
        return productImageList.stream()
                .filter(prdImg -> YesNoEnum.NO.getCode().equals(prdImg.getIsDeleted())
                        && StrUtil.isNotBlank(prdImg.getImageUrl()))
                .collect(Collectors.toMap(ProductImage::getProductId, ProductImage::getImageUrl,
                        (existing, replacement) -> existing));  //处理重复的展示图片
    }
    
    private ProductListVO buildProductListVO(Product prd, Map<String, String> prdIdShowImgUrlMap){
        ProductListVO productListVO = new ProductListVO();
        productListVO.setProductId(prd.getId());
        productListVO.setTitle(prd.getTitle());
        productListVO.setPrice(prd.getPrice());
        //todo 后续可改为默认图片链接
        String showImgUrl = StrUtil.emptyIfNull(prdIdShowImgUrlMap.get(prd.getId()));
        productListVO.setImageUrl(showImgUrl);
        return productListVO;
    }

    @Override
    public MerchantDetailReq findMerchantDetail() {
        return null;
    }

    @Override
    public void addMerchant() {

    }

    @Override
    public void updateMerhcantStatus() {

    }
}
