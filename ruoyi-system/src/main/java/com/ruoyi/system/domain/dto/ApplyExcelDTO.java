package com.ruoyi.system.domain.dto;

import lombok.Data;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class ApplyExcelDTO {

    @ExcelProperty("序号")
    private Integer index;

    @ExcelProperty("销售平台")
    private String platform;

    @ExcelProperty("申请id(uuid)")
    private String uuid;

    @ExcelProperty("以旧换新分类")
    private String bizTypeDesc;

    @ExcelProperty("申请日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date applyTime;

    @ExcelProperty("消费者名称")
    private String consumerName;

    @ExcelProperty("消费者手机号码")
    private String consumerNumber;

    @ExcelProperty("证件类型")
    private String cardType;

    @ExcelProperty("消费者身份证号")
    private String idcardNumber;

    @ExcelProperty("销售企业（网点）名称")
    private String saleEnterpriseName;

    @ExcelProperty("商品品类")
    private String cateCodeName;

    @ExcelProperty("商品条码")
    private String barcode;

    @ExcelProperty("商品名称")
    private String itemName;

    @ExcelProperty("品牌名称")
    private String brand;

//    @ExcelProperty("商品数量")
//    private Integer goodsNum;

    @ExcelProperty("商品序列号（SN码）")
    private String snCode;

    @ExcelProperty("IMEI1")
    private String imei1;

    @ExcelProperty("IMEI2")
    private String imei2;

    @ExcelProperty("能效等级")
    private String grade;

    @ExcelProperty("订单号")
    private String orderNumber;

    @ExcelProperty("付款方式")
    private String paychnl;

    @ExcelProperty("交易时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date tradeTime;

    @ExcelProperty("订单金额")
    private BigDecimal orderAmount;

    @ExcelProperty("旧机金额")
    private BigDecimal discountAmt;

    @ExcelProperty("订单中不参与国补活动的支付金额")
    private BigDecimal serviceAmt;

    @ExcelProperty("其他退款金额")
    private BigDecimal otherRefundAmt;

//    @ExcelProperty("实付金额")
//    // 订单-补贴
//    private BigDecimal taxInvoiceAmt1;

    @ExcelProperty("补贴金额（企业提供）")
    private BigDecimal subsidyAmt;

    @ExcelProperty("发票代码")
    private String invoiceCode;

    @ExcelProperty("发票号码")
    private String invoiceNumber;

    @ExcelProperty("开票日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date issueDate;

    @ExcelProperty("发票金额（含税）")
    private BigDecimal taxInvoiceAmt;

    @ExcelProperty("收货地址编码（市级）")
    private String addrLv2;

    @ExcelProperty("收货地址编码（区镇级）")
    private String addrLv3;

    @ExcelProperty("详细收货地址")
    private String addrLv4;

    @ExcelProperty("审核状态")
    private String verifyStatusDesc;

    @ExcelProperty("物流单号")
    private String trackNum;

    @ExcelProperty("物流公司")
    private String compName;

    @ExcelProperty("物流信息")
    private String logisticsDetailJson;

    @ExcelProperty("【发票查验】销方名称")
    private String selName;

    @ExcelProperty("【发票查验】销方税号")
    private String selTaxCode;

    @ExcelProperty("【发票查验】购方名称")
    private String buyName;

    @ExcelProperty("【发票查验】购方税号")
    private String buyTaxCode;

    @ExcelProperty("【发票查验】备注信息")
    private String remark;

    @ExcelProperty("【交易流水查验】")
    private String payInfoJson;

    @ExcelProperty("【粤焕新资格查验】")
    private String yhxJson;

}
