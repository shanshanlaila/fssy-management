package com.fssy.shareholder.management.pojo.system.operate.invest;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fssy.shareholder.management.pojo.common.BaseModel;
import java.io.Serializable;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * **数据表名：	bs_operate_invest_project_plan_trace	**数据表中文名：	项目进度计划跟踪表	**业务部门：	经营管理部	**数据表作用：	用于记录单一项目进展详情及跟踪情况	**创建人创建日期：	TerryZeng 2022-12-2
 * </p>
 *
 * @author Solomon
 * @since 2022-12-14
 */
@Getter
@Setter
@TableName("bs_operate_invest_project_plan_trace")
public class InvestProjectPlanTrace extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 序号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 项目状态锁定
     */
    @TableField("status")
    private String status;

    /**
     * 备注
     */
    @TableField("note")
    private String note;

    /**
     * 项目台账Id
     */
    @TableField("projectListId")
    private Integer projectListId;

    /**
     * 序号
     */
    @TableField("serial")
    private String serial;

    /**
     * 项目名称/工作内容描述
     */
    @TableField("projectName")
    private String projectName;

    /**
     * 项目目标
     */
    @TableField("projectTarget")
    private String projectTarget;

    /**
     * 输出结果
     */
    @TableField("outResult")
    private String outResult;

    /**
     * 主挡当
     */
    @TableField("responsePerson")
    private String responsePerson;

    /**
     * 次担当
     */
    @TableField("otherResponsePerson")
    private String otherResponsePerson;

    /**
     * 开始日期
     */
    @TableField("startDate")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private LocalDate startDate;

    /**
     * 结束日期
     */
    @TableField("endDate")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private LocalDate endDate;

    /**
     * 部门名称
     */
    @TableField("departmentName")
    private String departmentName;

    /**
     * 是否正常
     */
    @TableField("isNormal")
    private String isNormal;

    /**
     * 关键节点:是，否
     */
    @TableField("milestone")
    private String milestone;

    /**
     * 实际完成时间
     */
    @TableField("actualEndDate")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private LocalDate actualEndDate;

    /**
     * 存在问题
     */
    @TableField("issue")
    private String issue;

    /**
     * 改善措施
     */
    @TableField("improveAction")
    private String improveAction;

    /**
     * 项目跟踪
     */
    @TableField("projectTrace")
    private String projectTrace;

    /**
     * 检查人
     */
    @TableField("Inspectedby")
    private String inspectedby;

    /**
     * 可研报告计划完成时间
     */
    @TableField("feasibilityDate")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private LocalDate feasibilityDate;

    /**
     * 合同计划完成时间
     */
    @TableField("contractDate")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private LocalDate contractDate;

    /**
     * 年份
     */
    @TableField("year")
    private Integer year;

    /**
     * 项目指标
     */
    @TableField("projectIndicators")
    private String projectIndicators;

    /**
     * 检查时间
     */
    @TableField("inspectionDate")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private LocalDate inspectionDate;

    /**
     * 公司名称
     */
    @TableField("companyName")
    private String companyName;

    /**
     * 评价
     */
    @TableField("evaluate")
    private String evaluate;

    /**
     * 月份
     */
    @TableField("month")
    private Integer month;

    /**
     * 项目阶段
     */
    @TableField("projectPhase")
    private String projectPhase;

    /**
     * 检查结果
     */
    @TableField("inspectionResult")
    private String inspectionResult;

    /**
     * 课题内容
     */
    @TableField("projectContent")
    private String projectContent;

    /**
     * 序号id
     */
    @TableField("serialId")
    private Integer serialId;

    /**
     * 摘要
     */
    @TableField("abstracte")
    private String abstracte;

    /**
     * 总结评价
     */
    @TableField("evaluateSum")
    private String evaluateSum;


}
