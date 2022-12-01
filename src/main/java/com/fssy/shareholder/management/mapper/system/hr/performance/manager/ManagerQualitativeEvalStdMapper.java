package com.fssy.shareholder.management.mapper.system.hr.performance.manager;

import com.fssy.shareholder.management.pojo.system.hr.performance.manager.ManagerQualitativeEvalStd;
import com.fssy.shareholder.management.mapper.manage.common.MyBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * **数据表名：	bs_manager_qualitative_eval_std	**数据表中文名：	经理人绩效定性评分各项目占比表	**业务部门：	人力资源部	**数据表作用：	记录企业年度总经理、分管经理人定性项目占比表，因为该比例每年都可能变化 Mapper 接口
 * </p>
 *
 * @author Solomon
 * @since 2022-11-28
 */
@Mapper
public interface ManagerQualitativeEvalStdMapper extends MyBaseMapper<ManagerQualitativeEvalStd> {

}
