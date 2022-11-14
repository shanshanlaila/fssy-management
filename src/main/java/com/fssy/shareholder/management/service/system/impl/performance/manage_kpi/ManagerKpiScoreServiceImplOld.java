package com.fssy.shareholder.management.service.system.impl.performance.manage_kpi;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.pojo.system.config.Attachment;
import com.fssy.shareholder.management.pojo.system.performance.manage_kpi.ManageKpiLib;
import com.fssy.shareholder.management.pojo.system.performance.manage_kpi.ManageKpiMonthPerformance;
import com.fssy.shareholder.management.pojo.system.performance.manage_kpi.ManagerKpiScoreOld;
import com.fssy.shareholder.management.mapper.system.performance.manage_kpi.ManagerKpiScoreMapperOld;
import com.fssy.shareholder.management.pojo.system.performance.manage_kpi.ViewManagerKpiMonth;
import com.fssy.shareholder.management.service.common.SheetService;
import com.fssy.shareholder.management.service.system.performance.manage_kpi.ManagerKpiScoreServiceOld;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
 * 经理人绩效分数表		【修改内容】增加了年度关联ID字段	【修改时间】2022-10-27	【修改人】张泽鹏 服务实现类
 * </p>
 *
 * @author zzp
 * @since 2022-10-31
 */
@Service
public class ManagerKpiScoreServiceImplOld extends ServiceImpl<ManagerKpiScoreMapperOld, ManagerKpiScoreOld> implements ManagerKpiScoreServiceOld {

    @Autowired
    private SheetService sheetService;

    @Autowired
    private ManagerKpiScoreMapperOld managerKpiScoreMapper;

    /**
     * 通过查询条件 分页 查询列表
     *
     * @param params 查询条件
     * @return 分页数据
     */
    @Override
    public Page<ManagerKpiScoreOld> findViewManagerKpiMonthDataListPerPageByParams(Map<String, Object> params) {
        QueryWrapper<ManagerKpiScoreOld> queryWrapper = getQueryWrapper(params);
        int limit = (int)params.get("limit");
        int page = (int)params.get("page");
        Page<ManagerKpiScoreOld> myPage = new Page<>(page,limit);
        return managerKpiScoreMapper.selectPage(myPage, queryWrapper);
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
     * 导入附件
     * @param attachment 附件
     * @return
     */
    @Override
    @Transactional
    public Map<String, Object> readManagerKpiScoreOldDataSource(Attachment attachment) {
        // 返回消息
        Map<String, Object> result = new HashMap<>();
        result.put("content", "");
        result.put("failed", false);
        // 读取附件
        sheetService.load(attachment.getPath(), attachment.getFilename()); // 根据路径和名称读取附件
        // 读取表单
        sheetService.readByName("经理人KPI分数表"); //根据表单名称获取该工作表单
        // 获取表单数据
        Sheet sheet = sheetService.getSheet();
        if (ObjectUtils.isEmpty(sheet)) {
            throw new ServiceException("表单【经理人KPI分数表】不存在，无法读取数据，请检查");
        }

        // 获取单价列表数据
        Row row;
        List<ManagerKpiScoreOld> managerKpiScoreOlds = new ArrayList<>(); //实体类集合，用于后面的批量写入数据库
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
            Cell cell = row.createCell(SheetService.columnToIndex("R"));// 报错信息上传到excel R列（暂未实现）
            String year = cells.get(SheetService.columnToIndex("A"));
            String managerName = cells.get(SheetService.columnToIndex("B"));
            String companyName = cells.get(SheetService.columnToIndex("C"));
            String position = cells.get(SheetService.columnToIndex("D"));
            String generalManager = cells.get(SheetService.columnToIndex("E"));
            String month = cells.get(SheetService.columnToIndex("F"));
            String businessScore = cells.get(SheetService.columnToIndex("G"));
            String incentiveScore = cells.get(SheetService.columnToIndex("H"));
            String difficultyCoefficient = cells.get(SheetService.columnToIndex("I"));
            String generalManagerScore = cells.get(SheetService.columnToIndex("J"));
            String scoreAdjust = cells.get(SheetService.columnToIndex("K"));
            String scoreAuto = cells.get(SheetService.columnToIndex("L"));
            String advantageAnalyze = cells.get(SheetService.columnToIndex("M"));
            String disadvantageAnalyze = cells.get(SheetService.columnToIndex("N"));
            String riskDesc = cells.get(SheetService.columnToIndex("O"));
            String respDepartment = cells.get(SheetService.columnToIndex("P"));
            String groupImproveAction = cells.get(SheetService.columnToIndex("Q"));
            // 检查必填项
            if (ObjectUtils.isEmpty(companyName)) {
                setFailedContent(result, String.format("第%s行的经理人姓名是空的", j + 1));
                cell.setCellValue("经理人姓名是空的");
                throw new ServiceException("表中有空值");
            }

            //构建实体类
            ManagerKpiScoreOld managerKpiScoreOld = new ManagerKpiScoreOld();
            managerKpiScoreOld.setYear(Integer.valueOf(year));
            managerKpiScoreOld.setManagerName(managerName);
            managerKpiScoreOld.setCompanyName(companyName);
            managerKpiScoreOld.setPosition(position);
            managerKpiScoreOld.setGeneralManager(generalManager);
            managerKpiScoreOld.setMonth(Integer.valueOf(month));
            managerKpiScoreOld.setBusinessScore(new BigDecimal(businessScore));
            managerKpiScoreOld.setIncentiveScore(new BigDecimal(incentiveScore));
            if (!ObjectUtils.isEmpty(difficultyCoefficient)) {
                managerKpiScoreOld.setDifficultyCoefficient(new BigDecimal(difficultyCoefficient));
            }
            if (!ObjectUtils.isEmpty(generalManagerScore)) {
                managerKpiScoreOld.setGeneralManagerScore(new BigDecimal(generalManagerScore));
            }
            managerKpiScoreOld.setScoreAuto(new BigDecimal(scoreAuto));
            managerKpiScoreOld.setScoreAdjust(new BigDecimal(scoreAdjust));
            managerKpiScoreOld.setAdvantageAnalyze(advantageAnalyze);
            managerKpiScoreOld.setDisadvantageAnalyze(disadvantageAnalyze);
            managerKpiScoreOld.setRiskDesc(riskDesc);
            managerKpiScoreOld.setRespDepartment(respDepartment);
            managerKpiScoreOld.setGroupImproveAction(groupImproveAction);

            // 根据id进行判断，存在则更新，不存在则新增
            saveOrUpdate(managerKpiScoreOld);
            cell.setCellValue("导入成功");

        }

        sheetService.write(attachment.getPath(), attachment.getFilename());// 写入excel表
        return result;
    }


    /**
     * 通过查询条件查询履职计划map数据，用于导出
     *
     * @param params 查询条件
     * @return 数据列表
     */
    @Override
    public List<Map<String, Object>> findManagerKpiScoreOldDataByParams(Map<String, Object> params) {
        QueryWrapper<ManagerKpiScoreOld> queryWrapper = getQueryWrapper(params);
        return managerKpiScoreMapper.selectMaps(queryWrapper);
    }

    /**
     * 传入参数，对参数进行处理(查询条件)
     * @param params
     * @return
     */
    private QueryWrapper<ManagerKpiScoreOld> getQueryWrapper(Map<String, Object> params) {
        QueryWrapper<ManagerKpiScoreOld> queryWrapper = new QueryWrapper<>();
        if (params.containsKey("select")) {
            queryWrapper.select((String) params.get("select"));
        }
        if (params.containsKey("managerName")) {
            queryWrapper.eq("managerName", params.get("managerName"));
        }
        if (params.containsKey("companyName")) {
            queryWrapper.like("companyName", params.get("companyName"));
        }
        if (params.containsKey("generalManager")) {
            queryWrapper.like("generalManager", params.get("generalManager"));
        }
        if (params.containsKey("year")) {
            queryWrapper.eq("year", params.get("year"));
        }
        if (params.containsKey("month")) {
            queryWrapper.eq("month", params.get("month"));
        }
        return queryWrapper;
    }

    /**
     * 删除分数记录
     */
    @Override
    public boolean deleteManagerKpiScoreOldDataById(Integer id) {
        int result = managerKpiScoreMapper.deleteById(id);
        if (result > 0) {
            return true;
        }
        return false;
    }

    /**
     * 修改分数信息
     */
    @Override
    public boolean updateManagerKpiScoreOldData(ManagerKpiScoreOld managerKpiScoreOld) {
        int result = managerKpiScoreMapper.updateById(managerKpiScoreOld);
        if (result > 0) {
            return true;
        }
        return false;
    }
}
