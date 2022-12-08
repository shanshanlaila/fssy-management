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
 * **数据表名：	bs_operate_invest_project_month	**数据表中文名：	项目月度进展表	**业务部门：	经营管理部	**数据表作用：	记录 企业年度投资项目月度状态	**创建人创建日期：	TerryZeng 2022-12-2
 * </p>
 *
 * @author zzp
 * @since 2022-12-05
 */
@Getter
@Setter
@TableName("bs_operate_invest_project_month")
public class InvestProjectMonth extends BaseModel {

    private static final long serialVersionUID = 9007665933145901152L;

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
     * 项目名称
     */
    @TableField("projectName")
    private String projectName;

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
     * 月度投资额
     */
    @TableField("investmentVolumeMonth")
    private String investmentVolumeMonth;

    /**
     * 年度累积投资额
     */
    @TableField("investmentVolumeAccumulate")
    private String investmentVolumeAccumulate;

    /**
     * 项目月度状态
     */
    @TableField("projectStatusMonth")
    private String projectStatusMonth;

    /**
     * 月度项目总结
     */
    @TableField("projectReportMonth")
    private String projectReportMonth;

    /**
     * 项目月度风险
     */
    @TableField("projectRiskMonth")
    private String projectRiskMonth;

    /**
     * 项目月度存在问题
     */
    @TableField("monthIssue")
    private String monthIssue;

    /**
     * 项目月度改善措施
     */
    @TableField("improveAction")
    private String improveAction;

    /**
     * 企业名称
     */
    @TableField("companyName")
    private String companyName;


}
