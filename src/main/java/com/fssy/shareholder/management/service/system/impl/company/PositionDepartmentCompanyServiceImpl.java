package com.fssy.shareholder.management.service.system.impl.company;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fssy.shareholder.management.mapper.system.company.PositionDepartmentCompanyMapper;
import com.fssy.shareholder.management.pojo.system.company.PositionDepartmentCompany;
import com.fssy.shareholder.management.service.system.company.PositionDepartmentCompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * <p>
 * 基础-员工-职位-部门-公司表	 服务实现类
 * </p>
 *
 * @author 农浩
 * @since 2023-02-15
 */
@Service
public class PositionDepartmentCompanyServiceImpl extends ServiceImpl<PositionDepartmentCompanyMapper, PositionDepartmentCompany> implements PositionDepartmentCompanyService {
    @Autowired
    private PositionDepartmentCompanyMapper positionDepartmentCompanyMapper;

    @Override
    public Page<PositionDepartmentCompany> findDataListByParams(Map<String, Object> params) {
        QueryWrapper<PositionDepartmentCompany> queryWrapper = getQueryWrapper(params).orderByDesc("id");
        Page<PositionDepartmentCompany> myPage = new Page<>((int) params.get("page"), (int) params.get("limit"));
        return positionDepartmentCompanyMapper.selectPage(myPage, queryWrapper);
    }
    private QueryWrapper<PositionDepartmentCompany> getQueryWrapper(Map<String,Object> params) {
        QueryWrapper<PositionDepartmentCompany> queryWrapper = new QueryWrapper<>();
        if (params.containsKey("id")) {
            queryWrapper.eq("id",params.get("id"));
        }
        return queryWrapper;
    }
}
