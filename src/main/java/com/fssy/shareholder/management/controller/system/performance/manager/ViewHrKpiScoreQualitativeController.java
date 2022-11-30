package com.fssy.shareholder.management.controller.system.performance.manager;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.pojo.system.performance.manage_kpi.ManageKpiLib;
import com.fssy.shareholder.management.pojo.system.performance.manager.ViewHrKpiScoreQualitative;
import com.fssy.shareholder.management.service.system.performance.manager.ViewHrKpiScoreQualitativeService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 经理人定性评价汇总表 前端控制器
 * </p>
 *
 * @author Shizn
 * @since 2022-11-29
 */
@Controller
@RequestMapping("/system/performance/manager/view-kpi-score-qualitative")
public class ViewHrKpiScoreQualitativeController {
    @Autowired
    private ViewHrKpiScoreQualitativeService viewHrKpiScoreQualitativeService;
    /**
     * 经理人定性评价汇总表 管理页面
     * @param model
     * @return
     */
    @GetMapping("index")
    @RequiresPermissions("system:performance:manager:view-kpi-score-qualitative:view-kpi-score-qualitative-list:index")
    public String managerIndex(Model model) {
        Map<String, Object> params = new HashMap<>();
        return "system/performance/manager/view-kpi-score-qualitative/view-kpi-score-qualitative-list";
    }

    /**
     * 经理人定性评价汇总表 数据
     *
     * @param request
     * @return
     */
    @GetMapping("getObject")
    @ResponseBody
    public Map<String, Object> getViewQualitativeDatas(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> params = getParams(request);
        // 获取limit和page
        int limit = Integer.parseInt(request.getParameter("limit"));
        int page = Integer.parseInt(request.getParameter("page"));
        params.put("limit", limit);
        params.put("page", page);
        Page<ViewHrKpiScoreQualitative> viewHrKpiScoreQualitativePage = viewHrKpiScoreQualitativeService.findViewHrKpiScoreQualitativeDataListPerPageByParams(params);
        if (viewHrKpiScoreQualitativePage.getTotal() == 0) {
            result.put("code", 404);
            result.put("msg", "未查出数据");

        } else {
            result.put("code", 0);
            result.put("count", viewHrKpiScoreQualitativePage.getTotal());
            result.put("data", viewHrKpiScoreQualitativePage.getRecords());
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
        return params;
    }

}
