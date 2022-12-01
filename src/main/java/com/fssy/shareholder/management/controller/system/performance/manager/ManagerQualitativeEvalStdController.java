package com.fssy.shareholder.management.controller.system.performance.manager;



import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.pojo.common.SysResult;

import com.fssy.shareholder.management.pojo.system.performance.manager.ManagerQualitativeEval;
import com.fssy.shareholder.management.pojo.system.performance.manager.ManagerQualitativeEvalStd;
import com.fssy.shareholder.management.service.manage.company.CompanyService;

import com.fssy.shareholder.management.service.system.performance.manager.ManagerQualitativeEvalService;
import com.fssy.shareholder.management.service.system.performance.manager.ManagerQualitativeEvalStdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * **数据表名：	bs_manager_qualitative_eval_std	**数据表中文名：	经理人绩效定性评分各项目占比表	**业务部门：	人力资源部	**数据表作用：	记录企业年度总经理、分管经理人定性项目占比表，因为该比例每年都可能变化 前端控制器
 * </p>
 *
 * @author Solomon
 * @since 2022-11-28
 */
@Controller
@RequestMapping("/system/performance/manager/manager-qualitative-eval-std")
public class ManagerQualitativeEvalStdController {

    @Autowired
    private ManagerQualitativeEvalStdService managerQualitativeEvalStdService;

    @Autowired
    private CompanyService companyService;

    /**
     * 经理人绩效定性评分各项目占比管理页面
     * @param model
     * @return
     */
    @GetMapping("index2")
    public String manageIndex(Model model){
        Map<String, Object> params = new HashMap<>();
        List<Map<String, Object>> companyNameList = companyService.findCompanySelectedDataListByParams(params, new ArrayList<>());
        model.addAttribute("companyNameList",companyNameList);
        return "system/performance/manager/manager-qualitative-eval-std/manager-qualitative-eval-std-list";
    }

    /**
     * 添加经理人绩效定性评分各项目占比信息页
     * @param model
     * @return
     */
    @GetMapping("create")
    public String create(Model model){
        return "system/performance/manager/manager-qualitative-eval-std/manager-qualitative-eval-std-create";
    }

    /**
     * 添加经理人绩效定性评分各项目占比信息
     * @param managerQualitativeEvalStd
     * @param request
     * @return
     */
    @RequestMapping("store")
    @ResponseBody
    public SysResult storemanagerQualitativeEvalStd(ManagerQualitativeEvalStd managerQualitativeEvalStd,HttpServletRequest request){
        boolean result = managerQualitativeEvalStdService.insertManagerQualitativeEvalStd(managerQualitativeEvalStd);
        if (result) {
            return SysResult.ok();
        }
        return SysResult.build(500, "总经理和非总经理占比分别要100%，请检查数据后重新尝试");
    }

    /**
     * 返回经理人绩效定性评分各项目占比页面
     * @param request
     * @return
     */
    @GetMapping("getYearScore")
    @ResponseBody
    public Map<String,Object> getManagerQualitativeEvalStdDatas(HttpServletRequest request){
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> params = getParams(request);
        int limit = Integer.parseInt(request.getParameter("limit"));
        int page = Integer.parseInt(request.getParameter("page"));
        params.put("limit", limit);
        params.put("page", page);

        Page<ManagerQualitativeEvalStd> managerQualitativeEvalStdDataListPerPageByParams = managerQualitativeEvalStdService.findManagerQualitativeEvalStdDataListPerPageByParams(params);
        if (managerQualitativeEvalStdDataListPerPageByParams.getTotal() == 0) {
            result.put("code", 404);
            result.put("msg", "未查出数据");
        } else {
            result.put("code", 0);
            result.put("count", managerQualitativeEvalStdDataListPerPageByParams.getTotal());
            result.put("data", managerQualitativeEvalStdDataListPerPageByParams.getRecords());
        }
        return result;
    }

    /**
     * 修改经理人绩效定性评分各项目占比
     * @param request
     * @param model
     * @return
     */
    @GetMapping("edit")
    public String edit(HttpServletRequest request, Model model) {
        String year = request.getParameter("year");
        Map<String, Object> params = new HashMap<>();
        params.put("year", year);
        ManagerQualitativeEvalStd managerQualitativeEvalStd = managerQualitativeEvalStdService.findManagerQualitativeEvalStdDataByParams(params).get(0);
        model.addAttribute("managerQualitativeEvalStd", managerQualitativeEvalStd);
        return "system/performance/manager/manager-qualitative-eval-std/manager-qualitative-eval-std-edit";
    }

    /**
     * 修改经理人绩效定性评分各项目占比
     * @param managerQualitativeEvalStd
     * @return
     */
    @PostMapping("update")
    @ResponseBody
    public SysResult update(ManagerQualitativeEvalStd managerQualitativeEvalStd){

        Map<String, Object> params = new HashMap<>();
        boolean result = managerQualitativeEvalStdService.updateManagerQualitativeEvalStdData(managerQualitativeEvalStd,params);
        if (result) {
            return SysResult.ok();
        }
        return SysResult.build(500, "总经理和非总经理占比分别要100%，请检查数据后重新尝试");
    }

    /**
     * 以主键删除分数信息
     * @param id
     * @return true/false
     */
    @DeleteMapping("{id}")
    @ResponseBody
    public SysResult delete(@PathVariable(value = "id") Integer id,Map<String, Object> params) {
        boolean result = managerQualitativeEvalStdService.deleteManagerQualitativeEvalStdDataById(id,params);
        if (result) {
            return SysResult.ok();
        }
        return SysResult.build(500, "删除数据失败");
    }


    //获取数据库里的数据
    private Map<String, Object> getParams(HttpServletRequest request){
        Map<String, Object> params = new HashMap<>();
        if (!ObjectUtils.isEmpty(request.getParameter("year"))) {
            params.put("year", request.getParameter("year"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("skillScoreR"))) {
            params.put("skillScoreR", request.getParameter("skillScoreR"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("democraticEvalScoreR"))) {
            params.put("democraticEvalScoreR", request.getParameter("democraticEvalScoreR"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("superiorEvalScoreR"))) {
            params.put("superiorEvalScoreR", request.getParameter("superiorEvalScoreR"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("auditEvalScoreR"))) {
            params.put("auditEvalScoreR", request.getParameter("auditEvalScoreR"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("financialAuditScoreR"))) {
            params.put("financialAuditScoreR", request.getParameter("financialAuditScoreR"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("operationScoreR"))) {
            params.put("operationScoreR", request.getParameter("operationScoreR"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("leadershipScoreR"))) {
            params.put("leadershipScoreR", request.getParameter("leadershipScoreR"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("investScoreR"))) {
            params.put("investScoreR", request.getParameter("investScoreR"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("workReportScoreR"))) {
            params.put("workReportScoreR", request.getParameter("workReportScoreR"));
        }
        return params;

    }

}
