/**
 * ------------------------修改日志---------------------------------
 * 修改人			修改日期			修改内容
 * 兰宇铧			2022-12-30 		修改问题，先查询merge是否有数据，有直接用，没有则重新创建;修改问题，查询条件添加月份和部门并修改编号规则
 */
package com.fssy.shareholder.management.service.system.impl.performance.employee;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fssy.shareholder.management.mapper.manage.department.ViewDepartmentRoleUserMapper;
import com.fssy.shareholder.management.mapper.manage.user.UserMapper;
import com.fssy.shareholder.management.mapper.system.config.AttachmentMapper;
import com.fssy.shareholder.management.mapper.system.config.StateRelationAttachmentMapper;
import com.fssy.shareholder.management.mapper.system.performance.employee.*;
import com.fssy.shareholder.management.pojo.manage.department.ViewDepartmentRoleUser;
import com.fssy.shareholder.management.pojo.manage.user.User;
import com.fssy.shareholder.management.pojo.system.config.Attachment;
import com.fssy.shareholder.management.pojo.system.config.StateRelationAttachment;
import com.fssy.shareholder.management.pojo.system.performance.employee.*;
import com.fssy.shareholder.management.service.system.performance.employee.EntryExcellentStateDetailService;
import com.fssy.shareholder.management.tools.common.GetTool;
import com.fssy.shareholder.management.tools.common.InstandTool;
import com.fssy.shareholder.management.tools.common.IteratorTool;
import com.fssy.shareholder.management.tools.constant.PerformanceConstant;
import com.fssy.shareholder.management.tools.exception.ServiceException;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * <p>
 * *****业务部门：	绩效科		*****数据表中文名：	员工月度履职评价说明表明细		*****数据表名：	bs_performance_entry_excellent_state_detail		*****数据表作用：	员工月度履职评价为优时，填报的内容		*****变更记录：	时间         	变更人		变更内容	20220915	兰宇铧           	初始设计 服务实现类
 * </p>
 *
 * @author 农浩
 * @since 2022-10-24
 */
@Service
public class EntryExcellentStateDetailServiceImpl extends ServiceImpl<EntryExcellentStateDetailMapper, EntryExcellentStateDetail> implements EntryExcellentStateDetailService {
    @Autowired
    private EntryExcellentStateDetailMapper entryExcellentStateDetailMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private EntryCasMergeMapper entryCasMergeMapper;

    @Autowired
    private ViewDepartmentRoleUserMapper viewDepartmentRoleUserMapper;

    @Autowired
    private StateRelationMainUserMapper stateRelationMainUserMapper;

    @Autowired
    private StateRelationNextUserMapper stateRelationNextUserMapper;

    @Autowired
    private EntryExcellentStateMergeMapper entryExcellentStateMergeMapper;

    @Autowired
    private EntryCasReviewDetailMapper entryCasReviewDetailMapper;

    @Autowired
    private AttachmentMapper attachmentMapper;

    @Autowired
    private StateRelationAttachmentMapper stateRelationAttachmentMapper;

    /**
     * 根据分页查询数据
     *
     * @param params 查询参数
     */
    @Override
    public Page<EntryExcellentStateDetail> findDataListByParams(Map<String, Object> params) {
        QueryWrapper<EntryExcellentStateDetail> queryWrapper = getQueryWrapper(params);
        Page<EntryExcellentStateDetail> myPage = new Page<>((int) params.get("page"), (int) params.get("limit"));
        return entryExcellentStateDetailMapper.selectPage(myPage, queryWrapper);
    }


    private QueryWrapper<EntryExcellentStateDetail> getQueryWrapper(Map<String, Object> params) {
        QueryWrapper<EntryExcellentStateDetail> queryWrapper = new QueryWrapper<>();
        if (params.containsKey("select")) {
            queryWrapper.select((String) params.get("select"));
        }
        if (params.containsKey("id")) {
            queryWrapper.eq("id", params.get("id"));
        }
        if (params.containsKey("eventsId")) {
            queryWrapper.eq("eventsId", params.get("eventsId"));
        }
        if (params.containsKey("workEvents")) {
            queryWrapper.like("workEvents", params.get("workEvents"));
        }
        if (params.containsKey("mainUserName")) {
            queryWrapper.like("mainUserName", params.get("mainUserName"));
        }
        if (params.containsKey("completeDesc")) {
            queryWrapper.like("completeDesc", params.get("completeDesc"));
        }
        if (params.containsKey("actualCompleteDate")) {
            queryWrapper.eq("actualCompleteDate", params.get("actualCompleteDate"));
        }
        if (params.containsKey("oriTarget")) {
            queryWrapper.like("oriTarget", params.get("oriTarget"));
        }
        if (params.containsKey("actualTarget")) {
            queryWrapper.like("actualTarget", params.get("actualTarget"));
        }
        if (params.containsKey("innovative")) {
            queryWrapper.like("innovative", params.get("innovative"));
        }
        if (params.containsKey("note")) {
            queryWrapper.like("note", params.get("note"));
        }
        if (params.containsKey("departmentName")) {
            queryWrapper.like("departmentName", params.get("departmentName"));
        }
        if (params.containsKey("departmentIds")) {
            String departmentIdsStr = (String) params.get("departmentIds");
            List<String> departmentIds = Arrays.asList(departmentIdsStr.split(","));
            queryWrapper.in("departmentId", departmentIds);
        }
        if (params.containsKey("createDate")) {
            queryWrapper.eq("createDate", params.get("createDate"));
        }
        if (params.containsKey("createName")) {
            queryWrapper.eq("createName", params.get("createName"));
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
        if (params.containsKey("casPlanId")) {
            queryWrapper.eq("casPlanId", params.get("casPlanId"));
        }
        if (params.containsKey("casReviewId")) {
            queryWrapper.eq("casReviewId", params.get("casReviewId"));
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
        if (params.containsKey("ministerReview")) {
            queryWrapper.eq("ministerReview", params.get("ministerReview"));
        }
        if (params.containsKey("classReview")) {
            queryWrapper.eq("classReview", params.get("classReview"));
        }
        if (params.containsKey("ministerReviewUser")) {
            queryWrapper.like("ministerReviewUser", params.get("ministerReviewUser"));
        }
        if (params.containsKey("ministerReviewUserId")) {
            queryWrapper.eq("ministerReviewUserId", params.get("ministerReviewUserId"));
        }
        if (params.containsKey("classReviewUser")) {
            queryWrapper.like("classReviewUser", params.get("classReviewUser"));
        }
        if (params.containsKey("classReviewUserId")) {
            queryWrapper.like("classReviewUserId", params.get("classReviewUserId"));
        }
        if (params.containsKey("classReviewDate")) {
            queryWrapper.eq("classReviewDate", params.get("classReviewDate"));
        }
        if (params.containsKey("nextUserName")) {
            queryWrapper.like("nextUserName", params.get("nextUserName"));
        }
        if (params.containsKey("createdAt")) {
            queryWrapper.ge("createdAt", params.get("createdAt"));
        }
        if (params.containsKey("createdId")) {
            queryWrapper.eq("createdId", params.get("createdId"));
        }
        if (params.containsKey("createdName")) {
            queryWrapper.like("createdName", params.get("createdName"));
        }
        if (params.containsKey("updatedAt")) {
            queryWrapper.ge("updatedAt", params.get("updatedAt"));
        }
        if (params.containsKey("updatedId")) {
            queryWrapper.eq("updatedId", params.get("updatedId"));
        }
        if (params.containsKey("updatedName")) {
            queryWrapper.like("updatedName", params.get("updatedName"));
        }
        if (params.containsKey("statusOr")) {
            queryWrapper.eq("status", PerformanceConstant.WAIT_AUDIT_MANAGEMENT_CHIEF)
                    .or(item -> item.eq("status", PerformanceConstant.WAIT_AUDIT_PERFORMANCE));
        }
        if (params.containsKey("roleName")) {
            queryWrapper.like("roleName", params.get("roleName"));
        }
        if (params.containsKey("roleId")) {
            queryWrapper.eq("roleId", params.get("roleId"));
        }
        if (params.containsKey("roleIds")) {
            String roleIdsStr = (String) params.get("roleIds");
            List<String> roleIds = Arrays.asList(roleIdsStr.split(","));
            queryWrapper.in("roleId", roleIds);
        }
        if (params.containsKey("idAsc")) {
            queryWrapper.orderByAsc("id");
        } else {
            queryWrapper.orderByDesc("id");
        }
        if (params.containsKey("select")) {
            queryWrapper.select(InstandTool.objectToString(params.get("select")));
        }
        if (params.containsKey("groupBy")) {
            queryWrapper.groupBy(InstandTool.objectToString(params.get("groupBy")));
        }
        // 编制人
        if (params.containsKey("creatNameIds")) {
            String creatIdsStr = (String) params.get("creatNameIds");
            List<String> creatIds = Arrays.asList(creatIdsStr.split(","));
            queryWrapper.in("createId", creatIds);
        }
        // 实际完成日期起
        if (params.containsKey("actualCompleteDateStart")) {
            queryWrapper.ge("actualCompleteDate", params.get("actualCompleteDateStart"));
        }
        // 实际完成日期止
        if (params.containsKey("actualCompleteDateEnd")) {
            queryWrapper.le("actualCompleteDate", params.get("actualCompleteDateEnd"));
        }
        // 编制日期起
        if (params.containsKey("createDateStart")) {
            queryWrapper.ge("createDate", params.get("createDateStart"));
        }
        // 编制日期止
        if (params.containsKey("createDateEnd")) {
            queryWrapper.le("createDate", params.get("createDateStart"));
        }
        // 计划内容
        if (params.containsKey("planningWork")) {
            queryWrapper.like("planningWork", params.get("planningWork"));
        }
        return queryWrapper;
    }

    /**
     * 评优材料提交审核
     *
     * @param excellentStateDetailIds 履职总结的Ids
     */
    @Override
    public boolean submitAuditForExcellent(List<String> excellentStateDetailIds) {
        List<EntryExcellentStateDetail> entryExcellentStateDetails = entryExcellentStateDetailMapper.selectBatchIds(excellentStateDetailIds);
        for (EntryExcellentStateDetail entryExcellentStateDetail : entryExcellentStateDetails) {
            //只能提交 待提交审核 状态的评优材料
            if (entryExcellentStateDetail.getStatus().equals(PerformanceConstant.WAIT_SUBMIT_AUDIT)) {
                LambdaUpdateWrapper<EntryExcellentStateDetail> entryExcellentStateDetailLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                entryExcellentStateDetail.setStatus(PerformanceConstant.WAIT_AUDIT_PERFORMANCE);
                entryExcellentStateDetailLambdaUpdateWrapper.eq(EntryExcellentStateDetail::getId, entryExcellentStateDetail.getId());
                entryExcellentStateDetailMapper.update(entryExcellentStateDetail, entryExcellentStateDetailLambdaUpdateWrapper);
            } else {
                return false;
            }
        }
        return true;
    }

    /**
     * 评优材料撤销审核
     *
     * @param excellentStateDetailIds 评优材料的ids
     * @return 撤销结果
     */
    @Override
    public boolean retreatForExcellent(List<String> excellentStateDetailIds) {
        List<EntryExcellentStateDetail> entryExcellentStateDetails = entryExcellentStateDetailMapper.selectBatchIds(excellentStateDetailIds);
        for (EntryExcellentStateDetail entryExcellentStateDetail : entryExcellentStateDetails) {
            //校验方法
            if (entryExcellentStateDetail.getStatus().equals(PerformanceConstant.WAIT_SUBMIT_AUDIT)
                    || entryExcellentStateDetail.getStatus().equals(PerformanceConstant.FINAL) || entryExcellentStateDetail.getStatus().equals(PerformanceConstant.WAIT_AUDIT_MANAGEMENT_CHIEF)) {
                throw new ServiceException(String.format("不能撤销状态为【%s】的评优材料", entryExcellentStateDetail.getStatus()));
            }
            if (entryExcellentStateDetail.getStatus().equals(PerformanceConstant.WAIT_AUDIT_PERFORMANCE)) {
                entryExcellentStateDetail.setStatus(PerformanceConstant.WAIT_SUBMIT_AUDIT);
                entryExcellentStateDetailMapper.updateById(entryExcellentStateDetail);
            }
        }
        return true;
    }

    /**
     * 绩效科撤销审核评优材料
     *
     * @param excellentStateDetailIds 履职总结的Ids
     * @return
     */
    @Override
    public boolean PerformanceRetreat(List<String> excellentStateDetailIds) {
        List<EntryExcellentStateDetail> entryExcellentStateDetails = entryExcellentStateDetailMapper.selectBatchIds(excellentStateDetailIds);
        for (EntryExcellentStateDetail entryExcellentStateDetail : entryExcellentStateDetails) {
            if (entryExcellentStateDetail.getStatus().equals(PerformanceConstant.WAIT_AUDIT_PERFORMANCE)) {
                throw new ServiceException(String.format("不能撤销状态为【%s】的评优材料", entryExcellentStateDetail.getStatus()));
            }
            if (entryExcellentStateDetail.getStatus().equals(PerformanceConstant.WAIT_AUDIT_MANAGEMENT_CHIEF)) {
                entryExcellentStateDetail.setStatus(PerformanceConstant.WAIT_AUDIT_PERFORMANCE);
                entryExcellentStateDetail.setClassReview("");
                entryExcellentStateDetailMapper.updateById(entryExcellentStateDetail);
            }
        }
        return true;
    }

    /**
     * 绩效科审核评优材料
     *
     * @param entryExcellentStateDetail 评优材料实体
     * @return 结果
     */
    @Override
    public boolean updateExcellent(EntryExcellentStateDetail entryExcellentStateDetail, String mainIdsStr, String nextIdsStr) {
        if (ObjectUtils.isEmpty(mainIdsStr)) {
            throw new ServiceException("提交失败，请选择主担");
        }
        // 主/次担任人名称处理
        StringBuilder mainSb = new StringBuilder();
        StringBuilder nextSb = new StringBuilder();
        int countMain = 0, countNext = 0;
        String[] mainIds = mainIdsStr.split(",");
        String[] nextIds = nextIdsStr.split(",");
        for (String mainId : mainIds) {
            User mainUser = userMapper.selectById(mainId);
            countMain++;
            // 查询主担责任人，并用,拼接成字符串
            if (countMain == mainIds.length) {
                mainSb.append(mainUser.getName());
            } else {
                mainSb.append(mainUser.getName());
                mainSb.append(",");
            }
        }
        if (!ObjectUtils.isEmpty(nextIdsStr)) {
            for (String nextId : nextIds) {
                User nextUser = userMapper.selectById(nextId);
                countNext++;
                // 查询次担责任人，并用,拼接成字符串
                if (countNext == nextIds.length) {
                    nextSb.append(nextUser.getName());
                } else {
                    nextSb.append(nextUser.getName());
                    nextSb.append(",");
                }
            }
        }
        entryExcellentStateDetail.setNextUserName(nextSb.toString());
        entryExcellentStateDetail.setMainUserName(mainSb.toString());
        int result = entryExcellentStateDetailMapper.updateById(entryExcellentStateDetail);
        return result > 0;
    }

    /**
     * 经营管理部主管审核评优材料
     *
     * @param entryExcellentStateDetail 履职总结
     * @return
     */
    @Override
    public boolean updateMinister(EntryExcellentStateDetail entryExcellentStateDetail) {
        entryExcellentStateDetail.setStatus(PerformanceConstant.FINAL);
        QueryWrapper<EntryCasReviewDetail> entryCasReviewDetailQueryWrapper = new QueryWrapper<>();
        entryCasReviewDetailQueryWrapper.eq("id", entryExcellentStateDetail.getCasReviewId());
        List<EntryCasReviewDetail> entryCasReviewDetails = entryCasReviewDetailMapper.selectList(entryCasReviewDetailQueryWrapper);
        if (ObjectUtils.isEmpty(entryCasReviewDetails)) {
            throw new ServiceException(String.format("评优说明材料id【%s】不存在对应的履职总结", entryExcellentStateDetail.getId()));
        }
        EntryCasReviewDetail reviewDetail = entryCasReviewDetails.get(0);
        reviewDetail.setFinalNontransactionEvaluateLevel(entryExcellentStateDetail.getMinisterReview());
        entryCasReviewDetailMapper.updateById(reviewDetail);
        int result = entryExcellentStateDetailMapper.updateById(entryExcellentStateDetail);
        return result > 0;
    }

    /**
     * 经营管理部主管撤销审核评优材料
     *
     * @param excellentStateDetailIds 需要评优履职总结的Ids
     * @return
     */
    @Override
    public boolean MinisterRetreat(List<String> excellentStateDetailIds) {
        List<EntryExcellentStateDetail> entryExcellentStateDetails = entryExcellentStateDetailMapper.selectBatchIds(excellentStateDetailIds);
        for (EntryExcellentStateDetail entryExcellentStateDetail : entryExcellentStateDetails) {
            QueryWrapper<EntryCasReviewDetail> entryCasReviewDetailQueryWrapper = new QueryWrapper<>();
            entryCasReviewDetailQueryWrapper.eq("id", entryExcellentStateDetail.getCasReviewId());
            List<EntryCasReviewDetail> entryCasReviewDetails = entryCasReviewDetailMapper.selectList(entryCasReviewDetailQueryWrapper);
            if (ObjectUtils.isEmpty(entryCasReviewDetails)) {
                throw new ServiceException(String.format("评优说明材料id【%s】不存在对应的履职总结", entryExcellentStateDetail.getId()));
            }
            if (entryExcellentStateDetail.getStatus().equals(PerformanceConstant.WAIT_AUDIT_PERFORMANCE)
                    || entryExcellentStateDetail.getStatus().equals(PerformanceConstant.WAIT_AUDIT_MANAGEMENT_CHIEF)) {
                throw new ServiceException("此数据为【待审核】的数据，不能撤销审核");
            }
            // 只能撤销完结状态下的履职总结评优
            if (entryExcellentStateDetail.getStatus().equals(PerformanceConstant.FINAL)) {
                // 查询履职总结
                EntryCasReviewDetail reviewDetail = entryCasReviewDetails.get(0);
                entryExcellentStateDetail.setStatus(PerformanceConstant.WAIT_AUDIT_MANAGEMENT_CHIEF);
                entryExcellentStateDetail.setMinisterReview("");
                entryExcellentStateDetailMapper.updateById(entryExcellentStateDetail);
                reviewDetail.setStatus(PerformanceConstant.WAIT_AUDIT_MANAGEMENT);
                entryCasReviewDetailMapper.updateById(reviewDetail);
            }
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(EntryExcellentStateDetail entryExcellentStateDetail, Map<String, Object> param) {
        if (param.containsKey("mainIds")) {
            String mainIdsStr = (String) param.get("mainIds");
            if (ObjectUtils.isEmpty(mainIdsStr)) {
                throw new ServiceException("提交失败，请选择主担");
            }
        }
        // 查询当前登录用户对应的部门和角色信息
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        LambdaQueryWrapper<ViewDepartmentRoleUser> druWrapper = new LambdaQueryWrapper<>();
        druWrapper.eq(ViewDepartmentRoleUser::getUserId, user.getId());
        List<ViewDepartmentRoleUser> viewDepartmentRoleUsers = viewDepartmentRoleUserMapper.selectList(druWrapper);
        if (ObjectUtils.isEmpty(viewDepartmentRoleUsers)) {
            throw new ServiceException(String.format("登陆人id为【%s】的用户，不存在对应的部门，提交失败", user.getId()));
        }
        ViewDepartmentRoleUser viewDepartmentRoleUser = viewDepartmentRoleUsers.get(0);

        // 主/次担任人名称处理
        String mainIdsStr = (String) param.get("mainIds");
        String nextIdsStr = (String) param.get("nextIds");
        StringBuilder mainSb = new StringBuilder();
        StringBuilder nextSb = new StringBuilder();
        int countMain = 0, countNext = 0;
        String[] mainIds = mainIdsStr.split(",");
        String[] nextIds = nextIdsStr.split(",");
        for (String mainId : mainIds) {
            User mainUser = userMapper.selectById(mainId);
            countMain++;
            // 查询主担责任人，并用,拼接成字符串
            if (countMain == mainIds.length) {
                mainSb.append(mainUser.getName());
            } else {
                mainSb.append(mainUser.getName());
                mainSb.append(",");
            }
        }
        int result;
        if (!ObjectUtils.isEmpty(nextIdsStr)) {
            for (String nextId : nextIds) {
                User nextUser = userMapper.selectById(nextId);
                countNext++;
                // 查询次担责任人，并用,拼接成字符串
                if (countNext == nextIds.length) {
                    nextSb.append(nextUser.getName());
                } else {
                    nextSb.append(nextUser.getName());
                    nextSb.append(",");
                }
            }
        }

        int year = LocalDate.now().getYear();
        int month = LocalDate.now().getMonthValue();
        // 按照申报年份、月份加申报部门创建表“bs_performance_entry_excellent_state_merge”，
        // 2022-12-30 修改问题，先查询merge是否有数据，有直接用，没有则重新创建
        QueryWrapper<EntryExcellentStateMerge> mergeQueryWrapper = new QueryWrapper<>();
        mergeQueryWrapper.eq("year", year).eq("month", month).eq("departmentId",
                viewDepartmentRoleUser.getDepartmentId());
        List<EntryExcellentStateMerge> mergeList = entryExcellentStateMergeMapper
                .selectList(mergeQueryWrapper);
        EntryExcellentStateMerge entryExcellentStateMerge;
        if (ObjectUtils.isEmpty(mergeList)) {
            entryExcellentStateMerge = new EntryExcellentStateMerge();
            entryExcellentStateMerge.setCreateDate(LocalDate.now());
            entryExcellentStateMerge.setCreatedAt(LocalDateTime.now());
            entryExcellentStateMerge.setCreateId(user.getId());
            entryExcellentStateMerge.setCreateName(user.getName());
            entryExcellentStateMerge.setDepartmentName(viewDepartmentRoleUser.getDepartmentName());
            entryExcellentStateMerge.setDepartmentId(viewDepartmentRoleUser.getDepartmentId());
            entryExcellentStateMerge.setAuditName(null);
            entryExcellentStateMerge.setAuditId(null);
            entryExcellentStateMerge.setAuditDate(null);
            entryExcellentStateMerge.setApplyDate(LocalDate.now());
            entryExcellentStateMerge = storeNoticeMerge(LocalDate.now(),
                    new HashMap<String, Object>(), entryExcellentStateMerge);// 保存
        } else {
            entryExcellentStateMerge = mergeList.get(0);
        }

        // 创建评价说明材料明细
        entryExcellentStateDetail.setDepartmentId(entryExcellentStateMerge.getDepartmentId());
        entryExcellentStateDetail.setDepartmentName(entryExcellentStateMerge.getDepartmentName());
        entryExcellentStateDetail.setCreatedAt(LocalDateTime.now());
        entryExcellentStateDetail.setCreatedId(user.getId());
        entryExcellentStateDetail.setCreatedName(user.getName());
        entryExcellentStateDetail.setNextUserName(nextSb.toString());
        entryExcellentStateDetail.setMainUserName(mainSb.toString());
        entryExcellentStateDetail.setCreateDate(LocalDate.now());
        entryExcellentStateDetail.setCreateName(user.getName());
        entryExcellentStateDetail.setCreateId(user.getId());
        entryExcellentStateDetail.setStatus(PerformanceConstant.WAIT_SUBMIT_AUDIT);
        entryExcellentStateDetail.setApplyDate(LocalDate.now());
        entryExcellentStateDetail.setYear(year);
        entryExcellentStateDetail.setMonth(month);
        entryExcellentStateDetail.setMergeId(entryExcellentStateMerge.getId());
        entryExcellentStateDetail.setMergeNo(entryExcellentStateMerge.getMergeNo());
        result = entryExcellentStateDetailMapper.insert(entryExcellentStateDetail);// 保存

        // 维护bs_performance_state_relation_attachment（员工月度评价情况关联附件表）
        StateRelationAttachment stateRelationAttachment;
        if (param.containsKey("attachmentId")) {
            String attachmentIds = (String) param.get("attachmentId");
            System.out.println(attachmentIds);
            List<String> attachmentIdList = Arrays.asList(attachmentIds.split(","));
            if (!ObjectUtils.isEmpty(attachmentIdList)) {
                for (String attachmentId : attachmentIdList) {
                    stateRelationAttachment = new StateRelationAttachment();
                    Attachment attachment = attachmentMapper.selectById(attachmentId);
                    if (ObjectUtils.isEmpty(attachment)) {
                        throw new ServiceException("附件为空,提交失败");
                    }
                    stateRelationAttachment.setImportDate(attachment.getImportDate());
                    // 保存附件表
                    stateRelationAttachment.setFilename(attachment.getFilename());
                    // 默认就是正在导入
                    stateRelationAttachment.setMd5Path(attachment.getMd5Path());
                    stateRelationAttachment.setPath(attachment.getPath());
                    stateRelationAttachment.setAttachmentId(attachment.getId());
                    stateRelationAttachment.setNote(entryExcellentStateDetail.getNote());
                    stateRelationAttachment.setStateId(entryExcellentStateDetail.getId());
                    stateRelationAttachment.setConclusion(attachment.getConclusion());
                    // 默认就是上载成功
                    stateRelationAttachmentMapper.insert(stateRelationAttachment);
                }
            }
        }
        // 根据选择的主担，创建表“bs_performance_state_relation_main_user”
        for (String mainId : mainIds) {
            User mainUser = userMapper.selectById(mainId);
            StateRelationMainUser stateRelationMainUser = new StateRelationMainUser();
            stateRelationMainUser.setMainUserId(mainUser.getId());
            stateRelationMainUser.setMainUserName(mainUser.getName());
            stateRelationMainUser.setMainDepartmentId(viewDepartmentRoleUser.getDepartmentId());
            stateRelationMainUser.setMainDepartmentName(viewDepartmentRoleUser.getTheDepartmentName());
            stateRelationMainUser.setMainRoleId(viewDepartmentRoleUser.getRoleId());
            stateRelationMainUser.setMainRoleName(viewDepartmentRoleUser.getRoleName());
            stateRelationMainUser.setStateId(entryExcellentStateDetail.getId());
            stateRelationMainUser.setCreatedId(user.getId());
            stateRelationMainUser.setCreatedName(user.getName());
            stateRelationMainUser.setCreatedAt(LocalDateTime.now());
            stateRelationMainUserMapper.insert(stateRelationMainUser);// 保存
        }
        if (!ObjectUtils.isEmpty(nextIdsStr)) {
            // 根据选择的次担，创建表“bs_performance_state_relation_next_user”
            for (String nextId : nextIds) {
                User nextUser = userMapper.selectById(nextId);
                StateRelationNextUser stateRelationNextUser = new StateRelationNextUser();
                stateRelationNextUser.setNextUserName(nextUser.getName());
                stateRelationNextUser.setNextUserId(nextUser.getId());
                stateRelationNextUser.setNextDepartmentId(viewDepartmentRoleUser.getDepartmentId());
                stateRelationNextUser.setNextDepartmentName(viewDepartmentRoleUser.getDepartmentName());
                stateRelationNextUser.setNextRoleId(viewDepartmentRoleUser.getRoleId());
                stateRelationNextUser.setNextRoleName(viewDepartmentRoleUser.getRoleName());
                stateRelationNextUser.setStateId(entryExcellentStateDetail.getId());
                stateRelationNextUser.setCreatedId(user.getId());
                stateRelationNextUser.setCreatedName(user.getName());
                stateRelationNextUser.setCreatedAt(LocalDateTime.now());
                stateRelationNextUserMapper.insert(stateRelationNextUser);// 保存
            }
        }
        // 评优材料提交成功后，修改总结的状态为【待经营管理部审核】
        EntryCasReviewDetail entryCasReviewDetail = entryCasReviewDetailMapper.selectById(entryExcellentStateDetail.getCasReviewId());
        entryCasReviewDetail.setStatus(PerformanceConstant.WAIT_EXCELLENT);
        entryCasReviewDetailMapper.updateById(entryCasReviewDetail);
        return result > 0;
    }

    /**
     * 评优材料表编号生成并保存(线程不安全，需要加锁)
     *
     * @param createDate               创建日期
     * @param otherParams              其他参数
     * @param entryExcellentStateMerge 评优材料表对象
     * @return
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public synchronized EntryExcellentStateMerge storeNoticeMerge(LocalDate createDate,
                                                                  Map<String, Object> otherParams, EntryExcellentStateMerge entryExcellentStateMerge) {
        // region 创建评优材料表数据
        // 生成评优材料表序列号
        int noticeMergeSerial;
        int year = createDate.getYear();
        int month = createDate.getMonthValue();
        QueryWrapper<EntryCasMerge> queryNoticeMergeSerialQueryWrapper = new QueryWrapper<>();
        // 2022-12-30 修改问题，查询条件添加月份和部门并修改编号规则
        queryNoticeMergeSerialQueryWrapper.eq("year", year)
                .eq("month", entryExcellentStateMerge.getMonth())
                .eq("departmentId", entryExcellentStateMerge.getDepartmentId())
                .select("max(serial) as serial");
        EntryCasMerge noticeMergeLastSerialData = entryCasMergeMapper
                .selectOne(queryNoticeMergeSerialQueryWrapper);
        noticeMergeSerial = !ObjectUtils.isEmpty(noticeMergeLastSerialData)
                && !ObjectUtils.isEmpty(noticeMergeLastSerialData.getSerial())
                ? noticeMergeLastSerialData.getSerial().intValue() + 1
                : 1;
        entryExcellentStateMerge
                .setMergeNo(String.format("PY%s%s%s", year, new DecimalFormat("00").format(month),
                        new DecimalFormat("000").format(noticeMergeSerial)));
        entryExcellentStateMerge.setSerial(noticeMergeSerial);
        entryExcellentStateMerge.setYear(year);
        entryExcellentStateMerge.setMonth(month);
        entryExcellentStateMerge.setCreateDate(createDate);

        entryExcellentStateMergeMapper.insert(entryExcellentStateMerge);
        // endregion
        return entryExcellentStateMerge;
    }

    @Override
    public boolean batchAudit(List<String> excellentStateDetailIds, String classReview, List<String> auditNotes) {

        //根据ID集合去找对应的实体类集合
        List<EntryExcellentStateDetail> entryExcellentStateDetails = entryExcellentStateDetailMapper.selectBatchIds(excellentStateDetailIds);
        Map<String, EntryExcellentStateDetail> keyByExcellentMap = IteratorTool.keyByPattern("id", entryExcellentStateDetails);
        for (int i = 0; i < entryExcellentStateDetails.size(); i++) {
            String excellentId = excellentStateDetailIds.get(i);
            String auditNote = null;
            if (!ObjectUtils.isEmpty(auditNotes)) {
                auditNote = auditNotes.get(i);
            }
            EntryExcellentStateDetail entryExcellentStateDetail = keyByExcellentMap.get(excellentId);
            if (entryExcellentStateDetail.getStatus().equals(PerformanceConstant.FINAL) || entryExcellentStateDetail.getStatus().equals(PerformanceConstant.WAIT_AUDIT_MANAGEMENT_CHIEF)) {
                throw new ServiceException(String.format("不能审核状态为【%s】的评优材料", entryExcellentStateDetail.getStatus()));
            }
            entryExcellentStateDetail.setClassReview(classReview);
            entryExcellentStateDetail.setStatus(PerformanceConstant.WAIT_AUDIT_MANAGEMENT_CHIEF);
            entryExcellentStateDetail.setClassReviewUser(GetTool.getUser().getName());
            entryExcellentStateDetail.setClassReviewUserId(GetTool.getUser().getId());
            entryExcellentStateDetail.setClassReviewDate(LocalDate.now());
            entryExcellentStateDetail.setAuditNote(auditNote);
            entryExcellentStateDetailMapper.updateById(entryExcellentStateDetail);
        }
        return true;
    }

    @Override
    @Transactional
    public boolean MinisterBatchAudit(List<String> excellentStateDetailIds, String ministerReview, List<String> auditNotes) {
        //根据Id集合去找对应的实体类集合
        List<EntryExcellentStateDetail> entryExcellentStateDetails = entryExcellentStateDetailMapper.selectBatchIds(excellentStateDetailIds);
        Map<String, EntryExcellentStateDetail> keyByExcellentMap = IteratorTool.keyByPattern("id", entryExcellentStateDetails);
        for (int i = 0; i < entryExcellentStateDetails.size(); i++) {
            String excellentId = excellentStateDetailIds.get(i);
            String auditNote = null;
            if (!ObjectUtils.isEmpty(auditNotes)) {
                auditNote = auditNotes.get(i);
            }
            EntryExcellentStateDetail entryExcellentStateDetail = keyByExcellentMap.get(excellentId);
            if (entryExcellentStateDetail.getStatus().equals(PerformanceConstant.FINAL)) {
                throw new ServiceException("不能审核【完结】状态的评优材料");
            }
            QueryWrapper<EntryCasReviewDetail> entryCasReviewDetailQueryWrapper = new QueryWrapper<>();
            entryCasReviewDetailQueryWrapper.eq("id", entryExcellentStateDetail.getCasReviewId());
            List<EntryCasReviewDetail> entryCasReviewDetails = entryCasReviewDetailMapper.selectList(entryCasReviewDetailQueryWrapper);
            if (ObjectUtils.isEmpty(entryCasReviewDetails)) {
                throw new ServiceException(String.format("评优说明材料id【%s】不存在对应的履职回顾", entryExcellentStateDetail.getId()));
            }
            EntryCasReviewDetail reviewDetail = entryCasReviewDetails.get(0);//查到对应ID的数据，然后取这条数据
            // 经营管理部审核为“符合”，设置评优材料和总结的最终非事务类评价等级为绩效科复核的结果
            if (ministerReview.equals(PerformanceConstant.CONFORM)) {
                // 修改“ministerReview”、“ministerReviewUser”、“ministerReviewUserId”、“ministerReviewDate”字段，status字段为“完结”
                entryExcellentStateDetail.setMinisterReview(entryExcellentStateDetail.getClassReview());// classReview
                entryExcellentStateDetail.setMinisterReviewUser(GetTool.getUser().getName());
                entryExcellentStateDetail.setMinisterReviewUserId(GetTool.getUser().getId());
                entryExcellentStateDetail.setMinisterReviewDate(LocalDate.now());
                entryExcellentStateDetail.setAuditId(GetTool.getUser().getId());
                entryExcellentStateDetail.setAuditName(GetTool.getUser().getName());
                entryExcellentStateDetail.setAuditDate(LocalDate.now());
                // 通过“bs_performance_entry_excellent_state_detail”的字段“casReviewId”修改id为“casReviewId”的“bs_performance_employee_entry_cas_review_detail”表字段“finalNontransactionEvaluateLevel”
                reviewDetail.setFinalNontransactionEvaluateLevel(entryExcellentStateDetail.getClassReview());
                entryExcellentStateDetail.setStatus(PerformanceConstant.FINAL);// 评优材料状态完结
                User user = GetTool.getUser();
                reviewDetail.setAuditId(user.getId());
                reviewDetail.setAuditName(user.getName());
                reviewDetail.setAuditDate(LocalDate.now());
                reviewDetail.setStatus(PerformanceConstant.FINAL);// 设置回顾状态为完结
                // 如果事件类型为‘新增工作流’，则将它设置为‘非事务类’，便于统计
                if (reviewDetail.getEventsFirstType().equals(PerformanceConstant.EVENT_FIRST_TYPE_NEW_EVENT)) {
                    reviewDetail.setEventsFirstType(PerformanceConstant.EVENT_FIRST_TYPE_NOT_TRANSACTION);
                }
                // 计算分数
                BigDecimal score = GetTool.getScore(reviewDetail, entryExcellentStateDetail.getClassReview());// classReview
                reviewDetail.setAutoScore(score);
                reviewDetail.setArtifactualScore(score);
            } else {
                // 经营管理部审为“不符合”,返回绩效科复核;
                entryExcellentStateDetail.setStatus(PerformanceConstant.WAIT_AUDIT_PERFORMANCE);
            }
            entryExcellentStateDetail.setAuditNote(auditNote);
            entryCasReviewDetailMapper.updateById(reviewDetail);
            entryExcellentStateDetailMapper.updateById(entryExcellentStateDetail);
        }
        return true;
    }

    @Override
    public Page<Map<String, Object>> findDataMapPageListByParams(Map<String, Object> params) {
        QueryWrapper<EntryExcellentStateDetail> queryWrapper = getQueryWrapper(params);
        Page<Map<String, Object>> myPage = new Page<>((int) params.get("page"),
                (int) params.get("limit"));
        myPage = entryExcellentStateDetailMapper.selectMapsPage(myPage, queryWrapper);
        // 2022-12-30 添加评优材料附件查询
        myPage.getRecords().stream().forEach(item -> {
            QueryWrapper<StateRelationAttachment> attachmentQueryWrapper = new QueryWrapper<>();
            attachmentQueryWrapper.eq("stateId", item.get("id"))
                    .select("stateId,attachmentId,md5Path,fileName");
            List<StateRelationAttachment> attachments = stateRelationAttachmentMapper
                    .selectList(attachmentQueryWrapper);
            item.put("attachmentList", attachments);
        });
        return myPage;
    }

    @Override
    public List<EntryExcellentStateDetail> findListByParams(Map<String, Object> params) {
        QueryWrapper<EntryExcellentStateDetail> queryWrapper = getQueryWrapper(params);// 全局根据id倒序
        return entryExcellentStateDetailMapper.selectList(queryWrapper);
    }

    @Override
    public List<Map<String, Object>> findDataMapListByParams(Map<String, Object> params) {
        QueryWrapper<EntryExcellentStateDetail> queryWrapper = getQueryWrapper(params);// 全局根据id倒序
        return entryExcellentStateDetailMapper.selectMaps(queryWrapper);
    }
}
