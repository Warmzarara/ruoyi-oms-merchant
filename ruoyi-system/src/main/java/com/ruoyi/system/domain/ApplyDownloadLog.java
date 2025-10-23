package com.ruoyi.system.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 下载日志实体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplyDownloadLog {
    private Long id;            // 主键ID
    private String batchId;     // 批次ID
    private String queryCond;   // JSON数组：申请明细ID
    private String status;      // 导出状态：INIT / FINISHED / FAILED
}