package com.fssy.shareholder.management.controller.manage.system;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fssy.shareholder.management.annotation.RequiredLog;
import com.fssy.shareholder.management.pojo.common.SysResult;
import com.fssy.shareholder.management.pojo.manage.system.System;
import com.fssy.shareholder.management.service.manage.system.SystemService;

/**
 * 系统功能控制器
 *
 * @author Solomon
 */
@Controller
@RequestMapping("/manage/system/")
public class SystemController
{
    private static final Logger LOGGER = LoggerFactory.getLogger(SystemController.class);

    @Autowired
    private SystemService systemService;

    /**
     * 返回权限列表页面
     *
     * @return
     */
	@RequiredLog("分系统管理")
    @GetMapping("index")
    @RequiresPermissions("manage:system:index")
    public String systemIndex()
    {
        return "manage/system/system-list";
    }

    /**
     * 返回权限列表页面数据
     *
     * @param request 请求实体
     * @return
     */
    @GetMapping("getObjects")
    @ResponseBody
    public List<System> getSystemDatas()
    {
        List<System> systems = systemService.findSystemDataListByParams(new HashMap<>());
        return systems;
    }

    /**
     * 返回创建页面
     *
     * @param request 请求实体
     * @return
     */
	@RequiredLog("创建分系统")
	@RequiresPermissions("manage:system:create")
    @GetMapping("create")
    public String createView(HttpServletRequest request)
    {
        return "manage/system/system-create";
    }

    /**
     * 创建系统功能
     *
     * @param system 系统功能实体
     * @return
     */
	@RequiredLog("添加分系统操作")
	@RequiresPermissions("manage:system:store")
	@PostMapping("store")
    @ResponseBody
    public SysResult store(System system)
    {
        System result = systemService.saveSystem(system);
        LOGGER.info(result.toString());
        return SysResult.ok();
    }

    /**
     * 返回修改页面
     *
     * @param request 请求实体
     * @param model   视图模型
     * @return
     */
	@RequiredLog("修改分系统")
	@RequiresPermissions("manage:system:edit")
	@GetMapping("edit")
    public String editView(HttpServletRequest request, Model model)
    {
        // 获取系统id
        String id = request.getParameter("id");
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        // 查询系统功能信息
        System system = systemService.findSystemDataListByParams(params).get(0);
        LOGGER.info("system name :" + system.getName());
        // 查询父级系统名称
        params.put("id", system.getParent());
        List<System> parentSystems = systemService.findSystemDataListByParams(params);
        System parentSystem = new System();
        if (!parentSystems.isEmpty())
        {
            parentSystem = parentSystems.get(0);
        }
        model.addAttribute("system", system);
        model.addAttribute("parentSystem", parentSystem);
        return "manage/system/system-edit";
    }

    /**
     * 修改系统功能
     *
     * @param system 系统功能实体
     * @return
     */
	@RequiredLog("修改分系统操作")
	@RequiresPermissions("manage:system:update")
	@PutMapping("update")
    @ResponseBody
    public SysResult update(System system)
    {
        boolean result = systemService.updateSystem(system);
        if (result)
        {
            return SysResult.ok();
        }
        return SysResult.build(500, "系统功能没有更新，请检查数据后重新尝试");
    }

    /**
     * 删除系统功能
     *
     * @param id 系统功能表主键
     * @return
     */
	@RequiredLog("删除分系统操作")
	@RequiresPermissions("manage:system:destroy")
	@DeleteMapping("{id}")
    @ResponseBody
    public SysResult destroy(@PathVariable(value = "id") Integer id)
    {
        boolean result = systemService.deleteSystemById(id);
        if (result)
        {
            return SysResult.ok();
        }
        return SysResult.build(500, "系统功能没有删除，请确认操作后重新尝试");
    }
}
