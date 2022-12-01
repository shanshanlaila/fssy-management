package com.fssy.shareholder.management.pojo.system.hr.performance.manage_kpi;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fssy.shareholder.management.pojo.common.BaseModel;

import java.math.BigDecimal;

import lombok.Data;

/**
 * <p>
 * 计算经营管理月度分数视图  实体类
 * </p>
 *
 * @author Shizn
 * @since 2022-11-10
 */
@Data
@TableName("view_manage_year_month_score")
public class ViewManageYearMonthScore extends BaseModel {

    private static final long serialVersionUID = 2247174466172391766L;

    /**
     * 序号
     */
    @TableField("id")
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
     * 指标类型
     */
    @TableField("projectType")
    private String projectType;
    /**
     * KPI项目描述
     */
    @TableField("projectDesc")
    private String projectDesc;

    /**
     * 单位
     */
    @TableField("unit")
    private String unit;

    /**
     * 评价模式：系统评分、人工评分
     */
    @TableField("evaluateMode")
    private String evaluateMode;
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
     * 自动生成分数
     */
    @TableField("scoreAuto")
    private BigDecimal scoreAuto;

    /**
     * 调整分数
     */
    @TableField("scoreAdjust")
    private BigDecimal scoreAdjust;

    /**
     * 分析结果
     */
    @TableField("analyzeRes")
    private String analyzeRes;

    /**
     * 分解目标方式：月度值(年度值/12*月份)；年度值（）
     */
    @TableField("kpiDecomposeMode")
    private String kpiDecomposeMode;

    /**
     * 绩效标识：识别经营管理指标和绩效指标
     */
    @TableField("managerKpiMark")
    private String managerKpiMark;


}
