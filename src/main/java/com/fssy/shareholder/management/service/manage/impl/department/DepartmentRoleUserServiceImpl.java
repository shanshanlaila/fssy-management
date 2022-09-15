/**   
 * ------------------------修改日志---------------------------------
 * 修改人                 修改日期                   修改内容
 * 兰宇铧		    2022-01-20   	 清楚所有用户权限缓存
 * 
 * 修改人                 修改日期                   修改内容
 * 兰宇铧		    2022-01-21 		 修改问题，关联用户显示不正确问题，不能用==号;允许清空岗位的所有定岗功能
 */
package com.fssy.shareholder.management.service.manage.impl.department;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fssy.shareholder.management.tools.common.UserCacheTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.fssy.shareholder.management.mapper.manage.department.DepartmentRoleUserMapper;
import com.fssy.shareholder.management.mapper.manage.user.UserMapper;
import com.fssy.shareholder.management.pojo.manage.department.DepartmentRoleUser;
import com.fssy.shareholder.management.pojo.manage.user.User;
import com.fssy.shareholder.management.service.manage.department.DepartmentRoleUserService;

/**
 * 组织结构功能设置用户角色业务实现类
 * 
 * @author Solomon
 */
@Service
public class DepartmentRoleUserServiceImpl implements DepartmentRoleUserService
{
	@Autowired
	private DepartmentRoleUserMapper mapper;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private UserCacheTool userCacheTool;

	@Override
	@Transactional
	public boolean changeRoleUserFromDepartment(Long departmentId, Long roleId,
			List<String> userList)
	{
		// 删除原来组织和角色相应的用户
		QueryWrapper<DepartmentRoleUser> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("departmentId", departmentId).eq("roleId", roleId);
		mapper.delete(queryWrapper);

		// 添加组织和用户新的角色
		List<DepartmentRoleUser> entityList = new ArrayList<DepartmentRoleUser>();
		for (String userIdStr : userList)
		{
			if (ObjectUtils.isEmpty(userIdStr))
			{
				continue;
			}
			Long userId = Long.valueOf(userIdStr);
			DepartmentRoleUser entity = new DepartmentRoleUser(null, roleId, userId, departmentId);
			entityList.add(entity);
		}

		int num = 0;
		// 2022-01-21 允许清空岗位的所有定岗功能
		if (!ObjectUtils.isEmpty(entityList))
		{
			num = mapper.insertBatchSomeColumn(entityList);
		}

		// 清除当前用户shiro框架使用的缓存
		// 2022-01-20 清楚所有用户权限缓存
		userCacheTool.removeAllAuthorizationCache();

		if (num > 0)
		{
			return true;
		}
		return false;
	}

	@Override
	public List<Map<String, Object>> findSelectedUserList(Long departmentId,
			Long roleId)
	{
		QueryWrapper<DepartmentRoleUser> queryWrapper = new QueryWrapper<>();
		List<DepartmentRoleUser> departmentRoleUserList = new ArrayList<>();
		if (!ObjectUtils.isEmpty(roleId))
		{
			queryWrapper.eq("departmentId", departmentId).eq("roleId", roleId);
			departmentRoleUserList = mapper.selectList(queryWrapper);
		}

		// 查询所有用户
		List<User> userList = userMapper.selectList(null);
		List<Map<String, Object>> resultList = new ArrayList<>();
		// 为选取的用户添加selected属性
		for (User user : userList)
		{
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("name", user.getName());
			result.put("value", user.getId());
			result.put("selected", false);
			for (DepartmentRoleUser departmentRoleUser : departmentRoleUserList)
			{
				// 2022-01-21 修改问题，关联用户显示不正确问题，不能用==号
				if (departmentRoleUser.getUserId().equals(user.getId()))
				{
					result.put("selected", true);
					break;
				}
			}
			resultList.add(result);
		}
		return resultList;
	}

}
