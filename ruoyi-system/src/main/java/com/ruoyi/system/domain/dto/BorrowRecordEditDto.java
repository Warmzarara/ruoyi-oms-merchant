package com.ruoyi.system.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class BorrowRecordEditDto {
    @NotNull(message = "归还记录id不能为空")
    private Long id;
    //实际归还时间
    private Date returnDate;
    //备注
    private String remark;
    //是否逾期
    private int status;
}
