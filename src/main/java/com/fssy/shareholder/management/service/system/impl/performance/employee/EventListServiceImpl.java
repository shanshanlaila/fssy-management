/*
 * @Title: fssy-management
 * @Description: TODO
 * @author MI
 * @date 2022/10/8 10:33
 * @version
 */
package com.fssy.shareholder.management.service.system.impl.performance.employee;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.mapper.system.performance.employee.EventListMapper;
import com.fssy.shareholder.management.pojo.system.config.Attachment;
import com.fssy.shareholder.management.pojo.system.performance.employee.EventList;
import com.fssy.shareholder.management.service.common.SheetService;
import com.fssy.shareholder.management.service.system.performance.employee.EventListService;
import com.fssy.shareholder.management.tools.common.InstandTool;
import com.fssy.shareholder.management.tools.exception.ServiceException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        if (params.containsKey("status")) {
            queryWrapper.like("status", params.get("status"));
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
            Cell cell = row.createCell(SheetService.columnToIndex("I"));// 报错信息上传到S列
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
            eventList.setId(Long.valueOf(id));// 要更新的数据id
            eventList.setEventsType(eventsType);
            eventList.setJobName(jobName);
            eventList.setWorkEvents(workEvents);
            eventList.setDelowStandard(delowStandard);
            eventList.setMiddleStandard(middleStandard);
            eventList.setFineStandard(fineStandard);
            eventList.setExcellentStandard(excellentStandard);
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
}
