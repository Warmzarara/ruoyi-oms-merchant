package com.ruoyi.web.controller.borrow;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.BorrowRecord;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.dto.BookExcelDto;
import com.ruoyi.system.domain.dto.BorrowRecordEditDto;
import com.ruoyi.system.domain.req.SysBookReq;
import com.ruoyi.system.domain.req.SysBookTagReq;
import com.ruoyi.system.domain.vo.BookBookTagVo;
import com.ruoyi.system.service.IBookService;
import com.ruoyi.system.service.IBorrowRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 图书租借记录
 */
@RestController
@RequestMapping("/borrow")
public class BorrowController extends BaseController {

    @Autowired
    private IBorrowRecordService borrowRecordService;

    @Resource
    private IBookService bookService;

    @GetMapping("/list")
    public TableDataInfo getBorrowList(BorrowRecord borrowRecord) {
        startPage();
        List<BorrowRecord> borrowRecords = borrowRecordService.selectBorrowRecord(borrowRecord);
        return getDataTable(borrowRecords);
    }

    @PostMapping("/addBorrowRecord")
    public AjaxResult addBorrowRecord(@Validated @RequestBody BorrowRecord borrowRecord) {
        return toAjax(borrowRecordService.addBorrowRecord(borrowRecord));
    }

    @PostMapping("/editBorrowRecord")
    public AjaxResult editBorrowRecord(@Validated @RequestBody BorrowRecordEditDto editDto) {
        return toAjax(borrowRecordService.editBorrowRecord(editDto));
    }

    @GetMapping("/getBookBorrowRecord")
    public TableDataInfo getBookBorrowRecord() {
        return null;
    }

    @GetMapping("/getBookList")
    public TableDataInfo getBookList(SysBookReq sysBook) {
        startPage();
        List<BookBookTagVo> bookBookTagVos = bookService.selectBookList(sysBook);
        return getDataTable(bookBookTagVos);
    }

    @PostMapping("/exportBookList")
    public void export(HttpServletResponse response, SysBookReq book){
        System.out.println("aaa");
        System.out.println("aaa");
        System.out.println("aaa");
        bookService.expertBookList(response,book);
        AjaxResult.success();
    }

    @PostMapping("/importBookTemplate")
    public void importBookTemplate(HttpServletResponse response)
    {
        ExcelUtil<BookExcelDto> util = new ExcelUtil<BookExcelDto>(BookExcelDto.class);
        util.importTemplateExcel(response, "用户数据");
    }

    @Log(title = "图书管理", businessType = BusinessType.IMPORT)
//    @PreAuthorize("@ss.hasPermi('system:user:import')")
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception
    {
        ExcelUtil<BookExcelDto> util = new ExcelUtil<BookExcelDto>(BookExcelDto.class);
        List<BookExcelDto> bookExcelDtos = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = bookService.importBook(bookExcelDtos, updateSupport, operName);
        return success(message);
    }

    @PostMapping("/updateBookTag")
    public AjaxResult updateBookTag(@Validated @RequestBody List<SysBookTagReq> sysBookTagReqList) {
        bookService.insertBookTags(sysBookTagReqList);
        return AjaxResult.success();
    }
}
