package com.fssy.shareholder.management.service.system.performance.employee;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fssy.shareholder.management.pojo.system.performance.employee.EntryExcellentStateDetail;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * *****业务部门：	绩效科		*****数据表中文名：	员工月度履职评价说明表明细		*****数据表名：	bs_performance_entry_excellent_state_detail		*****数据表作用：	员工月度履职评价为优时，填报的内容		*****变更记录：	时间         	变更人		变更内容	20220915	兰宇铧           	初始设计 服务类
 * </p>
 *
 * @author 农浩
 * @since 2022-10-24
 */
public interface EntryExcellentStateDetailService extends IService<EntryExcellentStateDetail> {
    /**
     * 根据参数分页查询数据
     *
     * @param params 查询参数
     * @return 数据分页
     */
    Page<EntryExcellentStateDetail> findDataListByParams(Map<String, Object> params);

    /**
     * 绩效科撤销审核评优材料
     *
     * @param excellentStateDetailIds 履职总结的Ids
     * @return 撤销成功
     */
    boolean PerformanceRetreat(List<String> excellentStateDetailIds);

    /**
     * 绩效科审核评优材料（修改按钮）
     *
     * @param entryExcellentStateDetail 履职总结
     * @return 提交成功
     */
    boolean updateExcellent(EntryExcellentStateDetail entryExcellentStateDetail, String mainIdsStr, String nextIdsStr);

    /**
     * 评优材料提交审核
     *
     * @param excellentStateDetailIds 履职总结的Ids
     * @return 提交成功
     */
    boolean submitAudit(List<String> excellentStateDetailIds);

    /**
     * 评优材料撤销审核
     *
     * @param excellentStateDetailIds 履职总结的Ids
     * @return 撤销成功
     */
    boolean retreat(List<String> excellentStateDetailIds);

    /**
     * 经营管理部主管审核评优材料（修改按钮）
     *
     * @param entryExcellentStateDetail 履职总结表
     * @return 提交成功
     */

    boolean updateMinister(EntryExcellentStateDetail entryExcellentStateDetail);

    /**
     * 经营管理部主管撤销审核评优材料
     *
     * @param excellentStateDetailIds 履职总结的Ids
     * @return 撤销成功
     */
    boolean MinisterRetreat(List<String> excellentStateDetailIds);

    /**
     * 创建履职评价说明明细
     *
     * @param entryExcellentStateDetail 履职评价说明明细
     * @param param 参数map
     * @return 创建结果
     */
    boolean save(EntryExcellentStateDetail entryExcellentStateDetail, Map<String, Object> param);

    /**
     * 绩效科评优材料批量审核
     *
     * @param excellentStateDetailIds 评优ids
     * @param classReview             总结
     * @return 结果
     */
    boolean batchAudit(List<String> excellentStateDetailIds, String classReview, List<String> auditNotes);

    /**
     * 经营管理部主管评优材料批量审核
     *
     * @param excellentStateDetailIds 评优ids
     * @param ministerReview          部长审核结果
     * @return 结果
     */

    boolean MinisterBatchAudit(List<String> excellentStateDetailIds, String ministerReview, List<String> auditNotes);


    /**
     * 根据参数分页查询数据(map)
     *
     * @param params 查询参数
     * @return 数据分页
     */
    Page<Map<String, Object>> findDataMapPageListByParams(Map<String, Object> params);

    /**
     * 根据参数查询数据
     *
     * @param params 查询参数
     * @return 数据列表
     */
    List<EntryExcellentStateDetail> findListByParams(Map<String, Object> params);
    
    /**
     * 根据参数查询数据(map)
     *
     * @param params 查询参数
     * @return 数据列表
     */
    List<Map<String, Object>> findDataMapListByParams(Map<String, Object> params);
}
