/**
 * ------------------------修改日志---------------------------------
 * 修改人			修改日期			修改内容
 */
package com.fssy.shareholder.management.pojo.system.performance.employee;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fssy.shareholder.management.pojo.common.BaseModel;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * *****业务部门： IT科 *****数据表中文名： 事件清单主担岗位关系表 *****数据表名：
 * bs_performance_events_relation_main_role *****数据表作用： 事件清单对应的主担岗位 *****变更记录：
 * 时间 变更人 变更内容 20220915 兰宇铧 初始设计
 * </p>
 *
 * @author Solomon
 * @since 2022-10-10
 */
@Getter
@Setter
@TableName("bs_performance_events_relation_role")
public class EventsRelationRole extends BaseModel {

    /**
	 *
	 */
	private static final long serialVersionUID = -6026015938943568306L;

	@TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 事件表主键
     */
    @TableField("eventsId")
    private Long eventsId;

    /**
     * 岗位名称
     */
    @TableField("roleName")
    private String roleName;

    /**
     * 岗位主键
     */
    @TableField("roleId")
    private Long roleId;

    /**
     * 岗位事件分数
     */
    @TableField("score")
    private BigDecimal score;

    /**
     * 编制年份
     */
    @TableField("year")
    private Integer year;

    /**
     * 编制月份
     */
    @TableField("month")
    private Integer month;

    /**
     * 编制日期
     */
    @TableField("createDate")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private LocalDate createDate;

    /**
     * 生效日期
     */
    @TableField("activeDate")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private LocalDate activeDate;

    /**
     * 部门表主键
     */
    @TableField("departmentId")
    private Long departmentId;

    /**
     * 部门名称
     */
    @TableField("departmentName")
    private String departmentName;

    /**
     * 占比，同一个生效日期的同一个事件所有主担岗位加所有次担岗位的占比必须为100%,单位%
     */
    @TableField("proportion")
    private BigDecimal proportion;

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
     * 事件类别
     */
    @TableField("eventsType")
    private String eventsType;

    /**
     * 主担次担标识，取值：主担、次担
     */
    @TableField("isMainOrNext")
    private String isMainOrNext;

    /**
     * 20220930添加，用户表名称
     */
    @TableField("userName")
    private String userName;

    /**
     * 20220930添加，用户表序号
     */
    @TableField("userId")
    private Long userId;

    /**
     * 20220930添加，事件岗位关联附件主键
     */
    @TableField("attachmentId")
    private Long attachmentId;

    /**
     * 事件标准价值
     */
    @TableField("standardValue")
    private BigDecimal standardValue;

    /**
     * 不合格价值
     */
    @TableField("delow")
    private BigDecimal delow;

    /**
     * 中价值
     */
    @TableField("middle")
    private BigDecimal middle;

    /**
     * 良价值
     */
    @TableField("fine")
    private BigDecimal fine;

    /**
     * 优价值
     */
    @TableField("excellent")
    private BigDecimal excellent;

    /**
     * 事件类型
     */
    @TableField("eventsFirstType")
    private String eventsFirstType;

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
}
