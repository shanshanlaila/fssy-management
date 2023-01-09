package com.fssy.shareholder.management.service.system.impl.operate.invest;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fssy.shareholder.management.mapper.manage.company.CompanyMapper;
import com.fssy.shareholder.management.mapper.system.config.AttachmentMapper;
import com.fssy.shareholder.management.mapper.system.operate.invest.ConditionMapper;
import com.fssy.shareholder.management.pojo.manage.company.Company;
import com.fssy.shareholder.management.pojo.manage.user.User;
import com.fssy.shareholder.management.pojo.system.config.Attachment;
import com.fssy.shareholder.management.pojo.system.operate.invest.Condition;
import com.fssy.shareholder.management.service.common.SheetService;
import com.fssy.shareholder.management.service.system.operate.invest.ConditionService;
import com.fssy.shareholder.management.tools.common.GetTool;
import com.fssy.shareholder.management.tools.common.InstandTool;
import com.fssy.shareholder.management.tools.exception.ServiceException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;

/**
 * <p>
 * *****业务部门：	经营投资科		*****数据表中文名：	投资执行情况表		*****数据表名：	bs_operate_invest_condition		*****数据表作用：	各企业公司的非权益投资执行情况，按公司、年、月分开记录		*****变更记录：	时间         	变更人		变更内容	20230103	兰宇铧           	初始设计 服务实现类
 * </p>
 *
 * @author 农浩
 * @since 2023-01-03
 */
@Service
public class ConditionServiceImpl extends ServiceImpl<ConditionMapper, Condition> implements ConditionService {
    @Autowired
    private ConditionMapper conditionMapper;
    @Autowired
    private SheetService sheetService;
    @Autowired
    private AttachmentMapper attachmentMapper;
    @Autowired
    private CompanyMapper companyMapper;

    @Override
    public Page<Condition> findDataListByParams(Map<String, Object> params) {
        QueryWrapper<Condition> queryWrapper = getQueryWrapper(params).orderByDesc("id");
        Page<Condition> myPage = new Page<>((int) params.get("page"), (int) params.get("limit"));
        return conditionMapper.selectPage(myPage, queryWrapper);
    }

    @Override
    public Map<String, Object> readConditionDataSource(Attachment attachment,HttpServletRequest request) {
        // 导入基础事件
        // 返回消息
        Map<String, Object> result = new HashMap<>();
        result.put("content", "");
        result.put("failed", false);
        StringBuffer sb = new StringBuffer();// 用于数据校验
        //读取附件
        sheetService.load(attachment.getPath(), attachment.getFilename());//根据路径和名称读取附件
        //读取表单
        sheetService.readByName("年度项目投资执行情况表");//根据表单名称获取该工作表单
        //获取表单数据
        Sheet sheet = sheetService.getSheet();
        if (ObjectUtils.isEmpty(sheet)) {
            throw new ServiceException("表单【年度项目投资执行情况表】不存在，无法读取数据，请检查");
        }
        //处理导入日期
        Date importDate = attachment.getImportDate();

        // 获取request数据
        Long companyId = Long.valueOf(request.getParameter("companyId"));
        String yearMonth = request.getParameter("month");
        List<String> stringList = Arrays.asList(yearMonth.split("-"));
        //String year = stringList.get(0); 这次的导入不需要从这获取年份，因为导入模板有年份来填写。
        String month = stringList.get(1);
        //获取列表数据
        Row row;
        //实体类集合，用于后面的批量写入数据库
        // 2022-06-01 从决策系统导出数据，存在最后几行为空白数据，导致报数据越界问题，这里的长度由表头长度控制
        short maxSize = sheet.getRow(0).getLastCellNum();//列数(表头长度)
        // 循环总行数(不读表的标题，从第2行开始读)
        for (int j = 1; j <= sheet.getLastRowNum(); j++) {
            // getPhysicalNumberOfRows()此方法不会将空白行计入行数
            //if (j==3)continue;// 跳过第三行，因为2，3行合并为一行
            List<String> cells = new ArrayList<>();//每一行的数据用一个list接收
            row = sheet.getRow(j);//获取第j行
            // 获取一行中有多少列 Row：行，cell：列
            // short lastCellNum = row.getLastCellNum();
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
                //Cannot get a STRING value from a NUMERIC cell 无法从单元格获取数值
                Cell cell = row.getCell(k);
                String res = sheetService.getValue(cell).trim();//获取单元格的值
                cells.add(res);// 将单元格的值写入行
            }


            // 导入结果写入列
            Cell cell = row.createCell(SheetService.columnToIndex("L"));//报错信息上传到excel L列
            String lineNumber = cells.get(SheetService.columnToIndex("A"));
            String year = cells.get(SheetService.columnToIndex("B"));
            if (ObjectUtils.isEmpty(year)) {
                setFailedContent(result, String.format("第%s行的年度是空的", j + 1));
                cell.setCellValue("年度是空的");
                continue;
            }

            String firstType = cells.get(SheetService.columnToIndex("C"));
            if (ObjectUtils.isEmpty(firstType)) {
                setFailedContent(result, String.format("第%s行的类别是空的", j + 1));
                cell.setCellValue("类别是空的");
                continue;
            }
            String item = cells.get(SheetService.columnToIndex("D"));
            if (ObjectUtils.isEmpty(item)) {
                setFailedContent(result, String.format("第%s行的指标是空的", j + 1));
                cell.setCellValue("指标是空的");
                continue;
            }
            String describe = cells.get(SheetService.columnToIndex("E"));
            String unit = cells.get(SheetService.columnToIndex("F"));
            if (ObjectUtils.isEmpty(unit)) {
                setFailedContent(result, String.format("第%s行的单位是空的", j + 1));
                cell.setCellValue("单位是空的");
                continue;
            }
            String budget = cells.get(SheetService.columnToIndex("G"));
            if (ObjectUtils.isEmpty(budget)) {
                setFailedContent(result, String.format("第%s行的全年预算是空的", j + 1));
                cell.setCellValue("全年预算是空的");
                continue;
            }
            String accumulation = cells.get(SheetService.columnToIndex("H"));
            if (ObjectUtils.isEmpty(accumulation)) {
                setFailedContent(result, String.format("第%s行的全年实绩是空的", j + 1));
                cell.setCellValue("全年实绩是空的");
                continue;
            }
            String proportion = cells.get(SheetService.columnToIndex("I"));
            if (ObjectUtils.isEmpty(proportion)) {
                setFailedContent(result, String.format("第%s行的预算完成比例是空的", j + 1));
                cell.setCellValue("预算完成比例是空的");
                continue;
            }
            String evaluate = cells.get(SheetService.columnToIndex("J"));
            if (ObjectUtils.isEmpty(evaluate)) {
                setFailedContent(result, String.format("第%s行的评价是空的", j + 1));
                cell.setCellValue("评价是空的");
                continue;
            }
            // 数据校验
            if (ObjectUtils.isEmpty(companyId)) {
                throw new ServiceException("公司id为空，导入失败");
            }
            if (ObjectUtils.isEmpty(importDate)) {
                throw new ServiceException("导入日期为空，导入失败");
            }
            if (ObjectUtils.isEmpty(month)) {
                throw new ServiceException("月份为空，导入失败");
            }

            //构建实体类
            Condition condition = new Condition();
            //判断选填项
            if (!ObjectUtils.isEmpty(lineNumber)) {

                condition.setLineNumber(Integer.valueOf(lineNumber));
            }
            Date date = new Date();
            condition.setYear(InstandTool.stringToInteger(year));
            condition.setFirstType(firstType);
            condition.setItem(item);
            //判断选填项
            if (!ObjectUtils.isEmpty(describe)) {
                condition.setDescribe(describe);
            }
            condition.setUnit(unit);
            BigDecimal budgetBigDecimal = new BigDecimal(budget);
//            budgetBigDecimal.setScale(2,RoundingMode.HALF_UP);
            condition.setBudget(budgetBigDecimal.setScale(2,RoundingMode.HALF_UP));
            condition.setBudget(new BigDecimal(budget));
            BigDecimal accumulationBigDecimal = new BigDecimal(accumulation);
            condition.setAccumulation( accumulationBigDecimal.setScale(2,RoundingMode.HALF_UP));//2位小数点，四舍五入取值方式
            BigDecimal proportionBigDecimal = new BigDecimal(proportion);
//            proportionBigDecimal.setScale(4, RoundingMode.HALF_UP);//4位小数点，四舍五入取值方式
            condition.setProportion(proportionBigDecimal.setScale(4, RoundingMode.HALF_UP));
            condition.setEvaluate(evaluate);
            condition.setMonth(Integer.valueOf(month));//从前端月份选择下拉框过来的，代码在80行进行了处理编写
            condition.setYear(Integer.valueOf(year));
            User user = GetTool.getUser();
            condition.setCreatedAt(LocalDateTime.now());
            condition.setCreatedId(user.getId());
            condition.setCreatedName(user.getName());
            Company company = companyMapper.selectById(companyId);
            condition.setCompanyName(company.getName());
            condition.setCompanyId(companyId);
            condition.setCompanyShortName(company.getShortName());
            conditionMapper.insert(condition);
            cell.setCellValue("导入成功");
        }
        sheetService.write(attachment.getPath(), attachment.getFilename());//写入excel表
        return result;
    }

    @Override
    public boolean updateInvestConditionData(Condition condition, HttpServletRequest request) {
            Long companyId = Long.valueOf(request.getParameter("companyId"));
            if (ObjectUtils.isEmpty(companyId)) {
                throw new ServiceException("未选择公司");
            }
            Company company = companyMapper.selectById(companyId);
            condition.setCompanyName(company.getName());
            condition.setCompanyId(InstandTool.integerToLong(company.getId()));
            condition.setCompanyShortName(company.getShortName());
            condition.setCompanyCode(company.getCode());
            User user = GetTool.getUser();
            condition.setUpdatedAt(LocalDateTime.now());
            condition.setUpdatedId(user.getId());
            condition.setUpdatedName(user.getName());
            int result = conditionMapper.updateById(condition);
            if (result>0){
                return true;
            }
        return false;
    }

    @Override
    public boolean insertInvestCondition(Condition condition, HttpServletRequest request) {
        Long companyId = Long.valueOf(request.getParameter("companyId"));
        if (ObjectUtils.isEmpty(companyId)) {
            throw new ServiceException("未选择公司");
        }
        Company company = companyMapper.selectById(companyId);
        condition.setCompanyName(company.getName());
        condition.setCompanyId(companyId);
        condition.setCompanyShortName(company.getShortName());
        condition.setCompanyCode(company.getCode());
        User user = GetTool.getUser();
        condition.setCreatedAt(LocalDateTime.now());
        condition.setCreatedId(user.getId());
        condition.setCreatedName(user.getName());
        conditionMapper.insert(condition);
        return true;
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

    private QueryWrapper<Condition> getQueryWrapper(Map<String, Object> params) {
        QueryWrapper<Condition> queryWrapper = Wrappers.query();
        if (params.containsKey("select")) {
            queryWrapper.select(InstandTool.objectToString(params.get("select")));
        }
        if (params.containsKey("id")) {
            queryWrapper.eq("id", params.get("id"));
        }
        if (params.containsKey("createdAt")) {
            queryWrapper.eq("createdAt", params.get("createdAt"));
        }
        if (params.containsKey("note")) {
            queryWrapper.like("note", params.get("note"));
        }
        if (params.containsKey("year")) {
            queryWrapper.eq("year", params.get("year"));
        }
        if (params.containsKey("month")) {
            queryWrapper.eq("month", params.get("month"));
        }
        if (params.containsKey("companyCode")) {
            queryWrapper.eq("companyCode", params.get("companyCode"));
        }
        if (params.containsKey("companyName")) {
            queryWrapper.like("companyName", params.get("companyName"));
        }
        if (params.containsKey("companyId")) {
            queryWrapper.eq("companyId", params.get("companyId"));
        }
        if (params.containsKey("companyName")) {
            queryWrapper.eq("companyName", params.get("companyName"));
        }
        if (params.containsKey("lineNumber")) {
            queryWrapper.eq("lineNumber", params.get("lineNumber"));
        }
        if (params.containsKey("firstType")) {
            queryWrapper.eq("firstType", params.get("firstType"));
        }
        if (params.containsKey("item")) {
            queryWrapper.like("item", params.get("item"));
        }
        if (params.containsKey("budget")) {
            queryWrapper.eq("budget", params.get("budget"));
        }
        if (params.containsKey("accumulation")) {
            queryWrapper.eq("accumulation", params.get("accumulation"));
        }
        if (params.containsKey("unit")) {
            queryWrapper.eq("unit", params.get("unit"));
        }
        if (params.containsKey("describe")) {
            queryWrapper.like("describe", params.get("describe"));
        }
        if (params.containsKey("proportion")) {
            queryWrapper.eq("proportion", params.get("proportion"));
        }
        if (params.containsKey("evaluate")) {
            queryWrapper.like("evaluate", params.get("evaluate"));
        }
        if (params.containsKey("companyShortName")) {
            queryWrapper.eq("companyShortName", params.get("companyShortName"));
        }
        if (params.containsKey("companyCode")) {
            queryWrapper.eq("companyCode",params.get("companyCode"));
        }
        return queryWrapper;
    }
}
