package com.fssy.shareholder.management.service.system.impl.soft.task;

import com.fssy.shareholder.management.pojo.system.soft.task.TaskHourCondition;
import com.fssy.shareholder.management.mapper.system.soft.task.TaskHourConditionMapper;
import com.fssy.shareholder.management.service.system.soft.task.TaskHourConditionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * *****业务部门：	IT科		*****数据表中文名：	软件项目任务工时登记情况表		*****数据表名：	bs_soft_task_hour_condition		*****数据表作用：	记录没有任务的工时情况		*****变更记录：	时间         	变更人		变更内容	20230309	兰宇铧           	初始设计	 服务实现类
 * </p>
 *
 * @author SHANSHAN
 * @since 2023-03-09
 */
@Service
public class TaskHourConditionServiceImpl extends ServiceImpl<TaskHourConditionMapper, TaskHourCondition> implements TaskHourConditionService {

}
