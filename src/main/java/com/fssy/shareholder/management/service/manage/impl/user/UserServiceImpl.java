/**
 * ------------------------修改日志---------------------------------
 * 修改人    修改日期                   修改内容
 * 曾   涛    2021-10-18      添加用户select控件查询方法
 * <p>
 * 修改人    修改日期                   修改内容
 * 兰宇铧    2021-11-29 	   添加清空认证数据缓存
 * <p>
 * 修改人    修改日期                   修改内容
 * 兰宇铧    2022-01-21 	   添加从用户列表添加用户的岗位权限
 */
package com.fssy.shareholder.management.service.manage.impl.user;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fssy.shareholder.management.tools.common.UserCacheTool;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.mapper.manage.department.DepartmentRoleUserMapper;
import com.fssy.shareholder.management.mapper.manage.department.ViewDepartmentRoleUserMapper;
import com.fssy.shareholder.management.mapper.manage.user.UserMapper;
import com.fssy.shareholder.management.pojo.manage.department.DepartmentRoleUser;
import com.fssy.shareholder.management.pojo.manage.department.ViewDepartmentRoleUser;
import com.fssy.shareholder.management.pojo.manage.user.User;
import com.fssy.shareholder.management.service.manage.user.UserService;
import com.fssy.shareholder.management.tools.exception.ServiceException;

/**
 * 用户功能业务接口实现类
 *
 * @author Solomon
 * @date 2021-10-18
 */
@Service
public class UserServiceImpl implements UserService
{
    private static final Logger LOGGER = LoggerFactory
            .getLogger(UserServiceImpl.class);

	/**
	 * 用户数据访问层实现类
	 */
	@Autowired
	private UserMapper userMapper;

	/**
	 * 组织结构功能设置角色的用户数据访问实现类
	 */
	@Autowired
	private DepartmentRoleUserMapper departmentRoleUserMapper;

	/**
	 * 用户缓存工具类
	 */
	@Autowired
	private UserCacheTool userCacheTool;

	/**
	 * 组织结构，用户，角色关联表与组织结构表，角色表，用户表视图数据访问实现类
	 */
	@Autowired
	private ViewDepartmentRoleUserMapper viewDepartmentRoleUserMapper;

    @Override
    @Transactional
    @CacheEvict(value = "users", key = "'allUser'")
    public User saveUser(User user)
    {
        if (ObjectUtils.isEmpty(user))
        {
            throw new ServiceException("保存用户信息，用户对象不能为空！");
        }
        // 生成UUID盐值
        String saltString = UUID.randomUUID().toString();
        // 使用shiro加密盐值
        ByteSource salt = ByteSource.Util.bytes(saltString);
        // 加密次数与登录校验保持一致
        int count = 20;
        // 加密密码
        String passwordString = new SimpleHash("MD5", user.getPassword(), salt,
                count).toString();
        // 替换密码
        user.setPassword(passwordString);
        // 添加盐
        user.setSalt(saltString);
        // 从身份证号中获取倒数第2位获得性别
//		String idnumber = user.getIDNumber();
//		int sex = Integer.valueOf(idnumber.substring(idnumber.length() - 2,
//				idnumber.length() - 1)) % 2;
//		user.setSex(sex);
        // 写入新数据
        userMapper.insert(user);
        return user;
    }

    @Override
    @Transactional
    @CacheEvict(value = "users", key = "'allUser'")
    public boolean deleteUserById(int id)
    {
        // 删除组织结构，角色，用户表数据
        QueryWrapper<DepartmentRoleUser> deleteUserRelationQueryWrapper = new QueryWrapper<>();
        deleteUserRelationQueryWrapper.eq("userId", id);
        departmentRoleUserMapper.delete(deleteUserRelationQueryWrapper);
        int num = userMapper.deleteById(id);

        // 2021-11-29 添加清空认证数据缓存
        userCacheTool.removeCurrentUserAuthenticationCache();
        if (num > 0)
        {
            return true;
        }
        return false;
    }

    @Override
    public List<User> findUserDataListByParams(Map<String, Object> params)
    {
        LOGGER.debug("查询所有用户");
        QueryWrapper<User> queryWrapper = getQueryWrapper(params);
        List<User> users = userMapper.selectList(queryWrapper);
        return users;
    }

    @Override
    @Transactional
    @CacheEvict(value = "users", key = "'allUser'")
    public boolean updateUser(User user)
    {
        // 添加修改时间
        int num = userMapper.updateById(user);
        // 2021-11-29 添加清空认证数据缓存
        userCacheTool.removeCurrentUserAuthenticationCache();
        if (num > 0)
        {
            return true;
        }
        return false;
    }

    // 需要使用缓存，value的值要在ehcache.xml中配置
    @Override
    public Page<User> findUserDataListPerPageByParams(
            Map<String, Object> params)
    {
		QueryWrapper<User> queryWrapper = getQueryWrapper(params);
		int limit = (int) params.get("limit");
		int page = (int) params.get("page");
		Page<User> myPage = new Page<>(page, limit);
		Page<User> userPage = userMapper.selectPage(myPage, queryWrapper);
		return userPage;
    }

    private QueryWrapper<User> getQueryWrapper(Map<String, Object> params)
    {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        // 主键查询
        if (params.containsKey("id"))
        {
            queryWrapper.eq("id", params.get("id"));
        }
        // 账号精确查询
        if (params.containsKey("accountEq"))
        {
            queryWrapper.eq("account", params.get("accountEq"));
        }
        // 2021-12-15 是否为供应商
        if (params.containsKey("isSupplier"))
        {
            queryWrapper.eq("isSupplier", params.get("isSupplier"));
        }
        // 性别查询
        if (params.containsKey("sex"))
        {
            queryWrapper.eq("sex", params.get("sex"));
        }
        // 上级查询
        if (params.containsKey("superior"))
        {
            queryWrapper.eq("superior", params.get("superior"));
        }
        // 用户状态查询
        if (params.containsKey("status"))
        {
            queryWrapper.eq("status", params.get("status"));
        }
        // 激活状态查询
        if (params.containsKey("active"))
        {
            queryWrapper.eq("active", params.get("active"));
        }
        // 用户名称查询
        if (params.containsKey("name"))
        {
            queryWrapper.like("name", params.get("name"));
        }
        // 账号查询
        if (params.containsKey("account"))
        {
            queryWrapper.like("account", params.get("account"));
        }
        // 身份证查询
        if (params.containsKey("IDNumber"))
        {
            queryWrapper.like("IDNumber", params.get("IDNumber"));
        }
        // 电话查询
        if (params.containsKey("phone"))
        {
            queryWrapper.like("phone", params.get("phone"));
        }
        // 关键字查询
        if (params.containsKey("key"))
        {
            queryWrapper.and(i -> i.or(n1 -> n1.like("name", params.get("key")))
                    .or(n1 -> n1.like("account", params.get("key")))
                    .or(n1 -> n1.like("phone", params.get("key")))
                    .or(n1 -> n1.like("IDNumber", params.get("key"))));
        }
        return queryWrapper;
    }

    @Override
    @Transactional
    @CacheEvict(value = "users", key = "'allUser'")
    public boolean activateOrInactivateUser(int id, int active)
    {
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<User>();
        // 修改人
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        updateWrapper.eq("id", id).set("active", active)
                .set("updatedAt", LocalDateTime.now())
                .set("updatedId", user.getId());
        int num = userMapper.update(null, updateWrapper);
        // 2021-11-29 添加清空认证数据缓存
        userCacheTool.removeCurrentUserAuthenticationCache();
        if (num > 0)
        {
            return true;
        }
        return false;
    }

    @Override
    public List<Map<String, Object>> findUserSelectedDataListByParams(
            Map<String, Object> params, List<String> selectedIds)
    {
        List<User> userList = findUserDataListByParams(params);
        List<Map<String, Object>> resultList = new ArrayList<>();
        // 为选取的用户添加selected属性
        Map<String, Object> result;
        for (User user : userList)
        {
            result = new HashMap<String, Object>();
            result.put("name", user.getName());
            result.put("value", user.getId());
            result.put("id", user.getId());
            boolean selected = false;
            for (int i = 0; i < selectedIds.size(); i++)
            {
                if (selectedIds.get(i).equals(user.getId().toString()))
                {
                    selected = true;
                    break;
                }
            }
            result.put("selected", selected);
            resultList.add(result);
        }
        return resultList;
    }

    @Override
    @Cacheable(value = "users", key = "'allUser'")
    public List<Map<String, Object>> findAllUsers()
    {
        return userMapper.selectMaps(null);
    }


    /**
     * 保存用户修改个人信息
     *
     * @param request
     * @return
     */
    @Override
    @Transactional
    @CacheEvict(value = "users", key = "'allUser'")
    public boolean saveUserEdit(HttpServletRequest request)
    {
        if (ObjectUtils.isEmpty(request))
        {
            throw new ServiceException("保存用户信息，用户对象不能为空！");
        }
        UpdateWrapper<User> userUpdateWrapper = new UpdateWrapper<>();
        userUpdateWrapper.eq("id", request.getParameter("id"))
                .set("phone", request.getParameter("phone"));

        if (!ObjectUtils.isEmpty(request.getParameter("password")))
        {
            if (request.getParameter("password").equals(request.getParameter("repass")))
            {
                String password = request.getParameter("password");
                if (password.length() < 6 || password.length() > 12 || password.contains(" "))
                {
                    throw new ServiceException("密码必须6到12位，且不能出现空格");
                }

                // 生成UUID盐值
                String saltString = UUID.randomUUID().toString();
                // 使用shiro加密盐值
                ByteSource salt = ByteSource.Util.bytes(saltString);
                // 加密次数与登录校验保持一致
                int count = 20;
                // 加密密码
                String passwordString = new SimpleHash("MD5", request.getParameter("password"), salt,
                        count).toString();
                userUpdateWrapper.set("password", passwordString).set("salt", saltString);
            } else
            {
                throw new ServiceException("两次密码不一致请确认后提交");
            }
        }
        // 写入新数据
        int res = userMapper.update(null, userUpdateWrapper);
        // 添加清空认证数据缓存
        userCacheTool.removeCurrentUserAuthenticationCache();
        return res > 0;
    }

	@Override
	public Page<Map<String, Object>> findUserDataMapListPerPageByParams(Map<String, Object> params)
	{
		QueryWrapper<User> queryWrapper = getQueryWrapper(params);
		int limit = (int) params.get("limit");
		int page = (int) params.get("page");
		Page<Map<String, Object>> myPage = new Page<>(page, limit);
		Page<Map<String, Object>> userPage = userMapper.selectMapsPage(myPage, queryWrapper);
		// 2022-01-20 添加从用户列表添加用户的岗位权限
		QueryWrapper<ViewDepartmentRoleUser> viewDepartmentRoleUserQueryWrapper;
		for (Map<String, Object> userMap : userPage.getRecords())
		{
			// 查询用户岗位信息
			viewDepartmentRoleUserQueryWrapper = new QueryWrapper<>();
			viewDepartmentRoleUserQueryWrapper.eq("userId", userMap.get("id"));
			List<ViewDepartmentRoleUser> departmentRoleList = viewDepartmentRoleUserMapper
					.selectList(viewDepartmentRoleUserQueryWrapper);
			// 添加到list中
			userMap.put("children", departmentRoleList);
		}
		return userPage;
	}

    @Override
    public User getByName(LambdaQueryWrapper<User> userQueryWrapper) {
        return userMapper.selectOne(userQueryWrapper);
    }
}
