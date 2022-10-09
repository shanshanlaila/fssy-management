/*
 * @Title: fssy-management
 * @Description: TODO
 * @author MI
 * @date 2022/10/8 10:33
 * @version
 */
package com.fssy.shareholder.management.service.system.performance.employee;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.pojo.system.config.Attachment;
import com.fssy.shareholder.management.pojo.system.performance.employee.EventList;

import java.util.List;
import java.util.Map;

/**
 * @author MI
 * @ClassName: EventListService
 * @Description: 事件列表业务类接口
 * @date 2022/10/8 10:33
 */
public interface EventListService {
    /**
     * 根据参数分页查询数据
     *
     * @param params 查询参数
     * @return 数据分页
     */
    Page<EventList> findDataListByParams(Map<String, Object> params);

    /**
     * 读取附件数据到数据库表
     *
     * @param attachment 经理绩效附件
     * @return 经理绩效附件
     */
    Map<String, Object> readEventListDataSource(Attachment attachment);

    /**
     * 通过查询条件查询实时库存凭证关系台账列表(Map)
     *
     * @param params 查询条件
     * @return 实时库存凭证关系数据列表
     */
    List<Map<String, Object>> findEventListMapDataByParams(Map<String, Object> params);
}
