package com.fssy.shareholder.management.service.system.impl.performance.manage_kpi;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fssy.shareholder.management.mapper.system.performance.manage_kpi.ManageKpiYearMapper;
import com.fssy.shareholder.management.mapper.system.performance.manage_kpi.ManageKpiLibMapper;
import com.fssy.shareholder.management.pojo.system.config.Attachment;
import com.fssy.shareholder.management.pojo.system.performance.manage_kpi.ManageKpiYear;
import com.fssy.shareholder.management.pojo.system.performance.manage_kpi.ManageKpiLib;
import com.fssy.shareholder.management.service.common.SheetService;
import com.fssy.shareholder.management.service.system.performance.manage_kpi.ManageKpiLibService;
import com.fssy.shareholder.management.service.system.performance.manage_kpi.ManageKpiYearService;
import com.fssy.shareholder.management.tools.exception.ServiceException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.Year;
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
    @Autowired
    private ManageKpiLibService manageKpiLibService;

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
    public Map<String, Object> readManageKpiYearDataSource(Attachment attachment, String year,String companyName) {
        // 返回消息
        Map<String, Object> result = new HashMap<>();
        result.put("content", "");
        result.put("failed", false);
        // 读取附件
        sheetService.load(attachment.getPath(), attachment.getFilename()); // 根据路径和名称读取附件
        // 读取表单
        sheetService.readByName("年度经营管理指标"); //根据表单名称获取该工作表单
        // 获取表单数据
        Sheet sheet = sheetService.getSheet();
        if (ObjectUtils.isEmpty(sheet)) {
            throw new ServiceException("表单【年度经营管理指标】不存在，无法读取数据，请检查");
        }
        // 获取单价列表数据
        Row row;
        List<ManageKpiYear> manageKpiYears = new ArrayList<>(); //实体类集合，用于后面的批量写入数据库
        // 2022-06-01 从决策系统导出数据，存在最后几行为空白数据，导致报数据越界问题，这里的长度由表头长度控制
        short maxSize = sheet.getRow(0).getLastCellNum();//列数(表头长度)
        //获取年份和公司名称
        Cell companyCell = sheet.getRow(1).getCell(SheetService.columnToIndex("B"));
        Cell yearCell = sheet.getRow(1).getCell(SheetService.columnToIndex("F"));
        String companyCellValue = sheetService.getValue(companyCell);
        String yearCellValue = sheetService.getValue(yearCell);

        //效验年份、公司名称
        if (!year.equals(yearCellValue)){
            throw new ServiceException("导入的年份与excel中的年份不一致，导入失败");
        }
        if (!companyName.equals(companyCellValue)){
            throw new ServiceException("导入的公司名称与excel中的年份不一致，导入失败");
        }
        // 循环总行数(不读表的标题，从第1行开始读)
        for (int j = 4; j <= sheet.getLastRowNum(); j++) {// getPhysicalNumberOfRows()此方法不会将空白行计入行数
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
            Cell cell = row.createCell(SheetService.columnToIndex("AF"));
            String id = cells.get(SheetService.columnToIndex("A"));
            String projectType = cells.get(SheetService.columnToIndex("B"));
            String projectDesc = cells.get(SheetService.columnToIndex("C"));
            String kpiDefinition = cells.get(SheetService.columnToIndex("D"));
            String unit = cells.get(SheetService.columnToIndex("E"));
            String dataSource = cells.get(SheetService.columnToIndex("F"));
            String benchmarkCompany = cells.get(SheetService.columnToIndex("G"));
            String benchmarkValue = cells.get(SheetService.columnToIndex("H"));
            String pastThreeYearsActual = cells.get(SheetService.columnToIndex("I"));
            String pastTwoYearsActual = cells.get(SheetService.columnToIndex("J"));
            String pastOneYearActual = cells.get(SheetService.columnToIndex("K"));
            String basicTarget = cells.get(SheetService.columnToIndex("L"));
            String mustInputTarget = cells.get(SheetService.columnToIndex("M"));
            String reachTarget = cells.get(SheetService.columnToIndex("N"));
            String challengeTarget = cells.get(SheetService.columnToIndex("O"));
            String managerKpiMark= cells.get(SheetService.columnToIndex("P"));
            String evaluateMode= cells.get(SheetService.columnToIndex("Q"));
            String monitorDepartment = cells.get(SheetService.columnToIndex("AD"));
            String monitorUser = cells.get(SheetService.columnToIndex("AE"));

            // 根据项目名称和年份找指标库对应的id，后导入指标库id
            QueryWrapper<ManageKpiLib> manageKpiLibQueryWrapper = new QueryWrapper<>();
            manageKpiLibQueryWrapper.eq("projectDesc", projectDesc);
            List<ManageKpiLib> manageKpiLibs = manageKpiLibMapper.selectList(manageKpiLibQueryWrapper);
            if (manageKpiLibs.size() > 1) {
                setFailedContent(result, String.format("第%s行的指标存在多条", j + 1));
                cell.setCellValue("存在多个指标，检查数据是否正确");
                continue;
            }
            if (manageKpiLibs.size() == 0) {
                setFailedContent(result, String.format("第%s行的指标不存在", j + 1));
                cell.setCellValue("指标不存在，检查数据是否正确");
                continue;
            }
            ManageKpiLib manageKpiLib = manageKpiLibMapper.selectList(manageKpiLibQueryWrapper).get(0);

            //构建实体类
            ManageKpiYear manageKpiYear = new ManageKpiYear();
            //判断是否已经存在年度id
            QueryWrapper<ManageKpiYear> yearQueryWrapper = new QueryWrapper<>();
            yearQueryWrapper.eq("year",year).eq("companyName",companyName)
                    .eq("projectDesc",projectDesc);
            List<ManageKpiYear> manageKpiYearList = manageKpiYearMapper.selectList(yearQueryWrapper);
            if (manageKpiYearList.size()>1){
                setFailedContent(result,String.format("第%s行的年度指标存在多条", j + 1));
                cell.setCellValue("存在多条年度指标，检查数据是否正确");
                continue;
            }
            if (manageKpiYearList.size()==1){
                manageKpiYear.setId(manageKpiYearList.get(0).getId());  //年份id
            }

            //前端选择并写入
            manageKpiYear.setYear(Integer.valueOf(year));
            manageKpiYear.setCompanyName(companyName);
            //excel导入
            manageKpiYear.setKpiLibId(manageKpiLib.getId());  //指标库id
            manageKpiYear.setProjectType(projectType);
            manageKpiYear.setKpiDefinition(kpiDefinition);
            manageKpiYear.setProjectDesc(projectDesc);
            manageKpiYear.setUnit(unit);
            manageKpiYear.setDataSource(dataSource);
            manageKpiYear.setBenchmarkCompany(benchmarkCompany);
            manageKpiYear.setBenchmarkValue(benchmarkValue);
            manageKpiYear.setMonitorDepartment(monitorDepartment);
            manageKpiYear.setMonitorUser(monitorUser);
            manageKpiYear.setBasicTarget(basicTarget);
            manageKpiYear.setMustInputTarget(mustInputTarget);
            manageKpiYear.setReachTarget(reachTarget);
            manageKpiYear.setChallengeTarget(challengeTarget);
            manageKpiYear.setPastOneYearActual(pastOneYearActual);
            manageKpiYear.setPastTwoYearsActual(pastTwoYearsActual);
            manageKpiYear.setPastThreeYearsActual(pastThreeYearsActual);
            manageKpiYear.setManagerKpiMark(managerKpiMark);
            manageKpiYear.setEvaluateMode(evaluateMode);


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
