package com.fssy.shareholder.management.pojo.system.company;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fssy.shareholder.management.pojo.common.BaseModel;
import java.io.Serializable;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 基础	营业执照	
 * </p>
 *
 * @author Solomon
 * @since 2023-02-16
 */
@Getter
@Setter
@TableName("bs_business_license_warranty")
public class LicenseWarranty extends BaseModel {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 被投资公司ID
     */
    @TableField("companyId")
    private Integer companyId;

    /**
     * 被投资公司名称
     */
    @TableField("companyName")
    private String companyName;

    /**
     * 营业执照附件ID
     */
    @TableField("businessLicenseDocId")
    private Integer businessLicenseDocId;

    /**
     * 营业执照附件名称
     */
    @TableField("businessLicenseDocName")
    private String businessLicenseDocName;

    /**
     * 变更时间
     */
    @TableField("changeTime")
    private LocalDate changeTime;

    /**
     * 变更项目
     */
    @TableField("changeProject")
    private String changeProject;

    /**
     * 变更前
     */
    @TableField("changeBefore")
    private String changeBefore;

    /**
     * 变更后
     */
    @TableField("changeAfter")
    private String changeAfter;

    /**
     * 状态
     */
    @TableField("status")
    private String status;


}
