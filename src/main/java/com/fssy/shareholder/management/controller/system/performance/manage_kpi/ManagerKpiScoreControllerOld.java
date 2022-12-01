package com.fssy.shareholder.management.controller.system.performance.manage_kpi;


import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.fssy.shareholder.management.annotation.RequiredLog;
import com.fssy.shareholder.management.service.common.SheetOutputService;
import com.fssy.shareholder.management.service.system.performance.manage_kpi.ManagerKpiScoreServiceOld;
import com.fssy.shareholder.management.tools.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * <p>
 * 经理人绩效分数表		【修改内容】增加了年度关联ID字段	【修改时间】2022-10-27	【修改人】张泽鹏 前端控制器
 * 暂时没有使用
 * </p>
 *
 * @author zzp
 * @since 2022-10-31
 */
@Controller
@RequestMapping("/system/performance/manager_kpi/view-manager-kpi-month-score")
public class ManagerKpiScoreControllerOld {
    @Autowired
    private ManagerKpiScoreServiceOld managerKpiScoreServiceOld;



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
        params.put("select","year,managerName,companyName,position,generalManager,month," +
                "businessScore,incentiveScore,difficultyCoefficient,generalManagerScore,scoreAdjust,scoreAuto," +
                "advantageAnalyze,disadvantageAnalyze,riskDesc,respDepartment,groupImproveAction,anomalyType");
        //查询
        List<Map<String,Object>> managerKpiScoreOldDataByParams = managerKpiScoreServiceOld.findManagerKpiScoreOldDataByParams(params);
        LinkedHashMap<String,String> fieldMap = new LinkedHashMap<>();

        //需要改变背景的格子
        fieldMap.put("year", "年份");
        fieldMap.put("managerName", "经理人姓名");
        fieldMap.put("companyName", "企业名称");
        fieldMap.put("position", "职务");
        fieldMap.put("generalManager", "是否总经理");
        fieldMap.put("month", "月份");
        fieldMap.put("businessScore", "经营绩效分数");
        fieldMap.put("incentiveScore", "激励约束分数");
        fieldMap.put("difficultyCoefficient", "难度系数");
        fieldMap.put("generalManagerScore", "总经理经营绩效分数");
        fieldMap.put("scoreAuto", "系统生成分数");
        fieldMap.put("scoreAdjust", "人工调整分数");
        fieldMap.put("advantageAnalyze", "绩效优点");
        fieldMap.put("disadvantageAnalyze", "绩效存在问题");
        fieldMap.put("riskDesc", "绩效风险");
        fieldMap.put("respDepartment", "总部管控责任部门");
        fieldMap.put("groupImproveAction", "管控措施");
        fieldMap.put("anomalyType", "绩效异常类别");
        //标识字符串的列
        List<Integer> strList = Arrays.asList(0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17);
        SheetOutputService sheetOutputService = new SheetOutputService();
        if (org.apache.commons.lang3.ObjectUtils.isEmpty(managerKpiScoreOldDataByParams)) {
            throw new ServiceException("未查出数据");
        }
        sheetOutputService.exportNum("经理人KPI分数表", managerKpiScoreOldDataByParams, fieldMap, response, strList, null);
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
        if (!ObjectUtils.isEmpty(request.getParameter("anomalyMark"))) {
            params.put("anomalyMark", request.getParameter("anomalyMark"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("anomalyType"))) {
            params.put("anomalyType", request.getParameter("anomalyType"));
        }
        return params;
    }
}
