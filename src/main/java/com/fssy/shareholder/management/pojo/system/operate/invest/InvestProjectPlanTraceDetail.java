package com.fssy.shareholder.management.pojo.system.operate.invest;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fssy.shareholder.management.pojo.common.BaseModel;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * **数据表名：	bs_operate_invest_project_plan_trace	**数据表中文名：	项目进度计划跟踪表	**业务部门：	经营管理部	**数据表作用：	用于记录单一项目进展详情及跟踪情况	**创建人创建日期：	TerryZeng 2022-12-2
 * </p>
 *
 * @author Solomon
 * @since 2022-12-13
 */
@Getter
@Setter
@TableName("bs_operate_invest_project_plan_trace_detail")
public class InvestProjectPlanTraceDetail extends BaseModel {

    private static final long serialVersionUID = 1L;

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
     * 项目台账Id
     */
    @TableField("projectListId")
    private Integer projectListId;

    /**
     * 项目名称/工作内容描述
     */
    @TableField("projectName")
    private String projectName;

    /**
     * 部门名称
     */
    @TableField("departmentName")
    private String departmentName;

    /**
     * 年份
     */
    @TableField("year")
    private Integer year;

    /**
     * 公司名称
     */
    @TableField("companyName")
    private String companyName;

    /**
     * 月份
     */
    @TableField("month")
    private Integer month;

    /**
     * 总体评价
     */
    @TableField("evaluate")
    private String evaluate;

    /**
     * 摘要
     */
    @TableField("abstracte")
    private String abstracte;


}
