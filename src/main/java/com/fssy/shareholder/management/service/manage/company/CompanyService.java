/**
 * ------------------------修改日志---------------------------------
 * 修改人       修改日期         修改内容
 */
package com.fssy.shareholder.management.service.manage.company;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.pojo.manage.company.Company;

import java.util.List;
import java.util.Map;

/**
 * @author Shizn
 * @ClassName: CompanyService
 * @Description: 公司名称 业务接口类
 * @date 2022/11/17 0017 11:13
 */
public interface CompanyService {

    /**
     * 获取组织结构列表用于xm-select插件
     * @param params
     * @param selectedIds
     * @return
     */
    List<Map<String, Object>> findCompanySelectedDataListByParams(Map<String, Object> params, List<String> selectedIds);

    Page<Company> findDataListByParams(Map<String, Object> params);
}