package com.fssy.shareholder.management.service.system.impl.operate.company;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fssy.shareholder.management.mapper.system.operate.company.CompanyProfileMapper;
import com.fssy.shareholder.management.pojo.system.operate.company.CompanyProfile;
import com.fssy.shareholder.management.service.system.operate.company.CompanyProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 部门：		时间：2022/7/15		表名：公司简介		表名：company_profile		用途：存储公司的基本信息和简介 服务实现类
 * </p>
 *
 * @author 农浩
 * @since 2022-12-13
 */
@Service
public class CompanyProfileServiceImpl extends ServiceImpl<CompanyProfileMapper, CompanyProfile> implements CompanyProfileService {
    @Autowired
    private CompanyProfileMapper companyProfileMapper;

    /**
     * 获取组织结构列表用于xm-select插件
     * @param params
     * @param selectedIds
     * @return
     */
    @Override
    public List<Map<String, Object>> findCompanyProfileSelectedDataListByParams(Map<String, Object> params, List<String> selectedIds) {
        QueryWrapper<CompanyProfile> queryWrapper = getQueryWrapper(params);
        List<CompanyProfile> companyNameList = companyProfileMapper.selectList(queryWrapper);
        List<Map<String, Object>> resultList = new ArrayList<>();
        // 为选取的用户角色添加selected属性
        for (CompanyProfile companyProfile : companyNameList) {
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("name", companyProfile.getCompanyShortName());
            result.put("value", companyProfile.getId());
            result.put("id", companyProfile.getId());
            boolean selected = false;
            for (int i = 0; i < selectedIds.size(); i++) {
                if (selectedIds.get(i).equals(companyProfile.getId().toString())) {
                    selected = true;
                }
            }
            result.put("selected", selected);
            resultList.add(result);
        }
        return resultList;
    }

    private QueryWrapper<CompanyProfile> getQueryWrapper(Map<String, Object> params) {
        QueryWrapper<CompanyProfile> queryWrapper = new QueryWrapper<>();
        // 添加查询条件
        if (params.containsKey("id")) {
            queryWrapper.eq("id", params.get("id"));
        }
        if (params.containsKey("companyId")) {
            queryWrapper.eq("companyId", params.get("companyId"));
        }
        if (params.containsKey("companyCode")) {
            queryWrapper.eq("companyCode", params.get("companyCode"));
        }
        if (params.containsKey("companyName")) {
            queryWrapper.like("companyName", params.get("companyName"));
        }
        if (params.containsKey("companyShortName")) {
            queryWrapper.like("companyShortName", params.get("companyShortName"));
        }
        return queryWrapper;

    }
}
