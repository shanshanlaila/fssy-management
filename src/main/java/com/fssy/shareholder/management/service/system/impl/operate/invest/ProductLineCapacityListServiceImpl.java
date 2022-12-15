package com.fssy.shareholder.management.service.system.impl.operate.invest;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fssy.shareholder.management.mapper.system.operate.invest.ProductLineCapacityListMapper;
import com.fssy.shareholder.management.pojo.system.operate.invest.ProductLineCapacityList;
import com.fssy.shareholder.management.service.system.operate.invest.ProductLineCapacityListService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * **数据表名：	bs_operate_product_line_capacity_list	**数据表中文名：	重点产线产能数据	**业务部门：	经营管理部	**数据表作用：	记录 企业重点产线设计、SOP信息	**创建人创建日期：	TerryZeng 2022-12-2 服务实现类
 * </p>
 *
 * @author zzp
 * @since 2022-12-08
 *
 * 服务于ViewProductLineCapacityList
 */
@Service
public class ProductLineCapacityListServiceImpl extends ServiceImpl<ProductLineCapacityListMapper, ProductLineCapacityList> implements ProductLineCapacityListService {

}
