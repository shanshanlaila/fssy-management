package com.fssy.shareholder.management.pojo.system.operate.invest;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fssy.shareholder.management.pojo.common.BaseModel;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * **数据表名：	bs_operat_tech_capacity_evaluate	**数据表中文名：	企业研发工艺能力评价表	**业务部门：	经营管理部	**数据表作用：	记录 企业研发工艺能力年度评价项目、存在问题及改善点	**创建人创建日期：	TerryZeng 2022-12-2
 * </p>
 *
 * @author Solomon
 * @since 2022-12-02
 */
@Getter
@Setter
@TableName("bs_operate_tech_capacity_evaluate")
public class TechCapacityEvaluate extends BaseModel {

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
     * 指标类别
     */
    @TableField("projectTypeId")
    private String projectTypeId;

    /**
     * 指标类别
     */
    @TableField("projectName")
    private String projectName;

    /**
     * 关键指标
     */
    @TableField("kpiDesc")
    private String kpiDesc;

    /**
     * 管理方法
     */
    @TableField("manageMethod")
    private String manageMethod;

    /**
     * 指标计算公式
     */
    @TableField("kpiFormula")
    private String kpiFormula;

    /**
     * 指标创建年份
     */
    @TableField("year")
    private Integer year;

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
     * 存在问题
     */
    @TableField("issue")
    private String issue;

    /**
     * 企业自提对策
     */
    @TableField("improveActionSelf")
    private String improveActionSelf;

    /**
     * 责任人
     */
    @TableField("responsible")
    private String responsible;

    /**
     * 计划完成日期
     */
    @TableField("endDate")
    private LocalDate endDate;

    /**
     * 排序号
     */
    @TableField("serial")
    private Integer serial;

    /**
     * 过去第一年值
     */
    @TableField("pastOneYearActual")
    private BigDecimal pastOneYearActual;

    /**
     * 过去第二年值
     */
    @TableField("pastTwoYearsActual")
    private BigDecimal pastTwoYearsActual;

    /**
     * 过去第三年值
     */
    @TableField("pastThreeYearsActual")
    private BigDecimal pastThreeYearsActual;

    /**
     * 总结评价结果
     */
//    @TableField("evalRes")
//    private String evalRes;

    /**
     * 标杆值
     */
    @TableField("benchmark")
    private BigDecimal benchmark;

    /**
     * 标杆企业名称
     */
    @TableField("benchmarkCompany")
    private String benchmarkCompany;

}
