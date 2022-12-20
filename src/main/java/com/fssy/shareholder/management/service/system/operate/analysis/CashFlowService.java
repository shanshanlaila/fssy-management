package com.fssy.shareholder.management.service.system.operate.analysis;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fssy.shareholder.management.pojo.system.operate.analysis.CashFlow;

/**
 * 现金流量功能业务接口
 *
 * @author Solomon
 * @since 2022-12-20
 */
public interface CashFlowService extends IService<CashFlow>
{
	/**
	 * 根据参数分页查询现金流量表数据列表
	 *
	 * @param params 查询参数
	 * @return 数据分页
	 */
	List<CashFlow> findCashFlowDataByParams(Map<String, Object> params);

	/**
	 * 通过条件查询分页现金流量表数据列表
	 *
	 * @param params 参数
	 * @return 分页数据
	 */
	Page<CashFlow> findCashFlowDataPerPageByParams(Map<String, Object> params);

	/**
	 * 通过查询条件查询现金流量表数据列表(Map)
	 *
	 * @param params 查询条件
	 * @return 现金流量表数据列表
	 */
	List<Map<String, Object>> findCashFlowMapDataByParams(Map<String, Object> params);

	/**
	 * 根据参数分页查询现金流量表数据(map)
	 *
	 * @param params 查询参数
	 * @return 数据分页
	 */
	Page<Map<String, Object>> findCashFlowMapPerPageByParams(Map<String, Object> params);

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
