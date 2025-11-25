package com.ruoyi.merchant.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("T_SHIPMENT")
public class Shipment {
    /** 唯一键 */
    private String id;
    /** 订单id */
    private String orderId;
    /** 快递单号 */
    private String logisticsNo;
    /** 物流公司名称 */
    private String logisticsCompany;
    /** 发货时间 */
    private LocalDateTime shipTime;
    /** 发货状态 0=待发货,1=已发货,2=已签收 */
    private Integer status;
    /** 签收时间 */
    private LocalDateTime deliveryTime;
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
