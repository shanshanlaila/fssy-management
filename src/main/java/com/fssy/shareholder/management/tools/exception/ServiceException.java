package com.fssy.shareholder.management.tools.exception;

/**
 * 业务异常实体类
 * @author Solomon
 */
public class ServiceException extends RuntimeException
{
	private static final long serialVersionUID = -7052667843091663174L;

	public ServiceException()
	{
		super();
	}

	public ServiceException(String message)
	{
		super(message);
	}

	public ServiceException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public ServiceException(Throwable cause)
	{
		super(cause);
	}
}
