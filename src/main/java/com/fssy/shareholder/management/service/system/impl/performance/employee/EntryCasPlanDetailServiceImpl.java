/**
 * ------------------------修改日志---------------------------------
 * 修改人			修改日期			修改内容
 */
package com.fssy.shareholder.management.service.system.impl.performance.employee;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fssy.shareholder.management.mapper.manage.department.DepartmentMapper;
import com.fssy.shareholder.management.mapper.manage.role.RoleMapper;
import com.fssy.shareholder.management.mapper.manage.user.UserMapper;
import com.fssy.shareholder.management.mapper.system.performance.employee.EntryCasMergeMapper;
import com.fssy.shareholder.management.mapper.system.performance.employee.EntryCasPlanDetailMapper;
import com.fssy.shareholder.management.pojo.manage.department.Department;
import com.fssy.shareholder.management.pojo.manage.department.ViewDepartmentRoleUser;
import com.fssy.shareholder.management.pojo.manage.user.User;
import com.fssy.shareholder.management.pojo.system.config.Attachment;
import com.fssy.shareholder.management.pojo.system.performance.employee.EntryCasMerge;
import com.fssy.shareholder.management.pojo.system.performance.employee.EntryCasPlanDetail;
import com.fssy.shareholder.management.service.common.SheetService;
import com.fssy.shareholder.management.service.system.performance.employee.EntryCasPlanDetailService;
import com.fssy.shareholder.management.tools.common.GetTool;
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
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * <p>
 * *****业务部门： 绩效科 *****数据表中文名： 员工履职计划明细 *****数据表名：
 * bs_performance_employee_entry_cas_plan_detail *****数据表作用： 员工每月月初填写的履职情况计划明细
 * *****变更记录： 时间 变更人 变更内容 20220915 兰宇铧 初始设计 服务实现类
 * </p>
 *
 * @author Solomon
 * @since 2022-10-11
 */
@Service
public class EntryCasPlanDetailServiceImpl extends ServiceImpl<EntryCasPlanDetailMapper, EntryCasPlanDetail> implements EntryCasPlanDetailService {

    @Autowired
    private EntryCasPlanDetailMapper entryCasPlanDetailMapper;

    @Autowired
    private SheetService sheetService;

    @Autowired
    private DepartmentMapper departmentMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private EntryCasMergeMapper entryCasMergeMapper;

    @Override
    public Page<EntryCasPlanDetail> findDataListByParams(Map<String, Object> params) {
        QueryWrapper<EntryCasPlanDetail> queryWrapper = getQueryWrapper(params).orderByDesc("id");// 全局根据id倒序
        Page<EntryCasPlanDetail> myPage = new Page<>((int) params.get("page"), (int) params.get("limit"));
        return entryCasPlanDetailMapper.selectPage(myPage, queryWrapper);
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
    public Map<String, Object> readEntryCasPlanDetailDataSource(Attachment attachment) {
        // 导入月度履职计划
        // 返回消息
        Map<String, Object> result = new HashMap<>();
        StringBuffer sb = new StringBuffer();
        result.put("content", "");
        result.put("failed", false);
        // 读取附件
        sheetService.load(attachment.getPath(), attachment.getFilename()); // 根据路径和名称读取附件
        // 读取表单
        sheetService.readByName("履职管控表"); //根据表单名称获取该工作表单
        // 获取表单数据
        Sheet sheet = sheetService.getSheet();
        if (ObjectUtils.isEmpty(sheet)) {
            throw new ServiceException("表单【履职管控表】不存在，无法读取数据，请检查");
        }

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
            // 导入结果写入列
            Cell cell = row.createCell(SheetService.columnToIndex("Z"));// 每一行的结果信息上传到S列
            // 检查必填项
            String eventsRoleId = cells.get(SheetService.columnToIndex("A"));// 岗位关系序号
            String eventsId = cells.get(SheetService.columnToIndex("B"));// 事件清单序号
            String eventsFirstType = cells.get(SheetService.columnToIndex("C"));//事务类别
            String jobName = cells.get(SheetService.columnToIndex("D"));//工作职责
            String workEvents = cells.get(SheetService.columnToIndex("E"));//流程（工作事件）
            String departmentName = cells.get(SheetService.columnToIndex("F"));//部门
            String standardValue = cells.get(SheetService.columnToIndex("G"));// 事件价值标准分
            String mainOrNext = cells.get(SheetService.columnToIndex("H"));// 主/次担
            String userName = cells.get(SheetService.columnToIndex("I"));// 岗位人员姓名
            // 填写的数据
            String planningWork = cells.get(SheetService.columnToIndex("J"));// 对应工作事件的计划内容
            String times = cells.get(SheetService.columnToIndex("K"));// 频次
            String planOutput = cells.get(SheetService.columnToIndex("L"));// 表单（输出内容）
            String planStartDateStr = cells.get(SheetService.columnToIndex("M"));// 计划开始时间
            String planEndDateStr = cells.get(SheetService.columnToIndex("N"));// 计划完成时间

            // 检查必填项
            if (StringUtils.isEmpty(departmentName)) {
                setFailedContent(result, String.format("第%s行的部门名称为空", j + 1));
                cell.setCellValue("部门名称不能为空");
                continue;
            }
            if (StringUtils.isEmpty(userName)) {
                setFailedContent(result, String.format("第%s行的员工名称为空", j + 1));
                cell.setCellValue("员工名称不能为空");
                continue;
            }
            if (StringUtils.isEmpty(planStartDateStr)) {
                setFailedContent(result, String.format("第%s行的计划开始时间为空", j + 1));
                cell.setCellValue("计划开始时间不能为空");
                continue;
            }
            String planStartDate = planStartDateStr.trim().substring(0, 10);
            if (StringUtils.isEmpty(planEndDateStr)) {
                setFailedContent(result, String.format("第%s行的计划完成时间为空", j + 1));
                cell.setCellValue("计划完成时间不能为空");
                continue;
            }
            String planEndDate = planEndDateStr.trim().substring(0, 10);
            // 新增工作流不能填有价值分
            if (eventsFirstType.equals(PerformanceConstant.EVENTS_FIRST_TYPE_C) && !ObjectUtils.isEmpty(standardValue)) {
                throw new ServiceException(String.format("第【%s】行的价值分不能填写值，因为它的事件类型为【新增工作流】", j + 1));
            }
            // 数据校验
            if (!(eventsFirstType.equals(PerformanceConstant.EVENTS_FIRST_TYPE_B) || eventsFirstType.equals(PerformanceConstant.EVENTS_FIRST_TYPE_A) || eventsFirstType.equals(PerformanceConstant.EVENTS_FIRST_TYPE_C))) {
                setFailedContent(result, String.format("第%s行的事件类型填写错误", j + 1));
                cell.setCellValue("事件类型填写错误");
                continue;
            }

            // 构建实体类
            EntryCasPlanDetail entryCasPlanDetail = new EntryCasPlanDetail();
            // 查询当前登录用户
            User user = (User) SecurityUtils.getSubject().getPrincipal();
            // 查询视图
            ViewDepartmentRoleUser departmentRoleByUser = GetTool.getDepartmentRoleByUser(user);
            if (!ObjectUtils.isEmpty(eventsRoleId)) {
                entryCasPlanDetail.setEventsRoleId(Long.valueOf(eventsRoleId));
            }
            // 如果事件类型为新增工作流，设置字段isNewEvent为是
            if (eventsFirstType.equals(PerformanceConstant.EVENTS_FIRST_TYPE_C)) {
                entryCasPlanDetail.setIsNewEvent(PerformanceConstant.YES);
            } else entryCasPlanDetail.setIsNewEvent(PerformanceConstant.NO);
            entryCasPlanDetail.setEventsFirstType(eventsFirstType);
            entryCasPlanDetail.setJobName(jobName);
            entryCasPlanDetail.setWorkEvents(workEvents);
            entryCasPlanDetail.setMainOrNext(mainOrNext);
            entryCasPlanDetail.setPlanningWork(planningWork);
            entryCasPlanDetail.setTimes(times);
            entryCasPlanDetail.setPlanOutput(planOutput);
            entryCasPlanDetail.setPlanStartDate(LocalDate.parse(planStartDate));
            entryCasPlanDetail.setPlanEndDate(LocalDate.parse(planEndDate));
            entryCasPlanDetail.setStatus(PerformanceConstant.PLAN_DETAIL_STATUS_SUBMIT_AUDIT);
            if (!ObjectUtils.isEmpty(standardValue)) {
                entryCasPlanDetail.setStandardValue(new BigDecimal(standardValue));
            }
            entryCasPlanDetail.setAttachmentId(attachment.getId());
            // 数据库不能为null的字段设置值
            if (!ObjectUtils.isEmpty(eventsId)) {
                entryCasPlanDetail.setEventsId(Long.valueOf(eventsId));
            }
            entryCasPlanDetail.setDepartmentName(departmentName);
            LambdaQueryWrapper<Department> departmentLambdaQueryWrapper = new LambdaQueryWrapper<>();
            departmentLambdaQueryWrapper.eq(Department::getDepartmentName, departmentName);
            List<Department> departments = departmentMapper.selectList(departmentLambdaQueryWrapper);
            if (ObjectUtils.isEmpty(departments)) {
                setFailedContent(result, String.format("第%s行的部门未维护", j + 1));
                cell.setCellValue("表中部门名称未维护");
                continue;
            }
            entryCasPlanDetail.setDepartmentId(departments.get(0).getId());
            entryCasPlanDetail.setRoleName(departmentRoleByUser.getRoleName());
            entryCasPlanDetail.setRoleId(departmentRoleByUser.getRoleId());
            entryCasPlanDetail.setUserName(userName);
            List<User> users = userMapper.selectList(new LambdaQueryWrapper<User>().eq(User::getName, userName));
            if (ObjectUtils.isEmpty(users)) {
                setFailedContent(result, String.format("第%s行的员工名称未维护", j + 1));
                cell.setCellValue("表中员工名称未维护");
                continue;
            }
            entryCasPlanDetail.setUserId(users.get(0).getId());
            entryCasPlanDetail.setApplyDate(LocalDate.now());
            entryCasPlanDetail.setYear(LocalDate.now().getYear());
            entryCasPlanDetail.setMonth(LocalDate.now().getMonthValue());
            entryCasPlanDetail.setCreatedAt(LocalDateTime.now());
            entryCasPlanDetail.setCreateId(user.getId());
            entryCasPlanDetail.setCreateDate(LocalDate.now());
            entryCasPlanDetail.setCreateName(user.getName());
            // 根据条件查询或生成bs_performance_employee_entry_cas_merge表数据
            LambdaQueryWrapper<EntryCasMerge> entryCasMergeLambdaQueryWrapper = new LambdaQueryWrapper<>();
            entryCasMergeLambdaQueryWrapper
                    .eq(EntryCasMerge::getDepartmentName, departmentName)
                    .eq(EntryCasMerge::getYear, LocalDate.now().getYear());

            String key = departmentName + ":" + LocalDate.now().getYear();
            EntryCasMerge entryCasMerge;
            if (mergeMap.containsKey(key)) // 缓存存在则获取
            {
                entryCasMerge = mergeMap.get(key);
            } else {
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
                    entryCasMerge.setDepartmentName(departmentName);
                    entryCasMerge.setDepartmentId(departments.get(0).getDepartmentId());
                    entryCasMerge.setAuditName(null);
                    entryCasMerge.setAuditId(null);
                    entryCasMerge.setAuditDate(null);
                    entryCasMerge.setRoleId(departmentRoleByUser.getRoleId());
                    entryCasMerge.setRoleName(departmentRoleByUser.getRoleName());
                    entryCasMerge.setUserId(user.getId());
                    entryCasMerge.setUserName(user.getName());
                    // mergeNo
                    entryCasMerge.setApplyDate(LocalDate.now());
                    entryCasMerge.setYear(LocalDate.now().getYear());
                    entryCasMerge.setMonth(LocalDate.now().getMonthValue());
                    entryCasMerge.setStatus(PerformanceConstant.ENTRY_CAS_PLAN_DETAIL_STATUS_REVIEW);
                    // serial
                    entryCasMerge = storeNoticeMerge(LocalDate.now(), new HashMap<String, Object>(), entryCasMerge);
                }
                // 存入缓存
                mergeMap.put(key, entryCasMerge);
            }

            // 添加计划表编号和序号
            entryCasPlanDetail.setMergeNo(entryCasMerge.getMergeNo());
            entryCasPlanDetail.setMergeId(entryCasMerge.getId());

            // 更新
            entryCasPlanDetailMapper.insert(entryCasPlanDetail);
            cell.setCellValue("导入成功");// 写在upload目录下的excel表格
        }
        sheetService.write(attachment.getPath(), attachment.getFilename());// 写入excel表
        return result;
    }

    @Override
    public List<Map<String, Object>> findEntryCasPlanDetailMapDataByParams(Map<String, Object> params) {
        QueryWrapper<EntryCasPlanDetail> queryWrapper = getQueryWrapper(params);
        return entryCasPlanDetailMapper.selectMaps(queryWrapper);
    }


    private QueryWrapper<EntryCasPlanDetail> getQueryWrapper(Map<String, Object> params) {
        QueryWrapper<EntryCasPlanDetail> queryWrapper = new QueryWrapper<>();
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
        // 用户表主键列表查询
        if (params.containsKey("userIds")) {
            queryWrapper.in("userId", (List<String>) params.get("userIds"));
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
        if (params.containsKey("auditStatus")) {
            queryWrapper.like("auditStatus", params.get("auditStatus"));
        }
        if (params.containsKey("attachmentId")) {
            queryWrapper.eq("attachmentId", params.get("attachmentId"));
        }
        if (params.containsKey("statusCancel")) {
            queryWrapper.ne("status", PerformanceConstant.EVENT_LIST_STATUS_CANCEL);
        }
        if (params.containsKey("newStatus")) {
            queryWrapper.eq("newStatus", params.get("newStatus"));
        }
        // 状态为’待部长审核‘和事件类型为’非实物类‘或’新增工作流‘
        if (params.containsKey("twoStatus")) {
            queryWrapper
                    .eq("status", PerformanceConstant.PLAN_DETAIL_STATUS_AUDIT_MINISTER)
                    .and(item -> item.eq("eventsFirstType", PerformanceConstant.EVENTS_FIRST_TYPE_B).or().eq("eventsFirstType", PerformanceConstant.EVENTS_FIRST_TYPE_C));
        }
        // 审核页面，左侧表格按人名分组
        if (params.containsKey("groupByUserName")) {
            queryWrapper.select("userName,status").groupBy("userName");
        }
        // 审核页面，右侧表格根据左侧双击选择的名字显示
        if (params.containsKey("userNameRight")) {
            queryWrapper.eq("userName", params.get("userNameRight"));
        }
        return queryWrapper;
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

    /**
     * 提交审核
     *
     * @param planDetailIds
     * @return
     */
    @Override
    public boolean submitAudit(List<String> planDetailIds) {
        List<EntryCasPlanDetail> entryCasPlanDetails = entryCasPlanDetailMapper.selectBatchIds(planDetailIds);
        for (EntryCasPlanDetail entryCasPlanDetail : entryCasPlanDetails) {
            // 如果计划的状态为’待提交审核‘
            if (entryCasPlanDetail.getStatus().equals(PerformanceConstant.PLAN_DETAIL_STATUS_SUBMIT_AUDIT)) {
                LambdaUpdateWrapper<EntryCasPlanDetail> entryCasPlanDetailLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                // 如果事件类型为’事务类‘
                if (entryCasPlanDetail.getEventsFirstType().equals(PerformanceConstant.EVENTS_FIRST_TYPE_A)) {
                    entryCasPlanDetail.setStatus(PerformanceConstant.PLAN_DETAIL_STATUS_AUDIT_KEZHANG);
                    entryCasPlanDetailLambdaUpdateWrapper.eq(EntryCasPlanDetail::getId, entryCasPlanDetail.getId());
                    entryCasPlanDetailMapper.update(entryCasPlanDetail, entryCasPlanDetailLambdaUpdateWrapper);
                } else {
                    // 如果事件类型为’非事务类’或者‘新增工作流‘
                    entryCasPlanDetail.setStatus(PerformanceConstant.PLAN_DETAIL_STATUS_AUDIT_MINISTER);
                    entryCasPlanDetailLambdaUpdateWrapper.eq(EntryCasPlanDetail::getId, entryCasPlanDetail.getId());
                    entryCasPlanDetailMapper.update(entryCasPlanDetail, entryCasPlanDetailLambdaUpdateWrapper);
                }
            } else return false;
        }
        return true;
    }

    /**
     * 撤销审核
     *
     * @param planDetailIds
     * @return
     */
    @Override
    public boolean retreat(List<String> planDetailIds) {
        List<EntryCasPlanDetail> entryCasPlanDetails = entryCasPlanDetailMapper.selectBatchIds(planDetailIds);
        for (EntryCasPlanDetail entryCasPlanDetail : entryCasPlanDetails) {
            //校验方法
            if (entryCasPlanDetail.getStatus().equals(PerformanceConstant.PLAN_DETAIL_STATUS_SUBMIT_AUDIT) || entryCasPlanDetail.getStatus().equals(PerformanceConstant.ENTRY_CAS_PLAN_DETAIL_STATUS_REVIEW)) {
                throw new ServiceException("不能撤销待提交审核状态或待填报回顾状态下的的履职明细");
            }
            LambdaUpdateWrapper<EntryCasPlanDetail> entryCasPlanDetailLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            if (entryCasPlanDetail.getStatus().equals(PerformanceConstant.PLAN_DETAIL_STATUS_AUDIT_KEZHANG) ||
                    entryCasPlanDetail.getStatus().equals(PerformanceConstant.PLAN_DETAIL_STATUS_AUDIT_MINISTER)) {
                entryCasPlanDetail.setStatus(PerformanceConstant.PLAN_DETAIL_STATUS_SUBMIT_AUDIT);
                entryCasPlanDetailLambdaUpdateWrapper.eq(EntryCasPlanDetail::getId, entryCasPlanDetail.getId());
                entryCasPlanDetailMapper.update(entryCasPlanDetail, entryCasPlanDetailLambdaUpdateWrapper);
                continue;
            }

        }
        return true;
    }

    /**
     * 审核结果
     *
     * @param planDetailIds
     * @param
     * @return 通过/拒绝
     */
    @Override
    public boolean affirmStore(List<String> planDetailIds, String event, List<String> auditNotes) {
        List<EntryCasPlanDetail> entryCasPlanDetails = entryCasPlanDetailMapper.selectBatchIds(planDetailIds);
        for (int i = 0; i < entryCasPlanDetails.size(); i++) {
            String auditNote = null;
            if (!ObjectUtils.isEmpty(auditNotes)) {
                auditNote = auditNotes.get(i);
            }
            EntryCasPlanDetail entryCasPlanDetail = entryCasPlanDetails.get(i);
            if (event.equals("pass")) {
                // 部长、科长审核通过
                entryCasPlanDetail.setStatus(PerformanceConstant.ENTRY_CAS_PLAN_DETAIL_STATUS_REVIEW);
                entryCasPlanDetail.setAuditNote(auditNote);
                entryCasPlanDetailMapper.updateById(entryCasPlanDetail);
            }
            // 部长、科长审核拒绝
            else if (event.equals("noPass")) {
                entryCasPlanDetail.setStatus(PerformanceConstant.PLAN_DETAIL_STATUS_SUBMIT_AUDIT);
                entryCasPlanDetail.setAuditNote(auditNote);
                entryCasPlanDetailMapper.updateById(entryCasPlanDetail);
            }
        }
        return true;
    }

    @Override
    public boolean HRAffirmStore(List<String> planDetailIds, String event) {
        List<EntryCasPlanDetail> entryCasPlanDetails = entryCasPlanDetailMapper.selectBatchIds(planDetailIds);
        for (EntryCasPlanDetail entryCasPlanDetail : entryCasPlanDetails) {
            if (entryCasPlanDetail.getStatus().equals(PerformanceConstant.PLAN_DETAIL_STATUS_AUDIT_HR)) {
                if (event.equals("yes")) {
                    // 属于新增工作流,设置新状态为待创建基础事件
                    entryCasPlanDetail.setNewStatus(PerformanceConstant.WAIT_CREATE_EVENT);
                } else if (event.equals("no")) {
                    // 不属于新增工作流，设置状态为待选择基础事件
                    entryCasPlanDetail.setNewStatus(PerformanceConstant.PLAN_DETAIL_STATUS_SELECT);
                }
            }
            entryCasPlanDetail.setStatus(PerformanceConstant.ENTRY_CAS_PLAN_DETAIL_STATUS_REVIEW);
            entryCasPlanDetailMapper.updateById(entryCasPlanDetail);
        }
        return true;
    }

    @Override
    public boolean SelectUpdate(EntryCasPlanDetail entryCasPlanDetail) {
        return false;
    }

    @Override
    public boolean saveOneCasPlanDetail(EntryCasPlanDetail entryCasPlanDetail, HttpServletRequest request) {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        ViewDepartmentRoleUser viewDepartmentRoleUser = GetTool.getDepartmentRoleByUser(user);
        entryCasPlanDetail.setUserId(user.getId());
        entryCasPlanDetail.setCreatedAt(LocalDateTime.now());
        entryCasPlanDetail.setCreateId(user.getId());
        entryCasPlanDetail.setCreateDate(LocalDate.now());
        entryCasPlanDetail.setCreateName(user.getName());
        entryCasPlanDetail.setRoleName(viewDepartmentRoleUser.getRoleName());
        entryCasPlanDetail.setRoleId(viewDepartmentRoleUser.getRoleId());
        entryCasPlanDetail.setStatus(PerformanceConstant.PLAN_DETAIL_STATUS_SUBMIT_AUDIT);
        entryCasPlanDetail.setApplyDate(LocalDate.now());
        String eventsRoleId = request.getParameter("eventsRoleId");
        String eventsFirstType = request.getParameter("eventsFirstType");
        entryCasPlanDetail.setEventsFirstType(eventsFirstType);

        // 如果事件类型为新增工作流，设置字段isNewEvent为是

        // 查询视图
        ViewDepartmentRoleUser departmentRoleByUser = GetTool.getDepartmentRoleByUser(user);
        if (!ObjectUtils.isEmpty(eventsRoleId)) {
            entryCasPlanDetail.setEventsRoleId(Long.valueOf(eventsRoleId));
        }
        // 如果事件类型为新增工作流，设置字段isNewEvent为是
        if (eventsFirstType.equals(PerformanceConstant.EVENTS_FIRST_TYPE_C)) {
            entryCasPlanDetail.setIsNewEvent(PerformanceConstant.YES);
        } else entryCasPlanDetail.setIsNewEvent(PerformanceConstant.NO);


        Map<String, EntryCasMerge> mergeMap = new HashMap<>();
        // 根据条件查询或生成bs_performance_employee_entry_cas_merge表数据
        LambdaQueryWrapper<EntryCasMerge> entryCasMergeLambdaQueryWrapper = new LambdaQueryWrapper<>();
        entryCasMergeLambdaQueryWrapper
                .eq(EntryCasMerge::getDepartmentName, viewDepartmentRoleUser)
                .eq(EntryCasMerge::getYear, LocalDate.now().getYear());

        String key = viewDepartmentRoleUser + ":" + LocalDate.now().getYear();
        EntryCasMerge entryCasMerge;
        if (mergeMap.containsKey(key)) // 缓存存在则获取
        {
            entryCasMerge = mergeMap.get(key);
        } else {
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
                entryCasMerge.setDepartmentName(viewDepartmentRoleUser.getDepartmentName());
                entryCasMerge.setDepartmentId(viewDepartmentRoleUser.getDepartmentId());
                entryCasMerge.setAuditName(null);
                entryCasMerge.setAuditId(null);
                entryCasMerge.setAuditDate(null);
                entryCasMerge.setRoleId(viewDepartmentRoleUser.getRoleId());
                entryCasMerge.setRoleName(viewDepartmentRoleUser.getRoleName());
                entryCasMerge.setUserId(user.getId());
                entryCasMerge.setUserName(user.getName());
                // mergeNo
                entryCasMerge.setApplyDate(LocalDate.now());
                entryCasMerge.setYear(LocalDate.now().getYear());
                entryCasMerge.setMonth(LocalDate.now().getMonthValue());
                entryCasMerge.setStatus(PerformanceConstant.ENTRY_CAS_PLAN_DETAIL_STATUS_REVIEW);
                // serial
                entryCasMerge = storeNoticeMerge(LocalDate.now(), new HashMap<String, Object>(), entryCasMerge);
            }
            // 存入缓存
            mergeMap.put(key, entryCasMerge);
        }

        // 添加计划表编号和序号
        entryCasPlanDetail.setMergeNo(entryCasMerge.getMergeNo());
        entryCasPlanDetail.setMergeId(entryCasMerge.getId());

        int insert = entryCasPlanDetailMapper.insert(entryCasPlanDetail);
        return insert > 0;
    }

    @Override
    public Page<Map<String, Object>> findDataListByMapParams(Map<String, Object> params) {
        QueryWrapper<EntryCasPlanDetail> queryWrapper = getQueryWrapper(params);
        Page<Map<String, Object>> myPage = new Page<>((int) params.get("page"), (int) params.get("limit"));
        return entryCasPlanDetailMapper.selectMapsPage(myPage, queryWrapper);
    }

}
