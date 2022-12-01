package com.fssy.shareholder.management.service.system.impl.performance.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.mapper.system.hr.performance.manager.HrUserPositionDepartmentCompanyMapper;
import com.fssy.shareholder.management.pojo.system.hr.performance.manager.ViewHrKpiScoreQualitative;
import com.fssy.shareholder.management.mapper.system.hr.performance.manager.ViewHrKpiScoreQualitativeMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fssy.shareholder.management.service.system.hr.performance.manager.ViewHrKpiScoreQualitativeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * <p>
 * 经理人定性评价汇总表服务实现类
 * </p>
 *
 * @author Shizn
 * @since 2022-11-29
 */
@Service
public class ViewHrKpiScoreQualitativeServiceImpl extends ServiceImpl<ViewHrKpiScoreQualitativeMapper, ViewHrKpiScoreQualitative> implements ViewHrKpiScoreQualitativeService {

    @Autowired
    private ViewHrKpiScoreQualitativeMapper viewHrKpiScoreQualitativeMapper;
    @Autowired
    private HrUserPositionDepartmentCompanyMapper hrUserPositionDepartmentCompanyMapper;
    @Override
    public Page<ViewHrKpiScoreQualitative> findViewHrKpiScoreQualitativeDataListPerPageByParams(Map<String, Object> params) {
        QueryWrapper<ViewHrKpiScoreQualitative> queryWrapper = getQueryWrapper(params);
        int limit = (int) params.get("limit");
        int page = (int) params.get("page");
        Page<ViewHrKpiScoreQualitative> myPage = new Page<>(page, limit);
        return viewHrKpiScoreQualitativeMapper.selectPage(myPage, queryWrapper);
    }
    private QueryWrapper<ViewHrKpiScoreQualitative> getQueryWrapper(Map<String, Object> params) {
        QueryWrapper<ViewHrKpiScoreQualitative> queryWrapper = new QueryWrapper<>();
        if (params.containsKey("id")) {
            queryWrapper.eq("id", params.get("id"));
        }
        if (params.containsKey("select")) {
            queryWrapper.select((String) params.get("select"));
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
        if (params.containsKey("year")) {
            queryWrapper.eq("year", params.get("year"));
        }
        if (params.containsKey("month")) {
            queryWrapper.eq("month", params.get("month"));
        }
        if (params.containsKey("status")) {
            queryWrapper.eq("status", params.get("status"));
        }
        return queryWrapper;
    }
}
