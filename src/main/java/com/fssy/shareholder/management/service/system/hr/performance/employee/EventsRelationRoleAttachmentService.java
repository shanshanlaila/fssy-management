/**   
 * ------------------------修改日志---------------------------------
 * 修改人			修改日期			修改内容
 */
package com.fssy.shareholder.management.service.system.hr.performance.employee;

import java.util.Map;

import com.fssy.shareholder.management.pojo.system.config.Attachment;

/**
 * @Title: EventsRelationRoleAttachmentService.java
 * @Description: 事件清单岗位关系附件功能业务接口
 * @author Solomon  
 * @date 2022年10月12日 上午12:40:09 
 */
public interface EventsRelationRoleAttachmentService
{
	/**
	 * 从事件岗位关系表格附件读取数据到数据库
	 * 
	 * @param attachment 附件对象
	 * @return
	 */
	Map<String, Object> readAttachmentToData(Attachment attachment);
}
