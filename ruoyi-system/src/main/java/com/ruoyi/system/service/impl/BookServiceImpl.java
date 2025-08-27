package com.ruoyi.system.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.common.utils.bean.BeanValidators;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.SysBook;
import com.ruoyi.system.domain.SysBookBooktag;
import com.ruoyi.system.domain.dto.BookBooktagDto;
import com.ruoyi.system.domain.dto.BookExcelDto;
import com.ruoyi.system.domain.req.SysBookReq;
import com.ruoyi.system.domain.req.SysBookTagReq;
import com.ruoyi.system.domain.vo.BookBookTagVo;
import com.ruoyi.system.domain.vo.BookTagVo;
import com.ruoyi.system.mapper.BookMapper;
import com.ruoyi.system.mapper.dto.BookTagPair;
import com.ruoyi.system.service.IBookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements IBookService {

    private static final Logger log = LoggerFactory.getLogger(BookServiceImpl.class);

    @Resource
    private BookMapper bookMapper;

    @Autowired
    protected Validator validator;

    @Override
    public void expertBookList(HttpServletResponse response, SysBookReq sysBookReq) {
        List<BookBookTagVo> bookBookTagVos = bookMapper.selectBookList(sysBookReq);
        if (CollectionUtil.isEmpty(bookBookTagVos)) {
            bookBookTagVos = new ArrayList<>();
        }else {
            bookBookTagVos.forEach(book -> {
                String tags = book.getBookTagVoList().stream()
                        .map(BookTagVo::getTagName)
                        .filter(StrUtil::isNotBlank)
                        .collect(Collectors.joining(",", "{", "}"));
                book.setBookTag(tags);
            });
        }
        ExcelUtil<BookBookTagVo> util = new ExcelUtil<>(BookBookTagVo.class);
        util.exportExcel(response, bookBookTagVos, "用户数据");
    }

    @Override
    public List<BookBookTagVo> selectBookList(SysBookReq sysBook) {
        return bookMapper.selectBookList(sysBook);
    }

    /**
     * 导入图书数据
     *
     * @param bookExcelDtoList 图书数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    @Override
    public String importBook(List<BookExcelDto> bookExcelDtoList, Boolean isUpdateSupport, String operName)
    {
        //校验导入数据
        if (StringUtils.isNull(bookExcelDtoList) || bookExcelDtoList.size() == 0)
        {
            throw new ServiceException("导入用户数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (BookExcelDto bookExcelDto : bookExcelDtoList)
        {
            try
            {
                // 验证是否存在这个图书记录
                SysBookReq sysBookReq = new SysBookReq();
                BeanUtils.copyProperties(bookExcelDto, sysBookReq);
                List<BookBookTagVo> bookBookTagVos = bookMapper.selectBookList(sysBookReq);
                if (CollectionUtil.isEmpty(bookBookTagVos)) {
                    BeanValidators.validateWithException(validator, bookExcelDto);
                    SysBook sysBook = new SysBook();
                    BeanUtils.copyProperties(bookExcelDto, sysBook);
                    bookMapper.insertBook(sysBook);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、书名 " + bookExcelDto.getTitle() + " 导入成功");
                }
                else if (isUpdateSupport)
                {
                    BeanValidators.validateWithException(validator, bookExcelDto);
                    SysBook sysBook = new SysBook();
                    sysBook.setBookId(bookBookTagVos.get(0).getBookId());
                    sysBook.setTitle(bookExcelDto.getTitle());
                    sysBook.setAuthor(bookExcelDto.getAuthor());
                    sysBook.setPublisher(bookExcelDto.getPublisher());
                    sysBook.setIsbn(bookExcelDto.getIsbn());
                    bookMapper.updateBook(sysBook);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、书名 " + bookExcelDto.getTitle() + " 更新成功");
                }
                else
                {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、书名 " + bookExcelDto.getTitle() + " 已存在");
                }
            }
            catch (Exception e)
            {
                failureNum++;
                String msg = "<br/>" + failureNum + "、书名 " + bookExcelDto.getTitle() + " 导入失败";
                failureMsg.append(msg + e.getMessage());
                log.error(msg, e);
            }
        }
        if (failureNum > 0)
        {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new ServiceException(failureMsg.toString());
        }
        else
        {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public void insertBookTags(List<SysBookTagReq> sysBookTagReqs) {
        //校验图书id是否存在
        List<Long> bookIds = sysBookTagReqs.stream().map(SysBookTagReq::getBookId).collect(Collectors.toList());
        List<BookBookTagVo> bookBookTagVos = bookMapper.selectBookListByIds(bookIds);
        //todo 这里的错误信息太简单，要求返回那些书不存在
        if (CollectionUtil.isEmpty(bookBookTagVos)) {
            throw new ServiceException("系统数据异常");
        }
        BookBooktagDto bookBooktagDto = new BookBooktagDto();
        bookBooktagDto.setBookIdList(bookIds);
        List<SysBookBooktag> sysBookBooktags = bookMapper.selectBookBooktagList(bookBooktagDto);

        //todo 这里contains可以优化，流可以拆分为筛选和优化两批处理
        //将嵌套结构扁平化，插入到关系表
        List<SysBookBooktag> sysBookBooktagList = sysBookTagReqs.stream()
                .flatMap(req -> req.getTagIds().stream()
                        .map(tagId -> {
                            SysBookBooktag sysBookBooktag = new SysBookBooktag();
                            sysBookBooktag.setBookId(req.getBookId());
                            sysBookBooktag.setBooktagId(tagId);
                            sysBookBooktag.setStatus(1);
                            return sysBookBooktag;
                        }))
                .filter(bookBooktag -> !sysBookBooktags.contains(bookBooktag))
                .collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(sysBookBooktagList)) {
            bookMapper.insertBookBooktag(sysBookBooktagList);
        }else {
            log.info("没有新增的标签");
        }
    }

}
