package com.fssy.shareholder.management.pojo.system.operate.invest;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fssy.shareholder.management.pojo.common.BaseModel;
import java.io.Serializable;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * **数据表名：	bs_operate_process_ability_list	**数据表中文名：	工艺基础能力台账	**业务部门：	经营管理部	**数据表作用：	管理工艺基础能力对比情况台账	**创建人创建日期：	TerryZeng 2022-12-2
 * </p>
 *
 * @author Solomon
 * @since 2022-12-08
 */
@Getter
@Setter
@TableName("bs_operate_process_ability_list")
public class ProcessAbilityList extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 序号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 项目状态锁定
     */
    @TableField("status")
    private String status;

    /**
     * 备注
     */
    @TableField("note")
    private String note;

    /**
     * 指标创建年份
     */
    @TableField("year")
    private Integer year;

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
     * 企业名称简称
     */
    @TableField("companyShortName")
    private String companyShortName;

    /**
     * 主要竞争对手
     */
    @TableField("competeCompanyName")
    private String competeCompanyName;

    /**
     * 产品类别名称
     */
    @TableField("productLineTypeName")
    private String productLineTypeName;

    /**
     * 行业标杆企业
     */
    @TableField("benchmarkCompanyName")
    private String benchmarkCompanyName;

    /**
     * 工艺能力状态
     */
    @TableField("processAbilityStatus")
    private String processAbilityStatus;

    /**
     * 产品名称
     */
    @TableField("productName")
    private String productName;

    /**
     * 报告完成日期
     */
    @TableField("reportDate")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private LocalDate reportDate;


}
