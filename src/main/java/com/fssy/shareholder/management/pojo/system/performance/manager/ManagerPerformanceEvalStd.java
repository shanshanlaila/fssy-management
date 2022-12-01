package com.fssy.shareholder.management.pojo.system.performance.manager;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fssy.shareholder.management.pojo.common.BaseModel;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * **数据表名：	bs_manager_performance_std	**数据表中文名：	经理人绩效评分定性、定量分数占比表	**业务部门：	人力资源部	**数据表作用：	记录企业年度总经理、分管经理人定性、定量分数占比表，因为该比例每年都可能变化	
 * </p>
 *
 * @author Solomon
 * @since 2022-11-29
 */
@Getter
@Setter
@TableName("bs_hr_manager_performance_eval_std")
public class ManagerPerformanceEvalStd extends BaseModel {

    private static final long serialVersionUID = -8094976041607313234L;

    /**
     * 序号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 占比状态：生效；失效
     */
    @TableField("status")
    private String status;

    /**
     * 备注
     */
    @TableField("note")
    private String note;

    /**
     * 年份
     */
    @TableField("year")
    private Integer year;

    /**
     * 定量评价占比	
     */
    @TableField("kpiScoreR")
    private BigDecimal kpiScoreR;

    /**
     * 定性评价占比
     */
    @TableField("qualitativeScoreR")
    private Double qualitativeScoreR;


}
