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
 * **数据表名：	bs_manager_qualitative_eval_std	**数据表中文名：	经理人绩效定性评分各项目占比表	**业务部门：	人力资源部	**数据表作用：	记录企业年度总经理、分管经理人定性项目占比表，因为该比例每年都可能变化
 * </p>
 *
 * @author Solomon
 * @since 2022-11-28
 */
@Getter
@Setter
@TableName("bs_hr_manager_qualitative_eval_std")
public class ManagerQualitativeEvalStd extends BaseModel {

    private static final long serialVersionUID = -1488452602586579029L;

    /**
     * 序号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 占比状态：生效；失效
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
     * 技能评价占比(非总经理)
     */
    @TableField("skillScoreR")
    private Double skillScoreR;

    /**
     * 民主评议占比(非总经理)
     */
    @TableField("democraticEvalScoreR")
    private Double democraticEvalScoreR;

    /**
     * 上级评价占比(非总经理)
     */
    @TableField("superiorEvalScoreR")
    private Double superiorEvalScoreR;

    /**
     * 审计评分占比(总经理)
     */
    @TableField("auditEvalScoreR")
    private Double auditEvalScoreR;

    /**
     * 财务稽核占比(总经理)
     */
    @TableField("financialAuditScoreR")
    private Double financialAuditScoreR;

    /**
     * 运营管理占比(总经理)
     */
    @TableField("operationScoreR")
    private Double operationScoreR;

    /**
     * 组织领导力占比(总经理)
     */
    @TableField("leadershipScoreR")
    private Double leadershipScoreR;

    /**
     * 投资评价占比(总经理)
     */
    @TableField("investScoreR")
    private Double investScoreR;

    /**
     * 述职报告占比(总经理)
     */
    @TableField("workReportScoreR")
    private Double workReportScoreR;


}
