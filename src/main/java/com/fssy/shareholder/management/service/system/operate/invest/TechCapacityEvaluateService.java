package com.fssy.shareholder.management.service.system.operate.invest;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.pojo.system.config.Attachment;
import com.fssy.shareholder.management.pojo.system.operate.invest.TechCapacityEvaluate;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * **数据表名：	bs_operat_tech_capacity_evaluate	**数据表中文名：	企业研发工艺能力评价表	**业务部门：	经营管理部	**数据表作用：	记录 企业研发工艺能力年度评价项目、存在问题及改善点	**创建人创建日期：	TerryZeng 2022-12-2 服务类
 * </p>
 *
 * @author Solomon
 * @since 2022-12-02
 */

public interface TechCapacityEvaluateService extends IService<TechCapacityEvaluate> {

    /**
     * 通过查询条件，查询数据
     * @return
     */
    List<TechCapacityEvaluate> findOperateTechCapacityEvaluateDataByParams(Map<String, Object> params);



    /**
     * 通过查询条件 分页 查询列表
     * @param params
     * @return
     */
    Page<TechCapacityEvaluate> findOperateTechCapacityEvaluateDataListPerPageByParams(Map<String, Object> params);


    /**
     * 通过查询条件，查询数据(导出）
     * @param params
     * @return
     */
    List<Map<String,Object>> findOperateDataByParams(Map<String, Object> params);


    /**
     * 用于主键删除
     * @param id
     * @return
     */
    public boolean deleteOperateTechCapacityEvaluateDataById(Integer id);


    /**
     * 修改企业研发工艺能力评价信息
     * @param operateTechCapacityEvaluate
     * @param
     * @return
     */
    boolean updateOperateTechCapacityEvaluateData(TechCapacityEvaluate operateTechCapacityEvaluate);


    /**
     * 读取附件数据到数据库
     * @param attachment
     * @return
     */
    Map<String,Object> reaadOperateTechCapacityEvaluateDataSource(Attachment attachment,String year,String companyName);


    boolean insertOperateTechCapacityEvaluate(TechCapacityEvaluate operateTechCapacityEvaluate);
}
