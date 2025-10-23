package com.ruoyi.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.system.domain.ApplyDownloadLog;

public interface ApplyDownloadLogService extends IService<ApplyDownloadLog> {
    ApplyDownloadLog findUnexportedOne();
}
