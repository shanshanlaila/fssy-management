package com.fssy.shareholder.management.controller.system.performance.manager;



import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.pojo.common.SysResult;
import com.fssy.shareholder.management.pojo.system.performance.manager.ManagerPerformanceEvalStd;
import com.fssy.shareholder.management.service.manage.company.CompanyService;
import com.fssy.shareholder.management.service.system.performance.manager.ManagerPerformanceEvalStdService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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
 * **数据表名：	bs_manager_performance_std	**数据表中文名：	经理人绩效评分定性、定量分数占比表	**业务部门：	人力资源部	**数据表作用：	记录企业年度总经理、分管经理人定性、定量分数占比表，因为该比例每年都可能变化	 前端控制器
 * </p>
 *
 * @author Solomon
 * @since 2022-11-29
 */
@Controller
@RequestMapping("/system/performance/manager/manager-performance-eval-std")
@RequiresPermissions("system:performance:manager:manager:performance:eval:std:index1")
public class ManagerPerformanceEvalStdController {

    @Autowired
    private ManagerPerformanceEvalStdService managerPerformanceEvalStdService;

    @Autowired
    private CompanyService companyService;



    /**
     * 经理人绩效评分定性、定量分数占比管理页面
     * @param model
     * @return
     */
    @GetMapping("index3")
    public String manageIndex(Model model){
        Map<String, Object> params = new HashMap<>();
        List<Map<String, Object>> companyNameList = companyService.findCompanySelectedDataListByParams(params, new ArrayList<>());
        model.addAttribute("companyNameList",companyNameList);
        return "system/performance/manager/manager-performance-eval-std/manager-performance-eval-std-list";
    }


    /**
     * 添加经理人绩效评分定性、定量分数占比管理页
     * @param model
     * @return
     */
    @GetMapping("create")
    public String create(Model model){
        return "system/performance/manager/manager-performance-eval-std/manager-performance-eval-std-create";
    }

    /**
     * 添加经经理人绩效评分定性、定量分数占比信息
     * @param managerPerformanceEvalStd
     * @param request
     * @return
     */
    @RequestMapping("store")
    @ResponseBody
    public SysResult storemanagerPerformanceEvalStd(ManagerPerformanceEvalStd managerPerformanceEvalStd, HttpServletRequest request){
        boolean result = managerPerformanceEvalStdService.insertManagerPerformanceEvalStd(managerPerformanceEvalStd);
        if (result) {
            return SysResult.ok();
        }
        return SysResult.build(500, "定量、定性评价占比要为100%，请检查后重试");
    }

    /**
     * 修改经经理人绩效评分定性、定量分数占比
     * @param request
     * @param model
     * @return
     */
    @GetMapping("edit")
    public String edit(HttpServletRequest request, Model model) {
        String id = request.getParameter("id");
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        ManagerPerformanceEvalStd managerPerformanceEvalStd = managerPerformanceEvalStdService.findManagerPerformanceEvalStdDataByParams(params).get(0);
        model.addAttribute("managerPerformanceEvalStd",managerPerformanceEvalStd);
        return "system/performance/manager/manager-performance-eval-std/manager-performance-eval-std-edit";
    }

    /**
     * 修改经理人绩效定性评分各项目占比
     * @param managerPerformanceEvalStd
     * @return
     */
    @PostMapping("update")
    @ResponseBody
    public SysResult update(ManagerPerformanceEvalStd managerPerformanceEvalStd){
        Map<String, Object> params = new HashMap<>();
        boolean result = managerPerformanceEvalStdService.updateManagerPerformanceEvalStdData(managerPerformanceEvalStd,params);
        if (result) {
            return SysResult.ok();
        }
        return SysResult.build(500, "定量、定性评价占比要为100%，请检查后重试");

    }

    /**
     * 以主键删除分数信息
     * @param id
     * @return true/false
     */
    @DeleteMapping("{id}")
    @ResponseBody
    public SysResult delete(@PathVariable(value = "id") Integer id,Map<String, Object> params) {
        boolean result = managerPerformanceEvalStdService.deleteManagerPerformanceEvalStdDataById(id,params);
        if (result) {
            return SysResult.ok();
        }
        return SysResult.build(500, "删除数据失败");
    }


    /**
     * 返回经理人绩效定性评分各项目占比页面
     * @param request
     * @return
     */
    @GetMapping("getYearScore")
    @ResponseBody
    public Map<String,Object> getManagerPerformanceEvalStdDatas(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> params = getParams(request);
        int limit = Integer.parseInt(request.getParameter("limit"));
        int page = Integer.parseInt(request.getParameter("page"));
        params.put("limit", limit);
        params.put("page", page);
        Page<ManagerPerformanceEvalStd> managerPerformanceEvalStdDataListPerPageByParams = managerPerformanceEvalStdService.findManagerPerformanceEvalStdDataListPerPageByParams(params);

        if (managerPerformanceEvalStdDataListPerPageByParams.getTotal() == 0) {
            result.put("code", 404);
            result.put("msg", "未查出数据");
        } else {
            result.put("code", 0);
            result.put("count", managerPerformanceEvalStdDataListPerPageByParams.getTotal());
            result.put("data", managerPerformanceEvalStdDataListPerPageByParams.getRecords());
        }
        return result;
    }

    private Map<String, Object> getParams(HttpServletRequest request){
        Map<String, Object> params = new HashMap<>();
        if (!ObjectUtils.isEmpty(request.getParameter("year"))) {
            params.put("year", request.getParameter("year"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("id"))) {
            params.put("id", request.getParameter("id"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("kpiScoreR"))) {
            params.put("kpiScoreR", request.getParameter("kpiScoreR"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("qualitativeScoreR"))) {
            params.put("qualitativeScoreR", request.getParameter("qualitativeScoreR"));
        }
        return params;
    }
}
