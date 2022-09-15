package com.fssy.shareholder.management.service.common.override;

import com.fssy.shareholder.management.service.common.SheetOutputService;
import com.fssy.shareholder.management.service.common.SheetService;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * @author QinYanZhou
 * @title: MaterialDataSheetOutPutService
 * @description: 物料表导出业务类
 * @date 2021/11/25
 */
public class MaterialDataSheetOutPutService extends SheetOutputService
{

    @Override
    public <T> int customizedHead(XSSFSheet sheet, List<T> list)
    {
        // 获取workbook
        XSSFWorkbook wb = sheet.getWorkbook();
        // 设置样式
        XSSFCellStyle style = wb.createCellStyle();
        // 字体格式
        XSSFFont font = wb.createFont();
        font.setBold(true);
        style.setFont(font);
        // 居中
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        // 自动换行
        style.setWrapText(true);
        // 设置边框
        setBorder(style, sheet);
        // 设置日期样式
        XSSFCellStyle dateStyle = style.copy();
        CreationHelper createHelper = wb.getCreationHelper();
        short dateFormat = createHelper.createDataFormat()
                .getFormat("yyyy-MM-dd");
        dateStyle.setDataFormat(dateFormat);
        // 1. 设置合并单元格
        // 标题  //0-1指列，A-R指宽
        CellRangeAddress titleRange = new CellRangeAddress(0, 0,
                SheetService.columnToIndex("A"),
                SheetService.columnToIndex("R"));
        //通知单
        CellRangeAddress titleNotice = new CellRangeAddress(1, 1,
                SheetService.columnToIndex("A"),
                SheetService.columnToIndex("Q"));
        //无作用空白
//        CellRangeAddress titleBlank = new CellRangeAddress(1, 2,
//                SheetService.columnToIndex("R"),
//                SheetService.columnToIndex("R"));
        //产品量产名称
        CellRangeAddress titleName = new CellRangeAddress(2, 2,
                SheetService.columnToIndex("A"),
                SheetService.columnToIndex("J"));
        //产品量产名称的空白格
        CellRangeAddress titleNameBlank = new CellRangeAddress(2, 2,
                SheetService.columnToIndex("K"),
                SheetService.columnToIndex("R"));
        //form 表单编号
        CellRangeAddress titleForm = new CellRangeAddress(3, 3,
                SheetService.columnToIndex("A"),
                SheetService.columnToIndex("J"));
        //填表日期
        CellRangeAddress titleDate = new CellRangeAddress(3, 3,
                SheetService.columnToIndex("K"),
                SheetService.columnToIndex("R"));
        //接受单位
        CellRangeAddress titleUnit = new CellRangeAddress(4, 4,
                SheetService.columnToIndex("A"),
                SheetService.columnToIndex("R"));
        //项目名称
        CellRangeAddress titleProject = new CellRangeAddress(5, 5,
                SheetService.columnToIndex("A"),
                SheetService.columnToIndex("R"));
        //编制人名称
        CellRangeAddress titleFormaction = new CellRangeAddress(6, 6,
                SheetService.columnToIndex("A"),
                SheetService.columnToIndex("C"));
        //编制人名称空白行
//        CellRangeAddress titleFormactionBlank = new CellRangeAddress(6, 6,
//                SheetService.columnToIndex("D"),
//                SheetService.columnToIndex("D"));
        //审批人名称
        CellRangeAddress titleCofig = new CellRangeAddress(6, 6,
                SheetService.columnToIndex("E"),
                SheetService.columnToIndex("R"));
        //接受确认
        CellRangeAddress titleAffirm = new CellRangeAddress(7, 8,
                SheetService.columnToIndex("A"),
                SheetService.columnToIndex("R"));
        sheet.addMergedRegion(titleRange);
        sheet.addMergedRegion(titleNotice);
//        sheet.addMergedRegion(titleBlank);
        sheet.addMergedRegion(titleDate);
        sheet.addMergedRegion(titleProject);
        sheet.addMergedRegion(titleUnit);
        sheet.addMergedRegion(titleNameBlank);
        sheet.addMergedRegion(titleName);
        sheet.addMergedRegion(titleFormaction);
//        sheet.addMergedRegion(titleFormactionBlank);
        sheet.addMergedRegion(titleCofig);
        sheet.addMergedRegion(titleAffirm);
        sheet.addMergedRegion(titleForm);

        // 2.写内容
        // 获取数据
        @SuppressWarnings("unchecked")
        Map<String, Object> data = (Map<String, Object>) list.get(0);
        //获取当前日期
        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        // 标题
        XSSFRow title1Row = sheet.createRow(0);
        XSSFCell  titleCell = title1Row.createCell(SheetService.columnToIndex("A"));
        titleCell.setCellStyle(style);
        titleCell.setCellValue("柳州裕信方盛汽车饰件有限公司");
        //通知单
        XSSFRow thirdRow = sheet.createRow(1);
        XSSFCell NoticeCell = thirdRow.createCell(SheetService.columnToIndex("A"));
        NoticeCell.setCellStyle(style);
        NoticeCell.setCellValue("产品量产/材料规格更新 通知单");
        //通知单的空白格
        XSSFCell thirBlank = thirdRow.createCell(SheetService.columnToIndex("R"));
        thirBlank.setCellStyle(style);
        thirBlank.setCellValue("");
        //产品产量变更
        XSSFRow productCell = sheet.createRow(2);
        XSSFCell productRow = productCell.createCell(SheetService.columnToIndex("A"));
        productRow.setCellStyle(style);
            productRow.setCellValue("产品量产      材料规格变更");
        //产品产量变更的空白格
        XSSFCell productBlankRow = productCell.createCell(SheetService.columnToIndex("K"));
        productBlankRow.setCellStyle(style);
        productBlankRow.setCellValue("");
        //表单编号
        XSSFRow formCell = sheet.createRow(3);
        XSSFCell formRow = formCell.createCell(SheetService.columnToIndex("A"));
        formRow.setCellStyle(style);
        formRow.setCellValue("表单编号：SWXPL" + date.format(formatter));
        //填表日期
        XSSFCell formDateRow = formCell.createCell(SheetService.columnToIndex("K"));
        formDateRow.setCellStyle(style);
        formDateRow.setCellValue("填表日期：" + date.format(formatter));
        //接受单位
        XSSFRow unitCell = sheet.createRow(4);
        XSSFCell unitRow = unitCell.createCell(SheetService.columnToIndex("A"));
        unitRow.setCellStyle(style);
        unitRow.setCellValue("接受单位：物控部");
        //项目名称
        XSSFRow projectCell = sheet.createRow(5);
        XSSFCell projectRow = projectCell.createCell(SheetService.columnToIndex("A"));
        projectRow.setCellStyle(style);
        projectRow.setCellValue("项目名称：");
        //编制
        XSSFRow FormactionCell = sheet.createRow(6);
        XSSFCell FormactionRow = FormactionCell.createCell(SheetService.columnToIndex("A"));
        FormactionRow.setCellStyle(style);
        FormactionRow.setCellValue("编制：杨思绿");
        //编制中空白行
        XSSFCell FormactionBlankRow = FormactionCell.createCell(SheetService.columnToIndex("D"));
        FormactionBlankRow.setCellStyle(style);
        FormactionBlankRow.setCellValue("");
        //审批
        XSSFCell CofigRow = FormactionCell.createCell(SheetService.columnToIndex("E"));
        CofigRow.setCellStyle(style);
        CofigRow.setCellValue("审批：何作然");
        //接受确认
        XSSFRow AffirmCell = sheet.createRow(7);
        XSSFCell AffirmRow = AffirmCell.createCell(SheetService.columnToIndex("A"));
        AffirmRow.setCellStyle(style);
        AffirmRow.setCellValue("接受确认：                                外购物料  月  日前如出现紧缺，由采购部负责处理。");

        // 3.设置合并单元格边框
        setRegionBorder(titleRange, sheet);
        setRegionBorder(titleNotice, sheet);
//        setRegionBorder(titleBlank, sheet);
        setRegionBorder(titleDate, sheet);
        setRegionBorder(titleProject, sheet);
        setRegionBorder(titleUnit, sheet);
        setRegionBorder(titleNameBlank, sheet);
        setRegionBorder(titleName, sheet);
        setRegionBorder(titleFormaction, sheet);
//        setRegionBorder(titleFormactionBlank, sheet);
        setRegionBorder(titleCofig, sheet);
        setRegionBorder(titleAffirm, sheet);
        setRegionBorder(titleForm, sheet);


        return 8+1;


    }
}
