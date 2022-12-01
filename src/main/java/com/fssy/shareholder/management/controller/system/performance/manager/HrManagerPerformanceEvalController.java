package com.fssy.shareholder.management.controller.system.performance.manager;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.pojo.common.SysResult;
import com.fssy.shareholder.management.pojo.system.performance.manager.HrManagerPerformanceEval;
import com.fssy.shareholder.management.service.system.performance.manager.HrManagerPerformanceEvalService;
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
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *经理人绩效汇总评分表 前端控制器
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

    /**
     * 经理人定性评价汇总表 管理页面
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
     *  自动生成分数
     * @param
     * @return 结果
     */
    @PostMapping("updateScore")
    @ResponseBody
    public SysResult updateScore(HttpServletRequest request){
        Map<String, Object> params = getParams(request);
        String month = (String) params.get("month");
        String year = (String) params.get("year");
        boolean result = hrManagerPerformanceEvalService.updateScore(year,month);
        if (result) {
            return SysResult.ok();
        }
        return SysResult.build(500, "分数生成失败");
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
        if (!com.baomidou.mybatisplus.core.toolkit.ObjectUtils.isEmpty(request.getParameter("month"))) {
            params.put("month", request.getParameter("month"));
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
