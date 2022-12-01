/**   
 * ------------------------修改日志---------------------------------
 * 修改人			修改日期			修改内容
 */
package com.fssy.shareholder.management.service.system.impl.performance.employee;

import com.fssy.shareholder.management.pojo.system.hr.performance.employee.EntryCasMerge;
import com.fssy.shareholder.management.mapper.system.hr.performance.employee.EntryCasMergeMapper;
import com.fssy.shareholder.management.service.system.hr.performance.employee.EntryCasMergeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * *****业务部门： 绩效科 *****数据表中文名： 员工履职表 *****数据表名：
 * bs_performance_employee_entry_cas_merge *****数据表作用：
 * 员工每月月初填写的履职情况计划表，以及在计划上写回顾，评价都在这里 *****变更记录： 时间 变更人 变更内容 20220915 兰宇铧 初始设计
 * 服务实现类
 * </p>
 *
 * @author Solomon
 * @since 2022-10-11
 */
@Service
public class EntryCasMergeServiceImpl extends ServiceImpl<EntryCasMergeMapper, EntryCasMerge> implements EntryCasMergeService {

}
