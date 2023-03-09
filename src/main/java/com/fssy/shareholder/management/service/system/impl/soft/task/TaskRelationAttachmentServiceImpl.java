package com.fssy.shareholder.management.service.system.impl.soft.task;

import com.fssy.shareholder.management.pojo.system.soft.task.TaskRelationAttachment;
import com.fssy.shareholder.management.mapper.system.soft.task.TaskRelationAttachmentMapper;
import com.fssy.shareholder.management.service.system.soft.task.TaskRelationAttachmentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * *****业务部门：	IT科		*****数据表中文名：	软件项目任务表关联附件表		*****数据表名：	bs_soft_task_relation_attachment		*****数据表作用：	软件项目任务表关联的附件清单（一对多）		*****变更记录：	时间         	变更人		变更内容	20230309	兰宇铧           	初始设计	 服务实现类
 * </p>
 *
 * @author SHANSHAN
 * @since 2023-03-09
 */
@Service
public class TaskRelationAttachmentServiceImpl extends ServiceImpl<TaskRelationAttachmentMapper, TaskRelationAttachment> implements TaskRelationAttachmentService {

}
