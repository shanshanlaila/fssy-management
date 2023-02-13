/**
 * ------------------------修改日志---------------------------------
 * 修改人       修改日期         修改内容
 */
package com.fssy.shareholder.management.service.system.performance;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.service.system.performance.employee.BaseService;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author MI
 * @ClassName: PerformanceServiceUtils
 * @Description: 绩效系统通用controller
 * @date 2023/2/13 9:19
 */
public class PerformanceServiceUtils<T> {

    /**
     * 通用index显示数据
     *
     * @param params  参数map
     * @param request 分页数据
     * @param service 实习service
     */
    public void getDataResult(Map<String, Object> result,Map<String, Object> params, HttpServletRequest request, BaseService<T> service) {
        params.put("page", Integer.parseInt(request.getParameter("page")));
        params.put("limit", Integer.parseInt(request.getParameter("limit")));
        Page<T> page = service.findDataListByParams(params);
        if (page.getTotal() == 0) {
            result.put("code", 404);
            result.put("msg", "未查出数据");
        } else {
            // 查出数据，返回分页数据
            result.put("code", 0);
            result.put("count", page.getTotal());
            result.put("data", page.getRecords());
        }
    }
}
