package com.fssy.shareholder.management.service.system.impl.manage;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.fssy.shareholder.management.mapper.system.manage.ManageKpiMonthPerformanceMapper;
import com.fssy.shareholder.management.mapper.system.manage.ManageKpiYearMapper;
import com.fssy.shareholder.management.pojo.system.config.Attachment;
import com.fssy.shareholder.management.pojo.system.manage.ManageKpiMonthPerformance;
import com.fssy.shareholder.management.pojo.system.manage.ManageKpiYear;
import com.fssy.shareholder.management.service.common.SheetService;
import com.fssy.shareholder.management.service.system.manage.ManageKpiMonthPerformanceService;
import com.fssy.shareholder.management.tools.exception.ServiceException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 经营管理月度实绩指标数据表 服务实现类
 * </p>
 *
 * @author zzp
 * @since 2022-10-24
 */
@Service
public class ManageKpiMonthPerformanceServiceImpl extends ServiceImpl<ManageKpiMonthPerformanceMapper, ManageKpiMonthPerformance> implements ManageKpiMonthPerformanceService {

    @Autowired
    private SheetService sheetService;
    @Autowired
    private ManageKpiMonthPerformanceMapper manageKpiMonthPerformanceMapper;
    @Autowired
    private ManageKpiYearMapper manageKpiYearMapper;

    /**
     * 通过查询条件 分页 查询列表
     * @param params 查询条件
     * @return 分页数据
     */
    @Override
    public Page<ManageKpiMonthPerformance> findManageKpiMonthDataListPerPageByParams(Map<String, Object> params) {
        QueryWrapper<ManageKpiMonthPerformance> queryWrapper = getQueryWrapper(params);
        int limit = (int)params.get("limit");
        int page = (int)params.get("page");
        Page<ManageKpiMonthPerformance> myPage = new Page<>(page,limit);
        return manageKpiMonthPerformanceMapper.selectPage(myPage,queryWrapper);
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
     * @param attachment 附件
     * @return 附件map集合
     */
    @Override
    @Transactional
    public Map<String, Object> readManageKpiMonthDataSource(Attachment attachment) {
        // 返回消息
        Map<String, Object> result = new HashMap<>();
        result.put("content", "");
        result.put("failed", false);
        // 读取附件
        sheetService.load(attachment.getPath(), attachment.getFilename()); // 根据路径和名称读取附件
        // 读取表单
        sheetService.readByName("经营管理月度项目指标"); //根据表单名称获取该工作表单
        // 获取表单数据
        Sheet sheet = sheetService.getSheet();
        if (ObjectUtils.isEmpty(sheet)) {
            throw new ServiceException("表单【经营管理月度项目指标】不存在，无法读取数据，请检查");
        }
        // 获取单价列表数据
        Row row;
        List<ManageKpiMonthPerformance> manageKpiMonthPerformances = new ArrayList<>(); //实体类集合，用于后面的批量写入数据库
        // 2022-06-01 从决策系统导出数据，存在最后几行为空白数据，导致报数据越界问题，这里的长度由表头长度控制
        short maxSize = sheet.getRow(0).getLastCellNum();//列数(表头长度)
        // 循环总行数(不读表的标题，从第1行开始读)
        //sheet.getLastRowNum();返回最后一行的索引，即比行总数小1
        for (int j = 3; j <= sheet.getLastRowNum(); j++) {// getPhysicalNumberOfRows()此方法不会将空白行计入行数
            List<String> cells = new ArrayList<>();// 每一行的数据用一个list接收
            row = sheet.getRow(j);// 获取第j行
            // 获取一行中有多少列 Row：行，cell：列
            // 循环row行中的每一个单元格
            for (int k = 0; k < maxSize; k++) {
                // 如果这单元格为空，就写入空
                if (row.getCell(k) == null) {
                    cells.add("");
                    continue;
                }
                // 处理单元格读取到公式的问题
                if (row.getCell(k).getCellType() == CellType.FORMULA) {
                    row.getCell(k).setCellType(CellType.STRING);
                    String res = row.getCell(k).getStringCellValue();
                    cells.add(res);
                    continue;
                }
                Cell cell = row.getCell(k);
                String res = sheetService.getValue(cell).trim();// 获取单元格的值
                cells.add(res);// 将单元格的值写入行
            }
            //导入结果写入列
            //错误信息提示存入到AD单元格内
            Cell cell = row.createCell(SheetService.columnToIndex("AD"));
            String id = cells.get(SheetService.columnToIndex("A"));
            String companyName = cells.get(SheetService.columnToIndex("B"));
            String status = cells.get(SheetService.columnToIndex("C"));
            String projectType = cells.get(SheetService.columnToIndex("D"));
            String projectDesc = cells.get(SheetService.columnToIndex("E"));
            String unit = cells.get(SheetService.columnToIndex("F"));
            String dataSource = cells.get(SheetService.columnToIndex("G"));
            String benchmarkCompany = cells.get(SheetService.columnToIndex("H"));
            String benchmarkValue = cells.get(SheetService.columnToIndex("I"));
            String monitorDepartment = cells.get(SheetService.columnToIndex("J"));
            String monitorUser = cells.get(SheetService.columnToIndex("K"));
            String year = cells.get(SheetService.columnToIndex("L"));
            String basicTarget = cells.get(SheetService.columnToIndex("M"));
            String mustInputTarget = cells.get(SheetService.columnToIndex("N"));
            String reachTarget = cells.get(SheetService.columnToIndex("O"));
            String challengeTarget = cells.get(SheetService.columnToIndex("P"));
//            String proportion = cells.get(SheetService.columnToIndex("Q"));
            String pastOneYearActual = cells.get(SheetService.columnToIndex("Q"));
            String pastTwoYearsActual = cells.get(SheetService.columnToIndex("R"));
            String pastThreeYearsActual = cells.get(SheetService.columnToIndex("S"));
            String setPolicy = cells.get(SheetService.columnToIndex("T"));
            String source = cells.get(SheetService.columnToIndex("U"));
            String monthTarget = cells.get(SheetService.columnToIndex("V"));
            String monthActualValue = cells.get(SheetService.columnToIndex("W"));
            String accumulateTarget = cells.get(SheetService.columnToIndex("X"));
            String accumulateActual = cells.get(SheetService.columnToIndex("Y"));
            String analyzeDesc = cells.get(SheetService.columnToIndex("Z"));
            // 判斷空值
            if (ObjectUtils.isEmpty(basicTarget)) {
                basicTarget = "0";
            }
            if (ObjectUtils.isEmpty(year)) {
                year = "0";
            }
            // 根据指标、年份和公司名称找月度报表对应的id，后导入id
            QueryWrapper<ManageKpiYear> manageKpiYearQueryWrapper = new QueryWrapper<>();
            manageKpiYearQueryWrapper.eq("projectDesc", projectDesc).eq("year", year).eq("companyName",companyName);
            List<ManageKpiYear> manageKpiYears = manageKpiYearMapper.selectList(manageKpiYearQueryWrapper);
            if (manageKpiYears.size()>1){
                setFailedContent(result, String.format("第%s行的指标存在多条", j + 1));
                cell.setCellValue("存在多个指标，检查指标、年份和公司名称是否正确");
                continue;
            } if(manageKpiYears.size()==0) {
                setFailedContent(result, String.format("第%s行的指标不存在", j + 1));
                cell.setCellValue("指标不存在，检查指标、年份和公司名称是否正确");
            }
            ManageKpiYear manageKpiYear = manageKpiYearMapper.selectList(manageKpiYearQueryWrapper).get(0);
            //构建实体类
            ManageKpiMonthPerformance manageKpiMonthPerformance = new ManageKpiMonthPerformance();
            manageKpiMonthPerformance.setId(Integer.valueOf(id));
            manageKpiMonthPerformance.setManageKpiYearId(manageKpiYear.getId());
            manageKpiMonthPerformance.setCompanyName(companyName);
            manageKpiMonthPerformance.setStatus(status);
            manageKpiMonthPerformance.setProjectType(projectType);
            manageKpiMonthPerformance.setProjectDesc(projectDesc);
            manageKpiMonthPerformance.setUnit(unit);
            manageKpiMonthPerformance.setDataSource(dataSource);
            manageKpiMonthPerformance.setBenchmarkCompany(benchmarkCompany);
            manageKpiMonthPerformance.setBenchmarkValue(benchmarkValue);
            manageKpiMonthPerformance.setMonitorDepartment(monitorDepartment);
            manageKpiMonthPerformance.setMonitorUser(monitorUser);
            manageKpiMonthPerformance.setYear(manageKpiYear.getYear());
            manageKpiMonthPerformance.setBasicTarget(basicTarget);
            manageKpiMonthPerformance.setMustInputTarget(mustInputTarget);
            manageKpiMonthPerformance.setReachTarget(reachTarget);
            manageKpiMonthPerformance.setChallengeTarget(challengeTarget);
//            manageKpiMonth.setProportion(new BigDecimal(proportion));
            manageKpiMonthPerformance.setPastOneYearActual(pastOneYearActual);
            manageKpiMonthPerformance.setPastTwoYearsActual(pastTwoYearsActual);
            manageKpiMonthPerformance.setPastThreeYearsActual(pastThreeYearsActual);
            manageKpiMonthPerformance.setSetPolicy(setPolicy);
            manageKpiMonthPerformance.setSource(source);
            manageKpiMonthPerformance.setMonthTarget(new BigDecimal(monthTarget));
            manageKpiMonthPerformance.setMonthActualValue(new BigDecimal(monthActualValue));
            manageKpiMonthPerformance.setAccumulateTarget(new BigDecimal(accumulateTarget));
            manageKpiMonthPerformance.setAccumulateActual(new BigDecimal(accumulateActual));
            manageKpiMonthPerformance.setAnalyzeDesc(analyzeDesc);

            // 根据id进行判断，存在则更新，不存在则新增
            saveOrUpdate(manageKpiMonthPerformance);
            cell.setCellValue("导入成功");
        }
        sheetService.write(attachment.getPath(), attachment.getFilename());// 写入excel表
        return result;
    }

    /**
     * 通过查询条件查询履职计划map数据，用于导出
     * @param params 查询条件
     * @return 数据列表
     */
    @Override
    public List<Map<String, Object>> findManageKpiMonthDataByParams(Map<String, Object> params) {
        QueryWrapper<ManageKpiMonthPerformance> queryWrapper = getQueryWrapper(params);
        return manageKpiMonthPerformanceMapper.selectMaps(queryWrapper);
    }
    private QueryWrapper<ManageKpiMonthPerformance> getQueryWrapper(Map<String,Object> params){
        QueryWrapper<ManageKpiMonthPerformance> queryWrapper = new QueryWrapper<>();
        if (params.containsKey("id")) {
            queryWrapper.eq("id", params.get("id"));
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
        return queryWrapper;
    }
}
