package com.fssy.shareholder.management.pojo.system.performance.manager;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fssy.shareholder.management.pojo.common.BaseModel;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * **数据表名：	bs_manager_qualitative_eval	**数据表中文名：	经理人绩效定性评价分数表	**业务部门：	人力资源部	**数据表作用：	记录企业年度总经理、分管经理人定性评价分数
 * </p>
 *
 * @author zzp
 * @since 2022-11-28
 */
@Getter
@Setter
@TableName("bs_hr_manager_qualitative_eval")
public class ManagerQualitativeEval extends BaseModel {

    private static final long serialVersionUID = -1449703154431814705L;

    /**
     * 序号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 定性评价标识：生效；失效
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
     * 企业名称缩写
     */
    @TableField("companyNameShort")
    private String companyNameShort;

    /**
     * 技能评价得分(非总经理)
     */
    @TableField("skillScore")
    private Double skillScore;

    /**
     * 民主评议得分(非总经理)
     */
    @TableField("democraticEvalScore")
    private Double democraticEvalScore;

    /**
     * 上级评价得分(非总经理)
     */
    @TableField("superiorEvalScore")
    private Double superiorEvalScore;

    /**
     * 年度重点工作事件(非总经理)
     */
    @TableField("yearImportantEvents")
    private String yearImportantEvents;

    /**
     * 年度跟踪结果(非总经理)
     */
    @TableField("eventsTrackAnnual")
    private String eventsTrackAnnual;

    /**
     * 半年度跟踪结果(非总经理)
     */
    @TableField("eventsTrackSemiannual")
    private String eventsTrackSemiannual;

    /**
     * 审计评分(总经理)
     */
    @TableField("auditEvalScore")
    private Double auditEvalScore;

    /**
     * 财务稽核得分(总经理)
     */
    @TableField("financialAuditScore")
    private Double financialAuditScore;

    /**
     * 运营管理得分(总经理)
     */
    @TableField("operationScore")
    private Double operationScore;

    /**
     * 组织领导力得分(总经理)
     */
    @TableField("leadershipScore")
    private Double leadershipScore;

    /**
     * 定性评价综合得分（自动）
     */
    @TableField("qualitativeEvalScoreAuto")
    private Double qualitativeEvalScoreAuto;

    /**
     * 投资评价得分(总经理)
     */
    @TableField("investScore")
    private Double investScore;

    /**
     * 述职报告得分(总经理)
     */
    @TableField("workReportScore")
    private Double workReportScore;

    /**
     * 定性评价综合得分（人工调整）
     */
    @TableField("qualitativeEvalScoreAdjust")
    private Double qualitativeEvalScoreAdjust;

    /**
     * 人工调整分数原因
     */
    @TableField("adjustCause")
    private String adjustCause;

    /**
     * 定性评价比例ID
     */
    @TableField("evalStdId")
    private Integer evalStdId;

}
