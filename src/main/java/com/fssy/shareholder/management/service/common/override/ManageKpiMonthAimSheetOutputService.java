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
 * @ClassName: ManageKpiMonthAimSheetOutputService
 * @Description: 月度目标 导出excel模板
 * @date 2022/10/21 0021 14:30
 */
public class ManageKpiMonthAimSheetOutputService extends SheetOutputService {
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
                SheetService.columnToIndex("AV"));
        //副标题：填报企业
        CellRangeAddress setCompanyName = new CellRangeAddress(1, 1,
                SheetService.columnToIndex("A"),
                SheetService.columnToIndex("B"));
        //副标题：企业名称
        CellRangeAddress getCompanyName = new CellRangeAddress(1, 1,
                SheetService.columnToIndex("C"),
                SheetService.columnToIndex("E"));
        //序号
        CellRangeAddress id = new CellRangeAddress(2, 3,
                SheetService.columnToIndex("A"),
                SheetService.columnToIndex("A"));
        //企业名称
        CellRangeAddress companyName = new CellRangeAddress(2, 3,
                SheetService.columnToIndex("B"),
                SheetService.columnToIndex("B"));
        //项目状态
        CellRangeAddress status = new CellRangeAddress(2, 3,
                SheetService.columnToIndex("C"),
                SheetService.columnToIndex("C"));
        //指标类型
        CellRangeAddress projectType = new CellRangeAddress(2, 3,
                SheetService.columnToIndex("D"),
                SheetService.columnToIndex("D"));
        //指标名称
        CellRangeAddress projectDesc = new CellRangeAddress(2, 3,
                SheetService.columnToIndex("E"),
                SheetService.columnToIndex("E"));
        //单位
        CellRangeAddress unit = new CellRangeAddress(2, 3,
                SheetService.columnToIndex("F"),
                SheetService.columnToIndex("F"));
        //数据来源部门
        CellRangeAddress dataSource = new CellRangeAddress(2, 3,
                SheetService.columnToIndex("G"),
                SheetService.columnToIndex("G"));
        //对标标杆公司名称
        CellRangeAddress benchmarkCompany = new CellRangeAddress(2, 3,
                SheetService.columnToIndex("H"),
                SheetService.columnToIndex("H"));
        //标杆值
        CellRangeAddress benchmarkValue = new CellRangeAddress(2, 3,
                SheetService.columnToIndex("I"),
                SheetService.columnToIndex("I"));
        //监控部门名称
        CellRangeAddress monitorDepartment = new CellRangeAddress(2, 3,
                SheetService.columnToIndex("J"),
                SheetService.columnToIndex("J"));
        //监控人姓名
        CellRangeAddress monitorUser = new CellRangeAddress(2, 3,
                SheetService.columnToIndex("K"),
                SheetService.columnToIndex("K"));
        //年份
        CellRangeAddress year = new CellRangeAddress(2, 3,
                SheetService.columnToIndex("L"),
                SheetService.columnToIndex("L"));
        //基本目标
        CellRangeAddress basicTarget = new CellRangeAddress(2, 3,
                SheetService.columnToIndex("M"),
                SheetService.columnToIndex("M"));
        //必达目标
        CellRangeAddress mustInputTarget = new CellRangeAddress(2, 3,
                SheetService.columnToIndex("N"),
                SheetService.columnToIndex("N"));
        //达标目标
        CellRangeAddress reachTarget = new CellRangeAddress(2, 3,
                SheetService.columnToIndex("O"),
                SheetService.columnToIndex("O"));
        //挑战目标
        CellRangeAddress challengeTarget = new CellRangeAddress(2, 3,
                SheetService.columnToIndex("P"),
                SheetService.columnToIndex("P"));
        //权重
        CellRangeAddress proportion = new CellRangeAddress(2, 3,
                SheetService.columnToIndex("Q"),
                SheetService.columnToIndex("Q"));
        //过去第一年值
        CellRangeAddress pastOneYearActual = new CellRangeAddress(2, 3,
                SheetService.columnToIndex("R"),
                SheetService.columnToIndex("R"));
        //过去第二年值
        CellRangeAddress pastTwoYearsActual = new CellRangeAddress(2, 3,
                SheetService.columnToIndex("S"),
                SheetService.columnToIndex("S"));
        //过去第三年值
        CellRangeAddress pastThreeYearsActual = new CellRangeAddress(2, 3,
                SheetService.columnToIndex("T"),
                SheetService.columnToIndex("T"));

        //选取原则
        CellRangeAddress setPolicy = new CellRangeAddress(2, 3,
                SheetService.columnToIndex("U"),
                SheetService.columnToIndex("U"));
        //KPI来源
        CellRangeAddress source = new CellRangeAddress(2, 3,
                SheetService.columnToIndex("V"),
                SheetService.columnToIndex("V"));
        //本年同期目标累计值
        CellRangeAddress accumulateTarget = new CellRangeAddress(2, 3,
                SheetService.columnToIndex("W"),
                SheetService.columnToIndex("W"));
        //本年同期实际累计值
        CellRangeAddress accumulateActual = new CellRangeAddress(2, 3,
                SheetService.columnToIndex("X"),
                SheetService.columnToIndex("X"));
        //1月
        CellRangeAddress January = new CellRangeAddress(2, 2,
                SheetService.columnToIndex("Y"),
                SheetService.columnToIndex("Z"));
        //2月
        CellRangeAddress February = new CellRangeAddress(2, 2,
                SheetService.columnToIndex("AA"),
                SheetService.columnToIndex("AB"));
        //3月
        CellRangeAddress March = new CellRangeAddress(2, 2,
                SheetService.columnToIndex("AC"),
                SheetService.columnToIndex("AD"));
        //4月
        CellRangeAddress April = new CellRangeAddress(2, 2,
                SheetService.columnToIndex("AE"),
                SheetService.columnToIndex("AF"));
        //5月
        CellRangeAddress May = new CellRangeAddress(2, 2,
                SheetService.columnToIndex("AG"),
                SheetService.columnToIndex("AH"));
        //6月
        CellRangeAddress June = new CellRangeAddress(2, 2,
                SheetService.columnToIndex("AI"),
                SheetService.columnToIndex("AJ"));
        //7月
        CellRangeAddress July = new CellRangeAddress(2, 2,
                SheetService.columnToIndex("AK"),
                SheetService.columnToIndex("AL"));
        //8月
        CellRangeAddress August = new CellRangeAddress(2, 2,
                SheetService.columnToIndex("AM"),
                SheetService.columnToIndex("AN"));
        //9月
        CellRangeAddress September = new CellRangeAddress(2, 2,
                SheetService.columnToIndex("AO"),
                SheetService.columnToIndex("AP"));
        //10月
        CellRangeAddress October = new CellRangeAddress(2, 2,
                SheetService.columnToIndex("AQ"),
                SheetService.columnToIndex("AR"));
        //11月
        CellRangeAddress November = new CellRangeAddress(2, 2,
                SheetService.columnToIndex("AS"),
                SheetService.columnToIndex("AT"));
        //12月
        CellRangeAddress December = new CellRangeAddress(2, 2,
                SheetService.columnToIndex("AU"),
                SheetService.columnToIndex("AV"));

        sheet.addMergedRegion(December);
        sheet.addMergedRegion(November);
        sheet.addMergedRegion(October);
        sheet.addMergedRegion(September);
        sheet.addMergedRegion(August);
        sheet.addMergedRegion(July);
        sheet.addMergedRegion(June);
        sheet.addMergedRegion(May);
        sheet.addMergedRegion(April);
        sheet.addMergedRegion(March);
        sheet.addMergedRegion(February);
        sheet.addMergedRegion(January);
        sheet.addMergedRegion(accumulateActual);
        sheet.addMergedRegion(accumulateTarget);
        sheet.addMergedRegion(source);
        sheet.addMergedRegion(setPolicy);
        sheet.addMergedRegion(pastThreeYearsActual);
        sheet.addMergedRegion(pastTwoYearsActual);
        sheet.addMergedRegion(pastOneYearActual);
        sheet.addMergedRegion(proportion);
        sheet.addMergedRegion(challengeTarget);
        sheet.addMergedRegion(reachTarget);
        sheet.addMergedRegion(mustInputTarget);
        sheet.addMergedRegion(basicTarget);
        sheet.addMergedRegion(year);
        sheet.addMergedRegion(monitorUser);
        sheet.addMergedRegion(monitorDepartment);
        sheet.addMergedRegion(benchmarkValue);
        sheet.addMergedRegion(benchmarkCompany);
        sheet.addMergedRegion(dataSource);
        sheet.addMergedRegion(unit);
        sheet.addMergedRegion(projectDesc);
        sheet.addMergedRegion(projectType);
        sheet.addMergedRegion(status);
        sheet.addMergedRegion(companyName);
        sheet.addMergedRegion(id);
        sheet.addMergedRegion(getCompanyName);
        sheet.addMergedRegion(setCompanyName);
        sheet.addMergedRegion(titleRange);

        // 2.写内容
        // 获取数据
        @SuppressWarnings("unchecked")
        Map<String, Object> data = (Map<String, Object>) list.get(0);
        // 标题
        XSSFCell titleCell = sheet.createRow(0)
                .createCell(SheetService.columnToIndex("A"));
        titleCell.setCellStyle(style);
        titleCell.setCellValue("年度经营计划管理项目月度报表");
        //填报企业
        XSSFRow twoRow = sheet.createRow(1);
        XSSFCell getCompanyNameCell = twoRow
                .createCell(SheetService.columnToIndex("A"));
        getCompanyNameCell.setCellStyle(style);
        getCompanyNameCell.setCellValue("填报企业");
        //填报企业
        XSSFCell setCompanyNameCell = twoRow
                .createCell(SheetService.columnToIndex("C"));
        setCompanyNameCell.setCellStyle(style);
        setCompanyNameCell.setCellValue(ObjectUtils.isEmpty(data.get("companyName")) ? ""
                : data.get("companyName").toString());
        //年份
        XSSFCell yearCell = twoRow
                .createCell(SheetService.columnToIndex("F"));
        yearCell.setCellStyle(style);
        yearCell.setCellValue("年份");
        //年份
        XSSFCell setYearCell = twoRow
                .createCell(SheetService.columnToIndex("G"));
        setYearCell.setCellStyle(style);
        setYearCell.setCellValue(ObjectUtils.isEmpty(data.get("year")) ? ""
                : data.get("year").toString());
        //填报月份
        XSSFCell monthCell = twoRow
                .createCell(SheetService.columnToIndex("H"));
        monthCell.setCellStyle(style);
        monthCell.setCellValue("月份");
        // 序号
        XSSFRow threeRow = sheet.createRow(2);
        XSSFCell idCell = threeRow
                .createCell(SheetService.columnToIndex("A"));
        idCell.setCellStyle(style);
        idCell.setCellValue("序号");
        //企业名称
        XSSFCell companyName1Cell = threeRow
                .createCell(SheetService.columnToIndex("B"));
        companyName1Cell.setCellStyle(style);
        companyName1Cell.setCellValue("企业名称");
        //项目状态
        XSSFCell statusCell = threeRow
                .createCell(SheetService.columnToIndex("C"));
        statusCell.setCellStyle(style);
        statusCell.setCellValue("项目状态");
        // 指标类别
        XSSFCell projectTypeCell = threeRow
                .createCell(SheetService.columnToIndex("D"));
        projectTypeCell.setCellStyle(style);
        projectTypeCell.setCellValue("指标类别");
        //指标名称
        XSSFCell projectDescCell = threeRow
                .createCell(SheetService.columnToIndex("E"));
        projectDescCell.setCellStyle(style);
        projectDescCell.setCellValue("指标名称");
        //单位
        XSSFCell unitCell = threeRow
                .createCell(SheetService.columnToIndex("F"));
        unitCell.setCellStyle(style);
        unitCell.setCellValue("单位");
        // 数据来源部门
        XSSFCell dataSourceCell = threeRow
                .createCell(SheetService.columnToIndex("G"));
        dataSourceCell.setCellStyle(style);
        dataSourceCell.setCellValue("数据来源部门");
        //对标标杆公司名称
        XSSFCell benchmarkCompanyCell = threeRow
                .createCell(SheetService.columnToIndex("H"));
        benchmarkCompanyCell.setCellStyle(style);
        benchmarkCompanyCell.setCellValue("对标标杆公司名称");
        //标杆值
        XSSFCell benchmarkValueCell = threeRow
                .createCell(SheetService.columnToIndex("I"));
        benchmarkValueCell.setCellStyle(style);
        benchmarkValueCell.setCellValue("标杆值");
        // 监控部门名称
        XSSFCell monitorDepartmentCell = threeRow
                .createCell(SheetService.columnToIndex("J"));
        monitorDepartmentCell.setCellStyle(style);
        monitorDepartmentCell.setCellValue("监控部门名称");
        //监控人姓名
        XSSFCell monitorUserCell = threeRow
                .createCell(SheetService.columnToIndex("K"));
        monitorUserCell.setCellStyle(style);
        monitorUserCell.setCellValue("监控人姓名");
        //年份
        XSSFCell year1Cell = threeRow
                .createCell(SheetService.columnToIndex("L"));
        year1Cell.setCellStyle(style);
        year1Cell.setCellValue("年份");
        // 基本目标
        XSSFCell basicTargetCell = threeRow
                .createCell(SheetService.columnToIndex("M"));
        basicTargetCell.setCellStyle(style);
        basicTargetCell.setCellValue("基本目标");
        //必达目标
        XSSFCell mustInputTargetCell = threeRow
                .createCell(SheetService.columnToIndex("N"));
        mustInputTargetCell.setCellStyle(style);
        mustInputTargetCell.setCellValue("必达目标");
        //达标目标
        XSSFCell reachTargetCell = threeRow
                .createCell(SheetService.columnToIndex("O"));
        reachTargetCell.setCellStyle(style);
        reachTargetCell.setCellValue("达标目标");
        // 挑战目标
        XSSFCell challengeTargetCell = threeRow
                .createCell(SheetService.columnToIndex("P"));
        challengeTargetCell.setCellStyle(style);
        challengeTargetCell.setCellValue("挑战目标");
        //权重
        XSSFCell proportionCell = threeRow
                .createCell(SheetService.columnToIndex("Q"));
        proportionCell.setCellStyle(style);
        proportionCell.setCellValue("权重");
        //过去第一年值
        XSSFCell pastOneYearActualCell = threeRow
                .createCell(SheetService.columnToIndex("R"));
        pastOneYearActualCell.setCellStyle(style);
        pastOneYearActualCell.setCellValue("过去第一年值");
        // 过去第二年值
        XSSFCell pastTwoYearsActualCell = threeRow
                .createCell(SheetService.columnToIndex("S"));
        pastTwoYearsActualCell.setCellStyle(style);
        pastTwoYearsActualCell.setCellValue("过去第二年值");
        //过去第三年值
        XSSFCell pastThreeYearsActualCell = threeRow
                .createCell(SheetService.columnToIndex("T"));
        pastThreeYearsActualCell.setCellStyle(style);
        pastThreeYearsActualCell.setCellValue("过去第三年值");
        //选取原则
        XSSFCell setPolicyCell = threeRow
                .createCell(SheetService.columnToIndex("U"));
        setPolicyCell.setCellStyle(style);
        setPolicyCell.setCellValue("选取原则");
        // KPI来源
        XSSFCell sourceCell = threeRow
                .createCell(SheetService.columnToIndex("V"));
        sourceCell.setCellStyle(style);
        sourceCell.setCellValue("KPI来源");
        //本年同期目标累计值
        XSSFCell accumulateTargetCell = threeRow
                .createCell(SheetService.columnToIndex("W"));
        accumulateTargetCell.setCellStyle(style);
        accumulateTargetCell.setCellValue("本年同期目标累计值");
        // 本年同期实际累计值
        XSSFCell accumulateActualCell = threeRow
                .createCell(SheetService.columnToIndex("X"));
        accumulateActualCell.setCellStyle(style);
        accumulateActualCell.setCellValue("本年同期实际累计值");
        //一月
        XSSFCell JanuaryCell = threeRow
                .createCell(SheetService.columnToIndex("Y"));
        JanuaryCell.setCellStyle(style);
        JanuaryCell.setCellValue("一月");
        //二月
        XSSFCell FebruaryCell = threeRow
                .createCell(SheetService.columnToIndex("AA"));
        FebruaryCell.setCellStyle(style);
        FebruaryCell.setCellValue("二月");
        // 三月
        XSSFCell MarchCell = threeRow
                .createCell(SheetService.columnToIndex("AC"));
        MarchCell.setCellStyle(style);
        MarchCell.setCellValue("三月");
        //四月
        XSSFCell AprilCell = threeRow
                .createCell(SheetService.columnToIndex("AE"));
        AprilCell.setCellStyle(style);
        AprilCell.setCellValue("四月");
        //五月
        XSSFCell MayCell = threeRow
                .createCell(SheetService.columnToIndex("AG"));
        MayCell.setCellStyle(style);
        MayCell.setCellValue("五月");
        // 六月
        XSSFCell JuneCell = threeRow
                .createCell(SheetService.columnToIndex("AI"));
        JuneCell.setCellStyle(style);
        JuneCell.setCellValue("六月");
        //七月
        XSSFCell JulyCell = threeRow
                .createCell(SheetService.columnToIndex("AK"));
        JulyCell.setCellStyle(style);
        JulyCell.setCellValue("七月");
        //八月
        XSSFCell AugustCell = threeRow
                .createCell(SheetService.columnToIndex("AM"));
        AugustCell.setCellStyle(style);
        AugustCell.setCellValue("八月");
        //九月
        XSSFCell SeptemberCell = threeRow
                .createCell(SheetService.columnToIndex("AO"));
        SeptemberCell.setCellStyle(style);
        SeptemberCell.setCellValue("九月");
        //十月
        XSSFCell OctoberCell = threeRow
                .createCell(SheetService.columnToIndex("AQ"));
        OctoberCell.setCellStyle(style);
        OctoberCell.setCellValue("十月");
        // 十一月
        XSSFCell NovemberCell = threeRow
                .createCell(SheetService.columnToIndex("AS"));
        NovemberCell.setCellStyle(style);
        NovemberCell.setCellValue("十一月");
        //十二月
        XSSFCell DecemberCell = threeRow
                .createCell(SheetService.columnToIndex("AU"));
        DecemberCell.setCellStyle(style);
        DecemberCell.setCellValue("十二月");


        // 3.设置合并单元格边框
        setRegionBorder(titleRange, sheet);
        setRegionBorder(setCompanyName, sheet);
        setRegionBorder(getCompanyName, sheet);
        setRegionBorder(companyName, sheet);
        setRegionBorder(status, sheet);
        setRegionBorder(projectType, sheet);
        setRegionBorder(projectDesc, sheet);
        setRegionBorder(unit, sheet);
        setRegionBorder(dataSource, sheet);
        setRegionBorder(benchmarkCompany, sheet);
        setRegionBorder(benchmarkValue, sheet);
        setRegionBorder(monitorDepartment, sheet);
        setRegionBorder(monitorUser, sheet);
        setRegionBorder(year, sheet);
        setRegionBorder(basicTarget, sheet);
        setRegionBorder(mustInputTarget, sheet);
        setRegionBorder(reachTarget, sheet);
        setRegionBorder(challengeTarget, sheet);
        setRegionBorder(proportion, sheet);
        setRegionBorder(pastOneYearActual, sheet);
        setRegionBorder(pastTwoYearsActual, sheet);
        setRegionBorder(pastThreeYearsActual, sheet);
        setRegionBorder(setPolicy, sheet);
        setRegionBorder(source, sheet);
        setRegionBorder(accumulateTarget, sheet);
        setRegionBorder(accumulateActual, sheet);
        setRegionBorder(January, sheet);
        setRegionBorder(February, sheet);
        setRegionBorder(March, sheet);
        setRegionBorder(April, sheet);
        setRegionBorder(May, sheet);
        setRegionBorder(June, sheet);
        setRegionBorder(July, sheet);
        setRegionBorder(August, sheet);
        setRegionBorder(September, sheet);
        setRegionBorder(October, sheet);
        setRegionBorder(November, sheet);
        setRegionBorder(December, sheet);
        return 3;
    }
}