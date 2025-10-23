package com.ruoyi.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.system.domain.ApplyDownloadLog;
import com.ruoyi.system.mapper.ApplyDownloadLogMapper;
import com.ruoyi.system.service.ApplyDownloadLogService;

public class ApplyDownloadServiceImpl extends ServiceImpl<ApplyDownloadLogMapper, ApplyDownloadLog> implements ApplyDownloadLogService {
    private final static String STATUS_EXPORTING = "EXPORTING";
    @Override
    public ApplyDownloadLog findUnexportedOne() {
        LambdaQueryWrapper<ApplyDownloadLog> logWrapper = new LambdaQueryWrapper<>();
        logWrapper.eq(ApplyDownloadLog::getStatus,STATUS_EXPORTING).last("limit 1");
        return getOne(logWrapper);
    }
}
