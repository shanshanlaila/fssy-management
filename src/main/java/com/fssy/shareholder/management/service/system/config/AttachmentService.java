/**
 * 
 */
package com.fssy.shareholder.management.service.system.config;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.pojo.system.config.Attachment;

import java.util.List;
import java.util.Map;

/**
 * 附件管理业务接口
 * 
 * @author Solomon
 */
public interface AttachmentService
{
	/**
	 * 通过查询条件查询附件列表
	 * 
	 * @param params 查询条件
	 * @return 附件列表
	 */
	List<Attachment> findAttachmentDataListByParams(Map<String, Object> params);

	/**
	 * 通过查询条件分页查询附件列表
	 * 
	 * @param params 查询条件
	 * @return 附件分页数据
	 */
	Page<Attachment> findAttachmentDataListPerPageByParams(
			Map<String, Object> params);


	/**
	 * 通过查询条件分页查询附件列表
	 *
	 * @param params 查询条件
	 * @return 附件分页数据
	 */
	Page<Map<String, Object>> findAttachmentDataListMapByParams(
			Map<String, Object> params);

	/**
	 * 改变导入的文件的状态
	 *
	 * @param status
	 * @param id
	 * @param msg
	 * @return
	 */
	boolean changeFileStatus(int status, String id, String msg);

	/**
	 * 改变导入状态
	 * 
	 * @param status 导入状态
	 * @param id     附件表主键
	 * @return
	 */
	boolean changeImportStatus(int status, String id);

	/**
	 * 改变导入状态并写入备注信息
	 * @param status 导入状态
	 * @param id 附件表主键
	 * @param msg 记录的信息
	 * @return
	 */
	boolean changeImportStatus(int status, String id,String msg);

	/**
	 *
	 * @param status 导入状态
	 * @param id 附件表主键
	 * @param msg 记录的信息
	 * @param conclusion 导入情况总述
	 * @return
	 */
	boolean changeImportStatus(int status,String id,String msg,String conclusion);

	/**
	 * 根据附件表主键删除附件
	 * 
	 * @param id 附件表主键
	 * @return
	 */
	boolean deleteAttachmentById(int id);
}
