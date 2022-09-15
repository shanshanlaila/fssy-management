/**   
 * ------------------------修改日志---------------------------------
 * 修改人                 修改日期                   修改内容
 * 兰宇铧		   2021-11-8              修改BOM读取时，由于取数值时，只取出整数的问题
 * 
 * 修改人                 修改日期                   修改内容
 * 兰宇铧		   2022-04-06            添加读取EXCEL时的写入数据并保存功能
 */
package com.fssy.shareholder.management.service.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

import com.fssy.shareholder.management.pojo.properties.FileProperties;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.fssy.shareholder.management.tools.exception.ServiceException;

/**
 * excel操作业务类
 * 
 * @author Solomon
 */
@Service
public class SheetService
{
	private static final Logger LOGGER = LoggerFactory
			.getLogger(SheetService.class);

	private Workbook workbook;

	private Sheet sheet;

	private final Path fileStorageLocation;

	public Workbook getWorkbook()
	{
		return workbook;
	}

	public void setWorkbook(Workbook workbook)
	{
		this.workbook = workbook;
	}

	public Sheet getSheet()
	{
		return sheet;
	}

	public void setSheet(Sheet sheet)
	{
		this.sheet = sheet;
	}
	
	public SheetService(FileProperties fileProperties)
	{
		// 从application.yml文件中获取附件保存路径
		this.fileStorageLocation = Paths.get(fileProperties.getUploadDir())
				.toAbsolutePath().normalize();
	}

	/**
	 * 读取excel文件
	 * 
	 * @param filePath 文件存储路径
	 * @param fileName 文件名字
	 */
	public void load(String filePath, String fileName)
	{
		// 这里需要转化成绝对路径
		Path fileStoragePath = this.fileStorageLocation.resolve(filePath)
				.normalize();
		File file = new File(fileStoragePath.toString());
		Workbook workbook = null;
		FileInputStream fis = null;
		try
		{
			fis = new FileInputStream(file);
			// 判断什么类型文件
			if (fileName.endsWith(".xls"))
			{
				workbook = new HSSFWorkbook(fis);
			}
			else if (fileName.endsWith(".xlsx"))
			{
				workbook = new XSSFWorkbook(fis);
			}

			if (workbook == null)
			{
				return;
			}

			this.workbook = workbook;
		}
		catch (FileNotFoundException e)
		{
			LOGGER.error(e.getMessage());
			throw new ServiceException("找不到读取文件，请联系管理员");
		}
		catch (IOException e)
		{
			LOGGER.error(e.getMessage());
			throw new ServiceException("读取文件错误，请联系管理员");
		}
		finally
		{
			if (!ObjectUtils.isEmpty(fis))
			{
				try
				{
					fis.close();
				}
				catch (IOException e)
				{
					LOGGER.error(e.getMessage());
					throw new ServiceException("系统错误，请联系管理员");
				}
			}
		}
	}
	
	/**
	 * 读取excel中指定下标表单
	 * 
	 * @param index 下标/序号
	 */
	public void readByIndex(int index)
	{
		this.sheet = workbook.getSheetAt(index);
	}

	/**
	 * 读取excel中指定名字表单
	 * 
	 * @param name 表单名
	 */
	public void readByName(String name)
	{
		this.sheet = workbook.getSheet(name);
	}

	/**
	 * 
	 * @param inputStream 来源于本地文件的流，也可以来源与上传上来的文件的流，也就是MultipartFile的流，使用getInputStream()方法进行获取
	 * @param fileName    文件名
	 */
	public void readExcelFile(InputStream inputStream, String fileName)
	{
		Workbook workbook = null;
		try
		{
			// 判断什么类型文件
			if (fileName.endsWith(".xls"))
			{
				workbook = new HSSFWorkbook(inputStream);
			}
			else if (fileName.endsWith(".xlsx"))
			{
				workbook = new XSSFWorkbook(inputStream);
			}

			if (workbook == null)
			{
				return;
			}

			// 获取所有的工作表的的数量
			int numOfSheet = workbook.getNumberOfSheets();
			LOGGER.debug(numOfSheet + "--->numOfSheet");
			// 遍历表
			for (int i = 0; i < numOfSheet; i++)
			{
				// 获取一个sheet也就是一个工作本。
				Sheet sheet = workbook.getSheetAt(i);
				if (sheet == null)
					continue;
				// 获取一个sheet有多少Row
				int lastRowNum = sheet.getLastRowNum();
				if (lastRowNum == 0)
					continue;
				Row row;
				for (int j = 1; j <= lastRowNum; j++)
				{
					row = sheet.getRow(j);
					if (row == null)
					{
						continue;
					}
					// 获取一个Row有多少Cell
					short lastCellNum = row.getLastCellNum();
					for (int k = 0; k <= lastCellNum; k++)
					{
						if (row.getCell(k) == null)
						{
							continue;
						}
						String res = row.getCell(k).getStringCellValue().trim();
						// 打印出cell(单元格的内容)
						System.out.println(res);
					}

				}
			}
		}
		catch (Exception e)
		{
			LOGGER.error(e.getMessage());
			throw new ServiceException("系统错误，请联系管理员");
		}
	}

	public Object getCellFormatValue(Cell cell)
	{
		Object cellValue = null;
		if (cell != null)
		{
			// 判断cell类型
			switch (cell.getCellType())
			{
			case NUMERIC:
			{
				String cellva = getValue(cell);
				cellValue = cellva.replaceAll("\n", " ") + ",";
				break;
			}
			case FORMULA:
			{
				// 判断cell是否为日期格式
				if (DateUtil.isCellDateFormatted(cell))
				{
					// 转换为日期格式YYYY-mm-dd
					cellValue = String.valueOf(cell.getDateCellValue())
							.replaceAll("\n", " ") + ",";
				}
				else
				{
					// 数字
					cellValue = String.valueOf(cell.getNumericCellValue())
							.replaceAll("\n", " ") + ",";
				}
				break;
			}
			case STRING:
			{
				cellValue = String.valueOf(cell.getRichStringCellValue())
						.replaceAll("\n", " ") + ",";
				break;
			}
			default:
				cellValue = "null" + ",";
			}
		}
		else
		{
			cellValue = "null" + ",";
		}
		return cellValue;
	}

	public String getValue(Cell cell)
	{
		String result = "";
		if (ObjectUtils.isEmpty(cell))
		{
			return result;
		}
		switch (cell.getCellType())
		{
		case BOOLEAN:
			result = String.valueOf(cell.getBooleanCellValue());
			break;
		case NUMERIC:
			short format = cell.getCellStyle().getDataFormat();
			if (DateUtil.isCellDateFormatted(cell))
			{// 处理日期格式（/和-分隔符的都可以识别）
//				Date d = cell.getDateCellValue();
//				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//				result = sdf.format(d);
				// 2021-11-15 Qinhui修改，修改读取不到日期时间的问题
				LocalDateTime d = cell.getLocalDateTimeCellValue();
				result = d.toString();
			}
			else if (format == 14 || format == 31 || format == 57
					|| format == 58)
			{ // 日期格式，如：XXXX年XX月XX日是31,yyyy年m月 57,m月d日 58
				// 处理自定义日期格式：m月d日(通过判断单元格的格式id解决，id的值是58)
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Date date = DateUtil.getJavaDate(cell.getNumericCellValue());
				result = sdf.format(date);
			}
			else if (format == 20 || format == 32 || format == 55
					|| format == 33 || format == 56)
			{ // 分秒格式,如：h"时"mm"分"为32，h"时"mm"分"ss"秒"为33，上午/下午h"时"mm"分"为55，上午/下午h"时"mm"分"ss"秒"为56
				SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
				Date date = DateUtil.getJavaDate(cell.getNumericCellValue());
				result = sdf.format(date);
			}
			else
			{ // 数字
				// 2021-11-8 修改BOM读取时，由于取数值时，只取出整数的问题
				double value = cell.getNumericCellValue();
				DecimalFormat decimalFormat = new DecimalFormat("#");
				// 取整数部分
				long longVal = Long.valueOf(decimalFormat.format(value));
				// 判断是否有.0
				if (Double.parseDouble(longVal + ".0") == value)
				{
					result = String.valueOf(longVal);
				}
				else
				{
					result = String.valueOf(value);
				}
			}
			break;
		case STRING:
			result = cell.getStringCellValue();
			break;
		case FORMULA:
			result = cell.getCellFormula();
			break;
		case BLANK:
			result = "";
			break;
		case ERROR:
			result = "非法字符";
			break;
		default:
			result = "未知字符";
			break;
		}
		return result;
	}

	/**
	 * 用于将Excel表格中列号字母转成列索引，从0对应A开始
	 * 
	 * @param column 列号
	 * @return 列索引
	 */
	public static int columnToIndex(String column)
	{
		if (!column.matches("[A-Z]+"))
		{
			try
			{
				throw new Exception("Invalid parameter");
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		int index = 0;
		char[] chars = column.toUpperCase().toCharArray();

		for (int i = 0; i < chars.length; i++)
		{
			index += ((int) chars[i] - (int) 'A' + 1)
					* (int) Math.pow(26, chars.length - i - 1);
		}

		return index - 1;
	}

	/**
	 * 用于将excel表格中列索引转成列号字母，从A对应1开始
	 * 
	 * @param index 列索引
	 * @return 列号
	 */
	public static String indexToColumn(int index)
	{
		if (index <= 0)
		{
			try
			{
				throw new Exception("Invalid parameter");
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		index--;
		String column = "";
		do
		{
			if (column.length() > 0)
			{
				index--;
			}
			column = ((char) (index % 26 + (int) 'A')) + column;
			index = (int) ((index - index % 26) / 26);
		} while (index > 0);

		return column;
	}
	
	/**
	 * 写入excel文件<br/>
	 * 2022-04-06添加
	 * @param filePath 文件存储路径
	 * @param fileName 文件名字
	 */
	public void write(String filePath, String fileName)
	{
		// 这里需要转化成绝对路径
		Path fileStoragePath = this.fileStorageLocation.resolve(filePath).normalize();
		File file = new File(fileStoragePath.toString());
		Workbook workbook = getWorkbook();
		if (ObjectUtils.isEmpty(workbook))
		{
			throw new ServiceException("文档未读取，不能写入");
		}
		FileOutputStream fos = null;
		try
		{
			fos = new FileOutputStream(file);

			workbook.write(fos);
		}
		catch (FileNotFoundException e)
		{
			LOGGER.error(e.getMessage());
			throw new ServiceException("找不到读取文件，请联系管理员");
		}
		catch (IOException e)
		{
			LOGGER.error(e.getMessage());
			throw new ServiceException("读取文件错误，请联系管理员");
		}
		finally
		{
			if (!ObjectUtils.isEmpty(fos))
			{
				try
				{
					fos.close();
				}
				catch (IOException e)
				{
					LOGGER.error(e.getMessage());
					throw new ServiceException("系统错误，请联系管理员");
				}
			}
		}
	}
}