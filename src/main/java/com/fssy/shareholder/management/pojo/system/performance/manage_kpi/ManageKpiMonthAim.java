package com.fssy.shareholder.management.pojo.system.performance.manage_kpi;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fssy.shareholder.management.pojo.common.BaseModel;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 经营管理月度指标数据表 实体类
 * </p>
 * @author Shizn
 * @since 2022-10-24
 */
@Getter
@Setter
@TableName("bs_manage_kpi_month")
public class ManageKpiMonthAim extends BaseModel {

    private static final long serialVersionUID = 3769607030427242946L;

    /**
     * 序号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 经理人KPI项目状态
     */
    @TableField("status")
    private String status;

    /**
     * 修改分数原因
     */
    @TableField("scoreAdjustCause")
    private String scoreAdjustCause;

    /**
     * 企业名称
     */
    @TableField("companyName")
    private String companyName;

    /**
     * 企业ID
     */
    @TableField("companyId")
    private Integer companyId;

    /**
     * 指标类别:经营管理指标、激励约束项目
     */
    @TableField("projectType")
    private String projectType;

    /**
     * 序号
     */
    @TableField("projectIndex")
    private Integer projectIndex;

    /**
     * KPI项目描述
     */
    @TableField("projectDesc")
    private String projectDesc;

    /**
     * 计算公式
     */
    @TableField("kpiFormula")
    private String kpiFormula;

    /**
     * 指标定义
     */
    @TableField("kpiDefinition")
    private String kpiDefinition;

    /**
     * 单位
     */
    @TableField("unit")
    private String unit;

    /**
     * 数据来源
     */
    @TableField("dataSource")
    private String dataSource;

    /**
     * 标杆企业名称
     */
    @TableField("benchmarkCompany")
    private String benchmarkCompany;

    /**
     * 标杆值
     */
    @TableField("benchmarkValue")
    private String benchmarkValue;

    /**
     * 监控部门名称
     */
    @TableField("monitorDepartment")
    private String monitorDepartment;

    /**
     * 监控部门ID
     */
    @TableField("monitorDepartmentId")
    private Integer monitorDepartmentId;

    /**
     * 监控人姓名
     */
    @TableField("monitorUser")
    private String monitorUser;
    /**
     * 评价状态
     */
    @TableField("evaluateStatus")
    private String evaluateStatus;
    /**
     * 监控人ID
     */
    @TableField("monitorUserId")
    private Integer monitorUserId;

    /**
     * 年份
     */
    @TableField("year")
    private Integer year;

    /**
     * 基本目标
     */
    @TableField("basicTarget")
    private String basicTarget;

    /**
     * 必达目标
     */
    @TableField("mustInputTarget")
    private String mustInputTarget;

    /**
     * 达标目标
     */
    @TableField("reachTarget")
    private String reachTarget;

    /**
     * 挑战目标
     */
    @TableField("challengeTarget")
    private String challengeTarget;


    /**
     * 过去第一年值
     */
    @TableField("pastOneYearActual")
    private String pastOneYearActual;

    /**
     * 过去第二年值
     */
    @TableField("pastTwoYearsActual")
    private String pastTwoYearsActual;

    /**
     * 过去第三年值
     */
    @TableField("pastThreeYearsActual")
    private String pastThreeYearsActual;

    /**
     * 选取原则:支持总经理KPI落实
     */
    @TableField("setPolicy")
    private String setPolicy;

    /**
     * KPI来源：自我提出;集团提出
     */
    @TableField("source")
    private String source;

    /**
     * 评价模式：系统评分、人工评分
     */
    @TableField("evaluateMode")
    private String evaluateMode;

    /**
     * 评价频次:
     */
    @TableField("evaluateFrequency")
    private String evaluateFrequency;

    /**
     * 累积评价标识
     */
    @TableField("accumulateTargetMark")
    private String accumulateTargetMark;

    /**
     * 月份
     */
    @TableField("month")
    private Integer month;

    /**
     * 月度目标
     */
    @TableField("monthTarget")
    private BigDecimal monthTarget;

    /**
     * 月度实绩
     */
    @TableField("monthActualValue")
    private BigDecimal monthActualValue;

    /**
     * 目标累积值
     */
    @TableField("accumulateTarget")
    private BigDecimal accumulateTarget;

    /**
     * 实际完成累积值
     */
    @TableField("accumulateActual")
    private BigDecimal accumulateActual;

    /**
     * 当期累积目标年度占比
     */
    @TableField("accumulateTargetRate")
    private BigDecimal accumulateTargetRate;

    /**
     * 分析类型
     */
    @TableField("analyzeType")
    private String analyzeType;

    /**
     * 修改分析标识原因
     */
    @TableField("adjustAnalyzeMarkCause")
    private String adjustAnalyzeMarkCause;

    /**
     * 分析结果
     */
    @TableField("analyzeRes")
    private String analyzeRes;

    /**
     * 企业名称缩写
     */
    @TableField("companyNameShort")
    private String companyNameShort;

    /**
     * KPI指标库ID
     */
    @TableField("kpiLibId")
    private Integer kpiLibId;

    /**
     * 管理幅度上限
     */
    @TableField("manageLevelUp")
    private String manageLevelUp;

    /**
     * 管理幅度下限
     */
    @TableField("manageLevelDown")
    private String manageLevelDown;

    /**
     * 当期评价结果(自动)
     */
    @TableField("termEvaluateAuto")
    private String termEvaluateAuto;

    /**
     * 评价结果分析(优点缺点)
     */
    @TableField("analyzeDesc")
    private String analyzeDesc;

    /**
     * 累积评价结果(自动）
     */
    @TableField("accumulateEvaluateAuto")
    private String accumulateEvaluateAuto;

    /**
     * 累积评价结果（调整）
     */
    @TableField("accumulateEvaluateAdjust")
    private String accumulateEvaluateAdjust;

    /**
     * 当期评价结果(调整)
     */
    @TableField("termEvaluateAdjust")
    private String termEvaluateAdjust;

    /**
     * 当期评价结果调整原因
     */
    @TableField("termEvaluateCause")
    private String termEvaluateCause;

    /**
     * 累积评价结果调整原因
     */
    @TableField("accumulateEvaluateCause")
    private String accumulateEvaluateCause;

    /**
     * 当期目标达成率
     */
    @TableField("termTargetRate")
    private BigDecimal termTargetRate;

    /**
     * 年度目标完成了率
     */
    @TableField("yearTargetRate")
    private BigDecimal yearTargetRate;

    /**
     * 年度目标项目ID：建立与年度关联
     */
    @TableField("manageKpiYearId")
    private Integer manageKpiYearId;
    /**
     * 自动生成分数
     */
    @TableField("scoreAuto")
    private BigDecimal scoreAuto;

    /**
     * 人工调整分数
     */
    @TableField("scoreAdjust")
    private BigDecimal scoreAdjust;
    /**
     * 分解目标方式
     */
    @TableField("kpiDecomposeMode")
    private String kpiDecomposeMode;
    /**
     * 绩效标识：识别经营管理指标和绩效指标
     */
    @TableField("performanceMark")
    private String performanceMark;


}
