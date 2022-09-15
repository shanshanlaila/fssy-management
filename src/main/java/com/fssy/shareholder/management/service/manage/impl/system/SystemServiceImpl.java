package com.fssy.shareholder.management.service.manage.impl.system;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.fssy.shareholder.management.mapper.manage.system.SystemMapper;
import com.fssy.shareholder.management.pojo.manage.system.System;
import com.fssy.shareholder.management.pojo.manage.user.User;
import com.fssy.shareholder.management.service.manage.system.SystemService;
import com.fssy.shareholder.management.tools.constant.CommonConstant;
import com.fssy.shareholder.management.tools.exception.ServiceException;

/**
 * 系统功能业务实现类
 * 
 * @author Solomon
 */
@Service
public class SystemServiceImpl implements SystemService
{
	@Autowired
	private SystemMapper systemMapper;

	@Override
	@Transactional
	public System saveSystem(System system)
	{
		// 当前用户
		User user = (User) SecurityUtils.getSubject().getPrincipal();
		
		systemMapper.insert(system);
		
		// 2.设置父节点数据的leaf字段
		UpdateWrapper<System> updateWrapper = new UpdateWrapper<>();
		updateWrapper.eq("id", system.getParent())
				.set("leaf", CommonConstant.FALSE)
				.set("updatedAt", LocalDateTime.now())
				.set("updatedId", user.getId())
				.set("updatedName", user.getName());
		systemMapper.update(null, updateWrapper);
		return system;
	}

	@Override
	@Transactional
	public boolean deleteSystemById(int id)
	{
		// 业务判断
		// 删除项存在子项时不允许删除
		QueryWrapper<System> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("parent", id);
		if (systemMapper.selectCount(queryWrapper) > 0)
		{
			throw new ServiceException("该权限存在子项，需要先对子项进行操作");
		}
		
		// 当前用户
		User user = (User) SecurityUtils.getSubject().getPrincipal();

		// 删除项父项只有一个子项，需要修改父项的leaf属性
		System deleteSystem = systemMapper.selectById(id);
		QueryWrapper<System> allChildMenuQueryWrapper = new QueryWrapper<>();
		allChildMenuQueryWrapper.eq("parent", deleteSystem.getParent());
		if (systemMapper.selectCount(allChildMenuQueryWrapper) == 1)
		{
			UpdateWrapper<System> updateWrapper = new UpdateWrapper<>();
			updateWrapper.eq("id", deleteSystem.getParent())
					.set("leaf", CommonConstant.TRUE)
					.set("updatedAt", LocalDateTime.now())
					.set("updatedId", user.getId())
					.set("updatedName", user.getName());
			systemMapper.update(null, updateWrapper);
		}

		int num = systemMapper.deleteById(id);

		if (num > 0)
		{
			return true;
		}
		return false;
	}

	@Override
	public List<System> findSystemDataListByParams(Map<String, Object> params)
	{
		QueryWrapper<System> queryWrapper = new QueryWrapper<>();
		// 添加查询条件
		if (params.containsKey("id"))
		{
			queryWrapper.eq("id", params.get("id"));
		}
		if (params.containsKey("name"))
		{
			queryWrapper.like("name", params.get("name"));
		}
		// 使用mybatis-plus插件自动生成查询语句
		List<System> systems = systemMapper.selectList(queryWrapper);
		return systems;
	}

	@Override
	@Transactional
	public boolean updateSystem(System system)
	{
		// 业务校验
		// 修改项存在子项时不允许修改
		QueryWrapper<System> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("parent", system.getId());
		if (systemMapper.selectCount(queryWrapper) > 0)
		{
			throw new ServiceException("该权限存在子项，需要先对子项进行操作");
		}
		
		// 当前用户
		User user = (User) SecurityUtils.getSubject().getPrincipal();

		// 判断是否修改原来的父节点权限的leaf
		// 条件为修改前后的父节点不同，并且原父节点只有一个子项
		QueryWrapper<System> oriSystemQueryWrapper = new QueryWrapper<>();
		oriSystemQueryWrapper.eq("id", system.getId());
		System oriSystem = systemMapper.selectOne(oriSystemQueryWrapper);
		QueryWrapper<System> oriChildPermissionQueryWrapper = new QueryWrapper<>();
		oriChildPermissionQueryWrapper.eq("parent", system.getParent());
		UpdateWrapper<System> updateWrapper = null;
		if (oriSystem.getParent() != system.getParent()
				&& systemMapper.selectCount(oriChildPermissionQueryWrapper) == 1)
		{
			updateWrapper = new UpdateWrapper<>();
			updateWrapper.eq("id", oriSystem.getParent())
					.set("leaf", CommonConstant.TRUE)
					.set("updatedAt", LocalDateTime.now())
					.set("updatedId", user.getId())
					.set("updatedName", user.getName());
			systemMapper.update(null, updateWrapper);
		}

		// 修改新的父节点为权限的leaf
		updateWrapper = new UpdateWrapper<>();
		updateWrapper.eq("id", oriSystem.getParent())
				.set("leaf", CommonConstant.FALSE)
				.set("updatedAt", LocalDateTime.now())
				.set("updatedId", user.getId())
				.set("updatedName", user.getName());
		systemMapper.update(null, updateWrapper);

		int num = systemMapper.updateById(system);
		if (num > 0)
		{
			return true;
		}
		return false;
	}

}
