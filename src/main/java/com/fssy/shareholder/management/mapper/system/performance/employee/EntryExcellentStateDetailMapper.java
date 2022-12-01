package com.fssy.shareholder.management.mapper.system.performance.employee;

import com.fssy.shareholder.management.pojo.system.performance.employee.EntryExcellentStateDetail;
import com.fssy.shareholder.management.mapper.manage.common.MyBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * *****业务部门：	绩效科		*****数据表中文名：	员工月度履职评价说明表明细		*****数据表名：	bs_performance_entry_excellent_state_detail		*****数据表作用：	员工月度履职评价为优时，填报的内容		*****变更记录：	时间         	变更人		变更内容	20220915	兰宇铧           	初始设计 Mapper 接口
 * </p>
 *
 * @author 农浩
 * @since 2022-10-24
 */
@Mapper
public interface EntryExcellentStateDetailMapper extends MyBaseMapper<EntryExcellentStateDetail> {

}
