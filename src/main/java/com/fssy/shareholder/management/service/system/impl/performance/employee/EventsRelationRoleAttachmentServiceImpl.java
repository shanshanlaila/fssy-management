/**
 * ------------------------修改日志---------------------------------
 * 修改人			修改日期			修改内容
 */
package com.fssy.shareholder.management.service.system.impl.performance.employee;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.Map.Entry;

import com.fssy.shareholder.management.service.system.hr.performance.employee.EventsRelationRoleService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.fssy.shareholder.management.mapper.manage.department.DepartmentMapper;
import com.fssy.shareholder.management.mapper.manage.role.RoleMapper;
import com.fssy.shareholder.management.mapper.manage.user.UserMapper;
import com.fssy.shareholder.management.mapper.system.hr.performance.employee.EventListMapper;
import com.fssy.shareholder.management.mapper.system.hr.performance.employee.EventsRelationRoleMapper;
import com.fssy.shareholder.management.pojo.manage.department.Department;
import com.fssy.shareholder.management.pojo.manage.role.Role;
import com.fssy.shareholder.management.pojo.manage.user.User;
import com.fssy.shareholder.management.pojo.system.config.Attachment;
import com.fssy.shareholder.management.pojo.system.hr.performance.employee.EventList;
import com.fssy.shareholder.management.pojo.system.hr.performance.employee.EventsRelationRole;
import com.fssy.shareholder.management.service.common.SheetService;
import com.fssy.shareholder.management.service.system.hr.performance.employee.EventsRelationRoleAttachmentService;
import com.fssy.shareholder.management.tools.common.DateTool;
import com.fssy.shareholder.management.tools.common.InstandTool;
import com.fssy.shareholder.management.tools.common.StringTool;
import com.fssy.shareholder.management.tools.constant.CommonConstant;
import com.fssy.shareholder.management.tools.exception.ServiceException;

/**
 * @author Solomon
 * @Title: EventsRelationRoleAttachmentServiceImpl.java
 * @Description: 事件清单岗位关系附件功能业务实现类
 * @date 2022年10月12日 上午12:42:48
 */
@Service
public class EventsRelationRoleAttachmentServiceImpl implements EventsRelationRoleAttachmentService {
    /**
     * 事件清单岗位关系业务实现类
     */
    @Autowired
    private EventsRelationRoleMapper eventsRelationRoleMapper;

    /**
     * excel操作业务类
     */
    @Autowired
    private SheetService sheetService;

    /**
     * 组织结构数据访问实现类
     */
    @Autowired
    private DepartmentMapper departmentMapper;

    /**
     * 角色数据访问层实现类
     */
    @Autowired
    private RoleMapper roleMapper;

    /**
     * 事件清单数据访问实现类
     */
    @Autowired
    private EventListMapper eventListMapper;

    /**
     * 用户数据访问层实现类
     */
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private EventsRelationRoleService eventsRelationRoleService;

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
    public Map<String, Object> readAttachmentToData(Attachment attachment) {
        // 返回消息
        Map<String, Object> result = new HashMap<>();
        result.put("content", "");
        StringBuffer sb = new StringBuffer();
        result.put("failed", false);
        // 读取附件
        sheetService.load(attachment.getPath(), attachment.getFilename()); // 根据路径和名称读取附件

        try {
            // 读取表单
            sheetService.readByName("事件分配岗位表");
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("表单【事件分配岗位表】不存在于EXCEL中");
        }

        // 获取表单数据
        Sheet sheet = sheetService.getSheet();
        if (ObjectUtils.isEmpty(sheet)) {
            throw new ServiceException("表单【事件分配岗位表】不存在，无法读取数据，请检查");
        }

        // 获取单价列表数据
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        Row row;
        short maxSize = sheet.getRow(0).getLastCellNum();// 列数(表头长度)
        // 表格数据
        List<List<String>> temps = new ArrayList<>();
        // 成功导入的数据数量
        int successNum = 0;
        // 检查事件岗位比重map
        Map<String, BigDecimal> checkProportionMap = new HashMap<>();
        // 需要更新基础事件完成岗位配比标识的集合
        List<Long> updatedEventsIds = new ArrayList<>();
        // 用来判断相同基础事件的生效日期可以相同，但数据库中不能存在的map
        Map<Long, LocalDate> eventIdWithActivityMap = new HashMap<>();
        // 存放relationRole的集合
        List<EventsRelationRole> relationRoleList = new ArrayList<>();
        // 循环总行数(不读表头，从第2行开始读，索引从0开始，所以j=1)
        for (int j = 1; j <= sheet.getLastRowNum(); j++) {// getPhysicalNumberOfRows()此方法不会将空白行计入行数
            List<String> temp = new ArrayList<>();
            row = sheet.getRow(j);// 获取第j行
            // 获取一行中有多少列 Row：行，cell：列
            // 循环row行中的每一个单元格
            for (int k = 0; k < maxSize; k++) {
                // 如果这单元格为空，就写入空
                if (row.getCell(k) == null) {
                    temp.add("");
                    continue;
                }
                // Cannot get a STRING value from a NUMERIC cell 无法从单元格获取数值
                Cell cell = row.getCell(k);
                String res = sheetService.getValue(cell).trim();// 获取单元格的值
                temp.add(res);// 将单元格的值写入行
            }
            temps.add(temp);
            // 导入结果写入列
            Cell cell = row.createCell(SheetService.columnToIndex("Q"));// 每一行的结果信息上传到O列

            // region 导入数据校验
            Long eventsId = null;
            try {
                eventsId = InstandTool.stringToLong(temp.get(SheetService.columnToIndex("A")));
            } catch (Exception e) {
                StringTool.setMsg(sb, String.format("第【%s】行事件清单序号格式不正确，必须为【数值】", j + 1));
                cell.setCellValue("事件清单序号格式不正确，必须为【数值】");
                continue;
            }
            if (ObjectUtils.isEmpty(eventsId)) {
                StringTool.setMsg(sb, String.format("第【%s】行事件清单序号不能为空", j + 1));
                cell.setCellValue("事件清单序号不能为空");
                continue;
            }
            String departmentName = temp.get(SheetService.columnToIndex("E"));
            if (ObjectUtils.isEmpty(departmentName)) {
                StringTool.setMsg(sb, String.format("第【%s】行部门名称不能为空", j + 1));
                cell.setCellValue("部门名称不能为空");
                continue;
            }
            String roleName = temp.get(SheetService.columnToIndex("F"));
            if (ObjectUtils.isEmpty(roleName)) {
                StringTool.setMsg(sb, String.format("第【%s】行岗位名称不能为空", j + 1));
                cell.setCellValue("岗位名称不能为空");
                continue;
            }
            String isMainOrNext = temp.get(SheetService.columnToIndex("H"));
            if (ObjectUtils.isEmpty(isMainOrNext)) {
                StringTool.setMsg(sb, String.format("第【%s】行是否主担不能为空", j + 1));
                cell.setCellValue("是否主担不能为空");
                continue;
            }
            String userName = temp.get(SheetService.columnToIndex("I"));
            if (ObjectUtils.isEmpty(userName)) {
                StringTool.setMsg(sb, String.format("第【%s】行职员名称不能为空", j + 1));
                cell.setCellValue("职员名称不能为空");
                continue;
            }
            BigDecimal proportion = null;
            try {
                proportion = new BigDecimal(temp.get(SheetService.columnToIndex("G")));
            } catch (Exception e) {
                StringTool.setMsg(sb, String.format("第【%s】行占比格式不正确，必须为【百分比】", j + 1));
                cell.setCellValue("占比格式不正确，必须为【百分比】");
                continue;
            }
            if (ObjectUtils.isEmpty(proportion)) {
                StringTool.setMsg(sb, String.format("第【%s】行占比不能为空", j + 1));
                cell.setCellValue("占比不能为空");
                continue;
            }
            BigDecimal standardValue;
            try {
                standardValue = new BigDecimal(temp.get(SheetService.columnToIndex("J")));// 事件标准价值
            } catch (Exception e) {
                StringTool.setMsg(sb, String.format("第【%s】行事件标准价值格式不正确，必须为【数值】", j + 1));
                cell.setCellValue("事件标准价值格式不正确，必须为【数值】");
                continue;
            }
            if (ObjectUtils.isEmpty(standardValue)) {
                StringTool.setMsg(sb, String.format("第【%s】行事件标准价值不能为空", j + 1));
                cell.setCellValue("事件标准价值不能为空");
                continue;
            }
            // 计算事件标准价值分
            standardValue = (standardValue.multiply(proportion)).setScale(2, RoundingMode.HALF_UP);

            BigDecimal delow;
            try {
                delow = new BigDecimal(temp.get(SheetService.columnToIndex("K")));
            } catch (Exception e) {
                StringTool.setMsg(sb, String.format("第【%s】行不合格价值格式不正确，必须为【数值】", j + 1));
                cell.setCellValue("不合格价值格式不正确，必须为【数值】");
                continue;
            }
            if (ObjectUtils.isEmpty(delow)) {
                StringTool.setMsg(sb, String.format("第【%s】行事件标准价值不能为空", j + 1));
                cell.setCellValue("事件标准价值不能为空");
                continue;
            }
            delow = (delow.multiply(proportion)).setScale(2, RoundingMode.HALF_UP);

            BigDecimal middle;
            try {
                middle = new BigDecimal(temp.get(SheetService.columnToIndex("L")));
            } catch (Exception e) {
                StringTool.setMsg(sb, String.format("第【%s】行中价值格式不正确，必须为【数值】", j + 1));
                cell.setCellValue("中价值格式不正确，必须为【数值】");
                continue;
            }
            if (ObjectUtils.isEmpty(middle)) {
                StringTool.setMsg(sb, String.format("第【%s】行中价值不能为空", j + 1));
                cell.setCellValue("中价值不能为空");
                continue;
            }
            middle = (middle.multiply(proportion)).setScale(2, RoundingMode.HALF_UP);

            BigDecimal fine;
            try {
                fine = new BigDecimal(temp.get(SheetService.columnToIndex("M")));// 事件标准价值
            } catch (Exception e) {
                StringTool.setMsg(sb, String.format("第【%s】行良价值格式不正确，必须为【数值】", j + 1));
                cell.setCellValue("良价值格式不正确，必须为【数值】");
                continue;
            }
            if (ObjectUtils.isEmpty(fine)) {
                StringTool.setMsg(sb, String.format("第【%s】行良价值不能为空", j + 1));
                cell.setCellValue("良价值不能为空");
                continue;
            }
            fine = (fine.multiply(proportion)).setScale(2, RoundingMode.HALF_UP);

            BigDecimal excellent;
            try {
                excellent = new BigDecimal(temp.get(SheetService.columnToIndex("N")));// 事件标准价值
            } catch (Exception e) {
                StringTool.setMsg(sb, String.format("第【%s】行优价值格式不正确，必须为【数值】", j + 1));
                cell.setCellValue("优价值格式不正确，必须为【数值】");
                continue;
            }
            if (ObjectUtils.isEmpty(excellent)) {
                StringTool.setMsg(sb, String.format("第【%s】行优价值不能为空", j + 1));
                cell.setCellValue("优价值不能为空");
                continue;
            }
            excellent = (excellent.multiply(proportion)).setScale(2, RoundingMode.HALF_UP);

            String activeDateStr = temp.get(SheetService.columnToIndex("O"));
            LocalDate activeDate = null;
            try {
                activeDate = LocalDate.parse(activeDateStr);
            } catch (Exception e) {
                StringTool.setMsg(sb, String.format("第【%s】行生效日期格式不正确，必须为【XXXX-XX-XX】", j + 1));
                cell.setCellValue("生效日期格式不正确，必须为【XXXX-XX-XX】");
                continue;
            }
            // endregion

            // region 业务校验
            // 事件清单信息部门与填报部门校验
            // 查询事件清单
            EventList eventList = eventListMapper.selectById(eventsId);
            if (eventIdWithActivityMap.containsKey(eventsId)) {
                // 如果基础事件的序号相同，则它们的生效日期也要相同
                if (!eventIdWithActivityMap.get(eventsId).isEqual(activeDate)) {
                    /*StringTool.setMsg(sb, String.format("第【%s】行序号为【%s】的事件清单的生效日期不一致，不能导入", j + 1, eventsId));
                    cell.setCellValue(String.format("序号为【%s】的事件清单的生效日期不一致，不能导入", eventsId));
                    continue;*/
                    throw new ServiceException(String.format("序号为【%s】的事件清单的生效日期不一致，不能导入", eventsId));
                }
            }
            eventIdWithActivityMap.put(eventsId, activeDate);
            if (ObjectUtils.isEmpty(eventList)) {
                StringTool.setMsg(sb,
                        String.format("第【%s】行序号为【%s】的事件清单不存在，必须为【数值】", j + 1, eventsId));
                cell.setCellValue(String.format("序号为【%s】的事件清单不存在，必须为【数值】", eventsId));
                continue;
            }
            if (!departmentName.equals(eventList.getDepartmentName())) {
                StringTool.setMsg(sb,
                        String.format("第【%s】行序号为【%s】的事件清单对应部门与填报部门不一致，不能导入", j + 1, eventsId));
                cell.setCellValue(String.format("序号为【%s】的事件清单对应部门与填报部门不一致，不能导入", eventsId));
                continue;
            }
            // 清单序号，生效日期相同不能再次导入
            boolean activeFlag = true;
            // 查询岗位关联数据
            QueryWrapper<EventsRelationRole> relationRoleCheckQueryWrapper = new QueryWrapper<>();
            relationRoleCheckQueryWrapper.eq("eventsId", eventList.getId()).select("activeDate,id,eventsId");
            List<EventsRelationRole> checkDataList = eventsRelationRoleMapper.selectList(relationRoleCheckQueryWrapper);
            for (EventsRelationRole eventsRelationRole : checkDataList) {
                if (activeDate.isEqual(eventsRelationRole.getActiveDate())) {
                    // 如果要导入数的生效日期在数据库中被查出来的话则不能导入
                    activeFlag = false;
                    break;
                }

            }
            if (!activeFlag) {
                // activeFlag=false
                /*StringTool.setMsg(sb, String.format("第【%s】行序号为【%s】的事件清单，生效日期为【%s】已经导入，不能重复导入",
                        j + 1, eventsId, activeDate.format(DateTimeFormatter.ISO_LOCAL_DATE)));
                setFailedContent(result,String.format("第【%s】行序号为【%s】的事件清单，生效日期为【%s】已经导入，不能重复导入",j + 1, eventsId, activeDate.format(DateTimeFormatter.ISO_LOCAL_DATE)));
                cell.setCellValue(String.format("序号为【%s】的事件清单，生效日期为【%s】已经导入，不能重复导入", eventsId,
                        activeDate.format(DateTimeFormatter.ISO_LOCAL_DATE)));
                continue;*/
                throw new ServiceException(String.format("第【%s】行序号为【%s】的事件清单，生效日期为【%s】已经导入，不能重复导入",j + 1, eventsId, activeDate.format(DateTimeFormatter.ISO_LOCAL_DATE)));
            }
            // endregion

            // region 查询部门，角色（岗位），用户信息
            QueryWrapper<Department> departmentQueryWrapper = new QueryWrapper<>();
            departmentQueryWrapper.eq("name", departmentName);
            List<Department> departmentList = departmentMapper.selectList(departmentQueryWrapper);
            if (ObjectUtils.isEmpty(departmentList)) {
                StringTool.setMsg(sb, String.format("第【%s】行名称为【%s】的部门未维护", j + 1, departmentName));
                cell.setCellValue(String.format("名称为【%s】的部门未维护", departmentName));
                continue;
            }
            Department department = departmentList.get(0);
            QueryWrapper<Role> roleQueryWrapper = new QueryWrapper<>();
            roleQueryWrapper.eq("name", roleName);
            List<Role> roleList = roleMapper.selectList(roleQueryWrapper);
            if (ObjectUtils.isEmpty(roleList)) {
                StringTool.setMsg(sb, String.format("第【%s】行名称为【%s】的角色（岗位）未维护", j + 1, roleName));
                cell.setCellValue(String.format("名称为【%s】的角色（岗位）未维护", roleName));
                continue;
            }
            Role role = roleList.get(0);
            QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
            userQueryWrapper.eq("name", userName);
            List<User> userList = userMapper.selectList(userQueryWrapper);
            if (ObjectUtils.isEmpty(userList)) {
                StringTool.setMsg(sb, String.format("第【%s】行名称为【%s】的用户未维护", j + 1, userName));
                cell.setCellValue(String.format("名称为【%s】的用户未维护", userName));
                continue;
            }
            User signUser = userList.get(0);
            // endregion

            // region 组装配比数据，相同事件序号的所有项比重之各必须等于100%，否则导入不成功
            String key = eventsId.toString();
            if (checkProportionMap.containsKey(key)) {
                BigDecimal tempDecimal = checkProportionMap.get(key).add(proportion);
                checkProportionMap.put(key, tempDecimal);
            } else {
                checkProportionMap.put(key, proportion);
            }
            EventsRelationRole eventsRelationRole = new EventsRelationRole();
            eventsRelationRole.setEventsId(eventsId);
            eventsRelationRole.setRoleName(role.getName());
            eventsRelationRole.setRoleId(role.getId());
            eventsRelationRole.setStandardValue(standardValue);
            eventsRelationRole.setDelow(delow);
            eventsRelationRole.setMiddle(middle);
            eventsRelationRole.setFine(fine);
            eventsRelationRole.setExcellent(excellent);
            LocalDate createDate = DateTool.dateToLocalDate(attachment.getImportDate());
            eventsRelationRole.setCreateDate(createDate);
            eventsRelationRole.setYear(createDate.getYear());
            eventsRelationRole.setMonth(createDate.getMonthValue());
            eventsRelationRole.setActiveDate(activeDate);
            eventsRelationRole.setDepartmentId(department.getId());
            eventsRelationRole.setDepartmentName(department.getName());
            eventsRelationRole.setProportion(proportion);
            eventsRelationRole.setJobName(temp.get(SheetService.columnToIndex("B")));
            eventsRelationRole.setWorkEvents(temp.get(SheetService.columnToIndex("C")));
            eventsRelationRole.setEventsFirstType(temp.get(SheetService.columnToIndex("D")));
            eventsRelationRole.setEventsType(eventList.getEventsType());
            eventsRelationRole.setIsMainOrNext(isMainOrNext);
            eventsRelationRole.setUserName(signUser.getName());
            eventsRelationRole.setUserId(signUser.getId());
            eventsRelationRole.setAttachmentId(attachment.getId());
            eventsRelationRole.setCreatedId(user.getId());
            eventsRelationRole.setUpdatedId(user.getId());
            eventsRelationRole.setCreatedName(user.getName());
            eventsRelationRole.setUpdatedName(user.getName());
            eventsRelationRole.setCreatedAt(LocalDateTime.now());
            eventsRelationRole.setUpdatedAt(LocalDateTime.now());
            // endregion

            // 写入更新
            //eventsRelationRoleService.save(eventsRelationRole);
            // 存入集合，用于读取循环数据完毕后批量写入数据库
            relationRoleList.add(eventsRelationRole);
            updatedEventsIds.add(eventList.getId());

            cell.setCellValue("成功");
            successNum++;
        }
        // 批量写入
        eventsRelationRoleService.saveBatch(relationRoleList);
        // 如果updatedEventsIds为空则报错
        if (ObjectUtils.isEmpty(updatedEventsIds)) {
            setFailedContent(result, "导入的生效日期都存在，请更改后重新导入");
            throw new ServiceException("导入的生效日期都存在，请更改后重新导入");
        }

        // region 检查占比
        boolean flag = true;
        List<Long> checkEventsIds = new ArrayList<>();
        for (Entry<String, BigDecimal> entry : checkProportionMap.entrySet()) {
            String eventsIdStr = entry.getKey();// 基础事件序号
            BigDecimal proportionDecimal = entry.getValue();// 占比
            // 不等于0时，报错
            if (proportionDecimal.compareTo(new BigDecimal(1)) != 0) {
                flag = false;
                checkEventsIds.add(InstandTool.stringToLong(eventsIdStr));
            }
        }
        //
        if (!flag) {
            throw new ServiceException(String.format("序号为【%s】的事件清单，维护岗位的配比不等于【1】,或者相同基础事件的【生效日期】存在导入记录,不允许本次操作，请检查",
                    checkEventsIds.toString()));
        }
        // endregion


        sheetService.write(attachment.getPath(), attachment.getFilename());// 写入excel表

        if (successNum == 0) {
            throw new ServiceException("数据导入失败，请检查表格内容");
        }

        // region 添加数据并修改对应事件清单的roleComplete字段
        UpdateWrapper<EventList> eventUpdateWrapper = new UpdateWrapper<>();
        eventUpdateWrapper.in("id", updatedEventsIds).set("roleComplete", CommonConstant.TRUE)
                .set("updatedId", user.getId()).set("updatedName", user.getName())
                .set("updatedAt", LocalDateTime.now());
        eventListMapper.update(null, eventUpdateWrapper);
        // endregion

        result.put("conclusion", String.format("excel数据行数%s行，其中成功导入%s行", temps.size(), successNum));

        return result;
    }

}
