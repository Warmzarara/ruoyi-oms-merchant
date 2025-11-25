package com.ruoyi.merchant.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;
import java.math.BigDecimal;

@Data
@TableName("T_ORDER")
public class Order {
    /** 唯一键 */
    private String id;
    /** 用户id */
    private String userId;
    /** 收货地址id */
    private String addressId;
    /** 订单状态 */
    private Integer status;
    /** 订单总金额 */
    private BigDecimal totalPrice;
    /** 订单物品总数 */
    private Integer totalQuantity;
    /** 订单备注 */
    private String remark;
    /** 支付时间 */
    private LocalDateTime payTime;
    /** 支付方式 */
    private Integer payType;
    /** 发货时间 */
    private LocalDateTime shipTime;
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
