package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.SysBook;
import com.ruoyi.system.domain.req.SysBookReq;
import com.ruoyi.system.domain.vo.BookBookTagVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface BookMapper {
    List<BookBookTagVo> selectBookList(SysBookReq sysBook);
    int insertBook(SysBook sysBook);
    int updateBook(SysBook sysBook);
}
