package com.fssy.shareholder.management.service.system.impl.performance.manage_kpi;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fssy.shareholder.management.mapper.manage.company.CompanyMapper;
import com.fssy.shareholder.management.mapper.system.performance.manage_kpi.*;
import com.fssy.shareholder.management.pojo.manage.company.Company;
import com.fssy.shareholder.management.pojo.system.config.Attachment;
import com.fssy.shareholder.management.pojo.system.performance.manage_kpi.*;
import com.fssy.shareholder.management.service.common.SheetService;
import com.fssy.shareholder.management.service.system.performance.manage_kpi.ViewManageMonthPerformanceService;
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
import java.util.*;

/**
 * <p>
 * 视图年度月度实绩值 服务实现类
 * </p>
 *
 * @author Shizn
 * @since 2022-11-16
 */
@Service
public class ViewManageMonthPerformanceServiceImpl extends ServiceImpl<ViewManageMonthPerformanceMapper, ViewManageMonthPerformance> implements ViewManageMonthPerformanceService {
    @Autowired
    private SheetService sheetService;
    @Autowired
    private ManageKpiLibMapper manageKpiLibMapper;
    @Autowired
    private ViewManageMonthPerformanceMapper viewManageMonthPerformanceMapper;
    @Autowired
    private ManageKpiMonthAimMapper manageKpiMonthAimMapper;
    @Autowired
    private ManageKpiMonthPerformanceMapper manageKpiMonthPerformanceMapper;
    @Autowired
    private ManageKpiYearMapper manageKpiYearMapper;
    @Autowired
    private CompanyMapper companyMapper;
    @Autowired
    private ManageKpiMonthAimServiceImpl manageKpiMonthAimServiceImpl;
    @Autowired
    private ManageKpiYearServiceImpl manageKpiYearServiceImpl;
    @Autowired
    private ManageKpiMonthPerformanceServiceImpl manageKpiMonthPerformanceServiceImpl;
    /**
     * 通过条件进行 分页查询(一年十二个月的数据展示）
     * @param params 查询条件
     * @return 分页数据
     */
    @Override
    public Page<Map<String, Object>> findManageKpiMonthDataMapListPerPageByParams(Map<String, Object> params) {
        QueryWrapper<ViewManageMonthPerformance> queryWrapper = getQueryWrapper(params);
        int limit = (int) params.get("limit");
        int page = (int) params.get("page");
        Page<Map<String, Object>> myPage = new Page<>(page, limit);
        return viewManageMonthPerformanceMapper.selectMapsPage(myPage, queryWrapper);
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
     * 读取附件数据到数据库表（年度目标的导入）
     *
     * @param attachment 附件
     * @param year
     * @return 附件map集合
     */
    @Override
    @Transactional
    public Map<String, Object> readManageKpiYearDataSource(Attachment attachment, String year, String companyName) {
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
            //根据公司名称与公司表中的公司简称对应找到公司id并写入新表中
            QueryWrapper<Company> companyQueryWrapper = new QueryWrapper<>();
            companyQueryWrapper.eq("shortName",companyName);
            List<Company> companyList = companyMapper.selectList(companyQueryWrapper);
            if (companyList.size() > 1) {
                setFailedContent(result, String.format("第%s行的公司存在多条", j + 1));
                cell.setCellValue("存在多个公司名称，公司名称是否正确");
                continue;
            }
            if (companyList.size() == 0) {
                setFailedContent(result, String.format("第%s行的公司不存在", j + 1));
                cell.setCellValue("公司名称不存在，公司名称是否正确");
                continue;
            }
            //公司表中存在数据，获取这个公司名称的id
            Company company = companyMapper.selectList(companyQueryWrapper).get(0);

            manageKpiYear.setCompanyId(company.getId());      //公司id
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
            manageKpiYearServiceImpl.saveOrUpdate(manageKpiYear);
            cell.setCellValue("导入成功");
        }
        sheetService.write(attachment.getPath(), attachment.getFilename());// 写入excel表
        return result;
    }
    /**
     * 读取附件数据到数据库表(月度实绩导入）
     * @param attachment 附件
     * @return 附件map集合
     */
    @Override
    @Transactional
    public Map<String, Object> readManageKpiMonthPerformanceDataSource(Attachment attachment, String companyName, String year, String month) {
        // 返回消息
        Map<String, Object> result = new HashMap<>();
        result.put("content", "");
        result.put("failed", false);
        // 读取附件
        sheetService.load(attachment.getPath(), attachment.getFilename()); // 根据路径和名称读取附件
        // 读取表单
        sheetService.readByName("经营管理月度实绩项目"); //根据表单名称获取该工作表单
        // 获取表单数据
        Sheet sheet = sheetService.getSheet();
        if (ObjectUtils.isEmpty(sheet)) {
            throw new ServiceException("表单【经营管理月度实绩项目】不存在，无法读取数据，请检查");
        }
        // 获取单价列表数据
        Row row;
        List<ManageKpiMonthPerformance> manageKpiMonthPerformances = new ArrayList<>(); //实体类集合，用于后面的批量写入数据库
        // 2022-06-01 从决策系统导出数据，存在最后几行为空白数据，导致报数据越界问题，这里的长度由表头长度控制
        short maxSize = sheet.getRow(0).getLastCellNum();//列数(表头长度)

        //获取年份月份值
        Cell yearCell = sheet.getRow(1).getCell(SheetService.columnToIndex("H"));
        Cell monthCell = sheet.getRow(1).getCell(SheetService.columnToIndex("L"));
        Cell companyCell = sheet.getRow(1).getCell(SheetService.columnToIndex("B"));
        String companyCellValue = sheetService.getValue(companyCell);
        String monthCellValue = sheetService.getValue(monthCell);
        String yearCellValue = sheetService.getValue(yearCell);
        //效验年份、公司名称
        if (!companyName.equals(companyCellValue)){
            throw new ServiceException("导入的公司名称与excel中的名称不一致，导入失败");
        }
        if (!year.equals(yearCellValue)){
            throw new ServiceException("导入的年份与excel中的年份不一致，导入失败");
        }
        if (!month.equals(monthCellValue)){
            throw new ServiceException("导入的月份与excel中的名称不一致，导入失败");
        }

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
            Cell cell = row.createCell(SheetService.columnToIndex("U"));
            String projectDesc = cells.get(SheetService.columnToIndex("C"));
            String monthTarget = cells.get(SheetService.columnToIndex("R"));
            String monthActualValue = cells.get(SheetService.columnToIndex("S"));
            String accumulateTarget = cells.get(SheetService.columnToIndex("T"));
            String accumulateActual = cells.get(SheetService.columnToIndex("U"));
            String analyzeRes = cells.get(SheetService.columnToIndex("V"));

            //根据副标题中的年份、公司、月份和excel中的项目名称，对月份表进行查询找到唯一的id，再根据月份id进行导入
            QueryWrapper<ManageKpiMonthPerformance> manageKpiMonthPerformanceQueryWrapper = new QueryWrapper<>();
            manageKpiMonthPerformanceQueryWrapper.eq("year",yearCellValue)
                    .eq("companyName",companyCellValue).eq("month",monthCellValue).eq("projectDesc",projectDesc);
            List<ManageKpiMonthPerformance> monthPerformances = manageKpiMonthPerformanceMapper.selectList(manageKpiMonthPerformanceQueryWrapper);
            if (monthPerformances.size()>1){
                setFailedContent(result, String.format("第%s行的指标存在多条", j + 1));
                cell.setCellValue("存在多个指标，检查数据是否正确");
                continue;
            }
            if (monthPerformances.size() == 0) {
                setFailedContent(result, String.format("第%s行的指标不存在", j + 1));
                cell.setCellValue("指标不存在，检查数据是否正确");
                continue;
            }
            ManageKpiMonthPerformance performance = monthPerformances.get(0);

            //根据公司名称与公司表中的公司简称对应找到公司id并写入新表中
            QueryWrapper<Company> companyQueryWrapper = new QueryWrapper<>();
            companyQueryWrapper.eq("shortName",companyName);
            List<Company> companyList = companyMapper.selectList(companyQueryWrapper);
            if (companyList.size() > 1) {
                setFailedContent(result, String.format("第%s行的公司存在多条", j + 1));
                cell.setCellValue("存在多个公司名称，公司名称是否正确");
                continue;
            }
            if (companyList.size() == 0) {
                setFailedContent(result, String.format("第%s行的公司不存在", j + 1));
                cell.setCellValue("公司名称不存在，公司名称是否正确");
                continue;
            }
            //公司表中存在数据，获取这个公司名称的id
            Company company = companyMapper.selectList(companyQueryWrapper).get(0);

            //构建实体类
            ManageKpiMonthPerformance manageKpiMonthPerformance = new ManageKpiMonthPerformance();
            manageKpiMonthPerformance.setCompanyId(company.getId());      //公司id
            manageKpiMonthPerformance.setId(Integer.valueOf(performance.getId()));  //月份id
            if (!ObjectUtils.isEmpty(monthTarget)){
                manageKpiMonthPerformance.setMonthTarget(new BigDecimal(monthTarget));
            }
            if (!ObjectUtils.isEmpty(monthActualValue)){
                manageKpiMonthPerformance.setMonthActualValue(new BigDecimal(monthActualValue));
            }
            if (!ObjectUtils.isEmpty(accumulateTarget)){
                manageKpiMonthPerformance.setAccumulateTarget(new BigDecimal(accumulateTarget));
            }
            if (!ObjectUtils.isEmpty(accumulateActual)){
                manageKpiMonthPerformance.setAccumulateActual(new BigDecimal(accumulateActual));
            }
            manageKpiMonthPerformance.setAnalyzeRes(analyzeRes);
            manageKpiMonthPerformance.setMonth(Integer.parseInt(monthCellValue));
            manageKpiMonthPerformance.setYear(Integer.parseInt(yearCellValue));
            // 根据id进行判断，存在则更新，不存在则新增
            manageKpiMonthPerformanceServiceImpl.saveOrUpdate(manageKpiMonthPerformance);
            cell.setCellValue("导入成功");
        }
        sheetService.write(attachment.getPath(), attachment.getFilename());// 写入excel表
        return result;
    }
    /**
     * 读取附件数据到数据库表（月度目标导入）
     *
     * @param attachment 附件
     * @return 数据列表
     */
    @Override
    @Transactional
    public Map<String, Object> readManageKpiMonthAimDataSource(Attachment attachment, String companyName, String year) {
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
        List<ManageKpiMonthAim> manageKpiMonthAims = new ArrayList<>(); //实体类集合，用于后面的批量写入数据库
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
        for (int i = 1; i <=12; i++) {
            // 循环总行数(不读表的标题，从第5行开始读)
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


                int month=i;
                String monthTarget = null;
                if (i==1) {
                    monthTarget = cells.get(SheetService.columnToIndex("R"));
                }
                if (i==2) {
                    monthTarget = cells.get(SheetService.columnToIndex("S"));
                }
                if (i==3) {
                    monthTarget = cells.get(SheetService.columnToIndex("T"));
                }
                if (i==4) {
                    monthTarget = cells.get(SheetService.columnToIndex("U"));
                }
                if (i==5) {
                    monthTarget = cells.get(SheetService.columnToIndex("V"));
                }
                if (i==6) {
                    monthTarget = cells.get(SheetService.columnToIndex("W"));
                }
                if (i==7) {
                    monthTarget = cells.get(SheetService.columnToIndex("X"));
                }
                if (i==8) {
                    monthTarget = cells.get(SheetService.columnToIndex("Y"));
                }
                if (i==9) {
                    monthTarget = cells.get(SheetService.columnToIndex("Z"));
                }
                if (i==10) {
                    monthTarget = cells.get(SheetService.columnToIndex("AA"));
                }
                if (i==11) {
                    monthTarget = cells.get(SheetService.columnToIndex("AB"));
                }
                if (i==12) {
                    monthTarget = cells.get(SheetService.columnToIndex("AC"));
                }
                // 根据项目名称和年份找指标库对应的id，后导入指标库id
                QueryWrapper<ManageKpiLib> manageKpiLibQueryWrapper = new QueryWrapper<>();
                manageKpiLibQueryWrapper.eq("projectDesc", projectDesc);
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

                //查询出对应的经营管理年度指标，如果存在两条及两条以上的数据就抛出错误,月度id
                QueryWrapper<ManageKpiMonthAim> manageKpiMonthAimQueryWrapper = new QueryWrapper<>();
                manageKpiMonthAimQueryWrapper.eq("companyName", companyName).eq("year", year)
                        .eq("month", month).eq("projectDesc", projectDesc);
                List<ManageKpiMonthAim> monthAimList = manageKpiMonthAimMapper.selectList(manageKpiMonthAimQueryWrapper);
                if (monthAimList.size() > 1) {
                    setFailedContent(result, String.format("第%s行的月度指标存在多条", j + 1));
                    cell.setCellValue("存在多个指标，检查数据是否正确");
                    continue;
                }
                //构建实体类
                ManageKpiMonthAim manageKpiMonthAim = new ManageKpiMonthAim();
                //根据公司名称与公司表中的公司简称对应找到公司id并写入新表中
                QueryWrapper<Company> companyQueryWrapper = new QueryWrapper<>();
                companyQueryWrapper.eq("shortName",companyName);
                List<Company> companyList = companyMapper.selectList(companyQueryWrapper);
                if (companyList.size() > 1) {
                    setFailedContent(result, String.format("第%s行的公司存在多条", j + 1));
                    cell.setCellValue("存在多个公司名称，公司名称是否正确");
                    continue;
                }
                if (companyList.size() == 0) {
                    setFailedContent(result, String.format("第%s行的公司不存在", j + 1));
                    cell.setCellValue("公司名称不存在，公司名称是否正确");
                    continue;
                }
                //公司表中存在数据，获取这个公司名称的id
                Company company = companyMapper.selectList(companyQueryWrapper).get(0);

                manageKpiMonthAim.setCompanyId(company.getId());      //公司id

                //更新id
                if (monthAimList.size()==1){
                    Integer monthId = monthAimList.get(0).getId();
                    manageKpiMonthAim.setId(monthId);  //月度id
                }
                //查询年度经营管理指标库中的id，写入月度表中，找出年度id
                QueryWrapper<ManageKpiYear> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("companyName", companyName)
                        .eq("year", year).eq("projectDesc", projectDesc);
                List<ManageKpiYear> manageKpiYearList = manageKpiYearMapper.selectList(queryWrapper);
                if (manageKpiYearList.size() > 1) {
                    setFailedContent(result, String.format("第%s行的年度指标存在多条", j + 1));
                    cell.setCellValue("存在多个指标，检查指标和年份是否正确");
                    continue;
                }
                if (manageKpiYearList.size() == 0) {
                    setFailedContent(result, String.format("第%s行的年度指标不存在", j + 1));
                    cell.setCellValue("指标不存在，检查指标和年份是否正确");
                    continue;
                }
                ManageKpiYear manageKpiYear = manageKpiYearList.get(0);
                //导入时固定状态
                String status="未锁定";
                //前端选择并写入
                manageKpiMonthAim.setYear(Integer.valueOf(year));
                manageKpiMonthAim.setCompanyName(companyName);
                //自定义月份根据循环次数依次对应相应月份
                manageKpiMonthAim.setMonth(Integer.valueOf(month));
                //对应id数据
//                manageKpiMonthAim.setId(monthAim.getId());  //月度id
                manageKpiMonthAim.setManageKpiYearId(manageKpiYear.getId());//经营管理年度指标id
                manageKpiMonthAim.setKpiLibId(manageKpiLib.getId());  //指标库id
                manageKpiMonthAim.setStatus(status);//固定状态
                //excel导入
                manageKpiMonthAim.setProjectType(projectType);
                manageKpiMonthAim.setProjectDesc(projectDesc);
                manageKpiMonthAim.setUnit(unit);
                manageKpiMonthAim.setDataSource(dataSource);
                manageKpiMonthAim.setBenchmarkCompany(benchmarkCompany);
                manageKpiMonthAim.setBenchmarkValue(benchmarkValue);
                manageKpiMonthAim.setMonitorDepartment(monitorDepartment);
                manageKpiMonthAim.setMonitorUser(monitorUser);
                manageKpiMonthAim.setBasicTarget(basicTarget);
                manageKpiMonthAim.setMustInputTarget(mustInputTarget);
                manageKpiMonthAim.setReachTarget(reachTarget);
                manageKpiMonthAim.setChallengeTarget(challengeTarget);
                manageKpiMonthAim.setPastOneYearActual(pastOneYearActual);
                manageKpiMonthAim.setPastTwoYearsActual(pastTwoYearsActual);
                manageKpiMonthAim.setPastThreeYearsActual(pastThreeYearsActual);
                manageKpiMonthAim.setKpiDefinition(kpiDefinition);
                if (!ObjectUtils.isEmpty(monthTarget)){
                    manageKpiMonthAim.setMonthTarget(new BigDecimal(monthTarget));
                }
                manageKpiMonthAim.setManagerKpiMark(managerKpiMark);
                manageKpiMonthAim.setEvaluateMode(evaluateMode);



                // 根据id进行判断，存在则更新，不存在则新增
                manageKpiMonthAimServiceImpl.saveOrUpdate(manageKpiMonthAim);
                cell.setCellValue("导入成功");
            }
        }
        sheetService.write(attachment.getPath(), attachment.getFilename());// 写入excel表
        return result;
    }
    /**
     * 通过查询条件查询数据，用于导出
     *
     * @param params 查询条件
     * @return 数据列表
     */
    @Override
    public List<Map<String, Object>> findViewManageMonthPerformanceMapDataByParams(Map<String, Object> params) {
        QueryWrapper<ViewManageMonthPerformance> queryWrapper = getQueryWrapper(params);
        return viewManageMonthPerformanceMapper.selectMaps(queryWrapper);
    }
    private QueryWrapper<ViewManageMonthPerformance> getQueryWrapper(Map<String, Object> params) {
        QueryWrapper<ViewManageMonthPerformance> queryWrapper = new QueryWrapper<>();
        //对前端传过来的公司主键进行非空判断，再进行字符串拆分使用SQL in进行查询
        Object companyId = params.get("companyIds");
        if (!ObjectUtils.isEmpty(companyId)) {
            if (params.containsKey("companyId")) {
                String companyIds = (String) params.get("companyId");
                List<String> strings = Arrays.asList(companyIds.split(","));
                queryWrapper.in("companyId", strings);
            }
        }
        //拆分前端的年月份的字符串，进行年月的查询
        int month=0;
        int year=0;
        String yearMonth = (String) params.get("yearMonth");
        if (!ObjectUtils.isEmpty(yearMonth)) {
            if (params.containsKey("yearMonth")) {
                List<String> strings = Arrays.asList(yearMonth.split("-"));
                year=Integer.parseInt(strings.get(0));
                month= Integer.parseInt(strings.get(1));
            }
        }
        //设置月份常量，十二月一共循环十二次，与数据查询的月份无关联
        int MONTH=12;
        // 达成数量
        StringBuilder selectStr1 = new StringBuilder("manageKpiYearId");
        do {
            selectStr1.append(", sum(if(MONTH =" + MONTH + ",monthTarget,null)) AS 'monthTarget" + MONTH + "'"
                    + ", sum(if(MONTH =" + MONTH + ",monthActualValue,null)) AS 'monthActual" + MONTH + "'");
            MONTH--;
        } while (MONTH>0);
        queryWrapper.select(selectStr1.toString()).eq("year",year)
                .groupBy("manageKpiYearId");

        if (params.containsKey("companyName")) {
            queryWrapper.like("companyName", params.get("companyName"));
        }
        if (params.containsKey("year")) {
            queryWrapper.eq("year", params.get("year"));
        }
        if (params.containsKey("projectType")) {
            queryWrapper.like("projectType", params.get("projectType"));
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
