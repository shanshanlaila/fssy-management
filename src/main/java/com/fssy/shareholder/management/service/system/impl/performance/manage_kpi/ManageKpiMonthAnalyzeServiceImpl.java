/**
 * ------------------------修改日志---------------------------------
 * 修改人       修改日期         修改内容
 */
package com.fssy.shareholder.management.service.system.impl.performance.manage_kpi;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fssy.shareholder.management.mapper.system.performance.manage_kpi.ManageKpiMonthAimMapper;
import com.fssy.shareholder.management.mapper.system.performance.manage_kpi.ManageKpiYearMapper;
import com.fssy.shareholder.management.pojo.system.config.Attachment;
import com.fssy.shareholder.management.pojo.system.performance.manage_kpi.ManageKpiMonthAim;
import com.fssy.shareholder.management.pojo.system.performance.manage_kpi.ManageKpiYear;
import com.fssy.shareholder.management.service.common.SheetService;
import com.fssy.shareholder.management.service.system.performance.manage_kpi.ManageKpiMonthAnalyzeService;
import com.fssy.shareholder.management.tools.exception.ServiceException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Shizn
 * @ClassName: ManageKpiMonthAnalyzeServiceImpl
 * @Description: 经营管理月度风险分析 服务类接口
 * @date 2022/10/24 0024 17:05
 */
@Service
public class ManageKpiMonthAnalyzeServiceImpl extends ServiceImpl<ManageKpiMonthAimMapper, ManageKpiMonthAim> implements ManageKpiMonthAnalyzeService {
    @Autowired
    private SheetService sheetService;
    @Autowired
    private ManageKpiMonthAimMapper manageKpiMonthAimMapper;
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
     * 设置失败的内容
     *
     * @param result 结果map
     * @param append 导入失败的原因
     */
    private void setFailedContent(Map<String, Object> result, String append) {
        String content = result.get("content").toString();
        if (org.springframework.util.ObjectUtils.isEmpty(content)) {
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
    public Map<String, Object> readManageKpiMonthDataSource(Attachment attachment) {
        //        // 返回消息
        Map<String, Object> result = new HashMap<>();
        result.put("content", "");
        result.put("failed", false);
        // 读取附件
        sheetService.load(attachment.getPath(), attachment.getFilename()); // 根据路径和名称读取附件
        // 读取表单
        sheetService.readByName("经营管理月度风险分析报表"); //根据表单名称获取该工作表单
        // 获取表单数据
        Sheet sheet = sheetService.getSheet();
        if (ObjectUtils.isEmpty(sheet)) {
            throw new ServiceException("表单【经营管理月度风险分析报表】不存在，无法读取数据，请检查");
        }
        // 获取单价列表数据
        Row row;
        List<ManageKpiMonthAim> manageKpiMonthAims = new ArrayList<>(); //实体类集合，用于后面的批量写入数据库
        // 2022-06-01 从决策系统导出数据，存在最后几行为空白数据，导致报数据越界问题，这里的长度由表头长度控制
        short maxSize = sheet.getRow(0).getLastCellNum();//列数(表头长度)
        //定义项目状态，实现导入成则改变项目的状态
        String evaluateStatus = "已分析";
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
            //导入结果写入列
            //错误信息提示存入的单元格
            Cell cell = row.createCell(SheetService.columnToIndex("AZ"));
            String id = cells.get(SheetService.columnToIndex("A"));
            String companyName = cells.get(SheetService.columnToIndex("B"));
            String projectDesc = cells.get(SheetService.columnToIndex("E"));
            String year = cells.get(SheetService.columnToIndex("L"));
            String analyzeDesc = cells.get(SheetService.columnToIndex("AW"));
            String analyzeRes = cells.get(SheetService.columnToIndex("AX"));
            //根据项目名称、年份、公司找经营管理年度指标id，进行效验
            QueryWrapper<ManageKpiYear> manageKpiMonthQueryWrapper = new QueryWrapper<>();
            manageKpiMonthQueryWrapper.eq("projectDesc", projectDesc).
                    eq("year", year).eq("companyName", companyName);
            List<ManageKpiYear> manageKpiYears = manageKpiYearMapper.selectList(manageKpiMonthQueryWrapper);
            if (manageKpiYears.size() > 1) {
                setFailedContent(result, String.format("第%s行的年度指标存在多条", j + 1));
                cell.setCellValue("存在多个年度指标，检查指标和年份和企业名称是否正确");
                continue;
            }
            if (manageKpiYears.size() == 0) {
                setFailedContent(result, String.format("第%s行的年度指标不存在", j + 1));
                cell.setCellValue("年度指标不存在，检查指标和年份和企业名称是否正确");
                continue;
            }
            //构建实体类
            ManageKpiMonthAim manageKpiMonthAim = new ManageKpiMonthAim();
            manageKpiMonthAim.setId(Integer.valueOf(id));
            manageKpiMonthAim.setAnalyzeDesc(analyzeDesc);
            manageKpiMonthAim.setAnalyzeRes(analyzeRes);
            manageKpiMonthAim.setEvaluateStatus(evaluateStatus);
            // 根据id进行判断，存在则更新，不存在则新增
            saveOrUpdate(manageKpiMonthAim);
            cell.setCellValue("导入成功");
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
        if (params.containsKey("month")) {
            queryWrapper.eq("month", params.get("month"));
        }
        if (params.containsKey("projectDesc")) {
            queryWrapper.like("projectDesc", params.get("projectDesc"));
        }
        if (params.containsKey("dataSource")) {
            queryWrapper.like("dataSource", params.get("dataSource"));
        }
        if (params.containsKey("evaluateStatus")) {
            queryWrapper.eq("evaluateStatus", params.get("evaluateStatus"));
        }
        if (params.containsKey("yearIsNotNull")) {
            queryWrapper.isNotNull("year");
        }
        if (params.containsKey("group")) {
            queryWrapper.groupBy((String) params.get("group"));
        }

        return queryWrapper;
    }
}