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
import com.fssy.shareholder.management.pojo.manage.department.Department;
import com.fssy.shareholder.management.pojo.manage.department.ViewDepartmentRoleUser;
import com.fssy.shareholder.management.pojo.manage.role.Role;
import com.fssy.shareholder.management.pojo.manage.user.User;
import com.fssy.shareholder.management.pojo.system.config.Attachment;
import com.fssy.shareholder.management.pojo.system.performance.employee.*;
import com.fssy.shareholder.management.service.common.SheetService;
import com.fssy.shareholder.management.service.system.performance.employee.EntryCasReviewDetailService;
import com.fssy.shareholder.management.tools.common.GetTool;
import com.fssy.shareholder.management.tools.common.IteratorTool;
import com.fssy.shareholder.management.tools.common.StringTool;
import com.fssy.shareholder.management.tools.constant.PerformanceConstant;
import com.fssy.shareholder.management.tools.exception.ServiceException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

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
    private SheetService sheetService;

    @Autowired
    private EntryCasPlanDetailMapper entryCasPlanDetailMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DepartmentMapper departmentMapper;

    @Autowired
    private EntryCasMergeMapper entryCasMergeMapper;

    @Autowired
    private ViewDepartmentRoleUserMapper viewDepartmentRoleUserMapper;

    @Autowired
    private EventsRelationRoleMapper eventsRelationRoleMapper;

    @Autowired
    private EventListMapper eventListMapper;


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
        if (params.containsKey("roleIdList")) {
            queryWrapper.in("roleId", (List<String>) params.get("roleIdList"));
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
        // 工作计划完成情况部长审核不显示事务类
        if (params.containsKey("eventsFirstTypeNe")) {
            queryWrapper.ne("eventsFirstType", PerformanceConstant.EVENT_FIRST_TYPE_TRANSACTION);
        }
        if (params.containsKey("auditStatus")) {
            queryWrapper.like("auditStatus", params.get("auditStatus"));
        }
        if (params.containsKey("attachmentId")) {
            queryWrapper.eq("attachmentId", params.get("attachmentId"));
        }
        if (params.containsKey("statusCancel")) {
            queryWrapper.ne("status", PerformanceConstant.CANCEL);
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
        // 审核页面，左侧表格按人名分组
        if (params.containsKey("groupByUserName")) {
            queryWrapper.select("userName,roleName,departmentName").groupBy("userName", "roleName", "departmentName");
        }
        // 审核页面，右侧表格根据左侧双击选择的名字显示
        if (params.containsKey("userNameRight")) {
            queryWrapper.eq("userName", params.get("userNameRight"));
        }
        // 用户表主键列表查询
        if (params.containsKey("userIds")) {
            queryWrapper.in("userId", (List<String>) params.get("userIds"));
        }
        return queryWrapper;


    }

    /**
     * 修改总结-部门领导审核结果
     *
     * @param entryCasReviewDetail 履职总结
     * @return 审核结果
     */
    @Override
    public boolean updateEntryCasReviewDetail(EntryCasReviewDetail entryCasReviewDetail) {
        // “status”取值为：当“ministerReview”为“优”，设置为“待经营管理部审核”，其他取值设置为“完结”
        if (entryCasReviewDetail.getMinisterReview().equals(PerformanceConstant.EXCELLENT)) {
            entryCasReviewDetail.setStatus(PerformanceConstant.WAIT_AUDIT_MANAGEMENT);
            entryCasReviewDetail.setIsExcellent(PerformanceConstant.YES);
        } else {
            entryCasReviewDetail.setFinalNontransactionEvaluateLevel(entryCasReviewDetail.getMinisterReview());
            entryCasReviewDetail.setStatus(PerformanceConstant.FINAL);// 完结
            BigDecimal actualAutoScore = GetTool.getScore(entryCasReviewDetail, entryCasReviewDetail.getMinisterReview());
            entryCasReviewDetail.setAutoScore(actualAutoScore);
            entryCasReviewDetail.setArtifactualScore(actualAutoScore);
            entryCasReviewDetail.setIsExcellent(PerformanceConstant.NO);
            // 总结完结时设置事件类型 将‘新增工作流’变成‘非事务类’
            if (entryCasReviewDetail.getEventsFirstType().equals(PerformanceConstant.EVENT_FIRST_TYPE_NEW_EVENT)) {
                entryCasReviewDetail.setEventsFirstType(PerformanceConstant.EVENT_FIRST_TYPE_NOT_TRANSACTION);
            }
        }
        User user = GetTool.getUser();
        entryCasReviewDetail.setAuditNote(entryCasReviewDetail.getAuditNote());
        entryCasReviewDetail.setAuditId(user.getId());
        entryCasReviewDetail.setAuditName(user.getName());
        entryCasReviewDetail.setAuditDate(LocalDate.now());
        int result = entryCasReviewDetailMapper.updateById(entryCasReviewDetail);
        return result > 0;
    }

    /**
     * 工作计划完成情况提交审核
     *
     * @param reviewDetailIds
     * @return
     */
    @Override
    public boolean submitAudit(List<String> reviewDetailIds) {
        List<EntryCasReviewDetail> entryCasReviewDetails = entryCasReviewDetailMapper.selectBatchIds(reviewDetailIds);
        for (EntryCasReviewDetail entryCasReviewDetail : entryCasReviewDetails) {
            // 只能提交 待提交审核 状态的事件清单
            if (entryCasReviewDetail.getStatus().equals(PerformanceConstant.WAIT_SUBMIT_AUDIT)) {
                if (entryCasReviewDetail.getEventsFirstType().equals(PerformanceConstant.EVENT_FIRST_TYPE_TRANSACTION)) {
                    // 事务类，提交给科长审核
                    entryCasReviewDetail.setStatus(PerformanceConstant.WAIT_AUDIT_CHIEF);
                } else {
                    // 非事务类 和 新增工作流 交给部长审核
                    entryCasReviewDetail.setStatus(PerformanceConstant.WAIT_AUDIT_MINISTER);
                }
                entryCasReviewDetailMapper.updateById(entryCasReviewDetail);
            } else return false;
        }
        return true;
    }

    /**
     * 工作计划完成情况撤销审核
     *
     * @param reviewDetailIds
     * @return
     */
    @Override
    public boolean retreat(List<String> reviewDetailIds) {
        List<EntryCasReviewDetail> entryCasReviewDetails = entryCasReviewDetailMapper.selectBatchIds(reviewDetailIds);
        for (EntryCasReviewDetail entryCasReviewDetail : entryCasReviewDetails) {
            LambdaUpdateWrapper<EntryCasReviewDetail> entryCasReviewDetailLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            if (entryCasReviewDetail.getStatus().equals(PerformanceConstant.WAIT_AUDIT_CHIEF) ||
                    entryCasReviewDetail.getStatus().equals(PerformanceConstant.WAIT_AUDIT_MINISTER)) {
                entryCasReviewDetail.setStatus("待提交审核");
                entryCasReviewDetailLambdaUpdateWrapper.eq(EntryCasReviewDetail::getId, entryCasReviewDetail.getId());
                entryCasReviewDetailMapper.update(entryCasReviewDetail, entryCasReviewDetailLambdaUpdateWrapper);
                continue;
            }
            //校验方法
            if (entryCasReviewDetail.getStatus().equals(PerformanceConstant.WAIT_SUBMIT_AUDIT) || entryCasReviewDetail.getStatus().equals(PerformanceConstant.FINAL)) {
                throw new ServiceException("不能撤销待提交审核状态或完结状态下的的履职明细");
            }
        }
        return true;
    }

    /**
     * 修改更新科长、事物类审核评价
     *
     * @param entryCasReviewDetail 总结履职
     * @return
     */
    @Override
    public boolean sectionWorkAudit(EntryCasReviewDetail entryCasReviewDetail) {
        // 事务类评价等级与最终非事务类评价等级值相等
        entryCasReviewDetail.setFinalNontransactionEvaluateLevel(entryCasReviewDetail.getChargeTransactionEvaluateLevel());
        return entryCasReviewDetailMapper.updateById(entryCasReviewDetail) > 0;
    }

    /**
     * 设置失败的内容
     *
     * @param result 结果map
     * @param append 导入失败的原因
     */
    private void setFailedContent(Map<String, Object> result, String append) {
        String content = result.get("content").toString();
        if (ObjectUtils.isEmpty(content)) {
            result.put("content", append);
        } else {
            result.put("content", content + "," + append);
        }
        result.put("failed", true);
    }

    @Override
    @Transactional
    public Map<String, Object> readEntryCasReviewDetailDataSource(Attachment attachment) {
        // 导入履职总结
        // 返回消息
        Map<String, Object> result = new HashMap<>();
        StringBuffer sb = new StringBuffer();// 用于数据校验的StringBuffer
        result.put("content", "");
        result.put("failed", false);
        // 读取附件
        sheetService.load(attachment.getPath(), attachment.getFilename()); // 根据路径和名称读取附件
        // 读取表单
        sheetService.readByName("导出履职计划填报月底总结"); //根据表单名称获取该工作表单
        // 获取表单数据
        Sheet sheet = sheetService.getSheet();
        if (ObjectUtils.isEmpty(sheet)) {
            throw new ServiceException("表单【导出履职计划填报月底总结】不存在，无法读取数据，请检查");
        }

        // 导入成功数
        int successNumber = 0;
        // 导入总数
        int totalNumber = 0;
        // 判断重复计划id的集合
        ArrayList<Long> planIds = new ArrayList<>();
        // 获取单价列表数据
        Row row;
        // 2022-06-01 从决策系统导出数据，存在最后几行为空白数据，导致报数据越界问题，这里的长度由表头长度控制
        short maxSize = sheet.getRow(0).getLastCellNum();//列数(表头长度)
        // 缓存map
        Map<String, EntryCasMerge> mergeMap = new HashMap<>();
        // 循环总行数(不读表头，从第2行开始读，索引从0开始，所以j=1)
        for (int j = 1; j <= sheet.getLastRowNum(); j++) {// getPhysicalNumberOfRows()此方法不会将空白行计入行数
            List<String> cells = new ArrayList<>();// 每一行的数据用一个list接收
            row = sheet.getRow(j);// 获取第j行
            // 获取一行中有多少列 Row：行，cell：列
            // 循环row行中的每一个单元格
            for (int k = 0; k < maxSize + 2; k++) {
                // 如果这单元格为空，就写入空
                if (row.getCell(k) == null) {
                    cells.add("");
                    continue;
                }
                // 处理单元格读取到公式的问题
                if (row.getCell(k).getCellType() == CellType.FORMULA) {
                    row.getCell(k).setCellType(CellType.STRING);
                    String res = row.getCell(k).getStringCellValue();
                    cells.add(res);
                    continue;
                }
                // Cannot get a STRING value from a NUMERIC cell 无法从单元格获取数值
                Cell cell = row.getCell(k);
                String res = sheetService.getValue(cell).trim();// 获取单元格的值
                cells.add(res);// 将单元格的值写入行
            }
            totalNumber++;
            Cell cell = row.createCell(SheetService.columnToIndex("U"));
            // 读取数据
            String planId = cells.get(SheetService.columnToIndex("A"));// 履职计划主键
            String eventsRoleId = cells.get(SheetService.columnToIndex("B"));// 事件清单序号
            String eventsFirstType = cells.get(SheetService.columnToIndex("C"));// 事件类型
            String jobName = cells.get(SheetService.columnToIndex("D"));// 工作职责
            String workEvents = cells.get(SheetService.columnToIndex("E"));// 流程（工作事件）
            String standardValue = cells.get(SheetService.columnToIndex("F"));// 事件价值标准分
            String departmentName = cells.get(SheetService.columnToIndex("G"));// 部门名称
            String roleName = cells.get(SheetService.columnToIndex("H"));// 岗位名称
            String userName = cells.get(SheetService.columnToIndex("I"));// 员工姓名
            String applyDate = cells.get(SheetService.columnToIndex("J"));// 申报日期
            String mainOrNext = cells.get(SheetService.columnToIndex("K"));// 主/次担
            String planningWork = cells.get(SheetService.columnToIndex("L"));// 对应工作事件的计划内容
            String times = cells.get(SheetService.columnToIndex("M"));// 频次
            String planOutput = cells.get(SheetService.columnToIndex("N"));// 表单（输出内容）
            String planStartDate = cells.get(SheetService.columnToIndex("O"));// 计划开始时间
            String planEndDate = cells.get(SheetService.columnToIndex("P"));// 计划完成时间
            // 填写部分
            String actualCompleteDate = cells.get(SheetService.columnToIndex("Q"));// 实际完成时间
            String completeDesc = cells.get(SheetService.columnToIndex("R"));// 工作完成描述

            // 数据库必填项判断
            if (ObjectUtils.isEmpty(planId)) {
                setFailedContent(result, String.format("第%s行的履职计划序号为空", j + 1));
                cell.setCellValue("履职计划序号不能为空");
                continue;
            }
            if (ObjectUtils.isEmpty(eventsFirstType)) {
                setFailedContent(result, String.format("第%s行的事务类型为空", j + 1));
                cell.setCellValue("事务类型不能为空");
                continue;
            }
            // 事务类别数据校验：非事务类、事务类、新增工作流
            if (!(PerformanceConstant.EVENT_FIRST_TYPE_NOT_TRANSACTION.equals(eventsFirstType) || PerformanceConstant.EVENT_FIRST_TYPE_TRANSACTION.equals(eventsFirstType) || PerformanceConstant.EVENT_FIRST_TYPE_NEW_EVENT.equals(eventsFirstType))) {
                StringTool.setMsg(sb, String.format("第【%s】行的【%s】的事务类别不为‘非事务类、事务类、新增工作流’中的一种，不能导入", j + 1, eventsFirstType));
                cell.setCellValue(String.format("履职计划序号为【%s】的事务类别不为‘非事务类、事务类、新增工作流’中的一种，不能导入", planId));
                continue;
            }
            if (ObjectUtils.isEmpty(eventsRoleId)) {
                // 当事务类型不为 “新增工作流” 时才会进行判空，否则允许写入空值
                if (!eventsFirstType.equals(PerformanceConstant.EVENT_FIRST_TYPE_NEW_EVENT)) {
                    setFailedContent(result, String.format("第%s行的事件岗位表序号为空", j + 1));// 备注
                    cell.setCellValue("事件岗位表序号不能为空");
                    continue;
                }
            }
            if (ObjectUtils.isEmpty(jobName)) {
                setFailedContent(result, String.format("第%s行的工作职责为空", j + 1));
                cell.setCellValue("工作职责不能为空");
                continue;
            }
            if (ObjectUtils.isEmpty(workEvents)) {
                setFailedContent(result, String.format("第%s行的流程（工作事件）为空", j + 1));
                cell.setCellValue("流程（工作事件）不能为空");
                continue;
            }
            // ‘新增工作流’ 价值分只能为空
            if (eventsFirstType.equals(PerformanceConstant.EVENT_FIRST_TYPE_NEW_EVENT)) {
                if (!ObjectUtils.isEmpty(standardValue)) {
                    throw new ServiceException(String.format("第%s行的事务类型【新增工作流】价值分只能为空", j + 1));
                }
            }
            // 不属于‘新增工作流’的数据价值分不能为空
            if (!eventsFirstType.equals(PerformanceConstant.EVENT_FIRST_TYPE_NEW_EVENT)) {
                if (ObjectUtils.isEmpty(standardValue)) {
                    setFailedContent(result, String.format("第%s行的事件标准价值分为空", j + 1));
                    cell.setCellValue("事件标准价值分不能为空");
                    continue;
                }
            }
            if (ObjectUtils.isEmpty(departmentName)) {
                setFailedContent(result, String.format("第%s行的部门名称为空", j + 1));
                cell.setCellValue("部门名称不能为空");
                continue;
            }
            if (ObjectUtils.isEmpty(roleName)) {
                setFailedContent(result, String.format("第%s行的岗位名称为空", j + 1));
                cell.setCellValue("岗位名称不能为空");
                continue;
            }
            if (ObjectUtils.isEmpty(userName)) {
                setFailedContent(result, String.format("第%s行的员工姓名为空", j + 1));
                cell.setCellValue("员工姓名不能为空");
                continue;
            }
            if (ObjectUtils.isEmpty(applyDate)) {
                setFailedContent(result, String.format("第%s行的申报日期为空", j + 1));
                cell.setCellValue("申报日期不能为空");
                continue;
            }
            if (ObjectUtils.isEmpty(mainOrNext)) {
                setFailedContent(result, String.format("第%s行的主/次担为空", j + 1));
                cell.setCellValue("主/次担不能为空");
                continue;
            }
            if (ObjectUtils.isEmpty(planningWork)) {
                setFailedContent(result, String.format("第%s行的对应工作事件的计划内容为空", j + 1));
                cell.setCellValue("对应工作事件的计划内容不能为空");
                continue;
            }
            if (ObjectUtils.isEmpty(planStartDate)) {
                setFailedContent(result, String.format("第%s行的计划开始时间为空", j + 1));
                cell.setCellValue("计划开始时间不能为空");
                continue;
            }
            if (ObjectUtils.isEmpty(planEndDate)) {
                setFailedContent(result, String.format("第%s行的计划完成时间为空", j + 1));
                cell.setCellValue("计划完成时间不能为空");
                continue;
            }
            if (ObjectUtils.isEmpty(actualCompleteDate)) {
                setFailedContent(result, String.format("第%s行的实际完成时间为空", j + 1));
                cell.setCellValue("实际完成时间不能为空");
                continue;
            }
            if (ObjectUtils.isEmpty(completeDesc)) {
                setFailedContent(result, String.format("第%s行的工作完成描述为空", j + 1));
                cell.setCellValue("工作完成描述不能为空");
                continue;
            }
            // 数据检查校验
            // 一条计划只能对应一条总结，即导入的表中不能有重复的计划id且数据库中不能存在此id
            if (planIds.contains(Long.parseLong(planId))) {
                throw new ServiceException(String.format("第%s行的计划序号【%s】重复,本次导入失败", j + 1, planId));
            }
            LambdaQueryWrapper<EntryCasReviewDetail> reviewQueryWrapper = new LambdaQueryWrapper<>();
            reviewQueryWrapper.eq(EntryCasReviewDetail::getCasPlanId, planId);
            List<EntryCasReviewDetail> reviewDetails = entryCasReviewDetailMapper.selectList(reviewQueryWrapper);
            if (reviewDetails.size() > 0) {
                throw new ServiceException(String.format("第%s行的计划序号【%s】已创建总结,本次导入失败", j + 1, planId));
            }
            planIds.add(Long.valueOf(planId));

            // 申报年份
            String year = Arrays.asList(applyDate.split("-")).get(0);
            String month = Arrays.asList(applyDate.split("-")).get(1);
            // 构建实体
            EntryCasReviewDetail entryCasReviewDetail = new EntryCasReviewDetail();
            LambdaQueryWrapper<EntryCasPlanDetail> entryCasPlanDetailLambdaQueryWrapper = new LambdaQueryWrapper<>();
            if (!ObjectUtils.isEmpty(eventsRoleId)) {
                entryCasPlanDetailLambdaQueryWrapper.eq(EntryCasPlanDetail::getId, planId).eq(EntryCasPlanDetail::getEventsRoleId, eventsRoleId);
                List<EntryCasPlanDetail> entryCasPlanDetails = entryCasPlanDetailMapper.selectList(entryCasPlanDetailLambdaQueryWrapper);
                if (ObjectUtils.isEmpty(entryCasPlanDetails)) {
                    StringTool.setMsg(sb, String.format("第【%s】行的事件岗位序号【%s】在系统中未查找到，不能导入", j + 1, eventsRoleId));
                    cell.setCellValue(String.format("履职计划序号为【%s】的基础事件未查到，不能导入", planId));
                    continue;
                }
                EntryCasPlanDetail casPlanDetail = entryCasPlanDetails.get(0);
                entryCasReviewDetail.setEventsId(casPlanDetail.getEventsId());
                EventList eventList = eventListMapper.selectById(casPlanDetail.getEventsId());
                if (ObjectUtils.isEmpty(eventList)) {
                    StringTool.setMsg(sb, String.format("第【%s】行序号为【%s】的事件清单在系统中未查找到，不能导入", j + 1, departmentName));
                    cell.setCellValue(String.format("履职计划序号为【%s】的事件清单未查到，不能导入", planId));
                    continue;
                }
                entryCasReviewDetail.setWorkOutput(eventList.getWorkOutput());
            }
            entryCasReviewDetail.setCasPlanId(Long.valueOf(planId));

            if (!ObjectUtils.isEmpty(eventsRoleId)) {
                entryCasReviewDetail.setEventsRoleId(Long.valueOf(eventsRoleId));
            }
            entryCasReviewDetail.setJobName(jobName);
            entryCasReviewDetail.setWorkEvents(workEvents);
            // 给事件类型不为‘新增工作流’的总结写入价值分
            if (!eventsFirstType.equals(PerformanceConstant.EVENT_FIRST_TYPE_NEW_EVENT)) {
                entryCasReviewDetail.setStandardValue(new BigDecimal(standardValue));
            }
            entryCasReviewDetail.setDepartmentName(departmentName);
            entryCasReviewDetail.setRoleName(roleName);
            entryCasReviewDetail.setUserName(userName);
            entryCasReviewDetail.setApplyDate(LocalDate.parse(applyDate));
            entryCasReviewDetail.setMainOrNext(mainOrNext);
            entryCasReviewDetail.setPlanningWork(planningWork);
            entryCasReviewDetail.setTimes(times);
            entryCasReviewDetail.setPlanOutput(planOutput);
            entryCasReviewDetail.setPlanStartDate(LocalDate.parse(planStartDate));
            entryCasReviewDetail.setPlanEndDate(LocalDate.parse(planEndDate));
            entryCasReviewDetail.setActualCompleteDate(LocalDate.parse(actualCompleteDate));
            entryCasReviewDetail.setCompleteDesc(completeDesc);
            // 写入是否为新增工作流的字段标识
            entryCasReviewDetail.setIsNewEvent(eventsFirstType.equals(PerformanceConstant.EVENT_FIRST_TYPE_NEW_EVENT) ? PerformanceConstant.YES : PerformanceConstant.NO);
            entryCasReviewDetail.setEventsFirstType(eventsFirstType);
            // 查询部门id
            LambdaQueryWrapper<Department> departmentLambdaQueryWrapper = new LambdaQueryWrapper<>();
            departmentLambdaQueryWrapper.eq(Department::getDepartmentName, departmentName);
            List<Department> departments = departmentMapper.selectList(departmentLambdaQueryWrapper);
            if (ObjectUtils.isEmpty(departments)) {
                StringTool.setMsg(sb, String.format("第【%s】行的【%s】的部门名称在系统中未查找到，不能导入", j + 1, departmentName));
                cell.setCellValue(String.format("履职计划序号为【%s】的部门名称未查到，不能导入", planId));
                continue;
            }
            Long departmentId = departments.get(0).getId();
            entryCasReviewDetail.setDepartmentId(departmentId);
            // 查询角色id
            LambdaQueryWrapper<Role> roleLambdaQueryWrapper = new LambdaQueryWrapper<>();
            roleLambdaQueryWrapper.eq(Role::getName, roleName);
            List<Role> roles = roleMapper.selectList(roleLambdaQueryWrapper);
            if (ObjectUtils.isEmpty(roles)) {
                StringTool.setMsg(sb, String.format("第【%s】行的【%s】的岗位名称在系统中未查找到，不能导入", j + 1, roleName));
                cell.setCellValue(String.format("履职计划序号为【%s】的岗位名称未查到，不能导入", planId));
                continue;
            }
            Long roleId = roles.get(0).getId();
            entryCasReviewDetail.setRoleId(roleId);
            // 查询用户id
            LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
            userLambdaQueryWrapper.eq(User::getName, userName);
            List<User> users = userMapper.selectList(userLambdaQueryWrapper);
            if (ObjectUtils.isEmpty(users)) {
                StringTool.setMsg(sb, String.format("第【%s】行的【%s】的用户名称在系统中未查找到，不能导入", j + 1, roleName));
                cell.setCellValue(String.format("履职计划序号为【%s】的用户名称未查到，不能导入", planId));
                continue;
            }
            // 查询当前登录用户
            User user = GetTool.getUser();
            // 只能导入本人的总结
            /*if (!userName.equals(user.getName())) {
                StringTool.setMsg(sb, String.format("第【%s】行的员工姓名【%s】与当前登录用户不相等，导入失败", j + 1, userName));
                throw new ServiceException(String.format("第【%s】行的员工姓名【%s】与当前登录用户不相等，导入失败", j + 1, userName));
            }*/
            Long userId = users.get(0).getId();
            entryCasReviewDetail.setUserId(userId);
            // 查询MergeNo
            LambdaQueryWrapper<EntryCasMerge> entryCasMergeLambdaQueryWrapper = new LambdaQueryWrapper<>();
            entryCasMergeLambdaQueryWrapper
                    .eq(EntryCasMerge::getDepartmentName, departmentName)
                    .eq(EntryCasMerge::getYear, year);
            List<EntryCasMerge> entryCasMerges = entryCasMergeMapper.selectList(entryCasMergeLambdaQueryWrapper);
            // 不存在merge就报错
            if (ObjectUtils.isEmpty(entryCasMerges)) {
                StringTool.setMsg(sb, String.format("第【%s】行的【%s】的履职编号在系统中未查找到，不能导入", j + 1, roleName));
                cell.setCellValue(String.format("履职计划序号为【%s】的履职编号未查到，不能导入", planId));
                continue;
            }
            EntryCasMerge entryCasMerge = entryCasMerges.get(0);
            entryCasReviewDetail.setMergeNo(entryCasMerge.getMergeNo());
            entryCasReviewDetail.setMergeId(entryCasMerge.getId());

            // 根据user查询 部门-角色-用户 视图
            LambdaQueryWrapper<ViewDepartmentRoleUser> viewDepartmentRoleUserLambdaQueryWrapper = new LambdaQueryWrapper<>();
            viewDepartmentRoleUserLambdaQueryWrapper.eq(ViewDepartmentRoleUser::getUserId, user.getId());
            List<ViewDepartmentRoleUser> viewDepartmentRoleUsers = viewDepartmentRoleUserMapper.selectList(viewDepartmentRoleUserLambdaQueryWrapper);
            if (ObjectUtils.isEmpty(viewDepartmentRoleUsers)) {
//                throw new ServiceException(String.format("登陆人id为【%s】的用户，不存在对应的部门，请联系管理员", user.getId()));
                StringTool.setMsg(sb, String.format("第【%s】行登陆人id为的【%s】的用户在系统中未查找到，不能导入", j + 1, user.getId()));
                cell.setCellValue(String.format("登陆人id为【%s】的用户未查到，不能导入", user.getId()));
                continue;
            }
            entryCasReviewDetail.setCreateName(user.getName());
            entryCasReviewDetail.setCreateId(user.getId());
            entryCasReviewDetail.setCreateDate(LocalDate.now());
            entryCasReviewDetail.setCreatedAt(LocalDateTime.now());
            entryCasReviewDetail.setMonth(Integer.valueOf(month));
            entryCasReviewDetail.setYear(Integer.valueOf(year));
            entryCasReviewDetail.setStatus(PerformanceConstant.WAIT_SUBMIT_AUDIT);
            entryCasReviewDetail.setAttachmentId(attachment.getId());
            // 更新planDetail数据的状态为完结
            EntryCasPlanDetail entryCasPlanDetail = entryCasPlanDetailMapper.selectById(planId);
            if (ObjectUtils.isEmpty(entryCasPlanDetail)) {
//                throw new ServiceException("不存在对应的履职计划序号，导入失败");
                StringTool.setMsg(sb, String.format("第【%s】行的履职编号为【%s】在系统中未查找到，不能导入", j + 1, planId));
                cell.setCellValue(String.format("履职计划序号为【%s】的履职编号未查到，不能导入", planId));
                continue;
            }
            entryCasPlanDetail.setStatus(PerformanceConstant.FINAL);
            entryCasPlanDetailMapper.updateById(entryCasPlanDetail);
            // 新增
            save(entryCasReviewDetail);
            successNumber++;
            cell.setCellValue("导入成功");
        }
        sheetService.write(attachment.getPath(), attachment.getFilename());
        if (successNumber == 0) {
            throw new ServiceException("没有成功导入一条，导入失败");
        }
        return result;
    }

    @Override
    public boolean batchAudit(List<String> entryReviewDetailIds, String ministerReview, List<String> auditNotes) {
        List<EntryCasReviewDetail> entryCasReviewDetails = entryCasReviewDetailMapper.selectBatchIds(entryReviewDetailIds);
        Map<String, EntryCasReviewDetail> keyByReviewDetailMap = IteratorTool.keyByPattern("id", entryCasReviewDetails);
        for (int i = 0; i < entryCasReviewDetails.size(); i++) {
            String reviewId = entryReviewDetailIds.get(i);
            String auditNote = null;
            if (!ObjectUtils.isEmpty(auditNotes)) {
                auditNote = auditNotes.get(i);
            }
            EntryCasReviewDetail entryCasReviewDetail = keyByReviewDetailMap.get(reviewId);
            entryCasReviewDetail.setMinisterReview(ministerReview);
            // “status”取值为：当“ministerReview”为“优”，设置为“待经营管理部审核”，其他取值设置为“完结”
            if (ministerReview.equals(PerformanceConstant.EXCELLENT)) {
                entryCasReviewDetail.setStatus(PerformanceConstant.WAIT_AUDIT_MANAGEMENT);
                entryCasReviewDetail.setIsExcellent(PerformanceConstant.YES);
            } else {
                entryCasReviewDetail.setFinalNontransactionEvaluateLevel(ministerReview);
                entryCasReviewDetail.setStatus(PerformanceConstant.FINAL);// 完结
                BigDecimal actualAutoScore = GetTool.getScore(entryCasReviewDetail, ministerReview);
                entryCasReviewDetail.setAutoScore(actualAutoScore);
                entryCasReviewDetail.setArtifactualScore(actualAutoScore);
                entryCasReviewDetail.setIsExcellent(PerformanceConstant.NO);
                // 在算分时，如果是新增工作流就把他设置为非事务类
                if (entryCasReviewDetail.getEventsFirstType().equals(PerformanceConstant.EVENT_FIRST_TYPE_NEW_EVENT)) {
                    entryCasReviewDetail.setEventsFirstType(PerformanceConstant.EVENT_FIRST_TYPE_NOT_TRANSACTION);
                }
            }
            User user = GetTool.getUser();
            entryCasReviewDetail.setAuditNote(auditNote);
            entryCasReviewDetail.setAuditId(user.getId());
            entryCasReviewDetail.setAuditName(user.getName());
            entryCasReviewDetail.setAuditDate(LocalDate.now());
            entryCasReviewDetailMapper.updateById(entryCasReviewDetail);
        }
        return true;
    }

    @Override
    @Transactional
    public boolean batchAudit(List<String> entryReviewDetailIds, String chargeTransactionEvaluateLevel, String chargeTransactionBelowType, List<String> auditNotes) {
        List<EntryCasReviewDetail> entryCasReviewDetails = entryCasReviewDetailMapper.selectBatchIds(entryReviewDetailIds);
        Map<String, EntryCasReviewDetail> keyByReviewDetailMap = IteratorTool.keyByPattern("id", entryCasReviewDetails);
        for (int i = 0; i < entryCasReviewDetails.size(); i++) {
            String reviewId = entryReviewDetailIds.get(i);
            String auditNote = null;
            if (!ObjectUtils.isEmpty(auditNotes)) {
                auditNote = auditNotes.get(i);
            }
            EntryCasReviewDetail entryCasReviewDetail = keyByReviewDetailMap.get(reviewId);
            // 当事务评价等级为合格，事务类评价不同类型就取值为空，因为事务类评价不同类型是针对事务类评价等级为不合格时的情况。
            if (chargeTransactionEvaluateLevel.equals(PerformanceConstant.QUALIFIED)) {
                entryCasReviewDetail.setChargeTransactionEvaluateLevel(PerformanceConstant.QUALIFIED);
                entryCasReviewDetail.setChargeTransactionBelowType("");
            } else {
                entryCasReviewDetail.setChargeTransactionEvaluateLevel(PerformanceConstant.UNQUALIFIED);
                entryCasReviewDetail.setChargeTransactionBelowType(chargeTransactionBelowType);
            }
            entryCasReviewDetail.setStatus(PerformanceConstant.FINAL);
            if (entryCasReviewDetail.getEventsFirstType().equals(PerformanceConstant.EVENT_FIRST_TYPE_NEW_EVENT)) {
                entryCasReviewDetail.setEventsFirstType(PerformanceConstant.EVENT_FIRST_TYPE_NOT_TRANSACTION);
            }
            // 审核信息
            User user = GetTool.getUser();
            entryCasReviewDetail.setAuditId(user.getId());
            entryCasReviewDetail.setAuditDate(LocalDate.now());
            entryCasReviewDetail.setAuditName(user.getName());
            BigDecimal score = GetTool.getScore(entryCasReviewDetail, chargeTransactionEvaluateLevel);
            entryCasReviewDetail.setAutoScore(score);
            entryCasReviewDetail.setArtifactualScore(score);
            entryCasReviewDetail.setFinalTransactionEvaluateLevel(chargeTransactionEvaluateLevel);
            entryCasReviewDetail.setAuditNote(auditNote);
            entryCasReviewDetail.setIsExcellent(PerformanceConstant.NO);
            entryCasReviewDetailMapper.updateById(entryCasReviewDetail);
        }
        return true;
    }

    @Override
    public boolean saveOneReviewDetail(EntryCasReviewDetail entryCasReviewDetail) {
        EntryCasPlanDetail planDetail = entryCasPlanDetailMapper.selectById(entryCasReviewDetail.getCasPlanId());
        if (ObjectUtils.isEmpty(planDetail)) {
            throw new ServiceException(String.format("序号为【%s】的计划不存在,创建失败", entryCasReviewDetail.getCasPlanId()));
        }
        // 判断是否重复创建总结
        LambdaQueryWrapper<EntryCasReviewDetail> reviewQueryWrapper = new LambdaQueryWrapper<>();
        reviewQueryWrapper.eq(EntryCasReviewDetail::getCasPlanId, entryCasReviewDetail.getCasPlanId());
        List<EntryCasReviewDetail> reviewDetails = entryCasReviewDetailMapper.selectList(reviewQueryWrapper);
        if (reviewDetails.size() > 0) {
            throw new ServiceException(String.format("序号为【%s】的计划已经创建总结，不能重复创建", entryCasReviewDetail.getCasPlanId()));
        }
        // 查询MergeNo
        LambdaQueryWrapper<EntryCasMerge> entryCasMergeLambdaQueryWrapper = new LambdaQueryWrapper<>();
        entryCasMergeLambdaQueryWrapper
                .eq(EntryCasMerge::getDepartmentName, entryCasReviewDetail.getDepartmentName())
                .eq(EntryCasMerge::getYear, entryCasReviewDetail.getYear());
        List<EntryCasMerge> entryCasMerges = entryCasMergeMapper.selectList(entryCasMergeLambdaQueryWrapper);
        // 不存在merge就报错
        if (ObjectUtils.isEmpty(entryCasMerges)) {
            throw new ServiceException(String.format("序号为【%s】的计划不存在对应的履职编号，创建失败", entryCasReviewDetail.getCasPlanId()));
        }
        EntryCasMerge entryCasMerge = entryCasMerges.get(0);
        entryCasReviewDetail.setMergeNo(entryCasMerge.getMergeNo());
        entryCasReviewDetail.setMergeId(entryCasMerge.getId());
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        ViewDepartmentRoleUser viewDepartmentRoleUser = GetTool.getDepartmentRoleByUser(user);
        entryCasReviewDetail.setUserId(user.getId());
        entryCasReviewDetail.setUserName(user.getName());
        entryCasReviewDetail.setRoleId(viewDepartmentRoleUser.getRoleId());
        entryCasReviewDetail.setRoleName(viewDepartmentRoleUser.getRoleName());
        entryCasReviewDetail.setCreatedAt(LocalDateTime.now());
        entryCasReviewDetail.setCreateName(user.getName());
        entryCasReviewDetail.setCreateDate(LocalDate.now());
        entryCasReviewDetail.setStatus(PerformanceConstant.WAIT_SUBMIT_AUDIT);
        planDetail.setStatus(PerformanceConstant.FINAL);
        entryCasPlanDetailMapper.updateById(planDetail);
        entryCasReviewDetailMapper.insert(entryCasReviewDetail);
        return true;
    }

    @Override
    @Transactional
    public Boolean storeReviewNotPlan(EntryCasReviewDetail entryCasReviewDetail) {
        // 根据部门查部门id
        LambdaQueryWrapper<Department> departmentLambdaQueryWrapper = new LambdaQueryWrapper<>();
        departmentLambdaQueryWrapper.eq(Department::getDepartmentName, entryCasReviewDetail.getDepartmentName());
        List<Department> departments = departmentMapper.selectList(departmentLambdaQueryWrapper);
        if (ObjectUtils.isEmpty(departments)) {
            throw new ServiceException(String.format("部门名称为【%s】的部门不存在", entryCasReviewDetail.getDepartmentName()));
        }
        Department department = departments.get(0);
        // 根据角色查角色id
        LambdaQueryWrapper<Role> roleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        roleLambdaQueryWrapper.eq(Role::getName, entryCasReviewDetail.getRoleName());
        List<Role> roles = roleMapper.selectList(roleLambdaQueryWrapper);
        if (ObjectUtils.isEmpty(roles)) {
            throw new ServiceException(String.format("部门名称为【%s】的部门不存在", entryCasReviewDetail.getDepartmentName()));
        }
        Role role = roles.get(0);
        // 查询user
        User user = GetTool.getUser();
        // 申报年份
        String year = Arrays.asList(entryCasReviewDetail.getApplyDate().toString().split("-")).get(0);
        String month = Arrays.asList(entryCasReviewDetail.getApplyDate().toString().split("-")).get(1);
        // 新增计划
        EntryCasPlanDetail entryCasPlanDetail = new EntryCasPlanDetail();
        entryCasPlanDetail.setDepartmentId(department.getDepartmentId());
        entryCasPlanDetail.setDepartmentName(department.getDepartmentName());
        entryCasPlanDetail.setRoleId(role.getId());
        entryCasPlanDetail.setRoleName(role.getName());
        entryCasPlanDetail.setUserId(user.getId());
        entryCasPlanDetail.setUserName(user.getName());
        entryCasPlanDetail.setApplyDate(entryCasReviewDetail.getApplyDate());
        entryCasPlanDetail.setYear(Integer.valueOf(year));
        entryCasPlanDetail.setMonth(Integer.valueOf(month));
        entryCasPlanDetail.setPlanStartDate(entryCasReviewDetail.getPlanStartDate());
        entryCasPlanDetail.setPlanEndDate(entryCasReviewDetail.getPlanEndDate());
        // 添加cas-merge
        LambdaQueryWrapper<EntryCasMerge> entryCasMergeLambdaQueryWrapper = new LambdaQueryWrapper<>();
        entryCasMergeLambdaQueryWrapper
                .eq(EntryCasMerge::getDepartmentName, entryCasReviewDetail.getDepartmentName())
                .eq(EntryCasMerge::getYear, LocalDate.now().getYear());

        String key = entryCasReviewDetail.getDepartmentName() + ":" + LocalDate.now().getYear();
        EntryCasMerge entryCasMerge;
        List<EntryCasMerge> entryCasMerges = entryCasMergeMapper.selectList(entryCasMergeLambdaQueryWrapper);
        // 只查到一条数据
        if (entryCasMerges.size() >= 1) {
            entryCasMerge = entryCasMerges.get(0);
        } else
        // 查不到数据
        {
            entryCasMerge = new EntryCasMerge();
            entryCasMerge.setCreateDate(LocalDate.now());
            entryCasMerge.setCreatedAt(LocalDateTime.now());
            entryCasMerge.setCreateId(user.getId());
            entryCasMerge.setCreateName(user.getName());
            entryCasMerge.setDepartmentName(department.getDepartmentName());
            entryCasMerge.setDepartmentId(departments.get(0).getDepartmentId());
            entryCasMerge.setAuditName(null);
            entryCasMerge.setAuditId(null);
            entryCasMerge.setAuditDate(null);
            entryCasMerge.setRoleId(role.getId());
            entryCasMerge.setRoleName(role.getName());
            entryCasMerge.setUserId(user.getId());
            entryCasMerge.setUserName(user.getName());
            // mergeNo
            entryCasMerge.setApplyDate(LocalDate.now());
            entryCasMerge.setYear(LocalDate.now().getYear());
            entryCasMerge.setMonth(LocalDate.now().getMonthValue());
            entryCasMerge.setStatus(PerformanceConstant.WAIT_WRITE_REVIEW);
            // serial
            entryCasMerge = storeNoticeMerge(LocalDate.now(), new HashMap<String, Object>(), entryCasMerge);
        }
        entryCasPlanDetail.setMergeId(entryCasMerge.getId());
        entryCasPlanDetail.setMergeNo(entryCasMerge.getMergeNo());
        entryCasPlanDetailMapper.insert(entryCasPlanDetail);
        // 新增总结(数据写入根据总结导入代码)
        entryCasReviewDetail.setYear(Integer.valueOf(year));
        entryCasReviewDetail.setMonth(Integer.valueOf(month));
        entryCasReviewDetail.setCasPlanId(entryCasPlanDetail.getId());
        entryCasReviewDetail.setStatus(PerformanceConstant.WAIT_SUBMIT_AUDIT);
        entryCasReviewDetail.setMergeId(entryCasPlanDetail.getMergeId());
        entryCasReviewDetail.setMergeNo(entryCasPlanDetail.getMergeNo());
        entryCasReviewDetail.setDepartmentId(department.getDepartmentId());
        entryCasReviewDetail.setRoleId(role.getId());
        entryCasReviewDetail.setUserName(user.getName());
        entryCasReviewDetail.setUserId(user.getId());
        entryCasReviewDetail.setCreatedAt(LocalDateTime.now());
        entryCasReviewDetail.setCreateDate(LocalDate.now());
        entryCasReviewDetail.setCreateName(user.getName());
        entryCasReviewDetail.setCreateId(user.getId());
        entryCasReviewDetailMapper.insert(entryCasReviewDetail);
        return true;
    }

    @Override
    public Page<Map<String, Object>> findDataListByMapParams(Map<String, Object> params) {
        QueryWrapper<EntryCasReviewDetail> queryWrapper = getQueryWrapper(params);
        Page<Map<String, Object>> myPage = new Page<>((int) params.get("page"), (int) params.get("limit"));
        return entryCasReviewDetailMapper.selectMapsPage(myPage, queryWrapper);
    }

    public synchronized EntryCasMerge storeNoticeMerge(LocalDate createDate,
                                                       Map<String, Object> otherParams, EntryCasMerge entryCasMerge) {
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
        entryCasMerge.setMergeNo(
                String.format("JH%s%s", year,
                        new DecimalFormat("000").format(noticeMergeSerial)));
        entryCasMerge.setSerial(noticeMergeSerial);
        entryCasMerge.setYear(year);
        entryCasMerge.setMonth(month);
        entryCasMerge.setCreateDate(createDate);

        entryCasMergeMapper.insert(entryCasMerge);
        // endregion
        return entryCasMerge;
    }
}
