/**
 * ------------------------修改日志---------------------------------
 * 修改人			修改日期			修改内容
 * 兰宇铧			2023-03-07		修改岗位配比导入时的生效日期判断逻辑，需要保证一个时间段内只有一个清单的配比是生效的
 */
package com.fssy.shareholder.management.service.system.impl.performance.employee.relationRole;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.fssy.shareholder.management.mapper.manage.department.DepartmentMapper;
import com.fssy.shareholder.management.mapper.manage.department.ViewDepartmentRoleUserMapper;
import com.fssy.shareholder.management.mapper.manage.role.RoleMapper;
import com.fssy.shareholder.management.mapper.manage.user.UserMapper;
import com.fssy.shareholder.management.mapper.system.performance.employee.EventListMapper;
import com.fssy.shareholder.management.mapper.system.performance.employee.EventsRelationRoleMapper;
import com.fssy.shareholder.management.pojo.manage.department.Department;
import com.fssy.shareholder.management.pojo.manage.department.ViewDepartmentRoleUser;
import com.fssy.shareholder.management.pojo.manage.role.Role;
import com.fssy.shareholder.management.pojo.manage.user.User;
import com.fssy.shareholder.management.pojo.system.config.Attachment;
import com.fssy.shareholder.management.pojo.system.performance.employee.EventList;
import com.fssy.shareholder.management.pojo.system.performance.employee.EventsRelationRole;
import com.fssy.shareholder.management.service.common.SheetService;
import com.fssy.shareholder.management.service.system.performance.employee.EventsRelationRoleAttachmentService;
import com.fssy.shareholder.management.service.system.performance.employee.EventsRelationRoleService;
import com.fssy.shareholder.management.tools.common.DateTool;
import com.fssy.shareholder.management.tools.common.InstandTool;
import com.fssy.shareholder.management.tools.common.StringTool;
import com.fssy.shareholder.management.tools.constant.CommonConstant;
import com.fssy.shareholder.management.tools.constant.PerformanceConstant;
import com.fssy.shareholder.management.tools.exception.ServiceException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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

    @Autowired
    private ViewDepartmentRoleUserMapper viewDepartmentRoleUserMapper;

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
        // 导入事件岗位关系
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
        // 导入数据总数
        int totalNum = 0;
        // 添加数量
        int addNum = 0;
        // 修改数量
        int updateNum = 0;
        
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
            totalNum++;
            // 导入结果写入列
            Cell cell = row.createCell(SheetService.columnToIndex("Q"));// 每一行的结果信息上传到O列

            // region 导入数据校验
            Long eventsId;
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
            String departmentName = temp.get(SheetService.columnToIndex("F"));
            if (ObjectUtils.isEmpty(departmentName)) {
                StringTool.setMsg(sb, String.format("第【%s】行部门名称不能为空", j + 1));
                cell.setCellValue("部门名称不能为空");
                continue;
            }
            String office = temp.get(SheetService.columnToIndex("G"));
            if (ObjectUtils.isEmpty(office)) {
                StringTool.setMsg(sb, String.format("第【%s】行岗科室名称不能为空", j + 1));
                cell.setCellValue("科室名称不能为空");
                continue;
            }
            String roleName = temp.get(SheetService.columnToIndex("I"));
            if (ObjectUtils.isEmpty(roleName)) {
                StringTool.setMsg(sb, String.format("第【%s】行岗位名称不能为空", j + 1));
                cell.setCellValue("岗位名称不能为空");
                continue;
            }
            String isMainOrNext = temp.get(SheetService.columnToIndex("J"));
            if (ObjectUtils.isEmpty(isMainOrNext)) {
                StringTool.setMsg(sb, String.format("第【%s】行是否主担不能为空", j + 1));
                cell.setCellValue("是否主担不能为空");
                continue;
            }
            String userName = temp.get(SheetService.columnToIndex("K"));
            if (ObjectUtils.isEmpty(userName)) {
                StringTool.setMsg(sb, String.format("第【%s】行职员名称不能为空", j + 1));
                cell.setCellValue("职员名称不能为空");
                continue;
            }
            BigDecimal proportion;
            try {
                // 占比
                proportion = new BigDecimal(temp.get(SheetService.columnToIndex("H")));
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
                standardValue = new BigDecimal(temp.get(SheetService.columnToIndex("E")));// 事件标准价值
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

            String activeDateStr = temp.get(SheetService.columnToIndex("L"));
            LocalDate activeDate;
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
                    throw new ServiceException(String.format("序号为【%s】的事件清单的生效日期不一致，不能导入", eventsId));
                }
            }
            eventIdWithActivityMap.put(eventsId, activeDate);
            if (ObjectUtils.isEmpty(eventList)) {
                StringTool.setMsg(sb,
                        String.format("第【%s】行序号为【%s】的事件清单不存在", j + 1, eventsId));
                cell.setCellValue(String.format("序号为【%s】的事件清单不存在", eventsId));
                continue;
            }
            // 填报的部门，有多个工作项得话，必须有一个项的部门与基础事件的部门保持一致
            if (!departmentName.equals(eventList.getDepartmentName())) {
                StringTool.setMsg(sb,
                        String.format("第【%s】行序号为【%s】的事件清单对应部门与填报部门不一致，不能导入", j + 1, eventsId));
                cell.setCellValue(String.format("序号为【%s】的事件清单对应部门与填报部门不一致，不能导入", eventsId));
                continue;
            }
			QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
			userQueryWrapper.eq("name", userName);
			List<User> userList = userMapper.selectList(userQueryWrapper);
			if (ObjectUtils.isEmpty(userList))
			{
				StringTool.setMsg(sb, String.format("第【%s】行名称为【%s】的用户未维护", j + 1, userName));
				cell.setCellValue(String.format("名称为【%s】的用户未维护", userName));
				continue;
			}
			User signUser = userList.get(0);
            // 2023-03-07 修改问题，真实使用过程中，允许修改相同生效日期的情况,前提为状态不能是完结状态（已经填 报履职计划）
            // 清单序号，生效日期相同不能再次导入
            // 查询岗位关联数据
			QueryWrapper<EventsRelationRole> relationRoleCheckQueryWrapper = new QueryWrapper<>();
			relationRoleCheckQueryWrapper.eq("eventsId", eventList.getId())
					.eq("activeDate", activeDate).eq("userId", signUser.getId())
					.select("activeDate,id,eventsId,status");
			List<EventsRelationRole> checkList = eventsRelationRoleMapper.selectList(relationRoleCheckQueryWrapper);
			boolean checkOriRoleFlag = true;
			if (!ObjectUtils.isEmpty(checkList))
			{
				for (EventsRelationRole checkRole : checkList)
				{
					if (PerformanceConstant.FINAL.equals(checkRole.getStatus()))
					{
						checkOriRoleFlag = false;
						StringTool.setMsg(sb,
								String.format(
										"第【%s】行事件清单序号为【%s】生效日期为【%s】承担用户为【%s】的岗位配比已经填报计划，不能再次导入更新",
										j + 1, eventList.getId(), activeDate, signUser.getName()));
						cell.setCellValue(String.format(
								"第【%s】行事件清单序号为【%s】生效日期为【%s】承担用户为【%s】的岗位配比已经填报计划，不能再次导入更新", j + 1,
								eventList.getId(), activeDate, signUser.getName()));
						continue;
					}
				}
				if (!checkOriRoleFlag)
				{
					continue;
				}
				// 如果要导入数的生效日期在数据库中被查出来的话则不能导入
				updateNum++;
				eventsRelationRoleMapper.delete(relationRoleCheckQueryWrapper);
			}
			else
			{
				addNum++;
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
            // 校验部门、岗位、员工是否维护
            LambdaQueryWrapper<ViewDepartmentRoleUser> viewDepRoleUser = new LambdaQueryWrapper<>();
            viewDepRoleUser.eq(ViewDepartmentRoleUser::getDepartmentName, departmentName)
                    .eq(ViewDepartmentRoleUser::getRoleName, roleName)
                    .eq(ViewDepartmentRoleUser::getUserName, userName);
            List<ViewDepartmentRoleUser> viewDepartmentRoleUsers = viewDepartmentRoleUserMapper.selectList(viewDepRoleUser);
            if (ObjectUtils.isEmpty(viewDepartmentRoleUsers)) {
                StringTool.setMsg(sb, String.format("第【%s】行部门为【%s】、岗位为【%s】、职员名称为【%s】未维护", j + 1, departmentName, roleName, userName));
                cell.setCellValue(String.format("部门为【%s】、岗位为【%s】、职员名称为【%s】未维护", departmentName, roleName, userName));
                continue;
            }
            // endregion

            // region 组装配比数据，相同事件序号的所有项比重之各必须等于100%，否则导入不成功
            String key = eventsId.toString();
            if (checkProportionMap.containsKey(key)) {
                BigDecimal tempDecimal = checkProportionMap.get(key).add(proportion);
                checkProportionMap.put(key, tempDecimal);
            } else {
                checkProportionMap.put(key, proportion);
            }
            // endregion
            
            // region 构建实体类
            EventsRelationRole eventsRelationRole = new EventsRelationRole();
            eventsRelationRole.setEventsId(eventsId);
            eventsRelationRole.setRoleName(role.getName());
            eventsRelationRole.setRoleId(role.getId());
            eventsRelationRole.setDelow(standardValue.multiply(new BigDecimal(0)).multiply(proportion).setScale(2, RoundingMode.HALF_UP));
            eventsRelationRole.setMiddle(standardValue.multiply(new BigDecimal("0.6")).multiply(proportion).setScale(2, RoundingMode.HALF_UP));
            eventsRelationRole.setFine(standardValue.multiply(new BigDecimal("0.8")).multiply(proportion).setScale(2, RoundingMode.HALF_UP));
            eventsRelationRole.setExcellent(standardValue.multiply(new BigDecimal(1)).multiply(proportion).setScale(2, RoundingMode.HALF_UP));
            eventsRelationRole.setStandardValue(standardValue.multiply(new BigDecimal(1)).multiply(proportion).setScale(2, RoundingMode.HALF_UP));
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
            LambdaQueryWrapper<ViewDepartmentRoleUser> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(ViewDepartmentRoleUser::getOfficeName, office);
            List<ViewDepartmentRoleUser> roleUsers = viewDepartmentRoleUserMapper.selectList(wrapper);
            if (!ObjectUtils.isEmpty(roleUsers)) {
                eventsRelationRole.setOfficeId(roleUsers.get(0).getOfficeId());
            }
            eventsRelationRole.setOffice(office);
            eventsRelationRole.setStatus(PerformanceConstant.WAIT_PLAN);
            // endregion

			// 将基础事件的状态设置为‘完结’
			UpdateWrapper<EventList> listUpdateWrapper = new UpdateWrapper<>();
			listUpdateWrapper.eq("id", eventList.getId()).set("status", PerformanceConstant.FINAL)
					.set("updatedId", user.getId()).set("updatedName", user.getName())
					.set("updatedAt", LocalDateTime.now());
			eventListMapper.update(null, listUpdateWrapper);
            // 存入集合，用于读取循环数据完毕后批量写入数据库
            relationRoleList.add(eventsRelationRole);
            updatedEventsIds.add(eventList.getId());

            cell.setCellValue("成功");
            successNum++;
        }

        sheetService.write(attachment.getPath(), attachment.getFilename());// 写入excel表
        
        // 2023-03-07 修改岗位配比导入时的生效日期判断逻辑，需要保证一个时间段内只有一个清单的配比是生效的
        // region 设置满足条件的岗位配比取消
        Map<String, Boolean> groupByMap = new HashMap<>();
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		List<String> statusList = Arrays.asList(PerformanceConstant.FINAL,
				PerformanceConstant.WAIT_PLAN);
		for (EventsRelationRole tempRoleData : relationRoleList)
		{
			// 根据事件清单id和生效日期为主键，处理过放进来
			String key = tempRoleData.getEventsId() + "_"
					+ tempRoleData.getActiveDate().format(dtf);
			if (!groupByMap.containsKey(key))
			{
				// 1、新的配比生效日期大于当前日期，不用设置
				if (tempRoleData.getActiveDate().isAfter(LocalDate.now()))
				{
					continue;
				}
				else
				{
					// 2、新的配比生效日期小于当前日期
					// 查询事件最新的配比
					QueryWrapper<EventsRelationRole> roleQueryWrapper = new QueryWrapper<>();
					roleQueryWrapper.eq("eventsId", tempRoleData.getEventsId()).in("status",
							statusList);
					roleQueryWrapper.select("eventsId, MAX(activeDate) as activeDate")
							.groupBy("eventsId");
					List<EventsRelationRole> oriRoleList = eventsRelationRoleMapper
							.selectList(roleQueryWrapper);
					
					if (ObjectUtils.isEmpty(oriRoleList))
					{
						continue;
					}
					else
					{
						EventsRelationRole oriRelationRole = oriRoleList.get(0);
						// 2.1、新的配比生效日期小于已经生效日期，不用设置
						if (tempRoleData.getActiveDate().isBefore(oriRelationRole.getActiveDate()))
						{
							continue;
						}
						else
						{
							// 2.2、新的配比生效日期大于等于已经生效日期，需要原来最大的生效日期设置
							UpdateWrapper<EventsRelationRole> roleUpdateWrapper = new UpdateWrapper<>();
							roleUpdateWrapper.eq("eventsId", tempRoleData.getEventsId())
									.eq("activeDate", oriRelationRole.getActiveDate())
									.in("status", statusList)
									.set("status", PerformanceConstant.EVENT_ROLE_STATUS_CANCEL)
									.set("updatedId", user.getId())
									.set("updatedName", user.getName())
									.set("updatedAt", LocalDateTime.now());
							eventsRelationRoleMapper.update(null, roleUpdateWrapper);

						}
					}
				}
				groupByMap.put(key, true);
			}
			else
			{
				continue;
			}
		}
		// endregion
        
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
