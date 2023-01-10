package com.fssy.shareholder.management.pojo.system.operate.invest;

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

/**
 * <p>
 * **数据表名：	bs_operate_invest_project_list	**数据表中文名：	年度投资项目清单	**业务部门：	经营管理部	**数据表作用：	记录 企业年度投资项目清单	**创建人创建日期：	TerryZeng 2022-12-2
 * </p>
 *
 * @author Solomon
 * @since 2022-12-09
 */
@Getter
@Setter
@TableName("bs_operate_invest_project_list")
public class InvestProjectList extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 序号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

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
     * 项目名称
     */
    @TableField("projectName")
    private String projectName;

    /**
     * 指标创建年份
     */
    @TableField("year")
    private Integer year;

    /**
     * 企业名称
     */
    @TableField("companyName")
    private String companyName;

    /**
     * 企业ID
     */
    @TableField("companyId")
    private Long companyId;

    /**
     * 企业名称简称
     */
    @TableField("companyShortName")
    private String companyShortName;

    /**
     * 项目来源:年度计划,临时新增
     */
    @TableField("projectSource")
    private String projectSource;

    /**
     * 项目类别：自动化、质量改善
     */
    @TableField("projectType")
    private String projectType;

    /**
     * 项目投资类型:重点项目、一般项目
     */
    @TableField("projectClass")
    private String projectClass;

    /**
     * 项目简介
     */
    @TableField("projectAbstract")
    private String projectAbstract;

    /**
     * 计划投资额
     */
    @TableField("investmentVolumePlan")
    private Double investmentVolumePlan;

    /**
     * 实际投资额
     */
    @TableField("investmentVolumeActual")
    private Double investmentVolumeActual;

    /**
     * 企业分管经理人
     */
    @TableField("respManager")
    private String respManager;

    /**
     * 项目联络人
     */
    @TableField("projectContact")
    private String projectContact;

    /**
     * 项目开始时间计划
     */
    @TableField("projectSrartDatePlan")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(pattern="yyyy-MM-dd",timezone="Asia/Shanghai")
    private LocalDate projectSrartDatePlan;

    /**
     * 项目开始时间实际
     */
    @TableField("projectSrartDateActual")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(pattern="yyyy-MM-dd",timezone="Asia/Shanghai")
    private LocalDate projectSrartDateActual;

    /**
     * 项目结束时间计划
     */
    @TableField("projectEndDatePlan")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(pattern="yyyy-MM-dd",timezone="Asia/Shanghai")
    private LocalDate projectEndDatePlan;

    /**
     * 项目结束时间实际
     */
    @TableField("projectEndDateActual")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(pattern="yyyy-MM-dd",timezone="Asia/Shanghai")
    private LocalDate projectEndDateActual;

    /**
     * 项目当前所处阶段
     */
    @TableField("currentProjectStatus")
    private String currentProjectStatus;

    /**
     * 项目汇报日期
     */
    @TableField("projectReportDate")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(pattern="yyyy-MM-dd",timezone="Asia/Shanghai")
    private LocalDate projectReportDate;

    /**
     * 可研报告状态
     */
    @TableField("projectFeasibilityStudyReport")
    private String projectFeasibilityStudyReport;

    /**
     * 经委会审批状态
     */
    @TableField("committeeAuditStatus")
    private String committeeAuditStatus;

    /**
     * 董事会审批状态
     */
    @TableField("boardAuditStatus")
    private String boardAuditStatus;

    /**
     * 项目完成状态
     */
    @TableField("projectFinishStatus")
    private String projectFinishStatus;

    /**
     * 项目风险状态
     */
    @TableField("projectRiskStatus")
    private String projectRiskStatus;

    /**
     * 项目评价状态
     */
    @TableField("projectEvalStatus")
    private String projectEvalStatus;

    /**
     * 项目自评状态
     */
    @TableField("projectSelfEvalStatus")
    private String projectSelfEvalStatus;

    /**
     * 所属产业单元
     */
    @TableField("businessUnit")
    private String businessUnit;

    /**
     * 涉及生产线名称
     */
    @TableField("productLineName")
    private String productLineName;

    /**
     * 涉及生产线Id
     */
    @TableField("productLineId")
    private Long productLineId;



    /**
     * 月份
     */
    @TableField("month")
    private Integer month;


    /**
     * 可研报告预计汇报日期
     */
    @TableField("projectFeasibilityStudyReportDate")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(pattern="yyyy-MM-dd",timezone="Asia/Shanghai")
    private LocalDate projectFeasibilityStudyReportDate;


    /**
     * 可研报告预计汇报日期,没有日期启动这个字段
     */
    @TableField("projectFeasibilityStudyReportPlan")
    private String projectFeasibilityStudyReportPlan;

}
