package com.fssy.shareholder.management.service.system.impl.operate.invest;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.mapper.manage.company.CompanyMapper;
import com.fssy.shareholder.management.pojo.system.config.Attachment;
import com.fssy.shareholder.management.pojo.system.operate.invest.ProcessAbilityList;
import com.fssy.shareholder.management.mapper.system.operate.invest.ProcessAbilityListMapper;
import com.fssy.shareholder.management.pojo.system.operate.invest.TechCapacityEvaluate;
import com.fssy.shareholder.management.pojo.system.operate.invest.ViewOperateProcessAbility;
import com.fssy.shareholder.management.pojo.system.performance.manager.ManagerQualitativeEval;
import com.fssy.shareholder.management.pojo.system.performance.manager.ManagerQualitativeEvalStd;
import com.fssy.shareholder.management.service.common.SheetService;
import com.fssy.shareholder.management.service.system.operate.invest.ProcessAbilityListService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fssy.shareholder.management.tools.exception.ServiceException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * **数据表名：	bs_operate_process_ability_list	**数据表中文名：	工艺基础能力台账	**业务部门：	经营管理部	**数据表作用：	管理工艺基础能力对比情况台账	**创建人创建日期：	TerryZeng 2022-12-2 服务实现类
 * </p>
 *
 * @author Solomon
 * @since 2022-12-08
 */
@Service
public class ProcessAbilityListServiceImpl extends ServiceImpl<ProcessAbilityListMapper, ProcessAbilityList> implements ProcessAbilityListService {

    @Autowired
    private SheetService sheetService;

    @Autowired
    private CompanyMapper companyMapper;

    @Autowired
    private ProcessAbilityListMapper processAbilityListMapper;

    /**
     * 分页查询经 工艺基础能力台账信息
     * @param params
     * @return
     */
    @Override
    public Page<ProcessAbilityList> findProcessAbilityListDataListPerPageByParams(Map<String, Object> params) {
        QueryWrapper<ProcessAbilityList> queryWrapper = getQueryWrapper(params);
        int limit = (int) params.get("limit");
        int page = (int) params.get("page");
        Page<ProcessAbilityList> myPage = new Page<>(page, limit);
        return processAbilityListMapper.selectPage(myPage, queryWrapper);

    }

    /**
     * 查询所有工艺基础能力台账信息
     * @param params
     * @return
     */
    @Override
    public List<ProcessAbilityList> findProcessAbilityListDataByParams(Map<String, Object> params) {
        QueryWrapper<ProcessAbilityList> queryWrapper = getQueryWrapper(params);
        List<ProcessAbilityList> processAbilityLists = processAbilityListMapper.selectList(queryWrapper);

        return processAbilityLists;
    }

    /**
     * 查询工艺基础能力台账信息
     * @param params
     * @return
     */
    @Override
    public List<Map<String, Object>> findManagerDataByParams(Map<String, Object> params) {
        QueryWrapper<ProcessAbilityList> queryWrapper = getQueryWrapper(params);
        return processAbilityListMapper.selectMaps(queryWrapper);

    }

    @Override
    public boolean deleteProcessAbilityListDataById(Integer id) {

        int result = processAbilityListMapper.deleteById(id);
        if (result > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean updateProcessAbilityListData(ProcessAbilityList processAbilityList) {
        //QueryWrapper<ProcessAbilityList> queryWrapper = getQueryWrapper(params);
        int result = processAbilityListMapper.updateById(processAbilityList);
        if (result > 0) {
            return true;
        }
        return false;
    }

    @Override
    public Map<String, Object> reaadProcessAbilityListDataSource(Attachment attachment, String year, String companyName) {
        // 返回消息
        Map<String, Object> result = new HashMap<>();
        result.put("content", "");
        result.put("failed", false);
        // 读取附件
        sheetService.load(attachment.getPath(), attachment.getFilename()); // 根据路径和名称读取附件
        // 读取表单
        sheetService.readByName("工艺基础能力表达台账表"); //根据表单名称获取该工作表单

        // 获取表单数据
        Sheet sheet = sheetService.getSheet();

        if (ObjectUtils.isEmpty(sheet)) {
            throw new ServiceException("表单【工艺基础能力表达台账表】不存在，无法读取数据，请检查");
        }
        // 获取单价列表数据
        Row row;
        //实体类集合，用于后面的批量写入数据库
        List<TechCapacityEvaluate> operateTechCapacityEvaluates = new ArrayList<>();
        // 2022-06-01 从决策系统导出数据，存在最后几行为空白数据，导致报数据越界问题，这里的长度由表头长度控制
        short maxSize = sheet.getRow(0).getLastCellNum();//列数(表头长度)


        Cell companyNames = sheet.getRow(1).getCell(SheetService.columnToIndex("C"));
        Cell  years= sheet.getRow(1).getCell(SheetService.columnToIndex("B"));

        String companyCellValue = sheetService.getValue(companyNames);
        String yearCellValue = sheetService.getValue(years);

        //效验年份、公司名称
        if (!companyName.equals(companyCellValue)) {
            throw new ServiceException("导入的公司名称与excel中的公司名称不一致，导入失败");
        }
        if (!year.equals(yearCellValue)) {
            throw new ServiceException("导入的年份与excel中的年份不一致，导入失败");
        }

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
            Cell cell = row.createCell(SheetService.columnToIndex("L"));

            String yearValue = cells.get(SheetService.columnToIndex("B"));
            String companyNameValue = cells.get(SheetService.columnToIndex("C"));
            String companyId = cells.get(SheetService.columnToIndex("D"));
            String companyShortName = cells.get(SheetService.columnToIndex("E"));
            String productLineTypeName = cells.get(SheetService.columnToIndex("F"));
            String competeCompanyName = cells.get(SheetService.columnToIndex("G"));
            String benchmarkCompanyName = cells.get(SheetService.columnToIndex("G"));
            String processAbilityStatus = cells.get(SheetService.columnToIndex("H"));
            String productName = cells.get(SheetService.columnToIndex("G"));
            String reportDate = cells.get(SheetService.columnToIndex("K"));

            // 检查必填项
            if (ObjectUtils.isEmpty(companyNameValue)) {
                setFailedContent(result, String.format("第%s行的经理人姓名是空的", j + 1));
                cell.setCellValue("企业名称是空的");
                throw new ServiceException("表中有空值");
            }

            //构建实体类
            ProcessAbilityList processAbilityList = new ProcessAbilityList();
            processAbilityList.setYear(Integer.valueOf(yearValue));
            processAbilityList.setCompanyName(companyNameValue);
            processAbilityList.setCompanyId(Integer.valueOf(companyId));
            processAbilityList.setCompanyShortName(companyShortName);
            processAbilityList.setProductLineTypeName(productLineTypeName);
            processAbilityList.setCompeteCompanyName(competeCompanyName);
            processAbilityList.setBenchmarkCompanyName(benchmarkCompanyName);
            processAbilityList.setProcessAbilityStatus(processAbilityStatus);
            processAbilityList.setProductName(productName);
            if (!ObjectUtils.isEmpty(reportDate)){
                processAbilityList.setReportDate(LocalDate.parse(reportDate));
            }

            processAbilityListMapper.insert(processAbilityList);
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

    @Override
    public boolean insertProcessAbilityListStd(ProcessAbilityList processAbilityList) {
        return false;
    }

    private QueryWrapper<ProcessAbilityList> getQueryWrapper(Map<String,Object> params) {
        QueryWrapper<ProcessAbilityList> queryWrapper = new QueryWrapper<>();
        if (params.containsKey("select")) {
            queryWrapper.select((String) params.get("select"));
        }
        if (params.containsKey("id")) {
            queryWrapper.eq("id", params.get("id"));
        }
        if (params.containsKey("year")) {
            queryWrapper.eq("year", params.get("year"));
        }
        if (params.containsKey("companyName")) {
            queryWrapper.like("companyName", params.get("companyName"));
        }
        if (params.containsKey("companyId")) {
            queryWrapper.eq("companyId", params.get("companyId"));
        }
        if (params.containsKey("companyShortName")) {
            queryWrapper.like("companyShortName", params.get("companyShortName"));
        }
        if (params.containsKey("competeCompanyName")) {
            queryWrapper.like("competeCompanyName", params.get("competeCompanyName"));
        }
        if (params.containsKey("productLineTypeName")) {
            queryWrapper.like("productLineTypeName", params.get("productLineTypeName"));
        }
        if (params.containsKey("benchmarkCompanyName")) {
            queryWrapper.like("benchmarkCompanyName", params.get("benchmarkCompanyName"));
        }
        if (params.containsKey("processAbilityStatus")) {
            queryWrapper.like("processAbilityStatus", params.get("processAbilityStatus"));
        }
        if (params.containsKey("productName")) {
            queryWrapper.like("productName", params.get("productName"));
        }
        if (params.containsKey("reportDate")) {
            queryWrapper.like("reportDate", params.get("reportDate"));
        }
        return queryWrapper;


    }
}
