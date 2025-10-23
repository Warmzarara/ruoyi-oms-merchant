package com.ruoyi.web.merchant;

import com.ruoyi.common.core.domain.AjaxResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/merchant")
public class MerchantController {
    @GetMapping("/export")
    public AjaxResult exportMerchantDetailToZip(){
        return AjaxResult.success();
    }

}
