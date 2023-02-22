package com.fssy.shareholder.management.service.system.performance.employee;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.pojo.system.config.Attachment;
import com.fssy.shareholder.management.pojo.system.performance.employee.EntryCasReviewDetail;

import javax.servlet.http.HttpServletRequest;
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
public interface EntryCasReviewDetailService extends BaseService<EntryCasReviewDetail> {

    /**
     * 履职总结-部长审核（单条）
     *
     * @param entryCasReviewDetail 总结
     * @return 结果
     */
    boolean updateEntryCasReviewDetail(EntryCasReviewDetail entryCasReviewDetail, HttpServletRequest request);

    /**
     * 履职总结提交审核
     *
     * @param reviewDetailIds 总结ids
     * @return 审核结果
     */
    boolean submitAuditForReview(List<String> reviewDetailIds);

    /**
     * 总结撤销审核
     *
     * @param reviewDetailIds 总结dis
     * @param identification  区分科长和部长操作的标志
     * @return 结果
     */
    boolean retreatForReview(List<String> reviewDetailIds, String identification);

    /**
     * 履职总结-科长单条审核
     *
     * @param entryCasReviewDetail 总结履职
     * @return 结果
     */
    boolean sectionAudit(EntryCasReviewDetail entryCasReviewDetail);

    /**
     * 读取附件数据到数据库表
     *
     * @param attachment 履职计划总结表附件
     * @return 履职管控计划总结表附件map集合
     */
    Map<String, Object> readEntryCasReviewDetailDataSource(Attachment attachment);


    /**
     * 批量审核-计划工作完成情况评价（部长）
     *
     * @param entryReviewDetailIds 履职总结的dis
     * @param ministerReview       部长复核
     * @return 审核结果
     */
    boolean batchAudit(List<String> entryReviewDetailIds, String ministerReview, List<String> auditNotes);

    /**
     * 履职总结-科长审核
     *
     * @param entryReviewDetailIds           履职总结的Ids
     * @param chargeTransactionEvaluateLevel 事务类评价等级
     * @param chargeTransactionBelowType     事务类评价不同类型
     * @return 审核结果
     */
    boolean batchAudit(List<String> entryReviewDetailIds, String chargeTransactionEvaluateLevel, String chargeTransactionBelowType, List<String> auditNotes);

    /**
     * 新增单履职总结
     *
     * @param entryCasReviewDetail
     * @return
     */
    boolean saveOneReviewDetail(EntryCasReviewDetail entryCasReviewDetail);

    /**
     * 不根据计划创建履职总结
     *
     * @param entryCasReviewDetail 总结
     * @return 结果
     */
    Boolean storeReviewNotPlan(EntryCasReviewDetail entryCasReviewDetail);

    Page<Map<String, Object>> findDataListByMapParams(Map<String, Object> params);

    /**
     * 查找履职总结通知
     *
     * @return map
     */
    Map<Long, Map<String, Object>> findWeChatNoticeMap();
}
