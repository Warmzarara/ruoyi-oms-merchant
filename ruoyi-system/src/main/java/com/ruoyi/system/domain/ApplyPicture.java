package com.ruoyi.system.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 申请图片实体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplyPicture {
    private Long id;                // 主键ID
    private Long applyDetailId;     // 对应明细ID
    private String pictureUrl;      // 图片URL
}