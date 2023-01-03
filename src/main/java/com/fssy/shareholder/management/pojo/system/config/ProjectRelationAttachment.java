package com.fssy.shareholder.management.pojo.system.config;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fssy.shareholder.management.pojo.common.BaseModel;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * *****业务部门：	经营投资科		*****数据表中文名：	投资项目清单附件关联表		*****数据表名：	bs_operate_invest_project_relation_attachment		*****数据表作用：	投资项目清单关联附件关系		*****变更记录：	时间         	变更人		变更内容	20221229	兰宇铧           	初始设计
 * </p>
 *
 * @author SHANSHAN
 * @since 2023-01-03
 */
@Getter
@Setter
@TableName("bs_operate_invest_project_relation_attachment")
public class ProjectRelationAttachment extends BaseModel {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 备注
     */
    @TableField("note")
    private String note;

    /**
     * 附件表主键
     */
    @TableField("attachmentId")
    private Long attachmentId;

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
     * 导入情况综述，2022-2-21添加，用于记录导入的数量情况
     */
    @TableField("conclusion")
    private String conclusion;

    /**
     * 导入日期
     */
    @TableField("importDate")
    private Date importDate;

    /**
     * 年度投资项目清单表主键
     */
    @TableField("projectListId")
    private Long projectListId;

    /**
     * 项目名称
     */
    @TableField("projectName")
    private String projectName;

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
    private Long companyId;


}
