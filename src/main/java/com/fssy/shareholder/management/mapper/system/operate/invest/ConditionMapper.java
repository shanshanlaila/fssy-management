package com.fssy.shareholder.management.mapper.system.operate.invest;

import com.fssy.shareholder.management.pojo.system.operate.invest.Condition;
import com.fssy.shareholder.management.mapper.manage.common.MyBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * *****业务部门：	经营投资科		*****数据表中文名：	投资执行情况表		*****数据表名：	bs_operate_invest_condition		*****数据表作用：	各企业公司的非权益投资执行情况，按公司、年、月分开记录		*****变更记录：	时间         	变更人		变更内容	20230103	兰宇铧           	初始设计 Mapper 接口
 * </p>
 *
 * @author 农浩
 * @since 2023-01-03
 */
@Mapper
public interface ConditionMapper extends MyBaseMapper<Condition> {

}
