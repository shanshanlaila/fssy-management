/**
 * ------------------------修改日志---------------------------------
 * 修改人			修改日期			修改内容
 */
package com.fssy.shareholder.management.pojo.system.performance.employee;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fssy.shareholder.management.pojo.common.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * <p>
 * *****业务部门： 绩效科 *****数据表中文名： 员工履职计划明细 *****数据表名：
 * bs_performance_employee_entry_cas_plan_detail *****数据表作用： 员工每月月初填写的履职情况计划明细
 * *****变更记录： 时间 变更人 变更内容 20220915 兰宇铧 初始设计
 * </p>
 *
 * @author Solomon
 * @since 2022-10-11
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("bs_performance_employee_entry_cas_plan_detail")
public class EntryCasPlanDetail extends BaseModel {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 事件表主键
     */
    @TableField("eventsId")
    private Long eventsId;

    /**
     * 事件类别
     */
    @TableField("eventsType")
    private String eventsType;

    /**
     * 工作职责
     */
    @TableField("jobName")
    private String jobName;

    /**
     * 工作事件
     */
    @TableField("workEvents")
    private String workEvents;

    /**
     * 事件类型，取值有基础事件绩效/拓展事件绩效两种
     */
    @TableField("eventsForm")
    private String eventsForm;

    /**
     * 事件标准价值
     */
    @TableField("standardValue")
    private BigDecimal standardValue;

    /**
     * 不合格标准
     */
    @TableField("delowStandard")
    private String delowStandard;

    /**
     * 中标准
     */
    @TableField("middleStandard")
    private String middleStandard;

    /**
     * 良标准
     */
    @TableField("fineStandard")
    private String fineStandard;

    /**
     * 优标准
     */
    @TableField("excellentStandard")
    private String excellentStandard;

    /**
     * 主担/次担
     */
    @TableField("mainOrNext")
    private String mainOrNext;

    /**
     * 部门名称
     */
    @TableField("departmentName")
    private String departmentName;

    /**
     * 部门表主键
     */
    @TableField("departmentId")
    private Long departmentId;

    /**
     * 岗位（角色）名称
     */
    @TableField("roleName")
    private String roleName;

    /**
     * 岗位表（角色）主键
     */
    @TableField("roleId")
    private Long roleId;

    /**
     * 员工姓名
     */
    @TableField("userName")
    private String userName;

    /**
     * 员工表主键
     */
    @TableField("userId")
    private Long userId;

    /**
     * 申报日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    @TableField("applyDate")
    private LocalDate applyDate;

    /**
     * 申报年份
     */
    @TableField("year")
    private Integer year;

    /**
     * 申报月份
     */
    @TableField("month")
    private Integer month;

    /**
     * 对应工作事件的计划内容
     */
    @TableField("planningWork")
    private String planningWork;

    /**
     * 频次
     */
    @TableField("times")
    private String times;

    /**
     * 表单(输出内容)来源事件清单
     */
    @TableField("workOutput")
    private String workOutput;

    /**
     * 表单(输出内容)来源自己填写
     */
    @TableField("planOutput")
    private String planOutput;

    /**
     * 计划开始日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    @TableField("planStartDate")
    private LocalDate planStartDate;

    /**
     * 计划完成日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    @TableField("planEndDate")
    private LocalDate planEndDate;

    /**
     * 编制日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    @TableField("createDate")
    private LocalDate createDate;

    /**
     * 编制人
     */
    @TableField("createName")
    private String createName;

    /**
     * 编制人主键
     */
    @TableField("createId")
    private Long createId;

    /**
     * 审核人
     */
    @TableField("auditName")
    private String auditName;

    /**
     * 审核人主键
     */
    @TableField("auditId")
    private Long auditId;

    /**
     * 审核日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    @TableField("auditDate")
    private LocalDate auditDate;

    /**
     * 审核意见
     */
    @TableField("auditNote")
    private String auditNote;

    /**
     * 履职计划状态，待填报总结、完结
     */
    @TableField("status")
    private String status;

    /**
     * 员工月度履职计划表编号
     */
    @TableField("mergeNo")
    private String mergeNo;

    /**
     * 员工月度履职计划表主键
     */
    @TableField("mergeId")
    private Long mergeId;

    /**
     * 事件类型(事务类/非事务类/新增工作流)
     */
    @TableField("eventsFirstType")
    private String eventsFirstType;

    /**
     * 备注
     */
    @TableField("note")
    private String note;

    /**
     * 履职计划审核状态，待审核，审核通过，审核拒绝
     */
    @TableField("auditStatus")
    private String auditStatus;

    /**
     * 导入时，对应的附件表主键
     */
    @TableField("attachmentId")
    private Long attachmentId;

    /**
     * 事件岗位关系表主键
     */
    @TableField("eventsRoleId")
    private Long eventsRoleId;

    /**
     *
     */
    @TableField("isNewEvent")
    private String isNewEvent;

    /**
     * 新增工作流的状态标识
     */
    @TableField("newStatus")
    private String newStatus;

    /**
     * 科室
     */
    @TableField("office")
    private String office;

    /**
     * 科室id
     */
    @TableField("officeId")
    private Long officeId;

}
