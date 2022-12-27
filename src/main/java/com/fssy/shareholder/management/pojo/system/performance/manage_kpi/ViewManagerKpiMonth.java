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
 * 经理人KPI分数管理实体类
 * VIEW manager_kpi_year & manager_kpi_month 构建的视图，用来获取参与计算的相关字段
 * </p>
 *
 * @author zzp
 * @since 2022-10-31
 */
@Getter
@Setter
//获取数据库view_manager_kpi_month视图里面的字段
@TableName("view_manager_kpi_month")
public class ViewManagerKpiMonth extends BaseModel {

    private static final long serialVersionUID = 5128806343709986912L;

    /**
     * 序号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 填报企业
     */
    @TableField("companyName")
    private String companyName;

    /**
     * 公司id
     */
    @TableField("companyId")
    private Integer companyId;

    /**
     * 经理人姓名
     */
    @TableField("managerName")
    private String managerName;

    /**
     * 年份
     */
    @TableField("year")
    private Integer year;

    /**
     * 月份
     */
    @TableField("month")
    private Integer month;

    /**
     * 重点工作
     */
    @TableField("projectType")
    private String projectType;

    /**
     * 管理项目
     */
    @TableField("projectDesc")
    private String projectDesc;

    /**
     * 管理项目定义
     */
    @TableField("kpiFormula")
    private String kpiFormula;

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
     * 过去第三年值
     */
    @TableField("pastThreeYearsActual")
    private String pastThreeYearsActual;

    /**
     * 过去第二年值
     */
    @TableField("pastTwoYearsActual")
    private String pastTwoYearsActual;

    /**
     * 过去第一年值
     */
    @TableField("pastOneYearActual")
    private String pastOneYearActual;

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
     * 分析结果
     */
    @TableField("analyzeRes")
    private String analyzeRes;

    /**
     * 人工调整分数
     */
    @TableField("monthScoreAdjust")
    private BigDecimal monthScoreAdjust;

    /**
     * 权重%
     */
    @TableField("proportion")
    private BigDecimal proportion;

    /**
     * 年度目标项目ID：建立与年度关联
     */
    @TableField("manageKpiYearId")
    private Integer manageKpiYearId;

    /**
     * 职务
     */
    @TableField("position")
    private String position;

    /**
     * 是否总经理:是
     */
    @TableField("generalManager")
    private String generalManager;


    /**
     * 经理人KPI项目状态
     */
    @TableField("status")
    private String status;

    /**
     * 系统生成分数
     */
    @TableField("monthScoreAuto")
    private BigDecimal monthScoreAuto;

}
