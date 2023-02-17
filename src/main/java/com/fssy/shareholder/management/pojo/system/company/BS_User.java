package com.fssy.shareholder.management.pojo.system.company;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fssy.shareholder.management.pojo.common.BaseModel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * <p>
 * 基础-员工表	
 * </p>
 *
 * @author 农浩
 * @since 2023-02-16
 */
@Getter
@Setter
@TableName("bs_user")
public class BS_User extends BaseModel {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 姓名
     */
    @TableField("name")
    private String name;

    /**
     * 账户名
     */
    @TableField("account")
    private String account;

    /**
     * 密码
     */
    @TableField("password")
    private String password;

    /**
     * 性别：女0男1
     */
    @TableField("sex")
    private Integer sex;

    /**
     * 用户头像
     */
    @TableField("headImage")
    private String headImage;

    @TableField("phone")
    private String phone;

    @TableField("logsNote")
    private String logsNote;

    @TableField("superior")
    private String superior;

    /**
     * 用户状态 0：无效 1：有效（纳入绩效），默认为1，2021-7-19修改
     */
    @TableField("status")
    private Integer status;

    /**
     * 身份证号（不能重复）
     */
    @TableField("IDNumber")
    private String iDNumber;

    /**
     * 企业微信账号
     */
    @TableField("qyweixinUserId")
    private String qyweixinUserId;

    /**
     * 报表系统账号加密转url
     */
    @TableField("accountFineRsaUrl")
    private String accountFineRsaUrl;

    /**
     * 1公司内部人员；0公司外部人员
     */
    @TableField("internalUser")
    private Integer internalUser;

    /**
     * 出生年月
     */
    @TableField("birthday")
    private LocalDate birthday;

    /**
     * 学历
     */
    @TableField("educationBackground")
    private String educationBackground;

    /**
     * 简历
     */
    @TableField("profile")
    private String profile;


}
