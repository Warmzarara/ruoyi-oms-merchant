package com.ruoyi.system.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author fengji
 * @description
 * @date 2024/12/16
 **/

@Data
@TableName("E_MERCHANT_APPLY")
@ApiModel(value = "商家补贴申请", description = "")
public class MerchantApply {
    /**
     * 唯一键
     */
    //  @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    @ApiModelProperty(name = "唯一键", notes = "")
    private String id;
    @ApiModelProperty(name = "商家ID", notes = "")
    private String merchantId;
    /**
     * 申请时间
     */
    @ApiModelProperty(name = "申请时间", notes = "")
    private Date applyTime;
    /**
     * 销售企业（网点）编码
     */
    @ApiModelProperty(name = "销售企业（网点）编码", notes = "")
    private String saleEnterpriseCode;
    /**
     * 销售企业（网点）名称
     */
    @ApiModelProperty(name = "销售企业（网点）名称", notes = "")
    private String saleEnterpriseName;

    /**
     * 购买数量
     */
    @ApiModelProperty(name = "购买数量", notes = "")
    private Integer buyNums;
    /**
     * 交易日期
     */
    @ApiModelProperty(name = "交易日期", notes = "")
    private Date tradeTime;
    /**
     * 含税发票金额（元）
     */
    @ApiModelProperty(name = "含税发票金额（元）", notes = "")
    private BigDecimal taxInvoiceAmt;
    /**
     * 实付金额（元）
     */
    @ApiModelProperty(name = "实付金额（元）", notes = "")
    private BigDecimal subsidyAmt;
    /**
     * 财政补贴金额（元）
     */
    private BigDecimal fiscalSubsidyAmt;
    /**
     * 备注
     */
    @ApiModelProperty(name = "备注", notes = "")
    private String remark;
    /**
     * 订单号
     */
    @ApiModelProperty(name = "订单号", notes = "")
    private String orderNumber;
    /**
     * 消费者名称
     */
    @ApiModelProperty(name = "消费者名称", notes = "")
    private String consumerName;
    /**
     * 消费者电话
     */
    @ApiModelProperty(name = "消费者电话", notes = "")
    private String consumerNumber;
    /**
     * 身份证号码
     */
    @ApiModelProperty(name = "身份证号码", notes = "")
    private String idcardNumber;
    /**
     * 付款方式
     */
    @ApiModelProperty(name = "付款方式", notes = "")
    private String payType;
    /** 能效等级 */

    /**
     * 发票代码
     */
    @ApiModelProperty(name = "发票代码", notes = "")
    private String invoiceCode;
    /**
     * 发票号码
     */
    @ApiModelProperty(name = "发票号码", notes = "")
    private String invoiceNumber;
    /**
     * 发票开具日期
     */
    @ApiModelProperty(name = "发票开具日期", notes = "")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date issueDate;
    /**
     * 项目名称简称
     */
    @ApiModelProperty(name = "项目名称简称", notes = "")
    private String proName;
    /**
     * 项目名称简称对应的单价（不为0）
     */
    @ApiModelProperty(name = "项目名称简称对应的单价（不为0）", notes = "")
    private BigDecimal proAmt;
    /**
     * 是否删除
     */
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(name = "是否删除", notes = "")
    private Integer isDeleted;
    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(name = "创建人", notes = "")
    private String createdUser;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(name = "创建时间", notes = "")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdTime;
    /**
     * 更新人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(name = "更新人", notes = "")
    private String updatedUser;
    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(name = "更新时间", notes = "")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updatedTime;
    /**
     * 业务类型 1家电 2家装
     */
    @ApiModelProperty(name = "业务类型", notes = "")
    private Integer bizType;


    /**
     * 核验状态
     */
    private Integer verifyStatus;


    /**
     * 提交时间
     */
    private Date submissionDate;


    /**
     * 收货地址（省）
     */
    private String provinceAddr;

    /**
     * 收货地址（市）
     */
    private String cityAddr;

    /**
     * 收货地址（区）
     */
    private String districtAddr;

    /**
     * 详细收货地址
     */
    private String detailedAddr;


    /**
     * 校验码
     */
    @ApiModelProperty("校验码")
    private String checkCode;

    @ApiModelProperty(value = "销售平台")

    private String platform;

    @ApiModelProperty(value = "销售平台名称")
    private String platformName;
    @ApiModelProperty(value = "是否人工提交 1-是 2-否")
    private Integer isManual;

    /**
     * 活动ID
     */
    private String actId;

    /**
     * 补贴资金拨付生成时间
     */
    private Date paymentGenDate;

    /**
     * 补贴资金拨付日期
     */
    private Date paymentDate;

    /**
     * 资金撤回日期
     */
    private Date fundReturnDate;

    /**
     * 补贴计算总额
     */
    private BigDecimal totalAmt;

    /**
     * 资金拨付附言
     */
    private String postscript;

    /**
     * 资格对应的消费品类  家电品类
     */
    private String vouchType;
    /**
     * 订单金额
     */
    private BigDecimal amount;

    @ApiModelProperty("商户名称（网点）")
    private String merchName;

    @ApiModelProperty("商户ID（网点）")
    private String merchId;

    /**
     * 订单唯一id
     */
    private String orderInfoId;
    /**
     * 订单状态
     */
    @ApiModelProperty("订单状态")
    private Integer orderStatus;
    /**
     * 状态信息
     */
    private String statusMsg;
    /**
     * 最新补贴申请记录ID
     */
    @ApiModelProperty("最新补贴申请记录ID")
    private String latestRecordId;

    /**
     * 旧机金额：一站式换机等场景下，不参与国补活动但是开具到发票上的旧机回收金额
     */
    private BigDecimal discountAmt;

    /**
     * 订单中不参与国补活动的支付金额：如运费、服务费等
     */
    private BigDecimal serviceAmt;

    /**
     * 其他退款金额：部分退款时，不是原路返回的退款金额，如商家质量问题导致的退款不由平台负责，以现金或其他方式退款给消费者，支付机构感知不到的部分
     */
    private BigDecimal otherRefundAmt;
    /**
     * 物流状态：1-通过，2-不通过，3-确认
     * */
    private Integer logisticsStatus;


    /**
     * 下面字段是针对湖北需求增加：20250928
     * */
    @ApiModelProperty("核销券类别")
    private String couponChannel;

    @ApiModelProperty("核销券号")
    private String couponNo;

    @ApiModelProperty("消费资格码")
    private String cnsmpualsCd;

    @ApiModelProperty("发票头")
    private String invoiceHead;

    @ApiModelProperty("发票销售方名称")
    private String invoiceSaleName;

    @ApiModelProperty("发票备注实付金额")
    private BigDecimal invoiceRealAmt;

    @ApiModelProperty("发票备注政府补贴金额")
    private BigDecimal invoiceSubsidyAmt;

    @ApiModelProperty("发票备注69码")
    private String invoiceBarcode;

    @ApiModelProperty("预留字段1")
    private String resv1;

    @ApiModelProperty("预留字段2")
    private String resv2;

    @ApiModelProperty("预留字段3")
    private String resv3;


    @ApiModelProperty("支付平台编码")
    private String payPlatform;

    @ApiModelProperty("支付单号")
    private String payNo;

    @ApiModelProperty("消费者实付金额")
    private BigDecimal payAmt;

    public MerchantApply() {
    }

    public MerchantApply(String id, String merchantId, String actId) {
        this.id = id;
        this.merchantId = merchantId;
        this.actId = actId;
    }
}
