package com.ruoyi.merchant.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("T_PRODUCT_IMAGE")
public class ProductImage {
    /** 唯一键 */
    private String id;
    /** 商品id */
    private String productId;
    /** 图片链接 */
    private String imageUrl;
    /** 是否为列表展示图片 */
    private Integer isDisplay;
    /** 图片展示顺序 */
    private Integer displayOrder;
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
