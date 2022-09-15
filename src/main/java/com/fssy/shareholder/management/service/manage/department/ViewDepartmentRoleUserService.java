package com.fssy.shareholder.management.service.manage.department;

import com.fssy.shareholder.management.pojo.manage.department.ViewDepartmentRoleUser;

import java.util.List;
import java.util.Map;

/**
 * @author banqunwei
 * @title: ViewDepartmentRoleUserService
 * @description: 用户-部门-角色实现类
 * @date 2022/4/3
 */
public interface ViewDepartmentRoleUserService {


    /**
     * 通过查询条件查询用户列表
     *
     * @param params 查询条件
     * @return 用户列表
     */
    List<ViewDepartmentRoleUser> findViewDepartmentRoleUserDataListByParams(Map<String, Object> params);

    /**
     * 使用于xm-select控件使用数据
     * @param params 查询条件为active激活状态
     * @param selectedIds 选中数据的id
     * @return 列表数据
     */
    List<Map<String,Object>> findViewDepartmentRoleUserSelectedDataListByParams(Map<String,Object> params, List<String> selectedIds);

}
