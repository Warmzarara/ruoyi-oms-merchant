package com.ruoyi.system.domain.dto;

import com.ruoyi.system.domain.MerchantApplyDetail;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class MerchantExportParam {
    private Map<String, MerchantApplyDetail> applyMap = new HashMap<>();
}
