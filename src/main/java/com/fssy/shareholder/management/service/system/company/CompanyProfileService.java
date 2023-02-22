package com.fssy.shareholder.management.service.system.company;

import com.fssy.shareholder.management.pojo.system.company.CompanyProfile;
import com.fssy.shareholder.management.service.system.performance.employee.BaseService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 部门：		时间：2022/7/15		表名：公司简介		表名：company_profile		用途：存储公司的基本信息和简介 服务类
 * </p>
 *
 * @author 农浩
 * @since 2022-12-13
 */
public interface CompanyProfileService extends BaseService<CompanyProfile> {
    /**
     *  获取组织结构列表用于xm-select插件
     * @param params
     * @param selectedIds
     * @return
     */
    List<Map<String, Object>> findCompanyProfileSelectedDataListByParams(Map<String, Object> params, List<String> selectedIds);

    boolean insertCompanyProfile(CompanyProfile companyProfile, HttpServletRequest request);
}
