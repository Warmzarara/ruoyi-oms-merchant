package com.ruoyi.system.domain.dto;

import lombok.Data;

import java.util.List;

@Data
public class BookBooktagDto {
    private Long bookBooktagId;
    private Long tagId;
    private Long bookId;
    private List<Long> bookIdList;
}
