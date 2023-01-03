package com.fssy.shareholder.management.service.system.impl.operate.invest;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fssy.shareholder.management.mapper.system.operate.invest.InvestProjectListMapper;
import com.fssy.shareholder.management.mapper.system.operate.invest.InvestProjectPlanTraceDetailMapper;
import com.fssy.shareholder.management.mapper.system.operate.invest.InvestProjectPlanTraceMapper;
import com.fssy.shareholder.management.pojo.system.config.Attachment;
import com.fssy.shareholder.management.pojo.system.operate.invest.InvestProjectList;
import com.fssy.shareholder.management.pojo.system.operate.invest.InvestProjectPlanTrace;
import com.fssy.shareholder.management.pojo.system.operate.invest.TechCapacityEvaluate;
import com.fssy.shareholder.management.service.common.SheetService;
import com.fssy.shareholder.management.service.system.operate.invest.InvestProjectPlanTraceService;
import com.fssy.shareholder.management.tools.exception.ServiceException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * **数据表名：	bs_operate_invest_project_plan_trace	**数据表中文名：	项目进度计划跟踪表	**业务部门：	经营管理部	**数据表作用：	用于记录单一项目进展详情及跟踪情况	**创建人创建日期：	TerryZeng 2022-12-2 服务实现类
 * </p>
 *
 * @author Solomon
 * @since 2022-12-09
 */
@Service
public class InvestProjectPlanTraceServiceImpl extends ServiceImpl<InvestProjectPlanTraceMapper, InvestProjectPlanTrace> implements InvestProjectPlanTraceService {

    @Autowired
    private SheetService sheetService;

    @Autowired
    private InvestProjectPlanTraceMapper investProjectPlanTraceMapper;

    @Autowired
    private InvestProjectListMapper investProjectListMapper;

    @Autowired
    private InvestProjectPlanTraceDetailMapper investProjectPlanTraceDetailMapper;

    /**
     * 查询所有项目进度计划跟踪表信息
     * @param params
     * @return
     */
    @Override
    public List<Map<String, Object>> findInvestProjectPlanTracejectDataByParams(Map<String, Object> params) {
        QueryWrapper<InvestProjectPlanTrace> queryWrapper = getQueryWrapper(params);
        return investProjectPlanTraceMapper.selectMaps(queryWrapper);
    }

    /**
     * 分页查询项目进度计划跟踪表信息
     * @param params
     * @return
     */
    @Override
    public Page<InvestProjectPlanTrace> findInvestProjectPlanTraceDataListPerPageByParams(Map<String, Object> params) {
        QueryWrapper<InvestProjectPlanTrace> queryWrapper = getQueryWrapper(params);
        int limit = (int) params.get("limit");
        int page = (int) params.get("page");
        Page<InvestProjectPlanTrace> myPage = new Page<>(page, limit);
        return investProjectPlanTraceMapper.selectPage(myPage, queryWrapper);

    }

    @Override
    @Transactional
    public Map<String, Object> reaadInvestProjectPlanTraceDataSource(Attachment attachment, String year, String companyName, String month, String projectName,String projectListId) {

        // 返回消息
        Map<String, Object> result = new HashMap<>();
        result.put("content", "");
        result.put("failed", false);
        // 读取附件
        sheetService.load(attachment.getPath(), attachment.getFilename()); // 根据路径和名称读取附件
        // 读取表单
        sheetService.readByName("项目进度计划跟踪表"); //根据表单名称获取该工作表单

        // 获取表单数据
        Sheet sheet = sheetService.getSheet();

        if (ObjectUtils.isEmpty(sheet)) {
            throw new ServiceException("表单【项目进度计划跟踪表】不存在，无法读取数据，请检查");
        }
        // 获取单价列表数据
        Row row;
        //实体类集合，用于后面的批量写入数据库
        List<TechCapacityEvaluate> operateTechCapacityEvaluates = new ArrayList<>();
        // 2022-06-01 从决策系统导出数据，存在最后几行为空白数据，导致报数据越界问题，这里的长度由表头长度控制
        short maxSize = sheet.getRow(0).getLastCellNum();//列数(表头长度)


        Cell abstracte = sheet.getRow(1).getCell(SheetService.columnToIndex("C"));
        Cell evaluates = sheet.getRow(2).getCell(SheetService.columnToIndex("C"));

        Cell companyNames = sheet.getRow(0).getCell(SheetService.columnToIndex("C"));
        String companyNameValue = String.valueOf(companyNames);

        Cell projectNames = sheet.getRow(0).getCell(SheetService.columnToIndex("G"));
        String projectNameValue = String.valueOf(projectNames);

        Cell years = sheet.getRow(0).getCell(SheetService.columnToIndex("J"));
        String yearValue = String.valueOf(years);
        String yearValues = String.valueOf(Math.round(Float.parseFloat(yearValue)));


        Cell months = sheet.getRow(0).getCell(SheetService.columnToIndex("M"));
        String monthValue = String.valueOf(months);
        String monthValues = String.valueOf(Math.round(Float.parseFloat(monthValue)));

        //效验年份、公司名称
        if (!companyNameValue.equals(companyName)) {
            throw new ServiceException("导入的公司名称与excel中的公司名称不一致，导入失败");
        }
        if (!yearValues.equals(year)) {
            throw new ServiceException("导入的公司年份与excel中的公司年份不一致，导入失败");
        }
        if (!projectNameValue.equals(projectName)) {
            throw new ServiceException("导入的公司项目名称与excel中的公司项目名称不一致，导入失败");
        }
        if (!monthValues.equals(month)) {
            throw new ServiceException("导入的公司月份与excel中的公司月份不一致，导入失败");
        }


        // 根据id进行判断，存在则更新，不存在则新增
        //investProjectPlanTraceDetailMapper.insert(investProjectPlanTraceDetail);

        //investProjectPlanTraceDetailMapper.insertBatchSomeColumn();
        //每次导入更新前，删除前面导入旧数据
        QueryWrapper<InvestProjectPlanTrace> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("month",month).eq("year",year).eq("companyName",companyName).eq("projectName",projectName).eq("projectListId",projectListId);
        investProjectPlanTraceMapper.delete(queryWrapper);


        List<InvestProjectPlanTrace> investProjectPlanTraces = new ArrayList<>();
//        InvestProjectPlanTrace investProjectPlanTraceOne = new InvestProjectPlanTrace();
//        investProjectPlanTraceOne.setAbstracte(String.valueOf(abstracte));
//        investProjectPlanTraceOne.setEvaluateSum(String.valueOf(evaluates));
//        investProjectPlanTraceOne.setMonth(Integer.valueOf(month));
//        investProjectPlanTraceOne.setYear(Integer.valueOf(year));
//        investProjectPlanTraceOne.setCompanyName(companyName);
//        investProjectPlanTraceOne.setProjectName(projectName);
//        investProjectPlanTraceOne.setProjectListId(Integer.valueOf(projectListId));
//        investProjectPlanTraces.add(investProjectPlanTraceOne);

        //每次导入更新前，删除前面导入旧数据
//        QueryWrapper<InvestProjectPlanTraceDetail> detailQueryWrapper = new QueryWrapper<>();
//        detailQueryWrapper.eq("month",month).eq("year",year).eq("companyName",companyName).eq("projectName",projectName).eq("projectListId",projectListId);
//        investProjectPlanTraceDetailMapper.delete(detailQueryWrapper);

        //导入信息
//        InvestProjectPlanTraceDetail investProjectPlanTraceDetail = new InvestProjectPlanTraceDetail();
//        investProjectPlanTraceDetail.setAbstracte(String.valueOf(abstracte));
//        investProjectPlanTraceDetail.setEvaluate(String.valueOf(evaluates));
//        investProjectPlanTraceDetail.setMonth(Integer.valueOf(month));
//        investProjectPlanTraceDetail.setYear(Integer.valueOf(year));
//        investProjectPlanTraceDetail.setCompanyName(companyName);
//        investProjectPlanTraceDetail.setProjectName(projectName);
//        investProjectPlanTraceDetail.setProjectListId(Integer.valueOf(projectListId));
//
//        List<InvestProjectPlanTraceDetail> investProjectPlanTraceDetails = new ArrayList<>();
//        investProjectPlanTraceDetails.add(investProjectPlanTraceDetail);
//        investProjectPlanTraceDetailMapper.insertBatchSomeColumn(investProjectPlanTraceDetails);




        //InvestProjectPlanTrace investProjectPlanTrace = new InvestProjectPlanTrace();


        // 循环总行数(不读表的标题，从第1行开始读)
        for (int j = 4; j <= sheet.getLastRowNum(); j++) {// getPhysicalNumberOfRows()此方法不会将空白行计入行数
            InvestProjectPlanTrace investProjectPlanTrace = new InvestProjectPlanTrace();
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
            Cell cell = row.createCell(SheetService.columnToIndex("O"));
            String serial = cells.get(SheetService.columnToIndex("A"));
            if (serial.equals("编制")){
                break;
            }
            String projectPhase = cells.get(SheetService.columnToIndex("B"));
            String projectContent = cells.get(SheetService.columnToIndex("C"));
            String projectIndicators = cells.get(SheetService.columnToIndex("D"));
            String projectTarget = cells.get(SheetService.columnToIndex("E"));
            String feasibilityDate = cells.get(SheetService.columnToIndex("F"));
            String contractDate = cells.get(SheetService.columnToIndex("G"));
            String actualEndDate = cells.get(SheetService.columnToIndex("H"));
            String inspectionDate = cells.get(SheetService.columnToIndex("I"));
            String responsePerson = cells.get(SheetService.columnToIndex("J"));
            String Inspectedby = cells.get(SheetService.columnToIndex("K"));
            String inspectionResult = cells.get(SheetService.columnToIndex("L"));
            String evaluate = cells.get(SheetService.columnToIndex("M"));
            String note = cells.get(SheetService.columnToIndex("N"));
            //String serialId = cells.get(SheetService.columnToIndex("O"));


            QueryWrapper<InvestProjectList> investProjectListQueryWrapper = new QueryWrapper<>();
            investProjectListQueryWrapper.eq("companyName",companyName).eq("year",year).eq("month",month).eq("projectName",projectName);
            List<InvestProjectList> investProjectLists = investProjectListMapper.selectList(investProjectListQueryWrapper);
            if (investProjectLists.size()>1){
                setFailedContent(result,String.format("第%s行的指标存在多条",j+1));
                cell.setCellValue("存在多个指标,检查数据是否正确");
                continue;
            }
            if (investProjectLists.size() == 0 ){
                setFailedContent(result,String.format("第%s行的指标不存在",j+1));
                cell.setCellValue("指标不存在,检查数据是否正确");
                continue;
            }

            investProjectPlanTrace.setSerial(serial);
            investProjectPlanTrace.setProjectPhase(projectPhase);
            investProjectPlanTrace.setProjectContent(projectContent);
            if (!ObjectUtils.isEmpty(projectIndicators)){
                investProjectPlanTrace.setProjectIndicators(projectIndicators);
            }
            investProjectPlanTrace.setProjectTarget(projectTarget);
            if (!ObjectUtils.isEmpty(feasibilityDate)){
                investProjectPlanTrace.setFeasibilityDate(LocalDate.parse(feasibilityDate));
            }
            if (!ObjectUtils.isEmpty(contractDate)){
                investProjectPlanTrace.setContractDate(LocalDate.parse(contractDate));
            }
            if (!ObjectUtils.isEmpty(actualEndDate)){
                investProjectPlanTrace.setActualEndDate(LocalDate.parse(actualEndDate));
            }
            if (!ObjectUtils.isEmpty(inspectionDate)){
                investProjectPlanTrace.setInspectionDate(LocalDate.parse(inspectionDate));
            }

            investProjectPlanTrace.setResponsePerson(responsePerson);
            investProjectPlanTrace.setInspectedby(Inspectedby);
            investProjectPlanTrace.setInspectionResult(inspectionResult);
            investProjectPlanTrace.setEvaluate(evaluate);
            investProjectPlanTrace.setNote(note);
            investProjectPlanTrace.setYear(Integer.valueOf(year));
            investProjectPlanTrace.setMonth(Integer.valueOf(month));
            investProjectPlanTrace.setCompanyName(companyName);
            investProjectPlanTrace.setProjectName(projectName);
            investProjectPlanTrace.setProjectListId(Integer.valueOf(projectListId));
            //investProjectPlanTrace.setSerialId(Integer.valueOf(serialId));
            investProjectPlanTrace.setAbstracte(String.valueOf(abstracte));
            investProjectPlanTrace.setEvaluateSum(String.valueOf(evaluates));

//             // 根据id进行判断，存在则更新，不存在则新增
//            saveOrUpdate(investProjectPlanTrace);
            investProjectPlanTraces.add(investProjectPlanTrace);
            //saveOrUpdate(investProjectPlanTrace);
            cell.setCellValue("导入成功");
        }
        if (!ObjectUtils.isEmpty(investProjectPlanTraces)){
            investProjectPlanTraceMapper.insertBatchSomeColumn(investProjectPlanTraces);
        }



//        // 循环总行数(不读表的标题，从第18行开始读)
//        for (int j = 18; j < 21; j++) {// getPhysicalNumberOfRows()此方法不会将空白行计入行数
//            List<String> cells = new ArrayList<>();// 每一行的数据用一个list接收
//            row = sheet.getRow(j);// 获取第j行
//            // 获取一行中有多少列 Row：行，cell：列
//            // 循环row行中的每一个单元格
//            for (int k = 0; k < maxSize; k++) {
//                // 如果这单元格为空，就写入空
//                if (row.getCell(k) == null) {
//                    cells.add("");
//                    continue;
//                }
//                // 处理单元格读取到公式的问题
//                if (row.getCell(k).getCellType() == CellType.FORMULA) {
//                    row.getCell(k).setCellType(CellType.STRING);
//                    String res = row.getCell(k).getStringCellValue();
//                    cells.add(res);
//                    continue;
//                }
//                Cell cell = row.getCell(k);
//                String res = sheetService.getValue(cell).trim();// 获取单元格的值
//                cells.add(res);// 将单元格的值写入行
//            }
//            // 导入结果写入列
//            Cell cell = row.createCell(SheetService.columnToIndex("V"));
//            String serial = cells.get(SheetService.columnToIndex("A"));
//            String projectPhase = cells.get(SheetService.columnToIndex("B"));
//            String projectName = cells.get(SheetService.columnToIndex("C"));
//            String projectIndicators = cells.get(SheetService.columnToIndex("D"));
//            String inspectionTime = cells.get(SheetService.columnToIndex("I"));
//            String responsePerson = cells.get(SheetService.columnToIndex("J"));
//            String Inspectedby = cells.get(SheetService.columnToIndex("K"));
//            String inspectionResult = cells.get(SheetService.columnToIndex("L"));
//            String evaluate = cells.get(SheetService.columnToIndex("M"));
//            String note = cells.get(SheetService.columnToIndex("N"));
//            String companyNames = cells.get(SheetService.columnToIndex("Q"));
//            String years = cells.get(SheetService.columnToIndex("R"));
////            String month = cells.get(SheetService.columnToIndex("S"));
////            String departmentName = cells.get(SheetService.columnToIndex("T"));
//
//
//
//
//            investProjectPlanTrace.setSerial(serial);
//            investProjectPlanTrace.setProjectPhase(projectPhase);
//            investProjectPlanTrace.setSerial(projectIndicators);
//            investProjectPlanTrace.setProjectName(projectName);
//            investProjectPlanTrace.setStartDate(LocalDate.parse(inspectionTime));
//            investProjectPlanTrace.setEndDate(LocalDate.parse(responsePerson));
//            investProjectPlanTrace.setActualEndDate(LocalDate.parse(Inspectedby));
//            investProjectPlanTrace.setStatus(inspectionResult);
//            investProjectPlanTrace.setIssue(evaluate);
//            investProjectPlanTrace.setMilestone(note);
//            investProjectPlanTrace.setIssue(years);
////            investProjectPlanTrace.setImproveAction(month);
////            investProjectPlanTrace.setProjectTrace(departmentName);
//            investProjectPlanTrace.setProjectTrace(companyNames);
//
//            // 根据id进行判断，存在则更新，不存在则新增
//            saveOrUpdate(investProjectPlanTrace);
//            cell.setCellValue("导入成功");
//
//        }
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


    private QueryWrapper<InvestProjectPlanTrace> getQueryWrapper(Map<String,Object> params){
        QueryWrapper<InvestProjectPlanTrace> queryWrapper = new QueryWrapper<>();
        if (params.containsKey("select")) {
            queryWrapper.select((String) params.get("select"));
        }
        if (params.containsKey("id")) {
            queryWrapper.eq("id", params.get("id"));
        }
        if (params.containsKey("year")) {
            queryWrapper.eq("year", params.get("year"));
        }
        if (params.containsKey("projectListId")) {
            queryWrapper.eq("projectListId", params.get("projectListId"));
        }
        if (params.containsKey("serial")) {
            queryWrapper.like("serial", params.get("serial"));
        }
        if (params.containsKey("projectName")) {
            queryWrapper.like("projectName", params.get("projectName"));
        }
        if (params.containsKey("projectTarget")) {
            queryWrapper.like("projectTarget", params.get("projectTarget"));
        }
        if (params.containsKey("outResult")) {
            queryWrapper.like("outResult", params.get("outResult"));
        }
        if (params.containsKey("responsePerson")) {
            queryWrapper.like("responsePerson", params.get("responsePerson"));
        }
        if (params.containsKey("otherResponsePerson")) {
            queryWrapper.like("otherResponsePerson", params.get("otherResponsePerson"));
        }
        if (params.containsKey("startDate")) {
            queryWrapper.like("startDate", params.get("startDate"));
        }
        if (params.containsKey("endDate")) {
            queryWrapper.like("endDate", params.get("endDate"));
        }
        if (params.containsKey("departmentName")) {
            queryWrapper.like("departmentName", params.get("departmentName"));
        }
        if (params.containsKey("isNormal")) {
            queryWrapper.like("isNormal", params.get("isNormal"));
        }
        if (params.containsKey("milestone")) {
            queryWrapper.like("milestone", params.get("milestone"));
        }
        if (params.containsKey("actualEndDate")) {
            queryWrapper.like("actualEndDate", params.get("actualEndDate"));
        }
        if (params.containsKey("issue")) {
            queryWrapper.like("issue", params.get("issue"));
        }
        if (params.containsKey("improveAction")) {
            queryWrapper.like("improveAction", params.get("improveAction"));
        }
        if (params.containsKey("projectTrace")) {
            queryWrapper.like("projectTrace", params.get("projectTrace"));
        }
        if (params.containsKey("projectPhase")) {
            queryWrapper.like("projectPhase", params.get("projectPhase"));
        }
        if (params.containsKey("projectIndicators")) {
            queryWrapper.like("projectIndicators", params.get("projectIndicators"));
        }
        if (params.containsKey("inspectionDate")) {
            queryWrapper.like("inspectionDate", params.get("inspectionDate"));
        }
        if (params.containsKey("inspectionResult")) {
            queryWrapper.like("inspectionResult", params.get("inspectionResult"));
        }
        if (params.containsKey("evaluate")) {
            queryWrapper.like("evaluate", params.get("evaluate"));
        }
        if (params.containsKey("Inspectedby")) {
            queryWrapper.like("Inspectedby", params.get("Inspectedby"));
        }
        if (params.containsKey("feasibilityDate")) {
            queryWrapper.like("feasibilityDate", params.get("feasibilityDate"));
        }
        if (params.containsKey("contractDate")) {
            queryWrapper.like("contractDate", params.get("contractDate"));
        }
        if (params.containsKey("companyName")) {
            queryWrapper.like("companyName", params.get("companyName"));
        }
        if (params.containsKey("month")) {
            queryWrapper.eq("month", params.get("month"));
        }
        if (params.containsKey("year")) {
            queryWrapper.eq("year", params.get("year"));
        }
        if (params.containsKey("projectContent")) {
            queryWrapper.like("projectContent", params.get("projectContent"));
        }
        if (params.containsKey("note")) {
            queryWrapper.like("note", params.get("note"));
        }
        if (params.containsKey("serialId")) {
            queryWrapper.like("serialId", params.get("serialId"));
        }
        if (params.containsKey("abstracte")) {
            queryWrapper.like("abstracte", params.get("abstracte"));
        }
        if (params.containsKey("evaluateSum")) {
            queryWrapper.like("evaluateSum", params.get("evaluateSum"));
        }
        return queryWrapper;
    }



}
