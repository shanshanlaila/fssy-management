package com.fssy.shareholder.management.mapper.system.soft.task;

import com.fssy.shareholder.management.pojo.system.soft.task.TaskRelationCooperation;
import com.fssy.shareholder.management.mapper.manage.common.MyBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * *****业务部门：	IT科		*****数据表中文名：	软件项目任务关联协作人表		*****数据表名：	bs_soft_task_relation_cooperation		*****数据表作用：	记录任务关联的协作人		*****变更记录：	时间         	变更人		变更内容	20230309	兰宇铧           	初始设计	 Mapper 接口
 * </p>
 *
 * @author SHANSHAN
 * @since 2023-03-09
 */
@Mapper
public interface TaskRelationCooperationMapper extends MyBaseMapper<TaskRelationCooperation> {

}
