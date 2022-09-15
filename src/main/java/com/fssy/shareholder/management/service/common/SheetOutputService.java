/**
 * ------------------------修改日志---------------------------------
 * 修改人                 修改日期                   修改内容
 * 班群蔚		    2022-4-21 	         导出excel进行数值处理，数值型转成常规，而不是字符型
 */
package com.fssy.shareholder.management.service.common;

import com.fssy.shareholder.management.tools.exception.ServiceException;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * excel导出业务类
 * 
 * @author Solomon
 */
@Service
public class SheetOutputService
{
	private static final Logger LOGGER = LoggerFactory
			.getLogger(SheetOutputService.class);

	/**
	 * 导出Excel
	 *
	 * @param excelName 要导出的excel名称
	 * @param list      要导出的数据集合
	 * @param fieldMap  中英文字段对应Map，即要导出的excel表头
	 * @param response  使用response可以导出到浏览器
	 * @param <T>
	 */
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
			LOGGER.info(e1.getMessage());
		}

		// 创建一个WorkBook,对应一个Excel文件
		@SuppressWarnings("resource")
		XSSFWorkbook wb = new XSSFWorkbook();
		// 在Workbook中，创建一个sheet，对应Excel中的工作薄（sheet）
		XSSFSheet sheet = wb.createSheet(excelName);
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
			LOGGER.error(e.getMessage());
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
	 * 自定义表头
	 * 
	 * @param sheet excel的工作表名称
	 * @param list  数据源
	 * @return 返回起始位置
	 */
	public <T> int customizedHead(XSSFSheet sheet, List<T> list)
	{
		return 0;
	}

	/**
	 * 向工作表中填充数据
	 *
	 * @param sheet     excel的工作表名称
	 * @param list      数据源
	 * @param fieldMap  中英文字段对应关系的Map
	 * @param style     表格中的格式
	 * @param headIndex 表头开始
	 * @throws Exception 异常
	 */
	public <T> void fillSheet(XSSFSheet sheet, List<T> list,
			LinkedHashMap<String, String> fieldMap, XSSFCellStyle style,
			int headIndex) throws Exception
	{
		LOGGER.info("向工作表中填充数据:fillSheet()");
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
//			sheet.autoSizeColumn(i);
		}

		// 填充内容
		for (int index = 0; index < list.size(); index++)
		{
			row = sheet.createRow(++headIndex);
			// 获取单个对象
			T item = list.get(index);
			for (int i = 0; i < enFields.length; i++)
			{
				Object objValue = getFieldValueByNameSequence(enFields[i],
						item);
				String fieldValue = objValue == null ? "" : objValue.toString();

				row.createCell(i).setCellValue(fieldValue);
//				sheet.autoSizeColumn(i);
			}
		}
	}

	/**
	 * 根据带路径或不带路径的属性名获取属性值,即接受简单属性名，
	 * 如userName等，又接受带路径的属性名，如student.department.name等
	 *
	 * @param fieldNameSequence 带路径的属性名或简单属性名
	 * @param o                 对象
	 * @return 属性值
	 * @throws Exception 异常
	 */
	public Object getFieldValueByNameSequence(String fieldNameSequence,
			Object o) throws Exception
	{
		LOGGER.info(
				"根据带路径或不带路径的属性名获取属性值,即接受简单属性名:getFieldValueByNameSequence()");
		Object value = null;

		// 将fieldNameSequence进行拆分
		String[] attributes = fieldNameSequence.split("\\.");
		if (attributes.length == 1)
		{
			value = getFieldValueByName(fieldNameSequence, o);
		}
		else
		{
			// 根据数组中第一个连接属性名获取连接属性对象，如student.department.name
			Object fieldObj = getFieldValueByName(attributes[0], o);
			// 截取除第一个属性名之后的路径
			String subFieldNameSequence = fieldNameSequence
					.substring(fieldNameSequence.indexOf(".") + 1);
			// 递归得到最终的属性对象的值
			value = getFieldValueByNameSequence(subFieldNameSequence, fieldObj);
		}
		return value;

	}

	/**
	 * 根据字段名获取字段值
	 *
	 * @param fieldName 字段名
	 * @param o         对象
	 * @return 字段值
	 * @throws Exception 异常
	 */
	public Object getFieldValueByName(String fieldName, Object o)
			throws Exception
	{

		LOGGER.info("根据字段名获取字段值:getFieldValueByName()");
		Object value = null;
		// 如果map则使用get()
		if (o instanceof Map<?, ?>)
		{
			value = ((Map) o).get(fieldName);
			return value;
		}
		// 根据字段名得到字段对象
		Field field = getFieldByName(fieldName, o.getClass());

		// 如果该字段存在，则取出该字段的值
		if (field != null)
		{
			field.setAccessible(true);// 类中的成员变量为private,在类外边使用属性值，故必须进行此操作
			value = field.get(o);// 获取当前对象中当前Field的value
		}
		else
		{
			throw new Exception(
					o.getClass().getSimpleName() + "类不存在字段名 " + fieldName);
		}

		return value;
	}

	/**
	 * 根据字段名获取字段对象
	 *
	 * @param fieldName 字段名
	 * @param clazz     包含该字段的类
	 * @return 字段
	 */
	public Field getFieldByName(String fieldName, Class<?> clazz)
	{
		LOGGER.info("根据字段名获取字段对象:getFieldByName()");
		// 拿到本类的所有字段
		Field[] selfFields = clazz.getDeclaredFields();

		// 如果本类中存在该字段，则返回
		for (Field field : selfFields)
		{
			// 如果本类中存在该字段，则返回
			if (field.getName().equals(fieldName))
			{
				return field;
			}
		}

		// 否则，查看父类中是否存在此字段，如果有则返回
		Class<?> superClazz = clazz.getSuperclass();
		if (superClazz != null && superClazz != Object.class)
		{
			// 递归
			return getFieldByName(fieldName, superClazz);
		}

		// 如果本类和父类都没有，则返回空
		return null;
	}

	/**
	 * 设置合并区域边框
	 * 
	 * @param region 合并区域
	 * @param sheet  表单对象
	 */
	protected void setRegionBorder(CellRangeAddress region, XSSFSheet sheet)
	{
		RegionUtil.setBorderBottom(BorderStyle.THIN, region, sheet);
		RegionUtil.setBorderLeft(BorderStyle.THIN, region, sheet);
		RegionUtil.setBorderTop(BorderStyle.THIN, region, sheet);
		RegionUtil.setBorderRight(BorderStyle.THIN, region, sheet);
	}

	/**
	 * 设置单个单元格边框
	 * 
	 * @param style 单元格样式
	 * @param sheet 表单对象
	 */
	protected void setBorder(XSSFCellStyle style, XSSFSheet sheet)
	{
		style.setBorderBottom(BorderStyle.THIN);
		style.setBorderLeft(BorderStyle.THIN);
		style.setBorderTop(BorderStyle.THIN);
		style.setBorderRight(BorderStyle.THIN);
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
			LOGGER.info(e1.getMessage());
		}

		// 创建一个WorkBook,对应一个Excel文件
		@SuppressWarnings("resource")
		XSSFWorkbook wb = new XSSFWorkbook();
		// 在Workbook中，创建一个sheet，对应Excel中的工作薄（sheet）
		XSSFSheet sheet = wb.createSheet(excelName);
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
		// 设置导出颜色
		style.setWrapText(true);
		XSSFCellStyle styleColor = wb.createCellStyle();
		styleColor.setFillForegroundColor(IndexedColors.LIGHT_ORANGE.getIndex());
		styleColor.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		try
		{
			// 定制表头
			int headIndex = customizedHead(sheet, list);
			// 填充工作表
			fillSheetNum(sheet, list, fieldMap, style, headIndex, strList, styleColor, keyList);

			// 将文件输出
			OutputStream ouputStream = response.getOutputStream();
			wb.write(ouputStream);
			ouputStream.flush();
			ouputStream.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			LOGGER.error(e.getMessage());
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
							  int headIndex, List<Integer> strList, XSSFCellStyle styleColor, List<Integer> keyList) throws Exception
	{
		LOGGER.info("向工作表中填充数据:fillSheet()");
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
			row = sheet.createRow(++headIndex);
			// 获取单个对象
			T item = list.get(index);
			for (int i = 0; i < enFields.length; i++)
			{
				Object objValue = getFieldValueByNameSequence(enFields[i], item);
				String fieldValue = objValue == null ? "" : objValue.toString();
				XSSFCell cell = row.createCell(i);
				// 如果是以0开头并且不包含小数点，那就默认字符格式
				if (!ObjectUtils.isEmpty(strList) && strList.contains(i)) {
					cell.setCellValue(fieldValue);
				} else {
					try
					{
						if (fieldValue.matches("^(-?\\d+)(\\.\\d+)?$")) {
							if(fieldValue.matches("^[-\\+]?[\\d]*$")) {
								cell.setCellValue(Integer.parseInt(fieldValue));
							} else {
								cell.setCellValue(Double.parseDouble(fieldValue));
							}
						} else {
							cell.setCellValue(fieldValue);
						}
					}
					catch (Exception e)
					{
						cell.setCellValue(fieldValue);
					}
				}
				if (!ObjectUtils.isEmpty(keyList) && keyList.contains(i)) {
					cell.setCellStyle(styleColor);
				}
			}
		}
	}
}
