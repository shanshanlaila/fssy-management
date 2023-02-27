package com.fssy.shareholder.management.service.system.impl.company;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fssy.shareholder.management.mapper.manage.company.CompanyMapper;
import com.fssy.shareholder.management.mapper.manage.department.DepartmentMapper;
import com.fssy.shareholder.management.mapper.manage.role.RoleMapper;
import com.fssy.shareholder.management.mapper.manage.user.UserMapper;
import com.fssy.shareholder.management.mapper.system.company.PositionDepartmentCompanyMapper;
import com.fssy.shareholder.management.pojo.manage.company.Company;
import com.fssy.shareholder.management.pojo.manage.department.Department;
import com.fssy.shareholder.management.pojo.manage.role.Role;
import com.fssy.shareholder.management.pojo.manage.user.User;
import com.fssy.shareholder.management.pojo.system.company.PositionDepartmentCompany;
import com.fssy.shareholder.management.service.system.company.PositionDepartmentCompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <p>
 * 基础-员工-职位-部门-公司表	 服务实现类
 * </p>
 *
 * @author 农浩
 * @since 2023-02-15
 */
@Service
public class PositionDepartmentCompanyServiceImpl extends ServiceImpl<PositionDepartmentCompanyMapper, PositionDepartmentCompany> implements PositionDepartmentCompanyService {
    @Autowired
    private PositionDepartmentCompanyMapper positionDepartmentCompanyMapper;
    @Autowired
    private CompanyMapper companyMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private DepartmentMapper departmentMapper;
    @Autowired
    private RoleMapper roleMapper;

    @Override
    public Page<PositionDepartmentCompany> findDataListByParams(Map<String, Object> params) {
        QueryWrapper<PositionDepartmentCompany> queryWrapper = getQueryWrapper(params).orderByDesc("id");
        Page<PositionDepartmentCompany> myPage = new Page<>((int) params.get("page"), (int) params.get("limit"));
        return positionDepartmentCompanyMapper.selectPage(myPage, queryWrapper);
    }

    private QueryWrapper<PositionDepartmentCompany> getQueryWrapper(Map<String, Object> params) {
        QueryWrapper<PositionDepartmentCompany> queryWrapper = new QueryWrapper<>();
        if (params.containsKey("id")) {
            queryWrapper.eq("id", params.get("id"));
        }
        queryWrapper.eq(params.containsKey("departmentId"), "departmentId", params.get("departmentId"));//上一行代码格式的简化版
        queryWrapper.eq(params.containsKey("userId"), "userId", params.get("userId"));
        queryWrapper.eq(params.containsKey("userName"), "userName", params.get("userName"));
        queryWrapper.eq(params.containsKey("internalUser"), "internalUser", params.get("internalUser"));
        queryWrapper.eq(params.containsKey("startDate"), "startDate", params.get("startDate"));
        queryWrapper.eq(params.containsKey("endDate"), "endDate", params.get("endDate"));
        queryWrapper.eq(params.containsKey("appointPositionDocId"), "appointPositionDocId", params.get("appointPositionDocId"));
        queryWrapper.like(params.containsKey("appointPositionDocName"), "appointPositionDocName", params.get("appointPositionDocName"));
        queryWrapper.like(params.containsKey("appointPositionPeriod"), "appointPositionPeriod", params.get("appointPositionPeriod"));
        queryWrapper.eq(params.containsKey("appointMode"), "appointMode", params.get("appointMode"));
        queryWrapper.eq(params.containsKey("postionDepartComId"), "postionDepartComId", params.get("postionDepartComId"));
        queryWrapper.eq(params.containsKey("positionId"), "positionId", params.get("positionId"));
        queryWrapper.like(params.containsKey("positionName"), "positionName", params.get("positionName"));
        queryWrapper.eq(params.containsKey("positionTypeId"), "positionTypeId", params.get("positionTypeId"));
        queryWrapper.like(params.containsKey("positionTypeName"), "positionTypeName", params.get("positionTypeName"));
        queryWrapper.eq(params.containsKey("departComId"), "departComId", params.get("departComId"));
        queryWrapper.eq(params.containsKey("companyId"), "companyId", params.get("companyId"));
        queryWrapper.eq(params.containsKey("companyName"), "companyName", params.get("companyName"));
        queryWrapper.eq(params.containsKey("companyShortName"), "companyShortName", params.get("companyShortName"));
        if (params.containsKey("internalCompany")) {
            queryWrapper.eq("internalCompany", params.get("internalCompany"));
        }
//        queryWrapper.like(params.containsKey("internalCompany"), "internalCompany", params.get("internalCompany"));
        queryWrapper.eq(params.containsKey("departmentId"), "departmentId", params.get("departmentId"));
        queryWrapper.like(params.containsKey("departmentName"), "departmentName", params.get("departmentName"));
        queryWrapper.like(params.containsKey("sort"), "sort", params.get("sort"));
        return queryWrapper;
    }

    @Override
    public boolean insertPositionDepartmentCompany(PositionDepartmentCompany positionDepartmentCompany, HttpServletRequest request) {
        //从前端获得四个表的ID
        Long companyId= Long.valueOf(request.getParameter("companyId"));
        Long departmentId= Long.valueOf(request.getParameter("departmentId"));
        Long userId= Long.valueOf(request.getParameter("userId"));
        Long roleId= Long.valueOf(request.getParameter("positionId"));

        Company company = companyMapper.selectById(companyId);
        positionDepartmentCompany.setCompanyName(company.getName());
        positionDepartmentCompany.setCompanyShortName(company.getShortName());
        Department department = departmentMapper.selectById(departmentId);
        positionDepartmentCompany.setDepartmentName(department.getName());
        User user = userMapper.selectById(userId);
        positionDepartmentCompany.setUserName(user.getName());
        Role role = roleMapper.selectById(roleId);
        positionDepartmentCompany.setPositionName(role.getName());

        int insert = positionDepartmentCompanyMapper.insert(positionDepartmentCompany);
        return insert > 0;
    }
}
