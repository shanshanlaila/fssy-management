package com.fssy.shareholder.management.pojo.system.company;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fssy.shareholder.management.pojo.common.BaseModel;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * **数据表名：	bs_operate_company_finance_data	**数据表中文名：	企业财务基础数据(暂用于非权益投资管理)	**业务部门：	经营管理部	**数据表作用：	存放企业财务基础信息，方便在非权益投资页面计算。暂用于人工导入，未来采用数据对接	**创建人创建日期：	TerryZeng 2022-12-2
 * </p>
 *
 * @author 农浩
 * @since 2022-12-07
 */
@Getter
@Setter
@TableName("bs_operate_company_finance_data")
public class FinanceData extends BaseModel {

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
     * 月份
     */
    @TableField("month")
    private Integer month;

    /**
     * 经营性利润
     */
    @TableField("operatingProfit")
    private Double operatingProfit;

    /**
     * 上年末固定资产净值
     */
    @TableField("netValueOfFixedAssets")
    private Double netValueOfFixedAssets;

    /**
     * 研发费用
     */
    @TableField("researchDevelopmentCosts")
    private Double researchDevelopmentCosts;

    /**
     * 主营业务收入
     */
    @TableField("mainBusinessIncome")
    private Double mainBusinessIncome;


}
