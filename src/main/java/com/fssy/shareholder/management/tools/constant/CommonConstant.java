package com.fssy.shareholder.management.tools.constant;

/**
 * 常量接口
 *
 * @author Solomon
 */
public interface CommonConstant
{
    String METHOD_RETURN_IS_VIEW = "method_return_is_view";

    /**
     * 权限分类:菜单
     */
    int PERMISSION_TYPE_FOR_MENU = 0;

    /**
     * 权限分类:按钮
     */
    int PERMISSION_TYPE_FOR_BUTTON = 1;

    /**
     * 系统类别：管理系统
     */
    int MANAGE_SYSTEM = 1;

    /**
     * 系统类别：业务系统
     */
    int BUSINESS_SYSTEM = 2;

    /**
     * 系统类别：供应商系统
     */
    int SUPPLIER_SYSTEM = 3;

    /**
     * 角色：供应商
     */
    int ROLE_SUPPLIER = 63;

    /**
     * 否
     */
    int FALSE = 0;

    /**
     * 是
     */
    int TRUE = 1;

    /**
     * 鉴权缓存块名字
     */
    String CACHE_AUTHORIZATION = "authorizationCache";

    /**
     * 认证缓存块名字，一般是session管理，只有api时为缓存管理
     */
    String CACHE_AUTHENTICATION = "authenticationCache";


    /**
     * 导入结果：读取写入成功
     */
    Integer IMPORT_RESULT_SUCCESS = 1;

    /**
     * 导入结果：读取失败
     */
    Integer IMPORT_RESULT_FAILED = 2;


    /**
     * 数据状态：是
     */
    String YES = "是";

    /**
     * 数据状态：否
     */
    String NO = "否";

	/** 组织结构分类：部门 */
	Integer DEPARTMENT_TYPE_DEPARTMENT = 1;

	/** 组织结构分类：课室 */
	Integer DEPARTMENT_TYPE_OFFICE = 2;

	/** 组织结构分类：班/课室的细分 */
	Integer DEPARTMENT_TYPE_CLASS = 3;
}
