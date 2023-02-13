/**
 * ------------------------修改日志---------------------------------
 * 修改人       修改日期         修改内容
 */
package com.fssy.shareholder.management.service.system.performance.employee;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fssy.shareholder.management.pojo.system.performance.employee.EventsRelationRole;

import java.util.Map;

/**
 * @author MI
 * @ClassName: BaseService
 * @Description: TODO
 * @date 2023/2/13 8:45
 */
public interface BaseService<T> extends IService<T> {

    /**
     * 根据参数获取数据列表
     *
     * @param params 参数map
     * @return 分页对象
     */
    Page<T> findDataListByParams(Map<String, Object> params);

    //Page<EventsRelationRole> findDataListByParams(Map<String, Object> params);
}
