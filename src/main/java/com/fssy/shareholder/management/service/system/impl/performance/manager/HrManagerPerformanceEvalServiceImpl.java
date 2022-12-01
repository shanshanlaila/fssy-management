package com.fssy.shareholder.management.service.system.impl.performance.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.mapper.system.hr.performance.manage_kpi.ManagerKpiScoreMapper;
import com.fssy.shareholder.management.mapper.system.hr.performance.manager.ManagerPerformanceEvalStdMapper;
import com.fssy.shareholder.management.mapper.system.hr.performance.manager.ManagerQualitativeEvalMapper;
import com.fssy.shareholder.management.pojo.system.hr.performance.manage_kpi.ManagerKpiScore;
import com.fssy.shareholder.management.pojo.system.hr.performance.manager.*;
import com.fssy.shareholder.management.mapper.system.hr.performance.manager.HrManagerPerformanceEvalMapper;
import com.fssy.shareholder.management.service.system.hr.performance.manager.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fssy.shareholder.management.tools.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public List<HrManagerPerformanceEval> findHrManagerPerformanceEvalDataByParams(Map<String, Object> params) {
        return null;
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
            String managerType = hrUserPosition.getManagerType();
            Integer positionId = hrUserPosition.getPositionId();
            String positionName = hrUserPosition.getPositionName();
            Integer companyId = hrUserPosition.getCompanyId();
            String companyName = hrUserPosition.getCompanyName();
            String companyShortName = hrUserPosition.getCompanyShortName();

            temp.put("userPositionId",id);
            temp.put("managerId",userId);
            temp.put("companyId",companyId);
            temp.put("companyName",companyName);
            temp.put("managerName",userName);
            temp.put("position",positionName);
            temp.put("companyNameShort",companyShortName);

            //获取定量评价和定性评价年度占比
            QueryWrapper<ManagerPerformanceEvalStd> managerPerformanceEvalStdQueryWrapper = new QueryWrapper<>();
            managerPerformanceEvalStdQueryWrapper.eq("status", "生效").eq("year",year).orderByDesc("id").last("limit 1");
            List<ManagerPerformanceEvalStd> evalStdList = managerPerformanceEvalStdMapper.selectList(managerPerformanceEvalStdQueryWrapper);
            for (ManagerPerformanceEvalStd managerPerformanceEvalStd: evalStdList) {
                Integer performanceEvalStdId = managerPerformanceEvalStd.getId();
                BigDecimal kpiScoreR = managerPerformanceEvalStd.getKpiScoreR();
                Double qualitativeScoreR = managerPerformanceEvalStd.getQualitativeScoreR();

                temp.put("evalStdId",performanceEvalStdId);
                temp.put("kpiScoreR",kpiScoreR);
                temp.put("qualitativeScoreR",qualitativeScoreR);
            }

            //经理人KPI表查询
            QueryWrapper<ManagerKpiScore> managerKpiScoreQueryWrapper = new QueryWrapper<>();
            managerKpiScoreQueryWrapper.eq("year",year).eq("month",month)
                    .eq("companyName",companyName).eq("managerName",userName);
            List<ManagerKpiScore> kpiScoreList = managerKpiScoreMapper.selectList(managerKpiScoreQueryWrapper);
            for (ManagerKpiScore managerKpiScore:kpiScoreList) {
                //经理人KPI分数表获取数据
                Integer kpiScoreId = managerKpiScore.getId();
                BigDecimal scoreAdjust = managerKpiScore.getScoreAdjust();
                temp.put("kpiScoreId",kpiScoreId);
                temp.put("kpiScore",scoreAdjust);
            }
            //定性评价分数表查询
            QueryWrapper<ManagerQualitativeEval> performanceEvalQueryWrapper = new QueryWrapper<>();
            performanceEvalQueryWrapper.eq("year",year).eq("managerName",userName)
                    .eq("companyName",companyName).eq("position",positionName);
            List<ManagerQualitativeEval> performanceEvalList = managerQualitativeEvalMapper.selectList(performanceEvalQueryWrapper);
            for (ManagerQualitativeEval managerQualitativeEval:performanceEvalList) {
              //定性评价经理人分数表获取数据
                Integer qualitativeEvalId = managerQualitativeEval.getId();
                Double qualitativeEvalScoreAdjust = managerQualitativeEval.getQualitativeEvalScoreAdjust();
                String status = managerQualitativeEval.getStatus();
                temp.put("qualitativeEvalId",qualitativeEvalId);
                temp.put("qualitativeScore",qualitativeEvalScoreAdjust);
                temp.put("status",status);
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
            //判断是否生成分数
            QueryWrapper<HrManagerPerformanceEval> hrManagerPerformanceEvalQueryWrapper = new QueryWrapper<>();
            hrManagerPerformanceEvalQueryWrapper.eq("kpiScoreId",kpiScoreId).eq("evalStdId",evalStdId)
                    .eq("qualitativeEvalId",qualitativeEvalId).eq("userPositionId",userPositionId);
            List<HrManagerPerformanceEval> hrManagerPerformanceEvalList = hrManagerPerformanceEvalMapper.selectList(hrManagerPerformanceEvalQueryWrapper);
            if(hrManagerPerformanceEvalList.size()>1){
                throw new ServiceException("分数生成失败，数据错误");
            }
            if (hrManagerPerformanceEvalList.size()==1){
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
            hrManagerPerformanceEval.setQualitativeScoreR(qualitativeScoreR);

            hrManagerPerformanceEval.setCompanyNameShort(companyNameShort);
            hrManagerPerformanceEval.setYear(Integer.parseInt(year));
            hrManagerPerformanceEval.setKpiScoreMonth(Integer.parseInt(month));
            //分数计算
            hrManagerPerformanceEval.setKpiScore(kpiScore.multiply(kpiScoreR));  //KPI绩效分数的计算（定量评价）
            hrManagerPerformanceEval.setQualitativeScore(qualitativeScore*qualitativeScoreR);//定性评价的计算
            BigDecimal qualitativeScoreA = new BigDecimal(qualitativeScore);
            BigDecimal qualitativeScoreRA = new BigDecimal(qualitativeScoreR);
            BigDecimal qualitativeScoreAuto=qualitativeScoreA.multiply(qualitativeScoreRA);
            hrManagerPerformanceEval.setScoreAuto(kpiScore.multiply(kpiScoreR).add(qualitativeScoreAuto));//综合绩效（系统生成）
            hrManagerPerformanceEval.setScoreAdjust(kpiScore.multiply(kpiScoreR).add(qualitativeScoreAuto));//综合绩效（人工调整（默认））

            saveOrUpdate(hrManagerPerformanceEval);//根据id进行更新或添加
        }
        return true;
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
        if (params.containsKey("managerType")) {
            queryWrapper.eq("managerType", params.get("managerType"));
        }
        if (params.containsKey("status")) {
            queryWrapper.eq("status", params.get("status"));
        }
        return queryWrapper;
    }
}
