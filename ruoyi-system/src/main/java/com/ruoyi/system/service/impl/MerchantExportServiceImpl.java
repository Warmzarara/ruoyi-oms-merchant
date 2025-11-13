//package com.ruoyi.system.service.impl;
//
//import cn.hutool.core.io.FileUtil;
//import cn.hutool.core.util.IdUtil;
//import cn.hutool.core.util.StrUtil;
//import com.alibaba.fastjson2.JSONArray;
//import com.ruoyi.common.core.domain.AjaxResult;
//import com.ruoyi.system.domain.ApplyDownloadLog;
//import com.ruoyi.system.domain.GoodsType;
//import com.ruoyi.system.domain.MerchantApplyDetail;
//import com.ruoyi.system.domain.dto.AppLyExcelDTO;
//import com.ruoyi.system.domain.dto.OrderPictureApplyDTO;
//import com.ruoyi.system.service.ApplyDownloadLogService;
//import com.ruoyi.system.service.GoodsTypeService;
//import com.ruoyi.system.service.MerchantApplyDetailService;
//import com.ruoyi.system.service.MerchantExportService;
//
//import javax.annotation.Resource;
//import java.io.File;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//import java.util.concurrent.atomic.AtomicInteger;
//import java.util.stream.Collectors;
//
//public class MerchantExportServiceImpl implements MerchantExportService {
//    @Resource
//    private ApplyDownloadLogService applyDownloadLogService;
//
//    @Resource
//    private MerchantApplyDetailService merchantApplyDetailService;
//
//    @Resource
//    private GoodsTypeService goodsTypeService;
//
//    public void exportMerchantDetailToZip() {
//        ApplyDownloadLog applyDownloadLog = applyDownloadLogService.findUnexportedOne();
//        if (applyDownloadLog == null) {
//            AjaxResult.success("当前没有需要导出的数据");
//        }
//        String applyIds = applyDownloadLog.getQueryCond();
//        List<String> applyIdList = JSONArray.parseArray(applyIds, String.class);
//        //查询导出需要的数据
//        List<MerchantApplyDetail> merchantApplyDetails = merchantApplyDetailService.listByIds(applyIdList);
//        Map<Long, MerchantApplyDetail> applyMap = merchantApplyDetails.stream()
//                .collect(Collectors.toMap(MerchantApplyDetail::getId, item -> item));
//
//        List<OrderPictureApplyDTO> orderPictureApplyDTOS = merchantApplyDetailService.listPicturesByApplyDetailIds(applyIdList);
//        //初始化文件名
//        String tmpWorkSpace = FileUtil.getTmpDirPath() + IdUtil.fastSimpleUUID();
//        String excelFullPath = tmpWorkSpace + File.separator + applyDownloadLog.getBatchId() + ".xlsx";
//        String tmpZipPath = FileUtil.getTmpDirPath() + IdUtil.fastSimpleUUID() + File.separator + applyDownloadLog.getBatchId() + ".ZIP";
//        try{
//            FileUtil.mkdir(tmpWorkSpace);
//            AtomicInteger index = new AtomicInteger(1);
//            ArrayList<AppLyExcelDTO> sumExcelList = new ArrayList<AppLyExcelDTO>();
//            List<GoodsType> goodsTypeList = goodsTypeService.listAllNoDeleted();
//            applyMap.forEach((applyDetailId, merchantApplyDetail) -> {
//
//                // 明细文件夹
//                String goodTypeName;
//                Optional<GoodsType> typeOpt = goodsTypeList.stream().filter(type -> StrUtil.equals(type.getTypeCode(), merchantApplyDetail.getGoodsTypeCode())).findFirst();
//                if (typeOpt.isPresent()) {
//                    goodTypeName = typeOpt.get().getTypeName();
//                }else {
//                    goodTypeName = merchantApplyDetail.getGoodsTypeCode();
//                }
//                String detailDirName = StrUtil.format("{}_{}", merchantApplyDetail.getId(), goodTypeName);
//                String detailPath = tmpWorkSpace + File.separator + detailDirName;
//                FileUtil.mkdir(detailPath);
//                //明细Excel文件名
//                String detailExcelName = detailPath + File.separator + merchantApplyDetail.getId() + ".xlsx";
//                AppLyExcelDTO appLyExcelDTO = new AppLyExcelDTO();
//                appLyExcelDTO.setIndex(1);
//                appLyExcelDTO.setUuid(String.valueOf(merchantApplyDetail.getId()));
//                appLyExcelDTO.setApplyTime(merchantApplyDetail.getApplyDate());
//            })
//        } catch (Exception e) {
//            throw e;
//        }
//
//    }
//}
