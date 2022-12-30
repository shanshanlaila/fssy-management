package com.fssy.shareholder.management.pojo.system.operate.invest;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fssy.shareholder.management.pojo.common.BaseModel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * <p>
 * **数据表名：	bs_operate_invest_project_attachment_list	**数据表中文名：	非权益投资项目管理附件清单	**业务部门：	经营管理部	**数据表作用：	非权益投资项目管理附件清单	**创建人创建日期：	TerryZeng 2022-12-2
 * </p>
 *
 * @author 农浩
 * @since 2022-12-05
 */
@Getter
@Setter
@TableName("bs_operate_invest_project_attachment_list")
public class ProjectAttachmentList extends BaseModel {

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
     * 文件状态 0：失效 1:有效可以下载，默认为1
     */
    @TableField("status")
    private Integer status;

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
     * 导入状态：0：正在导入；1：导入成功；2：导入失败，默认为0
     */
    @TableField("importStatus")
    private Integer importStatus;

    /**
     * 导入情况综述，2022-2-21添加，用于记录导入的数量情况
     */
    @TableField("conclusion")
    private String conclusion;

    /**
     * 导入模块类型表主键
     */
    @TableField("module")
    private Integer module;

    /**
     * 导入日期
     */
    @TableField("importDate")
    private LocalDate importDate;

    /**
     * 导入模块类型名称
     */
    @TableField("moduleName")
    private String moduleName;

    /**
     * 投资项目附件类别:可研报告、会议纪要、自评报告、后评价报告
     */
    @TableField("attachmentType")
    private String attachmentType;

    /**
     * 附件报告签字日期
     */
    @TableField("reportDate")
    private String reportDate;

    /**
     * 关联项目ID
     */
    @TableField("projectListId")
    private Integer projectListId;

    /**
     * 附件状态:有效、失效
     */
    @TableField("attachmentStatus")
    private String attachmentStatus;


}
