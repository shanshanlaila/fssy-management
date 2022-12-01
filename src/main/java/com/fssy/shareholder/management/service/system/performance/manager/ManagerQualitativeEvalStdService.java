package com.fssy.shareholder.management.service.system.performance.manager;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.fssy.shareholder.management.pojo.system.performance.manager.ManagerQualitativeEvalStd;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * **数据表名：	bs_manager_qualitative_eval_std	**数据表中文名：	经理人绩效定性评分各项目占比表	**业务部门：	人力资源部	**数据表作用：	记录企业年度总经理、分管经理人定性项目占比表，因为该比例每年都可能变化 服务类
 * </p>
 *
 * @author Solomon
 * @since 2022-11-28
 */
public interface ManagerQualitativeEvalStdService extends IService<ManagerQualitativeEvalStd> {

    /**
     * 通过查询条件 分页 查询列表
     * @param params
     * @return
     */
    Page<ManagerQualitativeEvalStd> findManagerQualitativeEvalStdDataListPerPageByParams(Map<String, Object> params);




    /**
     * 通过查询条件，查询数据
     * @param params
     * @return
     */
    List<ManagerQualitativeEvalStd> findManagerQualitativeEvalStdDataByParams(Map<String, Object> params);


    /**
     * 用于主键删除
     * @param id
     * @return
     */
    public boolean deleteManagerQualitativeEvalStdDataById(Integer id,Map<String, Object> params);


    /**
     * 修改经理人绩效定性评分各项目占比
     * @param managerQualitativeEvalStd
     * @return
     */
    boolean updateManagerQualitativeEvalStdData(ManagerQualitativeEvalStd managerQualitativeEvalStd,Map<String, Object> params);


    /**
     * 添加经理人绩效定性评分各项目占比信息
     * @param managerQualitativeEvalStd
     * @return
     */
    boolean insertManagerQualitativeEvalStd(ManagerQualitativeEvalStd managerQualitativeEvalStd);
}
