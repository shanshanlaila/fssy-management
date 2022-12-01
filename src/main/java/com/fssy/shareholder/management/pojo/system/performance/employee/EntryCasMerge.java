/**   
 * ------------------------修改日志---------------------------------
 * 修改人			修改日期			修改内容
 */
package com.fssy.shareholder.management.pojo.system.performance.employee;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

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

/**
 * <p>
 * *****业务部门： 绩效科 *****数据表中文名： 员工履职表 *****数据表名：
 * bs_performance_employee_entry_cas_merge *****数据表作用：
 * 员工每月月初填写的履职情况计划表，以及在计划上写回顾，评价都在这里 *****变更记录： 时间 变更人 变更内容 20220915 兰宇铧 初始设计
 * </p>
 *
 * @author Solomon
 * @since 2022-10-11
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("bs_performance_employee_entry_cas_merge")
public class EntryCasMerge extends BaseModel {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

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
     * 履职计划状态，待审核，审核通过，审核拒绝
     */
    @TableField("status")
    private String status;

    /**
     * 员工月度履职计划表编号
     */
    @TableField("mergeNo")
    private String mergeNo;

    /**
     * mergeNo的序列号
     */
    @TableField("serial")
    private Integer serial;

    /**
     * 备注
     */
    @TableField("note")
    private String note;


}
