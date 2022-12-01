package com.fssy.shareholder.management.service.system.impl.performance.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.pojo.system.performance.manager.ManagerPerformanceEvalStd;
import com.fssy.shareholder.management.mapper.system.performance.manager.ManagerPerformanceEvalStdMapper;
import com.fssy.shareholder.management.service.system.performance.manager.ManagerPerformanceEvalStdService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * **数据表名：	bs_manager_performance_std	**数据表中文名：	经理人绩效评分定性、定量分数占比表	**业务部门：	人力资源部	**数据表作用：	记录企业年度总经理、分管经理人定性、定量分数占比表，因为该比例每年都可能变化	 服务实现类
 * </p>
 *
 * @author Solomon
 * @since 2022-11-29
 */
@Service
public class ManagerPerformanceEvalStdServiceImpl extends ServiceImpl<ManagerPerformanceEvalStdMapper, ManagerPerformanceEvalStd> implements ManagerPerformanceEvalStdService {

    @Autowired
    private ManagerPerformanceEvalStdMapper managerPerformanceEvalStdMapper;

    @Override
    public Page<ManagerPerformanceEvalStd> findManagerPerformanceEvalStdDataListPerPageByParams(Map<String, Object> params) {
        QueryWrapper<ManagerPerformanceEvalStd> queryWrapper = getQueryWrapper(params);
        int limit = (int) params.get("limit");
        int page = (int) params.get("page");
        Page<ManagerPerformanceEvalStd> myPage = new Page<>(page, limit);
        return managerPerformanceEvalStdMapper.selectPage(myPage, queryWrapper);

    }

    @Override
    public List<ManagerPerformanceEvalStd> findManagerPerformanceEvalStdDataByParams(Map<String, Object> params) {
        QueryWrapper<ManagerPerformanceEvalStd> queryWrapper = getQueryWrapper(params);
        List<ManagerPerformanceEvalStd> managerPerformanceEvalStds = managerPerformanceEvalStdMapper.selectList(queryWrapper);
        return managerPerformanceEvalStds;
    }

    @Override
    public boolean deleteManagerPerformanceEvalStdDataById(Integer id) {
        return false;
    }

    @Override
    public boolean updateManagerPerformanceEvalStdData(ManagerPerformanceEvalStd managerPerformanceEvalStd) {
        return false;
    }

    @Override
    public boolean insertManagerPerformanceEvalStd(ManagerPerformanceEvalStd managerPerformanceEvalStd) {
        return false;
    }

    private QueryWrapper<ManagerPerformanceEvalStd> getQueryWrapper(Map<String, Object> params){
        QueryWrapper<ManagerPerformanceEvalStd> queryWrapper = new QueryWrapper<>();
        if (params.containsKey("year")) {
            queryWrapper.eq("year", params.get("year"));
        }
        if (params.containsKey("id")) {
            queryWrapper.eq("id", params.get("id"));
        }
        if (params.containsKey("democraticEvalScoreR")) {
            queryWrapper.eq("democraticEvalScoreR", params.get("democraticEvalScoreR"));
        }
        if (params.containsKey("superiorEvalScoreR")) {
            queryWrapper.eq("superiorEvalScoreR", params.get("superiorEvalScoreR"));
        }
        if (params.containsKey("auditEvalScoreR")) {
            queryWrapper.eq("auditEvalScoreR", params.get("auditEvalScoreR"));
        }
        if (params.containsKey("financialAuditScoreR")) {
            queryWrapper.eq("financialAuditScoreR", params.get("financialAuditScoreR"));
        }
        if (params.containsKey("operationScoreR")) {
            queryWrapper.eq("operationScoreR", params.get("operationScoreR"));
        }
        if (params.containsKey("leadershipScoreR")) {
            queryWrapper.eq("leadershipScoreR", params.get("leadershipScoreR"));
        }
        if (params.containsKey("investScoreR")) {
            queryWrapper.eq("investScoreR", params.get("investScoreR"));
        }
        if (params.containsKey("workReportScoreR")) {
            queryWrapper.eq("workReportScoreR", params.get("workReportScoreR"));
        }

        return queryWrapper;
    }
}
