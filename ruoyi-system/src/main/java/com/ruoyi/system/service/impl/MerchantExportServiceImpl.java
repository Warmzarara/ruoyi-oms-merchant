package com.ruoyi.system.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.ApplyDownloadLog;
import com.ruoyi.system.domain.MerchantApplyDetail;
import com.ruoyi.system.service.ApplyDownloadLogService;
import com.ruoyi.system.service.MerchantApplyDetailService;
import com.ruoyi.system.service.MerchantExportService;

import javax.annotation.Resource;
import java.util.List;

public class MerchantExportServiceImpl implements MerchantExportService {
    @Resource
    private ApplyDownloadLogService applyDownloadLogService;

    @Resource
    private MerchantApplyDetailService merchantApplyDetailService;

    public void exportMerchantDetailToZip() {
        ApplyDownloadLog unexportedOne = applyDownloadLogService.findUnexportedOne();
        if (unexportedOne == null) {
            AjaxResult.success("当前没有需要导出的数据");
        }
        String applyIds = unexportedOne.getQueryCond();
        List<String> applyIdList = JSONArray.parseArray(applyIds, String.class);
        //查询导出需要的数据
        List<MerchantApplyDetail> merchantApplyDetails = merchantApplyDetailService.listByIds(applyIdList);
    }
}
