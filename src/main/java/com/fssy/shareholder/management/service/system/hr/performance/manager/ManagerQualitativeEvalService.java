package com.fssy.shareholder.management.service.system.hr.performance.manager;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fssy.shareholder.management.pojo.system.config.Attachment;
import com.fssy.shareholder.management.pojo.system.hr.performance.manager.ManagerQualitativeEval;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * **数据表名：	bs_manager_qualitative_eval	**数据表中文名：	经理人绩效定性评价分数表	**业务部门：	人力资源部	**数据表作用：	记录企业年度总经理、分管经理人定性评价分数 服务类
 * </p>
 *
 * @author zzp
 * @since 2022-11-28
 */
public interface ManagerQualitativeEvalService extends IService<ManagerQualitativeEval> {

    /**
     * 通过查询条件，查询经理人定性评价 数据
     * @param params
     * @return 经理人定性评价数据
     */

    List<ManagerQualitativeEval> findManagerQualitativeEvalSDataByParams(Map<String,Object> params);

    /**
     * 通过查询条件 分页查询列表
     *
     * @param params 查询条件
     * @return 分页数据
     */
    Page<ManagerQualitativeEval> findManagerQualitativeEvalDataListPerPageByParams(Map<String, Object> params);

    /**
     * 通过查询条件查询履职计划map数据，用于导出
     * @param params 查询条件
     * @return 数据列表
     */
    List<Map<String,Object>> findManagerQualitativeEvalDataByParams(Map<String, Object> params);

    /**
     * 读取附件数据到数据库表
     * @param attachment 附件
     * @return 附件map集合
     */
    Map<String,Object> readManagerQualitativeEvalDataSource(Attachment attachment);

    /**
     * 以主键删除 分数记录
     */
    public boolean deleteManagerQualitativeEvalDataById(Integer id);

    /**
     * 修改分数记录
     */
    public boolean updateManagerQualitativeEvalData(ManagerQualitativeEval managerQualitativeEval);

    /**
     * 生成分数
     * @param
     * @return
     */
    boolean createScore (Map<String, Object> params);
}
