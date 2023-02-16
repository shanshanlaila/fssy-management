package com.fssy.shareholder.management.pojo.system.company;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fssy.shareholder.management.pojo.common.BaseModel;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * 基础	股权明细
 * </p>
 *
 * @author Solomon
 * @since 2023-02-15
 */
@Getter
@Setter
@TableName("bs_stock_rights_detail")
public class RightsDetail extends BaseModel {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 股权台账id
     */
    @TableField("stockRightsListId")
    private Integer stockRightsListId;

    /**
     * 被投资公司ID
     */
    @TableField("companyId")
    private String companyId;

    /**
     * 投资公司名称
     */
    @TableField("companyName")
    private String companyName;

    /**
     * 股权文件id
     */
    @TableField("stockRightsDocId")
    private Integer stockRightsDocId;

    /**
     * 股权文件名称
     */
    @TableField("stockRightsDocName")
    private String stockRightsDocName;

    /**
     * 股权变更原因
     */
    @TableField("causeDesc")
    private String causeDesc;

    /**
     * 股权类别：成立股权；股权变更
     */
    @TableField("stockRightsType")
    private String stockRightsType;

    /**
     * 股权方类别：自然人股东、法人股东
     */
    @TableField("investorType")
    private String investorType;

    /**
     * 股权方ID
     */
    @TableField("investorId")
    private Integer investorId;

    /**
     * 股权方姓名
     */
    @TableField("investorName")
    private String investorName;

    /**
     * 变更前比例
     */
    @TableField("beforeRightsRatio")
    private String beforeRightsRatio;

    /**
     * 变更后/现在比例
     */
    @TableField("rightsRatio")
    private String rightsRatio;

    /**
     * 变更时间
     */
    @TableField("changeTime")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private LocalDate changeTime;

    /**
     * 标识是否是最新的记录（1为最新，0为历史记录）
     */
    @TableField("sign")
    private Integer sign;

    /**
     * 状态
     */
    @TableField("status")
    private String status;


}
