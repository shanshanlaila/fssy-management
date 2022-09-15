/**
 * 修改人                 修改日期                   修改内容
 * 兰宇铧	 	 	2021-12-17 	 	 上传附件和更改状态设置不需要事务，否则导入BOM报业务错误会写不进数据表
 */
package com.fssy.shareholder.management.service.system.impl.config;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.mapper.system.config.AttachmentMapper;
import com.fssy.shareholder.management.pojo.manage.user.User;
import com.fssy.shareholder.management.pojo.system.config.Attachment;
import com.fssy.shareholder.management.service.system.config.AttachmentService;
import com.fssy.shareholder.management.tools.common.FileAttachmentTool;
import com.fssy.shareholder.management.tools.common.InstandTool;
import com.fssy.shareholder.management.tools.constant.CommonConstant;
import com.fssy.shareholder.management.tools.exception.ServiceException;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 附件管理业务实体类
 * 
 * @author Solomon
 */
@Service
public class AttachmentServiceImpl implements AttachmentService
{
	@Autowired
	private AttachmentMapper attachmentMapper;

	/**
	 * 文件附件工具类
	 */
	@Autowired
	private FileAttachmentTool fileAttachmentTool;

	@Override
	public List<Attachment> findAttachmentDataListByParams(
			Map<String, Object> params)
	{
		QueryWrapper<Attachment> queryWrapper = getQueryWrapper(params);
		List<Attachment> attachmentList = attachmentMapper
				.selectList(queryWrapper);
		return attachmentList;
	}

	@Override
	public Page<Attachment> findAttachmentDataListPerPageByParams(
			Map<String, Object> params)
	{
		QueryWrapper<Attachment> queryWrapper = getQueryWrapper(params).orderByDesc("id");
		int limit = (int) params.get("limit");
		int page = (int) params.get("page");
		Page<Attachment> myPage = new Page<>(page, limit);
		Page<Attachment> attachmentPage = attachmentMapper.selectPage(myPage,
				queryWrapper);
		return attachmentPage;
	}


	@Override
	public Page<Map<String, Object>> findAttachmentDataListMapByParams(Map<String, Object> params) {
		QueryWrapper<Attachment> queryWrapper = getQueryWrapper(params)
				.select("id,status,importStatus,filename,importDate,note,createdName,createdId").orderByDesc("id");
		int limit = (int) params.get("limit");
		int page = (int) params.get("page");
		Page<Map<String, Object>> myPage = new Page<>(page, limit);
		Page<Map<String, Object>> attachmentPage = attachmentMapper.selectMapsPage(myPage, queryWrapper);
		return attachmentPage;
	}

	private QueryWrapper<Attachment> getQueryWrapper(Map<String, Object> params)
	{
		QueryWrapper<Attachment> queryWrapper = new QueryWrapper<>();
		// 附件id查询
		if (params.containsKey("id"))
		{
			queryWrapper.eq("id", params.get("id"));
		}
		// 创建人查询
		if (params.containsKey("createdName"))
		{
			queryWrapper.like("createdName", params.get("createdName"));
		}
		if (params.containsKey("createdId"))
		{
			queryWrapper.eq("createdId", params.get("createdId"));
		}
		// md5检查查询
		if (params.containsKey("md5Path"))
		{
			queryWrapper.eq("md5Path", params.get("md5Path"));
		}
		// 附件名称查询
		if (params.containsKey("filename"))
		{
			queryWrapper.like("filename", params.get("filename"));
		}
		// 附件状态查询
		if (params.containsKey("status"))
		{
			queryWrapper.like("status", params.get("status"));
		}
		// 附件导入状态状态查询
		if (params.containsKey("importStatus")
				&& !ObjectUtils.isEmpty(params.get("importStatus")))
		{
			queryWrapper.eq("importStatus", params.get("importStatus"));
		}
		if (params.containsKey("createdAtStart"))
		{
			queryWrapper.ge("createdAt", params.get("createdAtStart"));
		}
		if (params.containsKey("createdAtEnd"))
		{
			queryWrapper.le("createdAt", params.get("createdAtEnd"));
		}
		// 导入日期查询
		if (params.containsKey("importDateStart"))
		{
			queryWrapper.ge("importDate", params.get("importDateStart"));
		}
		if (params.containsKey("importDateEnd"))
		{
			queryWrapper.le("importDate", params.get("importDateEnd"));
		}
		if (params.containsKey("module"))
		{
			queryWrapper.eq("module", params.get("module"));
		}
		// 合同附件查找
		if (params.containsKey("contractAttachmentIds"))
		{
			List<String> contractAttachmentIds = Arrays.asList(InstandTool.objectToString(params.get("contractAttachmentIds")).split(","));
			queryWrapper.in("id", contractAttachmentIds);
		}
		if (params.containsKey("conclusionEq"))
		{
			queryWrapper.eq("conclusion", params.get("conclusionEq"));
		}
		if (params.containsKey("orderByDesc"))
		{
			queryWrapper.orderByDesc("id");
		}
		// 计划来源查询,1是前端导入，2是从定时任务获取的
		if (params.containsKey("dataSources"))
		{
			if (params.get("dataSources").equals("1")) {
				queryWrapper.ne("createdId", 0);
			} else {
				queryWrapper.eq("createdId", 0);
			}
		}
		if (params.containsKey("moduleList")) {
			List<String> moduleList = Arrays.asList(params.get("moduleList").toString().split("_"));
			queryWrapper.in("module", moduleList);
		}
		return queryWrapper;
	}

	@Override
	public boolean changeFileStatus(int status, String id, String msg)
	{
		Attachment deleteAttachment = attachmentMapper.selectById(id);

		// 删除附件文件
		try
		{
			fileAttachmentTool.deleteFileAsResource(deleteAttachment.getPath(),
					deleteAttachment.getFilename());
		} catch (ServiceException e)
		{
			System.out.println("触发异常：" + e.getMessage());
		}

		// 更新导入附件的数据记录状态
		UpdateWrapper<Attachment> updateWrapper = new UpdateWrapper<>();
		updateWrapper.eq("id", id)
				.set("status", status)
				.set("note", msg)
				.set("updatedAt", LocalDateTime.now());
		int num = attachmentMapper.update(null, updateWrapper);
		if (num > 0)
		{
			return true;
		}

		return false;
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED) // 这里不需要事务
	public boolean changeImportStatus(int status, String id)
	{
		User user = (User) SecurityUtils.getSubject().getPrincipal();
		UpdateWrapper<Attachment> updateWrapper = new UpdateWrapper<>();
		updateWrapper.eq("id", id).set("importStatus", status)
				.set("updatedAt", LocalDateTime.now())
				.set("updatedId", user.getId())
				.set("updatedName", user.getName());
		int num = attachmentMapper.update(null, updateWrapper);
		if (num > 0)
		{
			return true;
		}
		return false;
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public boolean changeImportStatus(int status, String id, String msg)
	{
		User user = (User) SecurityUtils.getSubject().getPrincipal();
		UpdateWrapper<Attachment> updateWrapper = new UpdateWrapper<>();
		updateWrapper.eq("id", id).set("importStatus", status)
				.set("note", msg)
				.set("updatedAt", LocalDateTime.now())
				.set("updatedId", user.getId())
				.set("updatedName", user.getName());
		int num = attachmentMapper.update(null, updateWrapper);
		if (num > 0)
		{
			return true;
		}
		return false;
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public boolean changeImportStatus(int status, String id, String msg, String conclusion)
	{

		User user = (User) SecurityUtils.getSubject().getPrincipal();
		UpdateWrapper<Attachment> updateWrapper = new UpdateWrapper<>();
		updateWrapper.eq("id", id).set("importStatus", status)
				.set("note", msg)
				.set("conclusion", conclusion)
				.set("updatedAt", LocalDateTime.now())
				.set("updatedId", user.getId())
				.set("updatedName", user.getName());
		int num = attachmentMapper.update(null, updateWrapper);
		if (num > 0)
		{
			return true;
		}
		return false;
	}




	@Override
	public boolean deleteAttachmentById(int id)
	{
		Attachment deleteAttachment = attachmentMapper.selectById(id);
		// 业务判断
		if (deleteAttachment
				.getImportStatus() == CommonConstant.IMPORT_RESULT_SUCCESS)
		{
			throw new ServiceException("附件已经导入，不允许删除");
		}

		// 删除附件文件
		fileAttachmentTool.deleteFileAsResource(deleteAttachment.getPath(),
				deleteAttachment.getFilename());

		int num = attachmentMapper.deleteById(id);

		if (num > 0)
		{
			return true;
		}
		return false;
	}
}
