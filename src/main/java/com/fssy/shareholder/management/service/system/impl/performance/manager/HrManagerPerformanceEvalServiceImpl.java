package com.fssy.shareholder.management.service.system.impl.performance.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.mapper.system.performance.manage_kpi.ManagerKpiScoreMapper;
import com.fssy.shareholder.management.mapper.system.performance.manager.ManagerPerformanceEvalStdMapper;
import com.fssy.shareholder.management.mapper.system.performance.manager.ManagerQualitativeEvalMapper;
import com.fssy.shareholder.management.pojo.system.performance.manage_kpi.ManageKpiLib;
import com.fssy.shareholder.management.pojo.system.performance.manage_kpi.ManagerKpiScore;
import com.fssy.shareholder.management.pojo.system.performance.manager.*;
import com.fssy.shareholder.management.mapper.system.performance.manager.HrManagerPerformanceEvalMapper;
import com.fssy.shareholder.management.service.system.performance.manager.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fssy.shareholder.management.tools.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 经理人绩效汇总评分表  服务实现类
 * </p>
 *
 * @author Shizn
 * @since 2022-11-30
 */
@Service
public class HrManagerPerformanceEvalServiceImpl extends ServiceImpl<HrManagerPerformanceEvalMapper, HrManagerPerformanceEval> implements HrManagerPerformanceEvalService {

    @Autowired
    private HrManagerPerformanceEvalMapper hrManagerPerformanceEvalMapper;
    @Autowired
    private HrUserPositionDepartmentCompanyService hrUserPositionDepartmentCompanyService;
    @Autowired
    private ManagerPerformanceEvalStdMapper managerPerformanceEvalStdMapper;
    @Autowired
    private ManagerQualitativeEvalMapper managerQualitativeEvalMapper;
    @Autowired
    private ManagerKpiScoreMapper managerKpiScoreMapper;

    @Override
    public List<Map<String, Object>> findHrManagerPerformanceEvalDataByParams(Map<String, Object> params) {
        QueryWrapper<HrManagerPerformanceEval> queryWrapper = getQueryWrapper(params);
        return hrManagerPerformanceEvalMapper.selectMaps(queryWrapper);
    }

    @Override
    public List<HrManagerPerformanceEval> findHrHrManagerPerformanceEvalDataByParams(Map<String, Object> params) {
        QueryWrapper<HrManagerPerformanceEval> queryWrapper = getQueryWrapper(params);
        return hrManagerPerformanceEvalMapper.selectList(queryWrapper);
    }


    @Override
    public Page<HrManagerPerformanceEval> findHrManagerPerformanceEvalDataListPerPageByParams(Map<String, Object> params) {
        QueryWrapper<HrManagerPerformanceEval> queryWrapper = getQueryWrapper(params);
        int limit = (int) params.get("limit");
        int page = (int) params.get("page");
        Page<HrManagerPerformanceEval> myPage = new Page<>(page, limit);
        return hrManagerPerformanceEvalMapper.selectPage(myPage, queryWrapper);
    }

    @Override
    public boolean updateScore(String year, String month) {
        Map<String, Object> temp = new HashMap<>();

        //查询出履历表中在职，且managerType=企业总经理、企业分管经理的人
        List<HrUserPositionDepartmentCompany> params = hrUserPositionDepartmentCompanyService.findHrUserPositionDepartmentCompany();
        for (HrUserPositionDepartmentCompany hrUserPosition : params) {
            //履职表获取数据
            Integer id = hrUserPosition.getId();
            Integer userId = hrUserPosition.getUserId();
            String userName = hrUserPosition.getUserName();
            String positionName = hrUserPosition.getPositionName();
            Integer companyId = hrUserPosition.getCompanyId();
            String companyName = hrUserPosition.getCompanyName();
            String companyShortName = hrUserPosition.getCompanyShortName();
            String statusPosition = hrUserPosition.getStatus();

            temp.put("userPositionId", id);
            temp.put("managerId", userId);
            temp.put("companyId", companyId);
            temp.put("companyName", companyName);
            temp.put("managerName", userName);
            temp.put("position", positionName);
            temp.put("companyNameShort", companyShortName);
            temp.put("status", statusPosition);

            //获取定量评价和定性评价年度占比
            QueryWrapper<ManagerPerformanceEvalStd> managerPerformanceEvalStdQueryWrapper = new QueryWrapper<>();
            managerPerformanceEvalStdQueryWrapper.eq("status", "生效").eq("year", year).orderByDesc("id").last("limit 1");
            List<ManagerPerformanceEvalStd> evalStdList = managerPerformanceEvalStdMapper.selectList(managerPerformanceEvalStdQueryWrapper);
            for (ManagerPerformanceEvalStd managerPerformanceEvalStd : evalStdList) {
                Integer performanceEvalStdId = managerPerformanceEvalStd.getId();
                BigDecimal kpiScoreR = managerPerformanceEvalStd.getKpiScoreR();
                Double qualitativeScoreR = managerPerformanceEvalStd.getQualitativeScoreR();

                temp.put("evalStdId", performanceEvalStdId);
                temp.put("kpiScoreR", kpiScoreR);
                temp.put("qualitativeScoreR", qualitativeScoreR);
            }

            //经理人KPI表查询
            QueryWrapper<ManagerKpiScore> managerKpiScoreQueryWrapper = new QueryWrapper<>();
            managerKpiScoreQueryWrapper.eq("year", year).eq("companyName", companyName).
                    eq("managerName", userName).eq("month", month);
            List<ManagerKpiScore> kpiScoreList = managerKpiScoreMapper.selectList(managerKpiScoreQueryWrapper);
            for (ManagerKpiScore managerKpiScore : kpiScoreList) {
                //经理人KPI分数表获取数据
                Integer kpiScoreId = managerKpiScore.getId();
                BigDecimal scoreAdjust = managerKpiScore.getScoreAdjust();
                temp.put("kpiScoreId", kpiScoreId);
                temp.put("kpiScore", scoreAdjust);
                temp.put("month", month);
            }
            //定性评价分数表查询
            QueryWrapper<ManagerQualitativeEval> performanceEvalQueryWrapper = new QueryWrapper<>();
            performanceEvalQueryWrapper.eq("year", year).eq("managerName", userName).eq("position", positionName);
            List<ManagerQualitativeEval> performanceEvalList = managerQualitativeEvalMapper.selectList(performanceEvalQueryWrapper);
            for (ManagerQualitativeEval managerQualitativeEval : performanceEvalList) {
                //定性评价经理人分数表获取数据
                Integer qualitativeEvalId = managerQualitativeEval.getId();
                Double qualitativeEvalScoreAdjust = managerQualitativeEval.getQualitativeEvalScoreAdjust();
                temp.put("qualitativeEvalId", qualitativeEvalId);
                temp.put("qualitativeScore", qualitativeEvalScoreAdjust);
            }
            //写入经理人绩效汇总表实体类
            HrManagerPerformanceEval hrManagerPerformanceEval = new HrManagerPerformanceEval();
            //获取map中的值
            String status = (String) temp.get("status");
            String managerName = (String) temp.get("managerName");
            Integer managerId = (Integer) temp.get("managerId");
            String companyNameEval = (String) temp.get("companyName");
            Integer companyIdEval = (Integer) temp.get("companyId");
            String position = (String) temp.get("position");
            String companyNameShort = (String) temp.get("companyNameShort");
            BigDecimal kpiScore = (BigDecimal) temp.get("kpiScore");
            Double qualitativeScore = (Double) temp.get("qualitativeScore");
            Integer kpiScoreId = (Integer) temp.get("kpiScoreId");
            Integer qualitativeEvalId = (Integer) temp.get("qualitativeEvalId");
            Integer evalStdId = (Integer) temp.get("evalStdId");
            Integer userPositionId = (Integer) temp.get("userPositionId");
            BigDecimal kpiScoreR = (BigDecimal) temp.get("kpiScoreR");
            Double qualitativeScoreR = (Double) temp.get("qualitativeScoreR");
            //判断年度占比是否为空，为空则计算失败
            if (ObjectUtils.isEmpty(kpiScoreR)) {
                return false;
            }
            if (ObjectUtils.isEmpty(kpiScoreR)) {
                return false;
            }
            //判断是否生成分数
            QueryWrapper<HrManagerPerformanceEval> hrManagerPerformanceEvalQueryWrapper = new QueryWrapper<>();
            hrManagerPerformanceEvalQueryWrapper.eq("kpiScoreId", kpiScoreId).eq("evalStdId", evalStdId)
                    .eq("qualitativeEvalId", qualitativeEvalId).eq("userPositionId", userPositionId);
            List<HrManagerPerformanceEval> hrManagerPerformanceEvalList = hrManagerPerformanceEvalMapper.selectList(hrManagerPerformanceEvalQueryWrapper);
            if (hrManagerPerformanceEvalList.size() == 1) {
                hrManagerPerformanceEval.setId(hrManagerPerformanceEvalList.get(0).getId());
            }

            //写入
            hrManagerPerformanceEval.setStatus(status);
            hrManagerPerformanceEval.setManagerId(managerId);
            hrManagerPerformanceEval.setManagerName(managerName);
            hrManagerPerformanceEval.setCompanyName(companyNameEval);
            hrManagerPerformanceEval.setCompanyId(companyIdEval);
            hrManagerPerformanceEval.setPosition(position);
            hrManagerPerformanceEval.setKpiScoreId(kpiScoreId);
            hrManagerPerformanceEval.setQualitativeEvalId(qualitativeEvalId);
            hrManagerPerformanceEval.setEvalStdId(evalStdId);
            hrManagerPerformanceEval.setKpiScoreR(kpiScoreR);
            hrManagerPerformanceEval.setUserPositionId(userPositionId);
            hrManagerPerformanceEval.setQualitativeScoreR(qualitativeScoreR);

            hrManagerPerformanceEval.setCompanyNameShort(companyNameShort);
            hrManagerPerformanceEval.setYear(Integer.parseInt(year));
            hrManagerPerformanceEval.setKpiScoreMonth(Integer.valueOf(month));
            //分数计算
            //判断分数是否为空，不为空继续计算，为空不写入表中
            BigDecimal qualitativeScoreAuto =null;
            if (!ObjectUtils.isEmpty(kpiScore)) {
                hrManagerPerformanceEval.setKpiScore(kpiScore.multiply(kpiScoreR));  //KPI绩效分数的计算（定量评价）
            }
            if (!ObjectUtils.isEmpty(qualitativeScore)){
                hrManagerPerformanceEval.setQualitativeScore(qualitativeScore * qualitativeScoreR);//定性评价的计算
                BigDecimal score = new BigDecimal(qualitativeScore);
                BigDecimal proportion = new BigDecimal(qualitativeScoreR);
                qualitativeScoreAuto = score.multiply(proportion);
            }
            //判断分数是否为空，为空则不对这个分数进行计算，最终分数等于另一个分数或都为空
            if (!ObjectUtils.isEmpty(kpiScore) && !ObjectUtils.isEmpty(qualitativeScore)) {
                hrManagerPerformanceEval.setScoreAuto(kpiScore.multiply(kpiScoreR).add(qualitativeScoreAuto));//综合绩效（系统生成）
                hrManagerPerformanceEval.setScoreAdjust(kpiScore.multiply(kpiScoreR).add(qualitativeScoreAuto));//综合绩效（人工调整（默认））
            }
            if (ObjectUtils.isEmpty(kpiScore) && ObjectUtils.isEmpty(qualitativeScore)){
                hrManagerPerformanceEval.setScoreAuto(null);
                hrManagerPerformanceEval.setScoreAdjust(null);
            }else if (ObjectUtils.isEmpty(kpiScore)){
                hrManagerPerformanceEval.setScoreAuto(qualitativeScoreAuto);
                hrManagerPerformanceEval.setScoreAdjust(qualitativeScoreAuto);
            }else if (ObjectUtils.isEmpty(qualitativeScore)){
                hrManagerPerformanceEval.setScoreAuto(kpiScore.multiply(kpiScoreR));
                hrManagerPerformanceEval.setScoreAdjust(kpiScore.multiply(kpiScoreR));
            }
            saveOrUpdate(hrManagerPerformanceEval);//根据id进行更新或添加
        }
        return true;
    }

    @Override
    public boolean updateHrManagerPerformanceEval(HrManagerPerformanceEval hrManagerPerformanceEval) {
        int result = hrManagerPerformanceEvalMapper.updateById(hrManagerPerformanceEval);
        if (result > 0) {
            return true;
        }
        return false;
    }


    private QueryWrapper<HrManagerPerformanceEval> getQueryWrapper(Map<String, Object> params) {
        QueryWrapper<HrManagerPerformanceEval> queryWrapper = new QueryWrapper<>();
        if (params.containsKey("id")) {
            queryWrapper.eq("id", params.get("id"));
        }
        if (params.containsKey("select")) {
            queryWrapper.select((String) params.get("select"));
        }
        if (params.containsKey("companyName")) {
            queryWrapper.like("companyName", params.get("companyName"));
        }
        if (params.containsKey("companyNameShort")) {
            queryWrapper.like("companyNameShort", params.get("companyNameShort"));
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
        if (params.containsKey("kpiScoreMonth")) {
            queryWrapper.eq("kpiScoreMonth", params.get("kpiScoreMonth"));
        }
        if (params.containsKey("managerType")) {
            queryWrapper.eq("managerType", params.get("managerType"));
        }
        if (params.containsKey("status")) {
            queryWrapper.eq("status", params.get("status"));
        }
        return queryWrapper;
    }
}
