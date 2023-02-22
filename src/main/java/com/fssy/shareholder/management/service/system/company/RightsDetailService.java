package com.fssy.shareholder.management.service.system.company;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.pojo.system.company.ContributionsDetail;
import com.fssy.shareholder.management.pojo.system.company.RightsDetail;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 基础	股权明细 服务类
 * </p>
 *
 * @author Solomon
 * @since 2023-02-15
 */
public interface RightsDetailService extends IService<RightsDetail> {


    /**
     * 通过查询条件  查询数据
     * @param params
     * @return
     */
    List<RightsDetail> findRightsDetailDataByParams(Map<String,Object> params);


    /**
     * 分页查询条件 分页 查询列表
     * @param params
     * @return
     */
    Page<Map<String, Object>> findRightsDetailDataListPerPageByParams(Map<String, Object> params);


    /**
     * 通过查询条件 查询数据 用于数据导出
     * @param params
     * @return
     */
    List<RightsDetail> findRightslDataByParams(Map<String,Object> params);


    /**
     * 通过主键删除用户信息
     * @param id
     * @return
     */
    public boolean delectRightsDetailDataById(Integer id);


    /**
     * 修改 基础	股权明细 服务类信息
     * @param rightsDetail
     * @return
     */
    public boolean updateRightsDetailData(RightsDetail rightsDetail, HttpServletRequest request);


    /**
     * 插入 基础	股权明细 服务类信息
     * @param rightsDetail
     * @return
     */
    public boolean insertRightsDetailData(RightsDetail rightsDetail,HttpServletRequest request);


    public boolean submitUploadFile(RightsDetail rightsDetail, Map<String, Object> param);

}
