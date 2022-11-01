package com.fssy.shareholder.management.service.system.impl.performance.manage_kpi;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.mapper.system.performance.manage_kpi.ManagerKpiScoreMapperOld;
import com.fssy.shareholder.management.pojo.system.performance.manage_kpi.ManagerKpiScoreOld;
import com.fssy.shareholder.management.pojo.system.performance.manage_kpi.ViewManagerKpiMonth;
import com.fssy.shareholder.management.mapper.system.performance.manage_kpi.ViewManagerKpiMonthMapper;
import com.fssy.shareholder.management.service.system.performance.manage_kpi.ViewManagerKpiMonthService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fssy.shareholder.management.tools.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 经理人KPI分数管理实现类
 * VIEW 服务实现类
 * </p>
 *
 * @author zzp
 * @since 2022-10-31
 */
@Service
public class ViewManagerKpiMonthServiceImpl extends ServiceImpl<ViewManagerKpiMonthMapper, ViewManagerKpiMonth> implements ViewManagerKpiMonthService {

    @Autowired
    private ViewManagerKpiMonthMapper viewManagerKpiMonthMapper;

    @Autowired
    private ManagerKpiScoreMapperOld managerKpiScoreMapper;

    /**
     * 通过查询条件 分页查询列表
     *
     * @param params 查询条件
     * @return 分页数据
     */
    @Override
    public Page<ViewManagerKpiMonth> findViewManagerKpiMonthDataListPerPageByParams(Map<String, Object> params) {
        QueryWrapper<ViewManagerKpiMonth> queryWrapper = getQueryWrapper(params);
        int limit = (int)params.get("limit");
        int page = (int)params.get("page");
        Page<ViewManagerKpiMonth> myPage = new Page<>(page,limit);
        return viewManagerKpiMonthMapper.selectPage(myPage, queryWrapper);
    }

    /**
     * 通过查询条件查询履职计划map数据， 用于导出
     *
     * @param params 查询条件
     * @return 数据列表
     */
    @Override
    public List<Map<String, Object>> findViewManagerKpiMonthMapDataByParams(Map<String, Object> params) {
        QueryWrapper<ViewManagerKpiMonth> queryWrapper = getQueryWrapper(params);
        return viewManagerKpiMonthMapper.selectMaps(queryWrapper);
    }

    /**
     * 根据条件查询所有数据
     *
     * @param params
     * @return
     */
    @Override
    public List<ViewManagerKpiMonth> findViewManagerKpiMonthDataByParams(Map<String, Object> params) {
        QueryWrapper<ViewManagerKpiMonth> queryWrapper = getQueryWrapper(params);
        return viewManagerKpiMonthMapper.selectList(queryWrapper);
    }

    /**
     * 拿到计算分数所需的字段，进行计算分数
     *
     * @param
     * @return
     */
    @Override
    @Transactional
    public boolean createScore(Map<String, Object> params) {
        QueryWrapper<ViewManagerKpiMonth> queryWrapper = getQueryWrapper(params);
        //条件查询出所有数据，进行未锁定进行筛选
        List<ViewManagerKpiMonth> ViewManagerKpiMonths = viewManagerKpiMonthMapper.selectList(queryWrapper);
        List<ViewManagerKpiMonth> filterList = ViewManagerKpiMonths.stream()
                .filter(i -> i.getStatus().equals("已分析")).collect(Collectors.toList());
        if (ObjectUtils.isEmpty(filterList)) {
            throw new ServiceException("没有查出数据或已生成！生成失败！");
        }
        //对要计算的变量进行初始化
        BigDecimal businessScore = new BigDecimal(0);
        BigDecimal incentiveScore = new BigDecimal(0);
        BigDecimal scoreSys = new BigDecimal(0);
        int i = filterList.size();
        //遍历获取到符合条件的所有记录
//        for (int i = 0; i < filterList.size(); i++) {
        for (ViewManagerKpiMonth temp : filterList) {
//            System.out.println("外循环一次");
            //首先看符合条件的记录有几条，那就循环几次计算出所需要算的分数
//            for (int i = 0; i < filterList.size(); i++) {
//                System.out.println("内循环第" + i + "次");
//            for (ViewManagerKpiMonth temp : filterList) {
            //对执行的每一条
            //①获取相应条件的记录中的权重和人工调整分数
            BigDecimal proportion =  new BigDecimal(String.valueOf(temp.getProportion()));
            BigDecimal monthScoreAdjust = new BigDecimal(String.valueOf(temp.getMonthScoreAdjust()));
            //②判断这条记录是哪种类型
            String projectType = temp.getProjectType();
            if(projectType.equals("1")){
                //③计算这条记录的经营绩效分数存入临时变量businessScoreTemp
                //定义百分号
                BigDecimal per = new BigDecimal(0.01);
                BigDecimal businessScoreTemp = proportion.multiply(monthScoreAdjust).multiply(per);
                //并且将词条记录的经营绩效分数存入最终经营绩效分数,如果下调记录类型还是1，就可以进行分数求和
                businessScore = businessScore.add(businessScoreTemp);
//                System.out.println("----------------" + 1 + "------------------");
//                System.out.println("businessScore = " + businessScore);
//                System.out.println("----------------------------------");
            } else if(projectType.equals("2")){
                //④计算这条记录的激励约束分数存入临时变量incentiveScoreTemp
                //将人工生成分数直接赋值给临时变量
                BigDecimal incentiveScoreTemp = monthScoreAdjust;
                //并且将词条记录的激励约束分数存入最终激励约束分数,如果下调记录类型还是2，就可以进行分数求和
                incentiveScore = incentiveScore.add(incentiveScoreTemp);
//                System.out.println("----------------" + 2 + "------------------");
//                System.out.println("monthScoreAdjust = " + monthScoreAdjust);
//                System.out.println("incentiveScore = " + incentiveScore);
//                System.out.println("----------------------------------");
            }
            i--;
//            System.out.println(i);
            //⑤计算系统生成分数和初始化人工调整分数
            scoreSys = businessScore.add(incentiveScore);
            //⑥向数据库插入数据
            if(i == 0){
                //对score表插入数据
                ManagerKpiScoreOld managerKpiScore = new ManagerKpiScoreOld();
                managerKpiScore.setManagerName(temp.getManagerName());
                managerKpiScore.setCompanyName(temp.getCompanyName());
                managerKpiScore.setYear(temp.getYear());
                managerKpiScore.setPosition(temp.getPosition());
                managerKpiScore.setGeneralManager(temp.getGeneralManager());
                managerKpiScore.setMonth(temp.getMonth());
                managerKpiScore.setBusinessScore(businessScore);
                managerKpiScore.setIncentiveScore(incentiveScore);
                managerKpiScore.setScoreSys(scoreSys);
                managerKpiScore.setScoreAdjust(scoreSys);
                managerKpiScore.setGeneralManagerScore(scoreSys);
//                System.out.println("************************************");
//                System.out.println("businessScore = " + businessScore);
//                System.out.println("incentiveScore = " + incentiveScore);
//                System.out.println("scoreSys = " + scoreSys);
//                System.out.println("************************************");
                //插入数据
                managerKpiScoreMapper.insert(managerKpiScore);
            }
//            }
        }
        return true;
    }

    /**
     * 查询条件
     * @param params
     * @return
     */
    private QueryWrapper<ViewManagerKpiMonth> getQueryWrapper(Map<String, Object> params) {
        QueryWrapper<ViewManagerKpiMonth> queryWrapper = new QueryWrapper<>();
        if (params.containsKey("select")) {
            queryWrapper.select((String) params.get("select"));
        }
        if (params.containsKey("managerName")) {
            queryWrapper.eq("managerName", params.get("managerName"));
        }
        if (params.containsKey("companyName")) {
            queryWrapper.like("companyName", params.get("companyName"));
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
        return queryWrapper;
    }
}
