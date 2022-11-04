package com.fssy.shareholder.management.service.system.impl.performance.manage_kpi;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.fssy.shareholder.management.mapper.system.performance.manage_kpi.ManagerKpiYearMapper;
import com.fssy.shareholder.management.mapper.system.performance.manage_kpi.ManageKpiYearMapper;
import com.fssy.shareholder.management.pojo.system.config.Attachment;
import com.fssy.shareholder.management.pojo.system.performance.manage_kpi.ManagerKpiYear;
import com.fssy.shareholder.management.pojo.system.performance.manage_kpi.ManageKpiYear;
import com.fssy.shareholder.management.service.common.SheetService;
import com.fssy.shareholder.management.service.system.performance.manage_kpi.ManagerKpiYearService;
import com.fssy.shareholder.management.tools.exception.ServiceException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 经理人年度kpi指标 服务实现类
 * </p>
 *
 * @author zzp
 * @since 2022-10-26
 */
@Service
public class ManagerKpiYearServiceImpl extends ServiceImpl<ManagerKpiYearMapper, ManagerKpiYear> implements ManagerKpiYearService {


}
