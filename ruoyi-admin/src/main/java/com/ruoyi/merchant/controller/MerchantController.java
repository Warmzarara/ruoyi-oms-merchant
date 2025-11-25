package com.ruoyi.merchant.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.merchant.domain.req.ProductListReq;
import com.ruoyi.merchant.service.MerchantService;
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
    public AjaxResult getMerchantList(ProductListReq merchantListReq){
        this.startPage();
        return AjaxResult.success(merchantService.findProductList(merchantListReq));
    }
    
    @GetMapping("/getMerchantDetail")
    public AjaxResult getMerchantDetail(@NotBlank(message = "productId不能为空") @RequestParam("productId") String productId){
        return AjaxResult.success(merchantService.findProductDetail(productId));
    }
    
    @PostMapping("/addMerchant")
    public AjaxResult addMerchant(){
        return null;
    }
    
    @PutMapping("updateMerchantStatus")
    public AjaxResult updateMerchantStatus(){
        return null;
    }
}
