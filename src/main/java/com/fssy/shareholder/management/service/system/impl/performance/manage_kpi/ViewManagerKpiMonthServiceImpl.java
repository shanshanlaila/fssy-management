package com.fssy.shareholder.management.service.system.impl.performance.manage_kpi;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.mapper.system.performance.manage_kpi.ManagerKpiCoefficientMapper;
import com.fssy.shareholder.management.mapper.system.performance.manage_kpi.ManagerKpiScoreMapperOld;
import com.fssy.shareholder.management.pojo.system.performance.manage_kpi.ManagerKpiCoefficient;
import com.fssy.shareholder.management.pojo.system.performance.manage_kpi.ManagerKpiScoreOld;
import com.fssy.shareholder.management.pojo.system.performance.manage_kpi.ViewManagerKpiMonth;
import com.fssy.shareholder.management.mapper.system.performance.manage_kpi.ViewManagerKpiMonthMapper;
import com.fssy.shareholder.management.service.system.performance.manage_kpi.ViewManagerKpiMonthService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fssy.shareholder.management.tools.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.*;
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

    @Autowired
    private ManagerKpiCoefficientMapper managerKpiCoefficientMapper;

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
     * 计算总经理和分管经理的分数
     * @param
     * @return
     */
    @Override
    @Transactional
    public boolean createScore(Map<String, Object> params) {
        //获取前端传进来的值
        Object generalManagerValue = params.get("generalManager");

        QueryWrapper<ViewManagerKpiMonth> queryWrapper = getQueryWrapper(params);
        //条件查询出所有数据，对未锁定进行筛选
        List<ViewManagerKpiMonth> ViewManagerKpiMonths = viewManagerKpiMonthMapper.selectList(queryWrapper);
        List<ViewManagerKpiMonth> filterList = ViewManagerKpiMonths.stream()
                .filter(i -> i.getStatus().equals("已锁定")).collect(Collectors.toList());
        if (ObjectUtils.isEmpty(filterList)) {
            throw new ServiceException("没有查出数据或已生成！生成失败！");
        }

        //对要计算的变量进行初始化
        BigDecimal businessScore = new BigDecimal(0);
        BigDecimal incentiveScore = new BigDecimal(0);
        BigDecimal scoreSys = new BigDecimal(0);
        BigDecimal scoreSysTemp = new BigDecimal(0);
        BigDecimal sumTemp = new BigDecimal(0);
        int i = filterList.size();

        if(generalManagerValue.equals("是")){
            //算总经理的分数
            //遍历获取到符合条件的所有记录
            for (ViewManagerKpiMonth temp : filterList) {
                //首先看符合条件的记录有几条，那就循环几次计算出所需要算的分数
                //对执行的每一条
                //①获取相应条件的记录中的权重和人工调整分数
                BigDecimal proportion =  new BigDecimal(String.valueOf(temp.getProportion()));
                BigDecimal monthScoreAdjust = new BigDecimal(String.valueOf(temp.getMonthScoreAdjust()));
                //②判断这条记录是哪种类型
                String projectType = temp.getProjectType();
                if(projectType.equals("经营管理指标")){
                    //③计算这条记录的经营绩效分数存入临时变量businessScoreTemp
                    //定义百分号
                    BigDecimal per = new BigDecimal(0.01);
                    BigDecimal businessScoreTemp = proportion.multiply(monthScoreAdjust).multiply(per);
                    //并且将词条记录的经营绩效分数存入最终经营绩效分数,如果下调记录类型还是1，就可以进行分数求和
                    businessScore = businessScore.add(businessScoreTemp);
                } else if(projectType.equals("激励约束项目")){
                    //④计算这条记录的激励约束分数存入临时变量incentiveScoreTemp
                    //将人工生成分数直接赋值给临时变量
                    BigDecimal incentiveScoreTemp = monthScoreAdjust;
                    //并且将词条记录的激励约束分数存入最终激励约束分数,如果下调记录类型还是2，就可以进行分数求和
                    incentiveScore = incentiveScore.add(incentiveScoreTemp);
                }
                i--;
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
                    managerKpiScore.setScoreAuto(scoreSys);
                    managerKpiScore.setScoreAdjust(scoreSys);
                    managerKpiScore.setGeneralManagerScore(scoreSys);
                    //插入数据
                    managerKpiScoreMapper.insert(managerKpiScore);
                }
            }
        }else if (generalManagerValue.equals("否")) {
            //算分管经理的分数
            /**
             * step1：拿businessScore + managerName
             */
            //首先需要进行判断此公司是否计算经理人分数，如果没有则需要计算经理人分数
            //所以需要拿出本公司总经理分数
            params.put("generalManager","是");
            QueryWrapper<ManagerKpiScoreOld> newQueryWrapper = newGetQueryWrapper(params);
            List<ManagerKpiScoreOld> NewManagerKpiScoreOlds = managerKpiScoreMapper.selectList(newQueryWrapper);
            List<ManagerKpiScoreOld> newFilterList = NewManagerKpiScoreOlds.stream()
                    .filter(j -> j.getGeneralManager().equals("是")).collect(Collectors.toList());
            if (ObjectUtils.isEmpty(newFilterList)) {
                throw new ServiceException("没有查出数据或已生成！生成失败！");
            }
            //现在已经查出本年本月本公司的总经理数据记录，并拿出businessScore
            BigDecimal newBusinessScore = newFilterList.get(0).getBusinessScore();
            //拿出分管经理人姓名
            String newManagerName = filterList.get(0).getManagerName();
            /**
             * step2：拿difficultCoefficient
             */
            params.remove("month");
            params.put("generalManager","否");
            System.out.println(params);
            //还需要拿出difficultyCoefficient,通过下面两条语句拿出系数表中的值
            QueryWrapper<ManagerKpiCoefficient> newQueryWrappers = newGetQueryWrappers(params);
            List<ManagerKpiCoefficient> ManagerKpiCoefficients = managerKpiCoefficientMapper.selectList(newQueryWrappers);
            BigDecimal difficultCoefficient = ManagerKpiCoefficients.get(0).getDifficultCoefficient();
            /**
             * step3：计算businessScore + incentiveScore + scoreSys
             */
            //计算分管经理分
            //遍历获取到符合条件的所有记录
            for (ViewManagerKpiMonth temp : filterList) {
                //首先看符合条件的记录有几条，那就循环几次计算出所需要算的分数
                //对执行的每一条
                //①获取相应条件的记录中的权重和人工调整分数
                BigDecimal proportion =  new BigDecimal(String.valueOf(temp.getProportion()));
                BigDecimal monthScoreAdjust = new BigDecimal(String.valueOf(temp.getMonthScoreAdjust()));
                //②判断这条记录是哪种类型
                String projectType = temp.getProjectType();
                if(projectType.equals("经营管理指标")){
                    //③计算这条记录的经营绩效分数存入临时变量businessScoreTemp
                    //定义百分号
                    BigDecimal per = new BigDecimal(0.01);
                    BigDecimal businessScoreTemp = proportion.multiply(monthScoreAdjust).multiply(per);
                    //并且将词条记录的经营绩效分数存入最终经营绩效分数,如果下调记录类型还是1，就可以进行分数求和
                    businessScore = businessScore.add(businessScoreTemp);
                } else if(projectType.equals("激励约束项目")){
                    //④计算这条记录的激励约束分数存入临时变量incentiveScoreTemp
                    //将人工生成分数直接赋值给临时变量
                    BigDecimal incentiveScoreTemp = monthScoreAdjust;
                    //并且将词条记录的激励约束分数存入最终激励约束分数,如果下调记录类型还是2，就可以进行分数求和
                    incentiveScore = incentiveScore.add(incentiveScoreTemp);
                }
                i--;
                //⑤计算系统生成分数也就是分管经理分数
                BigDecimal temp1 = new BigDecimal(0.6);
                scoreSysTemp = businessScore.add(incentiveScore);
                sumTemp = scoreSysTemp.multiply(difficultCoefficient);
                scoreSys = newBusinessScore.multiply(temp1).add(sumTemp);
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
                    managerKpiScore.setScoreAuto(scoreSys);
                    managerKpiScore.setScoreAdjust(scoreSys);
                    managerKpiScore.setDifficultyCoefficient(difficultCoefficient);
                    managerKpiScore.setGeneralManagerScore(newBusinessScore);
                    //插入数据
                    managerKpiScoreMapper.insert(managerKpiScore);
                }
            }
        }
        return true;
    }

    /**
     * 查询条件
     * @param params
     * @return
     */
    private QueryWrapper<ViewManagerKpiMonth> getQueryWrapper(Map<String, Object> params) {
        //⑤在数据库中根据条件进行查询
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
        if (params.containsKey("id")) {
            queryWrapper.eq("id", params.get("id"));
        }
        return queryWrapper;
    }
    private QueryWrapper<ManagerKpiScoreOld> newGetQueryWrapper(Map<String, Object> params) {
        QueryWrapper<ManagerKpiScoreOld> newQueryWrapper = new QueryWrapper<>();
        if (params.containsKey("select")) {
            newQueryWrapper.select((String) params.get("select"));
        }
        if (params.containsKey("managerName")) {
            newQueryWrapper.eq("managerName", params.get("managerName"));
        }
        if (params.containsKey("companyName")) {
            newQueryWrapper.like("companyName", params.get("companyName"));
        }
        if (params.containsKey("generalManager")) {
            newQueryWrapper.like("generalManager", params.get("generalManager"));
        }
        if (params.containsKey("year")) {
            newQueryWrapper.eq("year", params.get("year"));
        }
        if (params.containsKey("month")) {
            newQueryWrapper.eq("month", params.get("month"));
        }
        return newQueryWrapper;
    }
    private QueryWrapper<ManagerKpiCoefficient> newGetQueryWrappers(Map<String, Object> params) {
        QueryWrapper<ManagerKpiCoefficient> newQueryWrappers = new QueryWrapper<>();
        if (params.containsKey("select")) {
            newQueryWrappers.select((String) params.get("select"));
        }
        if (params.containsKey("managerName")) {
            newQueryWrappers.eq("managerName", params.get("managerName"));
        }
        if (params.containsKey("companyName")) {
            newQueryWrappers.like("companyName", params.get("companyName"));
        }
        if (params.containsKey("generalManager")) {
            newQueryWrappers.like("generalManager", params.get("generalManager"));
        }
        if (params.containsKey("year")) {
            newQueryWrappers.eq("year", params.get("year"));
        }
        if (params.containsKey("difficultyCoefficient")) {
            newQueryWrappers.eq("difficultyCoefficient", params.get("difficultyCoefficient"));
        }
        return newQueryWrappers;
    }
}
