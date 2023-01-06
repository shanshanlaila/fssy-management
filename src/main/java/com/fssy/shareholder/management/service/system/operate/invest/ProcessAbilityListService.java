package com.fssy.shareholder.management.service.system.operate.invest;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.pojo.system.config.Attachment;
import com.fssy.shareholder.management.pojo.system.operate.invest.ProcessAbilityList;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fssy.shareholder.management.pojo.system.performance.manager.ManagerQualitativeEvalStd;

import java.lang.management.OperatingSystemMXBean;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * **数据表名：	bs_operate_process_ability_list	**数据表中文名：	工艺基础能力台账	**业务部门：	经营管理部	**数据表作用：	管理工艺基础能力对比情况台账	**创建人创建日期：	TerryZeng 2022-12-2 服务类
 * </p>
 *
 * @author Solomon
 * @since 2022-12-08
 */
public interface ProcessAbilityListService extends IService<ProcessAbilityList> {

    /**
     * 通过查询条件 分页 查询列表
     * @param params
     * @return
     */
    Page<Map<String, Object>> findProcessAbilityListDataListPerPageByParams(Map<String, Object> params);


    /**
     * 通过查询条件，查询数据
     * @param params
     * @return
     */
    List<ProcessAbilityList> findProcessAbilityListDataByParams(Map<String, Object> params);    /**


     /**
     * 通过查询条件，查询数据(导出）
     * @param params
     * @return
     */
    List<Map<String,Object>> findManagerDataByParams(Map<String, Object> params);


    /**
     * 用于主键删除
     * @param id
     * @return
     */
    public boolean deleteProcessAbilityListDataById(Integer id);


    /**
     * 修改经理人绩效定性评分各项目占比
     * @param processAbilityList
     * @return
     */
    boolean updateProcessAbilityListData(ProcessAbilityList processAbilityList);


    /**
     * 读取附件数据到数据库
     * @param attachment
     * @return
     */
    Map<String,Object> reaadProcessAbilityListDataSource(Attachment attachment, String year, String companyName);



    /**
     * 添加经理人绩效定性评分各项目占比信息
     * @param processAbilityList
     * @return
     */
    boolean insertProcessAbilityListStd(ProcessAbilityList processAbilityList);

    /**
     * 提交工艺能力台账多附件上传
     * @param processAbilityList
     * @param param
     * @return
     */
    boolean submitUploadFile(ProcessAbilityList processAbilityList,Map<String,Object> param);


}
