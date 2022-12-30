package com.fssy.shareholder.management.service.system.impl.operate.company;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fssy.shareholder.management.mapper.system.operate.company.FinanceDataMapper;
import com.fssy.shareholder.management.pojo.system.config.Attachment;
import com.fssy.shareholder.management.pojo.system.operate.company.FinanceData;
import com.fssy.shareholder.management.service.common.SheetService;
import com.fssy.shareholder.management.service.system.operate.company.FinanceDataService;
import com.fssy.shareholder.management.tools.common.InstandTool;
import com.fssy.shareholder.management.tools.exception.ServiceException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.*;

/**
 * <p>
 * **数据表名：	bs_operate_company_finance_data	**数据表中文名：	企业财务基础数据(暂用于非权益投资管理)	**业务部门：	经营管理部	**数据表作用：	存放企业财务基础信息，方便在非权益投资页面计算。暂用于人工导入，未来采用数据对接	**创建人创建日期：	TerryZeng 2022-12-2 服务实现类
 * </p>
 *
 * @author 农浩
 * @since 2022-12-07
 */
@Service
public class FinanceDataServiceImpl extends ServiceImpl<FinanceDataMapper, FinanceData> implements FinanceDataService {
    @Autowired
    private FinanceDataMapper financeDataMapper;

    @Autowired
    private SheetService sheetService;





    @Override
    public Page<FinanceData> findDataListByParams(Map<String, Object> params) {
        QueryWrapper<FinanceData> queryWrapper = getQueryWrapper(params).orderByDesc("id");
        Page<FinanceData> myPage = new Page<>((int) params.get("page"), (int) params.get("limit"));
        return financeDataMapper.selectPage(myPage, queryWrapper);
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
    public boolean insertFinanceData(FinanceData financeData) {
        financeDataMapper.insert(financeData);//写入数据库
        financeDataMapper.updateById(financeData);//更新页面
        return true;
    }

    @Override
    public Map<String, Object> readFinanceDataDataSource(Attachment attachment) {
        // 导入基础事件
        // 返回消息
        Map<String, Object> result = new HashMap<>();
        result.put("content", "");
        result.put("failed", false);
        StringBuffer sb = new StringBuffer();// 用于数据校验
        //读取附件
        sheetService.load(attachment.getPath(), attachment.getFilename());//根据路径和名称读取附件
        //读取表单
        sheetService.readByName("企业财务基础数据附件");//根据表单名称获取该工作表单
        //获取表单数据
        Sheet sheet = sheetService.getSheet();
        if (ObjectUtils.isEmpty(sheet)) {
            throw new ServiceException("表单【企业财务基础数据附件】不存在，无法读取数据，请检查");
        }
        //处理导入日期
        Date importDate = attachment.getImportDate();
        //获取列表数据
        Row row;
        //实体类集合，用于后面的批量写入数据库
        // 2022-06-01 从决策系统导出数据，存在最后几行为空白数据，导致报数据越界问题，这里的长度由表头长度控制
        short maxSize = sheet.getRow(0).getLastCellNum();//列数(表头长度)
        // 循环总行数(不读表的标题，从第3行开始读,所以j == 2)
        for (int j = 2; j <= sheet.getLastRowNum(); j++) {
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
            Cell cell = row.createCell(SheetService.columnToIndex("K"));//报错信息上传到excel K列
            String id = cells.get(SheetService.columnToIndex("A"));
            String year = cells.get(SheetService.columnToIndex("B"));
            String companyName = cells.get(SheetService.columnToIndex("C"));
            String companyShortName = cells.get(SheetService.columnToIndex("D"));
            String competeCompanyName = cells.get(SheetService.columnToIndex("E"));
            String month = cells.get(SheetService.columnToIndex("F"));
            String operatingProfit = cells.get(SheetService.columnToIndex("G"));
            String netValueOfFixedAssets = cells.get(SheetService.columnToIndex("H"));
            String researchDevelopmentCosts = cells.get(SheetService.columnToIndex("I"));
            String mainBusinessIncome = cells.get(SheetService.columnToIndex("J"));



            //构建实体类
            FinanceData financeData = new FinanceData();
            financeData.setId(InstandTool.stringToInteger(id));
            financeData.setCompanyName(companyName);
            financeData.setCompanyShortName(companyShortName);
            Date date = new Date();
            financeData.setYear(InstandTool.stringToInteger(year));
            financeData.setMonth(InstandTool.stringToInteger(month));
            financeData.setMainBusinessIncome(InstandTool.stringToDouble(mainBusinessIncome));
            financeData.setCompeteCompanyName(competeCompanyName);
            financeData.setOperatingProfit(InstandTool.stringToDouble(operatingProfit));
            financeData.setNetValueOfFixedAssets(InstandTool.stringToDouble(netValueOfFixedAssets));
            financeData.setResearchDevelopmentCosts(InstandTool.stringToDouble(researchDevelopmentCosts));

            financeDataMapper.insert(financeData);
            cell.setCellValue("导入成功");
        }
        sheetService.write(attachment.getPath(), attachment.getFilename());//写入excel表
        return result;
    }

    private QueryWrapper<FinanceData> getQueryWrapper(Map<String,Object> params) {
        QueryWrapper<FinanceData> queryWrapper = new QueryWrapper<>();
        if (params.containsKey("select")) {
            queryWrapper.select((String) params.get("select"));
        }
        if (params.containsKey("id")) {
            queryWrapper.eq("id", params.get("id"));
        }
        if (params.containsKey("year")) {
            queryWrapper.eq("year", params.get("year"));
        }
        if (params.containsKey("year")) {
            queryWrapper.eq("year", params.get("year"));
        }
        if (params.containsKey("year")) {
            queryWrapper.eq("year", params.get("year"));
        }
        if (params.containsKey("year")) {
            queryWrapper.eq("year", params.get("year"));
        }
        if (params.containsKey("year")) {
            queryWrapper.eq("year", params.get("year"));
        }
        if (params.containsKey("year")) {
            queryWrapper.eq("year", params.get("year"));
        }
        if (params.containsKey("year")) {
            queryWrapper.eq("year", params.get("year"));
        }
        if (params.containsKey("year")) {
            queryWrapper.eq("year", params.get("year"));
        }
        if (params.containsKey("year")) {
            queryWrapper.eq("year", params.get("year"));
        }
        if (params.containsKey("year")) {
            queryWrapper.eq("year", params.get("year"));
        }
        if (params.containsKey("year")) {
            queryWrapper.eq("year", params.get("year"));
        }
        if (params.containsKey("year")) {
            queryWrapper.eq("year", params.get("year"));
        }
        return queryWrapper;
    }
}

