package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.SysBook;
import com.ruoyi.system.domain.SysBookBooktag;
import com.ruoyi.system.domain.dto.BookBooktagDto;
import com.ruoyi.system.domain.req.SysBookReq;
import com.ruoyi.system.domain.vo.BookBookTagVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.awt.print.Book;
import java.util.List;


@Mapper
public interface BookMapper {
    List<BookBookTagVo> selectBookList(SysBookReq sysBook);
    List<BookBookTagVo> selectBookListByIds(@Param("bookIds") List<Long> bookIds);
    int insertBook(SysBook sysBook);
    int updateBook(SysBook sysBook);
    int deleteBookById(SysBookBooktag sysbookbooktag);
    List<SysBookBooktag> selectBookBooktagList(BookBooktagDto bookbooktagdto);
    int insertBookBooktag(@Param("sysBookBooktagList") List<SysBookBooktag> sysBookBooktagList);
}
