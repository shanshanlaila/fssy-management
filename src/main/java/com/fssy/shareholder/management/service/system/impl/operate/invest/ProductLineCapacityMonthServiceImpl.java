package com.fssy.shareholder.management.service.system.impl.operate.invest;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fssy.shareholder.management.mapper.system.operate.invest.ProductLineCapacityMonthMapper;
import com.fssy.shareholder.management.pojo.system.operate.invest.ProductLineCapacityMonth;
import com.fssy.shareholder.management.service.system.operate.invest.ProductLineCapacityMonthService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * **数据表名：	bs_operat_tech_capacity_evaluate	**数据表中文名：	企业研发工艺能力评价表	**业务部门：	经营管理部	**数据表作用：	记录 企业研发工艺能力年度评价项目、存在问题及改善点	**创建人创建日期：	TerryZeng 2022-12-2 服务实现类
 * </p>
 *
 * @author zzp
 * @since 2022-12-08
 *
 * 服务于ViewProductLineCapacityList
 */
@Service
public class ProductLineCapacityMonthServiceImpl extends ServiceImpl<ProductLineCapacityMonthMapper, ProductLineCapacityMonth> implements ProductLineCapacityMonthService {

}
