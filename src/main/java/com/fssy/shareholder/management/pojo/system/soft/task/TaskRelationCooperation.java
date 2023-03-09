package com.fssy.shareholder.management.pojo.system.soft.task;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fssy.shareholder.management.pojo.common.BaseModel;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * *****业务部门：	IT科		*****数据表中文名：	软件项目任务关联协作人表		*****数据表名：	bs_soft_task_relation_cooperation		*****数据表作用：	记录任务关联的协作人		*****变更记录：	时间         	变更人		变更内容	20230309	兰宇铧           	初始设计	
 * </p>
 *
 * @author SHANSHAN
 * @since 2023-03-09
 */
@Getter
@Setter
@TableName("bs_soft_task_relation_cooperation")
public class TaskRelationCooperation extends BaseModel {

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
     * 协作人名称
     */
    @TableField("cooperationUserName")
    private String cooperationUserName;

    /**
     * 协作人主键
     */
    @TableField("cooperationUserId")
    private Integer cooperationUserId;

    /**
     * 软件项目任务清单表主键
     */
    @TableField("softTaskId")
    private Integer softTaskId;


}
