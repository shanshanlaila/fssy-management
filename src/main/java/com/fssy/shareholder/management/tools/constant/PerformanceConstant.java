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
    String FINAL = "完结";
    String EVENT_LIST_STATUS_WAIT = "待填报标准";
    String CANCEL = "取消";
    String WAIT_WRITE_REVIEW ="待填报回顾";
    String WAIT_SUBMIT_AUDIT ="待提交审核";
    String WAIT_AUDIT_MINISTER ="待部长审核";
    String WAIT_AUDIT_CHIEF ="待科长审核";
    String PLAN_DETAIL_STATUS_AUDIT_HR="待人力资源部审核";
    String WAIT_AUDIT_MANAGEMENT ="待经营管理部审核";// management
    String WAIT_AUDIT_MANAGEMENT_CHIEF ="待经营管理部主管复核";
    String WAIT_AUDIT_PERFORMANCE ="待绩效科复核";
    String PLAN_DETAIL_STATUS_SELECT="待选择基础事件";
    String EVENT_FIRST_TYPE_TRANSACTION ="事务类";//transaction
    String EVENT_FIRST_TYPE_NOT_TRANSACTION ="非事务类";
    String EVENT_FIRST_TYPE_NEW_EVENT ="新增工作流";
    String EXCELLENT="优";
    String UNQUALIFIED="不合格";
    String QUALIFIED="合格";
    String MIDDLE="中";
    String FINE="良";
    String CONFORM="符合";
    String NON_CONFORM="不符合";
    String BASICS_EVENT="基础事件绩效";
    String EXPAND_EVENT="拓展事件绩效";
    String WAIT_CREATE_EVENT="待创建基础事件";
    String YES="是";
    String NO="否";
}
