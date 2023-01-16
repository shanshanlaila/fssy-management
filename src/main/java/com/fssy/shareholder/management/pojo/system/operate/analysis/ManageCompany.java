package com.fssy.shareholder.management.pojo.system.operate.analysis;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fssy.shareholder.management.pojo.common.BaseModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * <p>
 * *****业务部门： 经营分析科 *****数据表中文名： 授权访问的公司代码列表 *****数据表名：
 * bs_operate_manage_company *****数据表作用： 经营分析可以授权访问的公司列表 *****变更记录： 时间 变更人 变更内容
 * 20221213 兰宇铧 初始设计
 * </p>
 *
 * @author Solomon
 * @since 2022-12-13
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("bs_operate_manage_company")
public class ManageCompany extends BaseModel
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
	@TableField("code")
	private String code;

	/**
	 * 公司名称
	 */
	@TableField("name")
	private String name;
	
	/**
	 * 状态：停用、启用
	 */
	@TableField("status")
	private String status;
	
	/**
	 * 启用日期
	 */
	@TableField("activeDate")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
	private LocalDate activeDate;
	
	/**
	 * 停用日期
	 */
	@TableField("inactiveDate")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
	private LocalDate inactiveDate;

	/**
	 * 基础公司表主键
	 */
	@NotNull(message = "基础公司不能为空")
	@TableField("basicCompanyId")
	private Long basicCompanyId;
	
	/**
	 * 公司简称
	 */
	@NotNull(message = "公司简称不能为空")
	@NotBlank(message = "公司简称不能为空")
	@TableField("shortName")
	private String shortName;
}
