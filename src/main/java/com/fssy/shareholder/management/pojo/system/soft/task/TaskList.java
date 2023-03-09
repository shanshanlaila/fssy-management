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
 * *****业务部门：	IT科		*****数据表中文名：	软件项目任务清单表		*****数据表名：	bs_soft_task_list		*****数据表作用：	记录软件项目任务清单，包含了主任务和子任务（子任务允许有多层）		*****变更记录：	时间         	变更人		变更内容	20230309	兰宇铧           	初始设计	
 * </p>
 *
 * @author SHANSHAN
 * @since 2023-03-09
 */
@Getter
@Setter
@TableName("bs_soft_task_list")
public class TaskList extends BaseModel {

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
     * 状态：取值范围：	待确认：创建时状态	待领导审核：工程师登记任务时状态	执行中：科长审核通过时状态	已完成：任务完成登记时状态，如果存在子项任务，则每次完成子任务后做判断已修改父任务	已取消：删除操作时状态	默认“待确认”
     */
    @TableField("status")
    private String status;

    /**
     * 任务标题
     */
    @TableField("name")
    private String name;

    /**
     * 任务描述，使用富文本工具填报
     */
    @TableField("content")
    private String content;

    /**
     * 主任务时，字段为0，子任务时，字段为上一次任务的id，允许有多层子集
     */
    @TableField("parent")
    private Integer parent;

    /**
     * 优先级，取值范围：严重；主要；次要；不重要；无优先级，默认“无优先级”
     */
    @TableField("priority")
    private String priority;

    /**
     * 计划开始时间
     */
    @TableField("planStartDate")
    private LocalDate planStartDate;

    /**
     * 计划完成时间
     */
    @TableField("planEndDate")
    private LocalDate planEndDate;

    /**
     * 总工时，单位小时，允许是小数
     */
    @TableField("totalHour")
    private BigDecimal totalHour;

    /**
     * 已完成工时，默认为0
     */
    @TableField("completeHour")
    private BigDecimal completeHour;

    /**
     * 待完成工时，默认等于总工时
     */
    @TableField("waitCompleteHour")
    private BigDecimal waitCompleteHour;

    /**
     * 负责人姓名
     */
    @TableField("principalUserName")
    private String principalUserName;

    /**
     * 负责人主键
     */
    @TableField("principalUserId")
    private Integer principalUserId;

    /**
     * 协作人姓名拼接，使用"|"符号
     */
    @TableField("cooperationUser")
    private String cooperationUser;


}
