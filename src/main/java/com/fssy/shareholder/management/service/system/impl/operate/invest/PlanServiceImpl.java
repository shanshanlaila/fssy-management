package com.fssy.shareholder.management.service.system.impl.operate.invest;

import com.fssy.shareholder.management.pojo.system.operate.invest.Plan;
import com.fssy.shareholder.management.mapper.system.operate.invest.PlanMapper;
import com.fssy.shareholder.management.service.system.operate.invest.PlanService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * *****业务部门：	经营投资科		*****数据表中文名：	投资计划表(N+3)		*****数据表名：	bs_operate_invest_plan		*****数据表作用：	各企业公司的非权益投资计划情况，按公司、年、月分开记录		*****变更记录：	时间         	变更人		变更内容	20230103	兰宇铧           	初始设计 服务实现类
 * </p>
 *
 * @author SHANSHAN
 * @since 2023-01-03
 */
@Service
public class PlanServiceImpl extends ServiceImpl<PlanMapper, Plan> implements PlanService {

}
