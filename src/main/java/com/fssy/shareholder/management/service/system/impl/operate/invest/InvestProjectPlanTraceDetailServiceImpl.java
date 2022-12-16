package com.fssy.shareholder.management.service.system.impl.operate.invest;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fssy.shareholder.management.pojo.system.operate.invest.InvestProjectList;
import com.fssy.shareholder.management.pojo.system.operate.invest.InvestProjectPlanTraceDetail;
import com.fssy.shareholder.management.mapper.system.operate.invest.InvestProjectPlanTraceDetailMapper;
import com.fssy.shareholder.management.service.system.operate.invest.InvestProjectPlanTraceDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * **数据表名：	bs_operate_invest_project_plan_trace	**数据表中文名：	项目进度计划跟踪表	**业务部门：	经营管理部	**数据表作用：	用于记录单一项目进展详情及跟踪情况	**创建人创建日期：	TerryZeng 2022-12-2 服务实现类
 * </p>
 *
 * @author Solomon
 * @since 2022-12-13
 */
@Service
public class InvestProjectPlanTraceDetailServiceImpl extends ServiceImpl<InvestProjectPlanTraceDetailMapper, InvestProjectPlanTraceDetail> implements InvestProjectPlanTraceDetailService {
    @Autowired
    private InvestProjectPlanTraceDetailMapper investProjectPlanTraceDetailMapper;


    @Override
    public List<InvestProjectPlanTraceDetail> findInvestProjectPlanTraceDetailDataByParams(Map<String, Object> params) {
        QueryWrapper<InvestProjectPlanTraceDetail> queryWrapper = getQueryWrapper(params);
        return investProjectPlanTraceDetailMapper.selectList(queryWrapper);

    }

    @Override
    public List<Map<String, Object>> findInvestProjectDataByParams(Map<String, Object> params) {
        QueryWrapper<InvestProjectPlanTraceDetail> queryWrapper = getQueryWrapper(params);
        return investProjectPlanTraceDetailMapper.selectMaps(queryWrapper);

    }

    private QueryWrapper<InvestProjectPlanTraceDetail> getQueryWrapper(Map<String,Object> params){
        QueryWrapper<InvestProjectPlanTraceDetail> queryWrapper = new QueryWrapper<>();
        if (params.containsKey("select")) {
            queryWrapper.select((String) params.get("select"));
        }
        if (params.containsKey("id")) {
            queryWrapper.eq("id", params.get("id"));
        }
        if (params.containsKey("projectName")) {
            queryWrapper.like("projectName", params.get("projectName"));
        }
        if (params.containsKey("year")) {
            queryWrapper.eq("year", params.get("year"));
        }
        if (params.containsKey("companyName")) {
            queryWrapper.like("companyName", params.get("companyName"));
        }
        if (params.containsKey("projectListId")) {
            queryWrapper.eq("projectListId", params.get("projectListId"));
        }
        if (params.containsKey("departmentName")) {
            queryWrapper.eq("departmentName", params.get("departmentName"));
        }
        if (params.containsKey("month")) {
            queryWrapper.eq("month", params.get("month"));
        }
        if (params.containsKey("evaluate")) {
            queryWrapper.eq("evaluate", params.get("evaluate"));
        }
        if (params.containsKey("abstracte")) {
            queryWrapper.eq("abstracte", params.get("abstracte"));
        }

        return queryWrapper;
    }
}
