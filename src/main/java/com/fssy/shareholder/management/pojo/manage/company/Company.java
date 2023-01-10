package com.fssy.shareholder.management.pojo.manage.company;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fssy.shareholder.management.pojo.common.BaseModel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 公司实体类</br>
 * 对应表为company表
 *
 * @author Shizn
 */
@Getter
@Setter
@TableName("basic_company")
public class Company extends BaseModel
{
	private static final long serialVersionUID = 1L;

	/**
	 * 序号
	 */
	@TableId(value = "id", type = IdType.AUTO)
	private Integer id;

	/**
	 * 备注
	 */
	@TableField("note")
	private String note;
	/**
	 * 公司名称
	 */
	@TableField("name")
	private String name;
	/**
	 * 公司简称
	 */
	@TableField("shortName")
	private String shortName;
	/**
	 * 公司代码
	 */
	@TableField("code")
	private String code;
	/**
	 * 使用状态：启用、停用
	 */
	@TableField("status")
	private String status;
	/**
	 *创建人ID
	 */
	@TableField("createdId")
	private Long createdId;
	/**
	 *更新人ID
	 */
	@TableField("updatedId")
	private Long updatedId;
	/**
	 * 创建人姓名
	 */
	@TableField("createdName")
	private String createdName;
	/**
	 * 更新人姓名
	 */
	@TableField("updatedName")
	private String updatedName;
	/**
	 * 启用日期
	 */
	@TableField("activeDate")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@JsonFormat(pattern="yyyy-MM-dd",timezone="Asia/Shanghai")
	private LocalDate activeDate;
	/**
	 * 停用日期
	 */
	@TableField("inactiveDate")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@JsonFormat(pattern="yyyy-MM-dd",timezone="Asia/Shanghai")
	private LocalDate inactiveDate;
	/**
	 * 创建时间
	 */
	@TableField("createdAt")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@JsonFormat(pattern="yyyy-MM-dd",timezone="Asia/Shanghai")
	private LocalDateTime createdAt;
	/**
	 * 更新时间
	 */
	@TableField("updatedAt")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@JsonFormat(pattern="yyyy-MM-dd",timezone="Asia/Shanghai")
	private LocalDateTime updatedAt;



}
