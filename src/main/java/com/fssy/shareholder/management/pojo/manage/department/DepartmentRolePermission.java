package com.fssy.shareholder.management.pojo.manage.department;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 织结构，角色，权限关联表
 * 
 * @author Solomon
 */
@TableName("basic_department_role_permission")
public class DepartmentRolePermission implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2781023495990102032L;

	@TableId(type = IdType.AUTO)
	private Long id;

	private Long departmentId;

	private Long roleId;

	private Long permissionId;

	public DepartmentRolePermission(Long id, Long departmentId, Long roleId,
			Long permissionId)
	{
		super();
		this.id = id;
		this.departmentId = departmentId;
		this.roleId = roleId;
		this.permissionId = permissionId;
	}

	public DepartmentRolePermission()
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

	public Long getDepartmentId()
	{
		return departmentId;
	}

	public void setDepartmentId(Long departmentId)
	{
		this.departmentId = departmentId;
	}

	public Long getRoleId()
	{
		return roleId;
	}

	public void setRoleId(Long roleId)
	{
		this.roleId = roleId;
	}

	public Long getPermissionId()
	{
		return permissionId;
	}

	public void setPermissionId(Long permissionId)
	{
		this.permissionId = permissionId;
	}

	@Override
	public String toString()
	{
		return "DepartmentRolePermission [id=" + id + ", departmentId="
				+ departmentId + ", roleId=" + roleId + ", permissionId="
				+ permissionId + "]";
	}
}
