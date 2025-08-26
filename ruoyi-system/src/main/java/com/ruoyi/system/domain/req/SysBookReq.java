package com.ruoyi.system.domain.req;

import lombok.Data;

@Data
public class SysBookReq {
    //书名
    private String title;
    //作者
    private String author;
    //标签
    private String bookTag;
}
