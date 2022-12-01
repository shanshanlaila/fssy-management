/**
 * ------------------------修改日志---------------------------------
 * 修改人       修改日期         修改内容
 */
package com.fssy.shareholder.management.service.system.hr.performance.manage_kpi;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fssy.shareholder.management.pojo.system.hr.performance.manage_kpi.ManagerKpiScoreOld;

import java.util.Map;

/**
 * @author 张泽鹏
 * @ClassName: ManagerKpiMonthScoreOldService
 * @Description: TODO
 * @date 2022-11-18 15:35
 */
public interface ManagerKpiMonthScoreOldService extends IService<ManagerKpiScoreOld> {

    /**
     * 查询一年十二个月的数据，展示查询,包含条件查询
     * @param params
     * @return
     */
    Page<Map<String,Object>> findManageKpiMonthScoreOldDataMapListPerPageByParams(Map<String,Object> params);
}
