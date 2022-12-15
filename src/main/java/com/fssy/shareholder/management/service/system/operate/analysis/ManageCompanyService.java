package com.fssy.shareholder.management.service.system.operate.analysis;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fssy.shareholder.management.pojo.system.operate.analysis.ManageCompany;

/**
 * <p>
 * *****业务部门： 经营分析科 *****数据表中文名： 授权访问的公司代码列表 *****数据表名：
 * bs_operate_manage_company *****数据表作用： 经营分析可以授权访问的公司列表 *****变更记录： 时间 变更人 变更内容
 * 20221213 兰宇铧 初始设计 服务类
 * </p>
 *
 * @author Solomon
 * @since 2022-12-13
 */
public interface ManageCompanyService extends IService<ManageCompany>
{
	/**
	 * 根据参数分页查询经营分析公司数据列表
	 *
	 * @param params 查询参数
	 * @return 数据分页
	 */
	List<ManageCompany> findManageCompanyDataByParams(Map<String, Object> params);

	/**
	 * 通过条件查询分页经营分析公司数据列表
	 *
	 * @param params 参数
	 * @return 分页数据
	 */
	Page<ManageCompany> findManageCompanyDataPerPageByParams(Map<String, Object> params);

	/**
	 * 通过查询条件查询经营分析公司数据列表(Map)
	 *
	 * @param params 查询条件
	 * @return 经营分析公司数据列表
	 */
	List<Map<String, Object>> findManageCompanyMapDataByParams(Map<String, Object> params);

	/**
	 * 根据参数分页查询经营分析公司数据(map)
	 *
	 * @param params 查询参数
	 * @return 数据分页
	 */
	Page<Map<String, Object>> findManageCompanyMapPerPageByParams(Map<String, Object> params);

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

	/**
	 * 通过查询条件查询用于xm-select控件的数据
	 * 
	 * @param params      查询条件
	 * @param selectedIds 已选择项主键
	 * @return xm-select控件使用数据
	 */
	List<Map<String, Object>> findManageCompanySelectedDataListByParams(Map<String, Object> params,
			List<String> selectedIds);
}
