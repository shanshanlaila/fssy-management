package com.fssy.shareholder.management.service.system.impl.company;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.pojo.system.company.RightsDetail;
import com.fssy.shareholder.management.mapper.system.company.RightsDetailMapper;
import com.fssy.shareholder.management.pojo.system.operate.invest.TechCapacityEvaluate;
import com.fssy.shareholder.management.service.system.company.RightsDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 基础	股权明细 服务实现类
 * </p>
 *
 * @author Solomon
 * @since 2023-02-15
 */
@Service
public class RightsDetailServiceImpl extends ServiceImpl<RightsDetailMapper, RightsDetail> implements RightsDetailService {

    @Autowired
    private RightsDetailMapper rightsDetailMapper;

    //查询 基础	股权明细 服务实现类信息
    @Override
    public List<RightsDetail> findRightsDetailDataByParams(Map<String, Object> params) {
        QueryWrapper<RightsDetail> queryWrapper = getQueryWrapper(params);
        return rightsDetailMapper.selectList(queryWrapper);
    }

    //分页查询 基础股权明细 服务实现类信息
    @Override
    public Page<RightsDetail> findRightsDetailDataListPerPageByParams(Map<String, Object> params) {
        QueryWrapper<RightsDetail> queryWrapper = getQueryWrapper(params);
        int limit = (int) params.get("limit");
        int page = (int) params.get("page");
        Page<RightsDetail> mypage = new Page<>(page,limit);
        return rightsDetailMapper.selectPage(mypage,queryWrapper);
    }

    @Override
    public List<RightsDetail> findRightslDataByParams(Map<String, Object> params) {
        return null;
    }

    //通过主键删除 基础股权明细 服务实现类信息
    @Override
    public boolean delectRightsDetailDataById(Integer id) {
        QueryWrapper<RightsDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",id);
        RightsDetail rightsDetail = rightsDetailMapper.selectList(queryWrapper).get(0);
        String status = rightsDetail.getStatus();
        rightsDetail.setStatus("未使用");
        int result = rightsDetailMapper.updateById(rightsDetail);
        if (result>0){
            return true;
        }
        return false;
    }

    //修改基础股权信息
    @Override
    public boolean updateRightsDetailData(RightsDetail rightsDetail) {
        int result = rightsDetailMapper.updateById(rightsDetail);
        if (result>0){
            return true;
        }
        return false;
    }

    //添加单条企业基础股权信息
    @Override
    public boolean insertRightsDetailData(RightsDetail rightsDetail) {
        int result = rightsDetailMapper.insert(rightsDetail);
        if (result > 0){
            return true;
        }
        return false;
    }

    private QueryWrapper<RightsDetail> getQueryWrapper(Map<String,Object> params) {
        QueryWrapper<RightsDetail> queryWrapper = new QueryWrapper<>();
        if (params.containsKey("select")) {
            queryWrapper.select((String) params.get("select"));
        }
        if (params.containsKey("id")) {
            queryWrapper.eq("id", params.get("id"));
        }
        if (params.containsKey("stockRightsListId")) {
            queryWrapper.eq("stockRightsListId", params.get("stockRightsListId"));
        }
        if (params.containsKey("companyId")) {
            queryWrapper.eq("companyId", params.get("companyId"));
        }
        if (params.containsKey("companyName")) {
            queryWrapper.like("companyName", params.get("companyName"));
        }
        if (params.containsKey("stockRightsDocId")) {
            queryWrapper.eq("stockRightsDocId", params.get("stockRightsDocId"));
        }
        if (params.containsKey("stockRightsDocName")) {
            queryWrapper.like("stockRightsDocName", params.get("stockRightsDocName"));
        }
        if (params.containsKey("causeDesc")) {
            queryWrapper.like("causeDesc", params.get("causeDesc"));
        }
        if (params.containsKey("stockRightsType")) {
            queryWrapper.like("stockRightsType", params.get("stockRightsType"));
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
        if (params.containsKey("beforeRightsRatio")) {
            queryWrapper.like("beforeRightsRatio", params.get("beforeRightsRatio"));
        }
        if (params.containsKey("rightsRatio")) {
            queryWrapper.like("rightsRatio", params.get("rightsRatio"));
        }
        if (params.containsKey("changeTime")) {
            queryWrapper.like("changeTime", params.get("changeTime"));
        }

        // 变更时间开始查询
        if (params.containsKey("changeTimeStart")) {
            queryWrapper.ge("changeTime", params.get("changeTimeStart"));
        }
        if (params.containsKey("changeTimeEnd")) {
            queryWrapper.le("changeTime", params.get("changeTimeEnd"));
        }

        if (params.containsKey("sign")) {
            queryWrapper.eq("sign", params.get("sign"));
        }
        if (params.containsKey("status")) {
            queryWrapper.like("status", params.get("status"));
        }

        return queryWrapper;
    }
}
