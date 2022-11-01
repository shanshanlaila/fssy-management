package com.fssy.shareholder.management.service.system.impl.performance.manage_kpi;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.pojo.system.config.Attachment;
import com.fssy.shareholder.management.pojo.system.performance.manage_kpi.ManageKpiMonthAim;
import com.fssy.shareholder.management.mapper.system.performance.manage_kpi.ManageKpiMonthAimMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fssy.shareholder.management.service.common.SheetService;
import com.fssy.shareholder.management.service.system.performance.manage_kpi.ManageKpiMonthAimService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 经营管理月度指标数据表 服务实现类
 * </p>
 *
 * @author Shizn
 * @since 2022-10-24
 */
@Service
public class ManageKpiMonthAimServiceImpl extends ServiceImpl<ManageKpiMonthAimMapper, ManageKpiMonthAim> implements ManageKpiMonthAimService {

    @Autowired
    private SheetService sheetService;
    @Autowired
    private ManageKpiMonthAimMapper manageKpiMonthAimMapper;

    /**
     * 通过查询条件 分页查询列表
     *
     * @param params 查询条件
     * @return 分页数据
     */
    @Override
    public Page<ManageKpiMonthAim> findManageKpiMonthDataListPerPageByParams(Map<String, Object> params) {
        QueryWrapper<ManageKpiMonthAim> queryWrapper = getQueryWrapper(params);
        int limit = (int) params.get("limit");
        int page = (int) params.get("page");
        Page<ManageKpiMonthAim> myPage = new Page<>(page, limit);
        return manageKpiMonthAimMapper.selectPage(myPage, queryWrapper);
    }

    /**
     * 设置失败的内容
     *
     * @param result 结果map
     * @param append 导入失败的原因
     */
    private void setFailedContent(Map<String, Object> result, String append) {
        String content = result.get("content").toString();
        if (ObjectUtils.isEmpty(content)) {
            result.put("content", append);
        } else {
            result.put("content", content + "," + append);
        }
        result.put("failed", true);
    }

    /**
     * 读取附件数据到数据库表
     *
     * @param attachment 附件
     * @return 数据列表
     */
    @Override
//    @Transactional
    public Map<String, Object> readManageKpiMonthDataSource(Attachment attachment) {
        return null;
    }

    /**
     * 通过查询条件查询履职计划map数据， 用于导出
     *
     * @param params 查询条件
     * @return 数据列表
     */
    @Override
    public List<Map<String, Object>> findManageKpiMonthMapDataByParams(Map<String, Object> params) {
        QueryWrapper<ManageKpiMonthAim> queryWrapper = getQueryWrapper(params);

        return manageKpiMonthAimMapper.selectMaps(queryWrapper);
    }

    private QueryWrapper<ManageKpiMonthAim> getQueryWrapper(Map<String, Object> params) {
        QueryWrapper<ManageKpiMonthAim> queryWrapper = new QueryWrapper<>();
        if (params.containsKey("id")) {
            queryWrapper.eq("id", params.get("id"));
        }
        if (params.containsKey("select")) {
            queryWrapper.select((String) params.get("select"));
        }
        if (params.containsKey("companyName")) {
            queryWrapper.like("companyName", params.get("companyName"));
        }
        if (params.containsKey("projectType")) {
            queryWrapper.like("projectType", params.get("projectType"));
        }
        if (params.containsKey("year")) {
            queryWrapper.eq("year", params.get("year"));
        }
        if (params.containsKey("month")) {
            queryWrapper.eq("month", params.get("month"));
        }
        if (params.containsKey("projectDesc")) {
            queryWrapper.like("projectDesc", params.get("projectDesc"));
        }
        if (params.containsKey("dataSource")) {
            queryWrapper.like("dataSource", params.get("dataSource"));
        }
        if (params.containsKey("status")) {
            queryWrapper.eq("status", params.get("status"));
        }
        return queryWrapper;
    }
}
