package com.fssy.shareholder.management.service.system.impl.company;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.pojo.system.company.LicenseWarranty;
import com.fssy.shareholder.management.mapper.system.company.LicenseWarrantyMapper;
import com.fssy.shareholder.management.service.system.company.LicenseWarrantyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * <p>
 * 基础	营业执照	 服务实现类
 * </p>
 *
 * @author Solomon
 * @since 2023-02-16
 */
@Service
public class LicenseWarrantyServiceImpl extends ServiceImpl<LicenseWarrantyMapper, LicenseWarranty> implements LicenseWarrantyService {

    @Autowired
    private LicenseWarrantyMapper licenseWarrantyMapper;

    //分页查询企业营业执照信息
    @Override
    public Page<LicenseWarranty> findLicenseWarrantyPerPageByParams(Map<String, Object> params) {
        QueryWrapper<LicenseWarranty> queryWrapper = getQueryWrapper(params);
        Integer limit = (Integer) params.get("limit");
        Integer page = (Integer) params.get("page");
        Page<LicenseWarranty> mypage = new Page<>(page,limit);
        return licenseWarrantyMapper.selectPage(mypage,queryWrapper);
    }

    //通过主键删除 企业营业执照信息
    @Override
    public boolean delectLicenseWarrantyById(Integer id) {
        QueryWrapper<LicenseWarranty> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",id);
        LicenseWarranty licenseWarranty = licenseWarrantyMapper.selectList(queryWrapper).get(0);
        licenseWarranty.setStatus("未使用");
        int result = licenseWarrantyMapper.updateById(licenseWarranty);
        if (result > 0 ){
            return true;
        }
        return false;
    }

    //修改企业营业执照信息
    @Override
    public boolean updateLicenseWarrantyData(LicenseWarranty licenseWarranty) {
        int result = licenseWarrantyMapper.updateById(licenseWarranty);
        if (result > 0 ){
            return true;
        }
        return false;
    }

    //添加企业营业执照单条记录
    @Override
    public boolean insertLicenseWarrantyData(LicenseWarranty licenseWarranty) {
        int result = licenseWarrantyMapper.insert(licenseWarranty);
        if (result > 0 ){
            return true;
        }
        return false;
    }

    private QueryWrapper<LicenseWarranty> getQueryWrapper(Map<String, Object>params){
        QueryWrapper<LicenseWarranty> queryWrapper = new QueryWrapper<>();
        if (params.containsKey("select")) {
            queryWrapper.select((String) params.get("select"));
        }
        if (params.containsKey("id")) {
            queryWrapper.eq("id", params.get("id"));
        }
        if (params.containsKey("businessLicenseDocId")) {
            queryWrapper.eq("businessLicenseDocId", params.get("businessLicenseDocId"));
        }
        if (params.containsKey("companyId")) {
            queryWrapper.eq("companyId", params.get("companyId"));
        }
        if (params.containsKey("companyName")) {
            queryWrapper.like("companyName", params.get("companyName"));
        }
        if (params.containsKey("businessLicenseDocName")) {
            queryWrapper.like("businessLicenseDocName", params.get("businessLicenseDocName"));
        }

        // 更改时间查询
        if (params.containsKey("changeTimeStart")) {
            queryWrapper.ge("changeTime", params.get("changeTimeStart"));
        }
        if (params.containsKey("changeTimeEnd")) {
            queryWrapper.le("changeTime", params.get("changeTimeEnd"));
        }

        if (params.containsKey("changeProject")) {
            queryWrapper.like("changeProject", params.get("changeProject"));
        }
        if (params.containsKey("changeBefore")) {
            queryWrapper.like("changeBefore", params.get("changeBefore"));
        }
        if (params.containsKey("changeAfter")) {
            queryWrapper.like("changeAfter", params.get("changeAfter"));
        }
        return queryWrapper;
    }
}
