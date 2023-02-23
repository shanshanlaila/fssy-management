package com.fssy.shareholder.management.service.system.impl.performance.manage_kpi;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.mapper.manage.user.UserMapper;
import com.fssy.shareholder.management.pojo.manage.user.User;
import com.fssy.shareholder.management.pojo.system.config.Attachment;
import com.fssy.shareholder.management.pojo.system.performance.manage_kpi.ManagerKpiScoreOld;
import com.fssy.shareholder.management.mapper.system.performance.manage_kpi.ManagerKpiScoreMapperOld;
import com.fssy.shareholder.management.service.common.SheetService;
import com.fssy.shareholder.management.service.system.performance.manage_kpi.ManagerKpiScoreServiceOld;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fssy.shareholder.management.tools.common.InstandTool;
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
import java.io.Serializable;
import java.util.*;

/**
 * <p>
 * 经理人绩效分数表		【修改内容】增加了年度关联ID字段	【修改时间】2022-10-27	【修改人】张泽鹏 服务实现类
 * </p>
 *
 * @author zzp
 * @since 2022-10-31
 */
@Service
public class ManagerKpiScoreServiceImplOld extends ServiceImpl<ManagerKpiScoreMapperOld, ManagerKpiScoreOld> implements ManagerKpiScoreServiceOld {

    @Autowired
    private SheetService sheetService;

    @Autowired
    private ManagerKpiScoreMapperOld managerKpiScoreMapper;
    /**
     * 用户信息
     */
    @Autowired
    private UserMapper userMapper;

    /**
     * 通过查询条件 分页 查询列表(绩效分数）
     *
     * @param params 查询条件
     * @return 分页数据
     */
    @Override
    public Page<ManagerKpiScoreOld> findManagerKpiScoreOldDataListPerPageByParams(Map<String, Object> params) {
        QueryWrapper<ManagerKpiScoreOld> queryWrapper = getQueryWrapper(params);
        queryWrapper.orderByDesc("year").orderByDesc("month");
        int limit = (int) params.get("limit");
        int page = (int) params.get("page");
        Page<ManagerKpiScoreOld> myPage = new Page<>(page, limit);
        return managerKpiScoreMapper.selectPage(myPage, queryWrapper);
    }

    /**
     * 通过查询条件 分页 查询列表（年度推移）
     *
     * @param params 查询条件
     * @return 分页数据
     */
    @Override
    public Page<Map<String, Object>> findViewManagerKpiMonthDataListPerPageByParams(Map<String, Object> params) {
        QueryWrapper<ManagerKpiScoreOld> queryWrapper = getQueryWrapper(params);
        String yearStr = (String) params.get("year");
        Integer year = InstandTool.stringToInteger(yearStr);
        if (ObjectUtils.isEmpty(year) || year == 0) {
            Calendar instance = Calendar.getInstance();
            year = instance.get(Calendar.YEAR);
        }
        StringBuilder stringBuilder = new StringBuilder("managerName,companyName,position,year," +
                "(SELECT scoreAdjust FROM bs_manager_kpi_score AS c WHERE c.YEAR = bs_manager_kpi_score.`year`-3 AND MONTH = 12 AND c.companyName = bs_manager_kpi_score.companyName AND c.managerName = bs_manager_kpi_score.managerName ) AS '" + (year - 3) + "'," +
                "(SELECT scoreAdjust FROM bs_manager_kpi_score AS b WHERE b.YEAR = bs_manager_kpi_score.`year`-2 AND MONTH = 12 AND b.companyName = bs_manager_kpi_score.companyName AND b.managerName = bs_manager_kpi_score.managerName ) AS '" + (year - 2) + "'," +
                "(SELECT scoreAdjust FROM bs_manager_kpi_score AS a WHERE a.YEAR = bs_manager_kpi_score.`year`-1 AND MONTH = 12 AND a.companyName = bs_manager_kpi_score.companyName AND a.managerName = bs_manager_kpi_score.managerName ) AS '" + (year - 1) + "'," +
                "(SELECT scoreAdjust FROM bs_manager_kpi_score AS d WHERE d.YEAR = bs_manager_kpi_score.`year` AND MONTH = 12 AND d.companyName = bs_manager_kpi_score.companyName AND d.managerName = bs_manager_kpi_score.managerName ) AS '" + year + "'," +
                "(SELECT scoreAdjust FROM bs_manager_kpi_score AS e WHERE e.YEAR = bs_manager_kpi_score.`year`+1 AND MONTH = 12 AND e.companyName = bs_manager_kpi_score.companyName AND e.managerName = bs_manager_kpi_score.managerName ) AS '" + (year + 1) + "'," +
                "(SELECT scoreAdjust FROM bs_manager_kpi_score AS f WHERE f.YEAR = bs_manager_kpi_score.`year`+2 AND MONTH = 12 AND f.companyName = bs_manager_kpi_score.companyName AND f.managerName = bs_manager_kpi_score.managerName ) AS '" + (year + 2) + "'," +
                "(SELECT scoreAdjust FROM bs_manager_kpi_score AS g WHERE g.YEAR = bs_manager_kpi_score.`year`+3 AND MONTH = 12 AND g.companyName = bs_manager_kpi_score.companyName AND g.managerName = bs_manager_kpi_score.managerName ) AS '" + (year + 3) + "'");
        queryWrapper.select(stringBuilder.toString()).groupBy("managerName,companyName,position,year");
        int limit = (int) params.get("limit");
        int page = (int) params.get("page");
        Page<Map<String, Object>> myPage = new Page<>(page, limit);
        return managerKpiScoreMapper.selectMapsPage(myPage, queryWrapper);
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
     * 导入附件
     *
     * @param attachment 附件
     * @return
     */
    @Override
    @Transactional
    public Map<String, Object> readManagerKpiScoreOldDataSource(Attachment attachment, HttpServletRequest request) {
        // 返回消息
        Map<String, Object> result = new HashMap<>();
        result.put("content", "");
        result.put("failed", false);
        // 读取附件
        sheetService.load(attachment.getPath(), attachment.getFilename()); // 根据路径和名称读取附件
        // 读取表单
        sheetService.readByName("经理人KPI分数表"); //根据表单名称获取该工作表单
        // 获取表单数据
        Sheet sheet = sheetService.getSheet();
        if (ObjectUtils.isEmpty(sheet)) {
            throw new ServiceException("表单【经理人KPI分数表】不存在，无法读取数据，请检查");
        }

        // 获取单价列表数据
        Row row;
        List<ManagerKpiScoreOld> managerKpiScoreOlds = new ArrayList<>(); //实体类集合，用于后面的批量写入数据库
        // 2022-06-01 从决策系统导出数据，存在最后几行为空白数据，导致报数据越界问题，这里的长度由表头长度控制
        short maxSize = sheet.getRow(0).getLastCellNum();//列数(表头长度)
        //获取年份
        Cell yearCell = sheet.getRow(1).getCell(SheetService.columnToIndex("A"));
        String yearValue = sheetService.getValue(yearCell);
        String yearRequest = request.getParameter("year");
        //验证年份
        if (!yearValue.equals(yearRequest)) {
            throw new ServiceException("导入的年份与Excel中的年份不一致，导入失败！");
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
            Cell cell = row.createCell(SheetService.columnToIndex("T"));// 报错信息上传到excel R列（暂未实现）
            String year = cells.get(SheetService.columnToIndex("A"));
            String managerName = cells.get(SheetService.columnToIndex("B"));
            String companyName = cells.get(SheetService.columnToIndex("C"));
            String month = cells.get(SheetService.columnToIndex("F"));
            String advantageAnalyze = cells.get(SheetService.columnToIndex("M"));
            String disadvantageAnalyze = cells.get(SheetService.columnToIndex("N"));
            String riskDesc = cells.get(SheetService.columnToIndex("O"));
            String respDepartment = cells.get(SheetService.columnToIndex("P"));
            String groupImproveAction = cells.get(SheetService.columnToIndex("Q"));
            String anomalyType = cells.get(SheetService.columnToIndex("R"));
            // 检查必填项
            if (ObjectUtils.isEmpty(companyName)) {
                setFailedContent(result, String.format("第%s行的经理人姓名是空的", j + 1));
                cell.setCellValue("经理人姓名是空的");
                throw new ServiceException("表中有空值");
            }
            // 根据经理人姓名，年份，月份,公司名称进行判断，存在则更新
            UpdateWrapper<ManagerKpiScoreOld> managerKpiScoreOldUpdateWrapper = new UpdateWrapper<>();
            managerKpiScoreOldUpdateWrapper.set("advantageAnalyze", advantageAnalyze)
                    .set("disadvantageAnalyze", disadvantageAnalyze).set("riskDesc", riskDesc)
                    .set("respDepartment", respDepartment).set("groupImproveAction", groupImproveAction)
                    .set("anomalyType", anomalyType)
                    .eq("managerName", managerName).eq("year", year)
                    .eq("month", month).eq("companyName", companyName);
            managerKpiScoreMapper.update(null, managerKpiScoreOldUpdateWrapper);
            cell.setCellValue("导入成功");

        }
        sheetService.write(attachment.getPath(), attachment.getFilename());// 写入excel表

        return result;
    }
    /**
     * 通过查询条件查询履职计划map数据，用于导出
     *
     * @param params 查询条件
     * @return 数据列表
     */
    @Override
    public List<Map<String, Object>> findManagerKpiScoreOldDataByParams(Map<String, Object> params) {
        QueryWrapper<ManagerKpiScoreOld> queryWrapper = getQueryWrapper(params);
        return managerKpiScoreMapper.selectMaps(queryWrapper);
    }

    /**
     * 删除分数记录
     */
    @Override
    public boolean deleteManagerKpiScoreOldDataById(Integer id) {
        int result = managerKpiScoreMapper.deleteById(id);
        if (result > 0) {
            return true;
        }
        return false;
    }

    /**
     * 修改分数信息
     */
    @Override
    public boolean updateManagerKpiScoreOldData(ManagerKpiScoreOld managerKpiScoreOld) {
        int result = managerKpiScoreMapper.updateById(managerKpiScoreOld);
        if (result > 0) {
            return true;
        }
        return false;
    }

    /**
     * 异常提醒
     *
     * @return
     */
    @Override
    public Map<Long, Map<String, Object>> findManagerKpiScoreParams() {
        Map<Long,Map<String, Object>> map = new HashMap<>(30);
        Map<String, Object> anomalyMap;
        QueryWrapper<ManagerKpiScoreOld> queryWrapper = new QueryWrapper<>();
        //筛选异常存在的数据
        queryWrapper.ne("anomalyType","").isNotNull("anomalyType");
        List<Map<String,Object>> kpiScore = managerKpiScoreMapper.selectMaps(queryWrapper);
        for (Map<String,Object> anomaly:kpiScore) {
                //出现异常则去用户表找到用户id和企业微信号
                User user = userMapper.selectById((Serializable) anomaly.get("managerId"));
                anomalyMap = new HashMap<>(50);
                anomalyMap.put("userId",user.getId());
                anomalyMap.put("userName",user.getName());
                anomalyMap.put("weChat",user.getQyweixinUserId());
                anomalyMap.put("anomalyType",anomaly.get("anomalyType"));
                anomalyMap.put("year",anomaly.get("year"));
                anomalyMap.put("month",anomaly.get("month"));
                //写入map中以用户id为准，将他剩下的其他数据填充
                map.put((Long) anomalyMap.get("userId"),anomalyMap);
        }
        return map;
    }

    /**
     * 传入参数，对参数进行处理(查询条件)
     *
     * @param params
     * @return
     */
    private QueryWrapper<ManagerKpiScoreOld> getQueryWrapper(Map<String, Object> params) {
        QueryWrapper<ManagerKpiScoreOld> queryWrapper = new QueryWrapper<>();
        if (params.containsKey("select")) {
            queryWrapper.select((String) params.get("select"));
        }
        if (params.containsKey("managerName")) {
            queryWrapper.eq("managerName", params.get("managerName"));
        }
        if (params.containsKey("companyName")) {
            queryWrapper.like("companyName", params.get("companyName"));
        }
        if (params.containsKey("companyNameShort")) {
            queryWrapper.like("companyNameShort", params.get("companyNameShort"));
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
