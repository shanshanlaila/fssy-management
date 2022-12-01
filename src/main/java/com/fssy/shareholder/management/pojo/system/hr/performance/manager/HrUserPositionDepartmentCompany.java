package com.fssy.shareholder.management.pojo.system.hr.performance.manager;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fssy.shareholder.management.pojo.common.BaseModel;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * **数据表名：	bs_hr_leader_position_department_company	**数据表中文名：任职履历表	**业务部门：	人力资源部	**数据表作用：	定义每名干部对应的职务、公司、部门，存在身兼多职的情况，所有历史任职都存在
 * </p>
 *
 * @author Shizn
 * @since 2022-11-29
 */
@Getter
@Setter
@TableName("bs_hr_user_position_department_company")
public class HrUserPositionDepartmentCompany extends BaseModel {

    private static final long serialVersionUID = -7902865299893549401L;

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
     * 职务状态:(在职、离职)
     */
    @TableField("status")
    private String status;

    /**
     * 1公司内部人员；0公司外部人员
     */
    @TableField("internalUser")
    private String internalUser;

    /**
     * 任职起日期
     */
    @TableField("startDate")
    private LocalDate startDate;

    /**
     * 任职止日期
     */
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
     * 经理人类型：企业总经理;企业分管经理人；集团部长；集团分管部长
     */
    @TableField("managerType")
    private String managerType;

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

    /**
     * 备注
     */
    @TableField("note")
    private String note;


}
