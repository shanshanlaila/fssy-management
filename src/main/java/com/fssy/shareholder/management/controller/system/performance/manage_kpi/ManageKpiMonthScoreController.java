/**
 * ------------------------修改日志---------------------------------
 * 修改人       修改日期         修改内容
 */
package com.fssy.shareholder.management.controller.system.performance.manage_kpi;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.annotation.RequiredLog;
import com.fssy.shareholder.management.pojo.common.SysResult;
import com.fssy.shareholder.management.pojo.system.performance.manage_kpi.ManageKpiMonthAim;
import com.fssy.shareholder.management.service.common.SheetOutputService;
import com.fssy.shareholder.management.service.system.performance.manage_kpi.ManageKpiMonthScoreService;
import com.fssy.shareholder.management.tools.exception.ServiceException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @author Shizn
 * @ClassName: ManageKpiMonthScoreController
 * @Description: 经营管理月度项目分数管理  控制类
 * @date 2022/10/27 0027 17:02
 */
@Controller
@RequestMapping("/system/performance/manager_kpi/manage-kpi-month-score/")
public class ManageKpiMonthScoreController {
    @Autowired
    private ManageKpiMonthScoreService manageKpiMonthScoreService;

    /**
     * 经营管理月度项目分数管理 页面
     *
     * @param model
     * @return
     */
    @GetMapping("index1")
    @RequiresPermissions("system:performance:manager_kpi:manage-kpi-month-score:index1")
    @RequiredLog("经营管理月度项目分数管理")
    public String manageIndex(Model model) {
        Map<String, Object> params = new HashMap<>();
        return "/system/performance/manager_kpi/manage-kpi-month-score/manage-kpi-month-score-list";
    }

    /**
     * 返回 经营管理月度项目分数数据表格
     *
     * @param request
     * @return
     */
    @GetMapping("getObjects")
    @ResponseBody
    public Map<String, Object> getManageKpiMonthScoreDatas(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> params = getParams(request);
        int limit = Integer.parseInt(request.getParameter("limit"));
        int page = Integer.parseInt(request.getParameter("page"));
        params.put("limit", limit);
        params.put("page", page);
        Page<ManageKpiMonthAim> manageKpiMonthScorePage = manageKpiMonthScoreService.findManageKpiMonthScoreDataListPerPageByParams(params);
        if (manageKpiMonthScorePage.getTotal() == 0) {
            result.put("code", 404);
            result.put("msg", "未查出数据");
        } else {
            result.put("code", 0);
            result.put("count", manageKpiMonthScorePage.getTotal());
            result.put("data", manageKpiMonthScorePage.getRecords());
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
        if (ObjectUtils.isEmpty(params.get("year"))) {
            throw new ServiceException("未选择年份，生成失败");
        }
        if (ObjectUtils.isEmpty(params.get("month"))) {
            throw new ServiceException("未选择月份，生成失败");
        }
        boolean result = manageKpiMonthScoreService.createScore(params);
        if (result) {
            return SysResult.ok();
        }
        return SysResult.build(500, "分数生成失败");
    }

    /**
     * 导出
     *
     * @param request
     * @param response
     */
    @RequiredLog("数据导出")
    @GetMapping("downloadForCharge")
    public void downloadForCharge(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> params = getParams(request);
        //sql语句
        params.put("select", "id,companyName,projectType,projectDesc,unit,year,basicTarget,mustInputTarget," +
                "reachTarget,challengeTarget,month,monthTarget,monthActualValue,monthTarget,monthActualValue," +
                "accumulateTarget,accumulateActual,scoreAuto,scoreAdjust");
        //查询
        List<Map<String, Object>> manageKpiMonthMapDataByParams = manageKpiMonthScoreService.findManageKpiMonthScoreMapDataByParams(params);
        LinkedHashMap<String, String> fieldMap = new LinkedHashMap<>();
        //需要改变背景的格子
        fieldMap.put("id", "序号");
        fieldMap.put("companyName", "企业名称");
        fieldMap.put("projectType", "指标类别");
        fieldMap.put("projectDesc", "项目名称");
        fieldMap.put("unit", "单位");
        fieldMap.put("year", "年份");
        fieldMap.put("basicTarget", "基本目标");
        fieldMap.put("mustInputTarget", "必达目标");
        fieldMap.put("reachTarget", "达标目标");
        fieldMap.put("challengeTarget", "挑战目标");
        fieldMap.put("month", "月份");
        fieldMap.put("monthTarget", "月份目标");
        fieldMap.put("monthActualValue", "月份实绩");
        fieldMap.put("accumulateTarget", "目标累计值");
        fieldMap.put("accumulateActual", "实绩累计值");
        fieldMap.put("scoreAuto", "自动生成分数");
        fieldMap.put("scoreAdjust", "人工评分");

        //标识字符串的列
        List<Integer> strList = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16);
        SheetOutputService sheetOutputService = new SheetOutputService();
        if (ObjectUtils.isEmpty(manageKpiMonthMapDataByParams)) {
            throw new ServiceException("未查出数据");
        }
        sheetOutputService.exportNum("经营月度项目分数报表", manageKpiMonthMapDataByParams, fieldMap, response, strList, null);

    }

    private Map<String, Object> getParams(HttpServletRequest request) {
        Map<String, Object> params = new HashMap<>();
        if (!ObjectUtils.isEmpty(request.getParameter("id"))) {
            params.put("id", request.getParameter("id"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("manageKpiYearId"))) {
            params.put("manageKpiYearId", request.getParameter("manageKpiYearId"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("companyName"))) {
            params.put("companyName", request.getParameter("companyName"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("status"))) {
            params.put("status", request.getParameter("status"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("projectType"))) {
            params.put("projectType", request.getParameter("projectType"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("projectDesc"))) {
            params.put("projectDesc", request.getParameter("projectDesc"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("unit"))) {
            params.put("unit", request.getParameter("unit"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("dataSource"))) {
            params.put("dataSource", request.getParameter("dataSource"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("benchmarkCompany"))) {
            params.put("benchmarkCompany", request.getParameter("benchmarkCompany"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("benchmarkValue"))) {
            params.put("benchmarkValue", request.getParameter("benchmarkValue"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("monitorDepartment"))) {
            params.put("monitorDepartment", request.getParameter("monitorDepartment"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("monitorUser"))) {
            params.put("monitorUser", request.getParameter("monitorUser"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("year"))) {
            params.put("year", request.getParameter("year"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("month"))) {
            params.put("month", request.getParameter("month"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("basicTarget"))) {
            params.put("basicTarget", request.getParameter("basicTarget"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("mustInputTarget"))) {
            params.put("mustInputTarget", request.getParameter("mustInputTarget"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("reachTarget"))) {
            params.put("reachTarget", request.getParameter("reachTarget"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("challengeTarget"))) {
            params.put("challengeTarget", request.getParameter("challengeTarget"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("proportion"))) {
            params.put("proportion", request.getParameter("proportion"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("pastOneYearActual"))) {
            params.put("pastOneYearActual", request.getParameter("pastOneYearActual"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("pastTwoYearsActual"))) {
            params.put("pastTwoYearsActual", request.getParameter("pastTwoYearsActual"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("pastThreeYearsActual"))) {
            params.put("pastThreeYearsActual", request.getParameter("pastThreeYearsActual"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("setPolicy"))) {
            params.put("setPolicy", request.getParameter("setPolicy"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("source"))) {
            params.put("source", request.getParameter("source"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("monthTarget"))) {
            params.put("monthTarget", request.getParameter("monthTarget"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("monthActualValue"))) {
            params.put("monthActualValue", request.getParameter("monthActualValue"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("accumulateTarget"))) {
            params.put("accumulateTarget", request.getParameter("accumulateTarget"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("accumulateActual"))) {
            params.put("accumulateActual", request.getParameter("accumulateActual"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("analyzeDesc"))) {
            params.put("analyzeDesc", request.getParameter("analyzeDesc"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("scoreAuto"))) {
            params.put("scoreAuto", request.getParameter("scoreAuto"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("scoreAdjust"))) {
            params.put("scoreAdjust", request.getParameter("scoreAdjust"));
        }
        return params;


    }
}