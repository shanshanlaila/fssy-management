package com.fssy.shareholder.management.service.system.impl.performance.manage_kpi;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fssy.shareholder.management.mapper.manage.company.CompanyMapper;
import com.fssy.shareholder.management.mapper.system.performance.manage_kpi.ManageKpiMonthAimMapper;
import com.fssy.shareholder.management.mapper.system.performance.manage_kpi.ViewManageYearMonthScoreMapper;
import com.fssy.shareholder.management.pojo.manage.company.Company;
import com.fssy.shareholder.management.pojo.system.config.Attachment;
import com.fssy.shareholder.management.pojo.system.performance.manage_kpi.ManageKpiMonthAim;
import com.fssy.shareholder.management.pojo.system.performance.manage_kpi.ViewManageYearMonthScore;
import com.fssy.shareholder.management.service.common.SheetService;
import com.fssy.shareholder.management.service.system.performance.manage_kpi.ViewManageYearMonthScoreService;
import com.fssy.shareholder.management.tools.exception.ServiceException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 计算经营管理月度分数视图 服务实现类
 * </p>
 *
 * @author Shizn
 * @since 2022-11-10
 */
@Service
public class ViewManageYearMonthScoreServiceImpl extends ServiceImpl<ViewManageYearMonthScoreMapper, ViewManageYearMonthScore> implements ViewManageYearMonthScoreService {
    @Autowired
    private ManageKpiMonthAimMapper manageKpiMonthAimMapper;
    @Autowired
    private ViewManageYearMonthScoreMapper viewManageYearMonthScoreMapper;
    @Autowired
    private SheetService sheetService;
    @Autowired
    private ManageKpiMonthAimServiceImpl manageKpiMonthAimServiceImpl;
    @Autowired
    private CompanyMapper companyMapper;

    /**
     * 通过查询条件 分页查询列表
     *
     * @param params 查询条件
     * @return 分页数据
     */
    @Override
    public Page<ViewManageYearMonthScore> findViewManageYearMonthScoreDataListPerPageByParams(Map<String, Object> params) {
        QueryWrapper<ViewManageYearMonthScore> queryWrapper = getQueryWrapper(params);
        queryWrapper.eq("managerKpiMark", "经理人指标");
        queryWrapper.eq("monthActualValueMark", "已生成");
        int limit = (int) params.get("limit");
        int page = (int) params.get("page");
        Page<ViewManageYearMonthScore> myPage = new Page<>(page, limit);
        return viewManageYearMonthScoreMapper.selectPage(myPage, queryWrapper);

    }

    /**
     * 通过查询条件查询履职计划map数据， 用于导出
     *
     * @param params 查询条件
     * @return 数据列表
     */
    @Override
    public List<Map<String, Object>> findViewManageYearMonthScoreMapDataByParams(Map<String, Object> params) {
        QueryWrapper<ViewManageYearMonthScore> queryWrapper = getQueryWrapper(params);
        queryWrapper.eq("managerKpiMark", "经理人指标").eq("evaluateMode", "人工评分").eq("status", "未锁定");
        return viewManageYearMonthScoreMapper.selectMaps(queryWrapper);
    }

    /**
     * 生成分数，同时锁定分数
     *
     * @param
     * @return
     */
    @Override
    @Transactional
    public boolean createScore(Map<String, Object> params) {
        QueryWrapper<ViewManageYearMonthScore> queryWrapper = getQueryWrapper(params);
        //条件查询出所有数据，进行未锁定和绩效指标进行筛选
        List<ViewManageYearMonthScore> viewManageYearMonthScoreList = viewManageYearMonthScoreMapper.selectList(queryWrapper);
        List<ViewManageYearMonthScore> filterList = viewManageYearMonthScoreList.stream()
                .filter(i -> i.getStatus().equals("未锁定") && "已生成".equals(i.getMonthActualValueMark()) && "经理人指标".equals(i.getManagerKpiMark()) && "系统评分".equals(i.getEvaluateMode())).collect(Collectors.toList());
        if (ObjectUtils.isEmpty(filterList)) {
            throw new ServiceException("没有查出数据或已生成！生成失败！");
        }
        //实例化经营管理月度表
        ManageKpiMonthAim manageKpiMonthAim = new ManageKpiMonthAim();
        //遍历
        for (ViewManageYearMonthScore monthScore : filterList) {
            //获取数据
            BigDecimal month = new BigDecimal(monthScore.getMonth());
            BigDecimal accumulateActual = monthScore.getAccumulateActual();
            BigDecimal basicTarget = new BigDecimal(monthScore.getBasicTarget());
            BigDecimal mustInputTarget = new BigDecimal(monthScore.getMustInputTarget());
            BigDecimal reachTarget = new BigDecimal(monthScore.getReachTarget());
            BigDecimal challengeTarget = new BigDecimal(monthScore.getChallengeTarget());
            //对每个月度进行月份目标的计算
            BigDecimal temp = new BigDecimal(12);
            BigDecimal basicTargetMonth = basicTarget.divide(temp).multiply(month);
            BigDecimal mustInputTargetMonth = mustInputTarget.divide(temp).multiply(month);
            BigDecimal reachTargetMonth = reachTarget.divide(temp).multiply(month);
            BigDecimal challengeTargetMonth = challengeTarget.divide(temp).multiply(month);
            //比较大小
            int basic = accumulateActual.compareTo(basicTargetMonth);           //基本
            int mustInput = accumulateActual.compareTo(mustInputTargetMonth);   //必达
            int reach = accumulateActual.compareTo(reachTargetMonth);           //达标
            int challenge = accumulateActual.compareTo(challengeTargetMonth);   //挑战
            //实绩累计值 <月度基本值
            if (basic < 0) {
                manageKpiMonthAim.setScoreAuto(BigDecimal.valueOf(0));  //分数为0
                manageKpiMonthAim.setScoreAdjust(BigDecimal.valueOf(0));  //分数为0
                manageKpiMonthAim.setTargetLevelActual("未达标");
            }
            //实绩累计值 > =月度基本  并且   < 月度必达
            if (basic >= 0 && mustInput < 0) {
                manageKpiMonthAim.setScoreAuto(BigDecimal.valueOf(80));  //分数为80
                manageKpiMonthAim.setScoreAdjust(BigDecimal.valueOf(80));
                manageKpiMonthAim.setTargetLevelActual("基本");
            }
            //实绩累计值 > =月度必达   并且   <月度达标
            if (mustInput >= 0 && reach < 0) {
                manageKpiMonthAim.setScoreAuto(BigDecimal.valueOf(100));  //分数为100
                manageKpiMonthAim.setScoreAdjust(BigDecimal.valueOf(100));
                manageKpiMonthAim.setTargetLevelActual("达标");
            }
            //实绩累计值  >= 月度达标  并且  < 月度挑战
            if (reach >= 0 && challenge < 0) {
                manageKpiMonthAim.setScoreAuto(BigDecimal.valueOf(120));  //分数为120
                manageKpiMonthAim.setScoreAdjust(BigDecimal.valueOf(120));
                manageKpiMonthAim.setTargetLevelActual("必达");
            }
            //实绩累计值  >=月度挑战
            if (challenge >= 0) {
                manageKpiMonthAim.setScoreAuto(BigDecimal.valueOf(150));  //分数为150
                manageKpiMonthAim.setScoreAdjust(BigDecimal.valueOf(150));
                manageKpiMonthAim.setTargetLevelActual("挑战");
            }
            Integer id = monthScore.getId();
            QueryWrapper<ManageKpiMonthAim> manageKpiMonthAimQueryWrapper = new QueryWrapper<>();
            manageKpiMonthAimQueryWrapper.eq("id", id);
            //修改项目状态
            manageKpiMonthAim.setStatus("已锁定");
            //修改分数
            manageKpiMonthAimMapper.update(manageKpiMonthAim, manageKpiMonthAimQueryWrapper);
        }
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

    /**
     * 读取附件数据到数据库表
     *
     * @param attachment 附件
     * @return 数据列表
     */
    @Override
    @Transactional
    public Map<String, Object> readViewManageYearMonthScoreDataSource(Attachment attachment, HttpServletRequest request) {

        // 返回消息
        Map<String, Object> result = new HashMap<>();
        result.put("content", "");
        result.put("failed", false);
        // 读取附件
        sheetService.load(attachment.getPath(), attachment.getFilename()); // 根据路径和名称读取附件
        // 读取表单
        sheetService.readByName("激励约束项目分数表"); //根据表单名称获取该工作表单
        // 获取表单数据
        Sheet sheet = sheetService.getSheet();
        if (ObjectUtils.isEmpty(sheet)) {
            throw new ServiceException("表单【激励约束项目分数表】不存在，无法读取数据，请检查");
        }
        // 获取单价列表数据
        Row row;
        List<ViewManageYearMonthScoreService> viewManageYearMonthScoreServices = new ArrayList<>(); //实体类集合，用于后面的批量写入数据库
        // 2022-06-01 从决策系统导出数据，存在最后几行为空白数据，导致报数据越界问题，这里的长度由表头长度控制
        short maxSize = sheet.getRow(0).getLastCellNum();//列数(表头长度)
        //获取年份和公司名称
        Cell companyCell = sheet.getRow(1).getCell(SheetService.columnToIndex("B"));
        Cell yearCell = sheet.getRow(1).getCell(SheetService.columnToIndex("E"));
        Cell monthCell = sheet.getRow(1).getCell(SheetService.columnToIndex("G"));
        String companyCellValue = sheetService.getValue(companyCell);
        String yearCellValue = sheetService.getValue(yearCell);
        String monthCellValue = sheetService.getValue(monthCell);
//根据Excel表中公司名称与公司表中的公司名称对应找到公司id并进行验证
        QueryWrapper<Company> companyQueryWrapper = new QueryWrapper<>();
        companyQueryWrapper.eq("name", companyCellValue);
        List<Company> companyList = companyMapper.selectList(companyQueryWrapper);
        if (companyList.size() > 1) {
            throw new ServiceException("Excel表格中公司名称存在多个id，请检查公司名称！");
        }
        if (companyList.size() == 0) {
            throw new ServiceException("Excel表格中公司名称不存在，请检查公司名称！");
        }
        //公司表中存在数据，获取这个公司名称的id/name/shortName
        Company company = companyMapper.selectList(companyQueryWrapper).get(0);
        Integer companyIdCell = company.getId();
        String companyNameCell = company.getName();
        String shortNameCell = company.getShortName();
        String companyIdStr = request.getParameter("companyId");
        int companyId = Integer.parseInt(companyIdStr);
        String year = request.getParameter("year");
        List<String> strings = Arrays.asList(year.split("-"));
        String yearStr = strings.get(0);
        String monthStr = strings.get(1);
        int month=Integer.parseInt(monthStr);  //改变前端月份的类型
        int monthValue=Integer.parseInt(monthCellValue); //改变Excel中月份的类型
        //效验年份、公司名称、月份
        if (companyId != companyIdCell) {
            throw new ServiceException("导入的公司名称与excel中的公司名称不一致，导入失败");
        }
        if (!yearStr.equals(yearCellValue)) {
            throw new ServiceException("导入的年份与excel中的年份不一致，导入失败");
        }
        if (month != monthValue) {
            throw new ServiceException("导入的月份与excel中的名称不一致，导入失败");
        }
        // 循环总行数(不读表的标题，从第5行开始读)
        for (int j = 3; j <= sheet.getLastRowNum(); j++) {// getPhysicalNumberOfRows()此方法不会将空白行计入行数
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
            Cell cell = row.createCell(SheetService.columnToIndex("N"));
            String projectType = cells.get(SheetService.columnToIndex("B"));
            String projectDesc = cells.get(SheetService.columnToIndex("C"));
            String unit = cells.get(SheetService.columnToIndex("D"));
            String basicTarget = cells.get(SheetService.columnToIndex("E"));
            String mustInputTarget = cells.get(SheetService.columnToIndex("F"));
            String reachTarget = cells.get(SheetService.columnToIndex("G"));
            String challengeTarget = cells.get(SheetService.columnToIndex("H"));
            String monthTarget = cells.get(SheetService.columnToIndex("I"));
            String monthActualValue = cells.get(SheetService.columnToIndex("J"));
            String accumulateTarget = cells.get(SheetService.columnToIndex("K"));
            String accumulateActual = cells.get(SheetService.columnToIndex("L"));
            String scoreAdjust = cells.get(SheetService.columnToIndex("M"));

            //必填性检查
            if (ObjectUtils.isEmpty(projectDesc)) {
                setFailedContent(result, String.format("第%s行的项目名称存在多条", j + 1));
                cell.setCellValue("项目名称未填写");
                continue;
            }
            if (ObjectUtils.isEmpty(companyCellValue)) {
                setFailedContent(result, String.format("第%s行的公司名称存在多条", j + 1));
                cell.setCellValue("公司名称未填写");
                continue;
            }
            if (ObjectUtils.isEmpty(yearCellValue)) {
                setFailedContent(result, String.format("第%s行的年份存在多条", j + 1));
                cell.setCellValue("年份未填写");
                continue;
            }
            if (ObjectUtils.isEmpty(monthCellValue)) {
                setFailedContent(result, String.format("第%s行的月份存在多条", j + 1));
                cell.setCellValue("月份未填写");
                continue;
            }
            //查询出对应的经营管理年度指标，如果存在两条及两条以上的数据就抛出错误,月度id
            QueryWrapper<ManageKpiMonthAim> manageKpiMonthAimQueryWrapper = new QueryWrapper<>();
            manageKpiMonthAimQueryWrapper.eq("companyName", companyNameCell).eq("year", year)
                    .eq("month", month).eq("projectDesc", projectDesc);
            List<ManageKpiMonthAim> monthAimList = manageKpiMonthAimMapper.selectList(manageKpiMonthAimQueryWrapper);
            if (monthAimList.size() > 1) {
                setFailedContent(result, String.format("第%s行的月度指标存在多条", j + 1));
                cell.setCellValue("存在多个指标，检查数据是否正确");
                continue;
            }
            if (monthAimList.size() == 0) {
                setFailedContent(result, String.format("第%s行的月度指标不存在", j + 1));
                cell.setCellValue("月度指标不存在，检查数据是否正确");
                continue;
            }
            //构建实体类
            ManageKpiMonthAim manageKpiMonthAim = new ManageKpiMonthAim();
            if (monthAimList.size() == 1) {
                Integer monthId = monthAimList.get(0).getId();
                manageKpiMonthAim.setId(monthId);  //月度id
            }

            //导入时固定状态
            String status = "已锁定";
            //前端选择并写入
            manageKpiMonthAim.setYear(Integer.valueOf(yearStr));
            manageKpiMonthAim.setCompanyName(companyNameCell);
            manageKpiMonthAim.setMonth(Integer.valueOf(month));
            manageKpiMonthAim.setStatus(status);//固定状态
            //excel导入
            manageKpiMonthAim.setProjectType(projectType);
            manageKpiMonthAim.setProjectDesc(projectDesc);
            manageKpiMonthAim.setUnit(unit);
            manageKpiMonthAim.setBasicTarget(basicTarget);
            manageKpiMonthAim.setMustInputTarget(mustInputTarget);
            manageKpiMonthAim.setReachTarget(reachTarget);
            manageKpiMonthAim.setChallengeTarget(challengeTarget);
            if (!ObjectUtils.isEmpty(monthTarget)) {
                manageKpiMonthAim.setMonthTarget(new BigDecimal(monthTarget));
            }
            if (!ObjectUtils.isEmpty(accumulateTarget)) {
                manageKpiMonthAim.setAccumulateTarget(new BigDecimal(accumulateTarget));
            }
            if (!ObjectUtils.isEmpty(monthActualValue)) {
                manageKpiMonthAim.setAccumulateActual(new BigDecimal(monthActualValue));
            }
            if (!ObjectUtils.isEmpty(accumulateActual)) {
                manageKpiMonthAim.setAccumulateActual(new BigDecimal(accumulateActual));
            }
            if (!ObjectUtils.isEmpty(scoreAdjust)) {
                manageKpiMonthAim.setScoreAdjust(new BigDecimal(scoreAdjust));
            }
            // 根据id进行判断，存在则更新，不存在则新增
            manageKpiMonthAimServiceImpl.saveOrUpdate(manageKpiMonthAim);
            cell.setCellValue("导入成功");
        }

        sheetService.write(attachment.getPath(), attachment.getFilename());// 写入excel表
        return result;
    }

    @Override
    public boolean updateViewManageYearMonthScoreData(ManageKpiMonthAim manageKpiMonthAim) {

        if (!manageKpiMonthAim.getStatus().equals("已锁定")) {
            throw new ServiceException("分数未生成，请生成后再进行修改分数");
        }
        if (!manageKpiMonthAim.getProjectDesc().equals("")) {

        }
        int result = manageKpiMonthAimMapper.updateById(manageKpiMonthAim);
        if (result > 0) {
            return true;
        }
        return false;
    }

    private QueryWrapper<ViewManageYearMonthScore> getQueryWrapper(Map<String, Object> params) {
        QueryWrapper<ViewManageYearMonthScore> queryWrapper = new QueryWrapper<>();

        if (params.containsKey("id")) {
            queryWrapper.eq("id", params.get("id"));
        }
        if (params.containsKey("select")) {
            queryWrapper.select((String) params.get("select"));
        }
        if (params.containsKey("projectType")) {
            queryWrapper.like("projectType", params.get("projectType"));
        }
        if (params.containsKey("companyName")) {
            queryWrapper.like("companyName", params.get("companyName"));
        }
        if (params.containsKey("year")) {
            queryWrapper.eq("year", params.get("year"));
        }
        if (params.containsKey("scoreAdjustCause")) {
            queryWrapper.eq("scoreAdjustCause", params.get("scoreAdjustCause"));
        }
        if (params.containsKey("month")) {
            queryWrapper.eq("month", params.get("month"));
        }
        if (params.containsKey("projectDesc")) {
            queryWrapper.like("projectDesc", params.get("projectDesc"));
        }
        //判断指标状态为未锁定且累计实绩值不为空的数据？
        if (params.containsKey("status")) {
            queryWrapper.eq("status", params.get("status"));
        }
        if (params.containsKey("evaluateMode")) {
            queryWrapper.eq("evaluateMode", params.get("evaluateMode"));
        }
        //月度实绩生成标识
        if (params.containsKey("monthActualValueMark")) {
            queryWrapper.eq("monthActualValueMark", params.get("monthActualValueMark"));
        }
        //拆分前端的年月份的字符串，进行年月的查询
        String yearMonth = (String) params.get("yearMonth");
        if (!ObjectUtils.isEmpty(yearMonth)) {
            if (params.containsKey("yearMonth")) {
                List<String> strings = Arrays.asList(yearMonth.split("-"));
                queryWrapper.eq("month", strings.get(1)).eq("year", strings.get(0));
            }
        }
        //对前端传过来的公司主键进行非空判断，再进行字符串拆分使用SQL in进行查询
        if (params.containsKey("companyIds")) {
            String companyIds = (String) params.get("companyIds");
            List<String> strings = Arrays.asList(companyIds.split(","));
            queryWrapper.in("companyId", strings);
        }

        return queryWrapper;
    }
}
