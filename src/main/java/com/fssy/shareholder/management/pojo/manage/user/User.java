package com.fssy.shareholder.management.pojo.manage.user;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fssy.shareholder.management.pojo.common.BaseModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户实体类
 * 
 * @author Solomon
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("basic_user")
public class User extends BaseModel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5081564418234260683L;

	@TableId(type = IdType.AUTO)
	private Long id;

	private String name;

	private String account;

	private String password;

	private String salt;

	private String IDNumber;

	private int sex;

	private String headImage;

	private String phone;

	private Long superior;

	private int status;

	private int active;

	private String qyweixinUserId;

	private int initializeType;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
	private Date passwordEditDate;

	private Integer isSupplier;

	private String ssoToken;

	private String allowWechat;

}
