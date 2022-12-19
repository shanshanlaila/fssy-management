/**
 * 
 */
package com.fssy.shareholder.management.pojo.manage.log;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fssy.shareholder.management.pojo.common.BaseModel;

import groovy.transform.EqualsAndHashCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 审计日志实体类</br>
 * 对应表audit_log
 * 
 * @author Solomon
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@lombok.EqualsAndHashCode(callSuper = false)
@TableName("basic_audit_log")
public class AuditLog extends BaseModel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 31968796230191029L;
	
	@TableId(type = IdType.AUTO)
	private Long id;
	
	private Integer status;
	
	private String name;
	
	private String operation;
	
	private String method;
	
	private String params;
	
	private Long time;
	
	private String ip;
	
	private String note;
}
