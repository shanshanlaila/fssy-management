package com.fssy.shareholder.management.pojo.system.operate.invest;

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
 * *****业务部门：	经营投资科		*****数据表中文名：	投资计划表(N+3)		*****数据表名：	bs_operate_invest_plan		*****数据表作用：	各企业公司的非权益投资计划情况，按公司、年、月分开记录		*****变更记录：	时间         	变更人		变更内容	20230103	兰宇铧           	初始设计
 * </p>
 *
 * @author SHANSHAN
 * @since 2023-01-03
 */
@Getter
@Setter
@TableName("bs_operate_invest_plan")
public class Plan extends BaseModel {

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
     * 预算金额，单位：万元等，对应I列
     */
    @TableField("budget")
    private BigDecimal budget;

    /**
     * 一级类别，对应B列
     */
    @TableField("firstType")
    private String firstType;

    /**
     * 二级类别，对应C列
     */
    @TableField("secondType")
    private String secondType;

    /**
     * 单位：如万元、%
     */
    @TableField("unit")
    private String unit;

    /**
     * 三级类型，对应D列，允许为空
     */
    @TableField("thirdType")
    private String thirdType;

    /**
     * 评价，先录入后面再加是否合格判断，对应K
     */
    @TableField("evaluate")
    private String evaluate;

    /**
     * 人工录入涨幅，对应J
     */
    @TableField("incrementArtificial")
    private BigDecimal incrementArtificial;

    /**
     * 人工录入涨幅
     */
    @TableField("incrementAuto")
    private BigDecimal incrementAuto;


}
