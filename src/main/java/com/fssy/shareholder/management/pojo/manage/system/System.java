package com.fssy.shareholder.management.pojo.manage.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fssy.shareholder.management.pojo.common.BaseModel;

/**
 * 系统功能实体类
 * @author Solomon
 */
@TableName("basic_system")
public class System extends BaseModel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6981571717892719710L;
	
	@TableId(type = IdType.AUTO)
	private Long id;
	private Integer leaf;
	private Integer status;
	private String name;
	private String route;
	private String desciption;
	private Integer parent;
	
	public System()
	{
		super();
	}
	
	public System(Long id, Integer leaf, Integer status, String name,
			String route, String desciption, Integer parent)
	{
		super();
		this.id = id;
		this.leaf = leaf;
		this.status = status;
		this.name = name;
		this.route = route;
		this.desciption = desciption;
		this.parent = parent;
	}
	
	public Long getId()
	{
		return id;
	}
	public void setId(Long id)
	{
		this.id = id;
	}
	public Integer getLeaf()
	{
		return leaf;
	}
	public void setLeaf(Integer leaf)
	{
		this.leaf = leaf;
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
	public String getRoute()
	{
		return route;
	}
	public void setRoute(String route)
	{
		this.route = route;
	}
	public String getDesciption()
	{
		return desciption;
	}
	public void setDesciption(String desciption)
	{
		this.desciption = desciption;
	}
	public Integer getParent()
	{
		return parent;
	}
	public void setParent(Integer parent)
	{
		this.parent = parent;
	}

	@Override
	public String toString()
	{
		return "System [id=" + id + ", leaf=" + leaf + ", status=" + status
				+ ", name=" + name + ", route=" + route + ", desciption="
				+ desciption + ", parent=" + parent + "]";
	}
}
