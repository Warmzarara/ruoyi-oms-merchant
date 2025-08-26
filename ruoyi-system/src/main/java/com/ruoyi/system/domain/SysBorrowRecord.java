package com.ruoyi.system.domain;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SysBorrowRecord {
    private Long borrowRecordId;
    private Long bookId;
    private String borrower;
    private LocalDateTime borrow_date;
    private LocalDateTime return_date;
    private String remark;
}
