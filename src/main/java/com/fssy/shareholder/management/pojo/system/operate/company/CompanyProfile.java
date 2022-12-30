package com.fssy.shareholder.management.pojo.system.operate.company;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fssy.shareholder.management.pojo.common.BaseModel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 部门：		时间：2022/7/15		表名：公司简介		表名：company_profile		用途：存储公司的基本信息和简介
 * </p>
 *
 * @author 农浩
 * @since 2022-12-13
 */
@Getter
@Setter
@TableName("bs_company_profile")
public class CompanyProfile extends BaseModel {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 公司id
     */
    @TableField("companyId")
    private Integer companyId;

    /**
     * 统一社会信用代码
     */
    @TableField("companyCode")
    private String companyCode;

    /**
     * 公司名称
     */
    @TableField("companyName")
    private String companyName;

    /**
     * 公司简称
     */
    @TableField("companyShortName")
    private String companyShortName;

    /**
     * 类型
     */
    @TableField("type")
    private String type;

    /**
     * 法定代表人
     */
    @TableField("legalPerson")
    private String legalPerson;

    /**
     * 注册资本金
     */
    @TableField("registeredCapital")
    private String registeredCapital;

    /**
     * 实缴资本
     */
    @TableField("contributedCapital")
    private String contributedCapital;

    /**
     * 成立日期
     */
    @TableField("dateOfEstablishment")
    private LocalDate dateOfEstablishment;

    /**
     * 核准日期
     */
    @TableField("dateOfApproval")
    private LocalDate dateOfApproval;

    /**
     * 营业期限
     */
    @TableField("businessTerm")
    private String businessTerm;

    /**
     * 登记机关
     */
    @TableField("registrationAuthority")
    private String registrationAuthority;

    /**
     * 行政划分
     */
    @TableField("administrationPartition")
    private String administrationPartition;

    /**
     * 工商注册号
     */
    @TableField("businessRegistrationNumber")
    private String businessRegistrationNumber;

    /**
     * 官网
     */
    @TableField("officialWebsite")
    private String officialWebsite;

    /**
     * 邮政编码
     */
    @TableField("postCode")
    private String postCode;

    /**
     * 地址
     */
    @TableField("address")
    private String address;

    /**
     * 经营状态
     */
    @TableField("managementFroms")
    private String managementFroms;

    /**
     * 纳税人识别号
     */
    @TableField("taxpayerID")
    private String taxpayerID;

    /**
     * 所属行业
     */
    @TableField("industryInvolved")
    private String industryInvolved;

    /**
     * 组织机构
     */
    @TableField("institutionalFramework")
    private String institutionalFramework;

    /**
     * 纳税人资
     */
    @TableField("taxpayerCapital")
    private String taxpayerCapital;

    /**
     * 参保人数
     */
    @TableField("contributors")
    private String contributors;

    /**
     * 曾用名
     */
    @TableField("formerName")
    private String formerName;

    /**
     * 简介
     */
    @TableField("brief")
    private String brief;

    /**
     * 经营范围
     */
    @TableField("operatingRange")
    private String operatingRange;

    /**
     * 更新时间
     */
    @TableField("changeTime")
    private LocalDateTime changeTime;


}
