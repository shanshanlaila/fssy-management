package com.fssy.shareholder.management.service.system.performance.manager;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.pojo.system.performance.manager.ManagerPerformanceEvalStd;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fssy.shareholder.management.pojo.system.performance.manager.ManagerQualitativeEvalStd;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * **数据表名：	bs_manager_performance_std	**数据表中文名：	经理人绩效评分定性、定量分数占比表	**业务部门：	人力资源部	**数据表作用：	记录企业年度总经理、分管经理人定性、定量分数占比表，因为该比例每年都可能变化	 服务类
 * </p>
 *
 * @author Solomon
 * @since 2022-11-29
 */
public interface ManagerPerformanceEvalStdService extends IService<ManagerPerformanceEvalStd> {

    /**
     * 通过查询条件 分页 查询列表
     * @param params
     * @return
     */
    Page<ManagerPerformanceEvalStd> findManagerPerformanceEvalStdDataListPerPageByParams(Map<String, Object> params);

    /**
     * 通过查询条件，查询数据
     * @param params
     * @return
     */
    List<ManagerPerformanceEvalStd> findManagerPerformanceEvalStdDataByParams(Map<String, Object> params);


    /**
     * 用于主键删除
     * @param id
     * @return
     */
    public boolean deleteManagerPerformanceEvalStdDataById(Integer id);


    /**
     * 修改经理人绩效评分定性、定量分数占比
     * @param managerPerformanceEvalStd
     * @return
     */
    boolean updateManagerPerformanceEvalStdData(ManagerPerformanceEvalStd managerPerformanceEvalStd);


    /**
     * 添加经理人绩效评分定性、定量分数占比信息
     * @param managerPerformanceEvalStd
     * @return
     */
    boolean insertManagerPerformanceEvalStd(ManagerPerformanceEvalStd managerPerformanceEvalStd);


}
