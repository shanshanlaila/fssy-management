package com.fssy.shareholder.management.controller.system.performance.employee.excellent;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fssy.shareholder.management.annotation.RequiredLog;
import com.fssy.shareholder.management.pojo.common.SysResult;
import com.fssy.shareholder.management.pojo.manage.user.User;
import com.fssy.shareholder.management.pojo.system.config.Attachment;
import com.fssy.shareholder.management.pojo.system.config.ImportModule;
import com.fssy.shareholder.management.pojo.system.performance.employee.EntryCasPlanDetail;
import com.fssy.shareholder.management.pojo.system.performance.employee.EntryCasReviewDetail;
import com.fssy.shareholder.management.pojo.system.performance.employee.EntryExcellentStateDetail;
import com.fssy.shareholder.management.pojo.system.performance.employee.EventList;
import com.fssy.shareholder.management.service.manage.department.DepartmentService;
import com.fssy.shareholder.management.service.manage.role.RoleService;
import com.fssy.shareholder.management.service.manage.user.UserService;
import com.fssy.shareholder.management.service.system.config.ImportModuleService;
import com.fssy.shareholder.management.service.system.performance.employee.EntryCasPlanDetailService;
import com.fssy.shareholder.management.service.system.performance.employee.EntryCasReviewDetailService;
import com.fssy.shareholder.management.service.system.performance.employee.EntryExcellentStateDetailService;
import com.fssy.shareholder.management.service.system.performance.employee.EventListService;
import com.fssy.shareholder.management.tools.common.FileAttachmentTool;
import com.fssy.shareholder.management.tools.common.GetTool;
import com.fssy.shareholder.management.tools.common.InstandTool;
import com.fssy.shareholder.management.tools.constant.PerformanceConstant;
import com.fssy.shareholder.management.tools.exception.ServiceException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 * *****业务部门：	绩效科		*****数据表中文名：	员工月度履职评价说明表明细		*****数据表名：	bs_performance_entry_excellent_state_detail		*****数据表作用：	员工月度履职评价为优时，填报的内容		*****变更记录：	时间         	变更人		变更内容	20220915	兰宇铧           	初始设计 前端控制器
 * </p>
 *
 * @author 农浩
 * @since 2022-10-24
 */
@Controller
@RequestMapping("/system/entry-excellent-state-detail/")
public class EntryExcellentStateDetailController {
    @Autowired
    private EntryExcellentStateDetailService excellentService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private FileAttachmentTool fileAttachmentTool;

    @Autowired
    private EventListService eventListService;

    @Autowired
    private EntryCasPlanDetailService entryCasPlanDetailService;

    @Autowired
    private EntryCasReviewDetailService entryCasReviewDetailService;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private ImportModuleService importModuleService;

    @GetMapping("index")
    @RequiresPermissions("system:performance:entryExcellentStateDetail:index")
    public String showEntryExcellentStateDetail(Model model) {
        Map<String, Object> departmentParams = new HashMap<>();
        List<Map<String, Object>> departmentNameList = departmentService.findDepartmentsSelectedDataListByParams(departmentParams, new ArrayList<>());
        model.addAttribute("departmentNameList", departmentNameList);
        Map<String, Object> roleParams = new HashMap<>();
        List<Map<String, Object>> roleNameList = roleService.findRoleSelectedDataListByParams(roleParams, new ArrayList<>());
        model.addAttribute("roleNameList", roleNameList);
        Map<String, Object> userParams = new HashMap<>();
        List<String> selectedUserIds = new ArrayList<>();
        List<Map<String, Object>> userList = userService.findUserSelectedDataListByParams(userParams, selectedUserIds);
        model.addAttribute("userList", userList);
        return "system/performance/employee/entry-excellent-state-detail-list";
    }

    @RequestMapping("getObjects")
    @ResponseBody
    public Map<String, Object> getObject(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>(20);
        Map<String, Object> params = getParams(request);
        GetTool.getPageDataRes(result,params,request,excellentService);
        return result;
    }

    private Map<String, Object> getParams(HttpServletRequest request) {
        Map<String, Object> params = new HashMap<>();
        if (!ObjectUtils.isEmpty(request.getParameter("id"))) {
            params.put("id", request.getParameter("id"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("createdAt"))) {
            params.put("createdAt", request.getParameter("createdAt"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("createdId"))) {
            params.put("createdId", request.getParameter("createdId"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("createdName"))) {
            params.put("createdName", request.getParameter("createdName"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("updatedAt"))) {
            params.put("updatedAt", request.getParameter("updatedAt"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("updatedId"))) {
            params.put("updatedId", request.getParameter("updatedId"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("updatedName"))) {
            params.put("updatedName", request.getParameter("updatedName"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("eventsId"))) {
            params.put("eventsId", request.getParameter("eventsId"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("workEvents"))) {
            params.put("workEvents", request.getParameter("workEvents"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("mainUserName"))) {
            params.put("mainUserName", request.getParameter("mainUserName"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("completeDesc"))) {
            params.put("completeDesc", request.getParameter("completeDesc"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("actualCompleteDate"))) {
            params.put("actualCompleteDate", request.getParameter("actualCompleteDate"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("oriTarget"))) {
            params.put("oriTarget", request.getParameter("oriTarget"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("actualTarget"))) {
            params.put("actualTarget", request.getParameter("actualTarget"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("innovative"))) {
            params.put("innovative", request.getParameter("innovative"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("note"))) {
            params.put("note", request.getParameter("note"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("departmentName"))) {
            params.put("departmentName", request.getParameter("departmentName"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("departmentIds"))) {
            String departmentIds = request.getParameter("departmentIds");
            List<String> departmentIdList = Arrays.asList(departmentIds.split(","));
            params.put("departmentIdList", departmentIdList);
        }
        if (!ObjectUtils.isEmpty(request.getParameter("createDate"))) {
            params.put("createDate", request.getParameter("createDate"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("createName"))) {
            params.put("createName", request.getParameter("createName"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("createId"))) {
            params.put("createId", request.getParameter("createId"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("auditName"))) {
            params.put("auditName", request.getParameter("auditName"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("auditId"))) {
            params.put("auditId", request.getParameter("auditId"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("auditDate"))) {
            params.put("auditDate", request.getParameter("auditDate"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("auditNote"))) {
            params.put("auditNote", request.getParameter("auditNote"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("mergeNo"))) {
            params.put("mergeNo", request.getParameter("mergeNo"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("mergeId"))) {
            params.put("mergeId", request.getParameter("mergeId"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("casPlanId"))) {
            params.put("casPlanId", request.getParameter("casPlanId"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("casReviewId"))) {
            params.put("casReviewId", request.getParameter("casReviewId"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("applyDate"))) {
            params.put("applyDate", request.getParameter("applyDate"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("year"))) {
            params.put("year", request.getParameter("year"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("month"))) {
            params.put("month", request.getParameter("month"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("ministerReview"))) {
            params.put("ministerReview", request.getParameter("ministerReview"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("classReview"))) {
            params.put("classReview", request.getParameter("classReview"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("status"))) {
            params.put("status", request.getParameter("status"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("ministerReviewUser"))) {
            params.put("ministerReviewUser", request.getParameter("ministerReviewUser"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("ministerReviewUserId"))) {
            params.put("ministerReviewUserId", request.getParameter("ministerReviewUserId"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("ministerReviewDate"))) {
            params.put("ministerReviewDate", request.getParameter("ministerReviewDate"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("classReviewUser"))) {
            params.put("classReviewUser", request.getParameter("classReviewUser"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("classReviewUserId"))) {
            params.put("classReviewUserId", request.getParameter("classReviewUserId"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("classReviewDate"))) {
            params.put("classReviewDate", request.getParameter("classReviewDate"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("nextUserName"))) {
            params.put("nextUserName", request.getParameter("nextUserName"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("statusOr"))) {
            params.put("statusOr", request.getParameter("statusOr"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("roleName"))) {
            params.put("roleName", request.getParameter("roleName"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("roleIds"))) {
            String roleIds = request.getParameter("roleIds");
            List<String> roleIdList = Arrays.asList(roleIds.split(","));
            params.put("roleIdList", roleIdList);
        }
        if (!ObjectUtils.isEmpty(request.getParameter("roleId"))) {
            params.put("roleId", request.getParameter("roleId"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("idAsc"))) {
            params.put("idAsc", request.getParameter("idAsc"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("select"))) {
            params.put("select", request.getParameter("select"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("groupBy"))) {
            params.put("groupBy", request.getParameter("groupBy"));
        }
        if (!ObjectUtils.isEmpty(request.getParameter("mainUserIds"))) {
            params.put("mainUserIds", request.getParameter("mainUserIds"));
        }
        return params;
    }

    /**
     * 筛选状态-评优材料提交审核
     *
     * @return 结果
     */
    @RequiredLog("总结评优提交审核")
    //@RequiresPermissions("system:performance:entryCasPlanDetail:indexStatus")
    @PostMapping("indexStatus")
    @ResponseBody
    public SysResult indexStatus(@RequestParam(value = "excellentStateDetailIds[]") List<String> excellentStateDetailIds) {
        boolean result = excellentService.submitAuditForExcellent(excellentStateDetailIds);
        if (result) {
            return SysResult.ok();
        }
        return SysResult.build(500, "只能选择待提交审核状态的评优材料，提交审核失败，请重新刷新后选择提交");
    }

    /**
     * 评优材料撤销审核
     *
     * @param excellentStateDetailIds
     * @return
     */
    @RequiredLog("总结评优撤销审核")
    @PostMapping("retreat")
    @ResponseBody
    public SysResult retreat(@RequestParam(value = "excellentStateDetailIds[]") List<String> excellentStateDetailIds) {
        boolean result = excellentService.retreatForExcellent(excellentStateDetailIds);
        if (result) {
            return SysResult.ok();
        }
        return SysResult.build(500, "撤销审核失败");
    }

    /**
     * 绩效科撤销审核评优材料
     *
     * @param excellentStateDetailIds
     * @return
     */
    @RequiredLog("绩效科总结评优撤销审核")
    @PostMapping("PerformanceRetreat")
    @ResponseBody
    public SysResult performanceRetreat(@RequestParam(value = "excellentStateDetailIds[]") List<String> excellentStateDetailIds) {
        boolean result = excellentService.PerformanceRetreat(excellentStateDetailIds);
        if (result) {
            return SysResult.ok();
        }
        return SysResult.build(500, "撤销审核失败");
    }

    /**
     * 绩效科审核展示修改页面
     *
     * @param id    履职明细id
     * @param model 数据模型
     * @return 修改页面
     */
    @GetMapping("edit/{id}")
    @RequiredLog("绩效科审核展示修改页面")
    public String showEditPage(@PathVariable Long id, Model model) {
        // 查询评优材料对象
        EntryExcellentStateDetail entryExcellentStateDetail = excellentService.getById(id);
        // 查询总结对象
        EntryCasReviewDetail entryCasReviewDetail = entryCasReviewDetailService.getById(entryExcellentStateDetail.getCasReviewId());
        if (ObjectUtils.isEmpty(entryCasReviewDetail)) {
            throw new ServiceException("不存在对应的总结");
        }
        // 查询用户
        Map<String, Object> params = new HashMap<>();
        List<Map<String, Object>> userNameList = userService.findUserSelectedDataListByParams(params, new ArrayList<>());
        // 查询主/次担
        String mainUserNameStr = entryExcellentStateDetail.getMainUserName();
        ArrayList<String> mainNameValues = new ArrayList<>();
        if (ObjectUtils.isEmpty(mainUserNameStr)) {
            model.addAttribute("mainNameValues", mainNameValues);
        } else {
            String[] mainUserNames = mainUserNameStr.split(",");
            for (String mainUserName : mainUserNames) {
                LambdaQueryWrapper<User> userQueryWrapper = new LambdaQueryWrapper<>();
                userQueryWrapper.eq(User::getName, mainUserName);
                User user = userService.getByName(userQueryWrapper);
                mainNameValues.add(String.valueOf(user.getId()));
            }
            model.addAttribute("mainNameValues", mainNameValues);
        }
        String nextUserNameStr = entryExcellentStateDetail.getNextUserName();
        ArrayList<String> nextNameValues = new ArrayList<>(100);
        if (ObjectUtils.isEmpty(nextUserNameStr)) {
            model.addAttribute("nextNameValues", nextNameValues);
        } else {
            String[] nextUserNames = nextUserNameStr.split(",");
            for (String nextUserName : nextUserNames) {
                LambdaQueryWrapper<User> userQueryWrapper = new LambdaQueryWrapper<>();
                userQueryWrapper.eq(User::getName, nextUserName);
                User user = userService.getByName(userQueryWrapper);
                nextNameValues.add(String.valueOf(user.getId()));
            }
            model.addAttribute("nextNameValues", nextNameValues);
        }
        model.addAttribute("userNameList", userNameList);
        model.addAttribute("entryCasReviewDetail", entryCasReviewDetail);
        model.addAttribute("entryExcellentStateDetail", entryExcellentStateDetail);
        return "system/performance/employee/entry-excellent-state-detail-edit";
    }

    /**
     * 修改评优材料
     *
     * @param entryExcellentStateDetail 评优材料实体类
     * @return 结果
     */
    @PostMapping("update")
    @ResponseBody
    @RequiredLog("修改评优材料")
    public SysResult update(EntryExcellentStateDetail entryExcellentStateDetail, HttpServletRequest request) {
        if (!(entryExcellentStateDetail.getStatus().trim().equals(PerformanceConstant.WAIT_SUBMIT_AUDIT))) {
            return SysResult.build(500, String.format("不能修改【%s】状态的总结评优材料", entryExcellentStateDetail.getStatus()));
        }
        String mainIds = request.getParameter("mainIds");
        String nextIds = request.getParameter("nextIds");
        boolean result = excellentService.updateExcellent(entryExcellentStateDetail, mainIds, nextIds);
        if (result) {
            return SysResult.ok();
        }
        return SysResult.build(500, "修改失败");
    }

    /**
     * 绩效科审核评优材料返回页面
     *
     * @param model
     * @return
     */
    @GetMapping("indexPerformance")
    @RequiredLog("绩效科审核评优材料返回页面")
    @RequiresPermissions("system:performance:entryExcellentStateDetail:indexPerformance")
    public String showIndexPerformance(Model model) {
        Map<String, Object> departmentParams = new HashMap<>();
        List<Map<String, Object>> departmentNameList = departmentService.findDepartmentsSelectedDataListByParams(departmentParams, new ArrayList<>());
        model.addAttribute("departmentNameList", departmentNameList);
        Map<String, Object> roleParams = new HashMap<>();
        List<Map<String, Object>> roleNameList = roleService.findRoleSelectedDataListByParams(roleParams, new ArrayList<>());
        model.addAttribute("roleNameList", roleNameList);
        return "system/performance/employee/entry-excellent-state-detail-performance-list";
    }

    /**
     * 经营管理部主管审核评优材料返回页面
     *
     * @param model
     * @return
     */
    @GetMapping("indexMinisterReview")
    @RequiredLog("经营管理部主管审核评优材料返回页面")
    @RequiresPermissions("system:performance:entryExcellentStateDetail:indexMinisterReview")
    public String showIndexMinisterReview(Model model) {
        Map<String, Object> departmentParams = new HashMap<>();
        List<Map<String, Object>> departmentNameList = departmentService.findDepartmentsSelectedDataListByParams(departmentParams, new ArrayList<>());
        model.addAttribute("departmentNameList", departmentNameList);
        Map<String, Object> roleParams = new HashMap<>();
        List<Map<String, Object>> roleNameList = roleService.findRoleSelectedDataListByParams(roleParams, new ArrayList<>());
        model.addAttribute("roleNameList", roleNameList);
        return "system/performance/employee/entry-excellent-state-detail-minister-list";
    }

    /**
     * 经营管理部主管展示修改页面
     *
     * @param id    履职明细id
     * @param model 数据模型
     * @return 修改页面
     */
    @GetMapping("editMinister/{id}")
    @RequiredLog("经营管理部主管展示修改页面")
    public String showEditMinister(@PathVariable String id, Model model) {
        EntryExcellentStateDetail entryExcellentStateDetail = excellentService.getById(id);
        if (entryExcellentStateDetail.getStatus().equals(PerformanceConstant.CANCEL)) {
            throw new ServiceException("不能修改取消状态下的事件请单");
        }
        model.addAttribute("entryExcellentStateDetail", entryExcellentStateDetail);
        return "system/performance/employee/entry-excellent-state-detail-minister-edit";
    }

    /**
     * 绩效科审核评优材料（修改按钮）
     *
     * @param entryExcellentStateDetail 评优材料
     * @return 结果
     */
    @PostMapping("updateMinister")
    @ResponseBody
    @RequiredLog("绩效科审核评优材料（修改按钮）")
    public SysResult updateMinister(EntryExcellentStateDetail entryExcellentStateDetail) {
        boolean result = excellentService.updateMinister(entryExcellentStateDetail);
        if (result) {
            return SysResult.ok();
        }
        return SysResult.build(500, "履职明细更新失败");
    }

    /**
     * 绩效科撤销审核评优材料
     *
     * @param excellentStateDetailIds
     * @return
     */
    @RequiredLog("绩效科撤销审核评优材料")
    @PostMapping("MinisterRetreat")
    @ResponseBody
    public SysResult ministerRetreat(@RequestParam(value = "excellentStateDetailIds[]") List<String> excellentStateDetailIds) {
        boolean result = excellentService.MinisterRetreat(excellentStateDetailIds);
        if (result) {
            return SysResult.ok();
        }
        return SysResult.build(500, "撤销审核失败");
    }

    /**
     * 展示评优材料上传页面
     *
     * @param id 评优材料id
     * @return 修改页面
     */
    @GetMapping("createAndUpload/{id}")
    @RequiresPermissions("system:performance:entryExcellentStateDetail:createAndUpload")
    @RequiredLog("展示评优材料上传页面")
    public String showExcellentPage(@PathVariable String id, Model model) {
        EntryCasReviewDetail entryCasReviewDetail = entryCasReviewDetailService.getById(id);
        EventList eventList = eventListService.getById(entryCasReviewDetail.getEventsId());
        // 查询事件清单
        if (!(entryCasReviewDetail.getEventsFirstType().equals(PerformanceConstant.EVENT_FIRST_TYPE_NEW_EVENT))) {
            if (ObjectUtils.isEmpty(eventList)) {
                throw new ServiceException("不存在对应的基础事件,请联系管理员");
            }
        } else {
            if (ObjectUtils.isEmpty(eventList)) {
                eventList = new EventList();
            }
        }
        // 查询履职计划
        EntryCasPlanDetail entryCasPlanDetail = entryCasPlanDetailService.getById(entryCasReviewDetail.getCasPlanId());
        // 履职计划判空
        if (!(entryCasReviewDetail.getEventsFirstType().equals(PerformanceConstant.EVENT_FIRST_TYPE_NEW_EVENT))) {
            if (ObjectUtils.isEmpty(entryCasPlanDetail)) {
                throw new ServiceException("不存在对应的履职计划,请联系管理员");
            }
        }
        // 查询用户
        Map<String, Object> params = new HashMap<>();
        List<Map<String, Object>> userNameList = userService.findUserSelectedDataListByParams(params, new ArrayList<>());
        model.addAttribute("eventList", eventList);
        model.addAttribute("entryCasPlanDetail", entryCasPlanDetail);
        model.addAttribute("entryCasReviewDetail", entryCasReviewDetail);
        model.addAttribute("userNameList", userNameList);
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        String importDateStart = sdf.format(date);
        model.addAttribute("importDateStart", importDateStart);
        // 查询导入场景
        params.put("noteEq", "评优材料上传");
        List<ImportModule> importModules = importModuleService
                .findImportModuleDataListByParams(params);
        if (ObjectUtils.isEmpty(importModules)) {
            throw new ServiceException(String.format("描述为【%s】的导入场景未维护，不允许查询", "项目月度进展表"));
        }
        model.addAttribute("module", importModules.get(0).getId());
        return "system/performance/employee/entry-excellent-state-detail-createAndUpload";
    }


    /**
     * 菜单跳转待评优的履职总结
     *
     * @param model 数据模型
     */
    @GetMapping("waitUploadList")
    @RequiredLog("菜单跳转待评优的履职总结")
    public String waitUploadList(Model model) {
        // 部门下拉选择
        Map<String, Object> departmentParams = new HashMap<>();
        List<Map<String, Object>> departmentNameList = departmentService.findDepartmentsSelectedDataListByParams(departmentParams, new ArrayList<>());
        model.addAttribute("departmentNameList", departmentNameList);
        Map<String, Object> roleParams = new HashMap<>();
        List<Map<String, Object>> roleNameList = roleService.findRoleSelectedDataListByParams(roleParams, new ArrayList<>());
        model.addAttribute("roleNameList", roleNameList);
        Map<String, Object> userParams = new HashMap<>();
        List<String> selectedUserIds = new ArrayList<>();
        List<Map<String, Object>> userList = userService.findUserSelectedDataListByParams(userParams, selectedUserIds);
        model.addAttribute("userList", userList);
        return "system/performance/employee/entry-review-detail-wait-upload-list";
    }

    /**
     * 多附件上传
     *
     * @param file       前台传来的附件数据
     * @param attachment 附件表实体类
     * @return 附件ID
     */
    @PostMapping("uploadFile")
    @RequiredLog("评优说明材料多附件上传")
    @ResponseBody
    public SysResult uploadFile(@RequestParam("file") MultipartFile file, Attachment attachment,
                                HttpServletRequest request) {
        // 保存附件
        Calendar calendar = Calendar.getInstance();
        attachment.setImportDate(calendar.getTime());// 设置时间
        // 查询导入场景对象
        ImportModule module = importModuleService.findById(InstandTool.integerToLong(attachment.getModule()));
        if (ObjectUtils.isEmpty(module)) {
            throw new ServiceException(String.format("序号为【%s】的导入场景未维护，不允许导入", attachment.getModule()));
        }
        Attachment result = fileAttachmentTool.storeFileToModule(file, module, attachment);
        return SysResult.ok(result.getId());
    }

    /**
     * 创建履职评价说明明细
     *
     * @param entryExcellentStateDetail 履职评价说明明细
     * @return 结果
     */
    @PostMapping("save")
    @ResponseBody
    @RequiredLog("创建履职评价说明明细")
    public SysResult create(EntryExcellentStateDetail entryExcellentStateDetail, HttpServletRequest request) {
        if (!(entryExcellentStateDetail.getStatus().trim().equals(PerformanceConstant.WAIT_SUBMIT_EXCELLENT))) {
            return SysResult.build(500, String.format("只能提交【%s】状态的总结评优材料", entryExcellentStateDetail.getStatus()));
        }
        Map<String, Object> param = new HashMap<>();
        String attachmentId = request.getParameter("attachmentId");
        String mainIds = request.getParameter("mainIds");
        String nextIds = request.getParameter("nextIds");
        if (!ObjectUtils.isEmpty(attachmentId)) {
            param.put("attachmentId", attachmentId);
        }
        param.put("mainIds", mainIds);
        param.put("nextIds", nextIds);
        boolean result = excellentService.save(entryExcellentStateDetail, param);
        if (result) {
            return SysResult.ok();
        }
        return SysResult.build(500, "提交失败");
    }

    /**
     * 绩效科评优材料批量审核
     *
     * @param excellentStateDetailIds 评优材料ids
     * @param request                 请求
     * @return 审核结果
     */
    @PostMapping("batchAudit")
    @ResponseBody
    @RequiredLog("绩效科评优材料批量审核")
    public SysResult batchAudit(HttpServletRequest request,
                                @RequestParam(value = "excellentStateDetailIds[]") List<String> excellentStateDetailIds,
                                @RequestParam(value = "auditNotes[]") List<String> auditNotes) {
        System.out.println("auditNotes = " + auditNotes);
        String classReview = request.getParameter("classReview");
        boolean result = excellentService.batchAudit(excellentStateDetailIds, classReview, auditNotes);
        if (result) {
            return SysResult.ok();
        }
        return SysResult.build(500, "批量审核失败");
    }

    /**
     * 经营管理部主管评优材料批量审核
     *
     * @param excellentStateDetailIds 评优材料ids
     * @param request                 请求
     * @return 结果
     */
    @PostMapping("MinsterBatchAudit")
    @ResponseBody
    @RequiredLog("经营管理部主管评优材料批量审核")
    public SysResult batchAuditByMinister(HttpServletRequest request,
                                          @RequestParam(value = "excellentStateDetailIds[]") List<String> excellentStateDetailIds,
                                          @RequestParam(value = "auditNotes[]") List<String> auditNotes) {
        String ministerReview = request.getParameter("ministerReview");
        boolean result = excellentService.MinisterBatchAudit(excellentStateDetailIds, ministerReview, auditNotes);
        if (result) {
            return SysResult.ok();
        }
        return SysResult.build(500, "批量审核失败");
    }
}
