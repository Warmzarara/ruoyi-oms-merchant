package com.ruoyi.system.mapper;


import com.ruoyi.common.core.domain.entity.BorrowRecord;
import com.ruoyi.system.domain.dto.BorrowRecordEditDto;
import com.ruoyi.system.domain.dto.BorrowRecordSelectDTO;
import com.ruoyi.system.domain.vo.BookBorrowRecordSelectVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BorrowRecordMapper {
    List<BorrowRecord> selectBorrowRecord(BorrowRecord borrowRecord);

    int addBorrowRecord(BorrowRecord borrowRecord);

    int editBorrowRecord(BorrowRecordEditDto editDto);

    List<BookBorrowRecordSelectVO> selectBookBorrowRecord(BorrowRecordSelectDTO borrowRecordSelectDTO);
}
