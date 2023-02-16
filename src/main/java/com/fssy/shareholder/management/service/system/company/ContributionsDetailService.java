package com.fssy.shareholder.management.service.system.company;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.pojo.system.company.ContributionsDetail;
import com.baomidou.mybatisplus.extension.service.IService;
import org.checkerframework.checker.units.qual.C;

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
    Page<ContributionsDetail> findContributionsDetailPerPageByParams(Map<String, Object> params);


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
    public boolean updateContributionsDetailData(ContributionsDetail contributionsDetail);


    /**
     * 增加 基础 出资方明细
     * @param contributionsDetail
     * @return
     */
    public boolean insertContributionsDetailData(ContributionsDetail contributionsDetail);
}
