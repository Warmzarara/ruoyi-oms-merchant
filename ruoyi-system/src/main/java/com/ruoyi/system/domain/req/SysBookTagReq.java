package com.ruoyi.system.domain.req;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class SysBookTagReq {
    @NotNull(message = "图书id不能为空")
    private Long bookId;
    @NotNull(message = "新增标签不能为空")
    private List<Long> tagIds;
}
