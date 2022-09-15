package com.fssy.shareholder.management.service.manage.user;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.pojo.manage.user.User;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户功能业务接口
 * 
 * @author Solomon
 */
/**
 * @author TerryZeng
 * @description: 新增了用于下拉选项findUserSelectedDataListByParams接口
 * @date 2021/10/1912:12
 */

public interface UserService
{
	/**
	 * 添加用户数据
	 * 
	 * @param user 用户实体
	 * @return 用户实体
	 */
	User saveUser(User user);

	/**
	 * 根据id删除用户
	 * 
	 * @param id 用户表主键
	 * @return true/false
	 */
	boolean deleteUserById(int id);

	/**
	 * 通过查询条件查询用户列表
	 * 
	 * @param params 查询条件
	 * @return 用户列表
	 */
	List<User> findUserDataListByParams(Map<String, Object> params);

	/**
	 * 通过查询条件分页查询用户列表
	 * 
	 * @param params 查询条件
	 * @return 用户分页数据
	 */
	Page<User> findUserDataListPerPageByParams(Map<String, Object> params);

	/**
	 * 修改用户数据
	 * 
	 * @param user 用户实体
	 * @return true/false
	 */
	boolean updateUser(User user);

	/**
	 * 启用或不启用用户
	 * 
	 * @param id     用户表主键
	 * @param active 状态
	 * @return
	 */
	boolean activateOrInactivateUser(int id, int active);
	
	/**
	 * 使用于xm-select控件使用数据
	 * @param params 查询条件为active激活状态
	 * @param selectedIds 选中数据的id
	 * @return 列表数据
	 */
	List<Map<String,Object>> findUserSelectedDataListByParams(Map<String,Object> params,List<String> selectedIds);
	
	/**
	 * 查询所有用户
	 * 
	 * @return 用户列表
	 */
	List<Map<String, Object>> findAllUsers();

	/**
	 * 个人修改信息
	 * @param request
	 * @return
	 */
	boolean saveUserEdit(HttpServletRequest request);
	
	/**
	 * 通过查询条件分页查询用户列表（map）
	 * 
	 * @param params 查询条件
	 * @return 用户分页数据
	 */
	Page<Map<String, Object>> findUserDataMapListPerPageByParams(Map<String, Object> params);
}
