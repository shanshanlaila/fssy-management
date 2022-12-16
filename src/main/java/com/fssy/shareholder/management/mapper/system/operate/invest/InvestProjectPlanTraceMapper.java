package com.fssy.shareholder.management.mapper.system.operate.invest;

import com.fssy.shareholder.management.pojo.system.operate.invest.InvestProjectPlanTrace;
import com.fssy.shareholder.management.mapper.manage.common.MyBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * **数据表名：	bs_operate_invest_project_plan_trace	**数据表中文名：	项目进度计划跟踪表	**业务部门：	经营管理部	**数据表作用：	用于记录单一项目进展详情及跟踪情况	**创建人创建日期：	TerryZeng 2022-12-2 Mapper 接口
 * </p>
 *
 * @author Solomon
 * @since 2022-12-09
 */
@Mapper
public interface InvestProjectPlanTraceMapper extends MyBaseMapper<InvestProjectPlanTrace> {

}
