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
 * **数据表名：	bs_operat_tech_capacity_evaluate	**数据表中文名：	企业研发工艺能力评价表	**业务部门：	经营管理部	**数据表作用：	记录 企业研发工艺能力年度评价项目、存在问题及改善点	**创建人创建日期：	TerryZeng 2022-12-2
 * </p>
 *
 * @author zzp
 * @since 2022-12-08
 *
 * 服务于ViewProductLineCapacityList
 */
@Getter
@Setter
@TableName("bs_operate_product_line_capacity_month")
public class ProductLineCapacityMonth extends BaseModel {

    private static final long serialVersionUID = 7825927943606998258L;

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
     * 月份
     */
    @TableField("month")
    private String month;

    /**
     * 月度实际产量
     */
    @TableField("yeildMonthActual")
    private int yeildMonthActual;

    /**
     * 年度累积产量
     */
    @TableField("yeildYearAccumulate")
    private int yeildYearAccumulate;

    /**
     * 月度生产时长(分钟)
     */
    @TableField("uptimeMonth")
    private LocalDateTime uptimeMonth;

    /**
     * 年度累积生产时长(分钟)
     */
    @TableField("uptimeYearAccumulate")
    private LocalDateTime uptimeYearAccumulate;

    /**
     * 月度节拍
     */
    @TableField("productionTaktMonth")
    private String productionTaktMonth;

    /**
     * 年度累积节拍
     */
    @TableField("ProductionTaktYear")
    private String productionTaktYear;

    /**
     * 月度产线负荷率
     */
    @TableField("lineLoadRateMonth")
    private String lineLoadRateMonth;

    /**
     * 年度产线负荷率
     */
    @TableField("lineLoadRateYear")
    private String lineLoadRateYear;

    /**
     * 本月存在问题
     */
    @TableField("issueMonth")
    private String issueMonth;

    /**
     * 本月改善措施
     */
    @TableField("improveAction")
    private String improveAction;

    /**
     * 年度存在问题
     */
    @TableField("issueYear")
    private String issueYear;

    /**
     * 年度改善措施
     */
    @TableField("improveActionYear")
    private String improveActionYear;

    /**
     * 产线类型(自动线,人工线)
     */
    @TableField("productLineClassName")
    private String productLineClassName;

    /**
     * 季度产线负荷率
     */
    @TableField("lineLoadRateQuarter")
    private Double lineLoadRateQuarter;

    /**
     * 季度
     */
    @TableField("quarter")
    private String quarter;

    /**
     * 评价
     */
    @TableField("evaluate")
    private String evaluate;

    /**
     * 季度累积产量
     */
    @TableField("yeildQuarterAccumulate")
    private Double yeildQuarterAccumulate;

    /**
     * 本季度存在问题
     */
    @TableField("issueQuarter")
    private String issueQuarter;

    /**
     * 季度标识
     */
    @TableField("quarterMark")
    private String quarterMark;

}
