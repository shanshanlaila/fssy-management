package com.fssy.shareholder.management.pojo.system.performance.manager;

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
 * **	经理人绩效汇总评分表  实体类
 * </p>
 *
 * @author Shizn
 * @since 2022-11-30
 */
@Getter
@Setter
@TableName("bs_hr_manager_performance_eval")
public class HrManagerPerformanceEval extends BaseModel {

    private static final long serialVersionUID = 9211249107741018013L;

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
     * 企业名称缩写
     */
    @TableField("companyNameShort")
    private String companyNameShort;

    /**
     * 定量评价分数
     */
    @TableField("kpiScore")
    private BigDecimal kpiScore;

    /**
     * 定性评价分数
     */
    @TableField("qualitativeScore")
    private Double qualitativeScore;

    /**
     * 优点分析
     */
    @TableField("advantageAnalyze")
    private String advantageAnalyze;

    /**
     * 现况分析
     */
    @TableField("disadvantageAnalyze")
    private String disadvantageAnalyze;

    /**
     * 风险描述
     */
    @TableField("riskDesc")
    private String riskDesc;

    /**
     * 责任部门
     */
    @TableField("respDepartment")
    private String respDepartment;

    /**
     * 集团改善措施
     */
    @TableField("groupImproveAction")
    private String groupImproveAction;

    /**
     * 定量评价月份
     */
    @TableField("kpiScoreMonth")
    private Integer kpiScoreMonth;

    /**
     * 定性、定量评价占比id
     */
    @TableField("evalStdId")
    private Integer evalStdId;

    /**
     * 经理人月份KPI绩效分数
     */
    @TableField("kpiScoreId")
    private Integer kpiScoreId;

    /**
     * 定性评价得分id
     */
    @TableField("qualitativeEvalId")
    private Integer qualitativeEvalId;
    /**
     * 干部履历表id
     */
    @TableField("userPositionId")
    private Integer userPositionId;

    /**
     * 定量评价占比
     */
    @TableField("kpiScoreR")
    private BigDecimal kpiScoreR;

    /**
     * 定性评价占比
     */
    @TableField("qualitativeScoreR")
    private Double qualitativeScoreR;
    /**
     * 定性评价占比
     */
    @TableField("scoreAdjustCause")
    private String scoreAdjustCause;


}
