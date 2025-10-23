package com.ruoyi.system.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.Date;

/**
 * 商户申请单实体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MerchantApply {
    private Long id;                // 申请单ID
    private String applyNo;         // 申请单编号
    private String merchantName;    // 商户名称
    private Date applyTime;         // 申请时间
    private String applyStatus;     // 申请状态：draft, pending, approved, rejected, finished, exported
    private String remark;          // 备注
}