package com.fssy.shareholder.management.service.system.impl.performance.employee;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fssy.shareholder.management.mapper.manage.department.DepartmentMapper;
import com.fssy.shareholder.management.mapper.manage.role.RoleMapper;
import com.fssy.shareholder.management.mapper.manage.user.UserMapper;
import com.fssy.shareholder.management.mapper.system.performance.employee.EntryCasReviewDetailMapper;
import com.fssy.shareholder.management.pojo.system.performance.employee.EntryCasReviewDetail;
import com.fssy.shareholder.management.service.system.performance.employee.EntryCasReviewDetailService;
import com.fssy.shareholder.management.tools.constant.PerformanceConstant;
import com.fssy.shareholder.management.tools.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * *****业务部门：	绩效科		*****数据表中文名：	员工履职回顾明细		*****数据表名：	bs_performance_employee_entry_cas_review_detail		*****数据表作用：	员工每月月末对月初计划的回顾		*****变更记录：	时间         	变更人		变更内容	20220915	兰宇铧           	初始设计 服务实现类
 * </p>
 *
 * @author 农浩
 * @since 2022-10-20
 */
@Service
public class EntryCasReviewDetailServiceImpl extends ServiceImpl<EntryCasReviewDetailMapper, EntryCasReviewDetail> implements EntryCasReviewDetailService {

    @Autowired
    private EntryCasReviewDetailMapper entryCasReviewDetailMapper;
    @Autowired
    private DepartmentMapper departmentMapper;
    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public Page<EntryCasReviewDetail> findDataListByParams(Map<String, Object> params) {
        QueryWrapper<EntryCasReviewDetail> queryWrapper = getQueryWrapper(params).orderByDesc("id");// 全局根据id倒序
        Page<EntryCasReviewDetail> myPage = new Page<>((int) params.get("page"), (int) params.get("limit"));
        return entryCasReviewDetailMapper.selectPage(myPage, queryWrapper);
    }

    private QueryWrapper<EntryCasReviewDetail> getQueryWrapper(Map<String, Object> params) {
        QueryWrapper<EntryCasReviewDetail> queryWrapper = new QueryWrapper<>();
        if (params.containsKey("select")) {
            queryWrapper.select((String) params.get("select"));
        }
        if (params.containsKey("id")) {
            queryWrapper.eq("id", params.get("id"));
        }
        if (params.containsKey("eventsId")) {
            queryWrapper.eq("eventsId", params.get("eventsId"));
        }
        if (params.containsKey("eventsType")) {
            queryWrapper.like("eventsType", params.get("eventsType"));
        }
        if (params.containsKey("jobName")) {
            queryWrapper.like("jobName", params.get("jobName"));
        }
        if (params.containsKey("workEvents")) {
            queryWrapper.like("workEvents", params.get("workEvents"));
        }
        if (params.containsKey("eventsForm")) {
            queryWrapper.like("eventsForm", params.get("eventsForm"));
        }
        if (params.containsKey("standardValue")) {
            queryWrapper.eq("standardValue", params.get("standardValue"));
        }
        if (params.containsKey("delowStandard")) {
            queryWrapper.like("delowStandard", params.get("delowStandard"));
        }
        if (params.containsKey("middleStandard")) {
            queryWrapper.like("middleStandard", params.get("middleStandard"));
        }
        if (params.containsKey("fineStandard")) {
            queryWrapper.like("fineStandard", params.get("fineStandard"));
        }
        if (params.containsKey("excellentStandard")) {
            queryWrapper.like("excellentStandard", params.get("excellentStandard"));
        }
        if (params.containsKey("mainOrNext")) {
            queryWrapper.like("mainOrNext", params.get("mainOrNext"));
        }
        if (params.containsKey("departmentName")) {
            queryWrapper.like("departmentName", params.get("departmentName"));
        }
        if (params.containsKey("departmentIdList")) {
            queryWrapper.in("departmentId", (List<String>) params.get("departmentIdList"));
        }
        if (params.containsKey("roleName")) {
            queryWrapper.like("roleName", params.get("roleName"));
        }
        if (params.containsKey("roleId")) {
            queryWrapper.eq("roleId", params.get("roleId"));
        }
        if (params.containsKey("userName")) {
            queryWrapper.like("userName", params.get("userName"));
        }
        if (params.containsKey("userId")) {
            queryWrapper.eq("userId", params.get("userId"));
        }
        if (params.containsKey("applyDate")) {
            queryWrapper.eq("applyDate", params.get("applyDate"));
        }
        if (params.containsKey("year")) {
            queryWrapper.eq("year", params.get("year"));
        }
        if (params.containsKey("month")) {
            queryWrapper.eq("month", params.get("month"));
        }
        if (params.containsKey("planningWork")) {
            queryWrapper.like("planningWork", params.get("planningWork"));
        }
        if (params.containsKey("times")) {
            queryWrapper.like("times", params.get("times"));
        }
        if (params.containsKey("workOutput")) {
            queryWrapper.like("workOutput", params.get("workOutput"));
        }
        if (params.containsKey("planOutput")) {
            queryWrapper.like("planOutput", params.get("planOutput"));
        }
        if (params.containsKey("planStartDate")) {
            queryWrapper.eq("planStartDate", params.get("planStartDate"));
        }
        if (params.containsKey("planEndDate")) {
            queryWrapper.eq("planEndDate", params.get("planEndDate"));
        }
        if (params.containsKey("createDate")) {
            queryWrapper.eq("createDate", params.get("createDate"));
        }
        if (params.containsKey("createName")) {
            queryWrapper.like("createName", params.get("createName"));
        }
        if (params.containsKey("createId")) {
            queryWrapper.eq("createId", params.get("createId"));
        }
        if (params.containsKey("auditName")) {
            queryWrapper.like("auditName", params.get("auditName"));
        }
        if (params.containsKey("auditId")) {
            queryWrapper.eq("auditId", params.get("auditId"));
        }
        if (params.containsKey("auditDate")) {
            queryWrapper.eq("auditDate", params.get("auditDate"));
        }
        if (params.containsKey("auditNote")) {
            queryWrapper.like("auditNote", params.get("auditNote"));
        }
        if (params.containsKey("status")) {
            queryWrapper.eq("status", params.get("status"));
        }
        if (params.containsKey("mergeNo")) {
            queryWrapper.like("mergeNo", params.get("mergeNo"));
        }
        if (params.containsKey("mergeId")) {
            queryWrapper.eq("mergeId", params.get("mergeId"));
        }
        if (params.containsKey("eventsFirstType")) {
            queryWrapper.eq("eventsFirstType", params.get("eventsFirstType"));
        }
        if (params.containsKey("note")) {
            queryWrapper.like("note", params.get("note"));
        }
        if (params.containsKey("eventsFirstType")) {
            queryWrapper.eq("eventsFirstType", params.get("eventsFirstType"));
        }
        if (params.containsKey("auditStatus")) {
            queryWrapper.like("auditStatus", params.get("auditStatus"));
        }
        if (params.containsKey("attachmentId")) {
            queryWrapper.eq("attachmentId", params.get("attachmentId"));
        }
        if (params.containsKey("statusCancel")) {
            queryWrapper.ne("status", PerformanceConstant.EVENT_LIST_STATUS_CANCEL);
        }
        if (params.containsKey("actualCompleteDate")) {
            queryWrapper.eq("actualCompleteDate", params.get("actualCompleteDate"));
        }
        if (params.containsKey("completeDesc")) {
            queryWrapper.eq("completeDesc", params.get("completeDesc"));
        }
        if (params.containsKey("chargeTransactionEvaluateLevel")) {
            queryWrapper.eq("chargeTransactionEvaluateLevel", params.get("chargeTransactionEvaluateLevel"));
        }
        if (params.containsKey("chargeTransactionBelowType")) {
            queryWrapper.eq("chargeTransactionBelowType", params.get("chargeTransactionBelowType"));
        }
        if (params.containsKey("chargeNontransactionEvaluateLevel")) {
            queryWrapper.eq("chargeNontransactionEvaluateLevel", params.get("chargeNontransactionEvaluateLevel"));
        }
        if (params.containsKey("chargeNontransactionBelowType")) {
            queryWrapper.eq("chargeNontransactionBelowType", params.get("chargeNontransactionBelowType"));
        }
        if (params.containsKey("excellentDesc")) {
            queryWrapper.eq("excellentDesc", params.get("excellentDesc"));
        }
        if (params.containsKey("isExcellent")) {
            queryWrapper.eq("isExcellent", params.get("isExcellent"));
        }
        if (params.containsKey("ministerReview")) {
            queryWrapper.eq("ministerReview", params.get("ministerReview"));
        }
        if (params.containsKey("autoScore")) {
            queryWrapper.eq("autoScore", params.get("autoScore"));
        }
        if (params.containsKey("artifactualScore")) {
            queryWrapper.eq("artifactualScore", params.get("artifactualScore"));
        }
        if (params.containsKey("casPlanId")) {
            queryWrapper.eq("casPlanId", params.get("casPlanId"));
        }
        if (params.containsKey("selfTransactionEvaluateLevel")) {
            queryWrapper.eq("selfTransactionEvaluateLevel", params.get("selfTransactionEvaluateLevel"));
        }
        if (params.containsKey("selfNontransactionEvaluateLevel")) {
            queryWrapper.eq("selfNontransactionEvaluateLevel", params.get("selfNontransactionEvaluateLevel"));
        }
        if (params.containsKey("finalTransactionEvaluateLevel")) {
            queryWrapper.eq("finalTransactionEvaluateLevel", params.get("finalTransactionEvaluateLevel"));
        }
        if (params.containsKey("finalNontransactionEvaluateLevel")) {
            queryWrapper.eq("finalNontransactionEvaluateLevel", params.get("finalNontransactionEvaluateLevel"));
        }
        return queryWrapper;


    }

    /**
     * 修改更新部 门 领导、非事务审核评价
     *
     * @param entryCasReviewDetail
     * @return
     */
    @Override
    public boolean updateEntryCasReviewDetail(EntryCasReviewDetail entryCasReviewDetail) {
        int result = entryCasReviewDetailMapper.updateById(entryCasReviewDetail);
        if (entryCasReviewDetail.getMinisterReview().equals(PerformanceConstant.REVIEW_DETAIL_MINISTER_REVIEW_EXCELLENT)) {
            entryCasReviewDetail.setStatus(PerformanceConstant.REVIEW_DETAIL_STATUS_AUDIT_A);
        } else {
            entryCasReviewDetail.setStatus(PerformanceConstant.EVENT_LIST_STATUS_FINAL);
        }
        entryCasReviewDetailMapper.update(entryCasReviewDetail,
                new LambdaQueryWrapper<EntryCasReviewDetail>().eq(EntryCasReviewDetail::getId, entryCasReviewDetail.getId()));
        return result > 0;
    }

    /**
     * 工作计划完成情况提交审核
     *
     * @param planDetailIds
     * @return
     */
    @Override
    public boolean submitAudit(List<String> planDetailIds) {
        List<EntryCasReviewDetail> entryCasReviewDetails = entryCasReviewDetailMapper.selectBatchIds(planDetailIds);
        for (EntryCasReviewDetail entryCasReviewDetail : entryCasReviewDetails) {
            // 只能提交 待提交审核 状态的事件清单
            if (entryCasReviewDetail.getStatus().equals(PerformanceConstant.PLAN_DETAIL_STATUS_SUBMIT_AUDIT)) {
                LambdaUpdateWrapper<EntryCasReviewDetail> entryCasReviewDetailLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                if (entryCasReviewDetail.getEventsFirstType().equals("事务类")) {
                    entryCasReviewDetail.setStatus(PerformanceConstant.PLAN_DETAIL_STATUS_AUDIT_KEZHANG);
                    entryCasReviewDetailLambdaUpdateWrapper.eq(EntryCasReviewDetail::getId, entryCasReviewDetail.getId());
                    entryCasReviewDetailMapper.update(entryCasReviewDetail, entryCasReviewDetailLambdaUpdateWrapper);
                } else {
                    entryCasReviewDetail.setStatus(PerformanceConstant.PLAN_DETAIL_STATUS_AUDIT_MINISTER);
                    entryCasReviewDetailLambdaUpdateWrapper.eq(EntryCasReviewDetail::getId, entryCasReviewDetail.getId());
                    entryCasReviewDetailMapper.update(entryCasReviewDetail, entryCasReviewDetailLambdaUpdateWrapper);
                }
            } else return false;
        }
        return true;
    }

    /**
     * 工作计划完成情况撤销审核
     *
     * @param planDetailIds
     * @return
     */
    @Override
    public boolean retreat(List<String> planDetailIds) {
        List<EntryCasReviewDetail> entryCasReviewDetails = entryCasReviewDetailMapper.selectBatchIds(planDetailIds);
        for (EntryCasReviewDetail entryCasReviewDetail : entryCasReviewDetails) {
            LambdaUpdateWrapper<EntryCasReviewDetail> entryCasReviewDetailLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            if (entryCasReviewDetail.getStatus().equals(PerformanceConstant.PLAN_DETAIL_STATUS_AUDIT_KEZHANG) ||
                    entryCasReviewDetail.getStatus().equals(PerformanceConstant.PLAN_DETAIL_STATUS_AUDIT_MINISTER)) {
                entryCasReviewDetail.setStatus("待提交审核");
                entryCasReviewDetailLambdaUpdateWrapper.eq(EntryCasReviewDetail::getId, entryCasReviewDetail.getId());
                entryCasReviewDetailMapper.update(entryCasReviewDetail, entryCasReviewDetailLambdaUpdateWrapper);
                continue;
            }
            //校验方法
            if (entryCasReviewDetail.getStatus().equals(PerformanceConstant.PLAN_DETAIL_STATUS_SUBMIT_AUDIT) || entryCasReviewDetail.getStatus().equals(PerformanceConstant.EVENT_LIST_STATUS_FINAL)) {
                throw new ServiceException("不能撤销待提交审核状态或完结状态下的的履职明细");
            }
        }
        return true;
    }

    @Override
    public boolean sectionWorkAudit(EntryCasReviewDetail entryCasReviewDetail) {
        // 事务类评价等级与最终非事务类评价等级值相等
        entryCasReviewDetail.setFinalNontransactionEvaluateLevel(entryCasReviewDetail.getChargeTransactionEvaluateLevel());
        return entryCasReviewDetailMapper.updateById(entryCasReviewDetail) > 0;
    }
}
