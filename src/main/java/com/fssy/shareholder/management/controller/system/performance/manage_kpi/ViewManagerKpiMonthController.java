package com.fssy.shareholder.management.controller.system.performance.manage_kpi;


import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.annotation.RequiredLog;
import com.fssy.shareholder.management.pojo.common.SysResult;
import com.fssy.shareholder.management.pojo.system.performance.manage_kpi.ManagerKpiScoreOld;
import com.fssy.shareholder.management.service.common.SheetOutputService;
import com.fssy.shareholder.management.service.common.override.ManageKpiMonthPerformanceSheetOutputService;
import com.fssy.shareholder.management.service.system.performance.manage_kpi.ManagerKpiScoreServiceOld;
import com.fssy.shareholder.management.service.system.performance.manage_kpi.ViewManagerKpiMonthService;
import com.fssy.shareholder.management.tools.exception.ServiceException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
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
 * VIEW 前端控制器
 * </p>
 *
 * @author zzp
 * @since 2022-10-31
 */
@Controller
@RequestMapping("/system/performance/manager_kpi/view-manager-kpi-month-score")
public class ViewManagerKpiMonthController {
    @Autowired
    private ViewManagerKpiMonthService viewManagerKpiMonthService;
    @Autowired
    private ManagerKpiScoreServiceOld managerKpiScoreService;


    /**
     * 经理人月度KPI分数管理界面
     * @param model
     * @return
     */
    @GetMapping("index1")
    @RequiresPermissions("system:performance:manager_kpi:view-manager-kpi-month-score:index1")
    @RequiredLog("经理人月度KPI分数")
    public String manageIndex(Model model) {
        Map<String, Object> params = new HashMap<>();
        return "/system/performance/manager_kpi/view-manager-kpi-month-score/view-manager-kpi-month-score-list";
    }

    /**
     * 返回 经理人月度KPI分数数据表格
     *
     * @param request
     * @return
     */
    @GetMapping("getObjects")
    @ResponseBody
    public Map<String, Object> getViewManagerKpiMonthDatas(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> params = getParams(request);
        int limit = Integer.parseInt(request.getParameter("limit"));
        int page = Integer.parseInt(request.getParameter("page"));
        params.put("limit", limit);
        params.put("page", page);
        Page<ManagerKpiScoreOld> managerKpiScorePage = managerKpiScoreService.findViewManagerKpiMonthDataListPerPageByParams(params);
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
     *  自动生成分数
     * @param
     * @return 结果
     */
    @PostMapping("updateScore")
    @ResponseBody
    public SysResult updateScore(HttpServletRequest request){
        Map<String, Object> params = getParams(request);
//        System.out.println("params = " + params);
//        System.out.println("------------------------");
        if (ObjectUtils.isEmpty(params.get("managerName"))){
            throw new ServiceException("未填写经理人姓名，生成失败");
        }
        if (ObjectUtils.isEmpty(params.get("companyName"))){
            throw new ServiceException("未填写企业名称，生成失败");
        }
        if (ObjectUtils.isEmpty(params.get("generalManager"))){
            throw new ServiceException("未选择是否为经理人，生成失败");
        }
        if (ObjectUtils.isEmpty(params.get("year"))){
            throw new ServiceException("未选择年份，生成失败");
        }
        if (ObjectUtils.isEmpty(params.get("month"))){
            throw new ServiceException("未选择年份，生成失败");
        }
        boolean result = viewManagerKpiMonthService.createScore(params);
        if (result) {
            return SysResult.ok();
        }
        return SysResult.build(500, "分数生成失败");
    }

    /**
     * excel 导出
     * @param request 请求
     * @param response 响应
     */
    @RequiredLog("数据导出")
    @GetMapping("downloadForCharge")
    public void downloadForCharge(HttpServletRequest request, HttpServletResponse response) {
        Map<String,Object> params = getParams(request);
        //Sql语句
        params.put("select","year,managerName,companyName,position,generalManager,month,scoreSys,scoreAdjust," +
                "businessScore,incentiveScore,difficultyCoefficient,generalManagerScore");
        //查询
        List<Map<String,Object>> viewManagerKpiYearDataByParams = viewManagerKpiMonthService.findViewManagerKpiMonthMapDataByParams(params);
        LinkedHashMap<String,String> fieldMap = new LinkedHashMap<>();

        //需要改变背景的格子
        fieldMap.put("year", "年份");
        fieldMap.put("managerName", "经理人姓名");
        fieldMap.put("companyName", "企业名称");
        fieldMap.put("position", "职务");
        fieldMap.put("generalManager", "是否总经理");
        fieldMap.put("month", "月份");
        fieldMap.put("scoreSys", "系统生成分数");
        fieldMap.put("scoreAdjust", "人工调整分数");
        fieldMap.put("businessScore", "经营绩效分数");
        fieldMap.put("incentiveScore", "激励约束分数");
        fieldMap.put("difficultyCoefficient", "难度系数");
        fieldMap.put("generalManagerScore", "总经理经营绩效分数");
        //标识字符串的列
        List<Integer> strList = Arrays.asList(0,1,2,3,4,5,6,7,8,9,10,11);
        SheetOutputService sheetOutputService = new ManageKpiMonthPerformanceSheetOutputService();
        if (org.apache.commons.lang3.ObjectUtils.isEmpty(viewManagerKpiYearDataByParams)) {
            throw new ServiceException("未查出数据");
        }
        sheetOutputService.exportNum("经理人月度KPI分数表", viewManagerKpiYearDataByParams, fieldMap, response, strList, null);
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
        if (!ObjectUtils.isEmpty(request.getParameter("monthScoreSys"))) {
            params.put("monthScoreSys", request.getParameter("monthScoreSys"));
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
