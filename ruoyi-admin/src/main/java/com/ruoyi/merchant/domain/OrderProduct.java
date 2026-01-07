package com.ruoyi.merchant.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("T_ORDER_PRODUCT")
public class OrderProduct {
    /** 唯一键 */
    private String id;
    /** 商品id */
    private String productId;
    /** 商品标题快照 */
    private String productNameSnapshot;
    /** 订单id */
    private String orderId;
    /** 相同商品总数 */
    private Integer productQuantity;
    /** 下单时商品单价 */
    private BigDecimal productPriceSnapshot;
    /** 是否删除 */
    private Integer isDeleted;
    /** 创建人 */
    private String createdUser;
    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;
    /** 更新人 */
    private String updatedUser;
    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedTime;
}
