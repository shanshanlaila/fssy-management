package com.fssy.shareholder.management.service.system.impl.performance.employee;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fssy.shareholder.management.mapper.manage.department.DepartmentMapper;
import com.fssy.shareholder.management.mapper.manage.department.ViewDepartmentRoleUserMapper;
import com.fssy.shareholder.management.mapper.manage.role.RoleMapper;
import com.fssy.shareholder.management.mapper.manage.user.UserMapper;
import com.fssy.shareholder.management.mapper.system.performance.employee.*;
import com.fssy.shareholder.management.pojo.manage.department.ViewDepartmentRoleUser;
import com.fssy.shareholder.management.pojo.manage.user.User;
import com.fssy.shareholder.management.pojo.system.performance.employee.*;
import com.fssy.shareholder.management.service.system.performance.employee.EntryExcellentStateDetailService;
import com.fssy.shareholder.management.tools.constant.PerformanceConstant;
import com.fssy.shareholder.management.tools.exception.ServiceException;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

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
    private DepartmentMapper departmentMapper;

    @Autowired
    private RoleMapper roleMapper;

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

    /**
     * 根据分页查询数据
     *
     * @param params 查询参数
     * @return
     */
    @Override
    public Page<EntryExcellentStateDetail> findDataListByParams(Map<String, Object> params) {
        QueryWrapper<EntryExcellentStateDetail> queryWrapper = getQueryWrapper(params).orderByDesc("id");//全局根据id倒序
        Page<EntryExcellentStateDetail> myPage = new Page<>((int) params.get("page"), (int) params.get("limit"));
        return entryExcellentStateDetailMapper.selectPage(myPage, queryWrapper);
    }

    //传入参数
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
        if (params.containsKey("departmentIdList")) {
            queryWrapper.in("departmentId", (List<String>) params.get("departmentIdList"));
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

        return queryWrapper;
    }

    /**
     * 评优材料提交审核
     *
     * @param excellentStateDetailIds 履职回顾的Ids
     * @return
     */
    @Override
    public boolean submitAudit(List<String> excellentStateDetailIds) {
        List<EntryExcellentStateDetail> entryExcellentStateDetails = entryExcellentStateDetailMapper.selectBatchIds(excellentStateDetailIds);
        for (EntryExcellentStateDetail entryExcellentStateDetail : entryExcellentStateDetails) {
            //只能提交 待提交审核 状态的评优材料
            if (entryExcellentStateDetail.getStatus().equals(PerformanceConstant.PLAN_DETAIL_STATUS_SUBMIT_AUDIT)) {
                LambdaUpdateWrapper<EntryExcellentStateDetail> entryExcellentStateDetailLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                entryExcellentStateDetail.setStatus(PerformanceConstant.PLAN_DETAIL_STATUS_AUDIT_PERFORMANCE);
                entryExcellentStateDetailLambdaUpdateWrapper.eq(EntryExcellentStateDetail::getId, entryExcellentStateDetail.getId());
                entryExcellentStateDetailMapper.update(entryExcellentStateDetail, entryExcellentStateDetailLambdaUpdateWrapper);
            } else return false;
        }
        return true;
    }

    /**
     * 评优材料撤销审核
     *
     * @param excellentStateDetailIds 履职回顾的Ids
     * @return
     */
    @Override
    public boolean retreat(List<String> excellentStateDetailIds) {
        List<EntryExcellentStateDetail> entryExcellentStateDetails = entryExcellentStateDetailMapper.selectBatchIds(excellentStateDetailIds);
        for (EntryExcellentStateDetail entryExcellentStateDetail : entryExcellentStateDetails) {
            LambdaUpdateWrapper<EntryExcellentStateDetail> entryExcellentStateDetailLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            if (entryExcellentStateDetail.getStatus().equals(PerformanceConstant.PLAN_DETAIL_STATUS_AUDIT_PERFORMANCE)) {
                entryExcellentStateDetail.setStatus(PerformanceConstant.PLAN_DETAIL_STATUS_SUBMIT_AUDIT);
                entryExcellentStateDetailLambdaUpdateWrapper.eq(EntryExcellentStateDetail::getId, entryExcellentStateDetail.getId());
                entryExcellentStateDetailMapper.update(entryExcellentStateDetail, entryExcellentStateDetailLambdaUpdateWrapper);
                continue;
            }
            //校验方法
            if (entryExcellentStateDetail.getStatus().equals(PerformanceConstant.PLAN_DETAIL_STATUS_SUBMIT_AUDIT)
                    || entryExcellentStateDetail.getStatus().equals(PerformanceConstant.EVENT_LIST_STATUS_FINAL) || entryExcellentStateDetail.getStatus().equals(PerformanceConstant.REVIEW_DETAIL_STATUS_AUDIT_A_ZHUGUAN)) {
                throw new ServiceException("不能撤销待提交审核状态或其他状态的评优材料");
            }
        }
        return true;
    }

    /**
     * 绩效科撤销审核评优材料
     *
     * @param excellentStateDetailIds 履职回顾的Ids
     * @return
     */
    @Override
    public boolean PerformanceRetreat(List<String> excellentStateDetailIds) {
        List<EntryExcellentStateDetail> entryExcellentStateDetails = entryExcellentStateDetailMapper.selectBatchIds(excellentStateDetailIds);
        for (EntryExcellentStateDetail entryExcellentStateDetail : entryExcellentStateDetails) {
            if (entryExcellentStateDetail.getStatus().equals(PerformanceConstant.REVIEW_DETAIL_STATUS_AUDIT_A_ZHUGUAN) || entryExcellentStateDetail.getStatus().equals(PerformanceConstant.EVENT_LIST_STATUS_FINAL)) {
                entryExcellentStateDetail.setStatus(PerformanceConstant.PLAN_DETAIL_STATUS_AUDIT_PERFORMANCE);
                entryExcellentStateDetail.setClassReview("");
                entryExcellentStateDetailMapper.updateById(entryExcellentStateDetail);
                continue;
            }
            if (entryExcellentStateDetail.getStatus().equals(PerformanceConstant.PLAN_DETAIL_STATUS_AUDIT_PERFORMANCE)) {
                throw new ServiceException("不能撤销待绩效科复核状态材料");
            }
        }

        return true;
    }

    /**
     * 绩效科审核评优材料
     *
     * @param entryExcellentStateDetail 履职回顾的
     * @return
     */
    @Override
    public boolean updateEntryExcellentStateDetail(EntryExcellentStateDetail entryExcellentStateDetail) {
        if (entryExcellentStateDetail.getClassReview().equals(PerformanceConstant.REVIEW_DETAIL_MINISTER_REVIEW_EXCELLENT)) {
            entryExcellentStateDetail.setStatus(PerformanceConstant.REVIEW_DETAIL_STATUS_AUDIT_A_ZHUGUAN);
        } else {
            entryExcellentStateDetail.setStatus(PerformanceConstant.EVENT_LIST_STATUS_FINAL);
        }
        int result = entryExcellentStateDetailMapper.updateById(entryExcellentStateDetail);
        return result > 0;
    }

    /**
     * 经营管理部主管审核评优材料
     *
     * @param entryExcellentStateDetail 履职回顾
     * @return
     */
    @Override
    public boolean updateMinister(EntryExcellentStateDetail entryExcellentStateDetail) {
        entryExcellentStateDetail.setStatus(PerformanceConstant.EVENT_LIST_STATUS_FINAL);
        int result = entryExcellentStateDetailMapper.updateById(entryExcellentStateDetail);
        return result>0;
    }

    /**
     * 经营管理部主管撤销审核评优材料
     * @param excellentStateDetailIds 履职回顾的Ids
     * @return
     */
    @Override
    public boolean MinisterRetreat(List<String> excellentStateDetailIds) {
        List<EntryExcellentStateDetail> entryExcellentStateDetails = entryExcellentStateDetailMapper.selectBatchIds(excellentStateDetailIds);
        for (EntryExcellentStateDetail entryExcellentStateDetail : entryExcellentStateDetails) {
            if (entryExcellentStateDetail.getStatus().equals(PerformanceConstant.EVENT_LIST_STATUS_FINAL)) {
                entryExcellentStateDetail.setStatus(PerformanceConstant.REVIEW_DETAIL_STATUS_AUDIT_A_ZHUGUAN);
                entryExcellentStateDetail.setMinisterReview("");
                entryExcellentStateDetailMapper.updateById(entryExcellentStateDetail);
                continue;
            }
            if (entryExcellentStateDetail.getStatus().equals(PerformanceConstant.PLAN_DETAIL_STATUS_AUDIT_PERFORMANCE)) {
                throw new ServiceException("不能撤销待绩效科复核状态材料");
            }
        }
        return true;
    }

    @Override
    public boolean save(EntryExcellentStateDetail entryExcellentStateDetail, String mainIdsStr, String nextIdsStr) {

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
        // 创建评价说明材料明细
        entryExcellentStateDetail.setDepartmentId(viewDepartmentRoleUser.getDepartmentId());
        entryExcellentStateDetail.setDepartmentName(viewDepartmentRoleUser.getDepartmentName());
        entryExcellentStateDetail.setCreatedAt(LocalDateTime.now());
        entryExcellentStateDetail.setCreatedId(user.getId());
        entryExcellentStateDetail.setCreatedName(user.getName());
        entryExcellentStateDetail.setNextUserName(nextSb.toString());
        entryExcellentStateDetail.setMainUserName(mainSb.toString());
        entryExcellentStateDetail.setCreateDate(LocalDate.now());
        entryExcellentStateDetail.setCreateName(user.getName());
        entryExcellentStateDetail.setCreateId(user.getId());
        entryExcellentStateDetail.setStatus(PerformanceConstant.PLAN_DETAIL_STATUS_SUBMIT_AUDIT);
        entryExcellentStateDetail.setApplyDate(LocalDate.now());
        int year = LocalDate.now().getYear();
        entryExcellentStateDetail.setYear(year);
        int month = LocalDate.now().getMonthValue();
        entryExcellentStateDetail.setMonth(month);

        // 按照申报年份、月份加申报部门创建表“bs_performance_entry_excellent_state_merge”，
        EntryExcellentStateMerge entryExcellentStateMerge = new EntryExcellentStateMerge();
        entryExcellentStateMerge.setCreateDate(LocalDate.now());
        entryExcellentStateMerge.setCreatedAt(LocalDateTime.now());
        entryExcellentStateMerge.setCreateId(user.getId());
        entryExcellentStateMerge.setCreateName(user.getName());
        entryExcellentStateMerge.setDepartmentName(viewDepartmentRoleUser.getDepartmentName());
        entryExcellentStateMerge.setDepartmentId(viewDepartmentRoleUser.getDepartmentId());
        entryExcellentStateMerge.setAuditName(null);
        entryExcellentStateMerge.setAuditId(null);
        entryExcellentStateMerge.setAuditDate(null);
        entryExcellentStateMerge.setApplyDate(entryExcellentStateDetail.getApplyDate());
        entryExcellentStateMerge = storeNoticeMerge(LocalDate.now(), new HashMap<String, Object>(), entryExcellentStateMerge);// 保存
        entryExcellentStateDetail.setMergeId(entryExcellentStateMerge.getId());
        entryExcellentStateDetail.setMergeNo(entryExcellentStateMerge.getMergeNo());
        int result = entryExcellentStateDetailMapper.insert(entryExcellentStateDetail);// 保存

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
        return result > 0;
    }

    public synchronized EntryExcellentStateMerge storeNoticeMerge(LocalDate createDate,
                                                                  Map<String, Object> otherParams, EntryExcellentStateMerge entryExcellentStateMerge) {
        // region 创建通知单数据
        Calendar calendar = Calendar.getInstance(Locale.CHINA);

        // 生成通知单明细合并序列号
        int noticeMergeSerial;
        int year = createDate.getYear();
        int month = createDate.getMonthValue();
        QueryWrapper<EntryCasMerge> queryNoticeMergeSerialQueryWrapper = new QueryWrapper<>();
        queryNoticeMergeSerialQueryWrapper.eq("year", year)
                .select("max(serial) as serial");
        EntryCasMerge noticeMergeLastSerialData = entryCasMergeMapper
                .selectOne(queryNoticeMergeSerialQueryWrapper);
        noticeMergeSerial = !ObjectUtils.isEmpty(noticeMergeLastSerialData)
                && !ObjectUtils.isEmpty(noticeMergeLastSerialData.getSerial())
                ? noticeMergeLastSerialData.getSerial().intValue() + 1
                : 1;
        entryExcellentStateMerge.setMergeNo(
                String.format("PJ%s%s", year,
                        new DecimalFormat("000").format(noticeMergeSerial)));
        entryExcellentStateMerge.setSerial(noticeMergeSerial);
        entryExcellentStateMerge.setYear(year);
        entryExcellentStateMerge.setMonth(month);
        entryExcellentStateMerge.setCreateDate(createDate);

        entryExcellentStateMergeMapper.insert(entryExcellentStateMerge);
        // endregion
        return entryExcellentStateMerge;
    }

}
