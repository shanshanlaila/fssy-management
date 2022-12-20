package com.fssy.shareholder.management.pojo.system.operate.analysis;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fssy.shareholder.management.pojo.common.BaseModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 变动分析表实体类
 *
 * @author Solomon
 * @since 2022-12-20
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("bs_operate_profit_analysis")
public class ProfitAnalysis extends BaseModel
{

	private static final long serialVersionUID = 1L;

	@TableId(value = "id", type = IdType.AUTO)
	private Integer id;

	/**
	 * 备注
	 */
	@TableField("note")
	private String note;

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
	private Integer companyId;

	/**
	 * 申报年份
	 */
	@TableField("year")
	private Integer year;

	/**
	 * 申报月份
	 */
	@TableField("month")
	private Integer month;

	/**
	 * 项目代码
	 */
	@TableField("projectCode")
	private String projectCode;

	/**
	 * 项目
	 */
	@TableField("project")
	private String project;

	/**
	 * 当年累计实际金额
	 */
	@TableField("accumulateMoney")
	private BigDecimal accumulateMoney;

	/**
	 * 本期累计预算金额
	 */
	@TableField("accumulateBudget")
	private BigDecimal accumulateBudget;

	/**
	 * 去年同期累计实际金额
	 */
	@TableField("lastYearAccumulate")
	private BigDecimal lastYearAccumulate;

	/**
	 * 全年计划预算金额
	 */
	@TableField("yearBudget")
	private BigDecimal yearBudget;

	/**
	 * 去年同期相比变化：金额变化（金额差）
	 */
	@TableField("moneyChangeToLastYear")
	private BigDecimal moneyChangeToLastYear;

	/**
	 * 去年同期相比变化：量差
	 */
	@TableField("differenceToLastYear")
	private BigDecimal differenceToLastYear;

	/**
	 * 去年同期相比变化：结构差
	 */
	@TableField("structureToLastYear")
	private BigDecimal structureToLastYear;

	/**
	 * 去年同期相比变化：价格差
	 */
	@TableField("priceToLastYear")
	private BigDecimal priceToLastYear;

	/**
	 * 去年同期相比变化：比对
	 */
	@TableField("comparisonToLastYear")
	private BigDecimal comparisonToLastYear;

	/**
	 * 与同期预算相比：金额
	 */
	@TableField("moneyChangeToBudget")
	private BigDecimal moneyChangeToBudget;

	/**
	 * 与同期预算相比：量差
	 */
	@TableField("differenceToBudget")
	private BigDecimal differenceToBudget;

	/**
	 * 与同期预算相比：结构差
	 */
	@TableField("structureToBudget")
	private BigDecimal structureToBudget;

	/**
	 * 与同期预算相比：价格差
	 */
	@TableField("priceToBudget")
	private BigDecimal priceToBudget;

	/**
	 * 与同期预算相比：比对
	 */
	@TableField("comparisonToBudget")
	private BigDecimal comparisonToBudget;

	/**
	 * 录入人
	 */
	@TableField("createName")
	private String createName;

	/**
	 * 录入日期
	 */
	@TableField("createDate")
	private LocalDateTime createDate;

}
