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
 * **数据表名：	bs_operate_process_ability_detail	**数据表中文名：	工艺能力基础详情	**业务部门：	经营管理部	**数据表作用：	记录 企业工艺能力基础详情，含有企业、竞争对手情况	**创建人创建日期：	TerryZeng 2022-12-2
 * </p>
 *
 * @author Solomon
 * @since 2022-12-07
 */
@Getter
@Setter
@TableName("bs_operate_process_ability_detail")
public class ProcessAbilityDetail extends BaseModel {

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
     * 工艺能力台账ID
     */
    @TableField("processAbilityListId")
    private Integer processAbilityListId;

    /**
     * 一级工艺分类
     */
    @TableField("processLevelOne")
    private String processLevelOne;

    /**
     * 二级工艺分类
     */
    @TableField("processLevelTwo")
    private String processLevelTwo;

    /**
     * 三级工艺分类
     */
    @TableField("processLevelThree")
    private String processLevelThree;

    /**
     * 四级工艺分类(底层)
     */
    @TableField("processLevelFour")
    private String processLevelFour;

    /**
     * 方盛企业是否有该能力：○：1，0
     */
    @TableField("companyHaveStatus")
    private String companyHaveStatus;

    /**
     * 竞争对手有该能力●：1，0
     */
    @TableField("competeCompanyHaveStatus")
    private String competeCompanyHaveStatus;

    /**
     * 未来计划提升工艺■；1，0
     */
    @TableField("improveProcess")
    private String improveProcess;

    /**
     * 重点关键设备★：1.0
     */
    @TableField("keyMachine")
    private String keyMachine;


}
