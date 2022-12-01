package com.fssy.shareholder.management.pojo.system.hr.performance.employee;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fssy.shareholder.management.pojo.common.BaseModel;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * *****业务部门：	绩效科		*****数据表中文名：	员工月度履职评价说明表		*****数据表名：	bs_performance_entry_excellent_state_merge		*****数据表作用：	员工月度履职评价为优时，填报的表		*****变更记录：	时间         	变更人		变更内容	20220915	兰宇铧           	初始设计
 * </p>
 *
 * @author shanshan
 * @since 2022-10-28
 */
@Getter
@Setter
@TableName("bs_performance_entry_excellent_state_merge")
public class EntryExcellentStateMerge extends BaseModel {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 备注
     */
    @TableField("note")
    private String note;

    /**
     * 评价部门
     */
    @TableField("departmentName")
    private String departmentName;

    /**
     * 评价部门主键
     */
    @TableField("departmentId")
    private Long departmentId;

    /**
     * 编制日期
     */
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
    @TableField("auditDate")
    private LocalDate auditDate;

    /**
     * 审核意见
     */
    @TableField("auditNote")
    private String auditNote;

    /**
     * 履职评价说明表编号，按年、月、部门分表
     */
    @TableField("mergeNo")
    private String mergeNo;

    /**
     * 申报日期
     */
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
     * mergeNo的序列号
     */
    @TableField("serial")
    private Integer serial;


}
