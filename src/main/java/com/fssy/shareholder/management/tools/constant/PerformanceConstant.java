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
    String PLAN_DETAIL_STATUS_AUDIT_ZHUGUAN="待主管审核";
    String PLAN_DETAIL_STATUS_AUDIT_HR="待人力资源部审核";
    String REVIEW_DETAIL_MINISTER_REVIEW_EXCELLENT="优";
    String REVIEW_DETAIL_STATUS_AUDIT_A="待经营管理部审核";
    String REVIEW_DETAIL_STATUS_AUDIT_A_ZHUGUAN="待经营管理部主管复核";
    String PLAN_DETAIL_STATUS_AUDIT_PERFORMANCE="待绩效科复核";
    String PLAN_DETAIL_STATUS_SELECT="待选择事件清单";
    String PLAN_DETAIL_STATUS_ALREADY_SELECT="已选择事件清单";
    String EVENTS_FIRST_TYPE_A="事务类";
    String EVENTS_FIRST_TYPE_B="非事务类";
    String EVENTS_FIRST_TYPE_C="新增工作流";
    String EXCELLENT="优";
    String UNQUALIFIED="不合格";
    String MIDDLE="中";
    String FINE="良";
    String CONFORM="符合";
    String NON_CONFORM="不符合";
}
