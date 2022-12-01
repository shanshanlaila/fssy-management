package com.fssy.shareholder.management.service.system.hr.performance.manager;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.pojo.system.hr.performance.manager.HrUserPositionDepartmentCompany;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
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

    /**
     * 查询指定的数据
     * @return
     */
    List<HrUserPositionDepartmentCompany> findHrUserPositionDepartmentCompany();


}
