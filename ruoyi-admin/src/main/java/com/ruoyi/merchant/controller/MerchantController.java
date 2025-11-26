package com.ruoyi.merchant.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.merchant.domain.req.ProductAddReq;
import com.ruoyi.merchant.domain.req.ProductListReq;
import com.ruoyi.merchant.domain.req.ProductStatusReq;
import com.ruoyi.merchant.service.MerchantService;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;

@RestController
@RequestMapping("/RuoYi/merchant")
@Validated
public class MerchantController extends BaseController {
    
    @Resource
    private MerchantService merchantService;
    
    @GetMapping("/getMerchantList")
    @ApiOperation("商家端-商品列表查询")
    public AjaxResult getMerchantList(ProductListReq merchantListReq){
        this.startPage();
        return AjaxResult.success(merchantService.findProductList(merchantListReq));
    }
    
    @GetMapping("/getMerchantDetail")
    @ApiOperation("商家端-商品详情查询")
    public AjaxResult getMerchantDetail(@NotBlank(message = "productId不能为空") @RequestParam("productId") String productId){
        return AjaxResult.success(merchantService.findProductDetail(productId));
    }
    
    @PostMapping("/addProduct")
    @ApiOperation("商家端-新增商品")
    public AjaxResult addMerchant(@RequestBody @Validated ProductAddReq productAddReq){
        merchantService.addProduct(productAddReq);
        return AjaxResult.success();
    }
    
    @PutMapping("updateMerchantStatus")
    @ApiOperation("商家端-商品状态修改")
    public AjaxResult updateMerchantStatus(@RequestBody @Validated ProductStatusReq productStatusReq){
        merchantService.updateProductStatus(productStatusReq);
        return AjaxResult.success();
    }
}
