package com.fssy.shareholder.management.pojo.common;

/**
 * 返回客户端的消息对象
 * 
 * @author Solomon
 *
 */
public class SysResult
{
	/**
	 * 200 成功 201 错误 400 参数错误 202重定向到错误页面
	 */
	private Integer status;

	/**
	 * 响应消息
	 */
	private String msg;

	/**
	 * 响应消息的数据
	 */
	private Object data;

	public SysResult()
	{
		super();
	}

	public SysResult(Integer status, String msg, Object data)
	{
		super();
		this.status = status;
		this.msg = msg;
		this.data = data;
	}

	public SysResult(Object data)
	{
		super();
		this.status = 200;
		this.msg = "操作成功";
		this.data = data;
	}

	public static SysResult build(Integer status, String msg, Object data)
	{
		return new SysResult(status, msg, data);
	}

	public static SysResult build(Integer status, String msg)
	{
		return new SysResult(status, msg, null);
	}

	public static SysResult ok(Object data)
	{
		return new SysResult(data);
	}

	public static SysResult ok()
	{
		return new SysResult(200, "操作成功", null);
	}

	public Boolean isOK()
	{
		return this.status == 200;
	}

	public Integer getStatus()
	{
		return status;
	}

	public void setStatus(Integer status)
	{
		this.status = status;
	}

	public String getMsg()
	{
		return msg;
	}

	public void setMsg(String msg)
	{
		this.msg = msg;
	}

	public Object getData()
	{
		return data;
	}

	public void setData(Object data)
	{
		this.data = data;
	}

}
