/**
 * ------------------------修改日志---------------------------------
 * 修改人       修改日期         修改内容
 */
package com.fssy.shareholder.management.controller.system.performance.manage_kpi;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.annotation.RequiredLog;
import com.fssy.shareholder.management.pojo.system.performance.manage_kpi.ManagerKpiScoreOld;
import com.fssy.shareholder.management.service.manage.company.CompanyService;
import com.fssy.shareholder.management.service.system.performance.manage_kpi.ManagerKpiScoreServiceOld;
import com.fssy.shareholder.management.service.system.performance.manage_kpi.ViewManagerKpiMonthService;
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
 * @author Shizn
 * @ClassName: ManagerKpiYearScoreController
 * @Description: 经理人年度KPI绩效分推移 * @date 2022/11/18 0018 10:27
 */
@Controller
@RequestMapping("/system/performance/manager_kpi/view-manager-kpi-year-score")
public class ManagerKpiYearScoreController {
    @Autowired
    private ManagerKpiScoreServiceOld managerKpiScoreServiceOld;
    @Autowired
    private ViewManagerKpiMonthService viewManagerKpiMonthService;
    @Autowired
    private CompanyService companyService;
    /**
     * 经理人年度KPI分数管理界面
     * @param model
     * @return
     */
    @GetMapping("index")
    @RequiresPermissions("system:performance:manager_kpi:view-manager-kpi-year-score:index")
    @RequiredLog("经理人年度KPI分数")
    public String manageIndex(Model model) {
        Map<String, Object> params = new HashMap<>();
        List<Map<String, Object>> companyNameList = companyService.findCompanySelectedDataListByParams(params, new ArrayList<>());
        model.addAttribute("companyNameList",companyNameList);
        return "system/performance/manager_kpi/view-manager-kpi-year-score/view-manager-kpi-year-score-list";
    }
    /**
     * 返回 经理人年度KPI分数数据表格
     *
     * @param request
     * @return
     */
    @GetMapping("getYearScore")
    @ResponseBody
    public Map<String, Object> getViewManagerKpiMonthDatas(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> params = getParams(request);
        int limit = Integer.parseInt(request.getParameter("limit"));
        int page = Integer.parseInt(request.getParameter("page"));
        params.put("limit", limit);
        params.put("page", page);
        //获取前端公司查询的主键
        String companyIds = request.getParameter("companyIds");
        params.put("companyId",companyIds);
        Page<Map<String,Object>> managerKpiScorePage = managerKpiScoreServiceOld.findViewManagerKpiMonthDataListPerPageByParams(params);
        if (managerKpiScorePage.getTotal() == 0) {
            result.put("code", 404);
            result.put("msg", "未查出数据");
        } else {
            result.put("code", 0);
            result.put("count", managerKpiScorePage.getTotal());
            result.put("data", managerKpiScorePage.getRecords());
        }
        return result;
    }


    /**
     * 返回查看指定经理人的下级分数构成数据页面<br/>
     * 添加根据给出姓名、公司名称、年份、月份查看所有其对应的分数数据
     *
     * @param request 请求实体
     * @param model   model对象
     * @return 页面
     */
    @GetMapping("search-detail")
    public String searchByAssignFromBtn(HttpServletRequest request, Model model)
    {
        //②将获取到的id返回到前端弹出层页面
        String month = "12";
        String year = request.getParameter("year");
        String companyName = request.getParameter("companyName");
        String managerName = request.getParameter("managerName");
        model.addAttribute("month", month);
        model.addAttribute("year", year);
        model.addAttribute("companyName", companyName);
        model.addAttribute("managerName", managerName);
        return "system/performance/manager_kpi/view-manager-kpi-year-score/view-manager-kpi-year-score-detail";
    }

    /**
     * 返回指定经理人分数明细表格数据
     */
    @GetMapping("getManagerScoreData")
    @ResponseBody
    public Map<String, Object> getManagerScoreData(HttpServletRequest request)
    {
        //④将传进来的参数交给数据库查，然后返回页面
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> params = new HashMap<>();
        String managerScoreDataMonth = request.getParameter("month");
        String managerScoreDataYear = request.getParameter("year");
        String managerScoreDataCompanyName = request.getParameter("companyName");
        String managerScoreDataManagerName = request.getParameter("managerName");
        params.put("month",managerScoreDataMonth);
        params.put("year",managerScoreDataYear);
        params.put("companyName",managerScoreDataCompanyName);
        params.put("managerName",managerScoreDataManagerName);
        List<Map<String, Object>> managerScoreDataIdList = viewManagerKpiMonthService
                .findViewManagerKpiMonthMapDataByParams(params);
        if (managerScoreDataIdList.size() == 0)
        {
            result.put("code", 404);
            result.put("msg", "未查出数据");
        }
        else
        {
            result.put("code", 0);
            result.put("count", managerScoreDataIdList.size());
            result.put("data", managerScoreDataIdList);
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
        if (!ObjectUtils.isEmpty(request.getParameter("newYear"))) {
            params.put("newYear", request.getParameter("newYear"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("oneYear"))) {
            params.put("oneYear", request.getParameter("oneYear"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("twoYear"))) {
            params.put("twoYear", request.getParameter("twoYear"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("threeYear"))) {
            params.put("threeYear", request.getParameter("threeYear"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("companyIds"))) {
            params.put("companyIds", request.getParameter("companyIds"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("companyList"))) {
            params.put("companyList", request.getParameter("companyList"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("2017"))) {
            params.put("2017", request.getParameter("2017"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("2018"))) {
            params.put("2018", request.getParameter("2018"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("2019"))) {
            params.put("2019", request.getParameter("2019"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("2020"))) {
            params.put("2020", request.getParameter("2020"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("2021"))) {
            params.put("2021", request.getParameter("2021"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("2022"))) {
            params.put("2022", request.getParameter("2022"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("2023"))) {
            params.put("2023", request.getParameter("2023"));
        }

        return params;
    }

}