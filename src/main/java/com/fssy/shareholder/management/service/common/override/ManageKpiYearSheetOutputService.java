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
 * @ClassName: ManageKpiYearSheetOutputService
 * @Description: 年度导出excel模板
 * @date 2022/10/21 0021 14:30
 */
public class ManageKpiYearSheetOutputService extends SheetOutputService {
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
        short dateFormat = createHelper.createDataFormat().getFormat("yyyy-MM-dd");
        dateStyle.setDataFormat(dateFormat);

        //1.设置合并单元格
        //标题
        CellRangeAddress titleRange = new CellRangeAddress(0, 0, SheetService.columnToIndex("A"), SheetService.columnToIndex("AD"));
        //副标题：企业名称
        CellRangeAddress getCompanyName = new CellRangeAddress(1, 1, SheetService.columnToIndex("B"), SheetService.columnToIndex("D"));
        //合并空格
        CellRangeAddress getBlank = new CellRangeAddress(1, 1, SheetService.columnToIndex("G"), SheetService.columnToIndex("AD"));
        //序号
        CellRangeAddress id = new CellRangeAddress(2, 3, SheetService.columnToIndex("A"), SheetService.columnToIndex("A"));
        //指标名类别
        CellRangeAddress projectType = new CellRangeAddress(2, 3, SheetService.columnToIndex("B"), SheetService.columnToIndex("B"));
        //指标名称
        CellRangeAddress projectDesc = new CellRangeAddress(2, 3, SheetService.columnToIndex("C"), SheetService.columnToIndex("C"));
        //指标定义
        CellRangeAddress kpiDefinition = new CellRangeAddress(2, 3, SheetService.columnToIndex("D"), SheetService.columnToIndex("D"));
        //单位
        CellRangeAddress unit = new CellRangeAddress(2, 3, SheetService.columnToIndex("E"), SheetService.columnToIndex("E"));
        //数据来源部门
        CellRangeAddress dataSource = new CellRangeAddress(2, 3, SheetService.columnToIndex("F"), SheetService.columnToIndex("F"));
        //对标标杆公司名称
        CellRangeAddress benchmarkCompany = new CellRangeAddress(2, 3, SheetService.columnToIndex("G"), SheetService.columnToIndex("G"));
        //标杆值
        CellRangeAddress benchmarkValue = new CellRangeAddress(2, 3, SheetService.columnToIndex("H"), SheetService.columnToIndex("H"));
        //过去第三年值
        CellRangeAddress pastThreeYearsActual = new CellRangeAddress(2, 3, SheetService.columnToIndex("I"), SheetService.columnToIndex("I"));
        //过去第二年值
        CellRangeAddress pastTwoYearsActual = new CellRangeAddress(2, 3, SheetService.columnToIndex("J"), SheetService.columnToIndex("J"));
        //过去第一年值
        CellRangeAddress pastOneYearActual = new CellRangeAddress(2, 3, SheetService.columnToIndex("K"), SheetService.columnToIndex("K"));
        //基本目标
        CellRangeAddress basicTarget = new CellRangeAddress(2, 3, SheetService.columnToIndex("L"), SheetService.columnToIndex("L"));
        //必达目标
        CellRangeAddress mustInputTarget = new CellRangeAddress(2, 3, SheetService.columnToIndex("M"), SheetService.columnToIndex("M"));
        //达标目标
        CellRangeAddress reachTarget = new CellRangeAddress(2, 3, SheetService.columnToIndex("N"), SheetService.columnToIndex("N"));
        //挑战目标
        CellRangeAddress challengeTarget = new CellRangeAddress(2, 3, SheetService.columnToIndex("O"), SheetService.columnToIndex("O"));
        //监控部门名称
        CellRangeAddress monitorDepartment = new CellRangeAddress(2, 3, SheetService.columnToIndex("AB"), SheetService.columnToIndex("AB"));
        //监控人姓名
        CellRangeAddress monitorUser = new CellRangeAddress(2, 3, SheetService.columnToIndex("AC"), SheetService.columnToIndex("AC"));
        //月度指标分解方式（月度值、年度值
        CellRangeAddress kpiDecomposeMode = new CellRangeAddress(2, 3, SheetService.columnToIndex("AD"), SheetService.columnToIndex("AD"));

        sheet.addMergedRegion(pastThreeYearsActual);
        sheet.addMergedRegion(pastTwoYearsActual);
        sheet.addMergedRegion(pastOneYearActual);
        sheet.addMergedRegion(kpiDefinition);
        sheet.addMergedRegion(challengeTarget);
        sheet.addMergedRegion(reachTarget);
        sheet.addMergedRegion(mustInputTarget);
        sheet.addMergedRegion(basicTarget);
        sheet.addMergedRegion(getBlank);
        sheet.addMergedRegion(monitorUser);
        sheet.addMergedRegion(monitorDepartment);
        sheet.addMergedRegion(benchmarkValue);
        sheet.addMergedRegion(benchmarkCompany);
        sheet.addMergedRegion(dataSource);
        sheet.addMergedRegion(unit);
        sheet.addMergedRegion(projectDesc);
        sheet.addMergedRegion(projectType);
        sheet.addMergedRegion(id);
        sheet.addMergedRegion(getCompanyName);
       sheet.addMergedRegion(kpiDecomposeMode);
        sheet.addMergedRegion(titleRange);

        // 2.写内容
        // 获取数据
        @SuppressWarnings("unchecked") Map<String, Object> data = (Map<String, Object>) list.get(0);
        // 标题
        XSSFCell titleCell = sheet.createRow(0).createCell(SheetService.columnToIndex("A"));
        titleCell.setCellStyle(style);
        titleCell.setCellValue("年度经营指标报表");
        //填报企业
        XSSFRow twoRow = sheet.createRow(1);
        XSSFCell getCompanyNameCell = twoRow.createCell(SheetService.columnToIndex("A"));
        getCompanyNameCell.setCellStyle(style);
        getCompanyNameCell.setCellValue("填报企业");
        //填报企业
        XSSFCell setCompanyNameCell = twoRow.createCell(SheetService.columnToIndex("B"));
        setCompanyNameCell.setCellStyle(style);
        setCompanyNameCell.setCellValue(ObjectUtils.isEmpty(data.get("companyName")) ? "" : data.get("companyName").toString());
        //年份
        XSSFCell yearCell = twoRow.createCell(SheetService.columnToIndex("E"));
        yearCell.setCellStyle(style);
        yearCell.setCellValue("年份");
        //年份
        XSSFCell setYearCell = twoRow.createCell(SheetService.columnToIndex("F"));
        setYearCell.setCellStyle(style);
        setYearCell.setCellValue(ObjectUtils.isEmpty(data.get("year")) ? "" : data.get("year").toString());
        // 序号
        XSSFRow threeRow = sheet.createRow(2);
        XSSFCell idCell = threeRow.createCell(SheetService.columnToIndex("A"));
        idCell.setCellStyle(style);
        idCell.setCellValue("序号");
        // 指标类别
        XSSFCell projectTypeCell = threeRow.createCell(SheetService.columnToIndex("B"));
        projectTypeCell.setCellStyle(style);
        projectTypeCell.setCellValue("重点工作");
        //指标名称
        XSSFCell projectDescCell = threeRow.createCell(SheetService.columnToIndex("C"));
        projectDescCell.setCellStyle(style);
        projectDescCell.setCellValue("管理项目");
        //指标名称
        XSSFCell kpiDefinitionCell = threeRow.createCell(SheetService.columnToIndex("D"));
        kpiDefinitionCell.setCellStyle(style);
        kpiDefinitionCell.setCellValue("管理项目定义（公式）");
        //单位
        XSSFCell unitCell = threeRow.createCell(SheetService.columnToIndex("E"));
        unitCell.setCellStyle(style);
        unitCell.setCellValue("单位");
        // 数据来源部门
        XSSFCell dataSourceCell = threeRow.createCell(SheetService.columnToIndex("F"));
        dataSourceCell.setCellStyle(style);
        dataSourceCell.setCellValue("数据来源部门");
        //对标标杆公司名称
        XSSFCell benchmarkCompanyCell = threeRow.createCell(SheetService.columnToIndex("G"));
        benchmarkCompanyCell.setCellStyle(style);
        benchmarkCompanyCell.setCellValue("对标标杆公司名称");
        //标杆值
        XSSFCell benchmarkValueCell = threeRow.createCell(SheetService.columnToIndex("H"));
        benchmarkValueCell.setCellStyle(style);
        benchmarkValueCell.setCellValue("标杆值");
        //过去第三年值
        XSSFCell pastThreeYearsActualCell = threeRow.createCell(SheetService.columnToIndex("I"));
        pastThreeYearsActualCell.setCellStyle(style);
        pastThreeYearsActualCell.setCellValue("过去第三年值");
        // 过去第二年值
        XSSFCell pastTwoYearsActualCell = threeRow.createCell(SheetService.columnToIndex("J"));
        pastTwoYearsActualCell.setCellStyle(style);
        pastTwoYearsActualCell.setCellValue("过去第二年值");
        //过去第一年值
        XSSFCell pastOneYearActualCell = threeRow.createCell(SheetService.columnToIndex("K"));
        pastOneYearActualCell.setCellStyle(style);
        pastOneYearActualCell.setCellValue("过去第一年值");
        // 基本目标
        XSSFCell basicTargetCell = threeRow.createCell(SheetService.columnToIndex("L"));
        basicTargetCell.setCellStyle(style);
        basicTargetCell.setCellValue("基本目标");
        //必达目标
        XSSFCell mustInputTargetCell = threeRow.createCell(SheetService.columnToIndex("M"));
        mustInputTargetCell.setCellStyle(style);
        mustInputTargetCell.setCellValue("必达目标");
        //达标目标
        XSSFCell reachTargetCell = threeRow.createCell(SheetService.columnToIndex("N"));
        reachTargetCell.setCellStyle(style);
        reachTargetCell.setCellValue("达标目标");
        // 挑战目标
        XSSFCell challengeTargetCell = threeRow.createCell(SheetService.columnToIndex("O"));
        challengeTargetCell.setCellStyle(style);
        challengeTargetCell.setCellValue("挑战目标");
        // 监控部门名称
        XSSFCell monitorDepartmentCell = threeRow.createCell(SheetService.columnToIndex("AB"));
        monitorDepartmentCell.setCellStyle(style);
        monitorDepartmentCell.setCellValue("监控部门名称");
        //监控人姓名
        XSSFCell monitorUserCell = threeRow.createCell(SheetService.columnToIndex("AC"));
        monitorUserCell.setCellStyle(style);
        monitorUserCell.setCellValue("监控人姓名");
        //监控人姓名
        XSSFCell kpiDecomposeModeCell = threeRow.createCell(SheetService.columnToIndex("AD"));
        kpiDecomposeModeCell.setCellStyle(style);
        kpiDecomposeModeCell.setCellValue("月度指标分解方式（月度值、年度值");

        //一月
        XSSFCell JanuaryCell = threeRow.createCell(SheetService.columnToIndex("P"));
        JanuaryCell.setCellStyle(style);
        JanuaryCell.setCellValue("一月");

        //二月
        XSSFCell FebruaryCell = threeRow.createCell(SheetService.columnToIndex("Q"));
        FebruaryCell.setCellStyle(style);
        FebruaryCell.setCellValue("二月");
        // 三月
        XSSFCell MarchCell = threeRow.createCell(SheetService.columnToIndex("R"));
        MarchCell.setCellStyle(style);
        MarchCell.setCellValue("三月");
        //四月
        XSSFCell AprilCell = threeRow.createCell(SheetService.columnToIndex("S"));
        AprilCell.setCellStyle(style);
        AprilCell.setCellValue("四月");
        //五月
        XSSFCell MayCell = threeRow.createCell(SheetService.columnToIndex("T"));
        MayCell.setCellStyle(style);
        MayCell.setCellValue("五月");
        // 六月
        XSSFCell JuneCell = threeRow.createCell(SheetService.columnToIndex("U"));
        JuneCell.setCellStyle(style);
        JuneCell.setCellValue("六月");
        //七月
        XSSFCell JulyCell = threeRow.createCell(SheetService.columnToIndex("V"));
        JulyCell.setCellStyle(style);
        JulyCell.setCellValue("七月");
        //八月
        XSSFCell AugustCell = threeRow.createCell(SheetService.columnToIndex("W"));
        AugustCell.setCellStyle(style);
        AugustCell.setCellValue("八月");
        //九月
        XSSFCell SeptemberCell = threeRow.createCell(SheetService.columnToIndex("X"));
        SeptemberCell.setCellStyle(style);
        SeptemberCell.setCellValue("九月");
        //十月
        XSSFCell OctoberCell = threeRow.createCell(SheetService.columnToIndex("Y"));
        OctoberCell.setCellStyle(style);
        OctoberCell.setCellValue("十月");
        // 十一月
        XSSFCell NovemberCell = threeRow.createCell(SheetService.columnToIndex("Z"));
        NovemberCell.setCellStyle(style);
        NovemberCell.setCellValue("十一月");
        //十二月
        XSSFCell DecemberCell = threeRow.createCell(SheetService.columnToIndex("AA"));
        DecemberCell.setCellStyle(style);
        DecemberCell.setCellValue("十二月");

        XSSFRow fourRow = sheet.createRow(3);
        //一月
        XSSFCell JanuaryAimCell = fourRow.createCell(SheetService.columnToIndex("P"));
        JanuaryAimCell.setCellStyle(style);
        JanuaryAimCell.setCellValue("目标值");

        //二月
        XSSFCell FebruaryAimCell = fourRow.createCell(SheetService.columnToIndex("Q"));
        FebruaryAimCell.setCellStyle(style);
        FebruaryAimCell.setCellValue("目标值");
        // 三月
        XSSFCell MarchAimCell = fourRow.createCell(SheetService.columnToIndex("R"));
        MarchAimCell.setCellStyle(style);
        MarchAimCell.setCellValue("目标值");
        //四月
        XSSFCell AprilAimCell = fourRow.createCell(SheetService.columnToIndex("S"));
        AprilAimCell.setCellStyle(style);
        AprilAimCell.setCellValue("目标值");
        //五月
        XSSFCell MayAimCell = fourRow.createCell(SheetService.columnToIndex("T"));
        MayAimCell.setCellStyle(style);
        MayAimCell.setCellValue("目标值");
        // 六月
        XSSFCell JuneAimCell = fourRow.createCell(SheetService.columnToIndex("U"));
        JuneAimCell.setCellStyle(style);
        JuneAimCell.setCellValue("目标值");
        //七月
        XSSFCell JulyAimCell = fourRow.createCell(SheetService.columnToIndex("V"));
        JulyAimCell.setCellStyle(style);
        JulyAimCell.setCellValue("目标值");
        //八月
        XSSFCell AugustAimCell = fourRow.createCell(SheetService.columnToIndex("W"));
        AugustAimCell.setCellStyle(style);
        AugustAimCell.setCellValue("目标值");
        //九月
        XSSFCell SeptemberAimCell = fourRow.createCell(SheetService.columnToIndex("X"));
        SeptemberAimCell.setCellStyle(style);
        SeptemberAimCell.setCellValue("目标值");
        //十月
        XSSFCell OctoberAimCell = fourRow.createCell(SheetService.columnToIndex("Y"));
        OctoberAimCell.setCellStyle(style);
        OctoberAimCell.setCellValue("目标值");
        // 十一月
        XSSFCell NovemberAimCell = fourRow.createCell(SheetService.columnToIndex("Z"));
        NovemberAimCell.setCellStyle(style);
        NovemberAimCell.setCellValue("目标值");
        //十二月
        XSSFCell DecemberAimCell = fourRow.createCell(SheetService.columnToIndex("AA"));
        DecemberAimCell.setCellStyle(style);
        DecemberAimCell.setCellValue("目标值");


        // 3.设置合并单元格边框
        setRegionBorder(titleRange, sheet);
        setRegionBorder(getCompanyName, sheet);
        setRegionBorder(getBlank, sheet);
        setRegionBorder(id, sheet);
        setRegionBorder(projectType, sheet);
        setRegionBorder(projectDesc, sheet);
        setRegionBorder(kpiDefinition, sheet);
        setRegionBorder(unit, sheet);
        setRegionBorder(dataSource, sheet);
        setRegionBorder(benchmarkCompany, sheet);
        setRegionBorder(benchmarkValue, sheet);
        setRegionBorder(pastOneYearActual, sheet);
        setRegionBorder(pastTwoYearsActual, sheet);
        setRegionBorder(pastThreeYearsActual, sheet);
        setRegionBorder(basicTarget, sheet);
        setRegionBorder(mustInputTarget, sheet);
        setRegionBorder(reachTarget, sheet);
        setRegionBorder(challengeTarget, sheet);
        setRegionBorder(monitorDepartment, sheet);
        setRegionBorder(monitorUser, sheet);
        setRegionBorder(kpiDecomposeMode, sheet);
        return 3;
    }
}