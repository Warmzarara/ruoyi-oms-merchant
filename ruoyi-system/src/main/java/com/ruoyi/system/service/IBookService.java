package com.ruoyi.system.service;

import com.ruoyi.system.domain.SysBook;
import com.ruoyi.system.domain.dto.BookExcelDto;
import com.ruoyi.system.domain.req.SysBookReq;
import com.ruoyi.system.domain.vo.BookBookTagVo;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface IBookService {
    void expertBookList(HttpServletResponse response, SysBookReq sysBookReq);
    List<BookBookTagVo> selectBookList(SysBookReq sysBook);
    String importBook(List<BookExcelDto> dto, Boolean isUpdateSupport, String operName);
}
