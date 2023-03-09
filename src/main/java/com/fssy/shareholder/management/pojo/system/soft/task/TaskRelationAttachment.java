package com.fssy.shareholder.management.pojo.system.soft.task;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fssy.shareholder.management.pojo.common.BaseModel;
import java.io.Serializable;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * *****业务部门：	IT科		*****数据表中文名：	软件项目任务表关联附件表		*****数据表名：	bs_soft_task_relation_attachment		*****数据表作用：	软件项目任务表关联的附件清单（一对多）		*****变更记录：	时间         	变更人		变更内容	20230309	兰宇铧           	初始设计	
 * </p>
 *
 * @author SHANSHAN
 * @since 2023-03-09
 */
@Getter
@Setter
@TableName("bs_soft_task_relation_attachment")
public class TaskRelationAttachment extends BaseModel {

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
     * 附件表主键
     */
    @TableField("attachmentId")
    private Integer attachmentId;

    /**
     * md5加密，作为查询条件
     */
    @TableField("md5Path")
    private String md5Path;

    /**
     * 文件名称
     */
    @TableField("filename")
    private String filename;

    /**
     * 物理存储路径
     */
    @TableField("path")
    private String path;

    /**
     * 导入情况综述
     */
    @TableField("conclusion")
    private String conclusion;

    /**
     * 导入日期
     */
    @TableField("importDate")
    private LocalDate importDate;

    /**
     * 软件项目任务清单表主键
     */
    @TableField("softTaskId")
    private Integer softTaskId;


}
