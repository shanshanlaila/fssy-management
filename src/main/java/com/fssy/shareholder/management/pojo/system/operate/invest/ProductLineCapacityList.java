package com.fssy.shareholder.management.pojo.system.operate.invest;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fssy.shareholder.management.pojo.common.BaseModel;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * **数据表名：	bs_operate_product_line_capacity_list	**数据表中文名：	重点产线产能数据	**业务部门：	经营管理部	**数据表作用：	记录 企业重点产线设计、SOP信息	**创建人创建日期：	TerryZeng 2022-12-2
 * </p>
 *
 * @author zzp
 * @since 2022-12-08
 *
 * 服务于ViewProductLineCapacityList
 */
@Getter
@Setter
@TableName("bs_operate_product_line_capacity_list")
public class ProductLineCapacityList extends BaseModel {

    private static final long serialVersionUID = -7360963393890025520L;

    /**
     * 序号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 项目状态锁定
     */
    @TableField("status")
    private String status;

    /**
     * 备注
     */
    @TableField("note")
    private String note;

    /**
     * 企业名称
     */
    @TableField("companyName")
    private String companyName;

    /**
     * 企业ID
     */
    @TableField("companyId")
    private Integer companyId;

    /**
     * 企业名称简称
     */
    @TableField("companyShortName")
    private String companyShortName;

    /**
     * 类别
     */
    @TableField("productTypeName")
    private String productTypeName;

    /**
     * 工艺别
     */
    @TableField("processTypeName")
    private String processTypeName;

    /**
     * 产线名称
     */
    @TableField("productLineName")
    private String productLineName;

    /**
     * 产线类型
     */
    @TableField("productLineTypeName")
    private String productLineTypeName;

    /**
     * 量产时间
     */
    @TableField("sopDate")
    private String sopDate;

    /**
     * 理论节拍
     */
    @TableField("designProductionTakt")
    private String designProductionTakt;

    /**
     * 市场份额
     */
    @TableField("marketShares")
    private String marketShares;

    /**
     * 理论年产能
     */
    @TableField("designCapacityPerYear")
    private String designCapacityPerYear;

    /**
     * 理论月产能
     */
    @TableField("designCapacityPerMonth")
    private int designCapacityPerMonth;

    /**
     * 实际节拍
     */
    @TableField("designActualTakt")
    private String designActualTakt;

    /**
     * 年度目标
     */
    @TableField("yearAim")
    private Double yearAim;

    /**
     * 重点产线产能数据台账ID
     */
    @TableField("productLineCapacityId")
    private Integer productLineCapacityId;

    /**
     * 当年生产车型
     */
    @TableField("yearVehicle")
    private String yearVehicle;

}
