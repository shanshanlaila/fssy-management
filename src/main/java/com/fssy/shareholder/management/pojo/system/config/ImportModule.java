/**   
 * ------------------------修改日志---------------------------------
 * 修改人			修改日期			修改内容
 */
package com.fssy.shareholder.management.pojo.system.config;

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
 * <p>
 * *****业务部门： IT科 *****数据表中文名： 导入场景表 *****数据表名： bs_common_import_module
 * *****数据表作用： 各导入模块（场景）记录 *****变更记录： 时间 变更人 变更内容 20220915 兰宇铧 初始设计
 * </p>
 *
 * @author Solomon
 * @since 2022-10-11
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("bs_common_import_module")
public class ImportModule extends BaseModel
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6369144394561385941L;

	@TableId(value = "id", type = IdType.AUTO)
	private Integer id;

	/**
	 * 备注
	 */
	@TableField("note")
	private String note;

	/**
	 * 导入模块名称
	 */
	@TableField("name")
	private String name;

}
