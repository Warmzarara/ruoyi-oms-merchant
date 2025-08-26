package com.ruoyi.common.core.domain.entity;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class BorrowRecord {
    //主键id
    private Long id;
    //书名
    @NotBlank(message = "书名不能为空")
    private String bookName;
    //借出者
    @NotBlank(message = "借出者不能为空")
    private String borrowerName;
    //借出时间
    @NotNull(message = "借出时间不能为空")
    private Date borrowDate;
    //归还时间
//    @NotNull(message = "归还时间不能为空")
    private Date returnDate;
    //应该归还时间
    @NotNull(message = "应该归还时间不能为空")
    private Date dueDate;
    //当前状态
    private int status;
    //备注
    private String remark;
}
