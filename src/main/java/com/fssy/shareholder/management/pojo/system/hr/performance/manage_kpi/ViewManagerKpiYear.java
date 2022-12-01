package com.fssy.shareholder.management.pojo.system.hr.performance.manage_kpi;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fssy.shareholder.management.pojo.common.BaseModel;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * VIEW 经理人年度kpi指标
 * </p>
 *
 * @author zzp
 * @since 2022-11-03
 */
@Getter
@Setter
@TableName("view_manager_kpi_year")
public class ViewManagerKpiYear extends BaseModel {

    private static final long serialVersionUID = -7199575718194834669L;

    /**
     * 序号
     */
    @TableField("id")
    private Integer id;

    /**
     * 经理人姓名
     */
    @TableField("managerName")
    private String managerName;

    /**
     * KPI项目描述
     */
    @TableField("projectDesc")
    private String projectDesc;

    /**
     * 权重：%
     */
    @TableField("proportion")
    private BigDecimal proportion;

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
     * 备注
     */
    @TableField("note")
    private String note;
    /**
     * 项目类别
     */
    @TableField("projectType")
    private String projectType;
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
     * 是否总经理
     */
    @TableField("generalManager")
    private String generalManager;

    /**
     * 职位类别
     */
    @TableField("position")
    private String position;
    /**
     * 经理人标识
     */
    @TableField("managerKpiMark")
    private String managerKpiMark;

    /**
     * 年份
     */
    @TableField("year")
    private Integer year;

}
