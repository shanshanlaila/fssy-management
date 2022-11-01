package com.fssy.shareholder.management.pojo.system.performance.manage_kpi;

import com.baomidou.mybatisplus.annotation.TableField;
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
     * 经理人姓名
     */
    @TableField("managerName")
    private String managerName;

    /**
     * 年度目标项目ID：建立与年度关联
     */
    @TableField("manageKpiYearId")
    private Integer manageKpiYearId;

    /**
     * 企业名称
     */
    @TableField("companyName")
    private String companyName;

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
     * KPI来源：自我提出;集团提出
     */
    @TableField("source")
    private String source;

    /**
     * 经理人KPI项目状态
     */
    @TableField("status")
    private String status;

    /**
     * 权重：%
     */
    @TableField("proportion")
    private BigDecimal proportion;

    /**
     * 人工调整分数
     */
    @TableField("monthScoreAdjust")
    private BigDecimal monthScoreAdjust;

    /**
     * 系统生成分数
     */
    @TableField("monthScoreSys")
    private BigDecimal monthScoreSys;

    /**
     * 指标类别:经营管理指标、激励约束项目
     */
    @TableField("projectType")
    private String projectType;


}
