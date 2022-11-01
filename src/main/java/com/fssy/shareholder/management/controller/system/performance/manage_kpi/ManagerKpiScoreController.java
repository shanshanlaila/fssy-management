package com.fssy.shareholder.management.controller.system.performance.manage_kpi;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.annotation.RequiredLog;
import com.fssy.shareholder.management.pojo.system.performance.manage_kpi.ManagerKpiScore;
import com.fssy.shareholder.management.service.system.performance.manage_kpi.ManagerKpiScoreService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 经理人绩效分数表 前端控制器
 * </p>
 *
 * @author Shizn
 * @since 2022-10-31
 */
@Controller
@RequestMapping("/system/performance/manager_kpi/manager-month-score/")
public class ManagerKpiScoreController {
    @Autowired
    private ManagerKpiScoreService managerKpiScoreService;

    /**
     * 经理人绩效分数 管理页面
     *
     * @param model
     * @return
     */
    @GetMapping("index")
    @RequiredLog("经理人绩效分数管理")
    @RequiresPermissions("system:performance:manager_kpi:manager-month-score:index")
    public String manageIndex(Model model) {
        Map<String, Object> params = new HashMap<>();
        return "/system/performance/manager_kpi/manager-month-score/manager-month-score-list";
    }

    /**
     * 返回经理人绩效分数 数据
     *
     * @param request
     * @return
     */
    @GetMapping("getObjects")
    @ResponseBody
    public Map<String, Object> getManagerMonthScoreDatas(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> params = getParams(request);
        int limit = Integer.parseInt(request.getParameter("limit"));
        int page = Integer.parseInt(request.getParameter("page"));
        params.put("limit", limit);
        params.put("page", page);
        Page<ManagerKpiScore> managerKpiScorePage = managerKpiScoreService.findManagerKpiScoreDataListPerPageByParams(params);
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
     * 打开详情信息
     * @param request
     * @param model
     * @return
     */
    @GetMapping("edit")
    public String edit(HttpServletRequest request, Model model) {
        Integer id = Integer.valueOf(request.getParameter("id"));
        ManagerKpiScore managerKpiScore = managerKpiScoreService.getById(id);
        model.addAttribute("managerKpiScore",managerKpiScore);
        return "system/student/student-edit";

    }
    private Map<String, Object> getParams(HttpServletRequest request) {
        Map<String, Object> params = new HashMap<>();
        if (!ObjectUtils.isEmpty(request.getParameter("id"))) {
            params.put("id", request.getParameter("id"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("year"))) {
            params.put("year", request.getParameter("year"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("managerName"))) {
            params.put("managerName", request.getParameter("managerName"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("companyName"))) {
            params.put("companyName", request.getParameter("companyName"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("position"))) {
            params.put("position", request.getParameter("position"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("generalManager"))) {
            params.put("generalManager", request.getParameter("generalManager"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("month"))) {
            params.put("month", request.getParameter("month"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("scoreSys"))) {
            params.put("scoreSys", request.getParameter("scoreSys"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("scoreAdjust"))) {
            params.put("scoreAdjust", request.getParameter("scoreAdjust"));
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
        return params;

    }

}
