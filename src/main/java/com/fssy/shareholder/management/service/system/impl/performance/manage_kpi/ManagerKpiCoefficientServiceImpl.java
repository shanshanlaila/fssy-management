package com.fssy.shareholder.management.service.system.impl.performance.manage_kpi;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fssy.shareholder.management.mapper.system.performance.manage_kpi.ManagerKpiCoefficientMapper;
import com.fssy.shareholder.management.pojo.system.config.Attachment;
import com.fssy.shareholder.management.pojo.system.performance.manage_kpi.ManageKpiYear;
import com.fssy.shareholder.management.pojo.system.performance.manage_kpi.ManagerKpiCoefficient;
import com.fssy.shareholder.management.service.system.performance.manage_kpi.ManagerKpiCoefficientService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 经理人绩效系数表 服务实现类
 * </p>
 *
 * @author zzp
 * @since 2022-11-07
 */
@Service
public class ManagerKpiCoefficientServiceImpl extends ServiceImpl<ManagerKpiCoefficientMapper, ManagerKpiCoefficient> implements ManagerKpiCoefficientService {

    @Override
    public Page<ManageKpiYear> findManagerKpiCoefficientDataListPerPageByParams(Map<String, Object> params) {
        return null;
    }

    @Override
    public Map<String, Object> readManagerKpiCoefficientDataSource(Attachment attachment) {
        return null;
    }

    @Override
    public List<Map<String, Object>> findManagerKpiCoefficientMapDataByParams(Map<String, Object> params) {
        return null;
    }
}
