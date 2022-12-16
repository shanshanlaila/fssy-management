package com.fssy.shareholder.management.service.system.operate.invest;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.pojo.system.operate.invest.TechCapacityEvaluateRes;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * **数据表名：	bs_operat_tech_capacity_evaluate	**数据表中文名：	企业研发工艺能力评价表	**业务部门：	经营管理部	**数据表作用：	记录 企业研发工艺能力年度评价项目、存在问题及改善点	**创建人创建日期：	TerryZeng 2022-12-2 服务类
 * </p>
 *
 * @author Solomon
 * @since 2022-12-06
 */
public interface TechCapacityEvaluateResService extends IService<TechCapacityEvaluateRes> {

    /**
     * 通过查询条件，查询数据(导出）
     * @param params
     * @return
     */
    List<Map<String,Object>> findTechDataByParams(Map<String, Object> params);


    /**
     * 通过查询条件 分页 查询列表
     * @param params
     * @return
     */
    Page<TechCapacityEvaluateRes> findTechCapacityEvaluateResDataListPerPageByParams(Map<String, Object> params);


    List<TechCapacityEvaluateRes> findTechCapacityEvaluateResDataByParams(Map<String, Object> params);

}
