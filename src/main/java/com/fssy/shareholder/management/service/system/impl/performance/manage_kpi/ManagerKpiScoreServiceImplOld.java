package com.fssy.shareholder.management.service.system.impl.performance.manage_kpi;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.pojo.system.performance.manage_kpi.ManagerKpiScoreOld;
import com.fssy.shareholder.management.mapper.system.performance.manage_kpi.ManagerKpiScoreMapperOld;
import com.fssy.shareholder.management.service.system.performance.manage_kpi.ManagerKpiScoreServiceOld;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * <p>
 * 经理人绩效分数表		【修改内容】增加了年度关联ID字段	【修改时间】2022-10-27	【修改人】张泽鹏 服务实现类
 * </p>
 *
 * @author zzp
 * @since 2022-10-31
 */
@Service
public class ManagerKpiScoreServiceImplOld extends ServiceImpl<ManagerKpiScoreMapperOld, ManagerKpiScoreOld> implements ManagerKpiScoreServiceOld {

    @Autowired
    private ManagerKpiScoreMapperOld managerKpiScoreMapper;

    @Override
    public Page<ManagerKpiScoreOld> findViewManagerKpiMonthDataListPerPageByParams(Map<String, Object> params) {
        QueryWrapper<ManagerKpiScoreOld> queryWrapper = getQueryWrapper(params);
        int limit = (int)params.get("limit");
        int page = (int)params.get("page");
        Page<ManagerKpiScoreOld> myPage = new Page<>(page,limit);
        return managerKpiScoreMapper.selectPage(myPage, queryWrapper);
    }
    /**
     * 查询条件
     * @param params
     * @return
     */
    private QueryWrapper<ManagerKpiScoreOld> getQueryWrapper(Map<String, Object> params) {
        QueryWrapper<ManagerKpiScoreOld> queryWrapper = new QueryWrapper<>();
        if (params.containsKey("select")) {
            queryWrapper.select((String) params.get("select"));
        }
        if (params.containsKey("managerName")) {
            queryWrapper.eq("managerName", params.get("managerName"));
        }
        if (params.containsKey("companyName")) {
            queryWrapper.like("companyName", params.get("companyName"));
        }
        if (params.containsKey("generalManager")) {
            queryWrapper.like("generalManager", params.get("generalManager"));
        }
        if (params.containsKey("year")) {
            queryWrapper.eq("year", params.get("year"));
        }
        if (params.containsKey("month")) {
            queryWrapper.eq("month", params.get("month"));
        }
        return queryWrapper;
    }
}
