package com.fssy.shareholder.management.mapper.system.performance.manage_kpi;

import com.fssy.shareholder.management.pojo.system.performance.manage_kpi.ManagerKpiScoreOld;
import com.fssy.shareholder.management.mapper.manage.common.MyBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 经理人绩效分数表		【修改内容】增加了年度关联ID字段	【修改时间】2022-10-27	【修改人】张泽鹏 Mapper 接口
 * </p>
 *
 * @author zzp
 * @since 2022-10-31
 */
@Mapper
public interface ManagerKpiScoreMapperOld extends MyBaseMapper<ManagerKpiScoreOld> {

}
