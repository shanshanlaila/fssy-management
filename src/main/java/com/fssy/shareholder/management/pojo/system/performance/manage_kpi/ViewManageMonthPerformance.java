package com.fssy.shareholder.management.pojo.system.performance.manage_kpi;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fssy.shareholder.management.pojo.common.BaseModel;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 *视图年度月度实绩值 实体类 </p>
 *
 * @author Shizn
 * @since 2022-11-16
 */
@Getter
@Setter
@TableName("view_manage_month_performance")
public class ViewManageMonthPerformance extends BaseModel {

    private static final long serialVersionUID = 1297948598377184246L;

    /**
     * 序号
     */
    @TableField("manageKpiYearId")
    private Integer manageKpiYearId;

    /**
     * 企业名称
     */
    @TableField("companyName")
    private String companyName;

    /**
     * 企业id
     */
    @TableField("companyId")
    private Integer companyId;
    /**
     * 指标类别:经营管理指标、激励约束项目
     */
    @TableField("projectType")
    private String projectType;

    /**
     * KPI项目描述
     */
    @TableField("projectDesc")
    private String projectDesc;

    /**
     * 项目定义
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
     * 年份
     */
    @TableField("year")
    private Integer year;

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
     * 月份
     */
    @TableField("month")
    private Integer month;

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
     * 权重
     */
    @TableField("proportion")
    private BigDecimal proportion;

    /**
     * 分析结果
     */
    @TableField("analyzeRes")
    private String analyzeRes;
    /**
     * 分解目标方式：
     */
    @TableField("kpiDecomposeMode")
    private String kpiDecomposeMode;

    /**
     * 经理人绩效指标标识：识别经营管理指标和经理人指标
     */
    @TableField("managerKpiMark")
    private String managerKpiMark;

    /**
     * 评价模式：系统评分、人工评分
     */
    @TableField("evaluateMode")
    private String evaluateMode;

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
     * 监控部门名称
     */
    @TableField("monitorDepartment")
    private String monitorDepartment;

    /**
     * 监控人姓名
     */
    @TableField("monitorUser")
    private String monitorUser;


}
