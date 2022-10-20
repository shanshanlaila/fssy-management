/**
 * ------------------------修改日志---------------------------------
 * 修改人       修改日期         修改内容
 * 伍坚山      2022-10-11      添加事件清单状态常量
 */
package com.fssy.shareholder.management.tools.constant;

/**
 * @author MI
 * @ClassName: PerformanceConstant
 * @Description: 绩效常量
 * @date 2022/10/9 15:59
 */
public interface PerformanceConstant {
    String EVENT_LIST_STATUS_FINAL = "完结";
    String EVENT_LIST_STATUS_WAIT = "待填报标准";
    String EVENT_LIST_STATUS_VALUE = "待填报价值";
    String EVENT_LIST_STATUS_CANCEL = "取消";
    String ENTRY_CAS_PLAN_DETAIL_STATUS_REVIEW="待填报回顾";
    String PLAN_DETAIL_STATUS_SUBMIT_AUDIT="待提交审核";
    String PLAN_DETAIL_STATUS_AUDIT_MINISTER="待部长审核";
    String PLAN_DETAIL_STATUS_AUDIT_KEZHANG="待科长审核";
    /**
     * 部长审核非事务类通过
     */
    String PLAN_DETAIL_STATUS_AUDIT_MINISTER_SUCCESS ="部长审核通过";
    /**
     * 部长审核非事务类拒绝
     */
    String PLAN_DETAIL_STATUS_AUDIT_MINISTER_ERROR ="部长审核拒绝";

    String PLAN_DETAIL_STATUS_AUDIT_KEZHANG_SUCCESS = "科长审核通过";
    String PLAN_DETAIL_STATUS_AUDIT_KEZHANG_ERROR="科长审核拒绝";
}
