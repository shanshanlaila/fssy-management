package com.fssy.shareholder.management.pojo.manage.log;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fssy.shareholder.management.pojo.common.BaseModel;

import groovy.transform.EqualsAndHashCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 定时任务记录表实体类
 *
 * @author Solomon
 * @since 2022-12-16
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("basic_schedule_audit_log")
public class ScheduleAuditLog extends BaseModel
{

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId(value = "id", type = IdType.AUTO)
	private Long id;

	/**
	 * 操作用户名称
	 */
	@TableField("name")
	private String name;

	/**
	 * 操作状态：失败，成功
	 */
	@TableField("status")
	private String status;

	/**
	 * 备注
	 */
	@TableField("note")
	private String note;

}
