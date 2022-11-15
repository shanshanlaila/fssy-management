/**
 * ------------------------修改日志---------------------------------
 * 修改人       修改日期         修改内容
 */
package com.fssy.shareholder.management.tools.common;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fssy.shareholder.management.mapper.manage.department.ViewDepartmentRoleUserMapper;
import com.fssy.shareholder.management.mapper.system.performance.employee.EntryCasReviewDetailMapper;
import com.fssy.shareholder.management.mapper.system.performance.employee.EventListMapper;
import com.fssy.shareholder.management.pojo.manage.department.ViewDepartmentRoleUser;
import com.fssy.shareholder.management.pojo.manage.user.User;
import com.fssy.shareholder.management.pojo.system.performance.employee.EntryCasReviewDetail;
import com.fssy.shareholder.management.pojo.system.performance.employee.EventList;
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

    @PostConstruct
    public void init() {
        viewDepartmentRoleUserMappers = viewDepartmentRoleUserMapper;
        eventListMappers = eventListMapper;
        entryCasReviewDetailMappers = entryCasReviewDetailMapper;
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
    public static BigDecimal getScore (EntryCasReviewDetail entryCasReviewDetail, String ministerReview) {
        // 通过事件清单序号（eventsId）找对应的事件清单，delow、middle、fine、excellent，
        EventList eventList = eventListMappers.selectById(entryCasReviewDetail.getEventsId());
        BigDecimal autoScore;
        switch (ministerReview) {
            case PerformanceConstant.UNQUALIFIED:
                // ministerReview=‘不合格’时取delow，设置到entryCasReviewDetail.autoScore和artifactualScore；
                autoScore = eventList.getDelow();
                break;
            case PerformanceConstant.MIDDLE:
                // ministerReview=‘中’时取middle，设置到entryCasReviewDetail.autoScore和artifactualScore；
                autoScore = eventList.getMiddle();
                break;
            case PerformanceConstant.FINE:
                // ministerReview=‘良’时取fine，设置到entryCasReviewDetail.autoScore和artifactualScore；
                autoScore = eventList.getMiddle();
                break;
            default:
                // ministerReview=‘优或者合格’excellent，设置到entryCasReviewDetail.autoScore和artifactualScore；
                autoScore = eventList.getExcellent();
                break;
        }
        // 以年份，月份，事件清单序号查询有多少条回顾，以回顾数除以分数，就是最终分数
        LambdaQueryWrapper<EntryCasReviewDetail> entryCasReviewDetailLambdaQueryWrapper = new LambdaQueryWrapper<>();
        entryCasReviewDetailLambdaQueryWrapper
                .eq(EntryCasReviewDetail::getYear, eventList.getYear())
                .eq(EntryCasReviewDetail::getMonth, eventList.getMonth())
                .eq(EntryCasReviewDetail::getEventsId, eventList.getId());
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