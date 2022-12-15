package com.fssy.shareholder.management.pojo.system.operate.analysis;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fssy.shareholder.management.pojo.common.BaseModel;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * *****业务部门： 经营分析科 *****数据表中文名： 利润表 *****数据表名： bs_operate_profit_statement
 * *****数据表作用： 各企业公司的利润表，以每月出的财务报表为基础 *****变更记录： 时间 变更人 变更内容 20221213 兰宇铧 初始设计
 * </p>
 *
 * @author Solomon
 * @since 2022-12-13
 */
@Getter
@Setter
@TableName("bs_operate_profit_statement")
public class ProfitStatement extends BaseModel
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
	 * 累积金额
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
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
	private LocalDateTime createDate;

}
