package com.fssy.shareholder.management.mapper.system.operate.invest;


import com.fssy.shareholder.management.mapper.manage.common.MyBaseMapper;
import com.fssy.shareholder.management.pojo.system.operate.invest.InvestProjectMonth;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * **数据表名：	bs_operate_invest_project_month	**数据表中文名：	项目月度进展表	**业务部门：	经营管理部	**数据表作用：	记录 企业年度投资项目月度状态	**创建人创建日期：	TerryZeng 2022-12-2 Mapper 接口
 * </p>
 *
 * @author zzp
 * @since 2022-12-05
 */
@Mapper
public interface InvestProjectMonthMapper extends MyBaseMapper<InvestProjectMonth> {

}
