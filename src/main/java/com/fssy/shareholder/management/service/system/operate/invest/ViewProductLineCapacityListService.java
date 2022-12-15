package com.fssy.shareholder.management.service.system.operate.invest;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fssy.shareholder.management.pojo.system.config.Attachment;
import com.fssy.shareholder.management.pojo.system.operate.invest.ProductLineCapacityMonth;
import com.fssy.shareholder.management.pojo.system.operate.invest.ViewProductLineCapacityList;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * VIEW 服务类 重点产线产能
 * </p>
 *
 * @author zzp
 * @since 2022-12-08
 */
public interface ViewProductLineCapacityListService extends IService<ViewProductLineCapacityList> {

    /**
     * 通过查询条件，查询重点产线产能 数据
     * @param params
     * @return 重点产线产能数据
     */

    List<ViewProductLineCapacityList> findViewProductLineCapacityListSDataByParams(Map<String,Object> params);

    /**
     * 通过查询条件 分页查询列表
     *
     * @param params 查询条件
     * @return 分页数据
     */
    Page<ViewProductLineCapacityList> findViewProductLineCapacityListDataListPerPageByParams(Map<String, Object> params);

    /**
     * 通过查询条件查询重点产线产能map数据，用于导出
     * @param params 查询条件
     * @return 数据列表
     */
    List<Map<String,Object>> findViewProductLineCapacityListDataByParams(Map<String, Object> params);

    /**
     * 实现导入功能 需要读取数据存入下面两张表
     * bs_operate_product_line_capacity_list
     * bs_operate_product_line_capacity_month
     * @param attachment 附件
     * @return 附件map集合
     */
    Map<String,Object> readViewProductLineCapacityListDataSource(Attachment attachment, String year, String quarter);

    /**
     * 填写存在问题及对策
     */
    public boolean updateViewProductLineCapacityListData(ProductLineCapacityMonth productLineCapacityMonth);

    /**
     * 以主键删除 选中的重点产线产能记录
     */
    public boolean deleteViewProductLineCapacityListDataById(Integer id);

    /**
     * 查询十二个月的数据，展示查询,包含条件查询
     * @param params
     * @return
     */
    Page<Map<String,Object>> findViewProductLineCapacityListDataMapListPerPageByParams(Map<String,Object> params,String quarterMark);

}
