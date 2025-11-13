package com.ruoyi.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 商品分类表
 *
 */
@Data
@TableName("E_GOODS_TYPE")
public class GoodsType {

    /**
     * 唯一标识
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 分类名称
     */
    private String typeName;

    /**
     * 分类编码
     */
    private String typeCode;

    /**
     * 活动编码
     * */
    private String activityCode;

    /**
     * 粤焕新编码
     * */
    private String yhxTypeCode;

    /**
     * 粤焕新品类名称
     * */
    private String yhxTypeName;

    /**
     * 发票规范商品简称 品类对应多个时用英文逗号分隔
     */
    private String shortName;

    /**
     * 发票关键词
     */
    private String keyWord;

    /**
     * 上级分类
     */
    private String parentId;

    /**
     * 是否删除
     */
    private Integer isDeleted;

    /**
     * 创建人
     */
    private String createdUser;

    /**
     * 创建时间
     */
    private Date createdTime;

    /**
     * 更新人
     */
    private String updatedUser;

    /**
     * 更新时间
     */
    private Date updatedTime;

    /**
     * 业务类型
     */
    private Integer bizType;

}
