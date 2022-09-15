package com.fssy.shareholder.management.pojo.manage.department;

import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 组织结构，用户，权限关联表与组织结构表，角色表，权限表视图实体类
 * 
 * @author Solomon
 */
@TableName("view_department_role_permission")
public class ViewDepartmentRolePermission
{
	private Long departmentId;
	
	private Long roleId;
	
	private Long permissionId;
	
	private String roleName;
	
	private String departmentName;
	
	private String permissionName;
	
	private Integer departmentLeaf;
	
	private Long departmentParent;
	
	private Integer permissionLeaf;
	
	private Integer isNormal;
	
	private Long permissionParent;
	
	private Integer system;
	
	private Integer sort;
	
	private String route;
	
	private Integer permissionType;
	
	private String authorizationName;

	public ViewDepartmentRolePermission()
	{
		super();
	}

	public ViewDepartmentRolePermission(Long departmentId, Long roleId,
			Long permissionId, String roleName, String departmentName,
			String permissionName, Integer departmentLeaf,
			Long departmentParent, Integer permissionLeaf, Integer isNormal,
			Long permissionParent, Integer system, Integer sort, String route,
			Integer permissionType, String authorizationName)
	{
		super();
		this.departmentId = departmentId;
		this.roleId = roleId;
		this.permissionId = permissionId;
		this.roleName = roleName;
		this.departmentName = departmentName;
		this.permissionName = permissionName;
		this.departmentLeaf = departmentLeaf;
		this.departmentParent = departmentParent;
		this.permissionLeaf = permissionLeaf;
		this.isNormal = isNormal;
		this.permissionParent = permissionParent;
		this.system = system;
		this.sort = sort;
		this.route = route;
		this.permissionType = permissionType;
		this.authorizationName = authorizationName;
	}

	public Long getDepartmentId()
	{
		return departmentId;
	}

	public Long getRoleId()
	{
		return roleId;
	}

	public Long getPermissionId()
	{
		return permissionId;
	}

	public String getRoleName()
	{
		return roleName;
	}

	public String getDepartmentName()
	{
		return departmentName;
	}

	public String getPermissionName()
	{
		return permissionName;
	}

	public Integer getDepartmentLeaf()
	{
		return departmentLeaf;
	}

	public Long getDepartmentParent()
	{
		return departmentParent;
	}

	public Integer getPermissionLeaf()
	{
		return permissionLeaf;
	}

	public Integer getIsNormal()
	{
		return isNormal;
	}

	public Long getPermissionParent()
	{
		return permissionParent;
	}

	public Integer getSystem()
	{
		return system;
	}

	public Integer getSort()
	{
		return sort;
	}

	public String getRoute()
	{
		return route;
	}

	public Integer getPermissionType()
	{
		return permissionType;
	}

	public String getAuthorizationName()
	{
		return authorizationName;
	}

	public void setAuthorizationName(String authorizationName)
	{
		this.authorizationName = authorizationName;
	}

	@Override
	public String toString()
	{
		return "ViewDepartmentRolePermission [departmentId=" + departmentId
				+ ", roleId=" + roleId + ", permissionId=" + permissionId
				+ ", roleName=" + roleName + ", departmentName="
				+ departmentName + ", permissionName=" + permissionName
				+ ", departmentLeaf=" + departmentLeaf + ", departmentParent="
				+ departmentParent + ", permissionLeaf=" + permissionLeaf
				+ ", isNormal=" + isNormal + ", permissionParent="
				+ permissionParent + ", system=" + system + ", sort=" + sort
				+ ", route=" + route + ", permissionType=" + permissionType
				+ ", authorizationName=" + authorizationName + "]";
	}

}
