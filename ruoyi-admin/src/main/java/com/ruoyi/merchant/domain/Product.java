package com.ruoyi.merchant.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("T_PRODUCT")
public class Product {
    /** 唯一键 */
    @TableId
    private String id;
    /** 商品标题 */
    private String title;
    /** 商品描述 */
    private String description;
    /** 单价 */
    private BigDecimal price;
    /** 库存数量 */
    private Integer stock;
    /** 状态 0=已下架 1=上架 */
    private Integer status;
    /** 是否删除 */
    private Integer isDeleted;
    /** 创建人 */
    private String createdUser;
    /** 缩略图链接 */
    private String thumbnail;
    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;
    /** 更新人 */
    private String updatedUser;
    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedTime;
}
