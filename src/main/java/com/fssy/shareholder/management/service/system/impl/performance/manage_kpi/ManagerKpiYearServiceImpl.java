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

    @Autowired
    private SheetService sheetService;
    @Autowired
    private ManagerKpiYearMapper managerKpiYearMapper;
    @Autowired
    private ManageKpiYearMapper manageKpiYearMapper;

    /**
     * 通过查询条件 分页 查询列表
     * @param params 查询条件
     * @return 分页数据
     */
    @Override
    public Page<ManagerKpiYear> findManagerKpiYearDataListPerPageByParams(Map<String, Object> params) {
        QueryWrapper<ManagerKpiYear> queryWrapper = getQueryWrapper(params);
        int limit = (int)params.get("limit");
        int page = (int)params.get("page");
        Page<ManagerKpiYear> myPage =new Page<>(page,limit);
        return managerKpiYearMapper.selectPage(myPage,queryWrapper);
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
    public Map<String, Object> readManagerKpiYearDataSource(Attachment attachment) {
        // 返回消息
        Map<String, Object> result = new HashMap<>();
        result.put("content", "");
        result.put("failed", false);
        // 读取附件
        sheetService.load(attachment.getPath(), attachment.getFilename()); // 根据路径和名称读取附件
        // 读取表单
        sheetService.readByName("经理人年度KPI"); //根据表单名称获取该工作表单
        // 获取表单数据
        Sheet sheet = sheetService.getSheet();
        if (ObjectUtils.isEmpty(sheet)) {
            throw new ServiceException("表单【经理人年度KPI指标】不存在，无法读取数据，请检查");
        }
        // 获取单价列表数据
        Row row;
        List<ManagerKpiYear> managerKpiYears = new ArrayList<>();//实体类集合，用于后面的批量写入数据库
        // 2022-06-01 从决策系统导出数据，存在最后几行为空白数据，导致报数据越界问题，这里的长度由表头长度控制
        short maxSize = sheet.getRow(0).getLastCellNum();//列数(表头长度)
        // 循环总行数(不读表的标题，从第1行开始读)
        //sheet.getLastRowNum();返回最后一行的索引，即比行总数小1
        for (int j = 1; j <= sheet.getLastRowNum(); j++) {// getPhysicalNumberOfRows()此方法不会将空白行计入行数
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
            Cell cell = row.createCell(SheetService.columnToIndex("J"));
            String id = cells.get(SheetService.columnToIndex("A"));
            String companyName = cells.get(SheetService.columnToIndex("B"));
            String status = cells.get(SheetService.columnToIndex("C"));
            String managerName = cells.get(SheetService.columnToIndex("D"));
            String generalManager = cells.get(SheetService.columnToIndex("E"));
            String position = cells.get(SheetService.columnToIndex("F"));
            String year = cells.get(SheetService.columnToIndex("G"));
            String projectDesc = cells.get(SheetService.columnToIndex("H"));
            String note = cells.get(SheetService.columnToIndex("I"));
            // 判斷空值
            if (ObjectUtils.isEmpty(companyName)) {
                companyName = "0";
            }
            if (ObjectUtils.isEmpty(projectDesc)) {
                projectDesc = "0";
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
                continue;
            }
            ManageKpiYear manageKpiYear = manageKpiYearMapper.selectList(manageKpiYearQueryWrapper).get(0);
            //构建实体类
            ManagerKpiYear managerKpiYear = new ManagerKpiYear();
            managerKpiYear.setId(Integer.valueOf(id));
            managerKpiYear.setManageKpiYearId(manageKpiYear.getId());
            managerKpiYear.setCompanyName(companyName);
            managerKpiYear.setStatus(status);
            managerKpiYear.setManagerName(managerName);
            managerKpiYear.setGeneralManager(generalManager);
            managerKpiYear.setPosition(position);
            managerKpiYear.setYear(Integer.valueOf(year));
            managerKpiYear.setProjectDesc(projectDesc);
            managerKpiYear.setNote(note);

            // 根据id进行判断，存在则更新，不存在则新增
            saveOrUpdate(managerKpiYear);
            cell.setCellValue("导入成功");
        }
        // 写入excel表
        sheetService.write(attachment.getPath(), attachment.getFilename());
        return result;
    }
    /**
     * 通过查询条件查询履职计划map数据，用于导出
     * @param params 查询条件
     * @return 数据列表
     */
    @Override
    public List<Map<String, Object>> findManagerKpiYearDataByParams(Map<String, Object> params) {
        QueryWrapper<ManagerKpiYear> queryWrapper = getQueryWrapper(params);
        return managerKpiYearMapper.selectMaps(queryWrapper);
    }
    private QueryWrapper<ManagerKpiYear> getQueryWrapper(Map<String,Object> params){
        QueryWrapper<ManagerKpiYear> queryWrapper = new QueryWrapper<>();
        if (params.containsKey("id")) {
            queryWrapper.eq("id", params.get("id"));
        }
        if (params.containsKey("companyName")) {
            queryWrapper.like("companyName", params.get("companyName"));
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
