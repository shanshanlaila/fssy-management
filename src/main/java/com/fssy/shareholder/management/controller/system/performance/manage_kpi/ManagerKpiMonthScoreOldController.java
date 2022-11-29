/**
 * ------------------------修改日志---------------------------------
 * 修改人       修改日期         修改内容
 */
package com.fssy.shareholder.management.controller.system.performance.manage_kpi;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.annotation.RequiredLog;
import com.fssy.shareholder.management.service.manage.company.CompanyService;
import com.fssy.shareholder.management.service.system.performance.manage_kpi.ManagerKpiMonthScoreOldService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 张泽鹏
 * @ClassName: ManagerKpiMonthScoreOldController
 * @Description: 经理人绩效月度推移
 * @date 2022-11-18 15:28
 */
@Controller
@RequestMapping("/system/performance/manager_kpi/manager-kpi-month-score-old")
public class ManagerKpiMonthScoreOldController {
    @Autowired
    private ManagerKpiMonthScoreOldService managerKpiMonthScoreOldService;
    @Autowired
    private CompanyService companyService;
    /**
     * 经理人绩效月度推移 管理页面
     *
     * @param model
     * @return
     */
    @RequiresPermissions("system:performance:manager_kpi:manager-kpi-month-score-old:index1")
    @RequiredLog("经理人绩效月度推移")
    @GetMapping("index1")
    public String manageIndex(Model model) {
        Map<String, Object> params = new HashMap<>();
        List<Map<String, Object>> companyNameList = companyService.findCompanySelectedDataListByParams(params, new ArrayList<>());
        model.addAttribute("companyNameList",companyNameList);
        return "system/performance/manager_kpi/manager-kpi-month-score-old/manager-kpi-month-score-old-list";
    }
    /**
     * 返回经理人绩效月度推移的表格
     *
     * @param request
     * @return
     */
    @GetMapping("getObjects")
    @ResponseBody
    public Map<String, Object> getManagerKpiMonthScoreOldDatas(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> params = getParams(request);
        int limit = Integer.parseInt(request.getParameter("limit"));
        int page = Integer.parseInt(request.getParameter("page"));
        params.put("limit", limit);
        params.put("page", page);
        //获取前端公司查询的主键
        String companyIds = request.getParameter("companyIds");
        params.put("companyId",companyIds);
        Page<Map<String, Object>> managerKpiMonthScoreOldPage = managerKpiMonthScoreOldService.findManageKpiMonthScoreOldDataMapListPerPageByParams(params);
        if (managerKpiMonthScoreOldPage.getTotal() == 0) {
            result.put("code", 404);
            result.put("msg", "未查出数据");
        } else {
            result.put("code", 0);
            result.put("count", managerKpiMonthScoreOldPage.getTotal());
            result.put("data", managerKpiMonthScoreOldPage.getRecords());
        }
        return result;
    }

    //获取数据库里的数据
    private Map<String, Object> getParams(HttpServletRequest request) {
        Map<String, Object> params = new HashMap<>();
        if (!ObjectUtils.isEmpty(request.getParameter("generalManager"))) {
            params.put("generalManager", request.getParameter("generalManager"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("manageKpiYearId"))) {
            params.put("manageKpiYearId", request.getParameter("manageKpiYearId"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("proportion"))) {
            params.put("proportion", request.getParameter("proportion"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("managerName"))) {
            params.put("managerName", request.getParameter("managerName"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("year"))) {
            params.put("year", request.getParameter("year"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("position"))) {
            params.put("position", request.getParameter("position"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("companyName"))) {
            params.put("companyName", request.getParameter("companyName"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("month"))) {
            params.put("month", request.getParameter("month"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("monthScoreAuto"))) {
            params.put("monthScoreAuto", request.getParameter("monthScoreAuto"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("monthScoreAdjust"))) {
            params.put("monthScoreAdjust", request.getParameter("monthScoreAdjust"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("manageKpiYearId"))) {
            params.put("manageKpiYearId", request.getParameter("manageKpiYearId"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("status"))) {
            params.put("status", request.getParameter("status"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("projectType"))) {
            params.put("projectType", request.getParameter("projectType"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("scoreAdjust"))) {
            params.put("scoreAdjust", request.getParameter("scoreAdjust"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("scoreAuto"))) {
            params.put("scoreAuto", request.getParameter("scoreAuto"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("businessScore"))) {
            params.put("businessScore", request.getParameter("businessScore"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("incentiveScore"))) {
            params.put("incentiveScore", request.getParameter("incentiveScore"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("difficultyCoefficient"))) {
            params.put("difficultyCoefficient", request.getParameter("difficultyCoefficient"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("generalManagerScore"))) {
            params.put("generalManagerScore", request.getParameter("generalManagerScore"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("advantageAnalyze"))) {
            params.put("advantageAnalyze", request.getParameter("advantageAnalyze"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("disadvantageAnalyze"))) {
            params.put("disadvantageAnalyze", request.getParameter("disadvantageAnalyze"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("riskDesc"))) {
            params.put("riskDesc", request.getParameter("riskDesc"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("respDepartment"))) {
            params.put("respDepartment", request.getParameter("respDepartment"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("groupImproveAction"))) {
            params.put("groupImproveAction", request.getParameter("groupImproveAction"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("month1"))) {
            params.put("month1", request.getParameter("month1"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("month2"))) {
            params.put("month2", request.getParameter("month2"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("month3"))) {
            params.put("month3", request.getParameter("month3"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("month4"))) {
            params.put("month4", request.getParameter("month4"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("month5"))) {
            params.put("month5", request.getParameter("month5"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("month6"))) {
            params.put("month6", request.getParameter("month6"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("month7"))) {
            params.put("month7", request.getParameter("month7"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("month8"))) {
            params.put("month8", request.getParameter("month8"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("month9"))) {
            params.put("month9", request.getParameter("month9"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("month10"))) {
            params.put("month10", request.getParameter("month10"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("month11"))) {
            params.put("month11", request.getParameter("month11"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("month12"))) {
            params.put("month12", request.getParameter("month12"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("companyIds"))) {
            params.put("companyIds", request.getParameter("companyIds"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("companyList"))) {
            params.put("companyList", request.getParameter("companyList"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("anomalyMark"))) {
            params.put("anomalyMark", request.getParameter("anomalyMark"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("abnormal1"))) {
            params.put("abnormal1", request.getParameter("abnormal1"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("abnormal2"))) {
            params.put("abnormal2", request.getParameter("abnormal2"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("abnormal3"))) {
            params.put("abnormal3", request.getParameter("abnormal3"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("abnormal4"))) {
            params.put("abnormal4", request.getParameter("abnormal4"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("abnormal5"))) {
            params.put("abnormal5", request.getParameter("abnormal5"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("abnormal6"))) {
            params.put("abnormal6", request.getParameter("abnormal6"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("abnormal7"))) {
            params.put("abnormal7", request.getParameter("abnormal7"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("abnormal8"))) {
            params.put("abnormal8", request.getParameter("abnormal8"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("abnormal9"))) {
            params.put("abnormal9", request.getParameter("abnormal9"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("abnormal10"))) {
            params.put("abnormal10", request.getParameter("abnormal10"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("abnormal11"))) {
            params.put("abnormal11", request.getParameter("abnormal11"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("abnormal12"))) {
            params.put("abnormal12", request.getParameter("abnormal12"));
        }
        return params;
    }
}