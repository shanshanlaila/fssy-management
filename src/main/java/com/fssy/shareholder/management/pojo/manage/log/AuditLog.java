/**
 * 
 */
package com.fssy.shareholder.management.pojo.manage.log;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fssy.shareholder.management.pojo.common.BaseModel;

/**
 * 审计日志实体类</br>
 * 对应表audit_log
 * 
 * @author Solomon
 */
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

	public AuditLog(Long id, Integer status, String name, String operation,
			String method, String params, Long time, String ip)
	{
		super();
		this.id = id;
		this.status = status;
		this.name = name;
		this.operation = operation;
		this.method = method;
		this.params = params;
		this.time = time;
		this.ip = ip;
	}

	public AuditLog()
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

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getOperation()
	{
		return operation;
	}

	public void setOperation(String operation)
	{
		this.operation = operation;
	}

	public String getMethod()
	{
		return method;
	}

	public void setMethod(String method)
	{
		this.method = method;
	}

	public String getParams()
	{
		return params;
	}

	public void setParams(String params)
	{
		this.params = params;
	}

	public Long getTime()
	{
		return time;
	}

	public void setTime(Long time)
	{
		this.time = time;
	}

	public String getIp()
	{
		return ip;
	}

	public void setIp(String ip)
	{
		this.ip = ip;
	}

	@Override
	public String toString()
	{
		return "AuditLog [id=" + id + ", status=" + status + ", name=" + name
				+ ", operation=" + operation + ", method=" + method
				+ ", params=" + params + ", time=" + time + ", ip=" + ip + "]";
	}
}
