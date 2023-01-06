package com.fssy.shareholder.management.service.system.impl.operate.invest;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.mapper.manage.company.CompanyMapper;
import com.fssy.shareholder.management.pojo.manage.company.Company;
import com.fssy.shareholder.management.pojo.manage.user.User;
import com.fssy.shareholder.management.pojo.system.config.Attachment;
import com.fssy.shareholder.management.pojo.system.operate.invest.InvestPlan;
import com.fssy.shareholder.management.mapper.system.operate.invest.InvestPlanMapper;
import com.fssy.shareholder.management.service.common.SheetService;
import com.fssy.shareholder.management.service.system.operate.invest.InvestPlanService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fssy.shareholder.management.tools.common.GetTool;
import com.fssy.shareholder.management.tools.exception.ServiceException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;

/**
 * <p>
 * *****业务部门：	经营投资科		*****数据表中文名：	投资计划表(N+3)		*****数据表名：	bs_operate_invest_plan		*****数据表作用：	各企业公司的非权益投资计划情况，按公司、年、月分开记录		*****变更记录：	时间         	变更人		变更内容	20230103	兰宇铧           	初始设计 服务实现类
 * </p>
 *
 * @author SHANSHAN
 * @since 2023-01-03
 */
@Service
public class InvestPlanServiceImpl extends ServiceImpl<InvestPlanMapper, InvestPlan> implements InvestPlanService {

    @Autowired
    private InvestPlanMapper investPlanMapper;

    @Autowired
    private SheetService sheetService;

    @Autowired
    private CompanyMapper companyMapper;

    @Override
    public Page<Map<String, Object>> findPlanDataByPageMap(Map<String, Object> params) {
        QueryWrapper<InvestPlan> queryWrapper = getQueryWrapper(params);
        Page<Map<String, Object>> planPage = new Page<>((int) params.get("page"), (int) params.get("limit"));
        return investPlanMapper.selectMapsPage(planPage, queryWrapper);
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
    public Map<String, Object> readInvestPlanDataSource(Attachment attachment, HttpServletRequest request) {
        // 导入投资计划
        // 返回消息
        Map<String, Object> result = new HashMap<>();
        result.put("content", "");
        result.put("failed", false);
        // 读取附件
        sheetService.load(attachment.getPath(), attachment.getFilename()); // 根据路径和名称读取附件
        // 读取表单
        sheetService.readByName("N+3计划 (投资计划)"); //根据表单名称获取该工作表单
        // 获取表单数据
        Sheet sheet = sheetService.getSheet();
        if (ObjectUtils.isEmpty(sheet)) {
            throw new ServiceException("表单【N+3计划 (投资计划)】不存在，无法读取数据，请检查");
        }
        // 实体集合，用于批量写入数据库
        List<InvestPlan> investPlanList = new ArrayList<>();
        String companyId = request.getParameter("companyId");// 公司id
        String yearMonth = request.getParameter("month");// 年月
        String importDate = request.getParameter("importDate");// 导入日期
        if (ObjectUtils.isEmpty(companyId) || companyId.equals("undefined")) {
            throw new ServiceException("未选择公司，导入失败");
        }
        if (ObjectUtils.isEmpty(yearMonth)) {
            throw new ServiceException("未选择年月，导入失败");
        }
        if (ObjectUtils.isEmpty(importDate)) {
            throw new ServiceException("导入日期为空，导入失败");
        }
        // 年月
        List<String> strings = Arrays.asList(yearMonth.split("-"));
        String year = strings.get(0);
        String month = strings.get(1);
        // 获取单价列表数据
        Row row;
        // 2022-06-01 从决策系统导出数据，存在最后几行为空白数据，导致报数据越界问题，这里的长度由表头长度控制
        short maxSize = sheet.getRow(0).getLastCellNum();//列数(表头长度)
        // 循环总行数(不读表头，从第2行开始读，索引从0开始，所以j=1)
        for (int j = 1; j <= sheet.getLastRowNum(); j++) {// getPhysicalNumberOfRows()此方法不会将空白行计入行数
            List<String> cells = new ArrayList<>();// 每一行的数据用一个list接收
            row = sheet.getRow(j);// 获取第j行
            // 获取一行中有多少列 Row：行，cell：列
            // 循环row行中的每一个单元格
            for (int k = 0; k < maxSize + 2; k++) {
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
                // Cannot get a STRING value from a NUMERIC cell 无法从单元格获取数值
                Cell cell = row.getCell(k);
                String res = sheetService.getValue(cell).trim();// 获取单元格的值
                cells.add(res);// 将单元格的值写入行
            }
            // 导入结果写入列
            Cell cell = row.createCell(SheetService.columnToIndex("L"));// 每一行的结果信息上传到S列
            // 获取数据
            String firstType = cells.get(SheetService.columnToIndex("A"));// 类别一
            String secondType = cells.get(SheetService.columnToIndex("B"));// 类别二
            String thirdType = cells.get(SheetService.columnToIndex("C"));// 类别三（可空）
            String unit = cells.get(SheetService.columnToIndex("D"));// 单位
            String budget = cells.get(SheetService.columnToIndex("H"));// 预算
            String incrementArtificial = cells.get(SheetService.columnToIndex("I"));// 涨幅
            String evaluate = cells.get(SheetService.columnToIndex("I"));// 评价

            // 检查必填项
            // region
            if (StringUtils.isEmpty(firstType)) {
                setFailedContent(result, String.format("第%s行的类别一为空", j + 1));
                cell.setCellValue("类别一不能为空");
                continue;
            }

            if (StringUtils.isEmpty(secondType)) {
                setFailedContent(result, String.format("第%s行的类别二为空", j + 1));
                cell.setCellValue("类别二不能为空");
                continue;
            }
            if (StringUtils.isEmpty(unit)) {
                setFailedContent(result, String.format("第%s行的单位为空", j + 1));
                cell.setCellValue("单位不能为空");
                continue;
            }
            if (StringUtils.isEmpty(budget)) {
                setFailedContent(result, String.format("第%s行的预算金额为空", j + 1));
                cell.setCellValue("预算金额不能为空");
                continue;
            }
            if (StringUtils.isEmpty(incrementArtificial)) {
                setFailedContent(result, String.format("第%s行的涨幅为空", j + 1));
                cell.setCellValue("涨幅不能为空");
                continue;
            }
            if (StringUtils.isEmpty(evaluate)) {
                setFailedContent(result, String.format("第%s行的评价为空", j + 1));
                cell.setCellValue("评价不能为空");
                continue;
            }
            // endregion
            // 数据校验

            // 构建实体类
            InvestPlan investPlan = new InvestPlan();
            investPlan.setFirstType(firstType);
            investPlan.setSecondType(secondType);
            if (!ObjectUtils.isEmpty(thirdType)) {
                investPlan.setThirdType(thirdType);
            }
            investPlan.setUnit(unit);
            if (!incrementArtificial.equals("/")) {
                investPlan.setIncrementArtificial(new BigDecimal(incrementArtificial).setScale(4, RoundingMode.HALF_UP));
            }
            investPlan.setBudget(new BigDecimal(budget));
            investPlan.setEvaluate(evaluate);
            // 数据库不能为null的字段设置值
            Company company = companyMapper.selectById(companyId);
            investPlan.setCompanyId(Long.parseLong(companyId));
            investPlan.setCompanyName(company.getName());
            investPlan.setYear(Integer.valueOf(year));
            investPlan.setMonth(Integer.valueOf(month));
            User user = GetTool.getUser();
            investPlan.setCreatedAt(LocalDateTime.now());
            investPlan.setCreatedId(user.getId());
            investPlan.setCreatedName(user.getName());
            // 存入集合
            investPlanList.add(investPlan);
            cell.setCellValue("导入成功");// 写在upload目录下的excel表格
        }
        // 根据companyId,year,month删除数据
        LambdaQueryWrapper<InvestPlan> investPlanWrapper = new LambdaQueryWrapper<>();
        investPlanWrapper.eq(InvestPlan::getCompanyId, companyId)
                .eq(InvestPlan::getYear, Integer.valueOf(year))
                .eq(InvestPlan::getMonth, Integer.valueOf(month));
        investPlanMapper.delete(investPlanWrapper);
        // 批量写入
        investPlanMapper.insertBatchSomeColumn(investPlanList);
        sheetService.write(attachment.getPath(), attachment.getFilename());// 写入excel表
        return result;
    }

    @Override
    public boolean update(InvestPlan investPlan, HttpServletRequest request) {
        String companyId = request.getParameter("companyId");
        if (ObjectUtils.isEmpty(companyId)){
            throw new ServiceException("所选公司未维护，修改失败");
        }
        Company company = companyMapper.selectById(companyId);
        investPlan.setCompanyId(Long.valueOf(companyId));
        investPlan.setCompanyName(company.getName());
        int result = investPlanMapper.updateById(investPlan);
        return result > 0;
    }

    private QueryWrapper<InvestPlan> getQueryWrapper(Map<String, Object> params) {
        QueryWrapper<InvestPlan> queryWrapper = new QueryWrapper<>();
        if (params.containsKey("id")) {
            queryWrapper.eq("id", params.get("id"));
        }
        if (params.containsKey("companyName")) {
            queryWrapper.like("companyName", params.get("companyName"));
        }
        if (params.containsKey("year")) {
            queryWrapper.eq("year", params.get("year"));
        }
        if (params.containsKey("month")) {
            queryWrapper.eq("month", params.get("month"));
        }
        if (params.containsKey("firstType")) {
            queryWrapper.like("firstType", params.get("firstType"));
        }
        if (params.containsKey("secondType")) {
            queryWrapper.like("secondType", params.get("secondType"));
        }
        if (params.containsKey("companyIds")) {
            String companyIds = (String) params.get("companyIds");
            List<String> strings = Arrays.asList(companyIds.split(","));
            queryWrapper.in("companyId", strings);
        }
        return queryWrapper;
    }
}
