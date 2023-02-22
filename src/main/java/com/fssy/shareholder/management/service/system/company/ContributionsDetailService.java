package com.fssy.shareholder.management.service.system.company;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.pojo.system.company.ContributionsDetail;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fssy.shareholder.management.pojo.system.operate.invest.InvestProjectList;
import org.checkerframework.checker.units.qual.C;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 基础	出资方明细	 服务类
 * </p>
 *
 * @author Solomon
 * @since 2023-02-15
 */
public interface ContributionsDetailService extends IService<ContributionsDetail> {


    /**
     * 分页查询条件 分页查询列表
     * @param params
     * @return
     */
    Page<Map<String, Object>> findContributionsDetailPerPageByParams(Map<String, Object> params);

//    List<Map<String, Object>> findContributionsDetailByParams(Map<String, Object>params ,List<String> selectedIds);


    /**
     * 主键删除 基础 出资方明细
     * @param id
     * @return
     */
    public boolean delectContributionsDetailByid(Integer id);


    /**
     * 修改 基础 出资方明细
     * @param contributionsDetail
     * @return
     */
    public boolean updateContributionsDetailData(ContributionsDetail contributionsDetail, HttpServletRequest request);


    /**
     * 增加 基础 出资方明细
     * @param contributionsDetail
     * @return
     */
    public boolean insertContributionsDetailData(ContributionsDetail contributionsDetail,HttpServletRequest request);


    public boolean submitUploadFile(ContributionsDetail contributionsDetail, Map<String, Object> param);

}
