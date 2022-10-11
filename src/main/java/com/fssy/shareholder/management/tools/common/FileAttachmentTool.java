/**
 * 修改人                 修改日期                   修改内容
 * 兰宇铧	 	 	2021-11-24 	 	 添加导入日期判断，使用upload插件不会被前端校验，测试问题修改
 * 
 * 修改人                 修改日期                   修改内容
 * 兰宇铧	 	 	2021-12-17 	 	 上传附件和更改状态设置不需要事务，否则导入BOM报业务错误会写不进数据表
 *
 * 修改人                 修改日期                   修改内容
 * 班群蔚	 	 	2022-4-1 	 	 销售合同、采购合同写入加密附件表
 *
 * 修改人                 修改日期                   修改内容
 * 班群蔚	 	 	2022-05-07 		 现场问题，上传库位和生产计划时，由于库位上传时间久，导致文件被覆盖问题，需要加锁
 */
package com.fssy.shareholder.management.tools.common;

import com.fssy.shareholder.management.mapper.system.config.AttachmentMapper;
import com.fssy.shareholder.management.mapper.system.config.AttachmentSecretMapper;
import com.fssy.shareholder.management.pojo.properties.FileProperties;
import com.fssy.shareholder.management.pojo.system.config.Attachment;
import com.fssy.shareholder.management.pojo.system.config.AttachmentSecret;
import com.fssy.shareholder.management.pojo.system.config.ImportModule;
import com.fssy.shareholder.management.pojo.common.Module;
import com.fssy.shareholder.management.tools.exception.ServiceException;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Calendar;
import java.util.UUID;

/**
 * 文件/附件操作工具类
 * 
 * @author Solomon
 *
 */
@Component
public class FileAttachmentTool
{
	private final Path fileStorageLocation; // 文件在本地存储的地址

	@Autowired
	private AttachmentMapper attachmentMapper;

	@Autowired
	private AttachmentSecretMapper attachmentSecretMapper;

	/**
	 * 构造器设置文件目录
	 * 
	 * @param fileProperties
	 */
	@Autowired
	public FileAttachmentTool(FileProperties fileProperties)
	{
		this.fileStorageLocation = Paths.get(fileProperties.getUploadDir())
				.toAbsolutePath().normalize();
		try
		{
			Files.createDirectories(this.fileStorageLocation);
		}
		catch (Exception ex)
		{
			throw new ServiceException("上传文件目录创建失败", ex);
		}
	}

	/**
	 * 存储文件到系统（弃用）
	 *
	 * @param file 文件
	 * @return 文件名
	 */
	public String storeFile(MultipartFile file)
	{
		// 格式化文件名，针对斜杠
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());

		try
		{
			// 判断文件名格式
			if (fileName.contains(".."))
			{
				throw new ServiceException("文件名格式不正确" + fileName);
			}

			// 保存文件（存在同名文件则覆盖）
			Path targetLocation = this.fileStorageLocation.resolve(fileName);
			Files.copy(file.getInputStream(), targetLocation,
					StandardCopyOption.REPLACE_EXISTING);

			return fileName;
		}
		catch (IOException ex)
		{
			throw new ServiceException("保存文件/附件失败，名字为：" + fileName + "，请重新尝试",
					ex);
		}
	}

	/**
	 * 按模块分开保存文件/附件
	 * 
	 * @param file   文件对象
	 * @param module 模块枚举类
	 * @return
	 */
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public synchronized Attachment storeFileToModule(MultipartFile file, ImportModule module,
                                                     Attachment attachment)
	{
		// 2022-05-07 现场问题，上传库位和生产计划时，由于库位上传时间久，导致文件被覆盖问题，需要加锁
		// 2021-11-24 添加导入日期判断，使用upload插件不会被前端校验
		if (ObjectUtils.isEmpty(attachment.getImportDate()))
		{
			throw new ServiceException("请选择导入日期之后再导入");
		}
		// 格式化文件名，针对斜杠
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		// 生成UUID盐值
		String saltString = UUID.randomUUID().toString();
		// 用于查询的条件
		String queryPath = DigestUtils.md5Hex(saltString);
		// 获取md5文件名
		String md5FileName = randomFileName(fileName, saltString);
		// 文件路径
		// 从模块开始到月份的相对路径
		String relativeFilePathStr = joinModuleAndDate(module.getName());
		String relativePathStr = relativeFilePathStr + File.separator
				+ md5FileName;
		try
		{
			// 判断文件名格式
			if (fileName.contains(".."))
			{
				throw new ServiceException("文件名格式不正确" + fileName);
			}

			// 获取文件所在目录信息
			Path targetDirLocation = this.fileStorageLocation
					.resolve(relativeFilePathStr);
			// 判断目录存在与否，不存在创建目录或者存在但是为文件
			File dir = new File(targetDirLocation.toString());
			if (!(dir.exists() && dir.isDirectory()))
			{
				dir.mkdirs();
			}
			// 解析目标文件名
			Path targetLocation = targetDirLocation.resolve(md5FileName);
			// 保存文件（存在同名文件则覆盖）
			Files.copy(file.getInputStream(), targetLocation,
					StandardCopyOption.REPLACE_EXISTING);

			// 保存附件表
			attachment.setFilename(fileName);
			// 默认就是正在导入
			attachment.setMd5Path(queryPath);
			attachment.setModule(module.getId());
			attachment.setPath(relativePathStr);
			// 默认就是上载成功
			attachmentMapper.insert(attachment);
			return attachment;
		}
		catch (IOException ex)
		{
			throw new ServiceException("保存文件/附件失败，名字为：" + fileName + "，请重新尝试",
					ex);
		}
	}

	/**
	 * 加载文件
	 * 
	 * @param path 文件路径名
	 * @param fileName 文件名
	 * @return 文件
	 */
	public Resource loadFileAsResource(String path, String fileName)
	{
		try
		{
			Path filePath = this.fileStorageLocation.resolve(path)
					.normalize();
			Resource resource = new UrlResource(filePath.toUri());
			if (resource.exists())
			{
				return resource;
			}
			else
			{
				throw new ServiceException("文件不存在，名字为：" + fileName);
			}
		}
		catch (MalformedURLException ex)
		{
			throw new ServiceException("文件不存在，名字为：" + fileName, ex);
		}
	}

	/**
	 * 删除文件
	 * 
	 * @param path 文件路径名
	 * @param fileName 文件名
	 * @return
	 */
	public boolean deleteFileAsResource(String path, String fileName)
	{
		Path filePath = this.fileStorageLocation.resolve(path).normalize();
		File file = new File(filePath.toString());
		if (file.exists())
		{
			return file.delete();
		}
		else
		{
			throw new ServiceException("文件不存在，名字为：" + fileName);
		}
	}

	/**
	 * 获取文件
	 * @param path
	 * @return
	 */
	public File getFile(String path)
	{
		Path filePath = this.fileStorageLocation.resolve(path).normalize();
		File file = new File(filePath.toString());
		return file;
	}

	/**
	 * 重新根据MD5拼凑文件名
	 * 
	 * @param fileName   原文件名
	 * @param saltString UUID字符串
	 * @return
	 */
	protected String randomFileName(String fileName, String saltString)
	{
		// 获取后缀名
		String extension = FilenameUtils.getExtension(fileName);
		String result = DigestUtils.md5Hex(saltString + fileName) + "."
				+ extension;
		return result;
	}

	/**
	 * 获取模块，年，月的目录路径
	 * 
	 * @param moduleName 模块名字
	 * @return
	 */
	protected String joinModuleAndDate(String moduleName)
	{
		// 添加年和月，默认为当地时区
		Calendar calendar = Calendar.getInstance();
		String yearString = String.valueOf(calendar.get(Calendar.YEAR));
		// 月份0代表1月份，需要加1
		String monthString = String.valueOf(calendar.get(Calendar.MONTH) + 1);
		String tempString = String.format("%s/%s", yearString, monthString);
		// 相对路径需要保存
		return ObjectUtils.isEmpty(moduleName) ? tempString
				: moduleName + File.separator + tempString;

	}


	/**
	 * 按模块分开保存文件/附件
	 *
	 * @param file   文件对象
	 * @param module 模块枚举类
	 * @return
	 */
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public synchronized AttachmentSecret storeSecretFileToModule(MultipartFile file, Module module,
                                                                 AttachmentSecret attachment)
	{
		// 2022-05-07 现场问题，上传库位和生产计划时，由于库位上传时间久，导致文件被覆盖问题，需要加锁
		// 格式化文件名，针对斜杠
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		// 生成UUID盐值
		String saltString = UUID.randomUUID().toString();
		// 用于查询的条件
		String queryPath = DigestUtils.md5Hex(saltString);
		// 获取md5文件名
		String md5FileName = randomFileName(fileName, saltString);
		// 文件路径
		// 从模块开始到月份的相对路径
		String relativeFilePathStr = joinModuleAndDate(module.getName());
		String relativePathStr = relativeFilePathStr + File.separator + md5FileName;
		try
		{
			// 判断文件名格式
			if (fileName.contains(".."))
			{
				throw new ServiceException("文件名格式不正确" + fileName);
			}

			// 获取文件所在目录信息
			Path targetDirLocation = this.fileStorageLocation.resolve(relativeFilePathStr);
			// 判断目录存在与否，不存在创建目录或者存在但是为文件
			File dir = new File(targetDirLocation.toString());
			if (!(dir.exists() && dir.isDirectory()))
			{
				dir.mkdirs();
			}
			// 解析目标文件名
			Path targetLocation = targetDirLocation.resolve(md5FileName);
			// 保存文件（存在同名文件则覆盖）
			Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
			Calendar calendar = Calendar.getInstance();
			attachment.setImportDate(calendar.getTime());
			// 保存附件表
			attachment.setFilename(fileName);
			// 默认就是正在导入
			attachment.setMd5Path(queryPath);
			attachment.setModule(module.getValue());
			attachment.setPath(relativePathStr);
			// 默认就是上载成功
			attachmentSecretMapper.insert(attachment);
			return attachment;
		}
		catch (IOException ex)
		{
			throw new ServiceException("保存文件/附件失败，名字为：" + fileName + "，请重新尝试", ex);
		}
	}

	/**
	 * 定时任务-自动读取下载到电脑端的客户计划
	 *
	 * @param module 模块枚举类
	 * @return
	 */
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public Attachment storeFileToModuleAuto(InputStream fileInputStream, Module module, String fileName)
	{
		Attachment attachment = new Attachment();
		Calendar calendar = Calendar.getInstance();
		attachment.setImportDate(calendar.getTime());
		// 生成UUID盐值
		String saltString = UUID.randomUUID().toString();
		// 用于查询的条件
		String queryPath = DigestUtils.md5Hex(saltString);
		// 获取md5文件名
		String md5FileName = randomFileName(fileName, saltString);
		// 文件路径
		// 从模块开始到月份的相对路径
		String relativeFilePathStr = joinModuleAndDate(module.getName());
		String relativePathStr = relativeFilePathStr + File.separator + md5FileName;
		try
		{
			// 判断文件名格式
			if (fileName.contains(".."))
			{
				throw new ServiceException("文件名格式不正确" + fileName);
			}

			// 获取文件所在目录信息
			Path targetDirLocation = this.fileStorageLocation
					.resolve(relativeFilePathStr);
			// 判断目录存在与否，不存在创建目录或者存在但是为文件
			File dir = new File(targetDirLocation.toString());
			if (!(dir.exists() && dir.isDirectory()))
			{
				dir.mkdirs();
			}
			// 解析目标文件名
			Path targetLocation = targetDirLocation.resolve(md5FileName);
			// 保存文件（存在同名文件则覆盖）
			Files.copy(fileInputStream, targetLocation,
					StandardCopyOption.REPLACE_EXISTING);

			// 保存附件表
			attachment.setFilename(fileName);
			// 默认就是正在导入
			attachment.setMd5Path(queryPath);
			attachment.setModule(module.getValue());
			attachment.setPath(relativePathStr);
			// 默认就是上载成功
			attachmentMapper.insert(attachment);
			return attachment;
		}
		catch (IOException ex)
		{
			throw new ServiceException("保存文件/附件失败，名字为：" + fileName + "，请重新尝试",
					ex);
		}
	}
}
