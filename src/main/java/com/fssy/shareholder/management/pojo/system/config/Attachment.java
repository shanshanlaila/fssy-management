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

/**
 * bom附件实体类
 * 
 * @author Solomon
 */
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

	public Attachment(Long id, Integer status, String md5Path, String filename,
					  String path, Integer importStatus, Integer module, Date importDate, String note,
					  String conclusion)
	{
		super();
		this.id = id;
		this.status = status;
		this.md5Path = md5Path;
		this.filename = filename;
		this.path = path;
		this.importStatus = importStatus;
		this.module = module;
		this.note = note;
		this.importDate = importDate;
		this.conclusion = conclusion;
	}

	public Attachment()
	{
		super();
	}

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public Integer getStatus()
	{
		return status;
	}

	public void setStatus(Integer status)
	{
		this.status = status;
	}

	public String getMd5Path()
	{
		return md5Path;
	}

	public void setMd5Path(String md5Path)
	{
		this.md5Path = md5Path;
	}

	public String getFilename()
	{
		return filename;
	}

	public void setFilename(String filename)
	{
		this.filename = filename;
	}

	public String getPath()
	{
		return path;
	}

	public void setPath(String path)
	{
		this.path = path;
	}

	public Integer getImportStatus()
	{
		return importStatus;
	}

	public void setImportStatus(Integer importStatus)
	{
		this.importStatus = importStatus;
	}

	public Integer getModule()
	{
		return module;
	}

	public void setModule(Integer module)
	{
		this.module = module;
	}

	public Date getImportDate()
	{
		return importDate;
	}

	public void setImportDate(Date importDate)
	{
		this.importDate = importDate;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getConclusion()
	{
		return conclusion;
	}

	public void setConclusion(String conclusion)
	{
		this.conclusion = conclusion;
	}

	@Override
	public String toString()
	{
		return "Attachment{" +
				"id=" + id +
				", status=" + status +
				", md5Path='" + md5Path + '\'' +
				", filename='" + filename + '\'' +
				", path='" + path + '\'' +
				", importStatus=" + importStatus +
				", module=" + module +
				", note='" + note + '\'' +
				", conclusion='" + conclusion + '\'' +
				", importDate=" + importDate +
				'}';
	}

}
