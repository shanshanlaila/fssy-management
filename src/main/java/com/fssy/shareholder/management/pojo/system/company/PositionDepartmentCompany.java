package com.fssy.shareholder.management.pojo.system.company;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fssy.shareholder.management.pojo.common.BaseModel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * <p>
 * 基础-员工-职位-部门-公司表	
 * </p>
 *
 * @author 农浩
 * @since 2023-02-15
 */
@Getter
@Setter
@TableName("bs_user_position_department_company")
public class PositionDepartmentCompany extends BaseModel {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户ID
     */
    @TableField("userId")
    private Integer userId;

    /**
     * 用户名称
     */
    @TableField("userName")
    private String userName;

    /**
     * 1公司内部人员；0公司外部人员
     */
    @TableField("internalUser")
    private String internalUser;

    /**
     * 任职起日期
     */
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(pattern="yyyy-MM-dd",timezone="Asia/Shanghai")
    @TableField("startDate")
    private LocalDate startDate;

    /**
     * 任职止日期
     */
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(pattern="yyyy-MM-dd",timezone="Asia/Shanghai")
    @TableField("endDate")
    private LocalDate endDate;

    /**
     * 聘任文件ID
     */
    @TableField("appointPositionDocId")
    private String appointPositionDocId;

    /**
     * 聘任文件名称
     */
    @TableField("appointPositionDocName")
    private String appointPositionDocName;

    /**
     * 聘任届期：仅董事会监事会有
     */
    @TableField("appointPositionPeriod")
    private String appointPositionPeriod;

    /**
     * 仅标准：新增
     */
    @TableField("appointMode")
    private String appointMode;

    /**
     * 职位-部门-公司-ID
     */
    @TableField("postionDepartComId")
    private Integer postionDepartComId;

    /**
     * 职位ID
     */
    @TableField("positionId")
    private Integer positionId;

    /**
     * 职位名称
     */
    @TableField("positionName")
    private String positionName;

    /**
     * 职位类别ID
     */
    @TableField("positionTypeId")
    private Integer positionTypeId;

    /**
     * 职位类别名称
     */
    @TableField("positionTypeName")
    private String positionTypeName;

    /**
     * 部门-公司-ID
     */
    @TableField("departComId")
    private Integer departComId;

    /**
     * 公司ID
     */
    @TableField("companyId")
    private Integer companyId;

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
     * 1集团内公司；0集团外公司
     */
    @TableField("internalCompany")
    private String internalCompany;

    /**
     * 部门Id
     */
    @TableField("departmentId")
    private Integer departmentId;

    /**
     * 部门名称
     */
    @TableField("departmentName")
    private String departmentName;

    /**
     * 排序
     */
    @TableField("sort")
    private Integer sort;


}
