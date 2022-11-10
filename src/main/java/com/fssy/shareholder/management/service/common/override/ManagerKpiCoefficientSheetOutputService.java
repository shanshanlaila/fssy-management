/**
 * ------------------------修改日志---------------------------------
 * 修改人       修改日期         修改内容
 */
package com.fssy.shareholder.management.service.common.override;

import com.fssy.shareholder.management.service.common.SheetOutputService;
import com.fssy.shareholder.management.service.common.SheetService;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Map;

/**
 * @author zzp
 * @ClassName: ManageKpiCoefficientSheetOutputService
 * @Description: 经理人项目难度系数导出excel模板
 * @date 2022/10/21 0021 14:30
 */
public class ManagerKpiCoefficientSheetOutputService extends SheetOutputService {
    /**
     * 重写自定义表头
     * @param sheet excel的工作表名称
     * @param list  数据源
     * @param <T>
     * @return
     */
    @Override
    public <T> int customizedHead(XSSFSheet sheet, List<T> list){
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
        // 加边框
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        // 自动换行
        style.setWrapText(true);
        // 设置日期样式
        XSSFCellStyle dateStyle = style.copy();
        CreationHelper createHelper = wb.getCreationHelper();
        short dateFormat = createHelper.createDataFormat()
                .getFormat("yyyy-MM-dd");
        dateStyle.setDataFormat(dateFormat);

        //1.设置合并单元格
        //标题
        CellRangeAddress titleRange = new CellRangeAddress(0,0,
                SheetService.columnToIndex("A"),
                SheetService.columnToIndex("G"));

        //副标题：企业名称
        CellRangeAddress getCompanyName = new CellRangeAddress(1,1,
                SheetService.columnToIndex("B"),
                SheetService.columnToIndex("D"));




        sheet.addMergedRegion(getCompanyName);
        sheet.addMergedRegion(titleRange);

        // 2.写内容
        // 获取数据
        @SuppressWarnings("unchecked")
        Map<String, Object> data = (Map<String, Object>) list.get(0);
        // 标题
        XSSFCell titleCell = sheet.createRow(0)
                .createCell(SheetService.columnToIndex("A"));
        titleCell.setCellStyle(style);
        titleCell.setCellValue("经理人项目难度系数报表");
        //填报企业
        XSSFRow twoRow = sheet.createRow(1);
        XSSFCell getCompanyNameCell = twoRow
                .createCell(SheetService.columnToIndex("A"));
        getCompanyNameCell.setCellStyle(style);
        getCompanyNameCell.setCellValue("填报企业");
        //填报企业
        XSSFCell setCompanyNameCell = twoRow
                .createCell(SheetService.columnToIndex("B"));
        setCompanyNameCell.setCellStyle(style);
        setCompanyNameCell.setCellValue(ObjectUtils.isEmpty(data.get("companyName")) ? ""
                :data.get("companyName").toString());
        //年份
        XSSFCell yearCell = twoRow
                .createCell(SheetService.columnToIndex("E"));
        yearCell.setCellStyle(style);
        yearCell.setCellValue("年份");
        //年份
        XSSFCell setYearCell = twoRow
                .createCell(SheetService.columnToIndex("F"));
        setYearCell.setCellStyle(style);
        setYearCell.setCellValue(ObjectUtils.isEmpty(data.get("year")) ? ""
                :data.get("year").toString());

        // 3.设置合并单元格边框
        setRegionBorder(titleRange, sheet);
        setRegionBorder(getCompanyName, sheet);
        //从excel表中的第2行开始读
        return 2;
    }
}