/**
 * ------------------------修改日志---------------------------------
 * 修改人       修改日期         修改内容
 */
package com.fssy.shareholder.management.service.system.impl.performance.manage_kpi;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fssy.shareholder.management.mapper.system.performance.manage_kpi.ManageKpiMonthAimMapper;
import com.fssy.shareholder.management.pojo.system.performance.manage_kpi.ManageKpiMonthAim;
import com.fssy.shareholder.management.service.system.performance.manage_kpi.ManageKpiMonthScoreService;
import com.fssy.shareholder.management.tools.common.InstandTool;
import com.fssy.shareholder.management.tools.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Shizn
 * @ClassName: ManageKpiMonthScoreServiceImpl
 * @Description: 经营管理月度项目分数管理 实现类
 * @date 2022/10/27 0027 17:04
 */
@Service
public class ManageKpiMonthScoreServiceImpl extends ServiceImpl<ManageKpiMonthAimMapper, ManageKpiMonthAim> implements ManageKpiMonthScoreService {
    @Autowired
    private ManageKpiMonthAimMapper manageKpiMonthAimMapper;

    /**
     * 通过查询条件 分页查询列表
     *
     * @param params 查询条件
     * @return 分页数据
     */
    @Override
    public Page<ManageKpiMonthAim> findManageKpiMonthScoreDataListPerPageByParams(Map<String, Object> params) {
        QueryWrapper<ManageKpiMonthAim> queryWrapper = getQueryWrapper(params);
        int limit = (int) params.get("limit");
        int page = (int) params.get("page");
        Page<ManageKpiMonthAim> myPage = new Page<>(page, limit);
        return manageKpiMonthAimMapper.selectPage(myPage, queryWrapper);

    }

    /**
     * 通过查询条件查询履职计划map数据， 用于导出
     *
     * @param params 查询条件
     * @return 数据列表
     */
    @Override
    public List<Map<String, Object>> findManageKpiMonthScoreMapDataByParams(Map<String, Object> params) {
        QueryWrapper<ManageKpiMonthAim> queryWrapper = getQueryWrapper(params);
        return manageKpiMonthAimMapper.selectMaps(queryWrapper);

    }

    /**
     * 根据条件查询所有数据
     *
     * @param params
     * @return
     */
    @Override
    public List<ManageKpiMonthAim> findManageKpiMonthScoreDataByParams(Map<String, Object> params) {
        QueryWrapper<ManageKpiMonthAim> queryWrapper = getQueryWrapper(params);
        return manageKpiMonthAimMapper.selectList(queryWrapper);
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
        QueryWrapper<ManageKpiMonthAim> queryWrapper = getQueryWrapper(params);
        //条件查询出所有数据，进行未锁定和绩效指标进行筛选
        List<ManageKpiMonthAim> manageKpiMonthAims = manageKpiMonthAimMapper.selectList(queryWrapper);
        List<ManageKpiMonthAim> filterList = manageKpiMonthAims.stream()
                .filter(i -> i.getStatus().equals("未锁定") && "经理人指标".equals(i.getManagerKpiMark())).collect(Collectors.toList());
        if (ObjectUtils.isEmpty(filterList)) {
            throw new ServiceException("没有查出数据或已生成！生成失败！");
        }
        //遍历
        for (ManageKpiMonthAim monthAim : filterList) {
            //获取数据
            BigDecimal month = new BigDecimal(monthAim.getMonth());
            BigDecimal accumulateActual = monthAim.getAccumulateActual();
            BigDecimal basicTarget = new BigDecimal(monthAim.getBasicTarget());
            BigDecimal mustInputTarget = new BigDecimal(monthAim.getMustInputTarget());
            BigDecimal reachTarget = new BigDecimal(monthAim.getReachTarget());
            BigDecimal challengeTarget = new BigDecimal(monthAim.getChallengeTarget());
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
                monthAim.setScoreAuto(BigDecimal.valueOf(0));  //分数为0
                monthAim.setScoreAdjust(BigDecimal.valueOf(0));  //分数为0
            }
            //实绩累计值 > =月度基本  并且   < 月度必达
            if (basic >= 0 && mustInput < 0) {
                monthAim.setScoreAuto(BigDecimal.valueOf(80));  //分数为80
                monthAim.setScoreAdjust(BigDecimal.valueOf(80));
            }
            //实绩累计值 > =月度必达   并且   <月度达标
            if (mustInput >= 0 && reach < 0) {
                monthAim.setScoreAuto(BigDecimal.valueOf(100));  //分数为100
                monthAim.setScoreAdjust(BigDecimal.valueOf(100));
            }
            //实绩累计值  >= 月度达标  并且  < 月度挑战
            if (reach >= 0 && challenge < 0) {
                monthAim.setScoreAuto(BigDecimal.valueOf(120));  //分数为120
                monthAim.setScoreAdjust(BigDecimal.valueOf(120));
            }
            //实绩累计值  >=月度挑战
            if (challenge >= 0) {
                monthAim.setScoreAuto(BigDecimal.valueOf(150));  //分数为150
                monthAim.setScoreAdjust(BigDecimal.valueOf(150));
            }
            //修改分数
            manageKpiMonthAimMapper.updateById(monthAim);
            //修改项目状态
            monthAim.setStatus("已锁定");
            manageKpiMonthAimMapper.updateById(monthAim);
        }
        return true;
    }

    private QueryWrapper<ManageKpiMonthAim> getQueryWrapper(Map<String, Object> params) {
        QueryWrapper<ManageKpiMonthAim> queryWrapper = new QueryWrapper<>();

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
        if (params.containsKey("month")) {
            queryWrapper.eq("month", params.get("month"));
        }
        if (params.containsKey("projectDesc")) {
            queryWrapper.like("projectDesc", params.get("projectDesc"));
        }
        if (params.containsKey("status")) {
            queryWrapper.eq("status", params.get("status"));
        }
        //进行非空判断
        if (params.containsKey("monthActualValue") && Boolean.valueOf(InstandTool.objectToString(params.get("monthActualValue")))) {
            queryWrapper.isNotNull("monthActualValue");
        }
        //拆分前端的年月份的字符串，进行年月的查询
        String yearMonth = (String) params.get("yearMonth");
        if (!ObjectUtils.isEmpty(yearMonth)) {
            if (params.containsKey("yearMonth")) {
                List<String> strings = Arrays.asList(yearMonth.split("-"));
                queryWrapper.eq("month", strings.get(1)).eq("year", strings.get(0));
            }
        }
        return queryWrapper;
    }
}
