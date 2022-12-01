package com.fssy.shareholder.management.mapper.system.hr.performance.employee;

import com.fssy.shareholder.management.pojo.system.hr.performance.employee.EntryCasReviewDetail;
import com.fssy.shareholder.management.mapper.manage.common.MyBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * *****业务部门：	绩效科		*****数据表中文名：	员工履职回顾明细		*****数据表名：	bs_performance_employee_entry_cas_review_detail		*****数据表作用：	员工每月月末对月初计划的回顾		*****变更记录：	时间         	变更人		变更内容	20220915	兰宇铧           	初始设计 Mapper 接口
 * </p>
 *
 * @author 农浩
 * @since 2022-10-20
 */
@Mapper
public interface EntryCasReviewDetailMapper extends MyBaseMapper<EntryCasReviewDetail> {

}
