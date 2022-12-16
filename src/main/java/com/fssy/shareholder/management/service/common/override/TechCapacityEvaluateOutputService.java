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
 * @author wusiyang
 * @ClassName: TechCapacityEvaluateOutputService
 * @Description: 企业研发工艺能力评价表 导出excel模板
 * @date
 */
public class TechCapacityEvaluateOutputService extends SheetOutputService {
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
                SheetService.columnToIndex("Q"));
        //公司名称
        CellRangeAddress getCompanyName = new CellRangeAddress(1, 1,
                SheetService.columnToIndex("B"),
                SheetService.columnToIndex("O"));
        //总结评价结果
        CellRangeAddress getevalRess = new CellRangeAddress(2, 2,
                SheetService.columnToIndex("B"),
                SheetService.columnToIndex("Q"));
        //2021年对标
        CellRangeAddress getBlank = new CellRangeAddress(3, 3,
                SheetService.columnToIndex("E"),
                SheetService.columnToIndex("F"));
        //近三年数据
        CellRangeAddress getThreeData = new CellRangeAddress(3, 3,
                SheetService.columnToIndex("G"),
                SheetService.columnToIndex("I"));

        //项目
        CellRangeAddress getProjectNames = new CellRangeAddress(3, 4,
                SheetService.columnToIndex("A"),
                SheetService.columnToIndex("A"));
        //管理方法
        CellRangeAddress getManageMethods = new CellRangeAddress(3, 4,
                SheetService.columnToIndex("B"),
                SheetService.columnToIndex("B"));
        //关键指标
        CellRangeAddress getKpiDescs = new CellRangeAddress(3, 4,
                SheetService.columnToIndex("C"),
                SheetService.columnToIndex("C"));
        //公式
        CellRangeAddress getKpiFormulas = new CellRangeAddress(3, 4,
                SheetService.columnToIndex("D"),
                SheetService.columnToIndex("D"));
        //涨幅
        CellRangeAddress getRates = new CellRangeAddress(3, 4,
                SheetService.columnToIndex("L"),
                SheetService.columnToIndex("L"));
        //评价
        CellRangeAddress getAssess = new CellRangeAddress(3, 4,
                SheetService.columnToIndex("M"),
                SheetService.columnToIndex("M"));
        //存在问题
        CellRangeAddress getIssues = new CellRangeAddress(3, 4,
                SheetService.columnToIndex("N"),
                SheetService.columnToIndex("N"));
        //企业自提对策
        CellRangeAddress getImproveActionSelfs = new CellRangeAddress(3, 4,
                SheetService.columnToIndex("O"),
                SheetService.columnToIndex("O"));
        //负责人
        CellRangeAddress geTresponsibles = new CellRangeAddress(3, 4,
                SheetService.columnToIndex("P"),
                SheetService.columnToIndex("P"));
        //计划完成时间
        CellRangeAddress getEndDates = new CellRangeAddress(3, 4,
                SheetService.columnToIndex("Q"),
                SheetService.columnToIndex("Q"));




        sheet.addMergedRegion(getCompanyName);
        sheet.addMergedRegion(titleRange);
        sheet.addMergedRegion(getBlank);
        sheet.addMergedRegion(getevalRess);
        sheet.addMergedRegion(getThreeData);
        sheet.addMergedRegion(getProjectNames);
        sheet.addMergedRegion(getManageMethods);
        sheet.addMergedRegion(getKpiDescs);
        sheet.addMergedRegion(getKpiFormulas);
        sheet.addMergedRegion(getRates);
        sheet.addMergedRegion(getAssess);
        sheet.addMergedRegion(getIssues);
        sheet.addMergedRegion(getImproveActionSelfs);
        sheet.addMergedRegion(geTresponsibles);
        sheet.addMergedRegion(getEndDates);


        // 2.写内容
        // 获取数据
        @SuppressWarnings("unchecked")
        Map<String, Object> data = (Map<String, Object>) list.get(0);
        // 标题
        XSSFCell titleCell = sheet.createRow(0)
                .createCell(SheetService.columnToIndex("A"));
        titleCell.setCellStyle(style);
        titleCell.setCellValue("专用车公司在研发工艺能力提升板块的总结");
        //填报企业
        XSSFRow twoRow = sheet.createRow(1);
        XSSFCell getCompanyNameCell = twoRow
                .createCell(SheetService.columnToIndex("A"));
        getCompanyNameCell.setCellStyle(style);
        getCompanyNameCell.setCellValue("填报企业");

        //公司名称
        XSSFCell getCompanyNameCellvalue = twoRow
                .createCell(SheetService.columnToIndex("B"));
        getCompanyNameCellvalue.setCellStyle(style);
        getCompanyNameCellvalue.setCellValue("六合方盛");

        //年份
        XSSFRow threeRow = sheet.createRow(2);
        XSSFCell getYearValue = threeRow
                .createCell(SheetService.columnToIndex("A"));
        getYearValue.setCellStyle(style);
        getYearValue.setCellValue("总结评价");
        //
        XSSFCell setYearCell = twoRow
                .createCell(SheetService.columnToIndex("Q"));
        setYearCell.setCellStyle(style);
        setYearCell.setCellValue(ObjectUtils.isEmpty(data.get("year")) ? ""
                : data.get("year").toString());

        //年份
        XSSFCell yearCell = twoRow
                .createCell(SheetService.columnToIndex("P"));
        yearCell.setCellStyle(style);
        yearCell.setCellValue("年份");

        //总结评价
        XSSFCell setEvalRes = threeRow
                .createCell(SheetService.columnToIndex("B"));
        setEvalRes.setCellStyle(style);
        setEvalRes.setCellValue(ObjectUtils.isEmpty(data.get("evalRes")) ? ""
                : data.get("evalRes").toString());

        XSSFRow foreRow = sheet.createRow(3);

        //项目
        XSSFCell getProjectName = foreRow
                .createCell(SheetService.columnToIndex("A"));
        getProjectName.setCellStyle(style);
        getProjectName.setCellValue("项目");


        //项目
        XSSFCell getmanageMethod = foreRow
                .createCell(SheetService.columnToIndex("B"));
        getmanageMethod.setCellStyle(style);
        getmanageMethod.setCellValue("管理方法");

        //项目
        XSSFCell getkpiDescs = foreRow
                .createCell(SheetService.columnToIndex("C"));
        getkpiDescs.setCellStyle(style);
        getkpiDescs.setCellValue("关键指标");

        //公式
        XSSFCell getkpiFormula = foreRow
                .createCell(SheetService.columnToIndex("D"));
        getkpiFormula.setCellStyle(style);
        getkpiFormula.setCellValue("公式");


        //公式
        XSSFCell getYearOne = foreRow
                .createCell(SheetService.columnToIndex("E"));
        getYearOne.setCellStyle(style);
        getYearOne.setCellValue("2021年对标");

        //公式
        XSSFCell getThreeYear = foreRow
                .createCell(SheetService.columnToIndex("G"));
        getThreeYear.setCellStyle(style);
        getThreeYear.setCellValue("近三年数据");

        //公式
        XSSFCell getRate = foreRow
                .createCell(SheetService.columnToIndex("L"));
        getRate.setCellStyle(style);
        getRate.setCellValue("涨幅");

        //公式
        XSSFCell getAsse = foreRow
                .createCell(SheetService.columnToIndex("M"));
        getAsse.setCellStyle(style);
        getAsse.setCellValue("评价");

        //公式
        XSSFCell getIssue = foreRow
                .createCell(SheetService.columnToIndex("N"));
        getIssue.setCellStyle(style);
        getIssue.setCellValue("存在问题");

        //公式
        XSSFCell getImproveActionSelf = foreRow
                .createCell(SheetService.columnToIndex("O"));
        getImproveActionSelf.setCellStyle(style);
        getImproveActionSelf.setCellValue("企业自提对策");

        //公式
        XSSFCell getResponsible = foreRow
                .createCell(SheetService.columnToIndex("P"));
        getResponsible.setCellStyle(style);
        getResponsible.setCellValue("负责人");

        //公式
        XSSFCell getOneYear = foreRow
                .createCell(SheetService.columnToIndex("J"));
        getOneYear.setCellStyle(style);
        getOneYear.setCellValue("2021年");

        //公式
        XSSFCell getTwoYear = foreRow
                .createCell(SheetService.columnToIndex("K"));
        getTwoYear.setCellStyle(style);
        getTwoYear.setCellValue("2022年");

        //公式
        XSSFCell getEndDate = foreRow
                .createCell(SheetService.columnToIndex("Q"));
        getEndDate.setCellStyle(style);
        getEndDate.setCellValue("计划完成时间");

        XSSFRow fiveRow = sheet.createRow(4);

        //公式
        XSSFCell getBusiness = fiveRow
                .createCell(SheetService.columnToIndex("E"));
        getBusiness.setCellStyle(style);
        getBusiness.setCellValue("企业");

        //公式
        XSSFCell getNumber = fiveRow
                .createCell(SheetService.columnToIndex("F"));
        getNumber.setCellStyle(style);
        getNumber.setCellValue("数值");

        //公式
        XSSFCell getNumberOne = fiveRow
                .createCell(SheetService.columnToIndex("G"));
        getNumberOne.setCellStyle(style);
        getNumberOne.setCellValue("2019年");

        //公式
        XSSFCell getNumberTwo = fiveRow
                .createCell(SheetService.columnToIndex("H"));
        getNumberTwo.setCellStyle(style);
        getNumberTwo.setCellValue("2020年");

        //公式
        XSSFCell getNumberThree = fiveRow
                .createCell(SheetService.columnToIndex("I"));
        getNumberThree.setCellStyle(style);
        getNumberThree.setCellValue("2021年");

        //公式
        XSSFCell getNumberFore = fiveRow
                .createCell(SheetService.columnToIndex("J"));
        getNumberFore.setCellStyle(style);
        getNumberFore.setCellValue("1-8月");

        //公式
        XSSFCell getNumberFive = fiveRow
                .createCell(SheetService.columnToIndex("K"));
        getNumberFive.setCellStyle(style);
        getNumberFive.setCellValue("1-8月");


        // 3.设置合并单元格边框

        setRegionBorder(titleRange, sheet);
        setRegionBorder(getCompanyName, sheet);
        setRegionBorder(getBlank,sheet);
        setRegionBorder(getevalRess,sheet);
        setRegionBorder(getThreeData,sheet);
        setRegionBorder(getProjectNames,sheet);
        setRegionBorder(getManageMethods,sheet);
        setRegionBorder(getKpiDescs,sheet);
        setRegionBorder(getKpiFormulas,sheet);
        setRegionBorder(getRates,sheet);
        setRegionBorder(getAssess,sheet);
        setRegionBorder(getIssues,sheet);
        setRegionBorder(getImproveActionSelfs,sheet);
        setRegionBorder(geTresponsibles,sheet);
        setRegionBorder(getEndDates,sheet);


        return 4;
    }
}