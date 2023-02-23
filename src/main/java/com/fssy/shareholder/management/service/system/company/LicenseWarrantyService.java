package com.fssy.shareholder.management.service.system.company;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.pojo.system.company.ContributionsDetail;
import com.fssy.shareholder.management.pojo.system.company.LicenseWarranty;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <p>
 * 基础	营业执照	 服务类
 * </p>
 *
 * @author Solomon
 * @since 2023-02-16
 */
public interface LicenseWarrantyService extends IService<LicenseWarranty> {

    /**
     * 分页查询条件 分页查询列表
     * @param params
     * @return
     */
    Page<Map<String,Object>> findLicenseWarrantyPerPageByParams(Map<String, Object>params);


    /**
     * 主键删除 基础 营业执照 信息
     * @param id
     * @return
     */
    public boolean delectLicenseWarrantyById(Integer id);

    /**
     * 修改 基础 营业执照信息
     * @param licenseWarranty
     * @return
     */
    public boolean updateLicenseWarrantyData(LicenseWarranty licenseWarranty, HttpServletRequest request);

    /**
     * 增加 基础 营业执照信息
     * @param licenseWarranty
     * @return
     */
    public boolean insertLicenseWarrantyData(LicenseWarranty licenseWarranty,HttpServletRequest request);


    public boolean submitUploadFile(LicenseWarranty licenseWarranty, Map<String, Object> param);
}
