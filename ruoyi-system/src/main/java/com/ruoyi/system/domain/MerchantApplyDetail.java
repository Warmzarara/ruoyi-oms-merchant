package com.ruoyi.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author mengShuai
 * @date 2024/9/7 17:38
 * @description
 **/
@Data
@TableName("E_MERCHANT_APPLY_DETAIL")
public class MerchantApplyDetail {

    /**
     * 唯一键
     */
    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;
    /**
     * 业务流水号ID
     */
    private String uuid;
    /**
     * 商户申请补贴ID
     */
    private String merchantApplyId;
    /**
     * 申请人
     */
    private String applyCreatedBy;
    /**
     * 申请时间
     */
    private Date applyTime;
    /**
     * 申请人电话
     */
    private String applyTenumber;
    /**
     * 统一社会信用代码
     */
    private String usciCode;
    /**
     * 企业经营类型（1）
     */
    private String operationType;
    /**
     * 销售企业（网点）编码或商户编码
     */
    private String saleEnterpriseCode;
    /**
     * 销售企业（网点）名称或商户名称
     */
    private String saleEnterpriseName;
    /**
     * 所在区编码
     */
    private String admDivCode;
    /**
     * 以旧换新分类编码
     */
    private String oldShiftNewCode;
    /**
     * 以旧换新分类名称
     */
    private String oldShiftNewName;
    /**
     * 商品类型编码
     */
    private String goodsTypeCode;
    /**
     * 商品类型名称
     */
    private String goodsTypeName;
    /**
     * 商品品牌
     */
    private String goodsBrand;
    /**
     * 商品规格型号
     */
    private String goodsModelCode;
    /**
     * 购买数量
     */
    private Integer buyNums;
    /**
     * 交易日期
     */
    private Date tradeTime;
    /**
     * 含税发票金额（元）
     */
    private BigDecimal taxInvoiceAmt;
    /**
     * 财政补贴金额（元）
     */
    private BigDecimal fiscalSubsidyAmt;
    /**
     * 实付金额（元）
     */
    private BigDecimal subsidyAmt;
    /**
     * 备注
     */
    private String remark;
    /**
     * 订单号
     */
    private String orderNumber;
    /**
     * 消费者名称
     */
    private String consumerName;
    /**
     * 消费者电话
     */
    private String consumerNumber;
    /**
     * 身份证号码
     */
    private String idcardNumber;
    /**
     * 付款方式（支付宝、微信、银联、其他）
     * 云闪付	UnP
     * 微信支付	WeP
     * 支付宝	AliP
     * 京东	JdP
     * 唯品会	VPal
     * 苏宁	SnP
     */
    private String payType;
    /**
     * 能效等级
     */
    private String energyEfficiencyType;
    /**
     * 发票代码
     */
    private String invoiceCode;
    /**
     * 发票号码
     */
    private String invoiceNumber;

    /**
     * 发票校验码
     */
    private String checkCode;
    /**
     * 发票开具日期
     */
    private Date issueDate;
    /**
     * 项目名称简称
     */
    private String proName;
    /**
     * 项目名称简称对应的单价（不为0）
     */
    private BigDecimal proAmt;
    /**
     * 查验状态(0-初始，1-已上链，2-回退)
     */
    private Integer checkStatus;
    /**
     * 查验结果(01-不通过，02-通过，03-基本信息不通过)
     */
    private String checkResult;
    /**
     * 补贴撤回后的轮循标记：用于判断是否生成负向补贴：2-未轮循，1-是轮循。轮循checkStatus=2
     */
    private Integer validStatus;
    /**
     * 补贴计算状态: 1已计算2未计算
     */
    private Integer subsidyStatus;
    /**
     * 0-初始 1-通过 2-重复状态 3-未核销 4核销不通过
     */
    private Integer certStatus;
    /**
     * 0-初始 1-通过 2-不通过
     */
    private Integer logisticsStatus;
    /**
     * 0-初始 1-通过 2无交易状态交易核验失败 3-不通过
     */
    private Integer payStatus;
    /**
     * 申请序列号
     */
    private String applySeqNo;

    /**
     * 业务类型：1-家电，2-家装，3-电动车，4-汽车
     */
    private Integer bizType;

    /**
     * 是否删除
     */
    private Integer isDeleted;
    /**
     * 创建人
     */
    private String createdUser;
    /**
     * 创建时间
     */
    private Date createdTime;
    /**
     * 更新人
     */
    private String updatedUser;
    /**
     * 更新时间
     */
    private Date updatedTime;

    /**
     * 组合参数
     */
    private String combinationParam;

    /**
     * 发票类型 01 家电，02家装
     */
    private String invoiceType;

    private String platCode;
    /**
     * 收货地区编码
     */
    private String addCode;
    /**
     * 收货地址
     */
    private String address;

    /**
     * 资格id
     */
    private String vouchId;

    /**
     * 交易流水组合参数
     */
    private String payCombinationParam;

    /**
     * 交易流水重复id
     */
    private String payRepeatId;

    /**
     * 发票来源类型 家装用
     * FromTypeEnum
     */
    private String fromType;
    /**
     * 重新核验状态
     */
    private Integer reCheckStatus;

    /**
     * 入驻平台统一社会信用代码
     */
    private String topUsciCode;

    /**
     * 预付申请业务流水号
     */
    private String preApplyUuid;

    /**
     * 发票结果集ID
     *
     * @link InvoiceCheckResult
     */
    private String checkResultId;

    /**
     * 商户id
     */
    private String merchId;

    /**
     * 订单金额
     */
    private BigDecimal orderAmount;

    /**
     * 销售平台
     */
    private String platform;

    /**
     * 活动id
     */
    private String actId;

    /**
     * 收货地址一级
     */
    private String addrLv1;

    /**
     * 收货地址二级
     */
    private String addrLv2;


    private String addrLv3;

    /**
     * 收货详细地址
     */
    private String addrLv4;

    /**
     * 规则核验状态 RuleStatusEnum
     */
    private Integer ruleStatus;

    /**
     * 资格对应的消费品类
     */
    private String vouchType;

    /**
     * 商品sn码核验状态 0未核验1核验成功2核验失败
     */
    private Integer goodsSnStatus;

    /**
     * 事前核验状态 null 1通过 2不通过
     */
    private Integer preCheckStatus;

    /**
     * 总的核验状态：
     * 参考 VerifyStatusEnum
     * */
    private Integer verifyStatus;

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
}

