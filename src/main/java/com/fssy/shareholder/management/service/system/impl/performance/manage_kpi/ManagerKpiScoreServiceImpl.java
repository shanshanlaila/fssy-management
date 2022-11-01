package com.fssy.shareholder.management.service.system.impl.performance.manage_kpi;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.pojo.system.performance.manage_kpi.ManagerKpiScore;
import com.fssy.shareholder.management.mapper.system.performance.manage_kpi.ManagerKpiScoreMapper;
import com.fssy.shareholder.management.service.system.performance.manage_kpi.ManagerKpiScoreService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * <p>
 * 经理人绩效分数表 服务实现类
 * </p>
 *
 * @author Shizn
 * @since 2022-10-31
 */
@Service
public class ManagerKpiScoreServiceImpl extends ServiceImpl<ManagerKpiScoreMapper, ManagerKpiScore> implements ManagerKpiScoreService {
    @Autowired
    private ManagerKpiScoreMapper managerKpiScoreMapper;
    @Override
    public Page<ManagerKpiScore> findManagerKpiScoreDataListPerPageByParams(Map<String, Object> params) {
        QueryWrapper<ManagerKpiScore> queryWrapper = getQueryWrapper(params);
        int limit = (int) params.get("limit");
        int page = (int) params.get("page");
        Page<ManagerKpiScore> myPage = new Page<>(page, limit);
        return managerKpiScoreMapper.selectPage(myPage, queryWrapper);
    }
    private QueryWrapper<ManagerKpiScore> getQueryWrapper(Map<String, Object> params) {
        QueryWrapper<ManagerKpiScore> queryWrapper = new QueryWrapper<>();
        if (params.containsKey("id")) {
            queryWrapper.eq("id", params.get("id"));
        }
        if (params.containsKey("select")) {
            queryWrapper.select((String) params.get("select"));
        }
        if (params.containsKey("managerName")) {
            queryWrapper.like("managerName", params.get("managerName"));
        }
        if (params.containsKey("companyName")) {
            queryWrapper.like("companyName", params.get("companyName"));
        }
        if (params.containsKey("year")) {
            queryWrapper.eq("year", params.get("year"));
        }
        if (params.containsKey("month")) {
            queryWrapper.eq("month", params.get("month"));
        }
        if (params.containsKey("position")) {
            queryWrapper.like("position", params.get("position"));
        }
        if (params.containsKey("generalManager")) {
            queryWrapper.eq("generalManager", params.get("generalManager"));
        }
        return queryWrapper;
    }
}
