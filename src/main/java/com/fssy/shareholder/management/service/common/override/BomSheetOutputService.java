/**
 * 
 */
package com.fssy.shareholder.management.service.common.override;

import java.util.List;
import java.util.Map;

import com.fssy.shareholder.management.service.common.SheetOutputService;
import com.fssy.shareholder.management.service.common.SheetService;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.ObjectUtils;

/**
 * bom导出业务类
 * 
 * @author Solomon
 */
public class BomSheetOutputService extends SheetOutputService
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
		// 标题
		CellRangeAddress titleRange = new CellRangeAddress(0, 1,
				SheetService.columnToIndex("A"),
				SheetService.columnToIndex("AO"));
		// Bom名称
		CellRangeAddress nameRange = new CellRangeAddress(2, 3,
				SheetService.columnToIndex("A"),
				SheetService.columnToIndex("H"));
		// 制定单位
		CellRangeAddress setCompanyRange = new CellRangeAddress(2, 2,
				SheetService.columnToIndex("J"),
				SheetService.columnToIndex("L"));
		// 编制人
		CellRangeAddress setPersonRange = new CellRangeAddress(2, 2,
				SheetService.columnToIndex("N"),
				SheetService.columnToIndex("O"));
		// 修订日期
		CellRangeAddress updateDateRange = new CellRangeAddress(2, 2,
				SheetService.columnToIndex("Q"),
				SheetService.columnToIndex("U"));
		// 文件编号
		CellRangeAddress FileCodeRange = new CellRangeAddress(2, 2,
				SheetService.columnToIndex("W"),
				SheetService.columnToIndex("AO"));
		// 审核
		CellRangeAddress auditRange = new CellRangeAddress(3, 3,
				SheetService.columnToIndex("J"),
				SheetService.columnToIndex("L"));
		// 批准
		CellRangeAddress approveRange = new CellRangeAddress(3, 3,
				SheetService.columnToIndex("N"),
				SheetService.columnToIndex("O"));
		// 制定日期
		CellRangeAddress setDateRange = new CellRangeAddress(3, 3,
				SheetService.columnToIndex("Q"),
				SheetService.columnToIndex("U"));
		// 编号（版本）
		CellRangeAddress versionRange = new CellRangeAddress(3, 3,
				SheetService.columnToIndex("W"),
				SheetService.columnToIndex("AO"));
		// 款式
		CellRangeAddress styleRange = new CellRangeAddress(4, 4,
				SheetService.columnToIndex("D"),
				SheetService.columnToIndex("H"));
		// 客户
		CellRangeAddress customerRange = new CellRangeAddress(4, 4,
				SheetService.columnToIndex("J"),
				SheetService.columnToIndex("AO"));
		// 构成
		CellRangeAddress makeUpRange = new CellRangeAddress(5, 5,
				SheetService.columnToIndex("A"),
				SheetService.columnToIndex("B"));
		// 零件基本信息
		CellRangeAddress componentBaseInforRange = new CellRangeAddress(5, 5,
				SheetService.columnToIndex("C"),
				SheetService.columnToIndex("K"));
		// 原料基本信息
		CellRangeAddress materialBaseInfoRange = new CellRangeAddress(5, 5,
				SheetService.columnToIndex("L"),
				SheetService.columnToIndex("P"));
		// 产品信息
		CellRangeAddress productInfoRange = new CellRangeAddress(5, 5,
				SheetService.columnToIndex("Q"),
				SheetService.columnToIndex("S"));
		// 设备及作业时间（理论）
		CellRangeAddress theoryInfoRange = new CellRangeAddress(5, 5,
				SheetService.columnToIndex("U"),
				SheetService.columnToIndex("X"));
		// 设备及作业时间（厂内实际）
		CellRangeAddress actualInfoRange = new CellRangeAddress(5, 5,
				SheetService.columnToIndex("Y"),
				SheetService.columnToIndex("AD"));
		// 零件供应状况
		CellRangeAddress componentSupplyRange = new CellRangeAddress(5, 5,
				SheetService.columnToIndex("AE"),
				SheetService.columnToIndex("AJ"));

		sheet.addMergedRegion(componentSupplyRange);
		sheet.addMergedRegion(actualInfoRange);
		sheet.addMergedRegion(theoryInfoRange);
		sheet.addMergedRegion(productInfoRange);
		sheet.addMergedRegion(materialBaseInfoRange);
		sheet.addMergedRegion(componentBaseInforRange);
		sheet.addMergedRegion(makeUpRange);
		sheet.addMergedRegion(customerRange);
		sheet.addMergedRegion(styleRange);
		sheet.addMergedRegion(versionRange);
		sheet.addMergedRegion(setDateRange);
		sheet.addMergedRegion(approveRange);
		sheet.addMergedRegion(auditRange);
		sheet.addMergedRegion(FileCodeRange);
		sheet.addMergedRegion(updateDateRange);
		sheet.addMergedRegion(setPersonRange);
		sheet.addMergedRegion(setCompanyRange);
		sheet.addMergedRegion(nameRange);
		sheet.addMergedRegion(titleRange);

		// 2.写内容
		// 获取数据
		@SuppressWarnings("unchecked")
		Map<String, Object> data = (Map<String, Object>) list.get(0);
		// 标题
		XSSFCell titleCell = sheet.createRow(0)
				.createCell(SheetService.columnToIndex("A"));
		titleCell.setCellStyle(style);
		titleCell.setCellValue("柳州裕信方盛汽车饰件有限公司");
		// bom名称
		XSSFRow thirdRow = sheet.createRow(2);
		XSSFCell nameCell = thirdRow
				.createCell(SheetService.columnToIndex("A"));
		nameCell.setCellStyle(style);
		nameCell.setCellValue(ObjectUtils.isEmpty(data.get("assemblyName")) ? ""
				: data.get("assemblyName").toString());
		// 制定单位
		XSSFCell prepareCompanyCell = thirdRow
				.createCell(SheetService.columnToIndex("I"));
		prepareCompanyCell.setCellStyle(style);
		prepareCompanyCell.setCellValue("制定单位");
		XSSFCell prepareCompanyTextCell = thirdRow
				.createCell(SheetService.columnToIndex("J"));
		prepareCompanyTextCell.setCellStyle(style);
		prepareCompanyTextCell.setCellValue(
				ObjectUtils.isEmpty(data.get("prepareCompany")) ? ""
						: data.get("prepareCompany").toString());
		// 编制人
		XSSFCell preparePersonCell = thirdRow
				.createCell(SheetService.columnToIndex("M"));
		preparePersonCell.setCellStyle(style);
		preparePersonCell.setCellValue("编制");
		XSSFCell preparePersonTextCell = thirdRow
				.createCell(SheetService.columnToIndex("N"));
		preparePersonTextCell.setCellStyle(style);
		preparePersonTextCell
				.setCellValue(ObjectUtils.isEmpty(data.get("prepareName")) ? ""
						: data.get("prepareName").toString());
		// 修订日期
		XSSFCell updateDateCell = thirdRow
				.createCell(SheetService.columnToIndex("P"));
		updateDateCell.setCellStyle(style);
		updateDateCell.setCellValue("修订日期");
		XSSFCell updateDateTextCell = thirdRow
				.createCell(SheetService.columnToIndex("Q"));
		updateDateTextCell.setCellStyle(dateStyle);
		updateDateTextCell
				.setCellValue(ObjectUtils.isEmpty(data.get("updatedDate")) ? ""
						: data.get("updatedDate").toString());
		// 文件编号
		XSSFCell fileCodeCell = thirdRow
				.createCell(SheetService.columnToIndex("V"));
		fileCodeCell.setCellStyle(style);
		fileCodeCell.setCellValue("文件编号");
		XSSFCell fileCodeTextCell = thirdRow
				.createCell(SheetService.columnToIndex("W"));
		fileCodeTextCell.setCellStyle(style);
		fileCodeTextCell
				.setCellValue(ObjectUtils.isEmpty(data.get("fileCode")) ? ""
						: data.get("fileCode").toString());
		// 审核
		XSSFRow forthRow = sheet.createRow(3);
		XSSFCell auditCell = forthRow
				.createCell(SheetService.columnToIndex("I"));
		auditCell.setCellStyle(style);
		auditCell.setCellValue("审核");
		XSSFCell auditTextCell = forthRow
				.createCell(SheetService.columnToIndex("J"));
		auditTextCell.setCellStyle(style);
		auditTextCell
				.setCellValue(ObjectUtils.isEmpty(data.get("auditName")) ? ""
						: data.get("auditName").toString());
		// 批准
		XSSFCell approveCell = forthRow
				.createCell(SheetService.columnToIndex("M"));
		approveCell.setCellStyle(style);
		approveCell.setCellValue("批准");
		XSSFCell approveTextCell = forthRow
				.createCell(SheetService.columnToIndex("N"));
		approveTextCell.setCellStyle(style);
		approveTextCell
				.setCellValue(ObjectUtils.isEmpty(data.get("approveName")) ? ""
						: data.get("approveName").toString());
		// 制定日期
		XSSFCell setDateCell = forthRow
				.createCell(SheetService.columnToIndex("P"));
		setDateCell.setCellStyle(style);
		setDateCell.setCellValue("制定日期");
		XSSFCell setDateTextCell = forthRow
				.createCell(SheetService.columnToIndex("Q"));
		setDateTextCell.setCellStyle(dateStyle);
		setDateTextCell
				.setCellValue(ObjectUtils.isEmpty(data.get("setDate")) ? ""
						: data.get("setDate").toString());
		// 编号
		XSSFCell versionCell = forthRow
				.createCell(SheetService.columnToIndex("V"));
		versionCell.setCellStyle(style);
		versionCell.setCellValue("编号");
		XSSFCell versionTextCell = forthRow
				.createCell(SheetService.columnToIndex("W"));
		versionTextCell.setCellStyle(style);
		versionTextCell
				.setCellValue(ObjectUtils.isEmpty(data.get("version")) ? ""
						: data.get("version").toString());
		// 车型
		XSSFRow fifthRow = sheet.createRow(4);
		XSSFCell modelCell = fifthRow
				.createCell(SheetService.columnToIndex("A"));
		modelCell.setCellStyle(style);
		modelCell.setCellValue("车型");
		XSSFCell modelTextCell = fifthRow
				.createCell(SheetService.columnToIndex("B"));
		modelTextCell.setCellStyle(style);
		modelTextCell.setCellValue(
				ObjectUtils.isEmpty(data.get("vehicleTypeName")) ? ""
						: data.get("vehicleTypeName").toString());
		// 款式
		XSSFCell styleCell = fifthRow
				.createCell(SheetService.columnToIndex("C"));
		styleCell.setCellStyle(style);
		styleCell.setCellValue("款式");
		XSSFCell makeUpTextCell = fifthRow
				.createCell(SheetService.columnToIndex("D"));
		makeUpTextCell.setCellStyle(style);
		makeUpTextCell
				.setCellValue(ObjectUtils.isEmpty(data.get("styleName")) ? ""
						: data.get("styleName").toString());
		// 客户
		XSSFCell customerCell = fifthRow
				.createCell(SheetService.columnToIndex("I"));
		customerCell.setCellStyle(style);
		customerCell.setCellValue("客户");
		XSSFCell customerTextCell = fifthRow
				.createCell(SheetService.columnToIndex("J"));
		customerTextCell.setCellStyle(style);
		customerTextCell
				.setCellValue(ObjectUtils.isEmpty(data.get("customerName")) ? ""
						: data.get("customerName").toString());
		// 构成
		XSSFRow sixthRow = sheet.createRow(5);
		XSSFCell makeUpCell = sixthRow
				.createCell(SheetService.columnToIndex("A"));
		makeUpCell.setCellStyle(style);
		makeUpCell.setCellValue("构成");
		// 1、零件基本信息
		XSSFCell compnenetBaseInfoCell = sixthRow
				.createCell(SheetService.columnToIndex("C"));
		compnenetBaseInfoCell.setCellStyle(style);
		compnenetBaseInfoCell.setCellValue("1、零件基本信息");
		// 2、原材料基本资料
		XSSFCell materialBaseInfoCell = sixthRow
				.createCell(SheetService.columnToIndex("L"));
		materialBaseInfoCell.setCellStyle(style);
		materialBaseInfoCell.setCellValue("2、原材料基本资料");
		// 3.1、产品信息
		XSSFCell productCell = sixthRow
				.createCell(SheetService.columnToIndex("Q"));
		productCell.setCellStyle(style);
		productCell.setCellValue("3.1、产品信息");
		// 3.2、原材料信息
		XSSFCell materialCell = sixthRow
				.createCell(SheetService.columnToIndex("T"));
		materialCell.setCellStyle(style);
		materialCell.setCellValue("3.2、原材料信息");
		// 4.1、设备及作业时间（理论）
		XSSFCell theoryInfoCell = sixthRow
				.createCell(SheetService.columnToIndex("U"));
		theoryInfoCell.setCellStyle(style);
		theoryInfoCell.setCellValue("4.1、设备及作业时间（理论）");
		// 4.2、设备及作业时间（厂内实际）
		XSSFCell actualInfoCell = sixthRow
				.createCell(SheetService.columnToIndex("Y"));
		actualInfoCell.setCellStyle(style);
		actualInfoCell.setCellValue("4.2、设备及作业时间（厂内实际）");
		// 5、零件供应状况
		XSSFCell componentSupplyCell = sixthRow
				.createCell(SheetService.columnToIndex("AE"));
		componentSupplyCell.setCellStyle(style);
		componentSupplyCell.setCellValue("5、零件供应状况");

		// 3.设置合并单元格边框
		setRegionBorder(makeUpRange, sheet);
		setRegionBorder(titleRange, sheet);
		setRegionBorder(nameRange, sheet);
		setRegionBorder(setCompanyRange, sheet);
		setRegionBorder(setPersonRange, sheet);
		setRegionBorder(updateDateRange, sheet);
		setRegionBorder(FileCodeRange, sheet);
		setRegionBorder(auditRange, sheet);
		setRegionBorder(approveRange, sheet);
		setRegionBorder(setDateRange, sheet);
		setRegionBorder(versionRange, sheet);
		setRegionBorder(styleRange, sheet);
		setRegionBorder(customerRange, sheet);
		setRegionBorder(componentBaseInforRange, sheet);
		setRegionBorder(materialBaseInfoRange, sheet);
		setRegionBorder(productInfoRange, sheet);
		setRegionBorder(theoryInfoRange, sheet);
		setRegionBorder(actualInfoRange, sheet);
		setRegionBorder(componentSupplyRange, sheet);
		return 5 + 1;
	}

}
