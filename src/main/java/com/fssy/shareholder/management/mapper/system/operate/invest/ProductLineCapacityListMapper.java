package com.fssy.shareholder.management.mapper.system.operate.invest;


import com.fssy.shareholder.management.mapper.manage.common.MyBaseMapper;
import com.fssy.shareholder.management.pojo.system.operate.invest.ProductLineCapacityList;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * **数据表名：	bs_operate_product_line_capacity_list	**数据表中文名：	重点产线产能数据	**业务部门：	经营管理部	**数据表作用：	记录 企业重点产线设计、SOP信息	**创建人创建日期：	TerryZeng 2022-12-2 Mapper 接口
 * </p>
 *
 * @author zzp
 * @since 2022-12-08
 *
 * 服务于ViewProductLineCapacityList
 */
@Mapper
public interface ProductLineCapacityListMapper extends MyBaseMapper<ProductLineCapacityList> {

}
