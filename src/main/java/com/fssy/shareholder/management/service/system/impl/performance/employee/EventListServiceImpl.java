/**
 * ------------------------修改日志---------------------------------
 * 修改人       修改日期         修改内容
 * 农浩         2022-10-11     新增无标准事件清单导入
 * 伍坚山       2022-10-11     新增事件清单评判标准管理
 */
package com.fssy.shareholder.management.service.system.impl.performance.employee;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.mapper.manage.department.DepartmentMapper;
import com.fssy.shareholder.management.mapper.system.performance.employee.EventListMapper;
import com.fssy.shareholder.management.pojo.manage.department.Department;
import com.fssy.shareholder.management.pojo.manage.user.User;
import com.fssy.shareholder.management.pojo.system.config.Attachment;
import com.fssy.shareholder.management.pojo.system.performance.employee.EventList;
import com.fssy.shareholder.management.service.common.SheetService;
import com.fssy.shareholder.management.service.system.performance.employee.EventListService;
import com.fssy.shareholder.management.tools.common.InstandTool;
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
import java.util.*;

/**
 * @author MI
 * @ClassName: EventListServiceImpl
 * @Description: 事件清单业务类接口实现类
 * @date 2022/10/8 10:33
 */
@Service
public class EventListServiceImpl implements EventListService {

    @Autowired
    private EventListMapper eventListMapper;

    @Autowired
    private SheetService sheetService;

    @Autowired
    private DepartmentMapper departmentMapper;

    @Override
    public Page<EventList> findDataListByParams(Map<String, Object> params) {
        QueryWrapper<EventList> queryWrapper = getQueryWrapper(params);
        Page<EventList> myPage = new Page<>((int) params.get("page"), (int) params.get("limit"));
        return eventListMapper.selectPage(myPage, queryWrapper);
    }

    private QueryWrapper<EventList> getQueryWrapper(Map<String, Object> params) {
        QueryWrapper<EventList> queryWrapper = Wrappers.query();
        if (params.containsKey("select")) {
            queryWrapper.select(InstandTool.objectToString(params.get("select")));
        }
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
            queryWrapper.like("eventsFirstType", params.get("eventsFirstType"));
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
        if (params.containsKey("departmentIdList")) {
            queryWrapper.in("departmentId", (List<String>) params.get("departmentIdList"));
        }
        if (params.containsKey("office")) {
            queryWrapper.like("office", params.get("office"));
        }
        if (params.containsKey("officeId")) {
            queryWrapper.eq("officeId", params.get("officeId"));
        }
        if (params.containsKey("statusWait")) {
            queryWrapper.eq("status", "待填报标准");
        }
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
    @Transactional
    public Map<String, Object> readEventListDataSource(Attachment attachment) {
        // 返回消息
        Map<String, Object> result = new HashMap<>();
        result.put("content", "");
        result.put("failed", false);
        // 读取附件
        sheetService.load(attachment.getPath(), attachment.getFilename()); // 根据路径和名称读取附件
        // 读取表单
        sheetService.readByName("事件清单评价表"); //根据表单名称获取该工作表单
        // 获取表单数据
        Sheet sheet = sheetService.getSheet();
        if (ObjectUtils.isEmpty(sheet)) {
            throw new ServiceException("表单【事件清单评价表】不存在，无法读取数据，请检查");
        }

        // 获取单价列表数据
        Row row;
        // 2022-06-01 从决策系统导出数据，存在最后几行为空白数据，导致报数据越界问题，这里的长度由表头长度控制
        short maxSize = sheet.getRow(0).getLastCellNum();//列数(表头长度)
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
            Cell cell = row.createCell(SheetService.columnToIndex("I"));// 每一行的结果信息上传到S列
            // 检查必填项
            String id = cells.get(SheetService.columnToIndex("A"));// 根据id去更新
            String eventsType = cells.get(SheetService.columnToIndex("B"));
            String jobName = cells.get(SheetService.columnToIndex("C"));
            String workEvents = cells.get(SheetService.columnToIndex("D"));
            String delowStandard = cells.get(SheetService.columnToIndex("E"));
            String middleStandard = cells.get(SheetService.columnToIndex("F"));
            String fineStandard = cells.get(SheetService.columnToIndex("G"));
            String excellentStandard = cells.get(SheetService.columnToIndex("H"));
            // 构建实体类
            EventList eventList = new EventList();
            User user = (User) SecurityUtils.getSubject().getPrincipal();
            eventList.setId(Long.valueOf(id));// 要更新的数据id
            eventList.setEventsType(eventsType);
            eventList.setJobName(jobName);
            eventList.setWorkEvents(workEvents);
            eventList.setDelowStandard(delowStandard);
            eventList.setMiddleStandard(middleStandard);
            eventList.setFineStandard(fineStandard);
            eventList.setExcellentStandard(excellentStandard);
            eventList.setStandardCreateUser(user.getName());
            eventList.setStandardCreateUserId(user.getId());
            eventList.setStandardCreateDate(new Date());
            eventList.setStandardAttachmentId(attachment.getId());
            eventList.setStatus(PerformanceConstant.EVENT_LIST_STATUS_FINAL);
            // 更新
            eventListMapper.updateById(eventList);
            cell.setCellValue("导入成功");// 写在upload目录下的excel表格
        }
        sheetService.write(attachment.getPath(), attachment.getFilename());// 写入excel表
        return result;
    }

    @Override
    public List<Map<String, Object>> findEventListMapDataByParams(Map<String, Object> params) {
        QueryWrapper<EventList> queryWrapper = getQueryWrapper(params);
        return eventListMapper.selectMaps(queryWrapper);
    }

    @Override
    @Transactional
    public Map<String, Object> readEventListWithoutStandardDataSource(Attachment attachment) {
        // 返回消息
        Map<String, Object> result = new HashMap<>();
        result.put("content", "");
        result.put("failed", false);
        //读取附件
        sheetService.load(attachment.getPath(), attachment.getFilename());//根据路径和名称读取附件
        //读取表单
        sheetService.readByName("Sheet1");//根据表单名称获取该工作表单
        //获取表单数据
        Sheet sheet = sheetService.getSheet();
        if (ObjectUtils.isEmpty(sheet)) {
            throw new ServiceException("表单【经理绩效表单】不存在，无法读取数据，请检查");
        }
        //处理导入日期
        Date importDate = attachment.getImportDate();
        //获取列表数据
        Row row;
        //实体类集合，用于后面的批量写入数据库
        // 2022-06-01 从决策系统导出数据，存在最后几行为空白数据，导致报数据越界问题，这里的长度由表头长度控制
        short maxSize = sheet.getRow(0).getLastCellNum();//列数(表头长度)
        // 循环总行数(不读表的标题，从第4行开始读)
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
            String eventsType = cells.get(SheetService.columnToIndex("F"));
            if (ObjectUtils.isEmpty(eventsType)) {
                setFailedContent(result, String.format("第%s行的事件类别是空的", j + 1));
                cell.setCellValue("事件类别是空的");
                continue;
            }
            String duration = cells.get(SheetService.columnToIndex("G"));
            if (ObjectUtils.isEmpty(duration)) {
                setFailedContent(result, String.format("第%s行的月工作标准时长（分）是空的", j + 1));
                cell.setCellValue("月工作标准时长（分）是空的");
                continue;
            }
            String level = cells.get(SheetService.columnToIndex("H"));
            if (ObjectUtils.isEmpty(level)) {
                setFailedContent(result, String.format("第%s行的评价等级是空的", j + 1));
                cell.setCellValue("评价等级是空的");
                continue;
            }
            String standardValue = cells.get(SheetService.columnToIndex("I"));
            if (ObjectUtils.isEmpty(standardValue)) {
                setFailedContent(result, String.format("第%s行的事件标准价值是空的", j + 1));
                cell.setCellValue("事件标准价值是空的");
                continue;
            }
            String delow = cells.get(SheetService.columnToIndex("J"));
            String middle = cells.get(SheetService.columnToIndex("K"));
            String fine = cells.get(SheetService.columnToIndex("L"));
            String excellent = cells.get(SheetService.columnToIndex("M"));

            String note = cells.get(SheetService.columnToIndex("N"));
            if (ObjectUtils.isEmpty(note)) {
                setFailedContent(result, String.format("第%s行的备注是空的", j + 1));
                cell.setCellValue("备注是空的");
                continue;
            }
            String departmentName = cells.get(SheetService.columnToIndex("O"));
            if (ObjectUtils.isEmpty(departmentName)) {
                setFailedContent(result, String.format("第%s行的部门名称是空的", j + 1));
                cell.setCellValue("部门名称是空的");
                continue;
            }
            String office = cells.get(SheetService.columnToIndex("P"));
            if (ObjectUtils.isEmpty(office)) {
                setFailedContent(result, String.format("第%s行的科室是空的", j + 1));
                cell.setCellValue("科室是空的");
                continue;
            }


            //构建实体类
            EventList eventList = new EventList();
            User user = (User) SecurityUtils.getSubject().getPrincipal();
            eventList.setStrategyFunctions(strategyFunctions);
            eventList.setSupportFunctions(supportFunctions);
            eventList.setJobName(jobName);
            eventList.setWorkEvents(workEvents);
            eventList.setWorkOutput(workOutput);
            eventList.setEventsType(eventsType);

            Date date = new Date();
            String format = new SimpleDateFormat("yyyy-MM-dd").format(date);
            String year = Arrays.asList(format.split("-")).get(0);
            eventList.setYear(InstandTool.stringToInteger(year));

            eventList.setDuration(new BigDecimal(duration));
            eventList.setLevel(level);
            eventList.setStandardValue(new BigDecimal(standardValue));
            eventList.setNote(note);
            eventList.setDepartmentName(departmentName);
            eventList.setDelow(new BigDecimal(delow));
            eventList.setMiddle(new BigDecimal(middle));
            eventList.setFine(new BigDecimal(fine));
            eventList.setExcellent(new BigDecimal(excellent));
            String month = Arrays.asList(format.split("-")).get(1);
            eventList.setMonth(Integer.valueOf(month));
            eventList.setCreateDate(new Date());
            eventList.setActiveDate(new Date());
            eventList.setPerformanceForm("拓展事件绩效");
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

            // 查询 部门 表主键
            // 创建条件构造器
            QueryWrapper<Department> departmentQueryWrapper = new QueryWrapper<>();
            // 设置条件
            departmentQueryWrapper.eq("name", eventList.getDepartmentName());
            // 查询
            List<Department> departments = departmentMapper.selectList(departmentQueryWrapper);
            if (ObjectUtils.isEmpty(departments)) {
                setFailedContent(result, String.format("第%s行的【%s】未在系统维护", j + 1, departmentName));
                cell.setCellValue(String.format("【%s】未在系统维护", departmentName));
                continue;
            }
            Department department = departments.get(0);
            eventList.setDepartmentId(department.getId());

            eventList.setOffice(office);
            eventList.setStatus(PerformanceConstant.EVENT_LIST_STATUS_WAIT);

            //eventLists.add(eventList);
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
    public boolean updateEventList(EventList eventList)
    {
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
}
