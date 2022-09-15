package com.fssy.shareholder.management.pojo.common;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 实体类父类
 * 
 * @author Solomon
 */
public class BaseModel implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** 使用mybatis-plus添加时自动填充 */
	@TableField(fill = FieldFill.INSERT)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	protected LocalDateTime createdAt;

	@TableField(fill = FieldFill.INSERT)
	protected Long createdId;

	@TableField(fill = FieldFill.INSERT)
	protected String createdName;

	/** 使用mybatis-plus修改时自动填充 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	protected LocalDateTime updatedAt;

	@TableField(fill = FieldFill.INSERT_UPDATE)
	protected Long updatedId;

	@TableField(fill = FieldFill.INSERT_UPDATE)
	protected String updatedName;

	public String getCreatedName()
	{
		return createdName;
	}

	public void setCreatedName(String createdName)
	{
		this.createdName = createdName;
	}

	public String getUpdatedName()
	{
		return updatedName;
	}

	public void setUpdatedName(String updatedName)
	{
		this.updatedName = updatedName;
	}

	public LocalDateTime getCreatedAt()
	{
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt)
	{
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt()
	{
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt)
	{
		this.updatedAt = updatedAt;
	}

	public Long getCreatedId()
	{
		return createdId;
	}

	public void setCreatedId(Long createdId)
	{
		this.createdId = createdId;
	}

	public Long getUpdatedId()
	{
		return updatedId;
	}

	public void setUpdatedId(Long updatedId)
	{
		this.updatedId = updatedId;
	}
}
