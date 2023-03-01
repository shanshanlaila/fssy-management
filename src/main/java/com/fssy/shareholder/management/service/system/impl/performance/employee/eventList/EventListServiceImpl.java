/**
 * ------------------------修改日志---------------------------------
 * 修改人       修改日期         修改内容
 * 农浩         2022-10-11     新增无标准事件清单导入
 * 伍坚山       2022-10-11     新增事件清单评判标准管理
 */
package com.fssy.shareholder.management.service.system.impl.performance.employee.eventList;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fssy.shareholder.management.mapper.manage.department.DepartmentMapper;
import com.fssy.shareholder.management.mapper.manage.department.ViewDepartmentRoleUserMapper;
import com.fssy.shareholder.management.mapper.manage.role.RoleMapper;
import com.fssy.shareholder.management.mapper.manage.user.UserMapper;
import com.fssy.shareholder.management.mapper.system.performance.employee.EntryCasPlanDetailMapper;
import com.fssy.shareholder.management.mapper.system.performance.employee.EventListMapper;
import com.fssy.shareholder.management.pojo.manage.department.Department;
import com.fssy.shareholder.management.pojo.manage.department.ViewDepartmentRoleUser;
import com.fssy.shareholder.management.pojo.manage.user.User;
import com.fssy.shareholder.management.pojo.system.config.Attachment;
import com.fssy.shareholder.management.pojo.system.performance.employee.EntryCasPlanDetail;
import com.fssy.shareholder.management.pojo.system.performance.employee.EventList;
import com.fssy.shareholder.management.service.common.SheetService;
import com.fssy.shareholder.management.service.system.performance.employee.EventListService;
import com.fssy.shareholder.management.tools.common.GetTool;
import com.fssy.shareholder.management.tools.common.InstandTool;
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
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author MI
 * @ClassName: EventListServiceImpl
 * @Description: 事件清单业务类接口实现类
 * @date 2022/10/8 10:33
 */
@Service
public class EventListServiceImpl extends ServiceImpl<EventListMapper, EventList> implements EventListService {

    @Autowired
    private EventListMapper eventListMapper;

    @Autowired
    private SheetService sheetService;

    @Autowired
    private DepartmentMapper departmentMapper;

    @Autowired
    private ViewDepartmentRoleUserMapper viewDepartmentRoleUserMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private EntryCasPlanDetailMapper entryCasPlanDetailMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public Page<EventList> findDataListByParams(Map<String, Object> params) {
        QueryWrapper<EventList> queryWrapper = getQueryWrapper(params).orderByDesc("id");
        Page<EventList> myPage = new Page<>((int) params.get("page"), (int) params.get("limit"));
        return eventListMapper.selectPage(myPage, queryWrapper);
    }

    private QueryWrapper<EventList> getQueryWrapper(Map<String, Object> params) {
        QueryWrapper<EventList> queryWrapper = Wrappers.query();
        if (params.containsKey("select")) {
            queryWrapper.select(InstandTool.objectToString(params.get("select")));
        }
        // 根据事务类型进行导出排序
        queryWrapper.orderByAsc(params.containsKey("sort"), "sort");
        if (params.containsKey("id")) {
            queryWrapper.eq("id", params.get("id"));
        }
        if (params.containsKey("departmentName")) {
            queryWrapper.like("departmentName", params.get("departmentName"));
        }
        if (params.containsKey("strategyFunctions")) {
            queryWrapper.like("strategyFunctions", params.get("strategyFunctions"));
        }
        if (params.containsKey("supportFunctions")) {
            queryWrapper.like("supportFunctions", params.get("supportFunctions"));
        }
        if (params.containsKey("jobName")) {
            queryWrapper.like("jobName", params.get("jobName"));
        }
        if (params.containsKey("workEvents")) {
            queryWrapper.like("workEvents", params.get("workEvents"));
        }
        if (params.containsKey("eventsType")) {
            queryWrapper.like("eventsType", params.get("eventsType"));
        }
        if (params.containsKey("workOutput")) {
            queryWrapper.like("workOutput", params.get("workOutput"));
        }
        if (params.containsKey("note")) {
            queryWrapper.like("note", params.get("note"));
        }
        if (params.containsKey("year")) {
            queryWrapper.eq("year", params.get("year"));
        }
        if (params.containsKey("eventsFirstType")) {
            queryWrapper.eq("eventsFirstType", params.get("eventsFirstType"));
        }
        if (params.containsKey("level")) {
            queryWrapper.like("level", params.get("level"));
        }
        if (params.containsKey("month")) {
            queryWrapper.eq("month", params.get("month"));
        }
        if (params.containsKey("createDate")) {
            queryWrapper.ge("createDate", params.get("createDate"));
        }
        if (params.containsKey("activeDate")) {
            queryWrapper.ge("activeDate", params.get("activeDate"));
        }
        if (params.containsKey("departmentIds")) {
            String departmentIdsStr = (String) params.get("departmentIds");
            List<String> departmentIdList = Arrays.asList(departmentIdsStr.split(","));
            queryWrapper.in("departmentId", departmentIdList);
        }
        if (params.containsKey("performanceForm")) {
            queryWrapper.like("performanceForm", params.get("performanceForm"));
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
        if (params.containsKey("listCreateUser")) {
            queryWrapper.like("listCreateUser", params.get("listCreateUser"));
        }
        if (params.containsKey("listCreateUserId")) {
            queryWrapper.eq("listCreateUserId", params.get("listCreateUserId"));
        }
        if (params.containsKey("valueCreateUser")) {
            queryWrapper.like("valueCreateUser", params.get("valueCreateUser"));
        }
        if (params.containsKey("valueCreateUserId")) {
            queryWrapper.eq("valueCreateUserId", params.get("valueCreateUserId"));
        }
        if (params.containsKey("valueCreateDate")) {
            queryWrapper.like("valueCreateDate", params.get("valueCreateDate"));
        }
        // 状态：完结
        if (params.containsKey("status")) {
            queryWrapper.eq("status", params.get("status"));
        }
        if (params.containsKey("listAttachmentId")) {
            queryWrapper.eq("listAttachmentId", params.get("listAttachmentId"));
        }
        if (params.containsKey("valueAttachmentId")) {
            queryWrapper.eq("valueAttachmentId", params.get("valueAttachmentId"));
        }
        if (params.containsKey("standardCreateUser")) {
            queryWrapper.like("standardCreateUser", params.get("standardCreateUser"));
        }
        if (params.containsKey("standardCreateUserId")) {
            queryWrapper.eq("standardCreateUserId", params.get("standardCreateUserId"));
        }
        if (params.containsKey("standardCreateDate")) {
            queryWrapper.like("standardCreateDate", params.get("standardCreateDate"));
        }
        if (params.containsKey("standardAttachmentId")) {
            queryWrapper.eq("standardAttachmentId", params.get("standardAttachmentId"));
        }
        if (params.containsKey("office")) {
            queryWrapper.like("office", params.get("office"));
        }
        if (params.containsKey("officeId")) {
            queryWrapper.eq("officeId", params.get("officeId"));
        }
        if (params.containsKey("statusCancel")) {
            queryWrapper.ne("status", PerformanceConstant.CANCEL);
        }
        // 编制日期起
        if (params.containsKey("createDateStart")) {
            queryWrapper.ge("createDate", params.get("createDateStart"));
        }
        // 编制日期止
        if (params.containsKey("createDateEnd")) {
            queryWrapper.le("createDate", params.get("createDateEnd"));
        }
        // 生效日期起
        if (params.containsKey("activeDateStart")) {
            queryWrapper.ge("activeDate", params.get("activeDateStart"));
        }
        // 生效日期止
        if (params.containsKey("activeDateEnd")) {
            queryWrapper.le("activeDate", params.get("activeDateEnd"));
        }
        // 事件清单创建人
        if (params.containsKey("listCreateUserIds")) {
            String listCreateUserIdsStr = (String) params.get("listCreateUserIds");
            List<String> listCreateUserIds = Arrays.asList(listCreateUserIdsStr.split(","));
            // 存储名字集合
            ArrayList<String> names = new ArrayList<>();
            for (User user : userMapper.selectBatchIds(listCreateUserIds)) {
                names.add(user.getName());
            }
            queryWrapper.in("listCreateUser", names);
        }
        // 科室
        queryWrapper.in(params.containsKey("officeIds"), "officeId", params.get("officeIds"));
        return queryWrapper;
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
    public List<Map<String, Object>> findEventListMapDataByParams(Map<String, Object> params) {
        QueryWrapper<EventList> queryWrapper = getQueryWrapper(params);
        return eventListMapper.selectMaps(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> readEventListWithoutStandardDataSource(Attachment attachment) {
        // 导入基础事件
        // 返回消息
        Map<String, Object> result = new HashMap<>();
        result.put("content", "");
        result.put("failed", false);
        StringBuffer sb = new StringBuffer();// 用于数据校验
        //读取附件
        sheetService.load(attachment.getPath(), attachment.getFilename());//根据路径和名称读取附件
        //读取表单
        sheetService.readByName("事件清单");//根据表单名称获取该工作表单
        //获取表单数据
        Sheet sheet = sheetService.getSheet();
        if (ObjectUtils.isEmpty(sheet)) {
            throw new ServiceException("表单【事件清单】不存在，无法读取数据，请检查");
        }
        //处理导入日期
        Date importDate = attachment.getImportDate();
        //获取列表数据
        Row row;
        //实体类集合，用于后面的批量写入数据库
        short maxSize = sheet.getRow(0).getLastCellNum();//列数(表头长度)
        // 循环总行数(不读表的标题，从第2行开始读)
        for (int j = 1; j <= sheet.getLastRowNum(); j++) {
            // getPhysicalNumberOfRows()此方法不会将空白行计入行数
            //if (j==3)continue;// 跳过第三行，因为2，3行合并为一行
            List<String> cells = new ArrayList<>();//每一行的数据用一个list接收
            row = sheet.getRow(j);//获取第j行
            // 获取一行中有多少列 Row：行，cell：列
            // short lastCellNum = row.getLastCellNum();
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
                //Cannot get a STRING value from a NUMERIC cell 无法从单元格获取数值
                Cell cell = row.getCell(k);
                String res = sheetService.getValue(cell).trim();//获取单元格的值
                cells.add(res);// 将单元格的值写入行
            }
            // 导入结果写入列
            Cell cell = row.createCell(SheetService.columnToIndex("R"));//报错信息上传到excel D列（暂未实现）
            String strategyFunctions = cells.get(SheetService.columnToIndex("A"));
            //检查必填项
            if (ObjectUtils.isEmpty(strategyFunctions)) {
                setFailedContent(result, String.format("第%s行的战略职能是空的", j + 1));
                cell.setCellValue("战略职能是空的");
                continue;
            }
            String supportFunctions = cells.get(SheetService.columnToIndex("B"));
            if (ObjectUtils.isEmpty(supportFunctions)) {
                setFailedContent(result, String.format("第%s行的支持职能是空的", j + 1));
                cell.setCellValue("支持职能是空的");
                continue;
            }

            String jobName = cells.get(SheetService.columnToIndex("C"));
            if (ObjectUtils.isEmpty(jobName)) {
                setFailedContent(result, String.format("第%s行的工作职责是空的", j + 1));
                cell.setCellValue("工作职责是空的");
                continue;
            }
            String workEvents = cells.get(SheetService.columnToIndex("D"));
            if (ObjectUtils.isEmpty(workEvents)) {
                setFailedContent(result, String.format("第%s行的流程（工作事件）是空的", j + 1));
                cell.setCellValue("流程（工作事件)是空的");
                continue;
            }
            String workOutput = cells.get(SheetService.columnToIndex("E"));
            if (ObjectUtils.isEmpty(workOutput)) {
                setFailedContent(result, String.format("第%s行的表单（输出内容）是空的", j + 1));
                cell.setCellValue("表单（输出内容）是空的");
                continue;
            }
            String eventsFirstType = cells.get(SheetService.columnToIndex("F"));
            if (ObjectUtils.isEmpty(eventsFirstType)) {
                setFailedContent(result, String.format("第%s行的事件类型是空的", j + 1));
                cell.setCellValue("事件类型是空的");
                continue;
            }
            //String duration = cells.get(SheetService.columnToIndex("G"));
            String standardValue = cells.get(SheetService.columnToIndex("H"));
            if (ObjectUtils.isEmpty(standardValue)) {
                setFailedContent(result, String.format("第%s行的事件标准价值是空的", j + 1));
                cell.setCellValue("事件标准价值是空的");
                continue;
            }

            String note = cells.get(SheetService.columnToIndex("I"));
            String departmentName = cells.get(SheetService.columnToIndex("J"));
            if (ObjectUtils.isEmpty(departmentName)) {
                setFailedContent(result, String.format("第%s行的部门名称是空的", j + 1));
                cell.setCellValue("部门名称是空的");
                continue;
            }
            // 科室
            String office = cells.get(SheetService.columnToIndex("K"));
            if (ObjectUtils.isEmpty(office)) {
                setFailedContent(result, String.format("第%s行的科室名称是空的", j + 1));
                cell.setCellValue("科室名称是空的");
                continue;
            }
            // 数据校验
            if (!(eventsFirstType.equals(PerformanceConstant.EVENT_FIRST_TYPE_TRANSACTION)
                    || eventsFirstType.equals(PerformanceConstant.EVENT_FIRST_TYPE_NOT_TRANSACTION))) {
                setFailedContent(result, String.format("第%s行的事件类型填写有误", j + 1));
                cell.setCellValue("表中事件类型填写有误");
                continue;
            }
            LambdaQueryWrapper<Department> departmentWrapper = new LambdaQueryWrapper<>();
            departmentWrapper.eq(Department::getName, departmentName);
            List<Department> departmentList = departmentMapper.selectList(departmentWrapper);
            if (ObjectUtils.isEmpty(departmentList)) {
                setFailedContent(result, String.format("第%s行的部门名称填写有误", j + 1));
                cell.setCellValue("部门名称填写有误");
                continue;
            }
            // 科室和部门相对应
            if (!office.equals(departmentName)) {
                LambdaQueryWrapper<Department> officeWrapper = new LambdaQueryWrapper<>();
                officeWrapper.eq(Department::getName, office);
                List<Department> officeList = departmentMapper.selectList(officeWrapper);
                if (!ObjectUtils.isEmpty(officeList)) {
                    if (!departmentList.get(0).getDepartmentId().equals(officeList.get(0).getDepartmentId())) {
                        setFailedContent(result, String.format("第%s行的科室和部门不相互对应", j + 1));
                        cell.setCellValue("科室和部门不相互对应");
                        continue;
                    }
                }
            }

            //构建实体类
            EventList eventList = new EventList();
            User user = (User) SecurityUtils.getSubject().getPrincipal();
            eventList.setStrategyFunctions(strategyFunctions);
            eventList.setSupportFunctions(supportFunctions);
            eventList.setJobName(jobName);
            eventList.setWorkEvents(workEvents);
            eventList.setWorkOutput(workOutput);
            eventList.setEventsFirstType(eventsFirstType);
            // 如果是事务类sort=1,非事务类sort=2
            switch (eventsFirstType) {
                case PerformanceConstant.EVENT_FIRST_TYPE_TRANSACTION:
                    eventList.setSort(1);
                    break;
                case PerformanceConstant.EVENT_FIRST_TYPE_NOT_TRANSACTION:
                    eventList.setSort(2);
                    break;
            }
            Date date = new Date();
            String format = new SimpleDateFormat("yyyy-MM-dd").format(date);
            String year = Arrays.asList(format.split("-")).get(0);
            eventList.setYear(InstandTool.stringToInteger(year));

            //eventList.setDuration(new BigDecimal(duration));
            eventList.setStandardValue(new BigDecimal(standardValue));
            eventList.setNote(note);
            eventList.setDepartmentName(departmentName);
            eventList.setDepartmentId(departmentList.get(0).getDepartmentId());
            String month = Arrays.asList(format.split("-")).get(1);
            eventList.setMonth(Integer.valueOf(month));
            eventList.setCreateDate(new Date());
            eventList.setActiveDate(new Date());
            eventList.setListCreateUser(user.getName());
            eventList.setListcreateUserId(user.getId());
            eventList.setValueCreateUser(user.getName());
            eventList.setValueCreateUserId(user.getId());
            eventList.setValueCreateDate(new Date());
            eventList.setListAttachmentId(attachment.getId());
            eventList.setValueAttachmentId(attachment.getId());
            eventList.setStandardCreateUser(user.getCreatedName());
            eventList.setStandardCreateUserId(user.getId());
            eventList.setStandardCreateDate(new Date());
            eventList.setStandardAttachmentId(attachment.getId());
            if (!ObjectUtils.isEmpty(office)) {
                eventList.setOffice(office);
                LambdaQueryWrapper<ViewDepartmentRoleUser> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(ViewDepartmentRoleUser::getTheDepartmentName, office);
                List<ViewDepartmentRoleUser> officeObj = viewDepartmentRoleUserMapper.selectList(wrapper);
                if (!ObjectUtils.isEmpty(officeObj)) {
                    eventList.setOfficeId(officeObj.get(0).getTheDepartmentId());
                }
            }
            LambdaQueryWrapper<ViewDepartmentRoleUser> viewDepartmentWrapper = new LambdaQueryWrapper<>();
            viewDepartmentWrapper.eq(ViewDepartmentRoleUser::getDepartmentName, departmentName);
            List<ViewDepartmentRoleUser> viewDepartmentRoleUsers = viewDepartmentRoleUserMapper.selectList(viewDepartmentWrapper);
            if (ObjectUtils.isEmpty(viewDepartmentRoleUsers)) {
                StringTool.setMsg(sb, String.format("第【%s】行的【%s】的部门名称在系统中未查找到，不能导入", j + 1, departmentName));
                cell.setCellValue(String.format("行数为【%s】的部门名称未查到，不能导入", j + 1));
                continue;
            }
            // 将状态设置为待导出维护岗位配比
            eventList.setStatus(PerformanceConstant.WAIT_RELATION_ROLE);
            eventListMapper.insert(eventList);
            cell.setCellValue("导入成功");
        }

        sheetService.write(attachment.getPath(), attachment.getFilename());//写入excel表
        return result;
    }

    @Override
    public Page<EventList> findEventListDataPerPageByParams(Map<String, Object> params) {
        QueryWrapper<EventList> queryWrapper = getQueryWrapper(params);
        int limit = (int) params.get("limit");
        int page = (int) params.get("page");
        Page<EventList> myPage = new Page<>(page, limit);
        return eventListMapper.selectPage(myPage, queryWrapper);
    }

    @Override
    public boolean updateEventList(EventList eventList) {
        int result = eventListMapper.updateById(eventList);
        return result > 0;
    }

    @Override
    public boolean deleteEventListById(int id) {
        int result = eventListMapper.deleteById(id);
        return result > 0;
    }

    @Override
    public EventList getById(Long id) {
        return eventListMapper.selectById(id);
    }

    @Override
    public boolean changeStatus(Integer id) {
        UpdateWrapper<EventList> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", id).set("status", PerformanceConstant.CANCEL);
        int update = eventListMapper.update(null, updateWrapper);
        return update == 1;
    }

    @Override
    public boolean insertEventList(EventList eventList) {
        LambdaQueryWrapper<Department> departmentLambdaQueryWrapper = new LambdaQueryWrapper<>();
        departmentLambdaQueryWrapper.eq(Department::getDepartmentId, eventList.getDepartmentId());
        List<Department> departments1 = departmentMapper.selectList(departmentLambdaQueryWrapper);
        if (ObjectUtils.isEmpty(departments1)) {
            throw new ServiceException("不存在部门");
        }
        Department department = departmentMapper.selectById(departments1.get(0).getDepartmentId());
        eventList.setDepartmentName(department.getDepartmentName());
        LambdaQueryWrapper<Department> departmentLambdaQueryWrapper1 = new LambdaQueryWrapper<>();
        departmentLambdaQueryWrapper1.eq(Department::getOfficeName, eventList.getOffice());
        List<Department> departments = departmentMapper.selectList(departmentLambdaQueryWrapper1);
        if (ObjectUtils.isEmpty(departments)) {
            throw new ServiceException("不存在科室");
        }
        eventList.setOfficeId(departments.get(0).getOfficeId());
        User user = GetTool.getUser();
        eventList.setListCreateUser(user.getName());
        eventList.setListcreateUserId(user.getId());
        eventList.setValueCreateUser(user.getName());
        eventList.setValueCreateUserId(user.getId());
        eventList.setValueCreateDate(new Date());
        Date date = new Date();
        String format = new SimpleDateFormat("yyyy-MM-dd").format(date);
        String year = Arrays.asList(format.split("-")).get(0);
        eventList.setYear(InstandTool.stringToInteger(year));
        String month = Arrays.asList(format.split("-")).get(1);
        eventList.setMonth(Integer.valueOf(month));
        eventList.setCreateDate(new Date());
        eventList.setActiveDate(new Date());
        eventList.setStatus(PerformanceConstant.FINAL);// 不需要填报事件标准，直接完结
        eventListMapper.insert(eventList);//写入数据库
        eventListMapper.updateById(eventList);//更新页面
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insertEventByPlan(EventList eventList, Long id) {
        EntryCasPlanDetail planDetail = entryCasPlanDetailMapper.selectById(id);
        eventList.setStatus(PerformanceConstant.FINAL);
        eventList.setYear(LocalDate.now().getYear());
        eventList.setMonth(LocalDate.now().getMonthValue());
        eventList.setCreatedAt(LocalDateTime.now());
        eventList.setCreateDate(new Date());
        eventList.setActiveDate(new Date());
        User user = GetTool.getUser();
        eventList.setListCreateUser(user.getName());
        eventList.setListcreateUserId(user.getId());
        eventList.setValueCreateUser(user.getName());
        eventList.setValueCreateUserId(user.getId());
        eventList.setValueCreateDate(new Date());
        Department department = departmentMapper.selectById(planDetail.getDepartmentId());
        if (ObjectUtils.isEmpty(department)) {
            throw new ServiceException("不存在部门");
        }
        eventList.setDepartmentId(department.getDepartmentId());
        // 新增event
        int result = eventListMapper.insert(eventList);
        planDetail.setEventsId(eventList.getId());
        planDetail.setStatus(PerformanceConstant.WAIT_WRITE_REVIEW);
        planDetail.setNewStatus(PerformanceConstant.FINAL);
        // 更新plan
        entryCasPlanDetailMapper.updateById(planDetail);
        return result > 0;
    }

    @Override
    public boolean isExistData() {
        ViewDepartmentRoleUser departmentRoleByUser = GetTool.getDepartmentRoleByUser();
        LambdaQueryWrapper<EventList> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EventList::getStatus, PerformanceConstant.WAIT_RELATION_ROLE)
                .eq(EventList::getOfficeId, departmentRoleByUser.getTheDepartmentId());
        List<EventList> selectList = eventListMapper.selectList(wrapper);
        return !ObjectUtils.isEmpty(selectList);
    }
}
