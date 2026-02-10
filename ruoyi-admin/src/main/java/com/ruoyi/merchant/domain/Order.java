package com.ruoyi.merchant.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("T_ORDER")
public class Order {
    /** 唯一键 */
    private String id;
    /** 用户id */
    private String customerId;
    /** 收货地址id */
    private String addressId;
    /** 订单状态 1:订单创建*/
    private Integer status;
    /** 订单总金额 */
    private BigDecimal totalPrice;
    /** 订单物品总数 */
    private Integer totalQuantity;
    /** 订单备注 */
    private String remark;
    /** 支付时间 */
    private Date payTime;
    /** 支付方式 */
    private String payType;
    /** 发货时间 */
    private Date shipTime;
    /** 是否删除 */
    private Integer isDeleted;
    /** 创建人 */
    private String createdUser;
    /** 创建时间 */
    private Date createdTime;
    /** 更新人 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String updatedUser;
    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedTime;
    
    private String receiverPhone;
    private String receiverName;
    private String receiverAddress;
}
