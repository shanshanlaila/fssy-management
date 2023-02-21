/**
 * ------------------------修改日志---------------------------------
 * 修改人       修改日期         修改内容
 */
package com.fssy.shareholder.management.tools.common;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.mapper.manage.department.ViewDepartmentRoleUserMapper;
import com.fssy.shareholder.management.mapper.system.performance.employee.EntryCasReviewDetailMapper;
import com.fssy.shareholder.management.mapper.system.performance.employee.EventListMapper;
import com.fssy.shareholder.management.mapper.system.performance.employee.EventsRelationRoleMapper;
import com.fssy.shareholder.management.pojo.manage.department.ViewDepartmentRoleUser;
import com.fssy.shareholder.management.pojo.manage.user.User;
import com.fssy.shareholder.management.pojo.system.performance.employee.EntryCasReviewDetail;
import com.fssy.shareholder.management.pojo.system.performance.employee.EventsRelationRole;
import com.fssy.shareholder.management.service.manage.department.DepartmentService;
import com.fssy.shareholder.management.service.manage.role.RoleService;
import com.fssy.shareholder.management.service.manage.user.UserService;
import com.fssy.shareholder.management.service.system.performance.employee.BaseService;
import com.fssy.shareholder.management.tools.constant.PerformanceConstant;
import com.fssy.shareholder.management.tools.exception.ServiceException;
import org.apache.poi.ss.formula.functions.T;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * @author MI
 * @ClassName: GetTool
 * @Description: 员工绩效工具类
 * @date 2022/11/7 11:25
 */
@Component
public class GetTool {

    @Autowired
    ViewDepartmentRoleUserMapper viewDepartmentRoleUserMapper;

    static ViewDepartmentRoleUserMapper viewDepartmentRoleUserMappers;

    @Autowired
    EventListMapper eventListMapper;

    static EventListMapper eventListMappers;

    @Autowired
    EntryCasReviewDetailMapper entryCasReviewDetailMapper;

    static EntryCasReviewDetailMapper entryCasReviewDetailMappers;

    @Autowired
    EventsRelationRoleMapper eventsRelationRoleMapper;

    static EventsRelationRoleMapper eventsRelationRoleMappers;

    @Autowired
    private DepartmentService departmentService;

    static DepartmentService departmentServices;

    @Autowired
    private RoleService roleService;

    static RoleService roleServices;

    @Autowired
    private UserService userService;

    static UserService userServices;

    @PostConstruct
    public void init() {
        viewDepartmentRoleUserMappers = viewDepartmentRoleUserMapper;
        eventListMappers = eventListMapper;
        entryCasReviewDetailMappers = entryCasReviewDetailMapper;
        eventsRelationRoleMappers = eventsRelationRoleMapper;
        departmentServices = departmentService;
        userServices = userService;
        roleServices = roleService;
    }

    /**
     * 获取部门-角色-用户视图实体类(重载)
     *
     * @param userName 职员名称
     * @return 部门-角色-用户视图实体类
     */
    public static ViewDepartmentRoleUser getDepartmentRoleByUser(String userName) {
        LambdaQueryWrapper<ViewDepartmentRoleUser> viewDepartmentRoleUserLambdaQueryWrapper = new LambdaQueryWrapper<>();
        viewDepartmentRoleUserLambdaQueryWrapper.eq(ViewDepartmentRoleUser::getUserName, userName);
        List<ViewDepartmentRoleUser> viewDepartmentRoleUsers = viewDepartmentRoleUserMappers.selectList(viewDepartmentRoleUserLambdaQueryWrapper);
        if (ObjectUtils.isEmpty(viewDepartmentRoleUsers)) {
            throw new ServiceException("当前登录用户信息不全");
        }
        return viewDepartmentRoleUsers.get(0);
    }


    /**
     * 获取部门-角色-用户视图实体类
     *
     * @param user 当前用户
     * @return 部门-角色-用户视图实体类
     */
    public static ViewDepartmentRoleUser getDepartmentRoleByUser(User user) {
        LambdaQueryWrapper<ViewDepartmentRoleUser> viewDepartmentRoleUserLambdaQueryWrapper = new LambdaQueryWrapper<>();
        viewDepartmentRoleUserLambdaQueryWrapper.eq(ViewDepartmentRoleUser::getUserId, user.getId());
        List<ViewDepartmentRoleUser> viewDepartmentRoleUsers = viewDepartmentRoleUserMappers.selectList(viewDepartmentRoleUserLambdaQueryWrapper);
        if (ObjectUtils.isEmpty(viewDepartmentRoleUsers)) {
            throw new ServiceException("当前登录用户信息不全");
        }
        return viewDepartmentRoleUsers.get(0);
    }

    /**
     * 获取部门-角色-用户视图实体类(重载)
     *
     * @return 部门-角色-用户视图实体类
     */
    public static ViewDepartmentRoleUser getDepartmentRoleByUser() {
        User user = getUser();
        LambdaQueryWrapper<ViewDepartmentRoleUser> viewDepartmentRoleUserLambdaQueryWrapper = new LambdaQueryWrapper<>();
        viewDepartmentRoleUserLambdaQueryWrapper.eq(ViewDepartmentRoleUser::getUserId, user.getId());
        List<ViewDepartmentRoleUser> viewDepartmentRoleUsers = viewDepartmentRoleUserMappers.selectList(viewDepartmentRoleUserLambdaQueryWrapper);
        if (ObjectUtils.isEmpty(viewDepartmentRoleUsers)) {
            throw new ServiceException("当前登录用户信息不全");
        }
        return viewDepartmentRoleUsers.get(0);
    }

    /**
     * 计算分数
     *
     * @param entryCasReviewDetail 总结
     * @param evaluationGrade      评价等级
     * @return 分数
     */
    public static BigDecimal getScore(EntryCasReviewDetail entryCasReviewDetail, String evaluationGrade) {
        // 获取方式改变：查询条件为部门review的departmentId、roleId、userId、year、month找事件岗位配比表，生效日期是year+month+当月最后一天之前（生效日期《=），取最近的那个（倒序，get（0））
        if (ObjectUtils.isEmpty(entryCasReviewDetail.getEventsRoleId())) {
            return null;
        }
        LambdaQueryWrapper<EventsRelationRole> relationRoleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        relationRoleLambdaQueryWrapper
                .eq(EventsRelationRole::getDepartmentId, entryCasReviewDetail.getDepartmentId())
                .eq(EventsRelationRole::getRoleId, entryCasReviewDetail.getRoleId())
                .eq(EventsRelationRole::getUserId, entryCasReviewDetail.getUserId())
                .eq(EventsRelationRole::getYear, entryCasReviewDetail.getYear())
                .eq(EventsRelationRole::getMonth, entryCasReviewDetail.getMonth())
                // 取当月最新的事件岗位配比数据
                .le(EventsRelationRole::getActiveDate, DateTool.getLastDayOfTodayString(entryCasReviewDetail.getYear(), entryCasReviewDetail.getMonth()))
                .orderByDesc(EventsRelationRole::getActiveDate);
        List<EventsRelationRole> eventsRelationRoles = eventsRelationRoleMappers.selectList(relationRoleLambdaQueryWrapper);
        if (ObjectUtils.isEmpty(eventsRelationRoles)) {
            throw new ServiceException(String.format("不存在该部门为【%s】、岗位为【%s】、姓名【%s】、年份【%s】、月份【%s】、生效日期小于【%s】事件的岗位配比数据",
                    entryCasReviewDetail.getDepartmentName(),
                    entryCasReviewDetail.getRoleName(),
                    entryCasReviewDetail.getUserName(),
                    entryCasReviewDetail.getYear(),
                    entryCasReviewDetail.getMonth(),
                    DateTool.getLastDayOfTodayString(entryCasReviewDetail.getYear(), entryCasReviewDetail.getMonth())
            ));
        }
        EventsRelationRole eventsRelationRole = eventsRelationRoles.get(0);
        entryCasReviewDetail.setEventsRoleId(eventsRelationRole.getId());
        BigDecimal autoScore;
        switch (evaluationGrade) {
            case PerformanceConstant.UNQUALIFIED:
                // evaluationGrade=‘不合格’时取delow，设置到entryCasReviewDetail.autoScore和artifactualScore；
                autoScore = eventsRelationRole.getDelow();
                break;
            case PerformanceConstant.MIDDLE:
                // evaluationGrade=‘中’时取middle，设置到entryCasReviewDetail.autoScore和artifactualScore；
                autoScore = eventsRelationRole.getMiddle();
                break;
            case PerformanceConstant.FINE:
                // evaluationGrade=‘良’时取fine，设置到entryCasReviewDetail.autoScore和artifactualScore；
                autoScore = eventsRelationRole.getFine();
                break;
            default:
                // evaluationGrade=‘优或者合格’excellent，设置到entryCasReviewDetail.autoScore和artifactualScore；
                // 结果为符合，以绩效科的审核结果为准
                autoScore = eventsRelationRole.getExcellent();
                break;
        }
        // 以年份，月份，事件岗位关系序号查询有多少条总结，以总结数除以分数，就是最终分数
        LambdaQueryWrapper<EntryCasReviewDetail> entryCasReviewDetailLambdaQueryWrapper = new LambdaQueryWrapper<>();
        entryCasReviewDetailLambdaQueryWrapper
                .eq(EntryCasReviewDetail::getEventsRoleId, eventsRelationRole.getId())
                .eq(EntryCasReviewDetail::getYear, entryCasReviewDetail.getYear())
                .eq(EntryCasReviewDetail::getMonth, entryCasReviewDetail.getMonth());
        Long count = entryCasReviewDetailMappers.selectCount(entryCasReviewDetailLambdaQueryWrapper);
        if (count == 0) {
            throw new ServiceException(String.format("未查出岗位配比序号为【%s】的数据", eventsRelationRole.getId()));
        }
        // 2022-12-16 发现问题，已经生成autoScore之后重新新增总结，此时，原来生成的autoScore没有重新除以总结数，所以额外添加修改操作
        // 重新修改
        entryCasReviewDetailLambdaQueryWrapper = new LambdaQueryWrapper<>();
        entryCasReviewDetailLambdaQueryWrapper
                .eq(EntryCasReviewDetail::getEventsRoleId, eventsRelationRole.getId())
                .eq(EntryCasReviewDetail::getYear, entryCasReviewDetail.getYear())
                .eq(EntryCasReviewDetail::getMonth, entryCasReviewDetail.getMonth())
                .ne(EntryCasReviewDetail::getId, entryCasReviewDetail.getId())
                .eq(EntryCasReviewDetail::getStatus, PerformanceConstant.FINAL)// 完结状态
                .ne(EntryCasReviewDetail::getEventsFirstType, PerformanceConstant.EVENT_FIRST_TYPE_NEW_EVENT);// 不等于新增工作流
        List<EntryCasReviewDetail> entryCasReviewDetails = entryCasReviewDetailMappers.selectList(entryCasReviewDetailLambdaQueryWrapper);
        if (!ObjectUtils.isEmpty(entryCasReviewDetails)) {
            for (EntryCasReviewDetail casReviewDetail : entryCasReviewDetails) {
                String reEvaluationGrade;
                if (PerformanceConstant.EVENT_FIRST_TYPE_TRANSACTION.equals(casReviewDetail.getEventsFirstType())) {
                    reEvaluationGrade = casReviewDetail.getFinalTransactionEvaluateLevel();
                } else {
                    reEvaluationGrade = casReviewDetail.getFinalNontransactionEvaluateLevel();
                }
                BigDecimal reAutoScore;
                switch (reEvaluationGrade) {
                    case PerformanceConstant.UNQUALIFIED:
                        // evaluationGrade=‘不合格’时取delow，设置到entryCasReviewDetail.autoScore和artifactualScore；
                        reAutoScore = eventsRelationRole.getDelow();
                        break;
                    case PerformanceConstant.MIDDLE:
                        // evaluationGrade=‘中’时取middle，设置到entryCasReviewDetail.autoScore和artifactualScore；
                        reAutoScore = eventsRelationRole.getMiddle();
                        break;
                    case PerformanceConstant.FINE:
                        // evaluationGrade=‘良’时取fine，设置到entryCasReviewDetail.autoScore和artifactualScore；
                        reAutoScore = eventsRelationRole.getFine();
                        break;
                    default:
                        // evaluationGrade=‘优或者合格’excellent，设置到entryCasReviewDetail.autoScore和artifactualScore；
                        // 结果为符合，以绩效科的审核结果为准
                        reAutoScore = eventsRelationRole.getExcellent();
                        break;
                }
                BigDecimal newAutoScore = reAutoScore.divide(new BigDecimal(count), 2, RoundingMode.HALF_UP);
                UpdateWrapper<EntryCasReviewDetail> updateWrapper = new UpdateWrapper<>();
                updateWrapper.eq("id", casReviewDetail.getId()).set("autoScore", newAutoScore);
                entryCasReviewDetailMappers.update(null, updateWrapper);
            }
        }
        return autoScore.divide(new BigDecimal(count), 2, RoundingMode.HALF_UP);
    }

    /**
     * 获取当前登录用户
     *
     * @return 当前登录用户
     */
    public static User getUser() {
        return (User) SecurityUtils.getSubject().getPrincipal();
    }

    /**
     * 获取selector数据用于前端展示
     * 使用此方法的前端xm-select的name要求如下：
     * 部门：departmentNameList
     * 角色：roleNameList
     * 用户：userList
     */
    public static void getSelectorData(Model model) {
        Map<String, Object> departmentParams = new HashMap<>(50);
        List<Map<String, Object>> departmentNameList = departmentServices.findDepartmentsSelectedDataListByParams(departmentParams, new ArrayList<>());
        model.addAttribute("departmentNameList", departmentNameList);
        Map<String, Object> roleParams = new HashMap<>(50);
        List<Map<String, Object>> roleNameList = roleServices.findRoleSelectedDataListByParams(roleParams, new ArrayList<>());
        model.addAttribute("roleNameList", roleNameList);
        Map<String, Object> userParams = new HashMap<>(50);
        List<String> selectedUserIds = new ArrayList<>(50);
        List<Map<String, Object>> userList = userServices.findUserSelectedDataListByParams(userParams, selectedUserIds);
        model.addAttribute("userList", userList);
    }

    /**
     * 封装多条件查询map
     *
     * @param request 查询请求
     * @return map
     */
    public static Map<String, Object> getParams(HttpServletRequest request) {
        Map<String, Object> params = new HashMap<>(30);
        // 所有请求参数集合map
        Map<String, String[]> parameterMap = request.getParameterMap();
        // 遍历map中的每一键值对
        for (Map.Entry<String, String[]> stringEntry : parameterMap.entrySet()) {
            // 判断值是否为空
            if (!Arrays.asList(stringEntry.getValue()).get(0).isEmpty()) {
                // 获取键
                String key = stringEntry.getKey();
                // 获取值
                String value = request.getParameter(stringEntry.getKey());
                params.put(key, value);
            }
        }
        return params;
    }

    /**
     * 获取分页数据
     *
     * @param params  参数map
     * @param request 分页数据
     * @param service service
     */
    public static <T>void getPageDataRes(Map<String, Object> result, Map<String, Object> params, HttpServletRequest request, BaseService<T> service) {
        params.put("page", Integer.parseInt(request.getParameter("page")));
        params.put("limit",  Integer.parseInt(request.getParameter("limit")));
        Page<T> page = service.findDataListByParams(params);
        if (page.getTotal() == 0) {
            result.put("code", 404);
            result.put("msg", "未查出数据");
        } else {
            // 查出数据，返回分页数据
            result.put("code", 0);
            result.put("count", page.getTotal());
            result.put("data", page.getRecords());
        }
    }
}
