package com.fssy.shareholder.management.service.system.impl.performance.manage_kpi;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fssy.shareholder.management.mapper.system.config.AttachmentMapper;
import com.fssy.shareholder.management.mapper.system.performance.manage_kpi.ManageKpiLibMapper;
import com.fssy.shareholder.management.pojo.system.config.Attachment;
import com.fssy.shareholder.management.pojo.system.performance.manage_kpi.ManageKpiLib;
import com.fssy.shareholder.management.service.common.SheetService;
import com.fssy.shareholder.management.service.system.performance.manage_kpi.ManageKpiLibService;
import com.fssy.shareholder.management.tools.exception.ServiceException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.*;

/**
 * <p>
 * 经营管理指标库 服务实现类
 * </p>
 *
 * @author Shizn
 * @since 2022-10-12
 */
@Service
public class ManageKpiLibServiceImpl extends ServiceImpl<ManageKpiLibMapper, ManageKpiLib
        > implements ManageKpiLibService {
    @Autowired
    private SheetService sheetService;
    @Autowired
    private ManageKpiLibMapper manageKpiLibMapper;
    @Autowired
    private AttachmentMapper attachmentMapper;


    @Override
    public Page<ManageKpiLib> findManagerKpiLibDataListPerPageByParams(Map<String, Object> params) {
        QueryWrapper<ManageKpiLib> queryWrapper = getQueryWrapper(params);
        int limit = (int) params.get("limit");
        int page = (int) params.get("page");
        Page<ManageKpiLib> myPage = new Page<>(page, limit);
        Page<ManageKpiLib> managerKpiLibPage = manageKpiLibMapper.selectPage(myPage, queryWrapper);
        return managerKpiLibPage;
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
    public Map<String, Object> readManagerKpiLibDataSource(Attachment attachment) {
        // 返回消息
        Map<String, Object> result = new HashMap<>();
        result.put("content", "");
        result.put("failed", false);
        // 读取附件
        sheetService.load(attachment.getPath(), attachment.getFilename()); // 根据路径和名称读取附件
        // 读取表单
        sheetService.readByName("指标库"); //根据表单名称获取该工作表单
        // 获取表单数据
        Sheet sheet = sheetService.getSheet();
        if (ObjectUtils.isEmpty(sheet)) {
            throw new ServiceException("表单【指标库】不存在，无法读取数据，请检查");
        }

        // 获取单价列表数据
        Row row;
        List<ManageKpiLib> manageKpiLibList = new ArrayList<>(); //实体类集合，用于后面的批量写入数据库
        // 2022-06-01 从决策系统导出数据，存在最后几行为空白数据，导致报数据越界问题，这里的长度由表头长度控制
        short maxSize = sheet.getRow(0).getLastCellNum();//列数(表头长度)
        // 循环总行数(不读表的标题，从第1行开始读)
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
            // 导入结果写入列
            Cell cell = row.createCell(SheetService.columnToIndex("N"));// 报错信息上传到excel D列（暂未实现）
            String id = cells.get(SheetService.columnToIndex("A"));
            String projectType = cells.get(SheetService.columnToIndex("B"));
            String projectDesc = cells.get(SheetService.columnToIndex("C"));
            // 检查必填项
            if (ObjectUtils.isEmpty(projectDesc)) {
                setFailedContent(result, String.format("第%s行的项目名称是空的", j + 1));
                cell.setCellValue("项目名称是空的");
                throw new ServiceException("表中有空值");
            }
            String status = cells.get(SheetService.columnToIndex("D"));
            String isCommon = cells.get(SheetService.columnToIndex("E"));
            String unit = cells.get(SheetService.columnToIndex("F"));
            String kpiDefinition = cells.get(SheetService.columnToIndex("G"));
            String kpiFormula = cells.get(SheetService.columnToIndex("H"));
            String kpiYear = cells.get(SheetService.columnToIndex("I"));
            String managerKpi = cells.get(SheetService.columnToIndex("J"));
            String cfoKpi = cells.get(SheetService.columnToIndex("K"));
            String note = cells.get(SheetService.columnToIndex("L"));

            //构建实体类
            ManageKpiLib manageKpiLib = new ManageKpiLib();
            manageKpiLib.setId(Integer.valueOf(id));
            manageKpiLib.setProjectType(projectType);
            manageKpiLib.setProjectDesc(projectDesc);
            manageKpiLib.setStatus(status);
            manageKpiLib.setIsCommon(isCommon);
            manageKpiLib.setUnit(unit);
            manageKpiLib.setKpiDefinition(kpiDefinition);
            manageKpiLib.setKpiFormula(kpiFormula);
            manageKpiLib.setKpiYear(Integer.valueOf(kpiYear));
            manageKpiLib.setManagerKpi(managerKpi);
            manageKpiLib.setCfoKpi(cfoKpi);
            manageKpiLib.setNote(note);

            // 根据id进行判断，存在则更新，不存在则新增
            saveOrUpdate(manageKpiLib);
            cell.setCellValue("导入成功");

        }

        sheetService.write(attachment.getPath(), attachment.getFilename());// 写入excel表
        return result;
    }

    @Override
    public List<Map<String, Object>> findManagerKpiLibDataSource(Map<String, Object> params) {
        QueryWrapper<ManageKpiLib> queryWrapper = getQueryWrapper(params);
        return manageKpiLibMapper.selectMaps(queryWrapper);
    }

    private QueryWrapper<ManageKpiLib> getQueryWrapper(Map<String, Object> params) {
        QueryWrapper<ManageKpiLib> queryWrapper = new QueryWrapper<>();
        // 经理人年度KPI指标库表主键查询
        if (params.containsKey("id")) {
            queryWrapper.eq("id", params.get("id"));
        }
        if (params.containsKey("select")) {
            queryWrapper.select((String) params.get("select"));
        }
        // 经理人年度KPI指标库表创建年份查询
        if (params.containsKey("kpiYear")) {
            queryWrapper.eq("kpiYear", params.get("kpiYear"));
        }
        // 经理人年度KPI指标库表是否经理人查询
        if (params.containsKey("managerKpi")) {
            queryWrapper.eq("managerKpi", params.get("managerKpi"));
        }
        // 经理人年度KPI指标库表是否是外派财务查询
        if (params.containsKey("cfoKpi")) {
            queryWrapper.eq("cfoKpi", params.get("cfoKpi"));
        }
        // 经理人年度KPI指标库表KPI名称模糊查询
        if (params.containsKey("projectDesc")) {
            queryWrapper.like("projectDesc", params.get("projectDesc"));
        }
        return queryWrapper;
    }
}
