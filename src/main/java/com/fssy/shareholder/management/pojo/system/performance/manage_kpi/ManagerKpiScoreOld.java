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
 * 经理人绩效分数表		【修改内容】增加了年度关联ID字段	【修改时间】2022-10-27	【修改人】张泽鹏
 * </p>
 *
 * @author zzp
 * @since 2022-10-31
 */
@Getter
@Setter
@TableName("bs_manager_kpi_score")
public class ManagerKpiScoreOld extends BaseModel {

    private static final long serialVersionUID = 9178769836525512783L;

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
     * 备注
     */
    @TableField("note")
    private String note;

    /**
     * 年份
     */
    @TableField("year")
    private Integer year;

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
     * 月份
     */
    @TableField("month")
    private Integer month;

    /**
     * 季度
     */
    @TableField("quarter")
    private Integer quarter;

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
     * 重点KPI分数(副职)
     */
    @TableField("kpiScoreAdjust")
    private BigDecimal kpiScoreAdjust;

    /**
     * 经营绩效分数(正职)
     */
    @TableField("businessScore")
    private BigDecimal businessScore;

    /**
     * 激励约束分数
     */
    @TableField("incentiveScore")
    private BigDecimal incentiveScore;

    /**
     * 难度系数
     */
    @TableField("difficultyCoefficient")
    private BigDecimal difficultyCoefficient;

    /**
     * 绩效考核系数
     */
    @TableField("incentiveCoefficient")
    private BigDecimal incentiveCoefficient;

    /**
     * 总经理经营绩效分数(不含激励约束)
     */
    @TableField("generalManagerScore")
    private BigDecimal generalManagerScore;

    /**
     * 企业名称缩写
     */
    @TableField("companyNameShort")
    private String companyNameShort;

    /**
     * 重点KPI分数(副职)
     */
    @TableField("kpiScore")
    private BigDecimal kpiScore;

    /**
     * 年度目标项目ID：建立与年度关联
     */
    @TableField("manageKpiYearId")
    private Integer manageKpiYearId;

    /**
     * 绩效优点(优点分析)
     */
    @TableField("advantageAnalyze")
    private String advantageAnalyze;

    /**
     * 绩效存在问题(现况分析)
     */
    @TableField("disadvantageAnalyze")
    private String disadvantageAnalyze;

    /**
     * 绩效风险(风险描述)
     */
    @TableField("riskDesc")
    private String riskDesc;

    /**
     * 总部管控责任部门(责任部门)
     */
    @TableField("respDepartment")
    private String respDepartment;

    /**
     * 管控措施(集团改善措施)
     */
    @TableField("groupImproveAction")
    private String groupImproveAction;
    /**
     * 绩效分数异常标识
     */
    @TableField("anomalyMark")
    private String anomalyMark;

    /**
     * 绩效异常类别
     */
    @TableField("anomalyType")
    private String anomalyType;

}
