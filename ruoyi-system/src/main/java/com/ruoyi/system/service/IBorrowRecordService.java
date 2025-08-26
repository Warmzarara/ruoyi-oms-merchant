package com.ruoyi.system.service;

import com.ruoyi.common.core.domain.entity.BorrowRecord;
import com.ruoyi.system.domain.dto.BorrowRecordEditDto;
import com.ruoyi.system.domain.dto.BorrowRecordSelectDTO;
import com.ruoyi.system.domain.vo.BookBorrowRecordSelectVO;

import java.util.List;

public interface IBorrowRecordService {
    List<BorrowRecord> selectBorrowRecord(BorrowRecord bookRecord);

    int addBorrowRecord(BorrowRecord bookRecord);

    int editBorrowRecord(BorrowRecordEditDto editDto);

    List<BookBorrowRecordSelectVO> selectBookBorrowRecords(BorrowRecordSelectDTO borrowRecordSelectDTO);
}
