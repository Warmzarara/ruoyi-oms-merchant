package com.ruoyi.system.service.impl;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.ruoyi.system.domain.ApplyDownloadLog;
import com.ruoyi.system.service.ExportMerchantService;

import java.util.List;

public class ExportMerchantServiceImpl implements ExportMerchantService {

    @Override
    public void exportMerchant(ApplyDownloadLog applyDownloadLog) {
//        List<String> list = JSONUtil.toList(applyDownloadLog.getQuery_cond(), String.class);
        //获取申请明细

        //生成明细文件夹，文件下下包含Excel和图片
        //生成汇总excel，与压缩包同名
        //打包为zip，上传到文件服务器
        //记录导出日志
    }
}
