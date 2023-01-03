package com.fssy.shareholder.management.controller.system.operate.invest;


import com.fssy.shareholder.management.service.system.operate.invest.PlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;

/**
 * <p>
 * *****业务部门：	经营投资科		*****数据表中文名：	投资计划表(N+3)		*****数据表名：	bs_operate_invest_plan		*****数据表作用：	各企业公司的非权益投资计划情况，按公司、年、月分开记录		*****变更记录：	时间         	变更人		变更内容	20230103	兰宇铧           	初始设计 前端控制器
 * </p>
 *
 * @author SHANSHAN
 * @since 2023-01-03
 */
@Controller
@RequestMapping("/system/operate/invest/plan")
public class PlanController {

    @Autowired
    private PlanService planService;



}
