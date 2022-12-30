package com.fssy.shareholder.management.service.system.operate.company;

import com.fssy.shareholder.management.pojo.system.operate.company.CompanyProfile;
import com.baomidou.mybatisplus.extension.service.IService;

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
public interface CompanyProfileService extends IService<CompanyProfile> {
    /**
     *  获取组织结构列表用于xm-select插件
     * @param params
     * @param selectedIds
     * @return
     */
    List<Map<String, Object>> findCompanyProfileSelectedDataListByParams(Map<String, Object> params, List<String> selectedIds);

}
