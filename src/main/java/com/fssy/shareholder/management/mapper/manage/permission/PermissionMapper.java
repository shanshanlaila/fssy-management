package com.fssy.shareholder.management.mapper.manage.permission;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fssy.shareholder.management.pojo.manage.permission.Permission;

/**
 * 权限数据访问层接口
 * @author Solomon
 */
public interface PermissionMapper extends BaseMapper<Permission>
{
	/**
	 * 根据权限id修改叶子结点属性（2021-10-15废弃）
	 * @param id 权限id
	 * @param leaf 是否为叶子结点
	 * @return
	 */
	@Update("update basic_permission set leaf=#{leaf},updatedAt=now() where id=#{id}")
	int updateLeafById(@Param("id")int id, @Param("leaf")int leaf);
}
