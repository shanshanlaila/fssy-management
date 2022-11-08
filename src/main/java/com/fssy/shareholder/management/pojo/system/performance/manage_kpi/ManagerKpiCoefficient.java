package com.fssy.shareholder.management.pojo.system.performance.manage_kpi;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fssy.shareholder.management.pojo.common.BaseModel;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 经理人绩效系数表
 * </p>
 *
 * @author zzp
 * @since 2022-11-07
 */
@Getter
@Setter
@TableName("bs_manager_kpi_coefficient")
public class ManagerKpiCoefficient extends BaseModel {

    private static final long serialVersionUID = 1L;

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
     * 绩效难度系数
     */
    @TableField("difficultCoefficient")
    private BigDecimal difficultCoefficient;

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
     * 绩效考核系数
     */
    @TableField("incentiveCoefficient")
    private BigDecimal incentiveCoefficient;


}
