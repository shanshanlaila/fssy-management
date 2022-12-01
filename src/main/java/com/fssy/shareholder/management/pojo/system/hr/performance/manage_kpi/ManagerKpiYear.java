package com.fssy.shareholder.management.pojo.system.hr.performance.manage_kpi;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fssy.shareholder.management.pojo.common.BaseModel;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * <p>
 * 经理人年度kpi指标
 * </p>
 *
 * @author zzp
 * @since 2022-10-26
 */
@Getter
@Setter
@TableName("bs_manager_kpi_year")
public class ManagerKpiYear extends BaseModel {

    private static final long serialVersionUID = 2610864134805417949L;

    /**
     * 序号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 经理人KPI项目状态:生效、失效
     */
    @TableField("status")
    private String status;

    /**
     * 备注
     */
    @TableField("note")
    private String note;
    /**
     * 数据来源
     */
    @TableField("dataSource")
    private String dataSource;
    /**
     * 权重：%
     */
    @TableField("proportion")
    private BigDecimal proportion;

    /**
     * 经理人姓名
     */
    @TableField("managerName")
    private String managerName;


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
     * 年度目标项目ID：建立与年度关联
     */
    @TableField("manageKpiYearId")
    private Integer manageKpiYearId;
//    ----------------------------
    /**
     * 年份
     */
    @TableField("year")
    private Integer year;
    /**
     * 项目类别
     */
    @TableField("projectType")
    private String projectType;
    /**
     * KPI项目描述
     */
    @TableField("projectDesc")
    private String projectDesc;
    /**
     * 绩效标识：识别经营管理指标和绩效指标
     */
    @TableField("managerKpiMark")
    private String managerKpiMark;
    /**
     * 是否是外派经理
     */
    @TableField("expatriateManager")
    private String expatriateManager;


}
