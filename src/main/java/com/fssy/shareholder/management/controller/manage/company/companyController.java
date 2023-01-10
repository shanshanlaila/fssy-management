/**
 * ------------------------修改日志---------------------------------
 * 修改人       修改日期         修改内容
 */
package com.fssy.shareholder.management.controller.manage.company;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.annotation.RequiredLog;
import com.fssy.shareholder.management.pojo.manage.company.Company;
import com.fssy.shareholder.management.service.manage.company.CompanyService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author nonghao
 * @ClassName: companyController
 * @Description: TODO
 * @date 2023/1/10 9:53
 */
@Controller
@RequestMapping("/manage/company/")
public class companyController {
    @Autowired
    private CompanyService companyService;

    /**
     * 返回公司列表页面
     *
     * @return
     */
    @RequiredLog("公司列表")
    @RequiresPermissions("manage:company:index")
    @GetMapping("index")
    public String companyIndex() {
        return "manage/company/company-list";
    }

    /**
     * 返回组公司列表页面
     *
     * @param request 请求实体
     * @return
     */
    @RequestMapping("getObjects")
    @ResponseBody
    public Map<String, Object> getObjects(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> params = getParams(request);
        params.put("page", Integer.parseInt(request.getParameter("page")));
        params.put("limit", Integer.parseInt(request.getParameter("limit")));

        Page<Company> handlersItemPage = companyService.findDataListByParams(params);
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
        if (!ObjectUtils.isEmpty(request.getParameter("note"))) {
            params.put("note", request.getParameter("note"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("name"))) {
            params.put("name", request.getParameter("name"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("shortName"))) {
            params.put("shortName", request.getParameter("shortName"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("createdAt"))) {
            params.put("createdAt", request.getParameter("createdAt"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("updatedAt"))) {
            params.put("updatedAt", request.getParameter("updatedAt"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("createdId"))) {
            params.put("createdId", request.getParameter("createdId"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("updatedId"))) {
            params.put("updatedId", request.getParameter("updatedId"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("createdName"))) {
            params.put("createdName", request.getParameter("createdName"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("updatedName"))) {
            params.put("updatedName", request.getParameter("updatedName"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("createdName"))) {
            params.put("createdName", request.getParameter("createdName"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("code"))) {
            params.put("code", request.getParameter("code"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("status"))) {
            params.put("status", request.getParameter("status"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("activeDate"))) {
            params.put("activeDate", request.getParameter("activeDate"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("inactiveDate"))) {
            params.put("inactiveDate", request.getParameter("inactiveDate"));
        }
        return params;
    }
}
