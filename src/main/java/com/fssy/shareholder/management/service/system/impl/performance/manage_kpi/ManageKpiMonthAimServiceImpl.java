package com.fssy.shareholder.management.service.system.impl.performance.manage_kpi;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.mapper.system.performance.manage_kpi.ManageKpiLibMapper;
import com.fssy.shareholder.management.mapper.system.performance.manage_kpi.ManageKpiYearMapper;
import com.fssy.shareholder.management.pojo.system.config.Attachment;
import com.fssy.shareholder.management.pojo.system.performance.manage_kpi.ManageKpiLib;
import com.fssy.shareholder.management.pojo.system.performance.manage_kpi.ManageKpiMonthAim;
import com.fssy.shareholder.management.mapper.system.performance.manage_kpi.ManageKpiMonthAimMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fssy.shareholder.management.pojo.system.performance.manage_kpi.ManageKpiYear;
import com.fssy.shareholder.management.service.common.SheetService;
import com.fssy.shareholder.management.service.system.performance.manage_kpi.ManageKpiMonthAimService;
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
    @Autowired
    private ManageKpiLibMapper manageKpiLibMapper;
    @Autowired
    private ManageKpiYearMapper manageKpiYearMapper;
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
     * 查询一年十二个月的数据，展示查询，包含条件查询
     * @param params
     * @return
     */
    @Override
    public Page<Map<String, Object>> findManageKpiMonthDataMapListPerPageByParams(Map<String, Object> params) {
        QueryWrapper<ManageKpiMonthAim> queryWrapper = getQueryWrapper(params);
        int limit = (int) params.get("limit");
        int page = (int) params.get("page");
        Page<Map<String,Object>> myPage = new Page<>(page, limit);
        return manageKpiMonthAimMapper.selectMapsPage(myPage, queryWrapper);
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
    @Transactional
    public Map<String, Object> readManageKpiMonthDataSource(Attachment attachment, String companyName, String year) {
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
                String monitorDepartment = cells.get(SheetService.columnToIndex("AB"));
                String monitorUser = cells.get(SheetService.columnToIndex("AC"));
                String kpiDecomposeMode = cells.get(SheetService.columnToIndex("AD"));

                int month=i;
                String monthTarget = null;
                if (i==1) {
                    monthTarget = cells.get(SheetService.columnToIndex("P"));
                }
                if (i==2) {
                    monthTarget = cells.get(SheetService.columnToIndex("Q"));
                }
                if (i==3) {
                    monthTarget = cells.get(SheetService.columnToIndex("R"));
                }
                if (i==4) {
                    monthTarget = cells.get(SheetService.columnToIndex("S"));
                }
                if (i==5) {
                    monthTarget = cells.get(SheetService.columnToIndex("T"));
                }
                if (i==6) {
                    monthTarget = cells.get(SheetService.columnToIndex("U"));
                }
                if (i==7) {
                    monthTarget = cells.get(SheetService.columnToIndex("V"));
                }
                if (i==8) {
                    monthTarget = cells.get(SheetService.columnToIndex("W"));
                }
                if (i==9) {
                    monthTarget = cells.get(SheetService.columnToIndex("X"));
                }
                if (i==10) {
                    monthTarget = cells.get(SheetService.columnToIndex("Y"));
                }
                if (i==11) {
                    monthTarget = cells.get(SheetService.columnToIndex("Z"));
                }
                if (i==12) {
                    monthTarget = cells.get(SheetService.columnToIndex("AA"));
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
                manageKpiMonthAim.setKpiDecomposeMode(kpiDecomposeMode);
                manageKpiMonthAim.setKpiDefinition(kpiDefinition);
                if (!ObjectUtils.isEmpty(monthTarget)){
                    manageKpiMonthAim.setMonthTarget(new BigDecimal(monthTarget));
                }


                // 根据id进行判断，存在则更新，不存在则新增
                saveOrUpdate(manageKpiMonthAim);
                cell.setCellValue("导入成功");
            }
        }
        sheetService.write(attachment.getPath(), attachment.getFilename());// 写入excel表
        return result;
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
        int month = 1;
        // 达成数量
        StringBuilder selectStr1 = new StringBuilder("manageKpiYearId,companyName,projectType,projectDesc,unit,benchmarkCompany," +
                "benchmarkValue,monitorDepartment,monitorUser,year,basicTarget,mustInputTarget,reachTarget,dataSource," +
                "challengeTarget,proportion,pastOneYearActual,pastTwoYearsActual,pastThreeYearsActual,kpiDefinition,kpiDecomposeMode");
        do
        {
            selectStr1.append(", sum(if(MONTH =" +  month + ",monthTarget,null)) AS 'month" + month + "'");
            month++;
        } while (month <= 12);
        queryWrapper.select(selectStr1.toString())
                .groupBy("manageKpiYearId");


        if (params.containsKey("id")) {
            queryWrapper.eq("id", params.get("id"));
        }
        if (params.containsKey("select")) {
            queryWrapper.select((String) params.get("select"));
        }
        //根据年份id进行分组
        if (params.containsKey("groupBy")){
            queryWrapper.groupBy("manageKpiYearId");
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
