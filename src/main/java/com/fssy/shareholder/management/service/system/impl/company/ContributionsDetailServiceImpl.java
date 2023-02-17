package com.fssy.shareholder.management.service.system.impl.company;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.pojo.system.company.ContributionsDetail;
import com.fssy.shareholder.management.mapper.system.company.ContributionsDetailMapper;
import com.fssy.shareholder.management.pojo.system.operate.invest.TechCapacityEvaluate;
import com.fssy.shareholder.management.service.system.company.ContributionsDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * <p>
 * 基础	出资方明细	 服务实现类
 * </p>
 *
 * @author Solomon
 * @since 2023-02-15
 */
@Service
public class ContributionsDetailServiceImpl extends ServiceImpl<ContributionsDetailMapper, ContributionsDetail> implements ContributionsDetailService {

    @Autowired
    private ContributionsDetailMapper contributionsDetailMapper;


    private QueryWrapper<ContributionsDetail> getQueryWrapper(Map<String,Object> params) {
        QueryWrapper<ContributionsDetail> queryWrapper = new QueryWrapper<>();
        if (params.containsKey("select")) {
            queryWrapper.select((String) params.get("select"));
        }
        if (params.containsKey("id")) {
            queryWrapper.eq("id", params.get("id"));
        }
        if (params.containsKey("contributionsListId")) {
            queryWrapper.eq("contributionsListId", params.get("contributionsListId"));
        }
        if (params.containsKey("companyId")) {
            queryWrapper.eq("companyId", params.get("companyId"));
        }
        if (params.containsKey("companyName")) {
            queryWrapper.like("companyName", params.get("companyName"));
        }
        if (params.containsKey("contributionsDocId")) {
            queryWrapper.eq("contributionsDocId", params.get("contributionsDocId"));
        }
        if (params.containsKey("contributionsDocName")) {
            queryWrapper.like("contributionsDocName", params.get("contributionsDocName"));
        }
        if (params.containsKey("causeDesc")) {
            queryWrapper.like("causeDesc", params.get("causeDesc"));
        }
        if (params.containsKey("investorType")) {
            queryWrapper.like("investorType", params.get("investorType"));
        }
        if (params.containsKey("investorId")) {
            queryWrapper.eq("investorId", params.get("investorId"));
        }
        if (params.containsKey("investorName")) {
            queryWrapper.like("investorName", params.get("investorName"));
        }
        if (params.containsKey("contributionsMode")) {
            queryWrapper.like("contributionsMode", params.get("contributionsMode"));
        }
        if (params.containsKey("registeredCapital")) {
            queryWrapper.eq("registeredCapital", params.get("registeredCapital"));
        }
        if (params.containsKey("contributedCapital")) {
            queryWrapper.like("contributedCapital", params.get("contributedCapital"));
        }

        // 计划到账时间查询
        if (params.containsKey("planArrivalTimeStart")) {
            queryWrapper.ge("planArrivalTime", params.get("planArrivalTimeStart"));
        }
        if (params.containsKey("planArrivalTimeEnd")) {
            queryWrapper.le("planArrivalTime", params.get("planArrivalTimeEnd"));
        }

        // 实际到账时间查询
        if (params.containsKey("actualArrivalTimeStart")) {
            queryWrapper.ge("actualArrivalTime", params.get("actualArrivalTimeStart"));
        }
        if (params.containsKey("actualArrivalTimeEnd")) {
            queryWrapper.le("actualArrivalTime", params.get("actualArrivalTimeEnd"));
        }


        if (params.containsKey("sign")) {
            queryWrapper.eq("sign", params.get("sign"));
        }

        return queryWrapper;
    }

    //分页查询基础 投资方信息
    @Override
    public Page<ContributionsDetail> findContributionsDetailPerPageByParams(Map<String, Object> params) {
        int limit = (int) params.get("limit");
        int page = (int) params.get("page");
        QueryWrapper<ContributionsDetail> queryWrapper = getQueryWrapper(params);
        Page<ContributionsDetail> mypage = new Page<>(page,limit);
        return contributionsDetailMapper.selectPage(mypage,queryWrapper);
    }

    //删除 基础 投资方信息
    @Override
    public boolean delectContributionsDetailByid(Integer id) {
        QueryWrapper<ContributionsDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",id);
        ContributionsDetail contributionsDetail = contributionsDetailMapper.selectList(queryWrapper).get(0);
        contributionsDetail.setStatus("未开业");
        int result = contributionsDetailMapper.updateById(contributionsDetail);
        if (result > 0){
            return true;
        }
        return false;
    }

    //修改基础 投资方信息
    @Override
    public boolean updateContributionsDetailData(ContributionsDetail contributionsDetail) {
        int result = contributionsDetailMapper.updateById(contributionsDetail);
        if (result > 0 ){
            return true;
        }
        return false;
    }

    //插入基础 投资方信息
    @Override
    public boolean insertContributionsDetailData(ContributionsDetail contributionsDetail) {
        int result = contributionsDetailMapper.insert(contributionsDetail);
        if (result > 0 ){
            return true;
        }
        return false;
    }
}
