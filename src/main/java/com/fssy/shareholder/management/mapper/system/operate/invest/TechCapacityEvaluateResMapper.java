package com.fssy.shareholder.management.mapper.system.operate.invest;

import com.fssy.shareholder.management.pojo.system.operate.invest.TechCapacityEvaluateRes;
import com.fssy.shareholder.management.mapper.manage.common.MyBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * **数据表名：	bs_operat_tech_capacity_evaluate	**数据表中文名：	企业研发工艺能力评价表	**业务部门：	经营管理部	**数据表作用：	记录 企业研发工艺能力年度评价项目、存在问题及改善点	**创建人创建日期：	TerryZeng 2022-12-2 Mapper 接口
 * </p>
 *
 * @author Solomon
 * @since 2022-12-06
 */
@Mapper
public interface TechCapacityEvaluateResMapper extends MyBaseMapper<TechCapacityEvaluateRes> {

}
