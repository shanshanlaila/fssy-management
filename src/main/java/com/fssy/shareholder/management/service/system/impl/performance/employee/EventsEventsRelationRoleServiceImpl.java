/**
 * ------------------------修改日志---------------------------------
 * 修改人			修改日期			修改内容
 */
package com.fssy.shareholder.management.service.system.impl.performance.employee;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fssy.shareholder.management.mapper.system.performance.employee.EventsRelationRoleMapper;
import com.fssy.shareholder.management.pojo.system.performance.employee.EventsRelationRole;
import com.fssy.shareholder.management.service.system.performance.employee.EventsRelationRoleService;
import com.fssy.shareholder.management.tools.common.InstandTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * <p>
 * *****业务部门： IT科 *****数据表中文名： 事件清单主担岗位关系表 *****数据表名：
 * bs_performance_events_relation_main_role *****数据表作用： 事件清单对应的主担岗位 *****变更记录：
 * 时间 变更人 变更内容 20220915 兰宇铧 初始设计 服务实现类
 * </p>
 *
 * @author Solomon
 * @since 2022-10-10
 */
@Service
public class EventsEventsRelationRoleServiceImpl
        extends ServiceImpl<EventsRelationRoleMapper, EventsRelationRole>
        implements EventsRelationRoleService {
    /**
     * 事件清单岗位关系数据访问实现类
     */
    @Autowired
    private EventsRelationRoleMapper eventsRelationRoleMapper;

    @Override
    @Transactional
    public EventsRelationRole savePerformanceEventsRelationRole(
            EventsRelationRole eventsRelationRole) {
        eventsRelationRoleMapper.insert(eventsRelationRole);
        return eventsRelationRole;
    }

    @Override
    public List<EventsRelationRole> findPerformanceEventsRelationRoleDataListByParams(
            Map<String, Object> params) {
        QueryWrapper<EventsRelationRole> queryWrapper = getQueryWrapper(params);
        return eventsRelationRoleMapper.selectList(queryWrapper);

    }

    @Override
    public Page<EventsRelationRole> findPerformanceEventsRelationRoleDataListPerPageByParams(
            Map<String, Object> params) {
        QueryWrapper<EventsRelationRole> queryWrapper = getQueryWrapper(params);
        int limit = (int) params.get("limit");
        int page = (int) params.get("page");
        Page<EventsRelationRole> myPage = new Page<>(page, limit);
        return eventsRelationRoleMapper.selectPage(myPage, queryWrapper);
    }

    @Override
    public List<Map<String, Object>> findRelationRoleMapDataByParams(
            Map<String, Object> params) {
        QueryWrapper<EventsRelationRole> queryWrapper = getQueryWrapper(params);
        return eventsRelationRoleMapper.selectMaps(queryWrapper);
    }

    @Override
    public Page<Map<String, Object>> findPerformanceEventsRelationRoleDataMapListPerPageByParams(
            Map<String, Object> params) {
        QueryWrapper<EventsRelationRole> queryWrapper = getQueryWrapper(params);
        int limit = (int) params.get("limit");
        int page = (int) params.get("page");
        Page<Map<String, Object>> myPage = new Page<>(page, limit);
        return eventsRelationRoleMapper.selectMapsPage(myPage, queryWrapper);
    }

    @Override
    public List<Map<String, Object>> findPerformanceEventsRelationRoleSelectedDataListByParams(
            Map<String, Object> params, List<String> selectedIds) {
        QueryWrapper<EventsRelationRole> queryWrapper = getQueryWrapper(params);
        List<EventsRelationRole> vehicleList = eventsRelationRoleMapper
                .selectList(queryWrapper);
        List<Map<String, Object>> resultList = new ArrayList<>();
        // 为选取的数据添加selected属性
        Map<String, Object> result;
        for (EventsRelationRole eventsRelationRole : vehicleList) {
            result = new HashMap<String, Object>();
            result.put("name", eventsRelationRole.getUserName());
            result.put("value", eventsRelationRole.getId());
            result.put("id", eventsRelationRole.getId());
            boolean selected = false;
            for (int i = 0; i < selectedIds.size(); i++) {
                if (selectedIds.get(i)
                        .equals(InstandTool.objectToString(eventsRelationRole.getId()))) {
                    selected = true;
                    break;
                }
            }
            result.put("selected", selected);
            resultList.add(result);
        }
        return resultList;
    }

    @Override
    @Transactional
    public Map<String, Object> updatePerformanceEventsRelationRole(
            EventsRelationRole eventsRelationRole) {
        eventsRelationRoleMapper.updateById(eventsRelationRole);
        return null;
    }

    @SuppressWarnings("unchecked")
    private QueryWrapper<EventsRelationRole> getQueryWrapper(Map<String, Object> params) {
        QueryWrapper<EventsRelationRole> queryWrapper = new QueryWrapper<>();
        // 事件清单岗位关系表主键查询
        if (params.containsKey("id")) {
            queryWrapper.eq("id", params.get("id"));
        }
        // 事件清单岗位关系表主键列表查询
        if (params.containsKey("ids")) {
            queryWrapper.in("id", (List<String>) params.get("ids"));
        }
        // 事件表主键主键查询
        if (params.containsKey("eventsId")) {
            queryWrapper.eq("eventsId", params.get("eventsId"));
        }
        // 事件表主键主键列表查询
        if (params.containsKey("eventsIds")) {
            queryWrapper.in("eventsId", (List<String>) params.get("eventsIds"));
        }
        // 岗位名称精确查询
        if (params.containsKey("roleNameEq")) {
            queryWrapper.eq("roleName", params.get("roleNameEq"));
        }
        // 岗位主键精确查询
        if (params.containsKey("roleId")) {
            queryWrapper.eq("roleId", params.get("roleId"));
        }
        if (params.containsKey("year")) {
            queryWrapper.eq("year", params.get("year"));
        }
        if (params.containsKey("month")) {
            queryWrapper.eq("month", params.get("month"));
        }
        // 生效日期查询
        if (params.containsKey("activeDateStart")) {
            queryWrapper.ge("activeDate", params.get("activeDateStart"));
        }
        if (params.containsKey("activeDateEnd")) {
            queryWrapper.le("activeDate", params.get("activeDateEnd"));
        }
        // 编制日期查询
        if (params.containsKey("createDateStart")) {
            queryWrapper.ge("createDate", params.get("createDateStart"));
        }
        if (params.containsKey("createDateEnd")) {
            queryWrapper.le("createDate", params.get("createDateEnd"));
        }
        // 部门名称精确查询
        if (params.containsKey("departmentNameEq")) {
            queryWrapper.eq("departmentName", params.get("departmentNameEq"));
        }
        // 部门主键精确查询
        if (params.containsKey("departmentId")) {
            queryWrapper.eq("departmentId", params.get("departmentId"));
        }
        // 事件类别精确查询
        if (params.containsKey("eventsTypeEq")) {
            queryWrapper.eq("eventsType", params.get("eventsTypeEq"));
        }
        if (params.containsKey("isMainOrNext")) {
            queryWrapper.eq("isMainOrNext", params.get("isMainOrNext"));
        }
        // 用户名称精确查询
        if (params.containsKey("userNameEq")) {
            queryWrapper.eq("userName", params.get("userNameEq"));
        }
        // 用户主键精确查询
        if (params.containsKey("userId")) {
            queryWrapper.eq("userId", params.get("userId"));
        }
        // 部门表主键列表查询
        if (params.containsKey("departmentIds")) {
            queryWrapper.in("departmentId", (List<String>) params.get("departmentIds"));
        }
        // 角色表主键列表查询
        if (params.containsKey("roleIds")) {
            queryWrapper.in("roleId", (List<String>) params.get("roleIds"));
        }
        // 岗位名称查询
        if (params.containsKey("roleName")) {
            queryWrapper.like("roleName", params.get("roleName"));
        }
        // 部门名称查询
        if (params.containsKey("departmentName")) {
            queryWrapper.like("departmentName", params.get("departmentName"));
        }
        // 事件类别查询
        if (params.containsKey("eventsType")) {
            queryWrapper.like("eventsType", params.get("eventsType"));
        }
        // 用户名称查询
        if (params.containsKey("userName")) {
            queryWrapper.like("userName", params.get("userName"));
        }
        // 用户表主键列表查询
        if (params.containsKey("userIds")) {
            queryWrapper.in("userId", (List<String>) params.get("userIds"));
        }
        if (params.containsKey("idDesc")) {
            queryWrapper.orderByDesc("id");
        }
        if (params.containsKey("groupBy")) {
            queryWrapper.groupBy(InstandTool.objectToString(params.get("groupBy")));
        }
        if (params.containsKey("select")) {
            queryWrapper.select(InstandTool.objectToString(params.get("select")));
        }
        // 年月组合查询
        if (params.containsKey("yearAndMonth")) {
            String yearAndMonth = (String) params.get("yearAndMonth");
            List<String> asList = Arrays.asList(yearAndMonth.split("-"));
            queryWrapper.eq("year", asList.get(0)).eq("month", asList.get(1));
        }
        return queryWrapper;
    }
}
