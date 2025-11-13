package com.ruoyi.merchant.controller;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.merchant.domain.req.ProductListReq;
import com.ruoyi.merchant.service.MerchantService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/RuoYi/merchant")
public class MerchantController {
    
    @Resource
    private MerchantService merchantService;
    
    @GetMapping("/getMerchantList")
    public AjaxResult getMerchantList(ProductListReq merchantListReq){
        AjaxResult.success(merchantService.findProductList(merchantListReq));
        return null;
    }
    
    @GetMapping("/getMerchantDetail")
    public AjaxResult getMerchantDetail(){
        return null;
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
