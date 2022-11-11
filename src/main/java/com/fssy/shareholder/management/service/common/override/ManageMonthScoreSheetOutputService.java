/**
 * ------------------------修改日志---------------------------------
 * 修改人       修改日期         修改内容
 */
package com.fssy.shareholder.management.service.common.override;

import com.fssy.shareholder.management.service.common.SheetOutputService;
import com.fssy.shareholder.management.service.common.SheetService;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Map;

/**
 * @author Shizn
 * @ClassName: ManageMonthScoreSheetOutputService
 * @Description: 激励约束项目评分表 导出excel模板
 * @date 2022/10/21 0021 14:30
 */
public class ManageMonthScoreSheetOutputService extends SheetOutputService {
    /**
     * 重写自定义表头
     *
     * @param sheet excel的工作表名称
     * @param list  数据源
     * @param <T>
     * @return
     */
    @Override
    public <T> int customizedHead(XSSFSheet sheet, List<T> list) {
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

        //1.设置合并单元格
        //标题
        CellRangeAddress titleRange = new CellRangeAddress(0, 0,
                SheetService.columnToIndex("A"),
                SheetService.columnToIndex("M"));
        CellRangeAddress getCompanyName = new CellRangeAddress(1, 1,
                SheetService.columnToIndex("B"),
                SheetService.columnToIndex("C"));
        CellRangeAddress getBlank = new CellRangeAddress(1, 1,
                SheetService.columnToIndex("H"),
                SheetService.columnToIndex("M"));




        sheet.addMergedRegion(getCompanyName);
        sheet.addMergedRegion(titleRange);
        sheet.addMergedRegion(getBlank);

        // 2.写内容
        // 获取数据
        @SuppressWarnings("unchecked")
        Map<String, Object> data = (Map<String, Object>) list.get(0);
        // 标题
        XSSFCell titleCell = sheet.createRow(0)
                .createCell(SheetService.columnToIndex("A"));
        titleCell.setCellStyle(style);
        titleCell.setCellValue("激励约束项目评分表");
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
                : data.get("companyName").toString());
        //年份
        XSSFCell yearCell = twoRow
                .createCell(SheetService.columnToIndex("D"));
        yearCell.setCellStyle(style);
        yearCell.setCellValue("年份");
        //年份
        XSSFCell setYearCell = twoRow
                .createCell(SheetService.columnToIndex("E"));
        setYearCell.setCellStyle(style);
        setYearCell.setCellValue(ObjectUtils.isEmpty(data.get("year")) ? ""
                : data.get("year").toString());
        //填报月份
        XSSFCell monthCell = twoRow
                .createCell(SheetService.columnToIndex("F"));
        monthCell.setCellStyle(style);
        monthCell.setCellValue("月份");
        //月份
        XSSFCell setMonthCell = twoRow
                .createCell(SheetService.columnToIndex("G"));
        setMonthCell.setCellStyle(style);
        setMonthCell.setCellValue(ObjectUtils.isEmpty(data.get("month")) ? ""
                : data.get("month").toString());
        // 序号
        XSSFRow threeRow = sheet.createRow(2);
        XSSFCell idCell = threeRow
                .createCell(SheetService.columnToIndex("A"));
        idCell.setCellStyle(style);
        idCell.setCellValue("序号");
        // 指标类别
        XSSFCell projectTypeCell = threeRow
                .createCell(SheetService.columnToIndex("B"));
        projectTypeCell.setCellStyle(style);
        projectTypeCell.setCellValue("指标类别");
        //指标名称
        XSSFCell projectDescCell = threeRow
                .createCell(SheetService.columnToIndex("C"));
        projectDescCell.setCellStyle(style);
        projectDescCell.setCellValue("指标名称");
        //单位
        XSSFCell unitCell = threeRow
                .createCell(SheetService.columnToIndex("D"));
        unitCell.setCellStyle(style);
        unitCell.setCellValue("单位");
        // 基本目标
        XSSFCell basicTargetCell = threeRow
                .createCell(SheetService.columnToIndex("E"));
        basicTargetCell.setCellStyle(style);
        basicTargetCell.setCellValue("基本目标");
        //必达目标
        XSSFCell mustInputTargetCell = threeRow
                .createCell(SheetService.columnToIndex("F"));
        mustInputTargetCell.setCellStyle(style);
        mustInputTargetCell.setCellValue("必达目标");
        //达标目标
        XSSFCell reachTargetCell = threeRow
                .createCell(SheetService.columnToIndex("G"));
        reachTargetCell.setCellStyle(style);
        reachTargetCell.setCellValue("达标目标");
        // 挑战目标
        XSSFCell challengeTargetCell = threeRow
                .createCell(SheetService.columnToIndex("H"));
        challengeTargetCell.setCellStyle(style);
        challengeTargetCell.setCellValue("挑战目标");
        // 月份目标
        XSSFCell JuneCell = threeRow
                .createCell(SheetService.columnToIndex("I"));
        JuneCell.setCellStyle(style);
        JuneCell.setCellValue("月份目标");
        //月份实绩
        XSSFCell JulyCell = threeRow
                .createCell(SheetService.columnToIndex("J"));
        JulyCell.setCellStyle(style);
        JulyCell.setCellValue("月份实绩");
        //本年同期目标累计值
        XSSFCell accumulateTargetCell = threeRow
                .createCell(SheetService.columnToIndex("K"));
        accumulateTargetCell.setCellStyle(style);
        accumulateTargetCell.setCellValue("目标累计值");
        // 本年同期实际累计值
        XSSFCell accumulateActualCell = threeRow
                .createCell(SheetService.columnToIndex("L"));
        accumulateActualCell.setCellStyle(style);
        accumulateActualCell.setCellValue("实绩累计值");
        //人工评分
        XSSFCell AugustCell = threeRow
                .createCell(SheetService.columnToIndex("M"));
        AugustCell.setCellStyle(style);
        AugustCell.setCellValue("人工评分");

        // 3.设置合并单元格边框

        setRegionBorder(titleRange, sheet);
        setRegionBorder(getCompanyName, sheet);
        setRegionBorder(getBlank,sheet);
        return 2;
    }
}