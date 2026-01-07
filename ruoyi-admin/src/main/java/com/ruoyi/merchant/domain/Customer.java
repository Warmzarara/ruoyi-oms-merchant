package com.ruoyi.merchant.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("T_CUSTOMER")
public class Customer {
    /** 唯一键 */
    private String id;
    /** 用户名 */
    private String userName;
    /** 性别 0=未知 1=男 2=女 */
    private Integer gender;
    /** 头像地址 */
    private String avatarUrl;
    /** 电话号码 */
    private String phoneNumber;
    /** 邮箱地址 */
    private String email;
    /** 用户状态 0=正常 1=异常 */
    private Integer userStatus;
    /** 是否删除 */
    private Integer isDeleted;
    /** 创建人 */
    private String createdUser;
    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdTime;
    /** 更新人 */
    private String updatedUser;
    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedTime;
    /** 客户类型 */
    private Integer customerType;
}
