package com.fssy.shareholder.management.service.system.impl.operate.invest;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fssy.shareholder.management.mapper.system.operate.invest.ProductLineCapacityListMapper;
import com.fssy.shareholder.management.mapper.system.operate.invest.ProductLineCapacityMonthMapper;
import com.fssy.shareholder.management.mapper.system.operate.invest.ViewProductLineCapacityListMapper;
import com.fssy.shareholder.management.pojo.system.config.Attachment;
import com.fssy.shareholder.management.pojo.system.operate.invest.ProductLineCapacityList;
import com.fssy.shareholder.management.pojo.system.operate.invest.ProductLineCapacityMonth;
import com.fssy.shareholder.management.pojo.system.operate.invest.ViewProductLineCapacityList;
import com.fssy.shareholder.management.service.common.SheetService;
import com.fssy.shareholder.management.service.system.operate.invest.ViewProductLineCapacityListService;
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
 * VIEW 服务实现类 重点产线产能
 * </p>
 *
 * @author zzp
 * @since 2022-12-08
 */
@Service
public class ViewProductLineCapacityListServiceImpl extends ServiceImpl<ViewProductLineCapacityListMapper, ViewProductLineCapacityList> implements ViewProductLineCapacityListService {

    @Autowired
    private ViewProductLineCapacityListMapper viewProductLineCapacityListMapper;
    @Autowired
    private ProductLineCapacityMonthMapper productLineCapacityMonthMapper;
    @Autowired
    private ProductLineCapacityListMapper productLineCapacityListMapper;
    @Autowired
    private SheetService sheetService;


    /**
     * 通过查询条件，查询重点产线产能 数据
     * @param params
     * @return 查询出的所有数据
     */
    @Override
    public List<ViewProductLineCapacityList> findViewProductLineCapacityListSDataByParams(Map<String, Object> params) {
        QueryWrapper<ViewProductLineCapacityList> queryWrapper = getQueryWrapper(params);
        return viewProductLineCapacityListMapper.selectList(queryWrapper);
    }

    /**
     * 通过查询条件 分页查询列表
     *
     * @param params 查询条件
     * @return 分页数据
     */
    @Override
    public Page<ViewProductLineCapacityList> findViewProductLineCapacityListDataListPerPageByParams(Map<String, Object> params) {
        QueryWrapper<ViewProductLineCapacityList> queryWrapper = getQueryWrapper(params);
        int limit = (int)params.get("limit");
        int page = (int)params.get("page");
        Page<ViewProductLineCapacityList> myPage = new Page<>(page,limit);
        return viewProductLineCapacityListMapper.selectPage(myPage, queryWrapper);
    }

    /**
     * 通过查询条件查询重点产线产能map数据，用于导出
     * @param params 查询条件
     * @return 数据列表
     */
    @Override
    public List<Map<String, Object>> findViewProductLineCapacityListDataByParams(Map<String, Object> params) {
        QueryWrapper<ViewProductLineCapacityList> queryWrapper = getQueryWrapper(params);
        return viewProductLineCapacityListMapper.selectMaps(queryWrapper);
    }

    /**
     * 实现导入功能 需要读取数据存入下面两张表
     * bs_operate_product_line_capacity_list
     * bs_operate_product_line_capacity_month
     * @param attachment 附件
     * @return 附件map集合
     */
    @Override
    public Map<String, Object> readViewProductLineCapacityListDataSource(Attachment attachment, String year, String quarter) {
        // 返回消息
        Map<String, Object> result = new HashMap<>();
        result.put("content", "");
        result.put("failed", false);
        // 读取附件
        sheetService.load(attachment.getPath(), attachment.getFilename()); // 根据路径和名称读取附件
        // 读取表单
        sheetService.readByName("重点产线产能季度数据"); //根据表单名称获取该工作表单
        // 获取表单数据
        Sheet sheet = sheetService.getSheet();
        if (ObjectUtils.isEmpty(sheet)) {
            throw new ServiceException("表单【重点产线产能季度数据】不存在，无法读取数据，请检查");
        }
        // 获取单价列表数据
        Row row;
        List<ViewProductLineCapacityList> viewProductLineCapacityLists = new ArrayList<>(); //实体类集合，用于后面的批量写入数据库
        // 2022-06-01 从决策系统导出数据，存在最后几行为空白数据，导致报数据越界问题，这里的长度由表头长度控制
        short maxSize = sheet.getRow(0).getLastCellNum();//列数(表头长度)
        //获取excel中的企业名称、年份、季度
        Cell companyNameCell = sheet.getRow(0).getCell(SheetService.columnToIndex("S"));
        Cell yearCell = sheet.getRow(0).getCell(SheetService.columnToIndex("V"));
        Cell quarterCell = sheet.getRow(0).getCell(SheetService.columnToIndex("X"));
        String companyNameCellValue = sheetService.getValue(companyNameCell);
        String yearCellValue = sheetService.getValue(yearCell);
        String quarterCellValue = sheetService.getValue(quarterCell);
        //校验年份、季度
        if(!year.equals(yearCellValue)){
            throw new ServiceException("导入的年份与excel中的年份不一致，导入失败");
        }
        if(!quarter.equals(quarterCellValue)){
            throw new ServiceException("导入的季度与excel中的季度不一致，导入失败");
        }
        //初始化[计数器变量]
        int i = 0;
        // 循环总行数(不读表的标题，从第1行开始读)
        for (int j = 3; j <= sheet.getLastRowNum(); j++) {// getPhysicalNumberOfRows()此方法不会将空白行计入行数
            //读取时按季度读取,不同季度对应不同月份
            //定义开始月份和终止月份,用来对应季节
            int monthStart = 0;
            int monthEnd = 0;
            //根据单元格进行初始化开始与终止月份
            if(quarterCellValue.equals("1")){
                monthStart = 1;
                monthEnd = 3;
            }else if(quarterCellValue.equals("2")){
                monthStart = 4;
                monthEnd = 6;
            }else if(quarterCellValue.equals("3")){
                monthStart = 7;
                monthEnd = 9;
            }else if(quarterCellValue.equals("4")){
                monthStart = 10;
                monthEnd = 12;
            }
            //根据季度循环读取excel中的对应月份数据,写入数据库
            for (int a = monthStart; a <= monthEnd; a++) {
                //定义计数器,做为数据插入ProductLineCapacityList的条件变量,因为此表不需要插入三次重复的数据,所以再循环末插入所有数据就可
                i++;
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
                Cell cell = row.createCell(SheetService.columnToIndex("Y"));// 报错信息上传到excel V列（暂未实现）
                //读取数据需要写入两张表,因此构建ProductLineCapacityList ProductLineCapacityMonth俩个实体类
                ProductLineCapacityList productLineCapacityList = new ProductLineCapacityList();
                ProductLineCapacityMonth productLineCapacityMonth = new ProductLineCapacityMonth();
                //首先get要存入productLineCapacityList表中的数据
                String productLineCapacityId = cells.get(SheetService.columnToIndex("A"));
                String productTypeName = cells.get(SheetService.columnToIndex("B"));
                String productLineName = cells.get(SheetService.columnToIndex("C"));
                String yearVehicle = cells.get(SheetService.columnToIndex("D"));
                String productLineTypeName = cells.get(SheetService.columnToIndex("E"));
                String sopDate = cells.get(SheetService.columnToIndex("F"));
                String designProductionTakt = cells.get(SheetService.columnToIndex("G"));
                String designCapacityPerYear = cells.get(SheetService.columnToIndex("H"));
                String marketShares = cells.get(SheetService.columnToIndex("I"));
                String designActualTakt = cells.get(SheetService.columnToIndex("J"));
                String designCapacityPerMonth = cells.get(SheetService.columnToIndex("K"));
                String yearAim = cells.get(SheetService.columnToIndex("L"));
                //计算公式：[截至当前季度负荷率、截至当月累计产量变量、理论月产能、季度末月产量]
                //1.lineLoadRateQuarter = monthActual / designCapacityMonth
                //2.yeildQuarterAccumulate = designCapacityMonth + yeildMonthActual
                //拿到理论月产能
                int designCapacityMonth = Integer.parseInt(designCapacityPerMonth);
                //将月份数据写入ProductLineCapacityMonth张表中,用productLineCapacityId来区分不同产线
                //并且将刚才已经拿出的年份和季度直接set
                productLineCapacityMonth.setProductLineCapacityId(Integer.valueOf(productLineCapacityId));
                productLineCapacityMonth.setYear(Integer.valueOf(yearCellValue));
                //当季度末的时候才向这条记录中插入季度数据
                if(i == 3){
                    //只有3、6、9、12月份后面会写季度，方便查询数据
                    productLineCapacityMonth.setQuarter(quarterCellValue);
                }
                //月份判断进行操作
                if(a == 1){
                    String yeildMonthActual = cells.get(SheetService.columnToIndex("M"));
                    productLineCapacityMonth.setMonth("1");
                    productLineCapacityMonth.setQuarterMark("1");
                    if(yeildMonthActual.equals("")){
                        productLineCapacityMonth.setYeildMonthActual(0);
                    }else{
                        productLineCapacityMonth.setYeildMonthActual(Integer.parseInt(yeildMonthActual));
                    }
                }else if(a == 2){
                    String yeildMonthActual = cells.get(SheetService.columnToIndex("N"));
                    productLineCapacityMonth.setMonth("2");
                    productLineCapacityMonth.setQuarterMark("1");
                    if(yeildMonthActual.equals("")){
                        productLineCapacityMonth.setYeildMonthActual(0);
                    }else{
                        productLineCapacityMonth.setYeildMonthActual(Integer.parseInt(yeildMonthActual));
                    }
                }else if(a == 3){
                    String yeildMonthActual = cells.get(SheetService.columnToIndex("O"));
                    productLineCapacityMonth.setMonth("3");
                    productLineCapacityMonth.setQuarterMark("1");
                    if(yeildMonthActual.equals("")){
                        productLineCapacityMonth.setYeildMonthActual(0);
                        //拿到季末月产量
                        double monthActual = 0;
                        //接下来进行计算[截至当前季度负荷率]
                        double lineLoadRateQuarter = monthActual / designCapacityMonth;
                        //将[截至当前季度负荷率]插入ProductLineCapacityMonth表中
                        productLineCapacityMonth.setLineLoadRateQuarter(lineLoadRateQuarter);
                    }else{
                        productLineCapacityMonth.setYeildMonthActual(Integer.parseInt(yeildMonthActual));
                        //把季末月产量传出去
                        double monthActual = Double.parseDouble(yeildMonthActual);
                        //接下来进行计算[截至当前季度负荷率]
                        double lineLoadRateQuarter = monthActual / designCapacityMonth;
                        //将[截至当前季度负荷率]插入ProductLineCapacityMonth表中
                        productLineCapacityMonth.setLineLoadRateQuarter(lineLoadRateQuarter);
                    }
                }else if(a == 4){
                    String yeildMonthActual = cells.get(SheetService.columnToIndex("P"));
                    productLineCapacityMonth.setMonth("4");
                    productLineCapacityMonth.setQuarterMark("2");
                    if(yeildMonthActual.equals("")){
                        productLineCapacityMonth.setYeildMonthActual(0);
                    }else{
                        productLineCapacityMonth.setYeildMonthActual(Integer.parseInt(yeildMonthActual));
                    }
                }else if(a == 5){
                    String yeildMonthActual = cells.get(SheetService.columnToIndex("Q"));
                    productLineCapacityMonth.setMonth("5");
                    productLineCapacityMonth.setQuarterMark("2");
                    if(yeildMonthActual.equals("")){
                        productLineCapacityMonth.setYeildMonthActual(0);
                    }else{
                        productLineCapacityMonth.setYeildMonthActual(Integer.parseInt(yeildMonthActual));
                    }
                }else if(a == 6){
                    String yeildMonthActual = cells.get(SheetService.columnToIndex("R"));
                    productLineCapacityMonth.setMonth("6");
                    productLineCapacityMonth.setQuarterMark("2");
                    if(yeildMonthActual.equals("")){
                        productLineCapacityMonth.setYeildMonthActual(0);
                        //拿到季末月产量
                        double monthActual = 0;
                        //接下来进行计算[截至当前季度负荷率]
                        double lineLoadRateQuarter = monthActual / designCapacityMonth;
                        //将[截至当前季度负荷率]插入ProductLineCapacityMonth表中
                        productLineCapacityMonth.setLineLoadRateQuarter(lineLoadRateQuarter);
                    }else{
                        productLineCapacityMonth.setYeildMonthActual(Integer.parseInt(yeildMonthActual));
                        //把季末月产量传出去
                        double monthActual = Double.parseDouble(yeildMonthActual);
                        //接下来进行计算[截至当前季度负荷率]
                        double lineLoadRateQuarter = monthActual / designCapacityMonth;
                        //将[截至当前季度负荷率]插入ProductLineCapacityMonth表中
                        productLineCapacityMonth.setLineLoadRateQuarter(lineLoadRateQuarter);
                    }
                }else if(a == 7){
                    String yeildMonthActual = cells.get(SheetService.columnToIndex("S"));
                    productLineCapacityMonth.setMonth("7");
                    productLineCapacityMonth.setQuarterMark("3");
                    if(yeildMonthActual.equals("")){
                        productLineCapacityMonth.setYeildMonthActual(0);
                    }else{
                        productLineCapacityMonth.setYeildMonthActual(Integer.parseInt(yeildMonthActual));
                    }
                }else if(a == 8){
                    String yeildMonthActual = cells.get(SheetService.columnToIndex("T"));
                    productLineCapacityMonth.setMonth("8");
                    productLineCapacityMonth.setQuarterMark("3");
                    if(yeildMonthActual.equals("")){
                        productLineCapacityMonth.setYeildMonthActual(0);
                    }else{
                        productLineCapacityMonth.setYeildMonthActual(Integer.parseInt(yeildMonthActual));
                    }
                }else if(a == 9){
                    String yeildMonthActual = cells.get(SheetService.columnToIndex("U"));
                    productLineCapacityMonth.setMonth("9");
                    productLineCapacityMonth.setQuarterMark("3");
                    if(yeildMonthActual.equals("")){
                        productLineCapacityMonth.setYeildMonthActual(0);
                        //拿到季末月产量
                        double monthActual = 0;
                        //接下来进行计算[截至当前季度负荷率]
                        double lineLoadRateQuarter = monthActual / designCapacityMonth;
                        //将[截至当前季度负荷率]插入ProductLineCapacityMonth表中
                        productLineCapacityMonth.setLineLoadRateQuarter(lineLoadRateQuarter);
                    }else{
                        productLineCapacityMonth.setYeildMonthActual(Integer.parseInt(yeildMonthActual));
                        //把季末月产量传出去
                        double monthActual = Double.parseDouble(yeildMonthActual);
                        //接下来进行计算[截至当前季度负荷率]
                        double lineLoadRateQuarter = monthActual / designCapacityMonth;
                        //将[截至当前季度负荷率]插入ProductLineCapacityMonth表中
                        productLineCapacityMonth.setLineLoadRateQuarter(lineLoadRateQuarter);
                    }
                }else if(a == 10){
                    String yeildMonthActual = cells.get(SheetService.columnToIndex("V"));
                    productLineCapacityMonth.setMonth("10");
                    productLineCapacityMonth.setQuarterMark("4");
                    if(yeildMonthActual.equals("")){
                        productLineCapacityMonth.setYeildMonthActual(0);
                    }else{
                        productLineCapacityMonth.setYeildMonthActual(Integer.parseInt(yeildMonthActual));
                    }
                }else if(a == 11){
                    String yeildMonthActual = cells.get(SheetService.columnToIndex("W"));
                    productLineCapacityMonth.setMonth("11");
                    productLineCapacityMonth.setQuarterMark("4");
                    if(yeildMonthActual.equals("")){
                        productLineCapacityMonth.setYeildMonthActual(0);
                    }else{
                        productLineCapacityMonth.setYeildMonthActual(Integer.parseInt(yeildMonthActual));
                    }
                }else if(a == 12){
                    //在年末求算各个项目年度产量总和
                    //首先拿出各个月的值
                    String yeildMonthActual1 = cells.get(SheetService.columnToIndex("M"));
                    if(yeildMonthActual1.equals("")){
                        yeildMonthActual1 = "0";
                    }
                    String yeildMonthActual2 = cells.get(SheetService.columnToIndex("N"));
                    if(yeildMonthActual2.equals("")){
                        yeildMonthActual2 = "0";
                    }
                    String yeildMonthActual3 = cells.get(SheetService.columnToIndex("O"));
                    if(yeildMonthActual3.equals("")){
                        yeildMonthActual3 = "0";
                    }
                    String yeildMonthActual4 = cells.get(SheetService.columnToIndex("P"));
                    if(yeildMonthActual4.equals("")){
                        yeildMonthActual4 = "0";
                    }
                    String yeildMonthActual5 = cells.get(SheetService.columnToIndex("Q"));
                    if(yeildMonthActual5.equals("")){
                        yeildMonthActual5 = "0";
                    }
                    String yeildMonthActual6 = cells.get(SheetService.columnToIndex("R"));
                    if(yeildMonthActual6.equals("")){
                        yeildMonthActual6 = "0";
                    }
                    String yeildMonthActual7 = cells.get(SheetService.columnToIndex("S"));
                    if(yeildMonthActual7.equals("")){
                        yeildMonthActual7 = "0";
                    }
                    String yeildMonthActual8 = cells.get(SheetService.columnToIndex("T"));
                    if(yeildMonthActual8.equals("")){
                        yeildMonthActual8 = "0";
                    }
                    String yeildMonthActual9 = cells.get(SheetService.columnToIndex("U"));
                    if(yeildMonthActual9.equals("")){
                        yeildMonthActual9 = "0";
                    }
                    String yeildMonthActual10 = cells.get(SheetService.columnToIndex("V"));
                    if(yeildMonthActual10.equals("")){
                        yeildMonthActual10 = "0";
                    }
                    String yeildMonthActual11 = cells.get(SheetService.columnToIndex("W"));
                    if(yeildMonthActual11.equals("")){
                        yeildMonthActual11 = "0";
                    }
                    String yeildMonthActual = cells.get(SheetService.columnToIndex("X"));
                    if(yeildMonthActual.equals("")){
                        yeildMonthActual = "0";
                    }
                    //进行求和
                    int yeildYearAccumulate = Integer.parseInt(yeildMonthActual1) + Integer.parseInt(yeildMonthActual2)
                            + Integer.parseInt(yeildMonthActual3) + Integer.parseInt(yeildMonthActual4)
                            + Integer.parseInt(yeildMonthActual5) + Integer.parseInt(yeildMonthActual6)
                            + Integer.parseInt(yeildMonthActual7) + Integer.parseInt(yeildMonthActual8)
                            + Integer.parseInt(yeildMonthActual9) + Integer.parseInt(yeildMonthActual10)
                            + Integer.parseInt(yeildMonthActual11) + Integer.parseInt(yeildMonthActual);
                    //将年度总和插入数据库
                    productLineCapacityMonth.setYeildYearAccumulate(yeildYearAccumulate);
                    productLineCapacityMonth.setMonth("12");
                    productLineCapacityMonth.setQuarterMark("4");
                    if(yeildMonthActual.equals("")){
                        productLineCapacityMonth.setYeildMonthActual(0);
                        //拿到季末月产量
                        double monthActual = 0;
                        //接下来进行计算[截至当前季度负荷率]
                        double lineLoadRateQuarter = monthActual / designCapacityMonth;
                        //将[截至当前季度负荷率]插入ProductLineCapacityMonth表中
                        productLineCapacityMonth.setLineLoadRateQuarter(lineLoadRateQuarter);
                    }else{
                        productLineCapacityMonth.setYeildMonthActual(Integer.parseInt(yeildMonthActual));
                        //把季末月产量传出去
                        double monthActual = Double.parseDouble(yeildMonthActual);
                        //接下来进行计算[截至当前季度负荷率]
                        double lineLoadRateQuarter = monthActual / designCapacityMonth;
                        //将[截至当前季度负荷率]插入ProductLineCapacityMonth表中
                        productLineCapacityMonth.setLineLoadRateQuarter(lineLoadRateQuarter);
                    }
                }
                //本季度末时,set数据，并将数据插入数据库
                if(i == 3){
                    productLineCapacityList.setProductLineCapacityId(Integer.valueOf(productLineCapacityId));
                    productLineCapacityList.setCompanyName(companyNameCellValue);
                    productLineCapacityList.setProductTypeName(productTypeName);
                    productLineCapacityList.setProductLineName(productLineName);
                    productLineCapacityList.setYearVehicle(yearVehicle);
                    productLineCapacityList.setProductLineTypeName(productLineTypeName);
                    productLineCapacityList.setSopDate(sopDate);
                    productLineCapacityList.setDesignProductionTakt(designProductionTakt);
                    productLineCapacityList.setDesignCapacityPerYear(designCapacityPerYear);
                    productLineCapacityList.setMarketShares(marketShares);
                    productLineCapacityList.setDesignActualTakt(designActualTakt);
                    productLineCapacityList.setDesignCapacityPerMonth(Integer.parseInt(designCapacityPerMonth));
                    productLineCapacityList.setYearAim(Double.valueOf(yearAim));
                    //不同季度可能会导入相同项目，但是么必要再次存储，因此需要根据类别、产线名称、生产车型来进行判断数据库中是否存在此条记录，
                    // 存在不插，不存在新增,并且如果yearVehicle为NULL的话就可以直接用类别、产线名称唯一确定
                    QueryWrapper<ProductLineCapacityList> queryWrapper = new QueryWrapper<>();
                    if(yearVehicle.equals("")){
                        queryWrapper.eq("productTypeName",productTypeName).eq("productLineName",productLineName);
                    }else{
                        queryWrapper.eq("productTypeName",productTypeName).eq("productLineName",productLineName)
                                .eq("yearVehicle",yearVehicle);
                    }
                    List<Map<String, Object>> maps = productLineCapacityListMapper.selectMaps(queryWrapper);
                    //当数据库中有相同的数据时，只需让计数器变量归零即可，无需做任何操作
                    if(maps.size() != 0){
                        i = 0;
                    }else{
                        productLineCapacityListMapper.insert(productLineCapacityList);
                    }
                    //最后让计数器回归0
                    i = 0;
                }
                //读取一行插一行
                productLineCapacityMonthMapper.insert(productLineCapacityMonth);
                cell.setCellValue("导入成功");
            }
        }
        sheetService.write(attachment.getPath(), attachment.getFilename());// 写入excel表
        return result;
    }

    /**
     * 填写存在问题及对策
     * @param productLineCapacityMonth
     * @return
     */
    @Override
    public boolean updateViewProductLineCapacityListData(ProductLineCapacityMonth productLineCapacityMonth) {
        int result = productLineCapacityMonthMapper.updateById(productLineCapacityMonth);
        if (result > 0) {
            return true;
        }
        return false;
    }

    /**
     * 以主键删除 选中的重点产线产能记录
     * @param id
     * @return
     */
    @Override
    public boolean deleteViewProductLineCapacityListDataById(Integer id) {
        int result = productLineCapacityMonthMapper.deleteById(id);
        if (result > 0) {
            return true;
        }
        return false;
    }

    /**
     * 查询一年十二个月的数据，展示查询，包含条件查询
     * @param params
     * @return
     */
    @Override
    public Page<Map<String, Object>> findViewProductLineCapacityListDataMapListPerPageByParams(Map<String, Object> params,String quarterMark) {
        QueryWrapper<ViewProductLineCapacityList> queryWrapper = getQuarterQueryWrapper(params,quarterMark);
        int limit = (int)params.get("limit");
        int page = (int)params.get("page");
        Page<Map<String,Object>> myPage = new Page<>(page, limit);
        return viewProductLineCapacityListMapper.selectMapsPage(myPage, queryWrapper);
    }

    /**
     * 查询条件 重点产线产能季度推移构建查询
     * @param params
     * @return
     */
    private QueryWrapper<ViewProductLineCapacityList> getQuarterQueryWrapper(Map<String, Object> params,String quarterMark) {
        QueryWrapper<ViewProductLineCapacityList> queryWrapper = new QueryWrapper<>();
        int month = 1;
        StringBuilder selectStr = new StringBuilder("productTypeName,productLineName,yearVehicle,year");
        do{
            selectStr.append(", sum(if(MONTH =" +  month + ",yeildMonthActual,null)) AS 'month" + month + "'");
            month++;
        }while (month <= 12);
        if(quarterMark.equals("1")){
            queryWrapper.eq("quarterMark","1");
        }else if(quarterMark.equals("2")){
            queryWrapper.eq("quarterMark","1").or()
                    .eq("quarterMark","2");
        }else if(quarterMark.equals("3")){
            queryWrapper.eq("quarterMark","1").or()
                    .eq("quarterMark","2").or()
                    .eq("quarterMark","3");
        }else if(quarterMark.equals("4")){
            queryWrapper.eq("quarterMark","1").or()
                    .eq("quarterMark","2").or()
                    .eq("quarterMark","3").or()
                    .eq("quarterMark","4");
        }

        queryWrapper.select(selectStr.toString()).groupBy("productTypeName,productLineName,yearVehicle,year");
        if (params.containsKey("select")) {
            queryWrapper.select((String) params.get("select"));
        }
        if (params.containsKey("productTypeName")) {
            queryWrapper.like("productTypeName", params.get("productTypeName"));
        }
        if (params.containsKey("productLineName")) {
            queryWrapper.like("productLineName", params.get("productLineName"));
        }
        if (params.containsKey("year")) {
            queryWrapper.eq("year", params.get("year"));
        }
        if (params.containsKey("yearVehicle")) {
            queryWrapper.eq("yearVehicle", params.get("yearVehicle"));
        }
        if (params.containsKey("yeildMonthActual")) {
            queryWrapper.eq("yeildMonthActual", params.get("yeildMonthActual"));
        }
        return queryWrapper;
    }

    /**
     * 查询条件 在重点产线产能数据库中进行查询
     * @param params
     * @return
     */
    private QueryWrapper<ViewProductLineCapacityList> getQueryWrapper(Map<String, Object> params) {
        QueryWrapper<ViewProductLineCapacityList> queryWrapper = new QueryWrapper<>();
        if (params.containsKey("select")) {
            queryWrapper.select((String) params.get("select"));
        }
        if (params.containsKey("productTypeName")) {
            queryWrapper.like("productTypeName", params.get("productTypeName"));
        }
        if (params.containsKey("productLineName")) {
            queryWrapper.like("productLineName", params.get("productLineName"));
        }
        if (params.containsKey("year")) {
            queryWrapper.eq("year", params.get("year"));
        }
        if (params.containsKey("quarter")) {
            queryWrapper.eq("quarter", params.get("quarter"));
        }
        if (params.containsKey("yearVehicle")) {
            queryWrapper.eq("yearVehicle", params.get("yearVehicle"));
        }
        return queryWrapper;
    }
}
