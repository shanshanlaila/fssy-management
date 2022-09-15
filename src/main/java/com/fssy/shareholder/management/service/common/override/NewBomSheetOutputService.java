package com.fssy.shareholder.management.service.common.override;

import com.fssy.shareholder.management.service.common.SheetOutputService;
import com.fssy.shareholder.management.service.common.SheetService;
import com.fssy.shareholder.management.tools.exception.ServiceException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author YanMing
 * @title: NewBomSheetOutputService
 * @description:  新bom查询导出自定义实现服务类
 * @date 2022/3/10
 */
public class NewBomSheetOutputService extends SheetOutputService {

    @Override
    public <T> int customizedHead(XSSFSheet sheet, List<T> list)
    {
//        //表头第一列宽度
//        sheet.setColumnWidth(0,12);
//        //表头第二列宽度
//        sheet.setColumnWidth(1,24);
//        //客户件号宽度
//        sheet.setColumnWidth(3,18);
//        //厂内件号宽度
//        sheet.setColumnWidth(4,18);
//        //名称宽度
//        sheet.setColumnWidth(6,28);
//        //领料班组宽度
//        sheet.setColumnWidth(32,18);
//        //发放原则宽度
//        sheet.setColumnWidth(37,14);
//        //仓库（入库仓库）宽度
//        sheet.setColumnWidth(38,18);
//        //生产场地宽度
//        sheet.setColumnWidth(39,28);

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
        // 标题
//        CellRangeAddress titleRange = new CellRangeAddress(0, 1,
//                SheetService.columnToIndex("A"),
//                SheetService.columnToIndex("AT"));
//        // Bom名称
//        CellRangeAddress nameRange = new CellRangeAddress(2, 3,
//                SheetService.columnToIndex("A"),
//                SheetService.columnToIndex("H"));
//        // 制定单位
//        CellRangeAddress setCompanyRange = new CellRangeAddress(2, 2,
//                SheetService.columnToIndex("J"),
//                SheetService.columnToIndex("L"));
//        // 编制人
//        CellRangeAddress setPersonRange = new CellRangeAddress(2, 2,
//                SheetService.columnToIndex("N"),
//                SheetService.columnToIndex("Q"));
//        // 修订日期
//        CellRangeAddress updateDateRange = new CellRangeAddress(2, 2,
//                SheetService.columnToIndex("S"),
//                SheetService.columnToIndex("W"));
//        // 文件编号
//        CellRangeAddress FileCodeRange = new CellRangeAddress(2, 2,
//                SheetService.columnToIndex("Y"),
//                SheetService.columnToIndex("AT"));
//        // 审核
//        CellRangeAddress auditRange = new CellRangeAddress(3, 3,
//                SheetService.columnToIndex("J"),
//                SheetService.columnToIndex("L"));
//        // 批准
//        CellRangeAddress approveRange = new CellRangeAddress(3, 3,
//                SheetService.columnToIndex("N"),
//                SheetService.columnToIndex("Q"));
//        // 制定日期
//        CellRangeAddress setDateRange = new CellRangeAddress(3, 3,
//                SheetService.columnToIndex("S"),
//                SheetService.columnToIndex("W"));
//        // 编号（版本）
//        CellRangeAddress versionRange = new CellRangeAddress(3, 3,
//                SheetService.columnToIndex("Y"),
//                SheetService.columnToIndex("AT"));
//        // 款式
//        CellRangeAddress styleRange = new CellRangeAddress(4, 4,
//                SheetService.columnToIndex("D"),
//                SheetService.columnToIndex("H"));
//        // 客户
//        CellRangeAddress customerRange = new CellRangeAddress(4, 4,
//                SheetService.columnToIndex("J"),
//                SheetService.columnToIndex("AT"));
        // 构成
        CellRangeAddress makeUpRange = new CellRangeAddress(10, 10,
                SheetService.columnToIndex("A"),
                SheetService.columnToIndex("C"));
        // 零件基本信息
        CellRangeAddress componentBaseInforRange = new CellRangeAddress(10, 10,
                SheetService.columnToIndex("D"),
                SheetService.columnToIndex("L"));
        // 原料基本信息
        CellRangeAddress materialBaseInfoRange = new CellRangeAddress(10, 10,
                SheetService.columnToIndex("M"),
                SheetService.columnToIndex("S"));
        // 产品信息
        CellRangeAddress productInfoRange = new CellRangeAddress(10, 10,
                SheetService.columnToIndex("T"),
                SheetService.columnToIndex("X"));
        // 设备及作业时间（理论）
        CellRangeAddress theoryInfoRange = new CellRangeAddress(10, 10,
                SheetService.columnToIndex("Y"),
                SheetService.columnToIndex("AA"));
        // 设备及作业时间（厂内实际）
        CellRangeAddress actualInfoRange = new CellRangeAddress(10, 10,
                SheetService.columnToIndex("AB"),
                SheetService.columnToIndex("AG"));
        // 零件供应状况
        CellRangeAddress componentSupplyRange = new CellRangeAddress(10, 10,
                SheetService.columnToIndex("AH"),
                SheetService.columnToIndex("AQ"));
//
        sheet.addMergedRegion(componentSupplyRange);
        sheet.addMergedRegion(actualInfoRange);
        sheet.addMergedRegion(theoryInfoRange);
        sheet.addMergedRegion(productInfoRange);
        sheet.addMergedRegion(materialBaseInfoRange);
        sheet.addMergedRegion(componentBaseInforRange);
        sheet.addMergedRegion(makeUpRange);
//        sheet.addMergedRegion(customerRange);
//        sheet.addMergedRegion(styleRange);
//        sheet.addMergedRegion(versionRange);
//        sheet.addMergedRegion(setDateRange);
//        sheet.addMergedRegion(approveRange);
//        sheet.addMergedRegion(auditRange);
//        sheet.addMergedRegion(FileCodeRange);
//        sheet.addMergedRegion(updateDateRange);
//        sheet.addMergedRegion(setPersonRange);
//        sheet.addMergedRegion(setCompanyRange);
//        sheet.addMergedRegion(nameRange);
//        sheet.addMergedRegion(titleRange);

        // 2.写内容
        // 获取数据
        @SuppressWarnings("unchecked")
        Map<String, Object> data = (Map<String, Object>) list.get(0);
        // bom名称
        XSSFRow firstRow = sheet.createRow(0);
        firstRow.setHeightInPoints(30);
        XSSFCell bomNameTextCell = firstRow
                .createCell(SheetService.columnToIndex("A"));
        bomNameTextCell.setCellStyle(style);
        bomNameTextCell.setCellValue("BOM表名称");

        XSSFCell bomNameCell = firstRow
                .createCell(SheetService.columnToIndex("B"));
        bomNameCell.setCellStyle(style);
        bomNameCell.setCellValue(ObjectUtils.isEmpty(data.get("bomName")) ? ""
                : data.get("bomName").toString());

        // bom代码
        XSSFRow secondRow = sheet.createRow(1);
        secondRow.setHeightInPoints(30);
        XSSFCell bomCodeTextCell = secondRow
                .createCell(SheetService.columnToIndex("A"));
        bomCodeTextCell.setCellStyle(style);
        bomCodeTextCell.setCellValue("BOM代码");

        XSSFCell bomCodeCell = secondRow
                .createCell(SheetService.columnToIndex("B"));
        bomCodeCell.setCellStyle(style);
        bomCodeCell.setCellValue(ObjectUtils.isEmpty(data.get("code")) ? ""
                : data.get("code").toString());

        // 项目
        XSSFRow thirdRow = sheet.createRow(2);
        thirdRow.setHeightInPoints(30);
        XSSFCell projectTextCell = thirdRow
                .createCell(SheetService.columnToIndex("A"));
        projectTextCell.setCellStyle(style);
        projectTextCell.setCellValue("项目");

        XSSFCell projectCell = thirdRow
                .createCell(SheetService.columnToIndex("B"));
        projectCell.setCellStyle(style);
        projectCell.setCellValue(ObjectUtils.isEmpty(data.get("projectName")) ? ""
                : data.get("projectName").toString());

        // 客户名称
        XSSFRow fourthRow = sheet.createRow(3);
        fourthRow.setHeightInPoints(30);
        XSSFCell customerTextCell = fourthRow
                .createCell(SheetService.columnToIndex("A"));
        customerTextCell.setCellStyle(style);
        customerTextCell.setCellValue("客户名称");

        XSSFCell customerCell = fourthRow
                .createCell(SheetService.columnToIndex("B"));
        customerCell.setCellStyle(style);
        customerCell.setCellValue(ObjectUtils.isEmpty(data.get("customerName")) ? ""
                : data.get("customerName").toString());

        // 车型
        XSSFRow fifthRow = sheet.createRow(4);
        fifthRow.setHeightInPoints(30);
        XSSFCell vehicleTypeTextCell = fifthRow
                .createCell(SheetService.columnToIndex("A"));
        vehicleTypeTextCell.setCellStyle(style);
        vehicleTypeTextCell.setCellValue("车型");

        XSSFCell vehicleTypeCell = fifthRow
                .createCell(SheetService.columnToIndex("B"));
        vehicleTypeCell.setCellStyle(style);
        vehicleTypeCell.setCellValue(ObjectUtils.isEmpty(data.get("vehicleType")) ? ""
                : data.get("vehicleType").toString());

        // 零件类型
        XSSFRow sixthRow = sheet.createRow(5);
        sixthRow.setHeightInPoints(30);
        XSSFCell partTypeTextCell = sixthRow
                .createCell(SheetService.columnToIndex("A"));
        partTypeTextCell.setCellStyle(style);
        partTypeTextCell.setCellValue("零件类型");

        XSSFCell partTypeCell = sixthRow
                .createCell(SheetService.columnToIndex("B"));
        partTypeCell.setCellStyle(style);
        partTypeCell.setCellValue(ObjectUtils.isEmpty(data.get("partType")) ? ""
                : data.get("partType").toString());

        // 是否颜色件
        XSSFRow seventhRow = sheet.createRow(6);
        seventhRow.setHeightInPoints(30);
        XSSFCell isColorTextCell = seventhRow
                .createCell(SheetService.columnToIndex("A"));
        isColorTextCell.setCellStyle(style);
        isColorTextCell.setCellValue("是否颜色件");

        XSSFCell isColorCell = seventhRow
                .createCell(SheetService.columnToIndex("B"));
        isColorCell.setCellStyle(style);
        isColorCell.setCellValue(ObjectUtils.isEmpty(data.get("isColor")) ? ""
                : data.get("isColor").toString());

        // BOM类型
        XSSFRow eighthRow = sheet.createRow(7);
        eighthRow.setHeightInPoints(30);
        XSSFCell bomTypeTextCell = eighthRow
                .createCell(SheetService.columnToIndex("A"));
        bomTypeTextCell.setCellStyle(style);
        bomTypeTextCell.setCellValue("BOM类型");

        XSSFCell bomTypeCell = eighthRow
                .createCell(SheetService.columnToIndex("B"));
        bomTypeCell.setCellStyle(style);
        bomTypeCell.setCellValue(ObjectUtils.isEmpty(data.get("bomType")) ? ""
                : data.get("bomType").toString());

        // 修订日期
        XSSFRow ninethRow = sheet.createRow(8);
        ninethRow.setHeightInPoints(30);
        XSSFCell updatedAtTextCell = ninethRow
                .createCell(SheetService.columnToIndex("A"));
        updatedAtTextCell.setCellStyle(style);
        updatedAtTextCell.setCellValue("修订日期");

//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");

        XSSFCell updatedAtCell = ninethRow
                .createCell(SheetService.columnToIndex("B"));
        updatedAtCell.setCellStyle(style);
        updatedAtCell.setCellValue(ObjectUtils.isEmpty(data.get("updatedAt")) ? ""
                : data.get("updatedAt").toString());

        // 版次
        XSSFRow tenthRow = sheet.createRow(9);
        tenthRow.setHeightInPoints(30);
        XSSFCell versionTextCell = tenthRow
                .createCell(SheetService.columnToIndex("A"));
        versionTextCell.setCellStyle(style);
        versionTextCell.setCellValue("版次");

        XSSFCell versionCell = tenthRow
                .createCell(SheetService.columnToIndex("B"));
        versionCell.setCellStyle(style);
        versionCell.setCellValue(ObjectUtils.isEmpty(data.get("bomVersion")) ? ""
                : data.get("bomVersion").toString());
        //台账ID（和版次同一行）
        XSSFCell bomListIdTextCell = tenthRow
                .createCell(SheetService.columnToIndex("AW"));
        bomListIdTextCell.setCellStyle(style);
        bomListIdTextCell.setCellValue("台账ID");

        XSSFCell bomListIdCell = tenthRow
                .createCell(SheetService.columnToIndex("AX"));
        bomListIdCell.setCellStyle(style);
        bomListIdCell.setCellValue(ObjectUtils.isEmpty(data.get("bomListId")) ? ""
                : data.get("bomListId").toString());

        // 构成
        XSSFRow eleventh = sheet.createRow(10);
        XSSFCell makeUpCell = eleventh
                .createCell(SheetService.columnToIndex("A"));
        makeUpCell.setCellStyle(style);
        makeUpCell.setCellValue("构成");
        // 1、零件基本信息
        XSSFCell compnenetBaseInfoCell = eleventh
                .createCell(SheetService.columnToIndex("D"));
        compnenetBaseInfoCell.setCellStyle(style);
        compnenetBaseInfoCell.setCellValue("1、零件基本信息");
        // 2、原材料基本资料
        XSSFCell materialBaseInfoCell = eleventh
                .createCell(SheetService.columnToIndex("M"));
        materialBaseInfoCell.setCellStyle(style);
        materialBaseInfoCell.setCellValue("2、原材料基本资料");
        // 3.1、产品信息
        XSSFCell productCell = eleventh
                .createCell(SheetService.columnToIndex("T"));
        productCell.setCellStyle(style);
        productCell.setCellValue("3、产品信息");
        // 3.2、原材料信息
//        XSSFCell materialCell = sixthRow
//                .createCell(SheetService.columnToIndex("T"));
//        materialCell.setCellStyle(style);
//        materialCell.setCellValue("3、原材料信息");
        // 4.1、设备及作业时间（理论）
        XSSFCell theoryInfoCell = eleventh
                .createCell(SheetService.columnToIndex("Y"));
        theoryInfoCell.setCellStyle(style);
        theoryInfoCell.setCellValue("4.1、设备及作业时间（理论）");
        // 4.2、设备及作业时间（厂内实际）
        XSSFCell actualInfoCell = eleventh
                .createCell(SheetService.columnToIndex("AB"));
        actualInfoCell.setCellStyle(style);
        actualInfoCell.setCellValue("4.2、设备及作业时间（厂内实际）");
        // 5、零件供应状况
        XSSFCell componentSupplyCell = eleventh
                .createCell(SheetService.columnToIndex("AH"));
        componentSupplyCell.setCellStyle(style);
        componentSupplyCell.setCellValue("5、零件供应状况");

        // 3.设置合并单元格边框
        setRegionBorder(makeUpRange, sheet);
//        setRegionBorder(titleRange, sheet);
//        setRegionBorder(nameRange, sheet);
//        setRegionBorder(setCompanyRange, sheet);
//        setRegionBorder(setPersonRange, sheet);
//        setRegionBorder(updateDateRange, sheet);
//        setRegionBorder(FileCodeRange, sheet);
//        setRegionBorder(auditRange, sheet);
//        setRegionBorder(approveRange, sheet);
//        setRegionBorder(setDateRange, sheet);
//        setRegionBorder(versionRange, sheet);
//        setRegionBorder(styleRange, sheet);
//        setRegionBorder(customerRange, sheet);
        setRegionBorder(componentBaseInforRange, sheet);
        setRegionBorder(materialBaseInfoRange, sheet);
        setRegionBorder(productInfoRange, sheet);
        setRegionBorder(theoryInfoRange, sheet);
        setRegionBorder(actualInfoRange, sheet);
        setRegionBorder(componentSupplyRange, sheet);

        return 10 + 1;
    }

    @Override
    public <T> void export(String excelName, List<T> list,
                           LinkedHashMap<String, String> fieldMap,
                           HttpServletResponse response)
    {

        // 设置默认文件名为当前时间：年月日时分秒
        if (excelName == null || excelName == "")
        {
            excelName = new SimpleDateFormat("yyyyMMddhhmmss")
                    .format(new Date()).toString();
        }
        // 设置response头信息
        response.reset();
        response.setContentType("application/vnd.ms-excel"); // 改成输出excel文件
        try
        {
            response.setHeader("Content-disposition", "attachment; filename="
                    + new String(excelName.getBytes("gb2312"), "ISO-8859-1")
                    + ".xlsx");
        }
        catch (UnsupportedEncodingException e1)
        {

        }

        // 创建一个WorkBook,对应一个Excel文件
        @SuppressWarnings("resource")
        XSSFWorkbook wb = new XSSFWorkbook();
        // 在Workbook中，创建一个sheet，对应Excel中的工作薄（sheet）
        XSSFSheet sheet = wb.createSheet("Sheet1");
        // 创建单元格，并设置值表头 设置表头居中
        XSSFCellStyle style = wb.createCellStyle();
        // 创建一个居中格式
        style.setAlignment(HorizontalAlignment.CENTER);
        // 字体格式
        XSSFFont font = wb.createFont();
        // 加粗
        font.setBold(true);
        style.setFont(font);
        // 加边框
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        // 自动换行
        style.setWrapText(true);
        try
        {
            // 定制表头
            int headIndex = customizedHead(sheet, list);
            // 填充工作表
            fillSheet(sheet, list, fieldMap, style, headIndex);

            // 将文件输出
            OutputStream ouputStream = response.getOutputStream();
            wb.write(ouputStream);
            ouputStream.flush();
            ouputStream.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            XSSFRow row = sheet.createRow(0);
            row.getCell(0).setCellValue("导出Excel失败！请联系管理员");
            // 将文件输出
            OutputStream ouputStream;
            try
            {
                ouputStream = response.getOutputStream();
                wb.write(ouputStream);
                ouputStream.flush();
                ouputStream.close();
            }
            catch (IOException e1)
            {
                e1.printStackTrace();
            }
            throw new ServiceException("导出Excel失败！请联系管理员");
        }
    }

    /**
     * 导出Excel，针对数值替换成常规格式，而不是默认导出数值型
     *
     * @param excelName 要导出的excel名称
     * @param list      要导出的数据集合
     * @param fieldMap  中英文字段对应Map，即要导出的excel表头
     * @param response  使用response可以导出到浏览器
     * @param strList  需要特别标识为字符串的列
     * @param <T>
     */
    @Override
    public <T> void exportNum(String excelName, List<T> list,
                              LinkedHashMap<String, String> fieldMap,
                              HttpServletResponse response, List<Integer> strList, List<Integer> keyList)
    {

        // 设置默认文件名为当前时间：年月日时分秒
        if (excelName == null || excelName == "")
        {
            excelName = new SimpleDateFormat("yyyyMMddhhmmss")
                    .format(new Date()).toString();
        }
        // 设置response头信息
        response.reset();
        response.setContentType("application/vnd.ms-excel"); // 改成输出excel文件
        try
        {
            response.setHeader("Content-disposition", "attachment; filename="
                    + new String(excelName.getBytes("gb2312"), "ISO-8859-1")
                    + ".xlsx");
        }
        catch (UnsupportedEncodingException e1)
        {

        }

        // 创建一个WorkBook,对应一个Excel文件
        @SuppressWarnings("resource")
        XSSFWorkbook wb = new XSSFWorkbook();
        // 在Workbook中，创建一个sheet，对应Excel中的工作薄（sheet）
        XSSFSheet sheet = wb.createSheet("Sheet1");
        // 创建单元格，并设置值表头 设置表头居中
        XSSFCellStyle style = wb.createCellStyle();
        // 创建一个居中格式
        style.setAlignment(HorizontalAlignment.CENTER);
        // 字体格式
        XSSFFont font = wb.createFont();
        // 加粗
        font.setBold(true);
        style.setFont(font);
        // 加边框
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        // 设置导出颜色
        style.setWrapText(true);
        XSSFCellStyle styleColor = wb.createCellStyle();
        styleColor.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
        styleColor.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        styleColor.setAlignment(HorizontalAlignment.LEFT);
        styleColor.setVerticalAlignment(VerticalAlignment.CENTER);
        //设置行的颜色
        XSSFCellStyle rowStyleColor = wb.createCellStyle();
        rowStyleColor.setFillForegroundColor(IndexedColors.SKY_BLUE.getIndex());
        rowStyleColor.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        rowStyleColor.setAlignment(HorizontalAlignment.LEFT);
        rowStyleColor.setVerticalAlignment(VerticalAlignment.CENTER);
        //设置行的颜色
        XSSFCellStyle rowDeliveryStyleColor = wb.createCellStyle();
        rowDeliveryStyleColor.setFillForegroundColor(IndexedColors.SEA_GREEN.getIndex());
        rowDeliveryStyleColor.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        rowDeliveryStyleColor.setAlignment(HorizontalAlignment.LEFT);
        rowDeliveryStyleColor.setVerticalAlignment(VerticalAlignment.CENTER);
//        // 设置对齐方式
//        XSSFCellStyle alignStyle = wb.createCellStyle();
//        alignStyle.setAlignment(HorizontalAlignment.LEFT);
//        alignStyle.setBorderBottom(BorderStyle.THIN);
//        alignStyle.setBorderLeft(BorderStyle.THIN);
//        alignStyle.setBorderTop(BorderStyle.THIN);
//        alignStyle.setBorderRight(BorderStyle.THIN);
        try
        {
            // 定制表头
            int headIndex = customizedHead(sheet, list);
            // 填充工作表
            fillSheetNum(sheet, list, fieldMap, style, headIndex, strList, styleColor, keyList,rowStyleColor,rowDeliveryStyleColor);

//            sheet.setColumnWidth(0,15);
//            sheet.setColumnWidth(1,30);
//            sheet.setColumnWidth(3,20);
//            sheet.setColumnWidth(4,25);
//            sheet.setColumnWidth(6,25);
            // 将文件输出
            OutputStream ouputStream = response.getOutputStream();
            wb.write(ouputStream);
            ouputStream.flush();
            ouputStream.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            XSSFRow row = sheet.createRow(0);
            row.getCell(0).setCellValue("导出Excel失败！请联系管理员");
            // 将文件输出
            OutputStream ouputStream;
            try
            {
                ouputStream = response.getOutputStream();
                wb.write(ouputStream);
                ouputStream.flush();
                ouputStream.close();
            }
            catch (IOException e1)
            {
                e1.printStackTrace();
            }
            throw new ServiceException("导出Excel失败！请联系管理员");
        }
    }

    /**
     * 向工作表中填充数据，根据不同的类型，导出不同的格式，数值型就导出数值型
     *
     * @param sheet     excel的工作表名称
     * @param list      数据源
     * @param fieldMap  中英文字段对应关系的Map
     * @param style     表格中的格式
     * @param headIndex 表头开始
     * @param strList 需要特别标识为字符的列
     * @throws Exception 异常
     */
    public <T> void fillSheetNum(XSSFSheet sheet, List<T> list,
                                 LinkedHashMap<String, String> fieldMap, XSSFCellStyle style,
                                 int headIndex, List<Integer> strList, XSSFCellStyle styleColor,
                                 List<Integer> keyList,XSSFCellStyle rowStyleColor,XSSFCellStyle rowDeliveryStyleColor) throws Exception
    {
        // 定义存放英文字段名和中文字段名的数组
        String[] enFields = new String[fieldMap.size()];
        String[] cnFields = new String[fieldMap.size()];

        // 填充数组
        int count = 0;
        for (Map.Entry<String, String> entry : fieldMap.entrySet())
        {
            enFields[count] = entry.getKey();
            cnFields[count] = entry.getValue();
            count++;
        }

        // 在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
        XSSFRow row = sheet.createRow(headIndex);

        // 填充表头
        for (int i = 0; i < cnFields.length; i++)
        {
            XSSFCell cell = row.createCell(i);
            cell.setCellValue(cnFields[i]);
            cell.setCellStyle(style);
        }

        // 填充内容
        for (int index = 0; index < list.size(); index++)
        {
            Boolean isDeliveryRow = false;
            row = sheet.createRow(++headIndex);
            row.setHeightInPoints(40);
            // 获取单个对象
            T item = list.get(index);
            for (int i = 0; i < enFields.length; i++)
            {
                Object objValue = getFieldValueByNameSequence(enFields[i], item);
                String fieldValue = objValue == null ? "" : objValue.toString();
                XSSFCell cell = row.createCell(i);
                try
                {
                    cell.setCellValue(fieldValue);
                }
                catch (Exception e)
                {
                    cell.setCellValue(fieldValue);
                }

                if (enFields[i].equals("cjs")) {
                    if(fieldValue.equals("1"))
                    {
                        isDeliveryRow = true;
                    }
                }

                //如果是变更行，则改变颜色 2022-06-08
                if (enFields[i].equals("isChanged")) {
                    if(fieldValue.equals("1"))
                    {
                        for (int j = 0; j < 5; j++) {
                            row.getCell(j).setCellStyle(rowStyleColor);
                        }
                    }
                    if(isDeliveryRow)
                    {
                        for (int j = 1; j < 40; j++) {
                            row.getCell(j).setCellStyle(rowDeliveryStyleColor);
                        }
                    }
                }

                if (!ObjectUtils.isEmpty(keyList) && keyList.contains(i) && ObjectUtils.isEmpty(fieldValue)) {
                    cell.setCellStyle(styleColor);
                }
            }
        }
    }
}
