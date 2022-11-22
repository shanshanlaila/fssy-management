package com.fssy.shareholder.management.pojo.manage.company;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fssy.shareholder.management.pojo.common.BaseModel;
import org.openxmlformats.schemas.officeDocument.x2006.docPropsVTypes.I4Document;

/**
 * 公司实体类</br>
 * 对应表为company表
 * 
 * @author Shizn
 */
@TableName("basic_company")
public class Company extends BaseModel
{

	/**
	 *
	 */
	private static final long serialVersionUID = -6080195749539778878L;

	@TableId(type = IdType.AUTO)
	private Integer id;

	private String name;

	private String shortName;

	@Override
	public String toString() {
		return "company{" +
				"id=" + id +
				", name='" + name + '\'' +
				", shortName='" + shortName + '\'' +
				'}';
	}

	public Company(Integer id, String name, String shortName) {
		this.id = id;
		this.name = name;
		this.shortName = shortName;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
}
