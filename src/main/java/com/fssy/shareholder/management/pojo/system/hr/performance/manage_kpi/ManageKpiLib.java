package com.fssy.shareholder.management.pojo.system.hr.performance.manage_kpi;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fssy.shareholder.management.pojo.common.BaseModel;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 经营管理指标库 实体类
 * </p>
 *
 * @author Shizn
 * @since 2022-10-12
 */
@Getter
@Setter
@TableName("bs_manage_kpi_lib")
public class ManageKpiLib extends BaseModel {

    private static final long serialVersionUID = 4816575383586312551L;

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
     * 指标类别
     */
    @TableField("projectType")
    private String projectType;
    /**
     * KPI项目名称
     */
    @TableField("projectDesc")
    private String projectDesc;

    /**
     * 通用项目;专用项目
     */
    @TableField("isCommon")
    private String isCommon;
    /**
     * 单位
     */
    @TableField("unit")
    private String unit;
    /**
     * 是否外派财务
     */
    @TableField("cfoKpi")
    private String cfoKpi;
    /**
     * 是否经理人
     */
    @TableField("managerKpi")
    private String managerKpi;
    /**
     * 创建年份
     */
    @TableField("kpiYear")
    private Integer kpiYear;
    /**
     * KPI公式
     */
    @TableField("kpiFormula")
    private String kpiFormula;
    /**
     * kpi定义
     */
    @TableField("kpiDefinition")
    private String kpiDefinition;

    /**
     * 评价模式：系统评分、人工评分
     */
    @TableField("evaluateMode")
    private String evaluateMode;


}
