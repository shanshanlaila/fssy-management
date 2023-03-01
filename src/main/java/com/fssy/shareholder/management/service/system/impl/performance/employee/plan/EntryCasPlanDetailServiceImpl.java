/**
 * ------------------------修改日志---------------------------------
 * 修改人			修改日期			修改内容
 * 兰宇铧			2022-12-30 		修改问题，查询条件添加月份和部门并修改编号规则
 */
package com.fssy.shareholder.management.service.system.impl.performance.employee.plan;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fssy.shareholder.management.mapper.manage.department.DepartmentMapper;
import com.fssy.shareholder.management.mapper.manage.user.UserMapper;
import com.fssy.shareholder.management.mapper.system.performance.employee.EntryCasMergeMapper;
import com.fssy.shareholder.management.mapper.system.performance.employee.EntryCasPlanDetailMapper;
import com.fssy.shareholder.management.mapper.system.performance.employee.EventListMapper;
import com.fssy.shareholder.management.mapper.system.performance.employee.EventsRelationRoleMapper;
import com.fssy.shareholder.management.pojo.manage.department.ViewDepartmentRoleUser;
import com.fssy.shareholder.management.pojo.manage.user.User;
import com.fssy.shareholder.management.pojo.system.config.Attachment;
import com.fssy.shareholder.management.pojo.system.performance.employee.EntryCasMerge;
import com.fssy.shareholder.management.pojo.system.performance.employee.EntryCasPlanDetail;
import com.fssy.shareholder.management.pojo.system.performance.employee.EventList;
import com.fssy.shareholder.management.pojo.system.performance.employee.EventsRelationRole;
import com.fssy.shareholder.management.service.common.SheetService;
import com.fssy.shareholder.management.service.system.performance.employee.EntryCasPlanDetailService;
import com.fssy.shareholder.management.tools.common.GetTool;
import com.fssy.shareholder.management.tools.common.IteratorTool;
import com.fssy.shareholder.management.tools.constant.PerformanceConstant;
import com.fssy.shareholder.management.tools.exception.ServiceException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
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
    private UserMapper userMapper;

    @Autowired
    private EntryCasMergeMapper entryCasMergeMapper;

    @Autowired
    private EventListMapper eventListMapper;

    @Autowired
    private EventsRelationRoleMapper eventsRelationRoleMapper;


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
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> readEntryCasPlanDetailDataSource(Attachment attachment, HttpServletRequest request) {
        String yearAndMonth = request.getParameter("yearAndMonth");
        if (ObjectUtils.isEmpty(yearAndMonth)) {
            throw new ServiceException("未选择申报年月，导入失败");
        }
        // 导入月度履职计划
        // 返回消息
        Map<String, Object> result = new HashMap<>();
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
            String eventsRoleId = cells.get(SheetService.columnToIndex("A"));// 事件岗位ID
            String eventsId = cells.get(SheetService.columnToIndex("B"));// 事件清单ID
            /*String departmentName = cells.get(SheetService.columnToIndex("C"));// 部门
            String userName = cells.get(SheetService.columnToIndex("E"));// 岗位人员姓名
            String eventsFirstType = cells.get(SheetService.columnToIndex("F"));// 事件类型
            String jobName = cells.get(SheetService.columnToIndex("G"));// 工作职责
            String workEvents = cells.get(SheetService.columnToIndex("H"));// 流程（工作事件）
            String standardValue = cells.get(SheetService.columnToIndex("I"));// 事件价值标准分
            String mainOrNext = cells.get(SheetService.columnToIndex("J"));// 主/次担*/
            // 填写的数据
            String planningWork = cells.get(SheetService.columnToIndex("L"));// 计划内容
            String times = cells.get(SheetService.columnToIndex("M"));// 频次
            String planOutput = cells.get(SheetService.columnToIndex("N"));// 表单（输出内容）
            String planStartDateStr = cells.get(SheetService.columnToIndex("O"));// 计划开始时间
            String planEndDateStr = cells.get(SheetService.columnToIndex("P"));// 计划完成时间

            // 检查必填项
            if (StringUtils.isEmpty(eventsRoleId)) {
                setFailedContent(result, String.format("第%s事件岗位Id为空", j + 1));
                cell.setCellValue(String.format("第%s事件岗位Id为空", j + 1));
                throw new ServiceException(String.format("第%s事件岗位Id为空", j + 1));
            }

            if (StringUtils.isEmpty(eventsId)) {
                setFailedContent(result, String.format("第%s基础事件Id为空", j + 1));
                cell.setCellValue("基础事件Id为空");
                throw new ServiceException(String.format("第%s基础事件Id为空", j + 1));
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
            // 查询部门-角色-姓名视图
            ViewDepartmentRoleUser depRoleUser = GetTool.getDepartmentRoleByUser();
            // 查询事件岗位
            EventsRelationRole eventsRelationRole = eventsRelationRoleMapper.selectById(eventsRoleId);
            // 校验是否当前登陆人操作数据
            if (!depRoleUser.getUserId().equals(eventsRelationRole.getUserId())) {
                setFailedContent(result, String.format("第%s行的数据不为您本人的，导入失败", j + 1));
                cell.setCellValue("导入失败，请导入本人的数据");
                throw new ServiceException("导入失败，请导入本人的数据");
            }
            String planEndDate = planEndDateStr.trim().substring(0, 10);
            // 构建实体类
            EntryCasPlanDetail entryCasPlanDetail = new EntryCasPlanDetail();
            entryCasPlanDetail.setEventsRoleId(Long.valueOf(eventsRoleId));
            entryCasPlanDetail.setIsNewEvent(PerformanceConstant.NO);
            entryCasPlanDetail.setEventsFirstType(eventsRelationRole.getEventsFirstType());
            entryCasPlanDetail.setJobName(eventsRelationRole.getJobName());
            entryCasPlanDetail.setWorkEvents(eventsRelationRole.getWorkEvents());
            entryCasPlanDetail.setMainOrNext(eventsRelationRole.getIsMainOrNext());
            entryCasPlanDetail.setPlanningWork(planningWork);
            entryCasPlanDetail.setTimes(times);
            entryCasPlanDetail.setPlanOutput(planOutput);
            entryCasPlanDetail.setPlanStartDate(LocalDate.parse(planStartDate));
            entryCasPlanDetail.setPlanEndDate(LocalDate.parse(planEndDate));
            entryCasPlanDetail.setStatus(PerformanceConstant.WAIT_SUBMIT_AUDIT);
            entryCasPlanDetail.setStandardValue(eventsRelationRole.getStandardValue());
            entryCasPlanDetail.setAttachmentId(attachment.getId());
            // 数据库不能为null的字段设置值
            entryCasPlanDetail.setEventsId(Long.valueOf(eventsId));
            entryCasPlanDetail.setDepartmentName(eventsRelationRole.getDepartmentName());

            EventList eventList = eventListMapper.selectById(eventsId);
            entryCasPlanDetail.setWorkOutput(eventList.getWorkOutput());
            entryCasPlanDetail.setDepartmentId(eventsRelationRole.getDepartmentId());
            entryCasPlanDetail.setRoleName(eventsRelationRole.getRoleName());
            entryCasPlanDetail.setRoleId(eventsRelationRole.getRoleId());
            entryCasPlanDetail.setUserName(depRoleUser.getUserName());
            entryCasPlanDetail.setOffice(eventsRelationRole.getOffice());
            entryCasPlanDetail.setOfficeId(eventsRelationRole.getOfficeId());
            entryCasPlanDetail.setUserId(depRoleUser.getUserId());
            entryCasPlanDetail.setApplyDate(LocalDate.now());

            String[] date = yearAndMonth.split("-");
            entryCasPlanDetail.setYear(Integer.valueOf(date[0]));
            entryCasPlanDetail.setMonth(Integer.valueOf(date[1]));
            entryCasPlanDetail.setCreatedAt(LocalDateTime.now());
            entryCasPlanDetail.setCreateId(depRoleUser.getUserId());
            entryCasPlanDetail.setCreateDate(LocalDate.now());
            entryCasPlanDetail.setCreateName(depRoleUser.getUserName());
            // 根据条件查询或生成bs_performance_employee_entry_cas_merge表数据
            LambdaQueryWrapper<EntryCasMerge> entryCasMergeLambdaQueryWrapper = new LambdaQueryWrapper<>();
            entryCasMergeLambdaQueryWrapper
                    .eq(EntryCasMerge::getDepartmentId, depRoleUser.getDepartmentId())
                    .eq(EntryCasMerge::getYear, LocalDate.now().getYear())
                    .eq(EntryCasMerge::getMonth, LocalDate.now().getMonthValue());

            String key = depRoleUser.getDepartmentId() + ":" + LocalDate.now().getYear() + ":" + LocalDate.now().getMonthValue();
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
                    entryCasMerge.setCreateId(depRoleUser.getUserId());
                    entryCasMerge.setCreateName(depRoleUser.getUserName());
                    entryCasMerge.setDepartmentName(depRoleUser.getDepartmentName());
                    entryCasMerge.setDepartmentId(depRoleUser.getDepartmentId());
                    entryCasMerge.setAuditName(null);
                    entryCasMerge.setAuditId(null);
                    entryCasMerge.setAuditDate(null);
                    entryCasMerge.setRoleId(depRoleUser.getRoleId());
                    entryCasMerge.setRoleName(depRoleUser.getRoleName());
                    entryCasMerge.setUserId(depRoleUser.getUserId());
                    entryCasMerge.setUserName(depRoleUser.getUserName());
                    entryCasMerge.setOffice(eventsRelationRole.getOffice());
                    entryCasMerge.setOfficeId(eventsRelationRole.getOfficeId());
                    // mergeNo
                    entryCasMerge.setApplyDate(LocalDate.now());
                    entryCasMerge.setYear(LocalDate.now().getYear());
                    entryCasMerge.setMonth(LocalDate.now().getMonthValue());
                    entryCasMerge.setStatus(PerformanceConstant.WAIT_WRITE_REVIEW);
                    // serial
                    entryCasMerge = storeNoticeMerge(LocalDate.now(), new HashMap<String, Object>(), entryCasMerge);
                }
                // 存入缓存
                mergeMap.put(key, entryCasMerge);
            }

            // 添加计划表编号和序号
            entryCasPlanDetail.setMergeNo(entryCasMerge.getMergeNo());
            entryCasPlanDetail.setMergeId(entryCasMerge.getId());
            //设置事件岗位清单状态为完结
            if (!ObjectUtils.isEmpty(eventsRelationRole)) {
                eventsRelationRole.setStatus(PerformanceConstant.FINAL);
                eventsRelationRoleMapper.updateById(eventsRelationRole);
            }
            // 写入计划
            entryCasPlanDetailMapper.insert(entryCasPlanDetail);
            cell.setCellValue("导入成功");
        }
        // 写入excel表
        sheetService.write(attachment.getPath(), attachment.getFilename());
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
        if (params.containsKey("departmentIds")) {
            String departmentIdsStr = (String) params.get("departmentIds");
            List<String> departmentIds = Arrays.asList(departmentIdsStr.split(","));
            queryWrapper.in("departmentId", departmentIds);
        }
        if (params.containsKey("roleName")) {
            queryWrapper.like("roleName", params.get("roleName"));
        }
        if (params.containsKey("roleId")) {
            queryWrapper.eq("roleId", params.get("roleId"));
        }
        if (params.containsKey("roleIds")) {
            String roleIds = (String) params.get("roleIds");
            List<String> roleIdList = Arrays.asList(roleIds.split(","));
            queryWrapper.in("roleId", roleIdList);
        }
        if (params.containsKey("userName")) {
            queryWrapper.like("userName", params.get("userName"));
        }
        if (params.containsKey("userId")) {
            queryWrapper.eq("userId", params.get("userId"));
        }
        // 用户表主键列表查询
        if (params.containsKey("userIds")) {
            String userIdsStr = (String) params.get("userIds");
            List<String> userIds = Arrays.asList(userIdsStr.split(","));
            queryWrapper.in("userId", userIds);
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
            queryWrapper.ne("status", PerformanceConstant.CANCEL);
        }
        if (params.containsKey("newStatus")) {
            queryWrapper.eq("newStatus", params.get("newStatus"));
        }
        if (params.containsKey("isNewEvent")) {
            queryWrapper.eq("isNewEvent", params.get("isNewEvent"));
        }
        // 审核页面，左侧表格按人名分组
        if (params.containsKey("groupByUserName")) {
            queryWrapper.select("userName,roleName,departmentName").groupBy("userName", "roleName", "departmentName");
        }
        // 审核页面，右侧表格根据左侧双击选择的名字显示
        if (params.containsKey("userNameRight")) {
            queryWrapper.eq("userName", params.get("userNameRight"));
        }
        // 编制日期起
        if (params.containsKey("createDateStart")) {
            queryWrapper.ge("createDate", params.get("createDateStart"));
        }
        // 编制日期止
        if (params.containsKey("createDateEnd")) {
            queryWrapper.le("createDate", params.get("createDateEnd"));
        }
        // 计划开始日期起
        if (params.containsKey("planStartDateStart")) {
            queryWrapper.ge("planStartDate", params.get("planStartDateStart"));
        }
        // 计划开始日期止
        if (params.containsKey("planStartDateEnd")) {
            queryWrapper.le("planStartDate", params.get("planStartDateEnd"));
        }
        // 计划完成日期起
        if (params.containsKey("planEndDateStart")) {
            queryWrapper.ge("planEndDate", params.get("planEndDateStart"));
        }
        // 计划完成日期止
        if (params.containsKey("planEndDateEnd")) {
            queryWrapper.le("planEndDate", params.get("planEndDateEnd"));
        }
        // 申报日期起
        if (params.containsKey("applyDateStart")) {
            queryWrapper.ge("applyDate", params.get("applyDateStart"));
        }
        // 申报日期止
        if (params.containsKey("applyDateEnd")) {
            queryWrapper.le("applyDate", params.get("applyDateEnd"));
        }
        return queryWrapper;
    }

    /**
     * 履职表编号生成并保存(线程不安全，需要加锁)
     *
     * @param createDate    创建日期
     * @param otherParams   其他参数
     * @param entryCasMerge 履职表对象
     * @return
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public synchronized EntryCasMerge storeNoticeMerge(LocalDate createDate,
                                                       Map<String, Object> otherParams, EntryCasMerge entryCasMerge) {
        // region 创建履职表数据
        // 生成履职表序列号
        int noticeMergeSerial;
        int year = createDate.getYear();
        int month = createDate.getMonthValue();
        QueryWrapper<EntryCasMerge> queryNoticeMergeSerialQueryWrapper = new QueryWrapper<>();
        // 2022-12-30 修改问题，查询条件添加月份和部门并修改编号规则
        queryNoticeMergeSerialQueryWrapper.eq("year", year)
                .eq("month", entryCasMerge.getMonth())
                .eq("departmentId", entryCasMerge.getDepartmentId())
                .select("max(serial) as serial");
        EntryCasMerge noticeMergeLastSerialData = entryCasMergeMapper
                .selectOne(queryNoticeMergeSerialQueryWrapper);
        noticeMergeSerial = !ObjectUtils.isEmpty(noticeMergeLastSerialData)
                && !ObjectUtils.isEmpty(noticeMergeLastSerialData.getSerial())
                ? noticeMergeLastSerialData.getSerial() + 1
                : 1;
        entryCasMerge.setMergeNo(
                String.format("JH%s%s%s", year, new DecimalFormat("00").format(month),
                        new DecimalFormat("000").format(noticeMergeSerial)));
        entryCasMerge.setSerial(noticeMergeSerial);
        entryCasMerge.setYear(year);
        entryCasMerge.setMonth(month);
        entryCasMerge.setCreateDate(createDate);

        entryCasMergeMapper.insert(entryCasMerge);
        // endregion
        return entryCasMerge;
    }

    @Override
    public boolean submitAuditForPlan(List<String> planDetailIds) {
        List<EntryCasPlanDetail> entryCasPlanDetails = entryCasPlanDetailMapper.selectBatchIds(planDetailIds);
        for (EntryCasPlanDetail entryCasPlanDetail : entryCasPlanDetails) {
            // 如果计划的状态为’待提交审核‘
            if (entryCasPlanDetail.getStatus().equals(PerformanceConstant.WAIT_SUBMIT_AUDIT)) {
                // 都提交至科长审核
                entryCasPlanDetail.setStatus(PerformanceConstant.WAIT_AUDIT_CHIEF);
                entryCasPlanDetailMapper.updateById(entryCasPlanDetail);
            } else {
                throw new ServiceException(String.format("不能提交状态为【%s】的履职计划", entryCasPlanDetail.getStatus()));
            }
        }
        return true;
    }

    @Override
    public boolean retreatForPlan(List<String> planDetailIds) {
        List<EntryCasPlanDetail> entryCasPlanDetails = entryCasPlanDetailMapper.selectBatchIds(planDetailIds);
        for (EntryCasPlanDetail entryCasPlanDetail : entryCasPlanDetails) {
            //校验方法
            if (!entryCasPlanDetail.getStatus().equals(PerformanceConstant.WAIT_AUDIT_CHIEF)) {
                throw new ServiceException(String.format("不能撤销状态为【%s】的履职计划", entryCasPlanDetail.getStatus()));
            } else {
                entryCasPlanDetail.setStatus(PerformanceConstant.WAIT_SUBMIT_AUDIT);
                entryCasPlanDetailMapper.updateById(entryCasPlanDetail);
            }
        }
        return true;
    }

    @Override
    public boolean affirmStore(List<String> planDetailIds, String event, List<String> auditNotes) {
        List<EntryCasPlanDetail> entryCasPlanDetails = entryCasPlanDetailMapper.selectBatchIds(planDetailIds);
        Map<String, EntryCasPlanDetail> keyByPlanDetailMap = IteratorTool.keyByPattern("id", entryCasPlanDetails);
        for (int i = 0; i < entryCasPlanDetails.size(); i++) {
            String planId = planDetailIds.get(i);
            String auditNote = null;
            if (!ObjectUtils.isEmpty(auditNotes)) {
                auditNote = auditNotes.get(i);
            }
            EntryCasPlanDetail entryCasPlanDetail = keyByPlanDetailMap.get(planId);
            // 审核通过设置状态为‘待填报总结’ 审核不通过设置状态为‘待提交审核’
            entryCasPlanDetail.setStatus("pass".equals(event) ? PerformanceConstant.WAIT_WRITE_REVIEW : PerformanceConstant.WAIT_SUBMIT_AUDIT);
            entryCasPlanDetail.setAuditNote(auditNote);
            entryCasPlanDetailMapper.updateById(entryCasPlanDetail);
        }
        return true;
    }


    @Override
    public boolean saveOneCasPlanDetail(EntryCasPlanDetail entryCasPlanDetail, HttpServletRequest request) {
        // 查询用户姓名
        Long userId = Long.valueOf(request.getParameter("userId"));
        User user = userMapper.selectById(userId);
        // 查询视图
        ViewDepartmentRoleUser viewDepartmentRoleUser = GetTool.getDepartmentRoleByUser(user);
        entryCasPlanDetail.setUserId(user.getId());
        entryCasPlanDetail.setCreatedAt(LocalDateTime.now());
        entryCasPlanDetail.setCreateId(user.getId());
        entryCasPlanDetail.setCreateDate(LocalDate.now());
        entryCasPlanDetail.setCreateName(user.getName());
        entryCasPlanDetail.setRoleName(viewDepartmentRoleUser.getRoleName());
        entryCasPlanDetail.setRoleId(viewDepartmentRoleUser.getRoleId());
        entryCasPlanDetail.setStatus(PerformanceConstant.WAIT_SUBMIT_AUDIT);
        entryCasPlanDetail.setApplyDate(LocalDate.now());
        String eventsRoleId = request.getParameter("eventsRoleId");
        String eventsFirstType = request.getParameter("eventsFirstType");
        entryCasPlanDetail.setEventsFirstType(eventsFirstType);


        if (!ObjectUtils.isEmpty(eventsRoleId)) {
            entryCasPlanDetail.setEventsRoleId(Long.valueOf(eventsRoleId));
        }
        // 如果事件类型为新增工作流，设置字段isNewEvent为是
        if (eventsFirstType.equals(PerformanceConstant.EVENT_FIRST_TYPE_NEW_EVENT)) {
            entryCasPlanDetail.setIsNewEvent(PerformanceConstant.YES);
        } else {
            entryCasPlanDetail.setIsNewEvent(PerformanceConstant.NO);
        }

        // 根据条件查询或生成bs_performance_employee_entry_cas_merge表数据
        LambdaQueryWrapper<EntryCasMerge> entryCasMergeLambdaQueryWrapper = new LambdaQueryWrapper<>();
        entryCasMergeLambdaQueryWrapper
                .eq(EntryCasMerge::getDepartmentId, viewDepartmentRoleUser.getDepartmentId())
                .eq(EntryCasMerge::getYear, LocalDate.now().getYear())
                .eq(EntryCasMerge::getMonth, LocalDate.now().getMonthValue())
        ;

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
            entryCasMerge.setStatus(PerformanceConstant.WAIT_WRITE_REVIEW);
            // serial
            entryCasMerge = storeNoticeMerge(LocalDate.now(), new HashMap<String, Object>(), entryCasMerge);
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

    @Override
    public Map<Long, Map<String, Object>> findWeChatNoticeMap() {
        Map<Long, Map<String, Object>> map = new HashMap<>(30);
        Map<String, Object> childMap;

        QueryWrapper<EventsRelationRole> wrapper = new QueryWrapper<>();
        wrapper.select("userId,COUNT(userId) as num")
                .lambda()
                // 今年
                .eq(EventsRelationRole::getYear, LocalDateTime.now().getYear())
                // 今月
                .eq(EventsRelationRole::getMonth, LocalDateTime.now().getMonth())
                // 状态为待导出填报计划
                .eq(EventsRelationRole::getStatus, PerformanceConstant.WAIT_PLAN)
                .groupBy(EventsRelationRole::getUserId);

        List<Map<String, Object>> eventsRelationRoles = eventsRelationRoleMapper.selectMaps(wrapper);
        if (ObjectUtils.isEmpty(eventsRelationRoles)) {
            return null;
        }
        for (Map<String, Object> eventsRelationRole : eventsRelationRoles) {
            User user = userMapper.selectById((Serializable) eventsRelationRole.get("userId"));
            childMap = new HashMap<>(50);
            childMap.put("userId", user.getId());
            childMap.put("userName", user.getName());
            childMap.put("weChat", user.getQyweixinUserId());
            childMap.put("num", eventsRelationRole.get("num"));
            map.put((Long) childMap.get("userId"), childMap);
        }
        return map;
    }

    @Override
    public boolean isExistExportData(User user) {
        LambdaQueryWrapper<EntryCasPlanDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EntryCasPlanDetail::getUserId, user.getId())
                .eq(EntryCasPlanDetail::getStatus, PerformanceConstant.WAIT_WRITE_REVIEW);
        List<EntryCasPlanDetail> selectList = entryCasPlanDetailMapper.selectList(wrapper);
        return !ObjectUtils.isEmpty(selectList);
    }

    @Override
    public boolean saveNewEve(EntryCasPlanDetail planDetail) {
        ViewDepartmentRoleUser departmentRoleByUser = GetTool.getDepartmentRoleByUser();
        planDetail.setCreatedAt(LocalDateTime.now());
        planDetail.setCreateId(departmentRoleByUser.getUserId());
        planDetail.setCreatedId(departmentRoleByUser.getUserId());
        planDetail.setCreateName(departmentRoleByUser.getUserName());
        planDetail.setCreatedName(departmentRoleByUser.getUserName());
        planDetail.setDepartmentId(departmentRoleByUser.getDepartmentId());
        planDetail.setDepartmentName(departmentRoleByUser.getDepartmentName());
        planDetail.setRoleId(departmentRoleByUser.getRoleId());
        planDetail.setRoleName(departmentRoleByUser.getRoleName());
        planDetail.setUserId(departmentRoleByUser.getUserId());
        planDetail.setUserName(departmentRoleByUser.getUserName());
        planDetail.setYear(planDetail.getApplyDate().getYear());
        planDetail.setMonth(planDetail.getApplyDate().getMonthValue());
        planDetail.setCreateDate(LocalDate.now());
        planDetail.setStatus(PerformanceConstant.WAIT_SUBMIT_AUDIT);
        planDetail.setEventsFirstType(PerformanceConstant.EVENT_FIRST_TYPE_NEW_EVENT);
        planDetail.setIsNewEvent(PerformanceConstant.YES);
        planDetail.setOffice(ObjectUtils.isEmpty(departmentRoleByUser.getOfficeName()) ? departmentRoleByUser.getDepartmentName() : departmentRoleByUser.getOfficeName());
        planDetail.setOfficeId(departmentRoleByUser.getTheDepartmentId());
        LambdaQueryWrapper<EntryCasMerge> entryCasMergeLambdaQueryWrapper = new LambdaQueryWrapper<>();
        entryCasMergeLambdaQueryWrapper
                .eq(EntryCasMerge::getDepartmentId, planDetail.getDepartmentId())
                .eq(EntryCasMerge::getYear, LocalDate.now().getYear())
                .eq(EntryCasMerge::getMonth, LocalDate.now().getMonthValue())
        ;

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
            entryCasMerge.setCreateId(departmentRoleByUser.getUserId());
            entryCasMerge.setCreateName(departmentRoleByUser.getUserName());
            entryCasMerge.setDepartmentName(departmentRoleByUser.getDepartmentName());
            entryCasMerge.setDepartmentId(departmentRoleByUser.getDepartmentId());
            entryCasMerge.setAuditName(null);
            entryCasMerge.setAuditId(null);
            entryCasMerge.setAuditDate(null);
            entryCasMerge.setRoleId(departmentRoleByUser.getRoleId());
            entryCasMerge.setRoleName(departmentRoleByUser.getRoleName());
            entryCasMerge.setUserId(departmentRoleByUser.getUserId());
            entryCasMerge.setUserName(departmentRoleByUser.getUserName());
            // mergeNo
            entryCasMerge.setApplyDate(LocalDate.now());
            entryCasMerge.setYear(LocalDate.now().getYear());
            entryCasMerge.setMonth(LocalDate.now().getMonthValue());
            entryCasMerge.setStatus(PerformanceConstant.WAIT_WRITE_REVIEW);
            // serial
            entryCasMerge = storeNoticeMerge(LocalDate.now(), new HashMap<>(), entryCasMerge);
        }

        // 添加计划表编号和序号
        planDetail.setMergeNo(entryCasMerge.getMergeNo());
        planDetail.setMergeId(entryCasMerge.getId());
        return baseMapper.insert(planDetail) > 0;
    }


}
