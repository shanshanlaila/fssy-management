/**
 * ------------------------修改日志---------------------------------
 * 修改人       修改日期         修改内容
 */
package com.fssy.shareholder.management.service.manage.impl.company;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fssy.shareholder.management.mapper.manage.company.CompanyMapper;
import com.fssy.shareholder.management.pojo.manage.company.Company;
import com.fssy.shareholder.management.service.manage.company.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Shizn
 * @ClassName: CompanyServiceImpl
 * @Description: 公司名称业务实现类
 * @date 2022/11/17 0017 11:17
 */
@Service
public class CompanyServiceImpl extends ServiceImpl<CompanyMapper, Company> implements CompanyService {

    @Autowired
    private CompanyMapper companyMapper;

    /**
     * 获取组织结构列表用于xm-select插件
     *
     * @param params
     * @param selectedIds
     * @return
     */
    @Override
    public List<Map<String, Object>> findCompanySelectedDataListByParams(Map<String, Object> params, List<String> selectedIds) {
        QueryWrapper<Company> queryWrapper = getQueryWrapper(params);
        List<Company> companyNameList = companyMapper.selectList(queryWrapper);
        List<Map<String, Object>> resultList = new ArrayList<>();
        // 为选取的用户角色添加selected属性
        for (Company company : companyNameList) {
            Map<String, Object> result = new HashMap<String, Object>();
            result.put("name", company.getShortName());
            result.put("value", company.getId());
            result.put("id", company.getId());
            boolean selected = false;
            for (int i = 0; i < selectedIds.size(); i++) {
                if (selectedIds.get(i).equals(company.getId().toString())) {
                    selected = true;
                }
            }
            result.put("selected", selected);
            resultList.add(result);
        }
        return resultList;
    }

    @Override
    public Page<Company> findDataListByParams(Map<String, Object> params) {
        QueryWrapper<Company> queryWrapper = getQueryWrapper(params).orderByDesc("id");
        Page<Company> myPage = new Page<>((int) params.get("page"), (int) params.get("limit"));
        return companyMapper.selectPage(myPage, queryWrapper);
    }

    private QueryWrapper<Company> getQueryWrapper(Map<String, Object> params) {
        QueryWrapper<Company> queryWrapper = new QueryWrapper<>();
        // 添加查询条件
        if (params.containsKey("id")) {
            queryWrapper.eq("id", params.get("id"));
        }
        if (params.containsKey("name")) {
            queryWrapper.like("name", params.get("name"));
        }
        if (params.containsKey("shortName")) {
            queryWrapper.like("shortName", params.get("shortName"));
        }
        if (params.containsKey("companyId")) {
            queryWrapper.eq("companyId", params.get("companyId"));
        }
        return queryWrapper;

    }
}