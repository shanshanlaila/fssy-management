/**
 * 修改人                 修改日期                   修改内容
 * 兰宇铧		   2021-11-29 		添加权限过滤，解决认证缓存不同清空的问题
 * 
 * 修改人                 修改日期                   修改内容
 * 兰宇铧		   2022-03-24 	  	simpleAuthorizationInfo会不名原因为空报错，加上判空，重新查询再放入缓存
 */
package com.fssy.shareholder.management.tools.common;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fssy.shareholder.management.mapper.manage.department.ViewDepartmentRolePermissionMapper;
import com.fssy.shareholder.management.mapper.manage.department.ViewDepartmentRoleUserMapper;
import com.fssy.shareholder.management.mapper.manage.permission.PermissionMapper;
import com.fssy.shareholder.management.pojo.manage.department.ViewDepartmentRolePermission;
import com.fssy.shareholder.management.pojo.manage.department.ViewDepartmentRoleUser;
import com.fssy.shareholder.management.pojo.manage.permission.Permission;
import com.fssy.shareholder.management.pojo.manage.user.User;
import com.fssy.shareholder.management.tools.constant.CommonConstant;
import com.fssy.shareholder.management.tools.shiro.ShiroRealm;

/**
 * 用户缓存工具类</br>
 * 用于在shiro整合缓存时,对cacheManager进行管理
 *
 * @author Solomon
 */
@Component
public class UserCacheTool
{
    @Autowired
    private CacheManager cacheManger;

    // 以下三个mapper都是2021-12-15添加的
    @Autowired
    private PermissionMapper permissionMapper;

    @Autowired
    private ViewDepartmentRolePermissionMapper viewDepartmentRolePermissionMapper;
    
	/**
	 * 组织结构，用户，角色关联表与组织结构表，角色表，用户表视图数据访问实现类
	 */
	@Autowired
	private ViewDepartmentRoleUserMapper viewDepartmentRoleUserMapper;

    /**
     * 清空当前用户权限缓存
     */
    public void removeCurrentUserAuthorizationCache()
    {
        // 刷新ehcache缓存
        Cache<SimplePrincipalCollection, Object> cache = cacheManger
                .getCache(CommonConstant.CACHE_AUTHORIZATION);
        // 获取当前用户
        PrincipalCollection principals = SecurityUtils.getSubject().getPrincipals();
        // 2021-11-29 这里需要注意，认证与授权的key在shiro中是不同的，认证使用的是登录的用户名作为key，而授权则用的是Subject作为key
        cache.remove(new SimplePrincipalCollection(principals));
    }

    /**
     * 清空当前用户认证缓存
     */
    public void removeCurrentUserAuthenticationCache()
    {
        // 刷新ehcache缓存
        DefaultWebSecurityManager securityManager = (DefaultWebSecurityManager) SecurityUtils
                .getSecurityManager();
        ShiroRealm shiroRealm = (ShiroRealm) securityManager.getRealms().iterator().next();
        // 2021-11-29 这里需要注意，认证与授权的key在shiro中是不同的，认证使用的是登录的用户名作为key，而授权则用的是Subject作为key
        // 这里涉及修改登录人以外的用户，所以最好是全部清空
        shiroRealm.getAuthenticationCache().clear();
        // 使用账户登录的Key
        // 使用名字登录的key
    }

    /**
     * 获取当前用户的鉴权字符串<br/>
     * 2021-11-29 加权限过滤
     *
     * @return Set 权限串
     */
    public Set<String> getCurrentUserAuthorizationStringPermmissions()
    {
        // 刷新ehcache缓存
        Cache<SimplePrincipalCollection, Object> cache = cacheManger
                .getCache(CommonConstant.CACHE_AUTHORIZATION);
        // 获取当前用户
        PrincipalCollection principals = SecurityUtils.getSubject().getPrincipals();
//		SimpleAuthorizationInfo simpleAuthorizationInfo = (SimpleAuthorizationInfo) cache
//				.get(new SimplePrincipalCollection(principals));


        // ---- 2021-12-15 start-------------
        // 由于分不同系统之后，登录后取不到权限，上述的simpleAuthorizationInfo为null，会报错。不过代码确实会执行到ShiroReaml中的goGetAuthorizationInfo，因为会输出Logger.
        // 所以判断上述cache中获取的simpleAuthorizationInfo，如果为空，就直接执行ShiroRealm中的代码

		// 创建鉴权实体
		SimplePrincipalCollection simplePrincipalCollection = new SimplePrincipalCollection(
				principals);
		SimpleAuthorizationInfo simpleAuthorizationInfo;
		simpleAuthorizationInfo = (SimpleAuthorizationInfo) cache.get(simplePrincipalCollection);
		if (ObjectUtils.isEmpty(simpleAuthorizationInfo))
		{
			simpleAuthorizationInfo = initAuthorizationInfo();
			cache.put(simplePrincipalCollection, simpleAuthorizationInfo);
		}


        // ---- 2021-12-15 end----------

        HashSet<String> permissions = (HashSet<String>) simpleAuthorizationInfo
                .getStringPermissions();

        return permissions;
    }

    /**
     * 获取当前用户的角色字符串<br/>
     * 2021-11-29 加权限过滤
     *
     * @return Set 角色串
     */
    public Set<String> getCurrentUserAuthorizationStringRoles()
    {
		// 刷新ehcache缓存
		Cache<SimplePrincipalCollection, Object> cache = cacheManger
				.getCache(CommonConstant.CACHE_AUTHORIZATION);
		// 获取当前用户
		PrincipalCollection principals = SecurityUtils.getSubject().getPrincipals();

		SimplePrincipalCollection simplePrincipalCollection = new SimplePrincipalCollection(
				principals);
		SimpleAuthorizationInfo simpleAuthorizationInfo = (SimpleAuthorizationInfo) cache
				.get(simplePrincipalCollection);

		// 2022-03-24 simpleAuthorizationInfo会不名原因为空报错，加上判空，重新查询再放入缓存
		if (ObjectUtils.isEmpty(simpleAuthorizationInfo))
		{
			simpleAuthorizationInfo = initAuthorizationInfo();
			cache.put(simplePrincipalCollection, simpleAuthorizationInfo);
		}
		HashSet<String> roles = (HashSet<String>) simpleAuthorizationInfo.getRoles();

		return roles;
    }
    
	/**
	 * 清空所有用户权限缓存
	 */
	public void removeAllAuthorizationCache()
	{
		// 刷新ehcache所有用户的权限缓存
		DefaultWebSecurityManager securityManager = (DefaultWebSecurityManager) SecurityUtils
				.getSecurityManager();
		ShiroRealm shiroRealm = (ShiroRealm) securityManager.getRealms().iterator().next();
		shiroRealm.getAuthorizationCache().clear();
    }
	
	/**
	 * 获取当前登录用户的岗位
	 * 
	 * @return 岗位描述
	 */
	public String getStationDescribe()
	{
		// 当前登录用户
    	User user = (User) SecurityUtils.getSubject().getPrincipal();
    	// 查询当前用户的岗位信息
    	QueryWrapper<ViewDepartmentRoleUser> viewDepartmentRoleUserQueryWrapper = new QueryWrapper<>();
    	viewDepartmentRoleUserQueryWrapper.eq("userId", user.getId());
		List<ViewDepartmentRoleUser> departmentRoleList = viewDepartmentRoleUserMapper
				.selectList(viewDepartmentRoleUserQueryWrapper);
		// 拼接岗位信息
		StringBuilder sb = new StringBuilder();
		if (ObjectUtils.isEmpty(departmentRoleList))
		{
			sb.append("管理员");
		}
		else
		{
			for (ViewDepartmentRoleUser viewDepartmentRoleUser : departmentRoleList)
			{
				if (ObjectUtils.isEmpty(sb))
				{
					sb.append(viewDepartmentRoleUser.getTheDepartmentName())
							.append(viewDepartmentRoleUser.getRoleName());
				}
				else
				{
					sb.append(";").append(viewDepartmentRoleUser.getTheDepartmentName())
							.append(viewDepartmentRoleUser.getRoleName());
				}
			}
		}
		return sb.toString();
	}
	
	/**
	 * 重新创建shiro的SimpleAuthorizationInfo
	 * 
	 * @return SimpleAuthorizationInfo
	 */
	private SimpleAuthorizationInfo initAuthorizationInfo()
	{
		// 创建鉴权实体
		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		User user = (User) SecurityUtils.getSubject().getPrincipal();
		Set<String> permissionSet = new HashSet<>();
		Set<String> roleSet = new HashSet<>();
		// 查询所有权限
		// 1)获取当前用户的用户表主键
		Long userId = user.getId();
		// 2)设置通用权限
		QueryWrapper<Permission> permissionQueryWrapper = new QueryWrapper<>();
		permissionQueryWrapper.eq("isNormal", 1).select("id", "authorizationName", "route");
		List<Permission> normalPermissionList = permissionMapper.selectList(permissionQueryWrapper);
		for (Permission viewDepartmentRolePermission : normalPermissionList)
		{
			permissionSet.add(viewDepartmentRolePermission.getAuthorizationName());
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
		permissionRelationQueryWrapper.ne("isNormal", 1).select("permissionId", "roleId",
				"departmentId", "authorizationName", "roleName");

		List<ViewDepartmentRolePermission> umnormalPermissionList = viewDepartmentRolePermissionMapper
				.selectList(permissionRelationQueryWrapper);
		// 筛选出当前用的权限,并添加到set中
		for (ViewDepartmentRolePermission viewDepartmentRolePermission : umnormalPermissionList)
		{
			for (ViewDepartmentRoleUser viewUserDepartmentRoleUser : userRelationList)
			{
				if (viewUserDepartmentRoleUser.getRoleId() == viewDepartmentRolePermission
						.getRoleId()
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

		authorizationInfo.setStringPermissions(permissionSet);
		authorizationInfo.setRoles(roleSet);
		return authorizationInfo;
	}
}
