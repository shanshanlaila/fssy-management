package com.fssy.shareholder.management.service.system.impl.operate.invest;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.pojo.system.operate.invest.TechCapacityEvaluateRes;
import com.fssy.shareholder.management.mapper.system.operate.invest.TechCapacityEvaluateResMapper;
import com.fssy.shareholder.management.service.system.operate.invest.TechCapacityEvaluateResService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * **数据表名：	bs_operat_tech_capacity_evaluate	**数据表中文名：	企业研发工艺能力评价表	**业务部门：	经营管理部	**数据表作用：	记录 企业研发工艺能力年度评价项目、存在问题及改善点	**创建人创建日期：	TerryZeng 2022-12-2 服务实现类
 * </p>
 *
 * @author Solomon
 * @since 2022-12-06
 */
@Service
public class TechCapacityEvaluateResServiceImpl extends ServiceImpl<TechCapacityEvaluateResMapper,TechCapacityEvaluateRes> implements TechCapacityEvaluateResService {

    @Autowired
    TechCapacityEvaluateResMapper techCapacityEvaluateResMapper;

    @Override
    public List<Map<String, Object>> findTechDataByParams(Map<String, Object> params) {
        QueryWrapper<TechCapacityEvaluateRes> queryWrapper = getQueryWrapper(params);
        return techCapacityEvaluateResMapper.selectMaps(queryWrapper);
    }

    @Override
    public Page<TechCapacityEvaluateRes> findTechCapacityEvaluateResDataListPerPageByParams(Map<String, Object> params) {
        QueryWrapper<TechCapacityEvaluateRes> queryWrapper = getQueryWrapper(params);
        int limit = (int) params.get("limit");
        int page = (int) params.get("page");
        Page<TechCapacityEvaluateRes> myPage = new Page<>(page, limit);
        return techCapacityEvaluateResMapper.selectPage(myPage, queryWrapper);

    }

    @Override
    public List<TechCapacityEvaluateRes> findTechCapacityEvaluateResDataByParams(Map<String, Object> params) {
        QueryWrapper<TechCapacityEvaluateRes> queryWrapper = getQueryWrapper(params);
        return techCapacityEvaluateResMapper.selectList(queryWrapper);

    }

    private QueryWrapper<TechCapacityEvaluateRes> getQueryWrapper(Map<String,Object> params){
        QueryWrapper<TechCapacityEvaluateRes> queryWrapper = new QueryWrapper<>();
        if (params.containsKey("select")) {
            queryWrapper.select((String) params.get("select"));
        }
        if (params.containsKey("id")) {
            queryWrapper.eq("id", params.get("id"));
        }
        if (params.containsKey("companyName")) {
            queryWrapper.like("companyName", params.get("companyName"));
        }
        if (params.containsKey("year")) {
            queryWrapper.eq("year", params.get("year"));
        }
        if (params.containsKey("companyId")) {
            queryWrapper.eq("companyId", params.get("companyId"));
        }
        if (params.containsKey("companyShortName")) {
            queryWrapper.like("companyShortName", params.get("companyShortName"));
        }

        if (params.containsKey("evalRes")) {
            queryWrapper.like("evalRes", params.get("evalRes"));
        }

        return queryWrapper;
    }

}
