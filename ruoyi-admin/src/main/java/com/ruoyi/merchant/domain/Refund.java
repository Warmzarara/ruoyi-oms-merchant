package com.ruoyi.merchant.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;
import java.math.BigDecimal;

@Data
@TableName("T_REFUND")
public class Refund {
    /** 唯一键 */
    private String id;
    /** 订单id */
    private String orderId;
    /** 退货原因 */
    private String reason;
    /** 退货状态 0=申请中, 1=已通过, 2=已拒绝, 3=已退款 */
    private Integer status;
    /** 申请时间 */
    private LocalDateTime applyTime;
    /** 退货金额 */
    private BigDecimal refundAmount;
    /** 商家审核时间 */
    private LocalDateTime reviewTime;
    /** 商家审核备注 */
    private String reviewRemark;
    /** 是否删除 */
    private Integer isDeleted;
    /** 创建人 */
    private String createdUser;
    /** 创建时间 */
    private LocalDateTime createdTime;
    /** 更新人 */
    private String updatedUser;
    /** 更新时间 */
    private LocalDateTime updatedTime;
}
