package com.fssy.shareholder.management.service.system.impl.performance.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.mapper.manage.company.CompanyMapper;


import com.fssy.shareholder.management.mapper.system.performance.manager.ManagerQualitativeEvalMapper;


import com.fssy.shareholder.management.pojo.system.performance.manager.ManagerQualitativeEval;
import com.fssy.shareholder.management.pojo.system.performance.manager.ManagerQualitativeEvalStd;
import com.fssy.shareholder.management.mapper.system.performance.manager.ManagerQualitativeEvalStdMapper;
import com.fssy.shareholder.management.service.common.SheetService;

import com.fssy.shareholder.management.service.system.performance.manager.ManagerQualitativeEvalStdService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fssy.shareholder.management.tools.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * **数据表名：	bs_manager_qualitative_eval_std	**数据表中文名：	经理人绩效定性评分各项目占比表	**业务部门：	人力资源部	**数据表作用：	记录企业年度总经理、分管经理人定性项目占比表，因为该比例每年都可能变化 服务实现类
 * </p>
 *
 * @author Solomon
 * @since 2022-11-28
 */
@Service
public class ManagerQualitativeEvalStdServiceImpl extends ServiceImpl<ManagerQualitativeEvalStdMapper, ManagerQualitativeEvalStd> implements ManagerQualitativeEvalStdService {

    @Autowired
    private SheetService sheetService;

    @Autowired
    private ManagerQualitativeEvalStdMapper managerQualitativeEvalStdMapper;

    @Autowired
    private CompanyMapper companyMapper;



    @Autowired
    private ManagerQualitativeEvalMapper managerQualitativeEvalMapper;


    /**
     * 分页查询经 理人绩效定性评分各项目占比信息
     */
    @Override
    public Page<ManagerQualitativeEvalStd> findManagerQualitativeEvalStdDataListPerPageByParams(Map<String, Object> params) {
        QueryWrapper<ManagerQualitativeEvalStd> queryWrapper = getQueryWrapper(params);
        int limit = (int) params.get("limit");
        int page = (int) params.get("page");
        Page<ManagerQualitativeEvalStd> myPage = new Page<>(page, limit);
        return managerQualitativeEvalStdMapper.selectPage(myPage, queryWrapper);

    }

    /**
     * 查询所有经理人绩效定性评分各项目占比信息
     * @param params
     * @return
     */
    @Override
    public List<ManagerQualitativeEvalStd> findManagerQualitativeEvalStdDataByParams(Map<String, Object> params) {
        QueryWrapper<ManagerQualitativeEvalStd> queryWrapper = getQueryWrapper(params);
        List<ManagerQualitativeEvalStd> managerQualitativeEvalStds = managerQualitativeEvalStdMapper.selectList(queryWrapper);

        return managerQualitativeEvalStds;
    }

    /**
     * 通过主键删除经理人绩效定性评分各项目占比信息
     * @param id
     * @return
     */
    @Override
    public boolean deleteManagerQualitativeEvalStdDataById(Integer id,Map<String, Object> params) {
        QueryWrapper<ManagerQualitativeEval> queryWrapper = managerQualitativeEvalQueryWrapper(params);
        //获取数据库中经理人绩效定性评价分数所有数据
        List<ManagerQualitativeEval> managerQualitativeEvals = managerQualitativeEvalMapper.selectList(queryWrapper);
        List<ManagerQualitativeEval> filterList = managerQualitativeEvals.stream()
                .filter(i -> i.getEvalStdId().equals(id)).collect(Collectors.toList());
        //拿出数据库中经理人绩效定性评价分数evalStdId与id进行比较
        for (ManagerQualitativeEval qualitativeEval : filterList) {
            Integer evalStdId = qualitativeEval.getEvalStdId();
            if (evalStdId==id)
                throw new ServiceException("此记录正在使用");
        }
        int result = managerQualitativeEvalStdMapper.deleteById(id);
        if (result > 0) {
            return true;
        }
        return false;
    }

    /**
     * 修改经理人绩效定性评分各项目占比表
     * @param managerQualitativeEvalStd
     * @return
     */
    @Override
    public boolean updateManagerQualitativeEvalStdData(ManagerQualitativeEvalStd managerQualitativeEvalStd,Map<String, Object> params) {
        Double skillScoreR = managerQualitativeEvalStd.getSkillScoreR();
        Double democraticEvalScoreR = managerQualitativeEvalStd.getDemocraticEvalScoreR();
        Double superiorEvalScoreR = managerQualitativeEvalStd.getSuperiorEvalScoreR();
        double vgm = skillScoreR + democraticEvalScoreR + superiorEvalScoreR;
        if (vgm!=1){
            return false;
        }
        Double auditEvalScoreR = managerQualitativeEvalStd.getAuditEvalScoreR();
        Double financialAuditScoreR = managerQualitativeEvalStd.getFinancialAuditScoreR();
        Double operationScoreR = managerQualitativeEvalStd.getOperationScoreR();
        Double leadershipScoreR = managerQualitativeEvalStd.getLeadershipScoreR();
        Double investScoreR = managerQualitativeEvalStd.getInvestScoreR();
        Double workReportScoreR = managerQualitativeEvalStd.getWorkReportScoreR();
        double gm = auditEvalScoreR + financialAuditScoreR + operationScoreR + leadershipScoreR + investScoreR + workReportScoreR;
        if (gm!=1){
            return false;
        }
        Integer id = managerQualitativeEvalStd.getId();
        //1.2   //查询出比例表中的相关的getEvalStdId所有记录,存入filterListStd
        QueryWrapper<ManagerQualitativeEval> queryWrapper = managerQualitativeEvalQueryWrapper(params);

        List<ManagerQualitativeEval> managerQualitativeEvals = managerQualitativeEvalMapper.selectList(queryWrapper);
        List<ManagerQualitativeEval> filterList = managerQualitativeEvals.stream()
                .filter(i -> i.getEvalStdId().equals(id)).collect(Collectors.toList());
        //拿出数据库中经理人绩效定性评价分数evalStdId与id进行比较
        for (ManagerQualitativeEval qualitativeEval : filterList) {
            Integer evalStdId = qualitativeEval.getEvalStdId();
            if (evalStdId==id)
                throw new ServiceException("此记录正在使用");
        }


        int result = managerQualitativeEvalStdMapper.updateById(managerQualitativeEvalStd);
        if (result > 0) {
            return true;
        }
        return false;
    }

    /**
     * 添加经理人绩效定性评分各项目占比信息
     * @param managerQualitativeEvalStd
     * @return
     */
    @Override
    public boolean insertManagerQualitativeEvalStd(ManagerQualitativeEvalStd managerQualitativeEvalStd) {
        Double skillScoreR = managerQualitativeEvalStd.getSkillScoreR();
        Double democraticEvalScoreR = managerQualitativeEvalStd.getDemocraticEvalScoreR();
        Double superiorEvalScoreR = managerQualitativeEvalStd.getSuperiorEvalScoreR();
        double vgm = skillScoreR + democraticEvalScoreR + superiorEvalScoreR;
        if (vgm!=100){
            return false;
        }
        Double auditEvalScoreR = managerQualitativeEvalStd.getAuditEvalScoreR();
        Double financialAuditScoreR = managerQualitativeEvalStd.getFinancialAuditScoreR();
        Double operationScoreR = managerQualitativeEvalStd.getOperationScoreR();
        Double leadershipScoreR = managerQualitativeEvalStd.getLeadershipScoreR();
        Double investScoreR = managerQualitativeEvalStd.getInvestScoreR();
        Double workReportScoreR = managerQualitativeEvalStd.getWorkReportScoreR();
        double gm = auditEvalScoreR + financialAuditScoreR + operationScoreR + leadershipScoreR + investScoreR + workReportScoreR;
        if (gm!=100){
            return false;
        }
        int result = managerQualitativeEvalStdMapper.insert(managerQualitativeEvalStd);
        if (result > 0) {
            return true;
        }
        return false;
    }


    private QueryWrapper<ManagerQualitativeEvalStd> getQueryWrapper(Map<String, Object> params){
        QueryWrapper<ManagerQualitativeEvalStd> queryWrapper = new QueryWrapper<>();
        if (params.containsKey("year")) {
            queryWrapper.eq("year", params.get("year"));
        }
        if (params.containsKey("skillScoreR")) {
            queryWrapper.eq("skillScoreR", params.get("skillScoreR"));
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

    private QueryWrapper<ManagerQualitativeEval> managerQualitativeEvalQueryWrapper(Map<String, Object> params){
        QueryWrapper<ManagerQualitativeEval> queryWrapper = new QueryWrapper<>();
        if (params.containsKey("year")) {
            queryWrapper.eq("year", params.get("year"));
        }
        if (params.containsKey("skillScoreR")) {
            queryWrapper.eq("skillScoreR", params.get("skillScoreR"));
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
