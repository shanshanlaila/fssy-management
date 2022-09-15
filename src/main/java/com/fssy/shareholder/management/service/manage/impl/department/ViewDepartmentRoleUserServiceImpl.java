package com.fssy.shareholder.management.service.manage.impl.department;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fssy.shareholder.management.mapper.manage.department.ViewDepartmentRoleUserMapper;
import com.fssy.shareholder.management.pojo.manage.department.ViewDepartmentRoleUser;
import com.fssy.shareholder.management.service.manage.department.ViewDepartmentRoleUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author banqunwei
 * @title: ViewDepartmentRoleUserServiceImpl
 * @description: 用户-部门-角色实现类
 * @date 2022/4/3
 */
@Service
public class ViewDepartmentRoleUserServiceImpl implements ViewDepartmentRoleUserService {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(ViewDepartmentRoleUserServiceImpl.class);


    @Autowired
    private ViewDepartmentRoleUserMapper viewDepartmentRoleUserMapper;

    @Override
    public List<ViewDepartmentRoleUser> findViewDepartmentRoleUserDataListByParams(Map<String, Object> params) {
        LOGGER.debug("查询所有用户");
        QueryWrapper<ViewDepartmentRoleUser> queryWrapper = getQueryWrapper(params);
        List<ViewDepartmentRoleUser> users = viewDepartmentRoleUserMapper.selectList(queryWrapper);
        return users;
    }

    private QueryWrapper<ViewDepartmentRoleUser> getQueryWrapper(Map<String, Object> params) {
        QueryWrapper<ViewDepartmentRoleUser> queryWrapper = new QueryWrapper<>();
        if (params.containsKey("userId")) {
            queryWrapper.eq("userId", params.get("userId"));
        }
        if (params.containsKey("userNameEq")) {
            queryWrapper.eq("userName", params.get("userNameEq"));
        }
        if (params.containsKey("theDepartmentName")) {
            queryWrapper.eq("theDepartmentName", params.get("theDepartmentName"));
        }
        if (params.containsKey("roleName")) {
            queryWrapper.in("roleName", params.get("roleName"));
        }

        return queryWrapper;
    }

    @Override
    public List<Map<String, Object>> findViewDepartmentRoleUserSelectedDataListByParams(Map<String, Object> params, List<String> selectedIds) {
        List<ViewDepartmentRoleUser> userList = findViewDepartmentRoleUserDataListByParams(params);
        List<Map<String, Object>> resultList = new ArrayList<>();
        // 为选取的用户添加selected属性
        Map<String, Object> result;
        for (ViewDepartmentRoleUser viewDepartmentRoleUser : userList)
        {
            result = new HashMap<String, Object>();
            result.put("name", viewDepartmentRoleUser.getUserName());
            result.put("value", viewDepartmentRoleUser.getUserId());
            result.put("id", viewDepartmentRoleUser.getUserId());
            boolean selected = false;
            for (int i = 0; i < selectedIds.size(); i++)
            {
                if (selectedIds.get(i).equals(viewDepartmentRoleUser.getUserId().toString()))
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
}
