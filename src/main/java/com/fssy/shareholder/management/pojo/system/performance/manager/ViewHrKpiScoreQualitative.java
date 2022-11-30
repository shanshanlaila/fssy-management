package com.fssy.shareholder.management.pojo.system.performance.manager;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fssy.shareholder.management.pojo.common.BaseModel;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 经理人定性评价汇总表实体类
 * </p>
 *
 * @author Shizn
 * @since 2022-11-29
 */
@Getter
@Setter
@TableName("view_hr_kpi_score_qualitative")
public class ViewHrKpiScoreQualitative extends BaseModel {

    private static final long serialVersionUID = -3208974312042019485L;

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
     * 年份
     */
    @TableField("year")
    private Integer year;

    /**
     * 定量评价月份
     */
    @TableField("kpiScoreMonth")
    private Integer kpiScoreMonth;

    /**
     * 经理人姓名
     */
    @TableField("managerName")
    private String managerName;

    /**
     * 经理人Id
     */
    @TableField("managerId")
    private Integer managerId;

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
     * 是否总经理:是
     */
    @TableField("generalManager")
    private String generalManager;

    /**
     * 职务
     */
    @TableField("position")
    private String position;

    /**
     * 企业名称缩写
     */
    @TableField("companyNameShort")
    private String companyNameShort;

    /**
     * 人工调整分数
     */
    @TableField("scoreAdjustKpi")
    private BigDecimal scoreAdjustKpi;

    /**
     * 定性评价综合得分（人工调整）
     */
    @TableField("qualitativeScore")
    private Double qualitativeScore;

    /**
     * 定量评价占比	
     */
    @TableField("kpiScoreR")
    private BigDecimal kpiScoreR;

    /**
     * 系统生成分数
     */
    @TableField("scoreAuto")
    private BigDecimal scoreAuto;
    /**
     * 人工调整分数
     */
    @TableField("scoreAdjust")
    private BigDecimal scoreAdjust;

    /**
     * 定量绩效分数
     */
    @TableField("kpiScore")
    private BigDecimal kpiScore;


}
