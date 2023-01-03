package com.fssy.shareholder.management.mapper.system.config;

import com.fssy.shareholder.management.pojo.system.config.ProjectRelationAttachment;
import com.fssy.shareholder.management.mapper.manage.common.MyBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * *****业务部门：	经营投资科		*****数据表中文名：	投资项目清单附件关联表		*****数据表名：	bs_operate_invest_project_relation_attachment		*****数据表作用：	投资项目清单关联附件关系		*****变更记录：	时间         	变更人		变更内容	20221229	兰宇铧           	初始设计 Mapper 接口
 * </p>
 *
 * @author SHANSHAN
 * @since 2023-01-03
 */
@Mapper
public interface ProjectRelationAttachmentMapper extends MyBaseMapper<ProjectRelationAttachment> {

}
