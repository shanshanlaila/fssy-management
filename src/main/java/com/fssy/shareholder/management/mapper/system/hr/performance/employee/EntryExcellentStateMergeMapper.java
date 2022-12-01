package com.fssy.shareholder.management.mapper.system.hr.performance.employee;

import com.fssy.shareholder.management.pojo.system.hr.performance.employee.EntryExcellentStateMerge;
import com.fssy.shareholder.management.mapper.manage.common.MyBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * *****业务部门：	绩效科		*****数据表中文名：	员工月度履职评价说明表		*****数据表名：	bs_performance_entry_excellent_state_merge		*****数据表作用：	员工月度履职评价为优时，填报的表		*****变更记录：	时间         	变更人		变更内容	20220915	兰宇铧           	初始设计 Mapper 接口
 * </p>
 *
 * @author shanshan
 * @since 2022-10-28
 */
@Mapper
public interface EntryExcellentStateMergeMapper extends MyBaseMapper<EntryExcellentStateMerge> {

}
