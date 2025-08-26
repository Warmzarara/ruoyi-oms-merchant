package com.ruoyi.system.domain;

import lombok.Data;

@Data
public class SysBook {
    private Long bookId;
    private String title;
    private String author;
    private String publisher;
    private String isbn;
}
