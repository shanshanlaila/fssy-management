/**   
 * ------------------------修改日志---------------------------------
 * 修改人                 修改日期                   修改内容
 */
package com.fssy.shareholder.management.service.common.override;

import java.util.List;
import java.util.Map;

import com.fssy.shareholder.management.service.common.SheetOutputService;
import com.fssy.shareholder.management.service.common.SheetService;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.ObjectUtils;

import com.fssy.shareholder.management.tools.common.InstandTool;

/**
 * @Title: InventoryCheckingSheetOutPutService.java
 * @Description: 库存盘点表导出格式自定义类  
 * @author Solomon  
 * @date 2022年6月10日 下午12:40:16 
 */
public class InventoryCheckingSheetOutPutService extends SheetOutputService
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
		short dateFormat = createHelper.createDataFormat().getFormat("yyyy-MM-dd");
		dateStyle.setDataFormat(dateFormat);
		
		// 1.写内容
		// 获取数据
		@SuppressWarnings("unchecked")
		Map<String, Object> data = (Map<String, Object>) list.get(0);
		// 第一行
		XSSFRow firstRow = sheet.createRow(0);
		// 盘点台账序号
		XSSFCell titleCell = firstRow.createCell(SheetService.columnToIndex("A"));
		titleCell.setCellStyle(style);
		titleCell.setCellValue("盘点表序号");
		XSSFCell titleCellText = firstRow.createCell(SheetService.columnToIndex("B"));
		titleCellText.setCellStyle(style);
		titleCellText
				.setCellValue(InstandTool.objectToString(data.get("warehouseInventoryListId")));
		// 盘点日期
		XSSFCell checkDateCell = firstRow.createCell(SheetService.columnToIndex("C"));
		checkDateCell.setCellStyle(style);
		checkDateCell.setCellValue("盘点日期");
		XSSFCell checkDateCellText = firstRow.createCell(SheetService.columnToIndex("D"));
		checkDateCellText.setCellStyle(dateStyle);
		checkDateCellText.setCellValue(ObjectUtils.isEmpty(data.get("checkingDate")) ? ""
				: data.get("checkingDate").toString());
		// 盘点仓库
		XSSFCell warehouseCell = firstRow.createCell(SheetService.columnToIndex("E"));
		warehouseCell.setCellStyle(style);
		warehouseCell.setCellValue("盘点仓库");
		XSSFCell warehouseCellText = firstRow.createCell(SheetService.columnToIndex("F"));
		warehouseCellText.setCellStyle(style);
		warehouseCellText.setCellValue(InstandTool.objectToString(data.get("warehouseName")));
		return 1;
	}


}
