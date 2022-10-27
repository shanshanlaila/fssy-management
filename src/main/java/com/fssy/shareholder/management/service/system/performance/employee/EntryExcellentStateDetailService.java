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
     * 评优材料撤销审核
     * @param excellentStateDetailIds
     * @return
     */
    boolean retreat(List<String> excellentStateDetailIds);

    /**
     * 修改更新提交评优材料
     * @param entryExcellentStateDetail
     * @return
     */
    boolean updateEntryExcellentStateDetail(EntryExcellentStateDetail entryExcellentStateDetail);
}
