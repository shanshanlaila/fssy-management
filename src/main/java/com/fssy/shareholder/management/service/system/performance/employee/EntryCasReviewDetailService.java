package com.fssy.shareholder.management.service.system.performance.employee;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fssy.shareholder.management.pojo.system.performance.employee.EntryCasReviewDetail;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * *****业务部门：	绩效科		*****数据表中文名：	员工履职回顾明细		*****数据表名：	bs_performance_employee_entry_cas_review_detail		*****数据表作用：	员工每月月末对月初计划的回顾		*****变更记录：	时间         	变更人		变更内容	20220915	兰宇铧           	初始设计 服务类
 * </p>
 *
 * @author 农浩
 * @since 2022-10-20
 */
public interface EntryCasReviewDetailService extends IService<EntryCasReviewDetail> {
    /**
     * 根据参数分页查询数据
     *
     * @param params 查询参数
     * @return 数据分页
     */
    Page<EntryCasReviewDetail> findDataListByParams(Map<String, Object> params);

    /**
     * 工作计划完成情况审核评价 （部 门 领导、非事务）
     *
     * @param entryCasReviewDetail
     * @return
     */
    boolean updateEntryCasReviewDetail(EntryCasReviewDetail entryCasReviewDetail);

    /**
     * 工作计划完成情况提交审核
     *
     * @param planDetailIds
     * @return
     */
    boolean submitAudit(List<String> planDetailIds);

    /**
     * 工作计划完成情况撤销审核
     *
     * @param planDetailIds
     * @return
     */
    boolean retreat(List<String> planDetailIds);

    /**
     * 工作计划完成情况审核评价 （科长，事务类）
     *
     * @param entryCasReviewDetail 回顾履职
     * @return 结果
     */
    boolean sectionWorkAudit(EntryCasReviewDetail entryCasReviewDetail);


}
