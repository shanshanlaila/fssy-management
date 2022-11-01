package com.fssy.shareholder.management.pojo.system.performance.manage_kpi;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fssy.shareholder.management.pojo.common.BaseModel;
import lombok.Getter;
import lombok.Setter;

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
     * KPI项目描述
     */
    @TableField("projectDesc")
    private String projectDesc;


}
