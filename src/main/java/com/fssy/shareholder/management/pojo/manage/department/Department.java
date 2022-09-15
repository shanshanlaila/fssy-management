/**   
 * ------------------------修改日志---------------------------------
 * 修改人                 修改日期                   修改内容
 * 兰宇铧			2021-11-10	 	添加departmentId、officeid、classId
 * 
 * 修改人                 修改日期                   修改内容
 * 兰宇铧			2021-12-23	 	添加factoryId、factoryName
 */
package com.fssy.shareholder.management.pojo.manage.department;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fssy.shareholder.management.pojo.common.BaseModel;

/**
 * @Title: Department.java
 * @Description: 组织结构实体类
 * @author Solomon
 * @date 2021年11月10日 上午9:38:54
 */
@TableName("basic_department")
public class Department extends BaseModel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8083674412415602403L;

	@TableId(type = IdType.AUTO)
	private Long id;

	private Integer leaf;

	private String name;

	private Integer parent;

	private String code;

	/** desc是关键字，需要加``符号 */
	@TableField("`desc`")
	private String desc;

	private Long departmentId;

	private String departmentName;

	private Long officeId;

	private String officeName;

	private Long classId;

	private String className;

	private Integer departmentType;

	private Long factoryId;

	private String factoryName;

	public Department(Long id, Integer leaf, String name, Integer parent, String code, String desc,
			Long departmentId, String departmentName, Long officeId, String officeName,
			Long classId, String className, Integer departmentType, Long factoryId,
			String factoryName)
	{
		super();
		this.id = id;
		this.leaf = leaf;
		this.name = name;
		this.parent = parent;
		this.code = code;
		this.desc = desc;
		this.departmentId = departmentId;
		this.departmentName = departmentName;
		this.officeId = officeId;
		this.officeName = officeName;
		this.classId = classId;
		this.className = className;
		this.departmentType = departmentType;
		this.factoryId = factoryId;
		this.factoryName = factoryName;
	}

	public Department()
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

	public Integer getLeaf()
	{
		return leaf;
	}

	public void setLeaf(Integer leaf)
	{
		this.leaf = leaf;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public Integer getParent()
	{
		return parent;
	}

	public void setParent(Integer parent)
	{
		this.parent = parent;
	}

	public String getDesc()
	{
		return desc;
	}

	public void setDesc(String desc)
	{
		this.desc = desc;
	}

	public String getCode()
	{
		return code;
	}

	public void setCode(String code)
	{
		this.code = code;
	}

	public Long getDepartmentId()
	{
		return departmentId;
	}

	public void setDepartmentId(Long departmentId)
	{
		this.departmentId = departmentId;
	}

	public Long getOfficeId()
	{
		return officeId;
	}

	public void setOfficeId(Long officeId)
	{
		this.officeId = officeId;
	}

	public Long getClassId()
	{
		return classId;
	}

	public void setClassId(Long classId)
	{
		this.classId = classId;
	}

	public Long getFactoryId()
	{
		return factoryId;
	}

	public void setFactoryId(Long factoryId)
	{
		this.factoryId = factoryId;
	}

	public String getFactoryName()
	{
		return factoryName;
	}

	public void setFactoryName(String factoryName)
	{
		this.factoryName = factoryName;
	}

	public Integer getDepartmentType()
	{
		return departmentType;
	}

	public void setDepartmentType(Integer departmentType)
	{
		this.departmentType = departmentType;
	}

	public String getDepartmentName()
	{
		return departmentName;
	}

	public void setDepartmentName(String departmentName)
	{
		this.departmentName = departmentName;
	}

	public String getOfficeName()
	{
		return officeName;
	}

	public void setOfficeName(String officeName)
	{
		this.officeName = officeName;
	}

	public String getClassName()
	{
		return className;
	}

	public void setClassName(String className)
	{
		this.className = className;
	}

	@Override
	public String toString()
	{
		return "Department [id=" + id + ", leaf=" + leaf + ", name=" + name + ", parent=" + parent
				+ ", code=" + code + ", desc=" + desc + ", departmentId=" + departmentId
				+ ", departmentName=" + departmentName + ", officeId=" + officeId + ", officeName="
				+ officeName + ", classId=" + classId + ", className=" + className
				+ ", departmentType=" + departmentType + ", factoryId=" + factoryId
				+ ", factoryName=" + factoryName + "]";
	}

}
