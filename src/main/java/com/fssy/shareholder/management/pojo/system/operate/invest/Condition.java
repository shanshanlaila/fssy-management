package com.fssy.shareholder.management.pojo.system.operate.invest;

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
 * *****业务部门：	经营投资科		*****数据表中文名：	投资执行情况表		*****数据表名：	bs_operate_invest_condition		*****数据表作用：	各企业公司的非权益投资执行情况，按公司、年、月分开记录		*****变更记录：	时间         	变更人		变更内容	20230103	兰宇铧           	初始设计
 * </p>
 *
 * @author 农浩
 * @since 2023-01-03
 */
@Getter
@Setter
@TableName("bs_operate_invest_condition")
public class Condition extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 序号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 备注
     */
    @TableField("note")
    private String note;

    /**
     * 预算创建年份
     */
    @TableField("year")
    private Integer year;

    /**
     * 预算创建月份
     */
    @TableField("month")
    private Integer month;

    /**
     * 公司代码
     */
    @TableField("companyCode")
    private String companyCode;

    /**
     * 公司名称
     */
    @TableField("companyName")
    private String companyName;

    /**
     * 公司表序号
     */
    @TableField("companyId")
    private Long companyId;

    /**
     * 原EXCEL行号，对应B列
     */
    @TableField("lineNumber")
    private Integer lineNumber;

    /**
     * 一级类别，对应C列
     */
    @TableField("firstType")
    private String firstType;

    /**
     * 指标，对应D列
     */
    @TableField("item")
    private String item;

    /**
     * 预算金额，单位：万元等，对应I列
     */
    @TableField("budget")
    private BigDecimal budget;

    /**
     * 全年实绩，单位：万元等，对应H列
     */
    @TableField("accumulation")
    private BigDecimal accumulation;

    /**
     * 单位：如万元、%，对应F列
     */
    @TableField("unit")
    private String unit;

    /**
     * 定义，对应E列
     */
    @TableField("`describe`")
    private String describe;

    /**
     * 预算完成比例I列
     */
    @TableField("proportion")
    private BigDecimal proportion;

    /**
     * 评价，对应J列
     */
    @TableField("evaluate")
    private String evaluate;
    /**
     * 企业简称，对应J列
     */
    @TableField("companyShortName")
    private String companyShortName;


}
