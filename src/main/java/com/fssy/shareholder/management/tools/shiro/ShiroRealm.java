/**
 * 修改人                 修改日期                   修改内容
 * 兰宇铧		   2021-12-20 	      添加的角色有三个部分：角色，组织以及组织角色，如：测试部门，测试员就保存测试部门，测试员，测试部门测试员
 */
package com.fssy.shareholder.management.tools.shiro;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fssy.shareholder.management.mapper.manage.department.ViewDepartmentRolePermissionMapper;
import com.fssy.shareholder.management.mapper.manage.department.ViewDepartmentRoleUserMapper;
import com.fssy.shareholder.management.mapper.manage.permission.PermissionMapper;
import com.fssy.shareholder.management.mapper.manage.user.UserMapper;
import com.fssy.shareholder.management.pojo.manage.department.ViewDepartmentRolePermission;
import com.fssy.shareholder.management.pojo.manage.department.ViewDepartmentRoleUser;
import com.fssy.shareholder.management.pojo.manage.permission.Permission;
import com.fssy.shareholder.management.pojo.manage.user.User;
import com.fssy.shareholder.management.tools.constant.CommonConstant;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Title: ShiroRealm.java
 * @Description: 重写shiro的认证和授权逻辑  
 * @author Solomon  
 * @date 2021年12月20日 下午5:11:14
 */
public class ShiroRealm extends AuthorizingRealm
{
    private static final Logger LOGGER = LoggerFactory
            .getLogger(AuthorizingRealm.class);

	/**
	 * 用户数据访问实现类
	 */
	@Autowired
	private UserMapper userMapper;

	/**
	 * 权限数据访问实现类
	 */
	@Autowired
	private PermissionMapper permissionMapper;

	/**
	 * 组织结构，用户，权限关联表与组织结构表，角色表，权限表视图数据访问实现类
	 */
	@Autowired
	private ViewDepartmentRolePermissionMapper viewDepartmentRolePermissionMapper;

	/**
	 * 组织结构，用户，角色关联表与组织结构表，角色表，用户表视图数据访问实现类
	 */
	@Autowired
	private ViewDepartmentRoleUserMapper viewDepartmentRoleUserMapper;

    /**
     * 权限配置类
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(
            PrincipalCollection principalCollection)
    {
        LOGGER.error("========= doGetAuthorizationInfo =========");
        // 从session中获取用户名称
        User user = (User) SecurityUtils.getSubject().getPrincipal();
//		User user = (User) SecurityUtils.getSubject().getSession()
//				.getAttribute("currentUser");

        // 创建鉴权实体
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        Set<String> permissionSet = new HashSet<>();
        Set<String> roleSet = new HashSet<>();
        // 查询所有权限
        // 1)获取当前用户的用户表主键
        Long userId = user.getId();
        // 2)设置通用权限
        QueryWrapper<Permission> permissionQueryWrapper = new QueryWrapper<>();
        permissionQueryWrapper.eq("isNormal", 1).select("id",
                "authorizationName", "route");
        List<Permission> normalPermissionList = permissionMapper
                .selectList(permissionQueryWrapper);
        for (Permission viewDepartmentRolePermission : normalPermissionList)
        {
            permissionSet
                    .add(viewDepartmentRolePermission.getAuthorizationName());
        }
        // 3)查询该用户所有的组织结构和角色组合属性
        QueryWrapper<ViewDepartmentRoleUser> userRelationQueryWrapper = new QueryWrapper<>();
        userRelationQueryWrapper.eq("userId", userId);
        List<ViewDepartmentRoleUser> userRelationList = viewDepartmentRoleUserMapper
                .selectList(userRelationQueryWrapper);
        if (ObjectUtils.isEmpty(userRelationList))
        {
            authorizationInfo.setStringPermissions(permissionSet);
            authorizationInfo.setRoles(roleSet);
            return authorizationInfo;
        }
        // 2021-12-20 添加的角色有三个部分：角色，组织以及组织角色，如：测试部门，测试员就保存测试部门，测试员，测试部门测试员
        for (ViewDepartmentRoleUser viewDepartmentRoleUser : userRelationList)
		{
			roleSet.add(viewDepartmentRoleUser.getRoleName());
			// 2021-12-20 添加的角色有三个部分：角色，组织以及组织角色，如：测试部门，测试员就保存测试部门，测试员，测试部门测试员
			roleSet.add(viewDepartmentRoleUser.getTheDepartmentName());
			roleSet.add(viewDepartmentRoleUser.getTheDepartmentName()
					+ viewDepartmentRoleUser.getRoleName());
		}
        
        // 以下为通用鉴权的permission
        // 4)通过组织结构和角色组合查询组织结构，角色，权限视图获取authorizationName
        QueryWrapper<ViewDepartmentRolePermission> permissionRelationQueryWrapper = new QueryWrapper<>();
        permissionRelationQueryWrapper.ne("isNormal", 1).select("permissionId",
                "roleId", "departmentId", "authorizationName", "roleName");

        List<ViewDepartmentRolePermission> umnormalPermissionList = viewDepartmentRolePermissionMapper
                .selectList(permissionRelationQueryWrapper);
        // 筛选出当前用的权限,并添加到set中
        for (ViewDepartmentRolePermission viewDepartmentRolePermission : umnormalPermissionList)
        {
            for (ViewDepartmentRoleUser viewUserDepartmentRoleUser : userRelationList)
            {
                if (viewUserDepartmentRoleUser
                        .getRoleId() == viewDepartmentRolePermission.getRoleId()
                        && viewUserDepartmentRoleUser
                        .getTheDepartmentId() == viewDepartmentRolePermission
                        .getDepartmentId())
                {
					permissionSet.add(viewDepartmentRolePermission.getAuthorizationName());
					// 2021-12-20 权限与用户的岗位和组织结构无关，取消
					break;
                }
            }
        }

        // 2021-12-13 查询所有视图的数据，以下为非通用的permission
        // 2021-12-20 权限与用户的岗位和组织结构无关，取消

		authorizationInfo.setStringPermissions(permissionSet);
        authorizationInfo.setRoles(roleSet);
        return authorizationInfo;
    }

    /**
     * 认证配置
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(
            AuthenticationToken authenticationToken)
            throws AuthenticationException
    {
        LOGGER.error("========= doGetAuthenticationInfo =========");
        if (ObjectUtils.isEmpty(authenticationToken.getPrincipal()))
        {
            return null;
        }
        // 获取用户信息
        String name = authenticationToken.getPrincipal().toString();
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.and(i -> i.or(n1 -> n1.eq("name", name))
                .or(n1 -> n1.eq("account", name))
                .or(n1 -> n1.eq("phone", name)));
        User user = userMapper.selectOne(queryWrapper);

        if (ObjectUtils.isEmpty(user))
        {
            throw new UnknownAccountException();
        }

        if (user.getActive() == CommonConstant.FALSE)
        {
            throw new DisabledAccountException();
        }

        // 验证token和simpleAuthenticationInfo的信息
        SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(
                user, user.getPassword(), getName());

        // 将用户保存到session中
        // 2021-9-26 移到doLogin方法
        return simpleAuthenticationInfo;
    }

}
