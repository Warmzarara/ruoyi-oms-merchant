package com.ruoyi.system.mapper.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BookTagPair {
    private Long bookId;
    private Long tagId;
}
