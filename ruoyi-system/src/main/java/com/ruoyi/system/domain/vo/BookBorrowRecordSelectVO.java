package com.ruoyi.system.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class BookBorrowRecordSelectVO {
    private Long borrowRecordId;
    private String title;
    private String author;
    private String publisher;
    private String isbn;
    private String bookId;
    private String borrower;
    private LocalDateTime borrowDate;
    private LocalDateTime returnDate;
    private String remark;
}
