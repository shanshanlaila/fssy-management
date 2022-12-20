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
 * 现金流量实体类
 *
 * @author Solomon
 * @since 2022-12-20
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("bs_operate_cash_flow")
public class CashFlow extends BaseModel
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
	 * 金额
	 */
	@TableField("amount")
	private BigDecimal amount;

	/**
	 * 金额累计
	 */
	@TableField("cumulativeBalance")
	private BigDecimal cumulativeBalance;

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
