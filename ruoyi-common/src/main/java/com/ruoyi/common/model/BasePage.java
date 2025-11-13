package com.ruoyi.common.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 分页查询请求参数
 */
@Data
@ApiModel(description = "分页请求参数")
public class BasePage {
    /**
     * 默认页码
     */
    public static final int DEFAULT_PAGE_NO=1;
    /**
     * 默认页数量
     */
    public static final int DEFAULT_PAGE_SIZE=10;
    /**
     * 最大页数量
     */
    public static final int MAX_PAGE_SIZE=1000;

    /**
     * 每页数量
     */
    @ApiModelProperty("每页数量")
    @NotNull(message = "pageSize不能为空")
    private int pageSize;

    /**
     * 当前页码
     */
    @ApiModelProperty("当前页码")
    @NotNull(message = "pageNo不能为空")
    private int pageNo;

    public int getPageSize() {
        if(pageSize<=0){
            return DEFAULT_PAGE_SIZE;
        }
        if (pageSize>MAX_PAGE_SIZE) {
            return MAX_PAGE_SIZE;
        }
        return pageSize;
    }

    public int getPageNo() {
        if(pageNo<=0){
            return DEFAULT_PAGE_NO;
        }
        return pageNo;
    }



}
