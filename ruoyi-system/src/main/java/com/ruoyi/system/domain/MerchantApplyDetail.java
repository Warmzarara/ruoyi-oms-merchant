package com.ruoyi.system.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 商户申请明细实体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MerchantApplyDetail {
    private Long id;                // 明细ID
    private Long applyId;           // 对应申请单ID
    private String consumerName;    // 消费者姓名（可能加密）
    private String consumerNumber;  // 消费者手机号（可能加密）
    private String goodsTypeCode;   // 品类编码
    private BigDecimal applyAmount; // 申请金额
    private Date applyDate;         // 申请日期
}