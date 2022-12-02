package com.fssy.shareholder.management.controller.system.performance.manager;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.annotation.RequiredLog;
import com.fssy.shareholder.management.pojo.common.SysResult;
import com.fssy.shareholder.management.pojo.system.performance.manager.HrManagerPerformanceEval;
import com.fssy.shareholder.management.pojo.system.performance.manager.ManagerQualitativeEval;
import com.fssy.shareholder.management.service.common.SheetOutputService;
import com.fssy.shareholder.management.service.system.performance.manage_kpi.ManagerKpiScoreService;
import com.fssy.shareholder.management.service.system.performance.manage_kpi.ManagerKpiScoreServiceOld;
import com.fssy.shareholder.management.service.system.performance.manage_kpi.ViewManagerKpiMonthService;
import com.fssy.shareholder.management.service.system.performance.manager.HrManagerPerformanceEvalService;
import com.fssy.shareholder.management.service.system.performance.manager.ManagerQualitativeEvalService;
import com.fssy.shareholder.management.service.system.performance.manager.ManagerQualitativeEvalStdService;
import com.fssy.shareholder.management.tools.exception.ServiceException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * <p>
 * 经理人绩效汇总评分表 前端控制器
 * </p>
 *
 * @author Shizn
 * @since 2022-11-30
 */
@Controller
@RequestMapping("/system/performance/manager/manager-performance-eval")
public class HrManagerPerformanceEvalController {
    @Autowired
    private HrManagerPerformanceEvalService hrManagerPerformanceEvalService;
    @Autowired
    private ViewManagerKpiMonthService viewManagerKpiMonthService;
    @Autowired
    private ManagerKpiScoreServiceOld managerKpiScoreServiceOld;
    @Autowired
    private ManagerQualitativeEvalStdService managerQualitativeEvalStdService;
    @Autowired
    private ManagerQualitativeEvalService managerQualitativeEvalService;
    /**
     * 经理人定性评价汇总表 管理页面
     *
     * @param model
     * @return
     */
    @GetMapping("index")
    @RequiresPermissions("system:performance:manager:manager-performance-eval:manager-performance-eval-list:index")
    public String managerIndex(Model model) {
        Map<String, Object> params = new HashMap<>();
        return "system/performance/manager/manager-performance-eval/manager-performance-eval-list";
    }

    /**
     * 经理人定性评价汇总表 数据
     *
     * @param request
     * @return
     */
    @GetMapping("getObject")
    @ResponseBody
    public Map<String, Object> getPerformanceEvalDatas(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> params = getParams(request);
        // 获取limit和page
        int limit = Integer.parseInt(request.getParameter("limit"));
        int page = Integer.parseInt(request.getParameter("page"));
        params.put("limit", limit);
        params.put("page", page);
        Page<HrManagerPerformanceEval> hrManagerPerformanceEvalPage = hrManagerPerformanceEvalService.findHrManagerPerformanceEvalDataListPerPageByParams(params);
        if (hrManagerPerformanceEvalPage.getTotal() == 0) {
            result.put("code", 404);
            result.put("msg", "未查出数据");

        } else {
            result.put("code", 0);
            result.put("count", hrManagerPerformanceEvalPage.getTotal());
            result.put("data", hrManagerPerformanceEvalPage.getRecords());
        }
        return result;
    }

    /**
     * 自动生成分数
     *
     * @param
     * @return 结果
     */
    @PostMapping("updateScore")
    @ResponseBody
    public SysResult updateScore(HttpServletRequest request) {
        Map<String, Object> params = getParams(request);
        String month = (String) params.get("kpiScoreMonth");
        String year = (String) params.get("year");
        boolean result = hrManagerPerformanceEvalService.updateScore(year, month);
        if (result) {
            return SysResult.ok();
        }
        return SysResult.build(500, "分数生成失败");
    }

    /**
     * excel 导出
     *
     * @param request  请求
     * @param response 相应
     */
    @GetMapping("downloadForCharge")
    @RequiredLog("数据导出")
    public void downloadForCharge(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> params = getParams(request);
        params.put("select", "id,managerName,companyName,position,year,kpiScore,qualitativeScore,scoreAdjust,kpiScoreR,qualitativeScoreR");
        List<Map<String, Object>> hrManagerPerformanceEvalDataByParams = hrManagerPerformanceEvalService.findHrManagerPerformanceEvalDataByParams(params);
        LinkedHashMap<String, String> fieldMap = new LinkedHashMap<>();

        //需要改变背景色的格子
        fieldMap.put("id", "序号");
        fieldMap.put("managerName", "经理人姓名");
        fieldMap.put("companyName", "所在公司");
        fieldMap.put("position", "职务");
        fieldMap.put("year", "年份");
        fieldMap.put("kpiScore", "定量评价");
        fieldMap.put("qualitativeScore", "定性评价");
        fieldMap.put("scoreAdjust", "综合绩效分数");
        fieldMap.put("kpiScoreR", "定量评价占比");
        fieldMap.put("qualitativeScoreR", "定性评价占比");

        //标识字符串的列
        List<Integer> strList = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
        SheetOutputService sheetOutputService = new SheetOutputService();
        if (ObjectUtils.isEmpty(hrManagerPerformanceEvalDataByParams)) {
            throw new ServiceException("未查出数据");
        }
        sheetOutputService.exportNum("企业经理人年度绩效汇总表", hrManagerPerformanceEvalDataByParams, fieldMap, response, strList, null);

    }

    /**
     * 修改经营管理指标库信息
     * @param request
     * @param model
     * @return
     */
    @GetMapping("edit")
    public String edit(HttpServletRequest request, Model model) {
        String id = request.getParameter("id");
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        HrManagerPerformanceEval hrManagerPerformanceEval = hrManagerPerformanceEvalService.findHrHrManagerPerformanceEvalDataByParams(params).get(0);
        model.addAttribute("hrManagerPerformanceEval", hrManagerPerformanceEval);
        return "system/performance/manager/manager-performance-eval/manager-performance-eval-edit";
    }
    /**
     * 更新经营管理指标库信息
     * @param hrManagerPerformanceEval
     * @return
     */
    @PostMapping("update")
    @ResponseBody
    public SysResult update(HrManagerPerformanceEval hrManagerPerformanceEval) {

        boolean result = hrManagerPerformanceEvalService.updateHrManagerPerformanceEval(hrManagerPerformanceEval);
        if (result) {
            return SysResult.ok();
        }
        return SysResult.build(500, "企业经理人年度绩效汇总信息没有更新，请检查数据后重新尝试");
    }
    /**
     * 返回查看指定总经理的定性评价分数详情页面<br/>
     * 添加根据给出姓名、公司名称、年份查看所有其对应的分数数据
     * @param request 请求实体
     * @param model   model对象
     * @return 页面
     */
    @GetMapping("search-qualitative")
    public String searchQualitative(HttpServletRequest request, Model model)
    {
        //②将获取到的姓名、公司名称、年份、月份返回到前端弹出层页面
        String month = request.getParameter("kpiScoreMonth");
        String year = request.getParameter("year");
        String companyName = request.getParameter("companyName");
        String managerName = request.getParameter("managerName");
        String position=request.getParameter("position");
        model.addAttribute("month", month);
        model.addAttribute("year", year);
        model.addAttribute("companyName", companyName);
        model.addAttribute("managerName", managerName);
        model.addAttribute("position",position);
        Map<String, Object> params = new HashMap<>();
        Map<String, Object> map = new HashMap<>();

        params.put("year",year);
        map.put("year",year);
        map.put("companyName",companyName);
        map.put("managerName",managerName);
        map.put("position",position);
        Map<String, Object> managerQualitativeEvalStd =
               managerQualitativeEvalStdService.findManagerDataByParams(params).get(0);
//        Map<String, Object> stringObjectMap = managerQualitativeEvalService.findManagerQualitativeEvalDataByParams(map).get(0);
//        model.addAttribute("stringObjectMap",stringObjectMap);
        model.addAttribute("managerQualitativeEvalStd", managerQualitativeEvalStd);

        return "system/performance/manager/manager-performance-eval/manager-performance-eval-general-detail";
    }

    /**
     * 返回指定总经理定性评价分数明细表格数据
     */
    @GetMapping("getGeneralScore")
    @ResponseBody
    public Map<String, Object> getGeneralScore(HttpServletRequest request)
    {
        //④将传进来的参数交给数据库查，然后返回页面
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> params = new HashMap<>();
        String managerScoreDataMonth = request.getParameter("kpiScoreMonth");
        String managerScoreDataYear = request.getParameter("year");
        String managerScoreDataCompanyName = request.getParameter("companyName");
        String managerScoreDataManagerName = request.getParameter("managerName");
        params.put("month",managerScoreDataMonth);
        params.put("year",managerScoreDataYear);
        params.put("companyName",managerScoreDataCompanyName);
        params.put("managerName",managerScoreDataManagerName);

        List<Map<String, Object>> managerQualitativeEvalDataByParams = managerQualitativeEvalService
                .findManagerQualitativeEvalDataByParams(params);
        if (managerQualitativeEvalDataByParams.size() == 0)
        {
            result.put("code", 404);
            result.put("msg", "未查出数据");
        }
        else
        {
            result.put("code", 0);
            result.put("count", managerQualitativeEvalDataByParams.size());
            result.put("data", managerQualitativeEvalDataByParams);
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
        //②将获取到的姓名、公司名称、年份、月份返回到前端弹出层页面
        String month = request.getParameter("kpiScoreMonth");
        String year = request.getParameter("year");
        String companyName = request.getParameter("companyName");
        String companyNameShort = request.getParameter("companyNameShort");
        String managerName = request.getParameter("managerName");
        model.addAttribute("month", month);
        model.addAttribute("year", year);
        model.addAttribute("companyName", companyName);
        model.addAttribute("managerName", managerName);

        Map<String, Object> params = new HashMap<>();
        params.put("month",month);
        params.put("year",year);
        params.put("companyName",companyName);
        params.put("managerName",managerName);
        Map<String, Object> managerKpiScoreOld = managerKpiScoreServiceOld.findManagerKpiScoreOldDataByParams(params).get(0);
        model.addAttribute("managerKpiScoreOld", managerKpiScoreOld);
        return "system/performance/manager_kpi/view-manager-kpi-month-score/view-manager-kpi-month-score-detail";
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
    private Map<String, Object> getParams(HttpServletRequest request) {
        Map<String, Object> params = new HashMap<>();
        //主键查询
        if (!ObjectUtils.isEmpty(request.getParameter("id"))) {
            params.put("id", request.getParameter("id"));
        }
        if (!com.baomidou.mybatisplus.core.toolkit.ObjectUtils.isEmpty(request.getParameter("year"))) {
            params.put("year", request.getParameter("year"));
        }
        if (!com.baomidou.mybatisplus.core.toolkit.ObjectUtils.isEmpty(request.getParameter("kpiScoreMonth"))) {
            params.put("kpiScoreMonth", request.getParameter("kpiScoreMonth"));
        }
        if (!com.baomidou.mybatisplus.core.toolkit.ObjectUtils.isEmpty(request.getParameter("status"))) {
            params.put("status", request.getParameter("status"));
        }
        if (!com.baomidou.mybatisplus.core.toolkit.ObjectUtils.isEmpty(request.getParameter("kpiScoreMonth"))) {
            params.put("kpiScoreMonth", request.getParameter("kpiScoreMonth"));
        }
        if (!com.baomidou.mybatisplus.core.toolkit.ObjectUtils.isEmpty(request.getParameter("managerName"))) {
            params.put("managerName", request.getParameter("managerName"));
        }
        if (!com.baomidou.mybatisplus.core.toolkit.ObjectUtils.isEmpty(request.getParameter("companyName"))) {
            params.put("companyName", request.getParameter("companyName"));
        }
        if (!com.baomidou.mybatisplus.core.toolkit.ObjectUtils.isEmpty(request.getParameter("generalManager"))) {
            params.put("generalManager", request.getParameter("generalManager"));
        }
        if (!com.baomidou.mybatisplus.core.toolkit.ObjectUtils.isEmpty(request.getParameter("position"))) {
            params.put("position", request.getParameter("position"));
        }
        if (!com.baomidou.mybatisplus.core.toolkit.ObjectUtils.isEmpty(request.getParameter("companyNameShort"))) {
            params.put("companyNameShort", request.getParameter("companyNameShort"));
        }
        if (!com.baomidou.mybatisplus.core.toolkit.ObjectUtils.isEmpty(request.getParameter("scoreAdjustKpi"))) {
            params.put("scoreAdjustKpi", request.getParameter("scoreAdjustKpi"));
        }
        if (!com.baomidou.mybatisplus.core.toolkit.ObjectUtils.isEmpty(request.getParameter("qualitativeScore"))) {
            params.put("qualitativeScore", request.getParameter("qualitativeScore"));
        }
        if (!com.baomidou.mybatisplus.core.toolkit.ObjectUtils.isEmpty(request.getParameter("kpiScoreR"))) {
            params.put("kpiScoreR", request.getParameter("kpiScoreR"));
        }
        if (!com.baomidou.mybatisplus.core.toolkit.ObjectUtils.isEmpty(request.getParameter("qualitativeScoreR"))) {
            params.put("qualitativeScoreR", request.getParameter("qualitativeScoreR"));
        }
        if (!com.baomidou.mybatisplus.core.toolkit.ObjectUtils.isEmpty(request.getParameter("scoreAuto"))) {
            params.put("scoreAuto", request.getParameter("scoreAuto"));
        }
        if (!com.baomidou.mybatisplus.core.toolkit.ObjectUtils.isEmpty(request.getParameter("scoreAdjust"))) {
            params.put("scoreAdjust", request.getParameter("scoreAdjust"));
        }
        if (!com.baomidou.mybatisplus.core.toolkit.ObjectUtils.isEmpty(request.getParameter("kpiScore"))) {
            params.put("kpiScore", request.getParameter("kpiScore"));
        }
        if (!com.baomidou.mybatisplus.core.toolkit.ObjectUtils.isEmpty(request.getParameter("scoreAdjustCause"))) {
            params.put("scoreAdjustCause", request.getParameter("scoreAdjustCause"));
        }
        return params;
    }
}
