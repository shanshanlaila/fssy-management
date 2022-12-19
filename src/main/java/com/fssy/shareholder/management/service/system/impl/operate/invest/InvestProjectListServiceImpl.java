package com.fssy.shareholder.management.service.system.impl.operate.invest;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.mapper.system.operate.invest.InvestProjectPlanTraceDetailMapper;
import com.fssy.shareholder.management.mapper.system.operate.invest.InvestProjectPlanTraceMapper;
import com.fssy.shareholder.management.pojo.system.config.Attachment;
import com.fssy.shareholder.management.pojo.system.operate.invest.InvestProjectList;
import com.fssy.shareholder.management.mapper.system.operate.invest.InvestProjectListMapper;
import com.fssy.shareholder.management.pojo.system.operate.invest.InvestProjectPlanTrace;
import com.fssy.shareholder.management.pojo.system.operate.invest.InvestProjectPlanTraceDetail;
import com.fssy.shareholder.management.pojo.system.operate.invest.TechCapacityEvaluate;
import com.fssy.shareholder.management.service.common.SheetService;
import com.fssy.shareholder.management.service.system.operate.invest.InvestProjectListService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * **数据表名：	bs_operate_invest_project_list	**数据表中文名：	年度投资项目清单	**业务部门：	经营管理部	**数据表作用：	记录 企业年度投资项目清单	**创建人创建日期：	TerryZeng 2022-12-2 服务实现类
 * </p>
 *
 * @author Solomon
 * @since 2022-12-09
 */
@Service
public class InvestProjectListServiceImpl extends ServiceImpl<InvestProjectListMapper, InvestProjectList> implements InvestProjectListService {
    @Autowired
    private SheetService sheetService;

    @Autowired
    private InvestProjectListMapper investProjectListMapper;

    @Autowired
    private InvestProjectPlanTraceMapper investProjectPlanTraceMapper;

    @Autowired
    private InvestProjectPlanTraceDetailMapper investProjectPlanTraceDetailMapper;

    /**
     * 查询年度投资项目清单
     * @param params
     * @return
     */
    @Override
    public List<InvestProjectList> findInvestProjectListDataByParams(Map<String, Object> params) {
        QueryWrapper<InvestProjectList> queryWrapper = getQueryWrapper(params);
        return investProjectListMapper.selectList(queryWrapper);

    }

    /**
     * 分页查询年度投资项目清单
     * @param params
     * @return
     */
    @Override
    public Page<InvestProjectList> findInvestProjectListDataListPerPageByParams(Map<String, Object> params) {
        QueryWrapper<InvestProjectList> queryWrapper = getQueryWrapper(params);
        int limit = (int) params.get("limit");
        int page = (int) params.get("page");
        Page<InvestProjectList> myPage = new Page<>(page, limit);
        return investProjectListMapper.selectPage(myPage, queryWrapper);

    }

    /**
     * 条件查询年度投资项目清单
     * @param params
     * @return
     */
    @Override
    public List<Map<String, Object>> findInvestProjectDataByParams(Map<String, Object> params) {
        QueryWrapper<InvestProjectList> queryWrapper = getQueryWrapper(params);
        return investProjectListMapper.selectMaps(queryWrapper);

    }

    @Override
    public boolean deleteInvestProjectListDataById(Integer id) {

        QueryWrapper<InvestProjectPlanTrace> queryWrapper = new QueryWrapper<>();
        String projectListId = String.valueOf(id);
        queryWrapper.eq("projectListId",projectListId);
        investProjectPlanTraceMapper.delete(queryWrapper);

        QueryWrapper<InvestProjectPlanTraceDetail> queryWrapperTwo = new QueryWrapper<>();
        queryWrapperTwo.eq("projectListId",projectListId);
        investProjectPlanTraceDetailMapper.delete(queryWrapperTwo);


        int result = investProjectListMapper.deleteById(id);
        if (result > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean updateInvestProjectListData(InvestProjectList investProjectList) {
        int result = investProjectListMapper.updateById(investProjectList);
        if (result > 0) {
            return true;
        }
        return false;
    }

    @Override
    public Map<String, Object> reaadInvestProjectListDataSource(Attachment attachment, String year, String companyName) {
        return null;
    }



    private QueryWrapper<InvestProjectList> getQueryWrapper(Map<String,Object> params){
        QueryWrapper<InvestProjectList> queryWrapper = new QueryWrapper<>();
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

        if (params.containsKey("companyId")) {
            queryWrapper.eq("companyId", params.get("companyId"));
        }
        if (params.containsKey("companyShortName")) {
            queryWrapper.like("companyShortName", params.get("companyShortName"));
        }
        if (params.containsKey("projectSource")) {
            queryWrapper.like("projectSource", params.get("projectSource"));
        }
        if (params.containsKey("projectType")) {
            queryWrapper.like("projectType", params.get("projectType"));
        }
        if (params.containsKey("projectClass")) {
            queryWrapper.like("projectClass", params.get("projectClass"));
        }
        if (params.containsKey("projectAbstract")) {
            queryWrapper.like("projectAbstract", params.get("projectAbstract"));
        }
        if (params.containsKey("investmentVolumePlan")) {
            queryWrapper.like("investmentVolumePlan", params.get("investmentVolumePlan"));
        }
        if (params.containsKey("investmentVolumeActual")) {
            queryWrapper.like("investmentVolumeActual", params.get("investmentVolumeActual"));
        }
        if (params.containsKey("respManager")) {
            queryWrapper.like("respManager", params.get("respManager"));
        }
        if (params.containsKey("projectContact")) {
            queryWrapper.like("projectContact", params.get("projectContact"));
        }
        if (params.containsKey("projectSrartDatePlan")) {
            queryWrapper.like("projectSrartDatePlan", params.get("projectSrartDatePlan"));
        }
        if (params.containsKey("projectSrartDateActual")) {
            queryWrapper.like("projectSrartDateActual", params.get("projectSrartDateActual"));
        }
        if (params.containsKey("projectEndDatePlan")) {
            queryWrapper.like("projectEndDatePlan", params.get("projectEndDatePlan"));
        }
        if (params.containsKey("currentProjectStatus")) {
            queryWrapper.like("currentProjectStatus", params.get("currentProjectStatus"));
        }
        if (params.containsKey("projectReportDate")) {
            queryWrapper.like("projectReportDate", params.get("projectReportDate"));
        }
        if (params.containsKey("projectFeasibilityStudyReport")) {
            queryWrapper.like("projectFeasibilityStudyReport", params.get("projectFeasibilityStudyReport"));
        }
        if (params.containsKey("committeeAuditStatus")) {
            queryWrapper.like("committeeAuditStatus", params.get("committeeAuditStatus"));
        }
        if (params.containsKey("boardAuditStatus")) {
            queryWrapper.like("boardAuditStatus", params.get("boardAuditStatus"));
        }
        if (params.containsKey("projectFinishStatus")) {
            queryWrapper.like("projectFinishStatus", params.get("projectFinishStatus"));
        }
        if (params.containsKey("projectRiskStatus")) {
            queryWrapper.like("projectRiskStatus", params.get("projectRiskStatus"));
        }
        if (params.containsKey("projectEvalStatus")) {
            queryWrapper.like("projectEvalStatus", params.get("projectEvalStatus"));
        }
        if (params.containsKey("projectSelfEvalStatus")) {
            queryWrapper.like("projectSelfEvalStatus", params.get("projectSelfEvalStatus"));
        }
        if (params.containsKey("businessUnit")) {
            queryWrapper.like("businessUnit", params.get("businessUnit"));
        }
        if (params.containsKey("productLineName")) {
            queryWrapper.like("productLineName", params.get("productLineName"));
        }
        if (params.containsKey("productLineId")) {
            queryWrapper.eq("productLineId", params.get("productLineId"));
        }
        return queryWrapper;
    }
}
