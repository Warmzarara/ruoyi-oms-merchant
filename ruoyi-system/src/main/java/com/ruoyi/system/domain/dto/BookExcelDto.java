package com.ruoyi.system.domain.dto;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.system.domain.vo.BookTagVo;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class BookExcelDto {
    @NotBlank(message = "书名不能为空")
    @Excel(name = "书名")
    private String title;

    @NotBlank(message = "作者不能为空")
    @Excel(name = "作者")
    private String author;

    @NotBlank(message = "出版社不能为空")
    @Excel(name = "出版社")
    private String publisher;

    @NotBlank(message = "图书编号不能为空")
    @Excel(name = "图书编号")
    private String isbn;

    /**
     * 拼接后的标签列表字符串，前缀后缀为{}，标签用逗号分隔，用于导出接口
     */
    @Excel(name = "标签",type = Excel.Type.EXPORT)
    private String bookTag;
}
