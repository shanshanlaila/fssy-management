package com.fssy.shareholder.management.service.system.impl.manage;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fssy.shareholder.management.mapper.system.manage.ManageKpiYearMapper;
import com.fssy.shareholder.management.mapper.system.manage.ManageKpiLibMapper;
import com.fssy.shareholder.management.pojo.system.config.Attachment;
import com.fssy.shareholder.management.pojo.system.manage.ManageKpiYear;
import com.fssy.shareholder.management.pojo.system.manage.ManageKpiLib;
import com.fssy.shareholder.management.service.common.SheetService;
import com.fssy.shareholder.management.service.system.manage.ManageKpiYearService;
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
 * 经营管理年度指标项目 服务实现类
 * </p>
 *
 * @author Shizn
 * @since 2022-10-20
 */
@Service
//@Transactional
public class ManageKpiYearServiceImpl extends ServiceImpl<ManageKpiYearMapper, ManageKpiYear> implements ManageKpiYearService {

    @Autowired
    private SheetService sheetService;
    @Autowired
    private ManageKpiYearMapper manageKpiYearMapper;
    @Autowired
    private ManageKpiLibMapper manageKpiLibMapper;

    @Override
    public Page<ManageKpiYear> findManageKpiYearDataListPerPageByParams(Map<String, Object> params) {
        QueryWrapper<ManageKpiYear> queryWrapper = getQueryWrapper(params);
        int limit = (int) params.get("limit");
        int page = (int) params.get("page");
        Page<ManageKpiYear> myPage = new Page<>(page, limit);
        return manageKpiYearMapper.selectPage(myPage, queryWrapper);
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

    @Override
    @Transactional
    public Map<String, Object> readManageKpiYearDataSource(Attachment attachment) {
        // 返回消息
        Map<String, Object> result = new HashMap<>();
        result.put("content", "");
        result.put("failed", false);
        // 读取附件
        sheetService.load(attachment.getPath(), attachment.getFilename()); // 根据路径和名称读取附件
        // 读取表单
        sheetService.readByName("经营管理年度指标项目"); //根据表单名称获取该工作表单
        // 获取表单数据
        Sheet sheet = sheetService.getSheet();
        if (ObjectUtils.isEmpty(sheet)) {
            throw new ServiceException("表单【经营管理年度指标项目】不存在，无法读取数据，请检查");
        }
        // 获取单价列表数据
        Row row;
        List<ManageKpiYear> manageKpiYears = new ArrayList<>(); //实体类集合，用于后面的批量写入数据库
        // 2022-06-01 从决策系统导出数据，存在最后几行为空白数据，导致报数据越界问题，这里的长度由表头长度控制
        short maxSize = sheet.getRow(0).getLastCellNum();//列数(表头长度)
        // 循环总行数(不读表的标题，从第1行开始读)
        for (int j = 2; j <= sheet.getLastRowNum(); j++) {// getPhysicalNumberOfRows()此方法不会将空白行计入行数
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
            // 导入结果写入列
            Cell cell = row.createCell(SheetService.columnToIndex("Z"));
            String id = cells.get(SheetService.columnToIndex("A"));
            //String kpiLibId = cells.get(SheetService.columnToIndex("B"));
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
            String proportion = cells.get(SheetService.columnToIndex("Q"));
            String pastOneYearActual = cells.get(SheetService.columnToIndex("R"));
            String pastTwoYearsActual = cells.get(SheetService.columnToIndex("S"));
            String pastThreeYearsActual = cells.get(SheetService.columnToIndex("T"));
            String setPolicy = cells.get(SheetService.columnToIndex("U"));
            String source = cells.get(SheetService.columnToIndex("V"));

            // 判斷空值
            if (ObjectUtils.isEmpty(proportion)) {
                proportion = "0";
            }
            if (ObjectUtils.isEmpty(year)) {
                year = "0";
            }

            // 根据项目名称和年份找指标库对应的id，后导入id
            QueryWrapper<ManageKpiLib> manageKpiLibQueryWrapper = new QueryWrapper<>();
            manageKpiLibQueryWrapper.eq("projectDesc", projectDesc).eq("kpiYear", year);
            List<ManageKpiLib> manageKpiLibs = manageKpiLibMapper.selectList(manageKpiLibQueryWrapper);
            if (manageKpiLibs.size() > 1) {
                setFailedContent(result, String.format("第%s行的指标存在多条", j + 1));
                cell.setCellValue("存在多个指标，检查指标和年份是否正确");
                continue;
            }
            if (manageKpiLibs.size() == 0) {
                setFailedContent(result, String.format("第%s行的指标不存在", j + 1));
                cell.setCellValue("指标不存在，检查指标和年份是否正确");
                continue;
            }
            ManageKpiLib manageKpiLib = manageKpiLibMapper.selectList(manageKpiLibQueryWrapper).get(0);

            //构建实体类
            ManageKpiYear manageKpiYear = new ManageKpiYear();
            manageKpiYear.setId(Integer.valueOf(id));
            manageKpiYear.setKpiLibId(manageKpiLib.getId());
            manageKpiYear.setCompanyName(companyName);
            manageKpiYear.setStatus(status);
            manageKpiYear.setProjectType(projectType);
            manageKpiYear.setProjectDesc(projectDesc);
            manageKpiYear.setUnit(unit);
            manageKpiYear.setDataSource(dataSource);
            manageKpiYear.setBenchmarkCompany(benchmarkCompany);
            manageKpiYear.setBenchmarkValue(benchmarkValue);
            manageKpiYear.setMonitorDepartment(monitorDepartment);
            manageKpiYear.setMonitorUser(monitorUser);
            manageKpiYear.setYear(manageKpiLib.getKpiYear());
            manageKpiYear.setBasicTarget(basicTarget);
            manageKpiYear.setMustInputTarget(mustInputTarget);
            manageKpiYear.setReachTarget(reachTarget);
            manageKpiYear.setChallengeTarget(challengeTarget);
            manageKpiYear.setProportion(new BigDecimal(proportion));
            manageKpiYear.setPastOneYearActual(pastOneYearActual);
            manageKpiYear.setPastTwoYearsActual(pastTwoYearsActual);
            manageKpiYear.setPastThreeYearsActual(pastThreeYearsActual);
            manageKpiYear.setSetPolicy(setPolicy);
            manageKpiYear.setSource(source);

            // 根据id进行判断，存在则更新，不存在则新增
            saveOrUpdate(manageKpiYear);
            cell.setCellValue("导入成功");
        }
        sheetService.write(attachment.getPath(), attachment.getFilename());// 写入excel表
        return result;
    }

    @Override
    public List<Map<String, Object>> findManageKpiYearMapDataByParams(Map<String, Object> params) {
        QueryWrapper<ManageKpiYear> queryWrapper = getQueryWrapper(params);
        return manageKpiYearMapper.selectMaps(queryWrapper);
    }

    private QueryWrapper<ManageKpiYear> getQueryWrapper(Map<String, Object> params) {
        QueryWrapper<ManageKpiYear> queryWrapper = new QueryWrapper<>();
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
        if (params.containsKey("projectDesc")) {
            queryWrapper.like("projectDesc", params.get("projectDesc"));
        }
        if (params.containsKey("dataSource")) {
            queryWrapper.like("dataSource", params.get("dataSource"));
        }
        return queryWrapper;
    }
}
