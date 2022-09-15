package com.fssy.shareholder.management.pojo.manage.role;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fssy.shareholder.management.pojo.common.BaseModel;

/**
 * 角色实体类</br>
 * 对应表为role表
 * 
 * @author Solomon
 */
@TableName("basic_role")
public class Role extends BaseModel
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5528474285510979945L;

	@TableId(type = IdType.AUTO)
	private Long id;
	
	private String name;

	public Role()
	{
		super();
	}

	public Role(Long id, String name)
	{
		super();
		this.id = id;
		this.name = name;
	}

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	@Override
	public String toString()
	{
		return "Role [id=" + id + ", name=" + name + "]";
	}
}
