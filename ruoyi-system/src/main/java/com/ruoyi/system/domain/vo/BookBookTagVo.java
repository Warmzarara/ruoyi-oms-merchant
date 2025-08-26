package com.ruoyi.system.domain.vo;

import com.ruoyi.common.annotation.Excel;
import lombok.Data;

import java.util.List;

@Data
public class BookBookTagVo {

    private Long bookId;

    @Excel(name = "书名")
    private String title;

    @Excel(name = "作者")
    private String author;

    @Excel(name = "出版社")
    private String publisher;

    @Excel(name = "图书编号")
    private String isbn;

    private List<BookTagVo> bookTagVoList;

    /**
     * 拼接后的标签列表字符串，前缀后缀为{}，标签用逗号分隔，用于导出接口
     */
    @Excel(name = "标签")
    private String bookTag;
}
