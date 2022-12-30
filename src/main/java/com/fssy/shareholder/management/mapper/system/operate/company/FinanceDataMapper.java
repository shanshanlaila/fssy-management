package com.fssy.shareholder.management.mapper.system.operate.company;

import com.fssy.shareholder.management.pojo.system.operate.company.FinanceData;
import com.fssy.shareholder.management.mapper.manage.common.MyBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * **数据表名：	bs_operate_company_finance_data	**数据表中文名：	企业财务基础数据(暂用于非权益投资管理)	**业务部门：	经营管理部	**数据表作用：	存放企业财务基础信息，方便在非权益投资页面计算。暂用于人工导入，未来采用数据对接	**创建人创建日期：	TerryZeng 2022-12-2 Mapper 接口
 * </p>
 *
 * @author 农浩
 * @since 2022-12-07
 */
@Mapper
public interface FinanceDataMapper extends MyBaseMapper<FinanceData> {

}
