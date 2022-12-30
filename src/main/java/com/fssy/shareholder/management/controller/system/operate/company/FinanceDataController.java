package com.fssy.shareholder.management.controller.system.operate.company;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.annotation.RequiredLog;
import com.fssy.shareholder.management.pojo.common.SysResult;
import com.fssy.shareholder.management.pojo.system.operate.company.FinanceData;
import com.fssy.shareholder.management.service.system.operate.company.FinanceDataService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * **数据表名：	bs_operate_company_finance_data	**数据表中文名：	企业财务基础数据(暂用于非权益投资管理)	**业务部门：	经营管理部	**数据表作用：	存放企业财务基础信息，方便在非权益投资页面计算。暂用于人工导入，未来采用数据对接	**创建人创建日期：	TerryZeng 2022-12-2 前端控制器
 * </p>
 *
 * @author 农浩
 * @since 2022-12-07
 */
@Controller
@RequestMapping("/system/operate/company/FinanceData/")
public class FinanceDataController {
    @Autowired
    private FinanceDataService financeDataService;
    /**
     * 企业财务基础数据管理
     * @param model
     * @return 企业财务基础数据管理页面
     */
    @GetMapping("index")
    @RequiredLog("企业财务基础数据管理")
    @RequiresPermissions("system:operate:company:FinanceData:index")
    public String showProjectList(Model model) {

        return "system/operate/company/finance-data-list";
    }
    /**
     * 返回 对象列表
     *
     * @param request 前端请求参数
     * @return Map集合
     */
    @RequestMapping("getObjects")
    @ResponseBody
    public Map<String, Object> getObjects(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> params = getParams(request);
        params.put("page", Integer.parseInt(request.getParameter("page")));
        params.put("limit", Integer.parseInt(request.getParameter("limit")));

        Page<FinanceData> handlersItemPage = financeDataService.findDataListByParams(params);
        if (handlersItemPage.getTotal() == 0) {
            result.put("code", 404);
            result.put("msg", "未查出数据");
        } else {
            // 查出数据，返回分页数据
            result.put("code", 0);
            result.put("count", handlersItemPage.getTotal());
            result.put("data", handlersItemPage.getRecords());
        }

        return result;
    }

    private Map<String, Object> getParams(HttpServletRequest request) {
        Map<String, Object> params = new HashMap<>();
        if (!ObjectUtils.isEmpty(request.getParameter("id"))) {
            params.put("id", request.getParameter("id"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("status"))) {
            params.put("status", request.getParameter("status"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("note"))) {
            params.put("note", request.getParameter("note"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("year"))) {
            params.put("year", request.getParameter("year"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("companyName"))) {
            params.put("companyName", request.getParameter("companyName"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("companyId"))) {
            params.put("companyId", request.getParameter("companyId"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("companyShortName"))) {
            params.put("companyShortName", request.getParameter("companyShortName"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("competeCompanyName"))) {
            params.put("competeCompanyName", request.getParameter("competeCompanyName"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("month"))) {
            params.put("month", request.getParameter("month"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("operatingProfit"))) {
            params.put("operatingProfit", request.getParameter("operatingProfit"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("netValueOfFixedAssets"))) {
            params.put("netValueOfFixedAssets", request.getParameter("netValueOfFixedAssets"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("status"))) {
            params.put("status", request.getParameter("status"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("status"))) {
            params.put("status", request.getParameter("status"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("status"))) {
            params.put("status", request.getParameter("status"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("status"))) {
            params.put("status", request.getParameter("status"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("status"))) {
            params.put("status", request.getParameter("status"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("status"))) {
            params.put("status", request.getParameter("status"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("status"))) {
            params.put("status", request.getParameter("status"));
        }
        return params;
    }
    /**
     * 返回新增单条企业财务基础数据页面
     *
     * @param request
     * @return
     */
    @GetMapping("create")
    public String createFinanceData(HttpServletRequest request, Model model) {
        return "system/operate/company/finance-data-create";
    }

    /**
     * 保存financeData表
     *
     * @param financeData
     * @return
     */
    @PostMapping("store")
    @RequiredLog("新增单条企业财务基础数据")
    @ResponseBody
    public SysResult Store(FinanceData financeData, HttpServletRequest request) {
        financeDataService.insertFinanceData(financeData);
        return SysResult.ok();
    }
    /**
     * 展示修改页面
     *
     * @param id   企业财务基础数据id
     * @param model 数据模型
     * @return 修改页面
     */
    @GetMapping("edit/{id}")
    public String showEditPage(@PathVariable String id, Model model) {
        FinanceData financeData = financeDataService.getById(id);
//        if (financeData.getStatus().equals(PerformanceConstant.EVENT_LIST_STATUS_CANCEL)) {
//            throw new ServiceException("不能修改取消状态下的事件请单");
//        }
        model.addAttribute("financeData", financeData);//projectList传到前端
        return "system/operate/company/finance-data-edit";
    }

    /**
     * 更新企业财务基础数据
     *
     * @param financeData 企业财务基础数据
     * @return 结果
     */
    @PostMapping("update")
    @ResponseBody
    public SysResult update(FinanceData financeData) {
        boolean result = financeDataService.updateById(financeData);
        if (result) {
            return SysResult.ok();
        }
        return SysResult.build(500, "企业财务基础数据");
    }

    /**
     * 删除企业财务基础数据
     *
     * @return 删除结果
     */
    @DeleteMapping("cancel/{id}")
    @ResponseBody
    public SysResult cancel(@PathVariable Long id) {
        boolean result = financeDataService.removeById(id);
        if (result)
            return SysResult.ok();
        return SysResult.build(500, "删除失败，请检查数据后重新提交");
    }
}
