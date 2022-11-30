package com.fssy.shareholder.management.service.system.impl.performance.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.pojo.system.performance.manage_kpi.ManageKpiMonthAim;
import com.fssy.shareholder.management.pojo.system.performance.manager.HrUserPositionDepartmentCompany;
import com.fssy.shareholder.management.mapper.system.performance.manager.HrUserPositionDepartmentCompanyMapper;
import com.fssy.shareholder.management.service.system.performance.manager.HrUserPositionDepartmentCompanyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 任职履历表 服务实现类
 * </p>
 *
 * @author Shizn
 * @since 2022-11-29
 */
@Service
public class HrUserPositionDepartmentCompanyServiceImpl extends ServiceImpl<HrUserPositionDepartmentCompanyMapper, HrUserPositionDepartmentCompany> implements HrUserPositionDepartmentCompanyService {
    @Autowired
    private HrUserPositionDepartmentCompanyMapper hrUserPositionDepartmentCompanyMapper;

    @Override
    public Page<HrUserPositionDepartmentCompany> findHrUserPositionDepartmentCompanyDataListPerPageByParams(Map<String, Object> params) {
        QueryWrapper<HrUserPositionDepartmentCompany> queryWrapper = getQueryWrapper(params);
        queryWrapper.eq("status","在职").in("managerType","企业总经理","企业分管经理人");
        int limit = (int) params.get("limit");
        int page = (int) params.get("page");
        Page<HrUserPositionDepartmentCompany> myPage = new Page<>(page, limit);
        return hrUserPositionDepartmentCompanyMapper.selectPage(myPage, queryWrapper);
    }

    @Override
    public List<HrUserPositionDepartmentCompany> findHrUserPositionDepartmentCompany() {
        QueryWrapper<HrUserPositionDepartmentCompany> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status","在职").in("managerType","企业总经理","企业分管经理人");
        return hrUserPositionDepartmentCompanyMapper.selectList(queryWrapper);
    }

    private QueryWrapper<HrUserPositionDepartmentCompany> getQueryWrapper(Map<String, Object> params) {
        QueryWrapper<HrUserPositionDepartmentCompany> queryWrapper = new QueryWrapper<>();
        if (params.containsKey("id")) {
            queryWrapper.eq("id", params.get("id"));
        }
        if (params.containsKey("companyName")) {
            queryWrapper.like("companyName", params.get("companyName"));
        }
        if (params.containsKey("managerName")) {
            queryWrapper.like("managerName", params.get("managerName"));
        }
        if (params.containsKey("position")) {
            queryWrapper.eq("position", params.get("position"));
        }
        if (params.containsKey("status")) {
            queryWrapper.eq("status", params.get("status"));
        }
        if (params.containsKey("managerType")) {
            queryWrapper.eq("managerType", params.get("managerType"));
        }
        return queryWrapper;
    }
}
