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
     * @param params 查询参数
     * @return 数据分页
     */
    Page<EntryExcellentStateDetail> findDataListByParams(Map<String, Object> params);

    /**
     * 绩效科撤销审核评优材料
     * @param excellentStateDetailIds 履职回顾的Ids
     * @return 撤销成功
     */
    boolean PerformanceRetreat(List<String> excellentStateDetailIds);

    /**
     * 绩效科审核评优材料（修改按钮）
     * @param entryExcellentStateDetail 履职回顾
     * @return 提交成功
     */
    boolean updateEntryExcellentStateDetail(EntryExcellentStateDetail entryExcellentStateDetail);

    /**
     * 评优材料提交审核
     * @param excellentStateDetailIds 履职回顾的Ids
     * @return 提交成功
     */
    boolean submitAudit(List<String> excellentStateDetailIds);

    /**
     * 评优材料撤销审核
     * @param excellentStateDetailIds 履职回顾的Ids
     * @return 撤销成功
     */
    boolean retreat(List<String> excellentStateDetailIds);

    /**
     * 经营管理部主管审核评优材料（修改按钮）
     * @param entryExcellentStateDetail 履职回顾表
     * @return 提交成功
     */

    boolean updateMinister(EntryExcellentStateDetail entryExcellentStateDetail);

    /**
     * 经营管理部主管撤销审核评优材料
     * @param excellentStateDetailIds 履职回顾的Ids
     * @return 撤销成功
     */
    boolean MinisterRetreat(List<String> excellentStateDetailIds);
}
