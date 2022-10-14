/**
 * 
 */
package com.fssy.shareholder.management.pojo.system.config;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fssy.shareholder.management.pojo.common.BaseModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * bom附件实体类
 * 
 * @author Solomon
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("bs_common_attachment")
public class Attachment extends BaseModel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8739612512801958161L;

	@TableId(type = IdType.AUTO)
	private Long id;

	private Integer status;

	private String md5Path;

	private String filename;

	private String path;

	private Integer importStatus;

	private Integer module;

	private String note;

	private String conclusion;

	/*
	 * DataTimeFormat是往数据库走时转换，时区指定在启动类中
	 * JsonFormat是数据往客户端时转换
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
	private Date importDate;

	private String moduleName;
}
