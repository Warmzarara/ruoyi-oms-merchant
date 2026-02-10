package com.ruoyi.merchant.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.merchant.domain.req.*;
import com.ruoyi.merchant.domain.vo.OrderCreateVO;
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
        return success(merchantService.findProductList(merchantListReq));
    }
    
    @GetMapping("/getMerchantDetail")
    @ApiOperation("商家端-商品详情查询")
    public AjaxResult getMerchantDetail(@NotBlank(message = "productId不能为空") @RequestParam("productId") String productId){
        return success(merchantService.findProductDetail(productId));
    }
    
    @PostMapping("/addProduct")
    @ApiOperation("商家端-新增商品")
    public AjaxResult addMerchant(@RequestBody @Validated ProductAddReq productAddReq){
        merchantService.addProduct(productAddReq);
        return success();
    }
    
    @PutMapping("/updateProductStatus")
    @ApiOperation("商家端-商品状态修改")
    public AjaxResult updateProductStatus(@RequestBody @Validated ProductStatusReq productStatusReq){
        merchantService.updateProductStatus(productStatusReq);
        return success();
    }
    
    @PutMapping("/updateProductDetail")
    public AjaxResult updateProductDetail(@RequestBody @Validated ProductEditReq productEditReq) {
        merchantService.updateProductDetail(productEditReq);
        return success();
    }
    
    @PostMapping("/merchantCreateOrder")
    public AjaxResult createOrder(@RequestBody MerchantOrderCreateReq merchantOrderCreateReq){
        OrderCreateVO orderCreateVO = merchantService.merchantOrderCreate(merchantOrderCreateReq);
        return success(orderCreateVO);
    }
    
    @PostMapping("/deliver")
    public AjaxResult deliver(@RequestBody MerchantDeliverReq merchantDeliverReq){
        return success();
    }
    
    @PostMapping("/pay")
    public AjaxResult MerchantPay(@RequestBody MerchantPayRequest merchantPayRequest){
        return success();
    }
}
