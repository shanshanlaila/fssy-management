package com.fssy.shareholder.management.service.system.operate.analysis;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fssy.shareholder.management.pojo.system.operate.analysis.ProfitStatement;

/**
 * <p>
 * *****业务部门： 经营分析科 *****数据表中文名： 利润表 *****数据表名： bs_operate_profit_statement
 * *****数据表作用： 各企业公司的利润表，以每月出的财务报表为基础 *****变更记录： 时间 变更人 变更内容 20221213 兰宇铧 初始设计
 * 服务类
 * </p>
 *
 * @author Solomon
 * @since 2022-12-13
 */
public interface ProfitStatementService extends IService<ProfitStatement>
{
	/**
	 * 根据参数分页查询利润表数据列表
	 *
	 * @param params 查询参数
	 * @return 数据分页
	 */
	List<ProfitStatement> findProfitStatementDataByParams(Map<String, Object> params);

	/**
	 * 通过条件查询分页利润表数据列表
	 *
	 * @param params 参数
	 * @return 分页数据
	 */
	Page<ProfitStatement> findProfitStatementDataPerPageByParams(Map<String, Object> params);

	/**
	 * 通过查询条件查询利润表数据列表(Map)
	 *
	 * @param params 查询条件
	 * @return 利润表数据列表
	 */
	List<Map<String, Object>> findProfitStatementMapDataByParams(Map<String, Object> params);

	/**
	 * 根据参数分页查询利润表数据(map)
	 *
	 * @param params 查询参数
	 * @return 数据分页
	 */
	Page<Map<String, Object>> findProfitStatementMapPerPageByParams(Map<String, Object> params);
	
	/**
	 * 人工操作接收对接系统数据
	 * 
	 * @param params 参数
	 * @return
	 */
	Map<String, Object> receiveDataByArtificial(Map<String, Object> params);

	/**
	 * 接收对接系统数据
	 * 
	 * @param params 参数
	 * @return
	 */
	Map<String, Object> receiveData(Map<String, Object> params);
}
