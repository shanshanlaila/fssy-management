/**
 * ------------------------修改日志---------------------------------
 * 修改人			修改日期			修改内容
 */
package com.fssy.shareholder.management.service.system.impl.performance.employee.relationRole;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fssy.shareholder.management.mapper.system.performance.employee.EventsRelationRoleMapper;
import com.fssy.shareholder.management.pojo.manage.user.User;
import com.fssy.shareholder.management.pojo.system.performance.employee.EventsRelationRole;
import com.fssy.shareholder.management.service.system.performance.employee.EventsRelationRoleService;
import com.fssy.shareholder.management.tools.common.GetTool;
import com.fssy.shareholder.management.tools.common.InstandTool;
import com.fssy.shareholder.management.tools.constant.PerformanceConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

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
    public Page<EventsRelationRole> findDataListByParams(Map<String, Object> params) {
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

    @Override
    public boolean isExistExportData() {
        User user = GetTool.getUser();
        LambdaQueryWrapper<EventsRelationRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EventsRelationRole::getStatus, PerformanceConstant.WAIT_PLAN)
                .eq(EventsRelationRole::getUserId, user.getId());
        List<EventsRelationRole> selectList = eventsRelationRoleMapper.selectList(wrapper);
        return !ObjectUtils.isEmpty(selectList);
    }

    @SuppressWarnings("unchecked")
    private QueryWrapper<EventsRelationRole> getQueryWrapper(Map<String, Object> params) {
        QueryWrapper<EventsRelationRole> queryWrapper = new QueryWrapper<>();
        // 事件清单岗位关系表主键查询
        if (params.containsKey("id")) {
            queryWrapper.eq("id", params.get("id"));
        }
        // 事件表主键主键查询
        if (params.containsKey("eventsId")) {
            queryWrapper.eq("eventsId", params.get("eventsId"));
        }
        // 事件表主键主键列表查询
        if (params.containsKey("eventsIds")) {
            queryWrapper.in("eventsId", (List<String>) params.get("eventsIds"));
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
            String departmentIdsStr = (String) params.get("departmentIds");
            List<String> departmentIds = Arrays.asList(departmentIdsStr.split(","));
            queryWrapper.in("departmentId", departmentIds);
        }
        // 角色表主键列表查询
        if (params.containsKey("roleIds")) {
            String roleIdsStr = (String) params.get("roleIds");
            List<String> roleIds = Arrays.asList(roleIdsStr.split(","));
            queryWrapper.in("roleId", roleIds);
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
            String userIdsStr = (String) params.get("userIds");
            List<String> userIds = Arrays.asList(userIdsStr.split(","));
            queryWrapper.in("userId", userIds);
        }
        if (params.containsKey("idDesc")) {
            queryWrapper.orderByDesc("id");
        }

        queryWrapper.like(params.containsKey("jobName"), "jobName", params.get("jobName"));

        queryWrapper.like(params.containsKey("workEvents"), "workEvents", params.get("workEvents"));

        if (params.containsKey("groupBy")) {
            queryWrapper.groupBy(InstandTool.objectToString(params.get("groupBy")));
        }
        if (params.containsKey("select")) {
            queryWrapper.select(InstandTool.objectToString(params.get("select")));
        }
        // 编制月份
        if (params.containsKey("month")) {
            queryWrapper.eq("month", params.get("month"));
        }
        if (params.containsKey("eventsFirstType")) {
            queryWrapper.eq("eventsFirstType", params.get("eventsFirstType"));
        }
        // 状态
        queryWrapper.eq(params.containsKey("status"), "status", params.get("status"));
        queryWrapper.eq(params.containsKey("officeId"), "officeId", params.get("officeId"));



        return queryWrapper;
    }
}
