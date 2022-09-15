package com.fssy.shareholder.management.mapper.manage.common;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface MyBaseMapper<T> extends BaseMapper<T>
{
	/**
	 * 批量插入 仅适用于mysql
	 *
	 * @param entityList 实体列表
	 * @return 影响行数
	 */
	Integer insertBatchSomeColumn(List<T> entityList);
}
