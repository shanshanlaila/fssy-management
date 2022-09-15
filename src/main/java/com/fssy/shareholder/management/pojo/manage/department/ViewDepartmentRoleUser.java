/**   
 * ------------------------修改日志---------------------------------
 * 修改人                 修改日期                   修改内容
 * 兰宇铧		   2022-04-18	      添加企业微信账号
 */
package com.fssy.shareholder.management.pojo.manage.department;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableName;

/**
 * @Title: ViewDepartmentRoleUser.java
 * @Description: 组织结构，用户，角色关联表与组织结构表，角色表，用户表视图实体类
 * @author Solomon
 * @date 2021年12月20日 下午5:02:44
 */
@TableName("view_department_role_user")
public class ViewDepartmentRoleUser implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5135996499391829296L;

	private Long roleId;

	private Long userId;

	private Long theDepartmentId;

	private String userName;

	private String account;

	private String theDepartmentName;

	private Long parent;

	private Long departmentId;

	private Long officeId;

	private Long classId;

	private Integer departmentType;

	private String departmentName;

	private String officeName;

	private String className;

	private String roleName;
	
	private String qyweixinUserId;

	public ViewDepartmentRoleUser(Long roleId, Long userId, Long theDepartmentId, String userName,
			String account, String theDepartmentName, Long parent, Long departmentId, Long officeId,
			Long classId, Integer departmentType, String departmentName, String officeName,
			String className, String roleName, String qyweixinUserId)
	{
		super();
		this.roleId = roleId;
		this.userId = userId;
		this.theDepartmentId = theDepartmentId;
		this.userName = userName;
		this.account = account;
		this.theDepartmentName = theDepartmentName;
		this.parent = parent;
		this.departmentId = departmentId;
		this.officeId = officeId;
		this.classId = classId;
		this.departmentType = departmentType;
		this.departmentName = departmentName;
		this.officeName = officeName;
		this.className = className;
		this.roleName = roleName;
		this.qyweixinUserId = qyweixinUserId;
	}

	public ViewDepartmentRoleUser()
	{
		super();
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

	public Long getTheDepartmentId()
	{
		return theDepartmentId;
	}

	public void setTheDepartmentId(Long theDepartmentId)
	{
		this.theDepartmentId = theDepartmentId;
	}

	public String getUserName()
	{
		return userName;
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
	}

	public String getAccount()
	{
		return account;
	}

	public void setAccount(String account)
	{
		this.account = account;
	}

	public String getTheDepartmentName()
	{
		return theDepartmentName;
	}

	public void setTheDepartmentName(String theDepartmentName)
	{
		this.theDepartmentName = theDepartmentName;
	}

	public Long getParent()
	{
		return parent;
	}

	public void setParent(Long parent)
	{
		this.parent = parent;
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

	public String getRoleName()
	{
		return roleName;
	}

	public void setRoleName(String roleName)
	{
		this.roleName = roleName;
	}

	public String getQyweixinUserId()
	{
		return qyweixinUserId;
	}

	public void setQyWeixinUserId(String qyweixinUserId)
	{
		this.qyweixinUserId = qyweixinUserId;
	}

	@Override
	public String toString()
	{
		return "ViewDepartmentRoleUser [roleId=" + roleId + ", userId=" + userId
				+ ", theDepartmentId=" + theDepartmentId + ", userName=" + userName + ", account="
				+ account + ", theDepartmentName=" + theDepartmentName + ", parent=" + parent
				+ ", departmentId=" + departmentId + ", officeId=" + officeId + ", classId="
				+ classId + ", departmentType=" + departmentType + ", departmentName="
				+ departmentName + ", officeName=" + officeName + ", className=" + className
				+ ", roleName=" + roleName + ", qyweixinUserId=" + qyweixinUserId + "]";
	}

}
