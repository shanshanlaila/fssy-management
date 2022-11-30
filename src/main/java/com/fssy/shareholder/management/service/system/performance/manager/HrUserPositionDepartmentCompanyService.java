package com.fssy.shareholder.management.service.system.performance.manager;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.pojo.system.performance.manage_kpi.ManageKpiMonthAim;
import com.fssy.shareholder.management.pojo.system.performance.manager.HrUserPositionDepartmentCompany;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * ：任职履历表 服务类
 * </p>
 *
 * @author Shizn
 * @since 2022-11-29
 */
public interface HrUserPositionDepartmentCompanyService extends IService<HrUserPositionDepartmentCompany> {
    /**
     * 通过查询条件 分页查询列表
     *
     * @param params 查询条件
     * @return 分页数据
     */
    Page<HrUserPositionDepartmentCompany> findHrUserPositionDepartmentCompanyDataListPerPageByParams(Map<String, Object> params);
}
