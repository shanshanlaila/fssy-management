package com.fssy.shareholder.management.service.system.company;

import com.fssy.shareholder.management.pojo.system.company.PositionDepartmentCompany;
import com.fssy.shareholder.management.service.system.performance.employee.BaseService;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 基础-员工-职位-部门-公司表	 服务类
 * </p>
 *
 * @author 农浩
 * @since 2023-02-15
 */
public interface PositionDepartmentCompanyService extends BaseService<PositionDepartmentCompany> {
    /**
     * 新增
     * @param positionDepartmentCompany
     * @return
     */
    boolean insertPositionDepartmentCompany(PositionDepartmentCompany positionDepartmentCompany, HttpServletRequest request);
}
