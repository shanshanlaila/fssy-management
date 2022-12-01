package com.fssy.shareholder.management.service.system.impl.performance.manage_kpi;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fssy.shareholder.management.mapper.system.config.AttachmentMapper;
import com.fssy.shareholder.management.mapper.system.hr.performance.manage_kpi.ManageKpiLibMapper;
import com.fssy.shareholder.management.pojo.system.config.Attachment;
import com.fssy.shareholder.management.pojo.system.hr.performance.manage_kpi.ManageKpiLib;
import com.fssy.shareholder.management.service.common.SheetService;
import com.fssy.shareholder.management.service.system.hr.performance.manage_kpi.ManageKpiLibService;
import com.fssy.shareholder.management.tools.exception.ServiceException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.*;

/**
 * <p>
 * 经营管理指标库 服务实现类
 * </p>
 *
 * @author Shizn
 * @since 2022-10-12
 */
@Service
public class ManageKpiLibServiceImpl extends ServiceImpl<ManageKpiLibMapper, ManageKpiLib> implements ManageKpiLibService {
    @Autowired
    private SheetService sheetService;
    @Autowired
    private ManageKpiLibMapper manageKpiLibMapper;
    @Autowired
    private AttachmentMapper attachmentMapper;


    /**
     * 修改经营管理指标库信息
     * @param manageKpiLib 经营管理指标库 实体
     * @return
     */
    @Override
    public boolean updateManageKpiLib(ManageKpiLib manageKpiLib) {
        int result = manageKpiLibMapper.updateById(manageKpiLib);
        if (result > 0) {
            return true;
        }
        return false;
    }
    //展示经营管理指标库信息
    @Override
    public List<ManageKpiLib> findStudentsDataByParams(Map<String, Object> params) {
        QueryWrapper<ManageKpiLib> queryWrapper = getQueryWrapper(params);
        List<ManageKpiLib> manageKpiLibList = manageKpiLibMapper.selectList(queryWrapper);
        return manageKpiLibList;
    }

    /**
     * 分页查询
     * @param params 查询条件
     * @return
     */
    @Override
    public Page<ManageKpiLib> findManagerKpiLibDataListPerPageByParams(Map<String, Object> params) {
        QueryWrapper<ManageKpiLib> queryWrapper = getQueryWrapper(params);
        int limit = (int) params.get("limit");
        int page = (int) params.get("page");
        Page<ManageKpiLib> myPage = new Page<>(page, limit);
        Page<ManageKpiLib> managerKpiLibPage = manageKpiLibMapper.selectPage(myPage, queryWrapper);
        return managerKpiLibPage;
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
     * @param attachment 经理绩效附件
     * @return
     */
    @Override
    @Transactional
    public Map<String, Object> readManagerKpiLibDataSource(Attachment attachment) {
        // 返回消息
        Map<String, Object> result = new HashMap<>();
        result.put("content", "");
        result.put("failed", false);
        // 读取附件
        sheetService.load(attachment.getPath(), attachment.getFilename()); // 根据路径和名称读取附件
        // 读取表单
        sheetService.readByName("经营管理指标库"); //根据表单名称获取该工作表单
        // 获取表单数据
        Sheet sheet = sheetService.getSheet();
        if (ObjectUtils.isEmpty(sheet)) {
            throw new ServiceException("表单【经营管理指标库】不存在，无法读取数据，请检查");
        }

        // 获取单价列表数据
        Row row;
        List<ManageKpiLib> manageKpiLibList = new ArrayList<>(); //实体类集合，用于后面的批量写入数据库
        // 2022-06-01 从决策系统导出数据，存在最后几行为空白数据，导致报数据越界问题，这里的长度由表头长度控制
        short maxSize = sheet.getRow(0).getLastCellNum();//列数(表头长度)
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
            Cell cell = row.createCell(SheetService.columnToIndex("I"));// 报错信息上传到excel D列（暂未实现）
            String projectType = cells.get(SheetService.columnToIndex("B"));
            String projectDesc = cells.get(SheetService.columnToIndex("C"));
            String unit = cells.get(SheetService.columnToIndex("D"));
            String kpiDefinition = cells.get(SheetService.columnToIndex("E"));
            String kpiFormula = cells.get(SheetService.columnToIndex("F"));
            String kpiYear = cells.get(SheetService.columnToIndex("G"));
            String note = cells.get(SheetService.columnToIndex("H"));
            // 检查必填项
            if (ObjectUtils.isEmpty(projectDesc)) {
                setFailedContent(result, String.format("第%s行的项目名称是空的", j + 1));
                cell.setCellValue("项目名称是空的");
                throw new ServiceException("表中有空值");
            }
            //构建实体类
            ManageKpiLib manageKpiLib = new ManageKpiLib();
            //通过指标名称确定唯一的id，存在则更新，不存在则自动递增
            QueryWrapper<ManageKpiLib> libQueryWrapper = new QueryWrapper<>();
            libQueryWrapper.eq("projectDesc",projectDesc);
            List<ManageKpiLib> kpiLibList = manageKpiLibMapper.selectList(libQueryWrapper);
            if (kpiLibList.size()>1){
                setFailedContent(result, String.format("第%s行的", j + 1));
                cell.setCellValue("项目名称是空的");
            }
            //防止相同数据id自增
            if (kpiLibList.size()==1){
                manageKpiLib.setId(kpiLibList.get(0).getId());
            }
            manageKpiLib.setProjectType(projectType);
            manageKpiLib.setProjectDesc(projectDesc);
            manageKpiLib.setUnit(unit);
            manageKpiLib.setKpiDefinition(kpiDefinition);
            manageKpiLib.setKpiFormula(kpiFormula);
            manageKpiLib.setKpiYear(Integer.valueOf(kpiYear));
            manageKpiLib.setNote(note);

            // 根据id进行判断，存在则更新，不存在则新增
            saveOrUpdate(manageKpiLib);
            cell.setCellValue("导入成功");

        }

        sheetService.write(attachment.getPath(), attachment.getFilename());// 写入excel表
        return result;
    }

    @Override
    public List<Map<String, Object>> findManagerKpiLibDataSource(Map<String, Object> params) {
        QueryWrapper<ManageKpiLib> queryWrapper = getQueryWrapper(params);
        return manageKpiLibMapper.selectMaps(queryWrapper);
    }

    private QueryWrapper<ManageKpiLib> getQueryWrapper(Map<String, Object> params) {
        QueryWrapper<ManageKpiLib> queryWrapper = new QueryWrapper<>();
        // 经理人年度KPI指标库表主键查询
        if (params.containsKey("id")) {
            queryWrapper.eq("id", params.get("id"));
        }
        if (params.containsKey("select")) {
            queryWrapper.select((String) params.get("select"));
        }
        // 指标类别
        if (params.containsKey("projectType")) {
            queryWrapper.like("projectType", params.get("projectType"));
        }
        // 指标名称
        if (params.containsKey("projectDesc")) {
            queryWrapper.like("projectDesc", params.get("projectDesc"));
        }
        // 创建年份
        if (params.containsKey("kpiYear")) {
            queryWrapper.eq("kpiYear", params.get("kpiYear"));
        }
        //评分模式
        if (params.containsKey("evaluateMode")) {
            queryWrapper.eq("evaluateMode", params.get("evaluateMode"));
        }
        return queryWrapper;
    }
}
