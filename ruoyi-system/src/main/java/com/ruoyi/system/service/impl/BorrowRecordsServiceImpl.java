package com.ruoyi.system.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.ruoyi.common.core.domain.entity.BorrowRecord;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.system.domain.dto.BorrowRecordEditDto;
import com.ruoyi.system.domain.dto.BorrowRecordSelectDTO;
import com.ruoyi.system.domain.vo.BookBorrowRecordSelectVO;
import com.ruoyi.system.mapper.BorrowRecordMapper;
import com.ruoyi.system.service.IBorrowRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.Date;
import java.util.List;

@Service
public class BorrowRecordsServiceImpl implements IBorrowRecordService {
    @Autowired
    private BorrowRecordMapper borrowRecordMapper;

    @Override
    public List<BorrowRecord> selectBorrowRecord(BorrowRecord bookRecord) {
        return borrowRecordMapper.selectBorrowRecord(bookRecord);
    }

    @Override
    public int addBorrowRecord(BorrowRecord bookRecord) {
        bookRecord.setStatus(0);
        return borrowRecordMapper.addBorrowRecord(bookRecord);
    }

    @Override
    public int editBorrowRecord(BorrowRecordEditDto editDto) {
        //取出参数
        Long borrowRecordId = editDto.getId();
        Date returnDate = editDto.getReturnDate();
        //参数校验
        editParamCheck(editDto,"参数不能为空");
        //更新记录
        BorrowRecord borrowRecord = new BorrowRecord();
        borrowRecord.setId(editDto.getId());
        List<BorrowRecord> borrowRecords = borrowRecordMapper.selectBorrowRecord(borrowRecord);
        if (borrowRecords.isEmpty()) {
            throw new ServiceException("没查询到id为" + borrowRecordId + "的记录",503);
        }
        //如果逾期，更新状态
        Date dueDate = borrowRecords.get(0).getDueDate();
        if (returnDate.getTime() > dueDate.getTime()) {
            editDto.setStatus(1);
        }
        return borrowRecordMapper.editBorrowRecord(editDto);
    }

    private void editParamCheck(BorrowRecordEditDto editDto, String msg) {
        if (StrUtil.isBlank(editDto.getRemark()) && ObjectUtil.isNull(editDto.getReturnDate())) {
            throw new ServiceException(msg, 504);
        }
    }

    public List<BookBorrowRecordSelectVO> selectBookBorrowRecords(BorrowRecordSelectDTO borrowRecordSelectDTO) {
        return borrowRecordMapper.selectBookBorrowRecord(borrowRecordSelectDTO);
    }


}
