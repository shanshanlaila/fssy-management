package com.fssy.shareholder.management.service.system.operate.invest;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.pojo.system.config.Attachment;
import com.fssy.shareholder.management.pojo.system.operate.invest.InvestPlan;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <p>
 * *****业务部门：	经营投资科		*****数据表中文名：	投资计划表(N+3)		*****数据表名：	bs_operate_invest_plan		*****数据表作用：	各企业公司的非权益投资计划情况，按公司、年、月分开记录		*****变更记录：	时间         	变更人		变更内容	20230103	兰宇铧           	初始设计 服务类
 * </p>
 *
 * @author SHANSHAN
 * @since 2023-01-03
 */
public interface InvestPlanService extends IService<InvestPlan> {

    /**
     * 分页显示数据
     *
     * @param params 封装得参数map
     * @return 分页对象
     */
    Page<Map<String, Object>> findPlanDataByPageMap(Map<String, Object> params);

    /**
     * 读取附件数据
     *
     * @param result 附件
     * @return 导入结果
     */
    Map<String, Object> readInvestPlanDataSource(Attachment result, HttpServletRequest request);
}
