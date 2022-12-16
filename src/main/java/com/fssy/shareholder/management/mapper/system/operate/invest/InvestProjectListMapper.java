package com.fssy.shareholder.management.mapper.system.operate.invest;

import com.fssy.shareholder.management.pojo.system.operate.invest.InvestProjectList;
import com.fssy.shareholder.management.mapper.manage.common.MyBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * **数据表名：	bs_operate_invest_project_list	**数据表中文名：	年度投资项目清单	**业务部门：	经营管理部	**数据表作用：	记录 企业年度投资项目清单	**创建人创建日期：	TerryZeng 2022-12-2 Mapper 接口
 * </p>
 *
 * @author Solomon
 * @since 2022-12-09
 */
@Mapper
public interface InvestProjectListMapper extends MyBaseMapper<InvestProjectList> {

}
