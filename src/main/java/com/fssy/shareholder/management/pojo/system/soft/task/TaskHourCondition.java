package com.fssy.shareholder.management.pojo.system.soft.task;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fssy.shareholder.management.pojo.common.BaseModel;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * *****业务部门：	IT科		*****数据表中文名：	软件项目任务工时登记情况表		*****数据表名：	bs_soft_task_hour_condition		*****数据表作用：	记录没有任务的工时情况		*****变更记录：	时间         	变更人		变更内容	20230309	兰宇铧           	初始设计	
 * </p>
 *
 * @author SHANSHAN
 * @since 2023-03-09
 */
@Getter
@Setter
@TableName("bs_soft_task_hour_condition")
public class TaskHourCondition extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 序号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 备注
     */
    @TableField("note")
    private String note;

    /**
     * 软件项目任务清单表主键
     */
    @TableField("softTaskId")
    private Integer softTaskId;

    /**
     * 任务标题
     */
    @TableField("taskName")
    private String taskName;

    /**
     * 登记工时，可为小数
     */
    @TableField("signHour")
    private BigDecimal signHour;

    /**
     * 剩余工时，自录
     */
    @TableField("residueHour")
    private BigDecimal residueHour;

    /**
     * 工作描述
     */
    @TableField("content")
    private String content;

    /**
     * 登记日期
     */
    @TableField("applyDate")
    private LocalDate applyDate;

    /**
     * 登记人
     */
    @TableField("applyName")
    private String applyName;

    /**
     * 登记人主键
     */
    @TableField("applyUserId")
    private Integer applyUserId;


}
