package com.fssy.shareholder.management.pojo.system.config;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fssy.shareholder.management.pojo.common.BaseModel;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * *****业务部门：	绩效科		*****数据表中文名：	员工月度评价情况关联附件表		*****数据表名：	bs_performance_state_relation_attachment		*****数据表作用：	员工月度评价情况关联附件关系		*****变更记录：	时间         	变更人		变更内容	20220915	兰宇铧           	初始设计
 * </p>
 *
 * @author shanshan
 * @since 2022-10-28
 */
@Getter
@Setter
@TableName("bs_performance_state_relation_attachment")
public class StateRelationAttachment extends BaseModel {

    private static final long serialVersionUID = 1L;

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
     * 员工月度履职评价表明细主键
     */
    @TableField("stateId")
    private Integer stateId;

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
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Shanghai")
    private LocalDate importDate;


}
