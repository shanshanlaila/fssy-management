/**
 * ------------------------修改日志---------------------------------
 * 修改人       修改日期         修改内容
 */
package com.fssy.shareholder.management.service.system.impl.performance.manage_kpi;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fssy.shareholder.management.mapper.system.performance.manage_kpi.ManagerKpiScoreMapperOld;
import com.fssy.shareholder.management.pojo.system.performance.manage_kpi.ManagerKpiScoreOld;
import com.fssy.shareholder.management.service.common.SheetService;
import com.fssy.shareholder.management.service.system.performance.manage_kpi.ManagerKpiMonthScoreOldService;
import com.fssy.shareholder.management.service.system.performance.manage_kpi.ManagerKpiScoreServiceOld;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author 张泽鹏
 * @ClassName: ManagerKpiMonthScoreOldServiceImpl
 * @Description: TODO
 * @date 2022-11-18 15:36
 */
@Service
public class ManagerKpiMonthScoreOldServiceImpl extends ServiceImpl<ManagerKpiScoreMapperOld, ManagerKpiScoreOld> implements ManagerKpiMonthScoreOldService{

    @Autowired
    private ManagerKpiScoreMapperOld managerKpiScoreMapper;

    /**
     * 查询一年十二个月的数据，展示查询，包含条件查询
     * @param params
     * @return
     */
    @Override
    public Page<Map<String, Object>> findManageKpiMonthScoreOldDataMapListPerPageByParams(Map<String, Object> params) {
        QueryWrapper<ManagerKpiScoreOld> queryWrapper = getQueryWrapper(params);
        int limit = (int)params.get("limit");
        int page = (int)params.get("page");
        Page<Map<String,Object>> myPage = new Page<>(page, limit);
        return managerKpiScoreMapper.selectMapsPage(myPage, queryWrapper);
    }

    /**
     * 传入参数，对参数进行处理(查询条件)
     * @param params
     * @return
     */
    private QueryWrapper<ManagerKpiScoreOld> getQueryWrapper(Map<String, Object> params) {
        QueryWrapper<ManagerKpiScoreOld> queryWrapper = new QueryWrapper<>();
        int month = 1;
        StringBuilder selectStr = new StringBuilder("id,companyName,managerName,position,year,scoreAdjust");
        do{
            selectStr.append(", sum(if(MONTH =" +  month + ",scoreAdjust,null)) AS 'month" + month + "'");
            month++;
        }while (month <= 12);
        queryWrapper.select(selectStr.toString()).groupBy("managerName,companyName,year");
        if (params.containsKey("select")) {
            queryWrapper.select((String) params.get("select"));
        }
        if (params.containsKey("managerName")) {
            queryWrapper.eq("managerName", params.get("managerName"));
        }
        if (params.containsKey("companyName")) {
            queryWrapper.like("companyName", params.get("companyName"));
        }
        if (params.containsKey("generalManager")) {
            queryWrapper.like("generalManager", params.get("generalManager"));
        }
        if (params.containsKey("year")) {
            queryWrapper.eq("year", params.get("year"));
        }
        if (params.containsKey("month")) {
            queryWrapper.eq("month", params.get("month"));
        }
        return queryWrapper;
    }
}