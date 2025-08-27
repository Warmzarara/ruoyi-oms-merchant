package com.ruoyi.system.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
public class SysBookBooktag {
    private Long bookBooktagId;
    private Long bookId;
    private Long booktagId;
    private int status;
}
