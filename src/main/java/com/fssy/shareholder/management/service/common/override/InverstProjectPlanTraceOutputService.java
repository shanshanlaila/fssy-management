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
public class InverstProjectPlanTraceOutputService extends SheetOutputService {
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
        //公司名称
        CellRangeAddress getCompanyNames = new CellRangeAddress(0, 0,
                SheetService.columnToIndex("A"),
                SheetService.columnToIndex("B"));
        //公司名称名称
        CellRangeAddress getCompanyVaueNames = new CellRangeAddress(0, 0,
                SheetService.columnToIndex("C"),
                SheetService.columnToIndex("D"));
        //项目名称名称
        CellRangeAddress getProjectNames = new CellRangeAddress(0, 0,
                SheetService.columnToIndex("E"),
                SheetService.columnToIndex("F"));
        //项目名称名称
        CellRangeAddress getProjectValueNames = new CellRangeAddress(0, 0,
                SheetService.columnToIndex("G"),
                SheetService.columnToIndex("H"));
        //年份
        CellRangeAddress getYear = new CellRangeAddress(0, 0,
                SheetService.columnToIndex("J"),
                SheetService.columnToIndex("K"));
        //月份
        CellRangeAddress getMonth = new CellRangeAddress(0, 0,
                SheetService.columnToIndex("M"),
                SheetService.columnToIndex("N"));
        //项目摘要
        CellRangeAddress getAbstractes = new CellRangeAddress(1, 1,
                SheetService.columnToIndex("A"),
                SheetService.columnToIndex("B"));
        CellRangeAddress getAbstracte = new CellRangeAddress(1, 1,
                SheetService.columnToIndex("C"),
                SheetService.columnToIndex("N"));
        //总体评价
        CellRangeAddress getEvaluates = new CellRangeAddress(2, 2,
                SheetService.columnToIndex("A"),
                SheetService.columnToIndex("B"));
        CellRangeAddress getEvaluate = new CellRangeAddress(2, 2,
                SheetService.columnToIndex("C"),
                SheetService.columnToIndex("N"));
        //具体事项指标
        CellRangeAddress getProjectIndicatorOne = new CellRangeAddress(19, 19,
                SheetService.columnToIndex("D"),
                SheetService.columnToIndex("N"));
        CellRangeAddress getProjectIndicatorTwo = new CellRangeAddress(20, 20,
                SheetService.columnToIndex("D"),
                SheetService.columnToIndex("H"));
        CellRangeAddress getProjectIndicatorThree = new CellRangeAddress(21, 21,
                SheetService.columnToIndex("D"),
                SheetService.columnToIndex("H"));
        CellRangeAddress getProjectIndicatorFore = new CellRangeAddress(22, 22,
                SheetService.columnToIndex("D"),
                SheetService.columnToIndex("H"));

        //编制
        CellRangeAddress organization = new CellRangeAddress(23, 23,
                SheetService.columnToIndex("B"),
                SheetService.columnToIndex("C"));
        //编制
        CellRangeAddress opinion = new CellRangeAddress(23, 23,
                SheetService.columnToIndex("G"),
                SheetService.columnToIndex("N"));


        sheet.addMergedRegion(getCompanyNames);
        sheet.addMergedRegion(getProjectNames);
        sheet.addMergedRegion(getYear);
        sheet.addMergedRegion(getMonth);
        sheet.addMergedRegion(getAbstractes);
        sheet.addMergedRegion(getAbstracte);
        sheet.addMergedRegion(getEvaluates);
        sheet.addMergedRegion(getEvaluate);
        sheet.addMergedRegion(getProjectIndicatorOne);
        sheet.addMergedRegion(getProjectIndicatorTwo);
        sheet.addMergedRegion(getProjectIndicatorThree);
        sheet.addMergedRegion(getProjectIndicatorFore);
        sheet.addMergedRegion(organization);
        sheet.addMergedRegion(opinion);
        sheet.addMergedRegion(getCompanyVaueNames);
        sheet.addMergedRegion(getProjectValueNames);




        // 2.写内容
        // 获取数据
        @SuppressWarnings("unchecked")
        Map<String, Object> data = (Map<String, Object>) list.get(0);
        XSSFRow oneRow = sheet.createRow(0);
        //填报企业
        XSSFCell getCompanyNameCell = oneRow
                .createCell(SheetService.columnToIndex("A"));
        getCompanyNameCell.setCellStyle(style);
        getCompanyNameCell.setCellValue("公司名称");

        XSSFCell setCompanyNameValueCell = oneRow
                .createCell(SheetService.columnToIndex("C"));
        setCompanyNameValueCell.setCellStyle(style);
        setCompanyNameValueCell.setCellValue(ObjectUtils.isEmpty(data.get("companyName")) ? ""
                : data.get("companyName").toString());

        //项目名称
        XSSFCell getProjectNameCell = oneRow
                .createCell(SheetService.columnToIndex("E"));
        getProjectNameCell.setCellStyle(style);
        getProjectNameCell.setCellValue("项目名称");

        XSSFCell getProjectNameValueCell = oneRow
                .createCell(SheetService.columnToIndex("G"));
        getProjectNameValueCell.setCellStyle(style);
        getProjectNameValueCell.setCellValue(ObjectUtils.isEmpty(data.get("projectName")) ? ""
                : data.get("projectName").toString());

        //年份
        XSSFCell getYearCell = oneRow
                .createCell(SheetService.columnToIndex("I"));
        getYearCell.setCellStyle(style);
        getYearCell.setCellValue("年份");

        XSSFCell setYearValueCell = oneRow
                .createCell(SheetService.columnToIndex("J"));
        setYearValueCell.setCellStyle(style);
        setYearValueCell.setCellValue(ObjectUtils.isEmpty(data.get("year")) ? ""
                : data.get("year").toString());

        //月份
        XSSFCell getMonthCell = oneRow
                .createCell(SheetService.columnToIndex("L"));
        getMonthCell.setCellStyle(style);
        getMonthCell.setCellValue("月份");

        XSSFCell setMonthValueCell = oneRow
                .createCell(SheetService.columnToIndex("M"));
        setMonthValueCell.setCellStyle(style);
        setMonthValueCell.setCellValue(ObjectUtils.isEmpty(data.get("month")) ? ""
                : data.get("month").toString());





//        XSSFRow twoRow = sheet.createRow(1);
//        //月份
//        XSSFCell getserialCell = twoRow
//                .createCell(SheetService.columnToIndex("A"));
//        getserialCell.setCellStyle(style);
//        getserialCell.setCellValue("序号");
//
//        XSSFCell getprojectPhaseCell = twoRow
//                .createCell(SheetService.columnToIndex("A"));
//        getprojectPhaseCell.setCellStyle(style);
//        getprojectPhaseCell.setCellValue("项目阶段");
//
//        XSSFCell getprojectContentCell = twoRow
//                .createCell(SheetService.columnToIndex("A"));
//        getprojectContentCell.setCellStyle(style);
//        getprojectContentCell.setCellValue("课题内容");
//
//        XSSFCell getprojectIndicatorsCell = twoRow
//                .createCell(SheetService.columnToIndex("A"));
//        getprojectIndicatorsCell.setCellStyle(style);
//        getprojectIndicatorsCell.setCellValue("具体事项/指标");
//
//        XSSFCell getprojectTargetCell = twoRow
//                .createCell(SheetService.columnToIndex("A"));
//        getprojectTargetCell.setCellStyle(style);
//        getprojectTargetCell.setCellValue("项目目标");
//
//        XSSFCell getfeasibilityDateCell = twoRow
//                .createCell(SheetService.columnToIndex("A"));
//        getfeasibilityDateCell.setCellStyle(style);
//        getfeasibilityDateCell.setCellValue("可研报告计划完成时间");
//
//        XSSFCell getcontractDateCell = twoRow
//                .createCell(SheetService.columnToIndex("A"));
//        getcontractDateCell.setCellStyle(style);
//        getcontractDateCell.setCellValue("合同计划完成时间");
//
//        XSSFCell getactualEndDateCell = twoRow
//                .createCell(SheetService.columnToIndex("A"));
//        getactualEndDateCell.setCellStyle(style);
//        getactualEndDateCell.setCellValue("实际完成时间");
//
//        XSSFCell getinspectionDateCell = twoRow
//                .createCell(SheetService.columnToIndex("A"));
//        getinspectionDateCell.setCellStyle(style);
//        getinspectionDateCell.setCellValue("检查时间/频次");
//
//        XSSFCell getresponsePersonCell = twoRow
//                .createCell(SheetService.columnToIndex("A"));
//        getresponsePersonCell.setCellStyle(style);
//        getresponsePersonCell.setCellValue("负责人");
//
//        XSSFCell getInspectedbyCell = twoRow
//                .createCell(SheetService.columnToIndex("A"));
//        getInspectedbyCell.setCellStyle(style);
//        getInspectedbyCell.setCellValue("检查人");
//
//        XSSFCell getInspectionResultCell = twoRow
//                .createCell(SheetService.columnToIndex("A"));
//        getInspectionResultCell.setCellStyle(style);
//        getInspectionResultCell.setCellValue("检查结果");
//
//        XSSFCell getevaluateCell = twoRow
//                .createCell(SheetService.columnToIndex("A"));
//        getevaluateCell.setCellStyle(style);
//        getevaluateCell.setCellValue("评价");
//
//        XSSFCell getNoteCell = twoRow
//                .createCell(SheetService.columnToIndex("A"));
//        getNoteCell.setCellStyle(style);
//        getNoteCell.setCellValue("备注");



        //项目摘要
        XSSFRow twoRow = sheet.createRow(1);
        XSSFCell getabstracteCell = twoRow
                .createCell(SheetService.columnToIndex("A"));
        getabstracteCell.setCellStyle(style);
        getabstracteCell.setCellValue("项目摘要");

        XSSFCell getabstracteValueCell = twoRow
                .createCell(SheetService.columnToIndex("C"));
        getabstracteValueCell.setCellStyle(style);
        getabstracteValueCell.setCellValue(ObjectUtils.isEmpty(data.get("abstracte")) ? ""
                : data.get("abstracte").toString());



        XSSFRow foreRow = sheet.createRow(2);
        //总体评价
        XSSFCell getevaluatesCell = foreRow
                .createCell(SheetService.columnToIndex("A"));
        getevaluatesCell.setCellStyle(style);
        getevaluatesCell.setCellValue("总体评价");

        XSSFCell getevaluatesValueCell = foreRow
                .createCell(SheetService.columnToIndex("C"));
        getevaluatesValueCell.setCellStyle(style);
        getevaluatesValueCell.setCellValue(ObjectUtils.isEmpty(data.get("evaluates")) ? ""
                : data.get("evaluates").toString());



        XSSFRow twentyFourRow = sheet.createRow(23);
        //编制
        XSSFCell getOrganizationCell = twentyFourRow
                .createCell(SheetService.columnToIndex("A"));
        getOrganizationCell.setCellStyle(style);
        getOrganizationCell.setCellValue("编制");

        //校对
        XSSFCell getProofreadCell = twentyFourRow
                .createCell(SheetService.columnToIndex("D"));
        getProofreadCell.setCellStyle(style);
        getProofreadCell.setCellValue("校对");

        //校对
        XSSFCell getOpinionCell = twentyFourRow
                .createCell(SheetService.columnToIndex("F"));
        getOpinionCell.setCellStyle(style);
        getOpinionCell.setCellValue("部门主管意见");

        // 3.设置合并单元格边框

        setRegionBorder(getCompanyNames, sheet);
        setRegionBorder(getProjectNames, sheet);
        setRegionBorder(getYear,sheet);
        setRegionBorder(getMonth,sheet);
        setRegionBorder(getAbstractes,sheet);
        setRegionBorder(getAbstracte,sheet);
        setRegionBorder(getEvaluates,sheet);
        setRegionBorder(getEvaluate,sheet);
        setRegionBorder(getProjectIndicatorOne,sheet);
        setRegionBorder(getProjectIndicatorTwo,sheet);
        setRegionBorder(getProjectIndicatorThree,sheet);
        setRegionBorder(getProjectIndicatorFore,sheet);
        setRegionBorder(organization,sheet);
        setRegionBorder(opinion,sheet);
        setRegionBorder(getCompanyVaueNames,sheet);
        setRegionBorder(getProjectValueNames,sheet);


        return 3;
    }
}