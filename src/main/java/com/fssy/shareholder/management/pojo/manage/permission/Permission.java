package com.fssy.shareholder.management.pojo.manage.permission;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fssy.shareholder.management.pojo.common.BaseModel;

/**
 * 权限表实体类 
 * @author Solomon
 */
@TableName("basic_permission")
public class Permission extends BaseModel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1127530763424127014L;
	
	@TableId(type = IdType.AUTO)
	private Long id;
	@TableField("`system`")
	private Integer system;
	private Integer parent;
	private Integer leaf;
	private Integer isNormal;
	private Integer sort;
	private String name;
	private String route;
	private Integer type;
	private String authorizationName;
	
	public Long getId()
	{
		return id;
	}
	public void setId(Long id)
	{
		this.id = id;
	}
	public Integer getSystem()
	{
		return system;
	}
	public void setSystem(Integer system)
	{
		this.system = system;
	}
	public Integer getParent()
	{
		return parent;
	}
	public void setParent(Integer parent)
	{
		this.parent = parent;
	}
	public Integer getLeaf()
	{
		return leaf;
	}
	public void setLeaf(Integer leaf)
	{
		this.leaf = leaf;
	}
	public Integer getIsNormal()
	{
		return isNormal;
	}
	public void setIsNormal(Integer isNormal)
	{
		this.isNormal = isNormal;
	}
	public Integer getSort()
	{
		return sort;
	}
	public void setSort(Integer sort)
	{
		this.sort = sort;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getRoute()
	{
		return route;
	}
	public void setRoute(String route)
	{
		this.route = route;
	}
	public Integer getType()
	{
		return type;
	}
	public void setType(Integer type)
	{
		this.type = type;
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
		return "Permission [id=" + id + ", system=" + system + ", parent="
				+ parent + ", leaf=" + leaf + ", isNormal=" + isNormal
				+ ", sort=" + sort + ", name=" + name + ", route=" + route
				+ ", type=" + type + ", authorizationName=" + authorizationName
				+ "]";
	}
}
