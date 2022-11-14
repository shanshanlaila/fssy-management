package com.fssy.shareholder.management.pojo.system.performance.employee;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fssy.shareholder.management.pojo.common.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * *****业务部门：
IT科

*****数据表中文名：
事件清单表

*****数据表名：
bs_performance_events_list

*****数据表作用：
员工基础绩效的事件清单

*****变更记录：
时间         	变更人		变更内容
20220915	兰宇铧           	初始设计
 * @TableName bs_performance_events_list
 */
@EqualsAndHashCode(callSuper = true)
@TableName(value ="bs_performance_events_list")
@Data
public class EventList extends BaseModel implements Serializable {
    /**
     * 序号
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 部门名称
     */
    private String departmentName;

    /**
     * 战略职能
     */
    private String strategyFunctions;

    /**
     * 支持职能
     */
    private String supportFunctions;

    /**
     * 工作职责
     */
    private String jobName;

    /**
     * 工作事件
     */
    private String workEvents;

    /**
     * 事件类别
     */
    private String eventsType;

    /**
     * 表单(输出内容)
     */
    private String workOutput;

    /**
     * 备注
     */
    private String note;

    /**
     * 编制年份
     */
    private Integer year;

    /**
     * 事件类型(事务类/非事务类/新增工作流)
     */
    private String eventsFirstType;

    /**
     * 部门表主键
     */
    private Long departmentId;

    /**
     * 月工作标准时长（分）
     */
    private BigDecimal duration;

    /**
     * 评价等级
     */
    private String level;

    /**
     * 事件标准价值
     */
    private BigDecimal standardValue;

    /**
     * 不合格价值
     */
    private BigDecimal delow;

    /**
     * 中价值
     */
    private BigDecimal middle;

    /**
     * 良价值
     */
    private BigDecimal fine;

    /**
     * 优价值
     */
    private BigDecimal excellent;

    /**
     * 编制月份
     */
    private Integer month;

    /**
     * 编制日期,事件清单创建日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private Date createDate;

    /**
     * 生效日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private Date activeDate;

    /**
     * 绩效类型，取值有基础事件绩效/拓展事件绩效两种
     */
    private String performanceForm;

    /**
     * 不合格标准
     */
    private String delowStandard;

    /**
     * 中标准
     */
    private String middleStandard;

    /**
     * 良标准
     */
    private String fineStandard;

    /**
     * 优标准
     */
    private String excellentStandard;

    /**
     * 事件清单创建人
     */
    private String listCreateUser;

    /**
     * 事件清单创建人主键
     */
    private Long listcreateUserId;

    /**
     * 事件价值创建人
     */
    private String valueCreateUser;

    /**
     * 事件价值创建人主键
     */
    private Long valueCreateUserId;

    /**
     * 事件价值创建日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private Date valueCreateDate;

    /**
     * 事件清单状态，待填报价值、待填报标准、完结（此状态才能导出做计划）
     */
    private String status;

    /**
     * 事件清单附件主键，导入时有值
     */
    private Long listAttachmentId;

    /**
     * 事件价值附件主键，导入时有值
     */
    private Long valueAttachmentId;

    /**
     * 事件标准创建人
     */
    private String standardCreateUser;

    /**
     * 事件标准创建人主键
     */
    private Long standardCreateUserId;

    /**
     * 事件标准创建日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private Date standardCreateDate;

    /**
     * 事件标准附件主键，导入时有值
     */
    private Long standardAttachmentId;

    /**
     * 科室
     */
    private String office;

    /**
     * 科室id
     */
    private Long officeId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    
	/**
	 * 岗位配比完成标识
	 */
	@TableField("roleComplete")
	private Integer roleComplete;
}
