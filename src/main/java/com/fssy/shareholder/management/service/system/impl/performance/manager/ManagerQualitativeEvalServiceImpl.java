package com.fssy.shareholder.management.service.system.impl.performance.manager;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fssy.shareholder.management.mapper.system.hr.performance.manager.ManagerQualitativeEvalMapper;
import com.fssy.shareholder.management.pojo.system.config.Attachment;
import com.fssy.shareholder.management.pojo.system.hr.performance.manager.ManagerQualitativeEval;
import com.fssy.shareholder.management.service.common.SheetService;
import com.fssy.shareholder.management.service.system.hr.performance.manager.ManagerQualitativeEvalService;
import com.fssy.shareholder.management.tools.exception.ServiceException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * **数据表名：	bs_manager_qualitative_eval	**数据表中文名：	经理人绩效定性评价分数表	**业务部门：	人力资源部	**数据表作用：	记录企业年度总经理、分管经理人定性评价分数 服务实现类
 * </p>
 *
 * @author zzp
 * @since 2022-11-28
 */
@Service
public class ManagerQualitativeEvalServiceImpl extends ServiceImpl<ManagerQualitativeEvalMapper, ManagerQualitativeEval> implements ManagerQualitativeEvalService {

    @Autowired
    private ManagerQualitativeEvalMapper managerQualitativeEvalMapper;
    @Autowired
    private SheetService sheetService;

    /**
     * 根据条件查询所有数据
     * @param params
     * @return
     */
    @Override
    public List<ManagerQualitativeEval> findManagerQualitativeEvalSDataByParams(Map<String, Object> params) {
        QueryWrapper<ManagerQualitativeEval> queryWrapper = getQueryWrapper(params);
        return managerQualitativeEvalMapper.selectList(queryWrapper);
    }

    /**
     * 通过查询条件 分页查询列表
     * @param params 查询条件
     * @return 分页数据
     */
    @Override
    public Page<ManagerQualitativeEval> findManagerQualitativeEvalDataListPerPageByParams(Map<String, Object> params) {
        QueryWrapper<ManagerQualitativeEval> queryWrapper = getQueryWrapper(params);
        int limit = (int)params.get("limit");
        int page = (int)params.get("page");
        Page<ManagerQualitativeEval> myPage = new Page<>(page,limit);
        return managerQualitativeEvalMapper.selectPage(myPage, queryWrapper);
    }

    /**
     * 通过查询条件 查询履职计划map数据 用于导出
     * @param params 查询条件
     * @return
     */
    @Override
    public List<Map<String, Object>> findManagerQualitativeEvalDataByParams(Map<String, Object> params) {
        QueryWrapper<ManagerQualitativeEval> queryWrapper = getQueryWrapper(params);
        return managerQualitativeEvalMapper.selectMaps(queryWrapper);
    }

    /**
     * 附件进行导入
     * @param attachment 附件
     * @return
     */
    @Override
    public Map<String, Object> readManagerQualitativeEvalDataSource(Attachment attachment) {
        // 返回消息
        Map<String, Object> result = new HashMap<>();
        result.put("content", "");
        result.put("failed", false);
        // 读取附件
        sheetService.load(attachment.getPath(), attachment.getFilename()); // 根据路径和名称读取附件
        // 读取表单
        sheetService.readByName("总经理定性评价"); //根据表单名称获取该工作表单
        // 获取表单数据
        Sheet sheet = sheetService.getSheet();
        if (ObjectUtils.isEmpty(sheet)) {
            throw new ServiceException("表单【总经理定性评价】不存在，无法读取数据，请检查");
        }

        // 获取单价列表数据
        Row row;
        List<ManagerQualitativeEval> managerQualitativeEvals = new ArrayList<>(); //实体类集合，用于后面的批量写入数据库
        // 2022-06-01 从决策系统导出数据，存在最后几行为空白数据，导致报数据越界问题，这里的长度由表头长度控制
        short maxSize = sheet.getRow(0).getLastCellNum();//列数(表头长度)
        // 循环总行数(不读表的标题，从第1行开始读)
        for (int j = 5; j <= sheet.getLastRowNum(); j++) {// getPhysicalNumberOfRows()此方法不会将空白行计入行数
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
            Cell cell = row.createCell(SheetService.columnToIndex("Q"));// 报错信息上传到excel R列（暂未实现）
            String managerName = cells.get(SheetService.columnToIndex("B"));
            String companyName = cells.get(SheetService.columnToIndex("C"));
            String position = cells.get(SheetService.columnToIndex("D"));
            String year = cells.get(SheetService.columnToIndex("E"));
            String auditEvalScore = cells.get(SheetService.columnToIndex("F"));
            String financialAuditScore = cells.get(SheetService.columnToIndex("G"));
            String operationScore = cells.get(SheetService.columnToIndex("H"));
            String leadershipScore = cells.get(SheetService.columnToIndex("I"));
            String investScore = cells.get(SheetService.columnToIndex("J"));
            String workReportScore = cells.get(SheetService.columnToIndex("K"));
            // 检查必填项
            if (ObjectUtils.isEmpty(managerName)) {
                setFailedContent(result, String.format("第%s行的经理人姓名是空的", j + 1));
                cell.setCellValue("经理人姓名是空的");
                throw new ServiceException("表中有空值");
            }

            //构建实体类  ManagerQualitativeEval
            ManagerQualitativeEval managerQualitativeEval = new ManagerQualitativeEval();
            managerQualitativeEval.setManagerName(managerName);
            managerQualitativeEval.setCompanyName(companyName);
            managerQualitativeEval.setPosition(position);
            managerQualitativeEval.setYear(Integer.valueOf(year));
            managerQualitativeEval.setAuditEvalScore(Double.valueOf(auditEvalScore));
            managerQualitativeEval.setFinancialAuditScore(Double.valueOf(financialAuditScore));
            managerQualitativeEval.setOperationScore(Double.valueOf(operationScore));
            managerQualitativeEval.setLeadershipScore(Double.valueOf(leadershipScore));
            managerQualitativeEval.setInvestScore(Double.valueOf(investScore));
            managerQualitativeEval.setWorkReportScore(Double.valueOf(workReportScore));

            // 根据经理人姓名,年份,职位,公司名称进行判断，存在则更新，不存在则新增
            UpdateWrapper<ManagerQualitativeEval> managerQualitativeEvalUpdateWrapper = new UpdateWrapper<>();
            managerQualitativeEvalUpdateWrapper.set("auditEvalScore", auditEvalScore)
                    .set("financialAuditScore", financialAuditScore).set("operationScore", operationScore)
                    .set("leadershipScore", leadershipScore).set("investScore", investScore)
                    .set("workReportScore",workReportScore)
                    .eq("managerName", managerName).eq("year", year)
                    .eq("companyName", companyName).eq("position", position);
            managerQualitativeEvalMapper.update(null, managerQualitativeEvalUpdateWrapper);
            cell.setCellValue("导入成功");

        }

        sheetService.write(attachment.getPath(), attachment.getFilename());// 写入excel表
        return result;
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
     * 删除分数记录
     * @param id
     * @return
     */
    @Override
    public boolean deleteManagerQualitativeEvalDataById(Integer id) {
        int result = managerQualitativeEvalMapper.deleteById(id);
        if (result > 0) {
            return true;
        }
        return false;
    }

    /**
     * 修改分数记录
     * @param managerQualitativeEval
     * @return
     */
    @Override
    public boolean updateManagerQualitativeEvalData(ManagerQualitativeEval managerQualitativeEval) {
        int result = managerQualitativeEvalMapper.updateById(managerQualitativeEval);
        if (result > 0) {
            return true;
        }
        return false;
    }

    /**
     * 计算分数
     * @param params
     * @return
     */
    @Override
    public boolean createScore(Map<String, Object> params) {
        return false;
    }

    /**
     * 查询条件 在数据库中进行查询
     * @param params
     * @return
     */
    private QueryWrapper<ManagerQualitativeEval> getQueryWrapper(Map<String, Object> params) {
        QueryWrapper<ManagerQualitativeEval> queryWrapper = new QueryWrapper<>();
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
        if (params.containsKey("id")) {
            queryWrapper.eq("id", params.get("id"));
        }
        return queryWrapper;
    }
}
