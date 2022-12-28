package com.fssy.shareholder.management.pojo.system.operate.invest;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fssy.shareholder.management.pojo.common.BaseModel;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * VIEW 重点产线产能
 * </p>
 *
 * @author zzp
 * @since 2022-12-08
 */
@Getter
@Setter
@TableName("view_product_line_capacity_list")
public class ViewProductLineCapacityList extends BaseModel {

    private static final long serialVersionUID = -6519745006918441079L;

    /**
     * 序号
     */
    @TableField("id")
    private Integer id;

    /**
     * 项目状态锁定
     */
    @TableField("status")
    private String status;

    /**
     * 企业名称
     */
    @TableField("companyName")
    private String companyName;

    /**
     * 类别
     */
    @TableField("productTypeName")
    private String productTypeName;

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
    private LocalDate sopDate;

    /**
     * 理论节拍
     */
    @TableField("designProductionTakt")
    private String designProductionTakt;

    /**
     * 理论年产能
     */
    @TableField("designCapacityPerYear")
    private String designCapacityPerYear;

    /**
     * 市场份额
     */
    @TableField("marketShares")
    private String marketShares;

    /**
     * 理论月产能
     */
    @TableField("designCapacityPerMonth")
    private String designCapacityPerMonth;

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
     * 年份
     */
    @TableField("year")
    private Integer year;

    /**
     * 季度
     */
    @TableField("quarter")
    private String quarter;

    /**
     * 当年生产车型
     */
    @TableField("yearVehicle")
    private String yearVehicle;

    /**
     * 年度累积产量
     */
    @TableField("yeildYearAccumulate")
    private String yeildYearAccumulate;

    /**
     * 季度产线负荷率
     */
    @TableField("lineLoadRateQuarter")
    private String lineLoadRateQuarter;

    /**
     * 评价
     */
    @TableField("evaluate")
    private String evaluate;

    /**
     * 本月改善措施
     */
    @TableField("improveAction")
    private String improveAction;

    /**
     * 季度累积产量
     */
    @TableField("yeildQuarterAccumulate")
    private String yeildQuarterAccumulate;

    /**
     * 月度实际产量
     */
    @TableField("yeildMonthActual")
    private String yeildMonthActual;

    /**
     * 本季度存在问题
     */
    @TableField("issueQuarter")
    private String issueQuarter;

    /**
     * 月份
     */
    @TableField("month")
    private String month;

    /**
     * 季度标识
     */
    @TableField("quarterMark")
    private String quarterMark;

}
