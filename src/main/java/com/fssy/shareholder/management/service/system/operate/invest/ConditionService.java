package com.fssy.shareholder.management.service.system.operate.invest;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fssy.shareholder.management.pojo.system.config.Attachment;
import com.fssy.shareholder.management.pojo.system.operate.invest.Condition;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <p>
 * *****业务部门：	经营投资科		*****数据表中文名：	投资执行情况表		*****数据表名：	bs_operate_invest_condition		*****数据表作用：	各企业公司的非权益投资执行情况，按公司、年、月分开记录		*****变更记录：	时间         	变更人		变更内容	20230103	兰宇铧           	初始设计 服务类
 * </p>
 *
 * @author 农浩
 * @since 2023-01-03
 */
public interface ConditionService extends IService<Condition> {
    /**
     * 根据参数分页查询数据
     * @param params 查询参数
     * @return 数据分页
     */
    Page<Condition> findDataListByParams(Map<String, Object> params);
    /**
     * 读取附件数据到数据库表
     * @param attachment 附件
     * @return 附件
     */
    Map<String, Object> readConditionDataSource(Attachment attachment,HttpServletRequest request);

    boolean updateInvestConditionData(Condition condition, HttpServletRequest request);
}
