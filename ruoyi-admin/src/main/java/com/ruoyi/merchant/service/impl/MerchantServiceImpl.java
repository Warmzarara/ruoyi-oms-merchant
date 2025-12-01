package com.ruoyi.merchant.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.ruoyi.common.enums.YesNoEnum;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.merchant.domain.Product;
import com.ruoyi.merchant.domain.ProductImage;
import com.ruoyi.merchant.domain.dto.ProductImgDTO;
import com.ruoyi.merchant.domain.req.*;
import com.ruoyi.merchant.domain.vo.ProductDetailVO;
import com.ruoyi.merchant.domain.vo.ProductListVO;
import com.ruoyi.merchant.manager.ProductImageManager;
import com.ruoyi.merchant.manager.ProductManager;
import com.ruoyi.merchant.manager.UserManager;
import com.ruoyi.merchant.service.MerchantService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    
    @Resource
    UserManager userManager;

    /**
     * 查询商品列表
     */
    @Override
    public List<ProductListVO> findProductList(ProductListReq req) {
        Product product = new Product();
        BeanUtil.copyProperties(req, product);
        // 通过分页获取商品数据
        List<Product> productList = productManager.findProductList(product);
        if (CollUtil.isEmpty(productList)) {
            return new ArrayList<>();
        }
        //  封装商品列表响应数据
        return buildProductListVOList(productList);
    }
    
    /**
     * 查询商品详情
     * @param productId
     * @return
     */
    @Override
    public ProductDetailVO findProductDetail(String productId) {
        // 得到扁平的单个商品详情数据
        Product product = productManager.findProductById(productId);
        if (ObjUtil.isNull(product)) {
            throw new ServiceException("该商品不存在");
        }
        List<ProductImage> productImgList = productManager.findProductImgListByPrdId(productId);
        if (CollUtil.isEmpty(productImgList)) {
            throw new ServiceException("该商品不存在图片信息");
        }
        // 获取首页展示图片
        String displayImg = getDisplayImg(productImgList);
        // 开始封装响应对象
        ProductDetailVO productDetailVO = new ProductDetailVO();
        BeanUtil.copyProperties(product, productDetailVO);
        productDetailVO.setProductId(productId);
        productDetailVO.setShowImgUrl(displayImg);
        productDetailVO.setProductImageList(productImgList);
        return productDetailVO;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addProduct(ProductAddReq productAddReq){
        // 校验并获取图片列表数据
        List<ProductImgDTO> prdImgDTOList = validAndGetImgList(productAddReq);
        Product product = new Product();
        BeanUtil.copyProperties(productAddReq, product);
        String savePrdId = IdUtil.fastSimpleUUID();
        product.setId(savePrdId);
        product.setCreatedUser(SecurityUtils.getUsername());
        product.setCreatedTime(new Date());
        if (!productManager.save(product)) {
            throw new ServiceException("商品数据新增失败");
        }
        // 处理图片数据
        Integer yesCode = YesNoEnum.YES.getCode();
        Integer noCode = YesNoEnum.NO.getCode();
        boolean hasDisplayImg = prdImgDTOList.stream().anyMatch(img -> ObjUtil.equals(img.getIsDisplay(), yesCode));
        if (!hasDisplayImg) {
            prdImgDTOList.get(0).setIsDisplay(yesCode);
        }

        List<ProductImage> savePrdImgList = prdImgDTOList.stream().map(img -> {
            ProductImage savePrdImg = new ProductImage();
            savePrdImg.setId(IdUtil.fastSimpleUUID());
            savePrdImg.setProductId(savePrdId);
            savePrdImg.setImageUrl(img.getImageUrl());
            savePrdImg.setDisplayOrder(img.getDisplayOrder());
            savePrdImg.setIsDisplay(img.getIsDisplay());
            savePrdImg.setIsDisplay(ObjUtil.defaultIfNull(img.getIsDisplay(), noCode));
            savePrdImg.setCreatedTime(new Date());
            savePrdImg.setCreatedUser(SecurityUtils.getUsername());
            return savePrdImg;
        }).collect(Collectors.toList());
        
        if (!productImageManager.saveBatch(savePrdImgList)) {
            throw new ServiceException("商品图片数据更新失败，商品新增失败");
        }
    }
    
    private List<ProductImgDTO> validAndGetImgList(ProductAddReq productAddReq){
        List<ProductImgDTO> dtoList = productAddReq.getPrdImgList();
        if (CollUtil.isEmpty(dtoList)) {
            throw new ServiceException("图片数据为空，请至少上传一张图片");
        }
        return dtoList;
    }

    private List<ProductImgDTO> validAndGetImgList(ProductEditReq productEditReq){
        List<ProductImgDTO> dtoList = productEditReq.getPrdImgList();
        if (CollUtil.isEmpty(dtoList)) {
            throw new ServiceException("图片数据为空，请至少上传一张图片");
        }
        return dtoList;
    }
    
    private String getDisplayImg(List<ProductImage> productImgList){
        // 获取首页展示图片，如果未配置则提供图片列表中的第一张
        ProductImage prdImg = productImgList.stream()
                .filter(productImg -> 
                        ObjUtil.isNotNull(productImg.getIsDisplay()) && 
                        productImg.getIsDisplay().equals(YesNoEnum.YES.getCode()))
                .findFirst()
                .orElse(productImgList.get(0));
        if (ObjUtil.isNotNull(prdImg) && StrUtil.isNotBlank(prdImg.getImageUrl())) {
            return prdImg.getImageUrl();
        }
        throw new ServiceException("商品图片数据异常");
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
    public void updateProductStatus(ProductStatusReq productStatusReq) {
        Product product = new Product();
        product.setStatus(productStatusReq.getStatus());
        product.setId(productStatusReq.getProductId());
        product.setUpdatedTime(new Date());
        product.setUpdatedUser(SecurityUtils.getUsername());
        if (!productManager.updateById(product)) {
            throw new ServiceException("商品状态修改失败");
        }
    }

    @Transactional
    @Override
    public void updateProductDetail(ProductEditReq productEditReq) {
        String prdId = productEditReq.getProductId();
        Product product = new Product();
        BeanUtil.copyProperties(productEditReq, product);
        product.setId(prdId);
        product.setUpdatedUser(SecurityUtils.getUsername());
        product.setUpdatedTime(new Date());
        if (!productManager.updateById(product)) {
            throw new ServiceException("商品信息修改失败");
        }
        List<ProductImgDTO> productImgDTOList = validAndGetImgList(productEditReq);
        List<ProductImage> prodductImageList = productImgDTOList.stream().map(imgDto -> {
            ProductImage productImage = new ProductImage();
            BeanUtil.copyProperties(imgDto, productImage);
            productImage.setProductId(prdId);
            productImage.setUpdatedTime(new Date());
            productImage.setUpdatedUser(SecurityUtils.getUsername());
            return productImage;
        }).collect(Collectors.toList());
        if (!productImageManager.updateBatchById(prodductImageList)) {
            throw new ServiceException("商品信息修改失败，图片数据修改失败");
        }
    }

    @Override
    public void merchantOrderCreate(MerchantOrderCreateReq merchantOrderCreateReq) {
        // 校验用户状态是否正常
//        userManager.getById()
        // 开始循环
        // 判断商品是否存在并且是上架状态
        // 执行商品减少库存的更新操作
        // 更新条数是否等于1
        // 插入订单信息
        // 插入订单详情信息
    }
}
