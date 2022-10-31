/**
 * ------------------------修改日志---------------------------------
 * 修改人       修改日期         修改内容
 */
package com.fssy.shareholder.management.service.system.manage;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fssy.shareholder.management.pojo.system.manage.ManageKpiMonthAim;

import java.util.List;
import java.util.Map;

/**
 * @author Shizn
 * @ClassName: ManageKpiMonthScoreService
 * @Description: 经营管理月度项目分数管理 服务类
 * @date 2022/10/27 0027 17:04
 */
public interface ManageKpiMonthScoreService extends IService<ManageKpiMonthAim> {
    /**
     * 通过查询条件 分页查询列表
     *
     * @param params 查询条件
     * @return 分页数据
     */
    Page<ManageKpiMonthAim> findManageKpiMonthScoreDataListPerPageByParams(Map<String, Object> params);

    /**
     * 通过查询条件查询履职计划map数据， 用于导出
     *
     * @param params 查询条件
     * @return 数据列表
     */
    List<Map<String, Object>> findManageKpiMonthScoreMapDataByParams(Map<String, Object> params);

    /**
     * 根据条件查询所有数据
     * @param params
     * @return
     */
    List<ManageKpiMonthAim> findManageKpiMonthScoreDataByParams(Map<String,Object> params);

    /**
     * 生成分数
     * @param
     * @return
     */
   boolean createScore (Map<String, Object> params);

}