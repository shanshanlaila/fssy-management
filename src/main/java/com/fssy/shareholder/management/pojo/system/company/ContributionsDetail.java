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
 * 基础	出资方明细	
 * </p>
 *
 * @author Solomon
 * @since 2023-02-15
 */
@Getter
@Setter
@TableName("bs_contributions_detail")
public class ContributionsDetail extends BaseModel {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 出资台账ID
     */
    @TableField("contributionsListId")
    private Integer contributionsListId;

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
     * 出资文件ID
     */
    @TableField("contributionsDocId")
    private Integer contributionsDocId;

    /**
     * 出资文件名称
     */
    @TableField("contributionsDocName")
    private String contributionsDocName;

    /**
     * 出资事由
     */
    @TableField("causeDesc")
    private String causeDesc;

    /**
     * 投资方类别
     */
    @TableField("investorType")
    private String investorType;

    /**
     * 出资方Id
     */
    @TableField("investorId")
    private String investorId;

    /**
     * 投资方姓名
     */
    @TableField("investorName")
    private String investorName;

    /**
     * 出资形式
     */
    @TableField("contributionsMode")
    private String contributionsMode;

    /**
     * 注册资本
     */
    @TableField("registeredCapital")
    private String registeredCapital;

    /**
     * 实缴/认缴资本
     */
    @TableField("contributedCapital")
    private String contributedCapital;

    /**
     * 计划到账时间
     */
    @TableField("planArrivalTime")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private LocalDate planArrivalTime;

    /**
     * 实际到账时间
     */
    @TableField("actualArrivalTime")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private LocalDate actualArrivalTime;

    /**
     * 标识是否为最新记录（1，是，0，否）
     */
    @TableField("sign")
    private Integer sign;


    /**
     * 状态
     */
    @TableField("status")
    private String status;
}
