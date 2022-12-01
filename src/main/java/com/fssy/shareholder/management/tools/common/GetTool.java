/**
 * ------------------------修改日志---------------------------------
 * 修改人       修改日期         修改内容
 */
package com.fssy.shareholder.management.tools.common;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fssy.shareholder.management.mapper.manage.department.ViewDepartmentRoleUserMapper;
import com.fssy.shareholder.management.mapper.system.performance.employee.EntryCasReviewDetailMapper;
import com.fssy.shareholder.management.mapper.system.performance.employee.EventListMapper;
import com.fssy.shareholder.management.mapper.system.performance.employee.EventsRelationRoleMapper;
import com.fssy.shareholder.management.pojo.manage.department.ViewDepartmentRoleUser;
import com.fssy.shareholder.management.pojo.manage.user.User;
import com.fssy.shareholder.management.pojo.system.performance.employee.EntryCasReviewDetail;
import com.fssy.shareholder.management.pojo.system.performance.employee.EventsRelationRole;
import com.fssy.shareholder.management.tools.constant.PerformanceConstant;
import com.fssy.shareholder.management.tools.exception.ServiceException;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

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

    @PostConstruct
    public void init() {
        viewDepartmentRoleUserMappers = viewDepartmentRoleUserMapper;
        eventListMappers = eventListMapper;
        entryCasReviewDetailMappers = entryCasReviewDetailMapper;
        eventsRelationRoleMappers = eventsRelationRoleMapper;
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
     * @param entryCasReviewDetail 回顾
     * @param ministerReview       部长审核结果
     * @return 分数
     */
    public static BigDecimal getScore(EntryCasReviewDetail entryCasReviewDetail, String ministerReview) {
        // 通过事件清单序号（eventsId）找对应的事件清单，delow、middle、fine、excellent，
        //EventsRelationRole eventsRelationRole = eventsRelationRoleMappers.selectById(entryCasReviewDetail.getEventsRoleId());
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
            throw new ServiceException("不存在该事件的岗位配比数据");
        }
        EventsRelationRole eventsRelationRole = eventsRelationRoles.get(0);
        BigDecimal autoScore;
        switch (ministerReview) {
            case PerformanceConstant.UNQUALIFIED:
                // ministerReview=‘不合格’时取delow，设置到entryCasReviewDetail.autoScore和artifactualScore；
                autoScore = eventsRelationRole.getDelow();
                break;
            case PerformanceConstant.MIDDLE:
                // ministerReview=‘中’时取middle，设置到entryCasReviewDetail.autoScore和artifactualScore；
                autoScore = eventsRelationRole.getMiddle();
                break;
            case PerformanceConstant.FINE:
                // ministerReview=‘良’时取fine，设置到entryCasReviewDetail.autoScore和artifactualScore；
                autoScore = eventsRelationRole.getFine();
                break;
            default:
                // ministerReview=‘优或者合格’excellent，设置到entryCasReviewDetail.autoScore和artifactualScore；
                // 结果为符合，以绩效科的审核结果为准
                autoScore = eventsRelationRole.getExcellent();
                break;
        }
        // 以年份，月份，事件岗位关系序号查询有多少条回顾，以回顾数除以分数，就是最终分数
        LambdaQueryWrapper<EntryCasReviewDetail> entryCasReviewDetailLambdaQueryWrapper = new LambdaQueryWrapper<>();
        entryCasReviewDetailLambdaQueryWrapper
                .eq(EntryCasReviewDetail::getYear, eventsRelationRole.getYear())
                .eq(EntryCasReviewDetail::getMonth, eventsRelationRole.getMonth())
                .eq(EntryCasReviewDetail::getEventsRoleId, eventsRelationRole.getId());
        Long count = entryCasReviewDetailMappers.selectCount(entryCasReviewDetailLambdaQueryWrapper);
        return autoScore.divide(new BigDecimal(count)).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * 获取当前登录用户
     *
     * @return 当前登录用户
     */
    public static User getUser() {
        return (User) SecurityUtils.getSubject().getPrincipal();
    }
}
