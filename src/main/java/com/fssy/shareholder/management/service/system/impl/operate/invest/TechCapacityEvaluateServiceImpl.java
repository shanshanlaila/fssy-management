package com.fssy.shareholder.management.service.system.impl.operate.invest;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.mapper.system.operate.invest.TechCapacityEvaluateResMapper;
import com.fssy.shareholder.management.pojo.system.config.Attachment;
import com.fssy.shareholder.management.pojo.system.operate.invest.TechCapacityEvaluate;
import com.fssy.shareholder.management.mapper.system.operate.invest.TechCapacityEvaluateMapper;
import com.fssy.shareholder.management.pojo.system.operate.invest.TechCapacityEvaluateRes;
import com.fssy.shareholder.management.service.common.SheetService;
import com.fssy.shareholder.management.service.system.operate.invest.TechCapacityEvaluateService;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * **数据表名：	bs_operat_tech_capacity_evaluate	**数据表中文名：	企业研发工艺能力评价表	**业务部门：	经营管理部	**数据表作用：	记录 企业研发工艺能力年度评价项目、存在问题及改善点	**创建人创建日期：	TerryZeng 2022-12-2 服务实现类
 * </p>
 *
 * @author Solomon
 * @since 2022-12-02
 */
@Service
public class TechCapacityEvaluateServiceImpl extends ServiceImpl<TechCapacityEvaluateMapper, TechCapacityEvaluate> implements TechCapacityEvaluateService {

    @Autowired
    private SheetService sheetService;

    @Autowired
    private TechCapacityEvaluateMapper operateTechCapacityEvaluateMapper;

    @Autowired
    private TechCapacityEvaluateResMapper techCapacityEvaluateResMapper;


    //查询所有企业研发工艺能力评价
    @Override
    public List<TechCapacityEvaluate>findOperateTechCapacityEvaluateDataByParams(Map<String, Object> params) {
        QueryWrapper<TechCapacityEvaluate> queryWrapper = getQueryWrapper(params);
        return operateTechCapacityEvaluateMapper.selectList(queryWrapper);

    }

    /**
     * 分页查询企业研发工艺能力评价信息
     * @param params
     * @return
     */
    @Override
    public Page<TechCapacityEvaluate> findOperateTechCapacityEvaluateDataListPerPageByParams(Map<String, Object> params) {
        QueryWrapper<TechCapacityEvaluate> queryWrapper = getQueryWrapper(params);
        int limit = (int) params.get("limit");
        int page = (int) params.get("page");
        Page<TechCapacityEvaluate> myPage = new Page<>(page, limit);
        return operateTechCapacityEvaluateMapper.selectPage(myPage, queryWrapper);
    }

    /**
     * 通过查询条件查询履职计划map数据，用于导出
     * @param params
     * @return
     */
    @Override
    public List<Map<String, Object>> findOperateDataByParams(Map<String, Object> params) {
        QueryWrapper<TechCapacityEvaluate> queryWrapper = getQueryWrapper(params);
        return operateTechCapacityEvaluateMapper.selectMaps(queryWrapper);
    }

    /**
     * 通过主键删除企业研发工艺能力评价
     * @param id
     * @param
     * @return
     */
    @Override
    public boolean deleteOperateTechCapacityEvaluateDataById(Integer id) {
        int result = operateTechCapacityEvaluateMapper.deleteById(id);
        if (result > 0) {
            return true;
        }
        return false;
    }

    /**
     * 修改企业研发工艺能力评价信息
     * @param operateTechCapacityEvaluate
     * @param
     * @return
     */
    @Override
    public boolean updateOperateTechCapacityEvaluateData(TechCapacityEvaluate operateTechCapacityEvaluate) {
        int result = operateTechCapacityEvaluateMapper.updateById(operateTechCapacityEvaluate);
        if (result > 0) {
            return true;
        }
        return false;
    }

    /**
     * 导入附件
     * @param attachment
     * @return
     */
    @Override
    @Transactional
    public Map<String, Object> reaadOperateTechCapacityEvaluateDataSource(Attachment attachment,String year,String companyName) {
        // 返回消息
        Map<String, Object> result = new HashMap<>();
        result.put("content", "");
        result.put("failed", false);
        // 读取附件
        sheetService.load(attachment.getPath(), attachment.getFilename()); // 根据路径和名称读取附件
        // 读取表单
        sheetService.readByName("企业研发工艺能力评价表"); //根据表单名称获取该工作表单

        // 获取表单数据
        Sheet sheet = sheetService.getSheet();

        if (ObjectUtils.isEmpty(sheet)) {
            throw new ServiceException("表单【企业研发工艺能力评价表】不存在，无法读取数据，请检查");
        }
        // 获取单价列表数据
        Row row;
        //实体类集合，用于后面的批量写入数据库
        List<TechCapacityEvaluate> operateTechCapacityEvaluates = new ArrayList<>();
        // 2022-06-01 从决策系统导出数据，存在最后几行为空白数据，导致报数据越界问题，这里的长度由表头长度控制
        short maxSize = sheet.getRow(0).getLastCellNum();//列数(表头长度)


        Cell evalRess = sheet.getRow(2).getCell(SheetService.columnToIndex("B"));
        Cell companyNames = sheet.getRow(1).getCell(SheetService.columnToIndex("B"));
        Cell  years= sheet.getRow(1).getCell(SheetService.columnToIndex("Q"));

        String  evalRes= sheetService.getValue(evalRess);

        String companyCellValue = sheetService.getValue(companyNames);
        String yearCellValue = sheetService.getValue(years);

        //在TechCapacityEvaluateRes表插入信息
        TechCapacityEvaluateRes techCapacityEvaluateRes = new TechCapacityEvaluateRes();
        techCapacityEvaluateRes.setYear(Integer.valueOf(year));
        techCapacityEvaluateRes.setCompanyName(companyName);
        techCapacityEvaluateRes.setEvalRes(evalRes);
        techCapacityEvaluateResMapper.insert(techCapacityEvaluateRes);

        //效验年份、公司名称
        if (!companyName.equals(companyCellValue)) {
            throw new ServiceException("导入的公司名称与excel中的公司名称不一致，导入失败");
        }
        if (!year.equals(yearCellValue)) {
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
            Cell cell = row.createCell(SheetService.columnToIndex("R"));

            String projectName = cells.get(SheetService.columnToIndex("A"));
            String manageMethod = cells.get(SheetService.columnToIndex("B"));
            String kpiDesc = cells.get(SheetService.columnToIndex("C"));
            //String companyName = cells.get(SheetService.columnToIndex("F"));
            //String benchmark = cells.get(SheetService.columnToIndex("G"));
            String pastOneYearActuals = cells.get(SheetService.columnToIndex("G"));
            String pastTwoYearsActuals = cells.get(SheetService.columnToIndex("H"));
            String pastThreeYearsActuals = cells.get(SheetService.columnToIndex("I"));
            String issue = cells.get(SheetService.columnToIndex("N"));
            String improveActionSelf = cells.get(SheetService.columnToIndex("O"));
            String responsible = cells.get(SheetService.columnToIndex("P"));
            String endDate = cells.get(SheetService.columnToIndex("Q"));

            //转换类型
            BigDecimal pastOneYearActual=new BigDecimal(pastOneYearActuals);
            BigDecimal pastTwoYearsActual=new BigDecimal(pastTwoYearsActuals);
            BigDecimal pastThreeYearsActual=new BigDecimal(pastThreeYearsActuals);
            // 检查必填项
            if (ObjectUtils.isEmpty(companyName)) {
                setFailedContent(result, String.format("第%s行的经理人姓名是空的", j + 1));
                cell.setCellValue("企业名称是空的");
                throw new ServiceException("表中有空值");
            }

            //构建实体类
            TechCapacityEvaluate operateTechCapacityEvaluate = new TechCapacityEvaluate();
            operateTechCapacityEvaluate.setCompanyName(companyName);
            operateTechCapacityEvaluate.setProjectName(projectName);
            operateTechCapacityEvaluate.setManageMethod(manageMethod);
            operateTechCapacityEvaluate.setKpiDesc(kpiDesc);
            operateTechCapacityEvaluate.setPastOneYearActual(pastOneYearActual);
            operateTechCapacityEvaluate.setPastTwoYearsActual(pastTwoYearsActual);
            operateTechCapacityEvaluate.setPastThreeYearsActual(pastThreeYearsActual);
            operateTechCapacityEvaluate.setIssue(issue);
            operateTechCapacityEvaluate.setImproveActionSelf(improveActionSelf);
            operateTechCapacityEvaluate.setResponsible(responsible);
            operateTechCapacityEvaluate.setEndDate(LocalDate.parse(endDate));
            operateTechCapacityEvaluate.setYear(Integer.valueOf(year));

            operateTechCapacityEvaluateMapper.insert(operateTechCapacityEvaluate);
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
     * 添加企业研发工艺能力评价信息
     * @param operateTechCapacityEvaluate
     * @return
     */
    @Override
    public boolean insertOperateTechCapacityEvaluate(TechCapacityEvaluate operateTechCapacityEvaluate) {
        int result = operateTechCapacityEvaluateMapper.insert(operateTechCapacityEvaluate);
        if (result > 0) {
            return true;
        }
        return false;
    }


    private QueryWrapper<TechCapacityEvaluate> getQueryWrapper(Map<String,Object> params){
        QueryWrapper<TechCapacityEvaluate> queryWrapper = new QueryWrapper<>();
        if (params.containsKey("select")) {
            queryWrapper.select((String) params.get("select"));
        }
        if (params.containsKey("id")) {
            queryWrapper.eq("id", params.get("id"));
        }
        if (params.containsKey("projectName")) {
            queryWrapper.like("projectName", params.get("projectName"));
        }
        if (params.containsKey("companyName")) {
            queryWrapper.like("companyName", params.get("companyName"));
        }
        if (params.containsKey("year")) {
            queryWrapper.eq("year", params.get("year"));
        }
        if (params.containsKey("kpiFormula")) {
            queryWrapper.eq("kpiFormula", params.get("kpiFormula"));
        }
        if (params.containsKey("companyId")) {
            queryWrapper.eq("companyId", params.get("companyId"));
        }
        if (params.containsKey("companyShortName")) {
            queryWrapper.like("companyShortName", params.get("companyShortName"));
        }
        if (params.containsKey("issue")) {
            queryWrapper.like("issue", params.get("issue"));
        }
        if (params.containsKey("improveActionSelf")) {
            queryWrapper.like("improveActionSelf", params.get("improveActionSelf"));
        }
        if (params.containsKey("responsible")) {
            queryWrapper.like("responsible", params.get("responsible"));
        }
        if (params.containsKey("endDate")) {
            queryWrapper.like("endDate", params.get("endDate"));
        }
        if (params.containsKey("kpiDesc")) {
            queryWrapper.like("kpiDesc", params.get("kpiDesc"));
        }
        if (params.containsKey("manageMethod")) {
            queryWrapper.like("manageMethod", params.get("manageMethod"));
        }
        if (params.containsKey("benchmarkCompany")) {
            queryWrapper.like("benchmarkCompany", params.get("benchmarkCompany"));
        }
        if (params.containsKey("pastOneYearActual")) {
            queryWrapper.like("pastOneYearActual", params.get("pastOneYearActual"));
        }
        if (params.containsKey("pastTwoYearsActual")) {
            queryWrapper.like("pastTwoYearsActual", params.get("pastTwoYearsActual"));
        }
        if (params.containsKey("pastThreeYearsActual")) {
            queryWrapper.like("pastThreeYearsActual", params.get("pastThreeYearsActual"));
        }
        if (params.containsKey("benchmark")) {
            queryWrapper.like("benchmark", params.get("benchmark"));
        }
        return queryWrapper;
    }
}
