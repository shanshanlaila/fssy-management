package com.fssy.shareholder.management.service.system.impl.operate.invest;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fssy.shareholder.management.mapper.system.operate.invest.InvestProjectMonthMapper;
import com.fssy.shareholder.management.pojo.system.config.Attachment;
import com.fssy.shareholder.management.pojo.system.operate.invest.InvestProjectMonth;
import com.fssy.shareholder.management.service.common.SheetService;
import com.fssy.shareholder.management.service.system.operate.invest.InvestProjectMonthService;
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
 * **数据表名：	bs_operate_invest_project_month	**数据表中文名：	项目月度进展表	**业务部门：	经营管理部	**数据表作用：	记录 企业年度投资项目月度状态	**创建人创建日期：	TerryZeng 2022-12-2 服务实现类
 * </p>
 *
 * @author zzp
 * @since 2022-12-05
 */
@Service
public class InvestProjectMonthServiceImpl extends ServiceImpl<InvestProjectMonthMapper, InvestProjectMonth> implements InvestProjectMonthService {

    @Autowired
    private InvestProjectMonthMapper investProjectMonthMapper;
    @Autowired
    private SheetService sheetService;

    /**
     * 根据条件查询所有数据
     * @param params
     * @return
     */
    @Override
    public List<InvestProjectMonth> findInvestProjectMonthSDataByParams(Map<String, Object> params) {
        QueryWrapper<InvestProjectMonth> queryWrapper = getQueryWrapper(params);
        return investProjectMonthMapper.selectList(queryWrapper);
    }

    /**
     * 查询一年十二个月的数据，展示查询，包含条件查询
     * @param params
     * @return
     */
    @Override
    public Page<Map<String, Object>> findInvestProjectMonthDataMapListPerPageByParams(Map<String, Object> params) {
        QueryWrapper<InvestProjectMonth> queryWrapper = getQueryWrapper(params);
        int limit = (int)params.get("limit");
        int page = (int)params.get("page");
        Page<Map<String,Object>> myPage = new Page<>(page, limit);
        return investProjectMonthMapper.selectMapsPage(myPage, queryWrapper);
    }

    /**
     * 通过查询条件 分页查询列表
     * @param params 查询条件
     * @return 分页数据
     */
    @Override
    public Page<InvestProjectMonth> findInvestProjectMonthDataListPerPageByParams(Map<String, Object> params) {
        QueryWrapper<InvestProjectMonth> queryWrapper = getQueryWrapper(params);
        int limit = (int)params.get("limit");
        int page = (int)params.get("page");
        Page<InvestProjectMonth> myPage = new Page<>(page,limit);
        return investProjectMonthMapper.selectPage(myPage, queryWrapper);
    }

    /**
     * 通过查询条件 查询履职计划map数据 用于导出
     * @param params 查询条件
     * @return
     */
    @Override
    public List<Map<String, Object>> findInvestProjectMonthDataByParams(Map<String, Object> params) {
        QueryWrapper<InvestProjectMonth> queryWrapper = getQueryWrapper(params);
        return investProjectMonthMapper.selectMaps(queryWrapper);
    }

    /**
     * 附件进行导入
     * @param attachment 附件
     * @return
     */
    @Override
    public Map<String, Object> readInvestProjectMonthDataSource(Attachment attachment, String year) {
        // 返回消息
        Map<String, Object> result = new HashMap<>();
        result.put("content", "");
        result.put("failed", false);
        // 读取附件
        sheetService.load(attachment.getPath(), attachment.getFilename()); // 根据路径和名称读取附件
        // 读取表单
        sheetService.readByName("项目月度进展表"); //根据表单名称获取该工作表单
        // 获取表单数据
        Sheet sheet = sheetService.getSheet();
        if (ObjectUtils.isEmpty(sheet)) {
            throw new ServiceException("表单【项目月度进展表】不存在，无法读取数据，请检查");
        }

        // 获取单价列表数据
        Row row;
        List<InvestProjectMonth> investProjectMonths = new ArrayList<>(); //实体类集合，用于后面的批量写入数据库
        // 2022-06-01 从决策系统导出数据，存在最后几行为空白数据，导致报数据越界问题，这里的长度由表头长度控制
        short maxSize = sheet.getRow(0).getLastCellNum();//列数(表头长度)
        //获取excel中的年份
        Cell yearCell = sheet.getRow(3).getCell(SheetService.columnToIndex("C"));
        String yearCellValue = sheetService.getValue(yearCell);
        //校验年份
        if(!year.equals(yearCellValue)){
            throw new ServiceException("导入的年份与excel中的年份不一致，导入失败");
        }
        // 循环总行数(不读表的标题，从第1行开始读)
        for (int j = 3; j <= sheet.getLastRowNum(); j++) {// getPhysicalNumberOfRows()此方法不会将空白行计入行数
            //循环读取excel中的月份,写入数据库
            for (int a = 0; a < 12; a++) {
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
                Cell cell = row.createCell(SheetService.columnToIndex("V"));// 报错信息上传到excel V列（暂未实现）
                //构建实体类  InvestProjectMonth
                InvestProjectMonth investProjectMonth = new InvestProjectMonth();
                //边读边写
//                String id = cells.get(SheetService.columnToIndex("A"));
                String companyName = cells.get(SheetService.columnToIndex("B"));
                String yearExcel = cells.get(SheetService.columnToIndex("C"));
                String projectName = cells.get(SheetService.columnToIndex("D"));
//                investProjectMonth.setId(Integer.valueOf(id));
                investProjectMonth.setCompanyName(companyName);
                investProjectMonth.setYear(Integer.valueOf(yearExcel));
                investProjectMonth.setProjectName(projectName);
                if(a == 0){
                    String investmentVolumeMonth = cells.get(SheetService.columnToIndex("E"));
                    investProjectMonth.setMonth("1");
                    investProjectMonth.setInvestmentVolumeMonth(investmentVolumeMonth);
                }else if(a == 1){
                    String investmentVolumeMonth = cells.get(SheetService.columnToIndex("F"));
                    investProjectMonth.setMonth("2");
                    investProjectMonth.setInvestmentVolumeMonth(investmentVolumeMonth);
                }else if(a == 2){
                    String investmentVolumeMonth = cells.get(SheetService.columnToIndex("G"));
                    investProjectMonth.setMonth("3");
                    investProjectMonth.setInvestmentVolumeMonth(investmentVolumeMonth);
                }else if(a == 3){
                    String investmentVolumeMonth = cells.get(SheetService.columnToIndex("H"));
                    investProjectMonth.setMonth("4");
                    investProjectMonth.setInvestmentVolumeMonth(investmentVolumeMonth);
                }else if(a == 4){
                    String investmentVolumeMonth = cells.get(SheetService.columnToIndex("I"));
                    investProjectMonth.setMonth("5");
                    investProjectMonth.setInvestmentVolumeMonth(investmentVolumeMonth);
                }else if(a == 5){
                    String investmentVolumeMonth = cells.get(SheetService.columnToIndex("J"));
                    investProjectMonth.setMonth("6");
                    investProjectMonth.setInvestmentVolumeMonth(investmentVolumeMonth);
                }else if(a == 6){
                    String investmentVolumeMonth = cells.get(SheetService.columnToIndex("K"));
                    investProjectMonth.setMonth("7");
                    investProjectMonth.setInvestmentVolumeMonth(investmentVolumeMonth);
                }else if(a == 7){
                    String investmentVolumeMonth = cells.get(SheetService.columnToIndex("L"));
                    investProjectMonth.setMonth("8");
                    investProjectMonth.setInvestmentVolumeMonth(investmentVolumeMonth);
                }else if(a == 8){
                    String investmentVolumeMonth = cells.get(SheetService.columnToIndex("M"));
                    investProjectMonth.setMonth("9");
                    investProjectMonth.setInvestmentVolumeMonth(investmentVolumeMonth);
                }else if(a == 9){
                    String investmentVolumeMonth = cells.get(SheetService.columnToIndex("N"));
                    investProjectMonth.setMonth("10");
                    investProjectMonth.setInvestmentVolumeMonth(investmentVolumeMonth);
                }else if(a == 10){
                    String investmentVolumeMonth = cells.get(SheetService.columnToIndex("O"));
                    investProjectMonth.setMonth("11");
                    investProjectMonth.setInvestmentVolumeMonth(investmentVolumeMonth);
                }else if(a == 11){
                    String investmentVolumeMonth = cells.get(SheetService.columnToIndex("P"));
                    investProjectMonth.setMonth("12");
                    investProjectMonth.setInvestmentVolumeMonth(investmentVolumeMonth);
                }
                String investmentVolumeAccumulate = cells.get(SheetService.columnToIndex("Q"));
                String projectStatusMonth = cells.get(SheetService.columnToIndex("R"));
                String projectRiskMonth = cells.get(SheetService.columnToIndex("S"));
                String monthIssue = cells.get(SheetService.columnToIndex("T"));
                String improveAction = cells.get(SheetService.columnToIndex("U"));
                investProjectMonth.setInvestmentVolumeAccumulate(investmentVolumeAccumulate);
                investProjectMonth.setProjectStatusMonth(projectStatusMonth);
                investProjectMonth.setProjectRiskMonth(projectRiskMonth);
                investProjectMonth.setMonthIssue(monthIssue);
                investProjectMonth.setImproveAction(improveAction);

                //根据id进行导入，存在则更新，不存在则新增，并且每次导入都会刷新分数，回归待计算状态
//                saveOrUpdate(investProjectMonth);
                investProjectMonthMapper.insert(investProjectMonth);
                cell.setCellValue("导入成功");
                //插入数据,先判断数据库中是否有此条数据，有则更新，无则插入
//                QueryWrapper<InvestProjectMonth> queryWrapperS = new QueryWrapper<>();
//                queryWrapperS.eq("companyName",companyName).eq("year",yearExcel)
//                        .eq("projectName",projectName);
//                List<Map<String, Object>> maps = investProjectMonthMapper.selectMaps(queryWrapperS);
//                investProjectMonthMapper.insert(investProjectMonth);

            }
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
     * 以主键删除 项目月度进展记录
     * @param id
     * @return
     */
    @Override
    public boolean deleteInvestProjectMonthDataById(Integer id) {
        int result = investProjectMonthMapper.deleteById(id);
        if (result > 0) {
            return true;
        }
        return false;
    }

    /**
     * 修改某条记录
     * @param investProjectMonth
     * @return
     */
    @Override
    public boolean updateInvestProjectMonthData(InvestProjectMonth investProjectMonth) {
        int result = investProjectMonthMapper.updateById(investProjectMonth);
        if (result > 0) {
            return true;
        }
        return false;
    }

    /**
     * 查询条件 在项目月度进展数据库中进行查询
     * @param params
     * @return
     */
    private QueryWrapper<InvestProjectMonth> getQueryWrapper(Map<String, Object> params) {
        QueryWrapper<InvestProjectMonth> queryWrapper = new QueryWrapper<>();
        int month = 1;
        StringBuilder selectStr = new StringBuilder("companyName,year,projectName,investmentVolumeAccumulate,projectStatusMonth,projectRiskMonth,monthIssue,improveAction");
        do{
            selectStr.append(", sum(if(MONTH =" +  month + ",investmentVolumeMonth,null)) AS 'month" + month + "'");
            month++;
        }while (month <= 12);
        queryWrapper.select(selectStr.toString()).groupBy("companyName,projectName,year");
        if (params.containsKey("select")) {
            queryWrapper.select((String) params.get("select"));
        }
        if (params.containsKey("projectName")) {
            queryWrapper.eq("projectName", params.get("projectName"));
        }
        if (params.containsKey("year")) {
            queryWrapper.like("year", params.get("year"));
        }
        if (params.containsKey("month")) {
            queryWrapper.like("month", params.get("month"));
        }
        if (params.containsKey("investmentVolumeMonth")) {
            queryWrapper.eq("investmentVolumeMonth", params.get("investmentVolumeMonth"));
        }
        if (params.containsKey("investmentVolumeAccumulate")) {
            queryWrapper.eq("investmentVolumeAccumulate", params.get("investmentVolumeAccumulate"));
        }
        if (params.containsKey("projectStatusMonth")) {
            queryWrapper.eq("projectStatusMonth", params.get("projectStatusMonth"));
        }
        if (params.containsKey("projectRiskMonth")) {
            queryWrapper.eq("projectRiskMonth", params.get("projectRiskMonth"));
        }
        if (params.containsKey("monthIssue")) {
            queryWrapper.eq("monthIssue", params.get("monthIssue"));
        }
        if (params.containsKey("improveAction")) {
            queryWrapper.eq("improveAction", params.get("improveAction"));
        }
        return queryWrapper;
    }

}
