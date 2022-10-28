package com.fssy.shareholder.management.mapper.system.performance.employee;

import com.fssy.shareholder.management.pojo.system.performance.employee.StateRelationMainUser;
import com.fssy.shareholder.management.mapper.manage.common.MyBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * *****业务部门：	绩效科		*****数据表中文名：	员工月度评价情况关联主担用户关系表		*****数据表名：	bs_performance_state_relation_main_user		*****数据表作用：	员工月度评价情况关联主担用户关系		*****变更记录：	时间         	变更人		变更内容	20220915	兰宇铧           	初始设计 Mapper 接口
 * </p>
 *
 * @author shanshan
 * @since 2022-10-28
 */
@Mapper
public interface StateRelationMainUserMapper extends MyBaseMapper<StateRelationMainUser> {

}
