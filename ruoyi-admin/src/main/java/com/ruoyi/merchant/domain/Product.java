package com.ruoyi.merchant.domain;

import lombok.Data;

import java.time.LocalDateTime;
import java.math.BigDecimal;

@Data
public class Product {
    /** 唯一键 */
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
    /** 创建时间 */
    private LocalDateTime createdTime;
    /** 更新人 */
    private String updatedUser;
    /** 更新时间 */
    private LocalDateTime updatedTime;
}
