package com.fssy.shareholder.management.service.system.company;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fssy.shareholder.management.pojo.system.config.Attachment;
import com.fssy.shareholder.management.pojo.system.company.FinanceData;

import java.util.Map;

/**
 * <p>
 * **数据表名：	bs_operate_company_finance_data	**数据表中文名：	企业财务基础数据(暂用于非权益投资管理)	**业务部门：	经营管理部	**数据表作用：	存放企业财务基础信息，方便在非权益投资页面计算。暂用于人工导入，未来采用数据对接	**创建人创建日期：	TerryZeng 2022-12-2 服务类
 * </p>
 *
 * @author 农浩
 * @since 2022-12-07
 */
public interface FinanceDataService extends IService<FinanceData> {
    /**
     * 根据参数分页查询数据
     * @param params 查询参数
     * @return 数据分页
     */
    Page<FinanceData> findDataListByParams(Map<String, Object> params);
    /**
     * 读取附件数据到数据库表
     * @param attachment 附件
     * @return 附件
     */

    /**
     * 新增单条企业财务基础数据
     * @param financeData  企业财务基础数据
     * @return 成功
     */
    boolean insertFinanceData(FinanceData financeData);

    Map<String, Object> readFinanceDataDataSource(Attachment attachment);
}
