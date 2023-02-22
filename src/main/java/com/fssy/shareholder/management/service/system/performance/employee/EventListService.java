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
public interface EventListService extends BaseService<EventList> {
    /**
     * 根据参数分页查询数据
     *
     * @param params 查询参数
     * @return 数据分页
     */
    //Page<EventList> findDataListByParams(Map<String, Object> params);

    /**
     * 通过查询条件查询实时库存凭证关系台账列表(Map)
     *
     * @param params 查询条件
     * @return 实时库存凭证关系数据列表
     */
    List<Map<String, Object>> findEventListMapDataByParams(Map<String, Object> params);

    /**
     * 读取附件数据到数据库表
     *
     * @param attachment 附件
     * @return 附件
     */
    Map<String, Object> readEventListWithoutStandardDataSource(Attachment attachment);


    /**
     * 通过条件查询分页数据表
     *
     * @param params 参数
     * @return 分页数据
     */
    Page<EventList> findEventListDataPerPageByParams(Map<String, Object> params);

    /**
     * 修改无标准事件内容数据
     *
     * @param eventList 无标准事件实体
     * @return true/false
     */
    boolean updateEventList(EventList eventList);

    /**
     * 删除无标准事件内容数据
     *
     * @param id 事件清单表主键
     * @return true/false
     */

    boolean deleteEventListById(int id);

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    EventList getById(Long id);

    /**
     * 取消无标准事件清单状态变为取消
     *
     * @param id
     * @return
     */
    boolean changeStatus(Integer id);

    /**
     * 新增单条基础事件
     * @param eventList 基础事件实体
     */
    boolean insertEventList(EventList eventList);

    /**
     * 属于新增工作流的履职计划创建关联基础事件
     *
     * @param eventList 基础事件
     * @param id        计划id
     * @return 结果
     */
    boolean insertEventByPlan(EventList eventList, Long id);
}
