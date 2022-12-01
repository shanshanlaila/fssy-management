package com.fssy.shareholder.management.service.system.impl.performance.manager;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fssy.shareholder.management.mapper.system.performance.manager.DepityManagerQualitativeEvalMapper;
import com.fssy.shareholder.management.mapper.system.performance.manager.ManagerQualitativeEvalStdMapper;
import com.fssy.shareholder.management.pojo.system.config.Attachment;
import com.fssy.shareholder.management.pojo.system.performance.manager.ManagerQualitativeEval;
import com.fssy.shareholder.management.pojo.system.performance.manager.ManagerQualitativeEvalStd;
import com.fssy.shareholder.management.service.common.SheetService;
import com.fssy.shareholder.management.service.system.performance.manager.DepityManagerQualitativeEvalService;
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
import java.util.stream.Collectors;

/**
 * <p>
 * **数据表名：	bs_manager_qualitative_eval	**数据表中文名：	经理人绩效定性评价分数表	**业务部门：	人力资源部	**数据表作用：	记录企业年度总经理、分管经理人定性评价分数 服务实现类
 * </p>
 *
 * @author zzp
 * @since 2022-11-28
 */
@Service
public class DepityManagerQualitativeEvalServiceImpl extends ServiceImpl<DepityManagerQualitativeEvalMapper, ManagerQualitativeEval> implements DepityManagerQualitativeEvalService {

    @Autowired
    private ManagerQualitativeEvalStdMapper managerQualitativeEvalStdMapper;
    @Autowired
    private DepityManagerQualitativeEvalMapper depityManagerQualitativeEvalMapper;
    @Autowired
    private SheetService sheetService;

    /**
     * 根据条件查询所有数据
     * @param params
     * @return
     */
    @Override
    public List<ManagerQualitativeEval> findManagerQualitativeEvalSDataByParams(Map<String, Object> params) {
        QueryWrapper<ManagerQualitativeEval> queryWrapper = getQueryWrapperManager(params);
        return depityManagerQualitativeEvalMapper.selectList(queryWrapper);
    }

    /**
     * 通过查询条件 分页查询列表
     * @param params 查询条件
     * @return 分页数据
     */
    @Override
    public Page<ManagerQualitativeEval> findManagerQualitativeEvalDataListPerPageByParams(Map<String, Object> params) {
        QueryWrapper<ManagerQualitativeEval> queryWrapper = getQueryWrapperManager(params);
        int limit = (int)params.get("limit");
        int page = (int)params.get("page");
        Page<ManagerQualitativeEval> myPage = new Page<>(page,limit);
        return depityManagerQualitativeEvalMapper.selectPage(myPage, queryWrapper);
    }

    /**
     * 通过查询条件 查询履职计划map数据 用于导出
     * @param params 查询条件
     * @return
     */
    @Override
    public List<Map<String, Object>> findManagerQualitativeEvalDataByParams(Map<String, Object> params) {
        QueryWrapper<ManagerQualitativeEval> queryWrapper = getQueryWrapperManager(params);
        return depityManagerQualitativeEvalMapper.selectMaps(queryWrapper);
    }

    /**
     * 附件进行导入
     * @param attachment 附件
     * @return
     */
    @Override
    public Map<String, Object> readManagerQualitativeEvalDataSource(Attachment attachment,String year) {
        // 返回消息
        Map<String, Object> result = new HashMap<>();
        result.put("content", "");
        result.put("failed", false);
        // 读取附件
        sheetService.load(attachment.getPath(), attachment.getFilename()); // 根据路径和名称读取附件
        // 读取表单
        sheetService.readByName("分管经理定性评价"); //根据表单名称获取该工作表单
        // 获取表单数据
        Sheet sheet = sheetService.getSheet();
        if (ObjectUtils.isEmpty(sheet)) {
            throw new ServiceException("表单【分管经理定性评价】不存在，无法读取数据，请检查");
        }

        // 获取单价列表数据
        Row row;
        List<ManagerQualitativeEval> managerQualitativeEvals = new ArrayList<>(); //实体类集合，用于后面的批量写入数据库
        // 2022-06-01 从决策系统导出数据，存在最后几行为空白数据，导致报数据越界问题，这里的长度由表头长度控制
        short maxSize = sheet.getRow(0).getLastCellNum();//列数(表头长度)
        //获取excel中的年份
        Cell yearCell = sheet.getRow(5).getCell(SheetService.columnToIndex("E"));
        String yearCellValue = sheetService.getValue(yearCell);
        //校验年份
        if(!year.equals(yearCellValue)){
            throw new ServiceException("导入的年份与excel中的年份不一致，导入失败");
        }
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
            Cell cell = row.createCell(SheetService.columnToIndex("R"));// 报错信息上传到excel R列（暂未实现）
            String id = cells.get(SheetService.columnToIndex("A"));
            String managerName = cells.get(SheetService.columnToIndex("B"));
            String companyName = cells.get(SheetService.columnToIndex("C"));
            String position = cells.get(SheetService.columnToIndex("D"));
            String yearExcel = cells.get(SheetService.columnToIndex("E"));
            String skillScore = cells.get(SheetService.columnToIndex("F"));
            String democraticEvalScore = cells.get(SheetService.columnToIndex("G"));
            String superiorEvalScore = cells.get(SheetService.columnToIndex("H"));

            //构建实体类  ManagerQualitativeEval
            ManagerQualitativeEval managerQualitativeEval = new ManagerQualitativeEval();
//            managerQualitativeEval.setId(Integer.valueOf(id));
            managerQualitativeEval.setManagerName(managerName);
            managerQualitativeEval.setCompanyName(companyName);
            managerQualitativeEval.setPosition(position);
            managerQualitativeEval.setYear(Integer.valueOf(yearExcel));
            managerQualitativeEval.setSkillScore(Double.valueOf(skillScore));
            managerQualitativeEval.setDemocraticEvalScore(Double.valueOf(democraticEvalScore));
            managerQualitativeEval.setSuperiorEvalScore(Double.valueOf(superiorEvalScore));
            managerQualitativeEval.setStatus("待计算");
            managerQualitativeEval.setGeneralManager("否");

            //根据id进行导入，存在则更新，不存在则新增，并且每次导入都会刷新分数，回归待计算状态
            saveOrUpdate(managerQualitativeEval);
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
        int result = depityManagerQualitativeEvalMapper.deleteById(id);
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
        int result = depityManagerQualitativeEvalMapper.updateById(managerQualitativeEval);
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
        //获取前端传进来的年份,进行数据查询
        String yearValue = (String) params.get("year");
        //因为数据库中的字段类型是int,前端传入是String,在使用equals时需保证与数据库字段相同,所以在此将类型转为int
        int year = Integer.parseInt(yearValue);
        /**
         * step1:分别条件查询出分管经理定性评价表和比例表的相关记录
         */
        //1.1 查询出分管经理表中的相关年份并且待计算的所有记录,存入filterListManager
        QueryWrapper<ManagerQualitativeEval> queryWrapperManger = getQueryWrapperManager(params);
        List<ManagerQualitativeEval> managerQualitativeEvals = depityManagerQualitativeEvalMapper.selectList(queryWrapperManger);
        List<ManagerQualitativeEval> filterListManager = managerQualitativeEvals.stream()
                .filter(i -> i.getStatus().equals("待计算")  && (i.getYear().equals(year))).collect(Collectors.toList());
        if (ObjectUtils.isEmpty(filterListManager)) {
            throw new ServiceException("没有查出数据或已生成！生成失败！");
        }
        //1.2 查询出比例表中的相关年份的所有记录,存入filterListStd
        QueryWrapper<ManagerQualitativeEvalStd> queryWrapperStd = getQueryWrapperStd(params);
        List<ManagerQualitativeEvalStd> managerQualitativeEvalStds = managerQualitativeEvalStdMapper.selectList(queryWrapperStd);
        List<ManagerQualitativeEvalStd> filterListStd = managerQualitativeEvalStds.stream()
                .filter(i -> i.getYear().equals(year)).collect(Collectors.toList());
        if (ObjectUtils.isEmpty(filterListStd)) {
            throw new ServiceException("没有查出数据或已生成！生成失败！");
        }
        /**
         * step2:获取查询到的比例表记录中的分管经理相关比例指标
         */
        //2.1 创建接收比例指标的变量
        double skillScoreR = 0;
        double democraticEvalScoreR  = 0;
        double superiorEvalScoreR = 0;
        int evalStdId = -1;
        //2.2 拿出记录中的相关值
        for (ManagerQualitativeEvalStd temp : filterListStd) {
            skillScoreR = temp.getSkillScoreR();
            democraticEvalScoreR = temp.getDemocraticEvalScoreR();
            superiorEvalScoreR = temp.getSuperiorEvalScoreR();
            evalStdId = temp.getId();
        }
        /**
         * step3:对查询到的记录进行循环遍历计算分数,并插入数据库
         */
        //3.1 遍历记录,进行分数计算
        for (ManagerQualitativeEval temp: filterListManager) {
            //3.2 获取词条记录中的s d s三个指标的分数,然后与比例记录中的值进行计算
            double skillScore = temp.getSkillScore() * skillScoreR;
            double democraticEvalScore = temp.getDemocraticEvalScore() * democraticEvalScoreR;
            double superiorEvalScore = temp.getSuperiorEvalScore() * superiorEvalScoreR;
            //3.3 计算出总和(自动),并初始化合计(人工)
            double qualitativeEvalScoreAuto = skillScore + democraticEvalScore + superiorEvalScore;
            double qualitativeEvalScoreAdjust = qualitativeEvalScoreAuto;
            //3.4 将分数插入数据库
            ManagerQualitativeEval managerQualitativeEval = new ManagerQualitativeEval();
            managerQualitativeEval.setSkillScore(skillScore);
            managerQualitativeEval.setDemocraticEvalScore(democraticEvalScore);
            managerQualitativeEval.setSuperiorEvalScore(superiorEvalScore);
            managerQualitativeEval.setQualitativeEvalScoreAuto(qualitativeEvalScoreAuto);
            managerQualitativeEval.setQualitativeEvalScoreAdjust(qualitativeEvalScoreAdjust);
            managerQualitativeEval.setStatus("已计算");
            managerQualitativeEval.setEvalStdId(evalStdId);
            // 根据经理人姓名,年份,公司名称进行判断，存在则更新，不存在则新增
            UpdateWrapper<ManagerQualitativeEval> UpdateWrapper = new UpdateWrapper<>();
            UpdateWrapper.set("skillScore", skillScore)
                    .set("democraticEvalScore", democraticEvalScore).set("superiorEvalScore", superiorEvalScore)
                    .set("qualitativeEvalScoreAuto",qualitativeEvalScoreAuto).set("evalStdId",evalStdId)
                    .set("qualitativeEvalScoreAdjust",qualitativeEvalScoreAdjust).set("status","已计算")
                    .eq("managerName", temp.getManagerName()).eq("year", temp.getYear())
                    .eq("companyName", temp.getCompanyName());
            depityManagerQualitativeEvalMapper.update(null, UpdateWrapper);
        }
        return true;
    }

    /**
     * 查询条件 在总经理定性评价数据库中进行查询
     * @param params
     * @return
     */
    private QueryWrapper<ManagerQualitativeEval> getQueryWrapperManager(Map<String, Object> params) {
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
        if (params.containsKey("evalStdId")) {
            queryWrapper.eq("evalStdId", params.get("evalStdId"));
        }
        return queryWrapper;
    }

    /**
     * 查询条件 在项目比例数据库中进行查询
     * @param params
     * @return
     */
    private QueryWrapper<ManagerQualitativeEvalStd> getQueryWrapperStd(Map<String, Object> params){
        QueryWrapper<ManagerQualitativeEvalStd> queryWrapper = new QueryWrapper<>();
        if (params.containsKey("year")) {
            queryWrapper.eq("year", params.get("year"));
        }
        if (params.containsKey("skillScoreR")) {
            queryWrapper.eq("skillScoreR", params.get("skillScoreR"));
        }
        if (params.containsKey("democraticEvalScoreR")) {
            queryWrapper.eq("democraticEvalScoreR", params.get("democraticEvalScoreR"));
        }
        if (params.containsKey("superiorEvalScoreR")) {
            queryWrapper.eq("superiorEvalScoreR", params.get("superiorEvalScoreR"));
        }
        if (params.containsKey("auditEvalScoreR")) {
            queryWrapper.eq("auditEvalScoreR", params.get("auditEvalScoreR"));
        }
        if (params.containsKey("financialAuditScoreR")) {
            queryWrapper.eq("financialAuditScoreR", params.get("financialAuditScoreR"));
        }
        if (params.containsKey("operationScoreR")) {
            queryWrapper.eq("operationScoreR", params.get("operationScoreR"));
        }
        if (params.containsKey("leadershipScoreR")) {
            queryWrapper.eq("leadershipScoreR", params.get("leadershipScoreR"));
        }
        if (params.containsKey("investScoreR")) {
            queryWrapper.eq("investScoreR", params.get("investScoreR"));
        }
        if (params.containsKey("workReportScoreR")) {
            queryWrapper.eq("workReportScoreR", params.get("workReportScoreR"));
        }
        return queryWrapper;
    }
}
