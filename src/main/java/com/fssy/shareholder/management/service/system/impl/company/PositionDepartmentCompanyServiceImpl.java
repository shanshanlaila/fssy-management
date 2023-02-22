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
        queryWrapper.eq(params.containsKey("departmentId"), "departmentId", params.get("departmentId"));
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
