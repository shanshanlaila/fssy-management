package com.fssy.shareholder.management.service.system.impl.performance.manage_kpi;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fssy.shareholder.management.mapper.manage.company.CompanyMapper;
import com.fssy.shareholder.management.mapper.system.performance.manage_kpi.ManagerKpiCoefficientMapper;
import com.fssy.shareholder.management.pojo.manage.company.Company;
import com.fssy.shareholder.management.pojo.system.config.Attachment;
import com.fssy.shareholder.management.pojo.system.performance.manage_kpi.*;
import com.fssy.shareholder.management.service.common.SheetService;
import com.fssy.shareholder.management.service.system.performance.manage_kpi.ManagerKpiCoefficientService;
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
 * 经理人绩效系数表 服务实现类
 * </p>
 *
 * @author zzp
 * @since 2022-11-07
 */
@Service
public class ManagerKpiCoefficientServiceImpl extends ServiceImpl<ManagerKpiCoefficientMapper, ManagerKpiCoefficient> implements ManagerKpiCoefficientService {

    @Autowired
    private SheetService sheetService;
    @Autowired
    private ManagerKpiCoefficientMapper managerKpiCoefficientMapper;
    @Autowired
    private CompanyMapper companyMapper;

    /**
     * 分页查询
     * @param params 查询条件
     * @return
     */
    @Override
    public Page<ManagerKpiCoefficient> findManagerKpiCoefficientDataListPerPageByParams(Map<String, Object> params) {
        QueryWrapper<ManagerKpiCoefficient> queryWrapper = getQueryWrapper(params);
        int limit = (int) params.get("limit");
        int page = (int) params.get("page");
        Page<ManagerKpiCoefficient> myPage = new Page<>(page, limit);
        Page<ManagerKpiCoefficient> managerKpiCoefficientPage = managerKpiCoefficientMapper.selectPage(myPage, queryWrapper);
        return managerKpiCoefficientPage;
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
     * 读取附件数据到数据库表,导入
     * @param attachment 附件
     * @return 附件map集合
     */
    @Override
    @Transactional
    public Map<String, Object> readManagerKpiCoefficientDataSource(Attachment attachment, String companyName, String year) {
        // 返回消息
        Map<String, Object> result = new HashMap<>();
        result.put("content", "");
        result.put("failed", false);
        // 读取附件
        sheetService.load(attachment.getPath(), attachment.getFilename()); // 根据路径和名称读取附件
        // 读取表单
        sheetService.readByName("经理人项目难度系数表"); //根据表单名称获取该工作表单
        // 获取表单数据
        Sheet sheet = sheetService.getSheet();
        if (ObjectUtils.isEmpty(sheet)) {
            throw new ServiceException("表单【经理人项目难度系数表】不存在，无法读取数据，请检查");
        }
        // 获取单价列表数据
        Row row;
        List<ManagerKpiCoefficient> managerKpiCoefficients = new ArrayList<>();//实体类集合，用于后面的批量写入数据库
        // 2022-06-01 从决策系统导出数据，存在最后几行为空白数据，导致报数据越界问题，这里的长度由表头长度控制
        short maxSize = sheet.getRow(0).getLastCellNum();//列数(表头长度)

        //获取年份和公司名称
        Cell companyCell = sheet.getRow(1).getCell(SheetService.columnToIndex("B"));
        Cell yearCell = sheet.getRow(1).getCell(SheetService.columnToIndex("D"));
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
            Cell cell = row.createCell(SheetService.columnToIndex("I"));

            String managerName = cells.get(SheetService.columnToIndex("B"));
            String position = cells.get(SheetService.columnToIndex("C"));
            String generalManager = cells.get(SheetService.columnToIndex("D"));
            String difficultCoefficient = cells.get(SheetService.columnToIndex("E"));
            String incentiveCoefficient = cells.get(SheetService.columnToIndex("F"));


            //构建实体类
            ManagerKpiCoefficient managerKpiCoefficient = new ManagerKpiCoefficient();
            //前端获取数据直接导入
            managerKpiCoefficient.setManagerName(managerName);
            managerKpiCoefficient.setCompanyName(companyName);
            //根据公司名称与公司表中的公司简称对应找到公司id并写入新表中
            QueryWrapper<Company> companyQueryWrapper = new QueryWrapper<>();
            companyQueryWrapper.eq("name",companyName);
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
            managerKpiCoefficient.setCompanyId(company.getId());      //公司id


            // 根据指标、年份和公司名称找月度报表对应的id，后导入id
            QueryWrapper<ManagerKpiCoefficient> managerKpiCoefficientQueryWrapper = new QueryWrapper<>();
            managerKpiCoefficientQueryWrapper.eq("companyName", companyName)
                    .eq("year",year).eq("managerName",managerName);
            List<ManagerKpiCoefficient> managerKpiCoefficients1 = managerKpiCoefficientMapper.selectList(managerKpiCoefficientQueryWrapper);
            if (managerKpiCoefficients1.size()>1){
                setFailedContent(result, String.format("第%s行的指标存在多条", j + 1));
                cell.setCellValue("存在多个指标，检查项目名称、年份和公司名称,经理人姓名是否正确");
                continue;
            }
            if (managerKpiCoefficients1.size()==1){
                managerKpiCoefficient.setId(managerKpiCoefficients1.get(0).getId());
            }

            managerKpiCoefficient.setYear(Integer.valueOf(year));
            managerKpiCoefficient.setPosition(position);
            managerKpiCoefficient.setGeneralManager(generalManager);
            if(!ObjectUtils.isEmpty(difficultCoefficient)){
                managerKpiCoefficient.setDifficultCoefficient(new BigDecimal(difficultCoefficient));
            }
            if(!ObjectUtils.isEmpty(incentiveCoefficient)){
                managerKpiCoefficient.setIncentiveCoefficient(new BigDecimal(incentiveCoefficient));
            }


            // 根据id进行判断，存在则更新，不存在则新增
            saveOrUpdate(managerKpiCoefficient);
            cell.setCellValue("导入成功");
        }
        // 写入excel表
        sheetService.write(attachment.getPath(), attachment.getFilename());
        return result;
    }

    /**
     * 通过查询条件查询履职计划map数据，用于导出
     *
     * @param params 查询条件
     * @return 数据列表
     */
    @Override
    public List<Map<String, Object>> findManagerKpiCoefficientMapDataByParams(Map<String, Object> params) {
        QueryWrapper<ManagerKpiCoefficient> queryWrapper = getQueryWrapper(params);
        return managerKpiCoefficientMapper.selectMaps(queryWrapper);
    }

    @Override
    public boolean updateManagerKpiCoefficientData(ManagerKpiCoefficient managerKpiCoefficient) {
        int result = managerKpiCoefficientMapper.updateById(managerKpiCoefficient);
        if (result > 0) {
            return true;
        }
        return false;
    }

    /**
     * 查询条件
     * @param params
     * @return
     */
    private QueryWrapper<ManagerKpiCoefficient> getQueryWrapper(Map<String, Object> params) {
        QueryWrapper<ManagerKpiCoefficient> queryWrapper = new QueryWrapper<>();
        if (params.containsKey("select")) {
            queryWrapper.select((String) params.get("select"));
        }
        if (params.containsKey("year")) {
            queryWrapper.eq("year", params.get("year"));
        }
        if (params.containsKey("managerName")) {
            queryWrapper.like("managerName", params.get("managerName"));
        }
        if (params.containsKey("companyName")) {
            queryWrapper.like("companyName", params.get("companyName"));
        }
        if (params.containsKey("projectDesc")) {
            queryWrapper.like("projectDesc", params.get("projectDesc"));
        }
        if (params.containsKey("difficultCoefficient")) {
            queryWrapper.eq("difficultCoefficient", params.get("difficultCoefficient"));
        }
        if (params.containsKey("generalManager")) {
            queryWrapper.eq("generalManager", params.get("generalManager"));
        }
        //对前端传过来的公司主键进行非空判断，再进行字符串拆分使用SQL in进行查询
        Object companyId = params.get("companyIds");
        if (!ObjectUtils.isEmpty(companyId)) {
            if (params.containsKey("companyId")) {
                String companyIds = (String) params.get("companyId");
                List<String> strings = Arrays.asList(companyIds.split(","));
                queryWrapper.in("companyId", strings);
            }
        }
        return queryWrapper;
    }
}
