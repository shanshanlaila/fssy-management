package com.fssy.shareholder.management.pojo.system.performance.employee;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fssy.shareholder.management.pojo.common.BaseModel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;

/**
 * <p>
 * *****业务部门：	绩效科		*****数据表中文名：	员工月度履职评价说明表明细		*****数据表名：	bs_performance_entry_excellent_state_detail		*****数据表作用：	员工月度履职评价为优时，填报的内容		*****变更记录：	时间         	变更人		变更内容	20220915	兰宇铧           	初始设计
 * </p>
 *
 * @author 农浩
 * @since 2022-10-24
 */
@Getter
@Setter
@TableName("bs_performance_entry_excellent_state_detail")
public class EntryExcellentStateDetail extends BaseModel {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 事件表主键
     */
    @TableField("eventsId")
    private Long eventsId;

    /**
     * 工作事件
     */
    @TableField("workEvents")
    private String workEvents;

    /**
     * 主担用户名称，姓名拼起来
     */
    @TableField("mainUserName")
    private String mainUserName;

    /**
     * 工作完成描述
     */
    @TableField("completeDesc")
    private String completeDesc;

    /**
     * 实际完成时间
     */
    @TableField("actualCompleteDate")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private LocalDate actualCompleteDate;

    /**
     * 创新或超越逾期的成果展示原设定目标
     */
    @TableField("oriTarget")
    private String oriTarget;

    /**
     * 创新或超越逾期的成果展示实际达成目标
     */
    @TableField("actualTarget")
    private String actualTarget;

    /**
     * 创新或超越逾期的成果展示岗位开展的有价值的创新工作描述
     */
    @TableField("innovative")
    private String innovative;

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
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
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
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
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
     * 履职评价说明表主键，按年、月、部门分表
     */
    @TableField("mergeId")
    private Long mergeId;

    /**
     * 对应的履职计划明细主键
     */
    @TableField("casPlanId")
    private Long casPlanId;

    /**
     * 对应的履职总结明细主键
     */
    @TableField("casReviewId")
    private Long casReviewId;

    /**
     * 申报日期
     */
    @TableField("applyDate")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
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
     * 经营管理部主管复核，取值：不合格，中，良，优，会修改对应的总结的评价内容
     */
    @TableField("ministerReview")
    private String ministerReview;

    /**
     * 绩效科复核，取值：不合格，中，良，优，结果为优时，转经营管理部主管复核，其他结果状态完结，会修改对应的总结的评价内容
     */
    @TableField("classReview")
    private String classReview;

    /**
     * 申报材料复核状态，待绩效科复核，待经营管理部主管复核，完结
     */
    @TableField("status")
    private String status;

    /**
     * 经营管理部主管复核人名称
     */
    @TableField("ministerReviewUser")
    private String ministerReviewUser;

    /**
     * 经营管理部主管复核人主键
     */
    @TableField("ministerReviewUserId")
    private Long ministerReviewUserId;

    /**
     * 经营管理部主管复核日期
     */
    @TableField("ministerReviewDate")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private LocalDate ministerReviewDate;

    /**
     * 绩效科复核人名称
     */
    @TableField("classReviewUser")
    private String classReviewUser;

    /**
     * 绩效科复核人主键
     */
    @TableField("classReviewUserId")
    private Long classReviewUserId;

    /**
     * 绩效科复核日期
     */
    @TableField("classReviewDate")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private LocalDate classReviewDate;

    /**
     * 次担用户名称，姓名拼起来，用于显示
     */
    @TableField("nextUserName")
    private String nextUserName;

    /*
     * DataTimeFormat是往数据库走时转换，时区指定在启动类中
     * JsonFormat是数据往客户端时转换
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    @TableField(exist = false)
    private Date importDate;

    /**
     * 事件岗位关系表主键
     */
    @TableField("eventsRoleId")
    private Long eventsRoleId;

    /**
     * 对应工作计划内容
     */
    @TableField("planningWork")
    private String planningWork;


}
