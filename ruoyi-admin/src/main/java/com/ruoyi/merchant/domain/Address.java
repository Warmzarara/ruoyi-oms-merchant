package com.ruoyi.merchant.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("T_ADDRESS")
public class Address {
    /** 唯一键 */
    private String id;
    /** 用户id */
    private String userId;
    /** 收件人名称 */
    private String recipientName;
    /** 收件人电话 */
    private String phone;
    /** 收件人地址 */
    private String address;
    /** 是否为默认地址 */
    private Integer isDefault;
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
