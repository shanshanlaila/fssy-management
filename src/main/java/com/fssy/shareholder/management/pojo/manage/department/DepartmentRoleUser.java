package com.fssy.shareholder.management.pojo.manage.department;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 组织结构，角色，用户关联表
 * 
 * @author Solomon
 */
@TableName("basic_department_role_user")
public class DepartmentRoleUser implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8562956648585863749L;

	@TableId(type = IdType.AUTO)
	private Long id;
	
	private Long roleId;
	
	private Long userId;
	
	private Long departmentId;

	public DepartmentRoleUser(Long id, Long roleId, Long userId,
			Long departmentId)
	{
		super();
		this.id = id;
		this.roleId = roleId;
		this.userId = userId;
		this.departmentId = departmentId;
	}

	public DepartmentRoleUser()
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

	public Long getRoleId()
	{
		return roleId;
	}

	public void setRoleId(Long roleId)
	{
		this.roleId = roleId;
	}

	public Long getUserId()
	{
		return userId;
	}

	public void setUserId(Long userId)
	{
		this.userId = userId;
	}

	public Long getDepartmentId()
	{
		return departmentId;
	}

	public void setDepartmentId(Long departmentId)
	{
		this.departmentId = departmentId;
	}

	@Override
	public String toString()
	{
		return "DepartmentRoleUser [id=" + id + ", roleId=" + roleId
				+ ", userId=" + userId + ", departmentId=" + departmentId + "]";
	}
}
