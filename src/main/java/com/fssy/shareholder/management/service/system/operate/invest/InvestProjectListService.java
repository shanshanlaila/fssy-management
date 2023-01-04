package com.fssy.shareholder.management.service.system.operate.invest;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fssy.shareholder.management.pojo.system.config.Attachment;
import com.fssy.shareholder.management.pojo.system.operate.invest.InvestProjectList;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * **数据表名：	bs_operate_invest_project_list	**数据表中文名：	年度投资项目清单	**业务部门：	经营管理部	**数据表作用：	记录 企业年度投资项目清单	**创建人创建日期：	TerryZeng 2022-12-2 服务类
 * </p>
 *
 * @author Solomon
 * @since 2022-12-09
 */
public interface InvestProjectListService extends IService<InvestProjectList> {
    /**
     * 通过查询条件，查询数据
     * @param params
     * @return
     */
    List<InvestProjectList> findInvestProjectListDataByParams(Map<String, Object> params);



    /**
     * 通过查询条件 分页 查询列表
     * @param params
     * @return
     */
    Page<Map<String, Object>> findInvestProjectListDataListPerPageByParams(Map<String, Object> params);



    /**
     * 通过查询条件，查询数据(导出）
     * @param params
     * @return
     */
    List<Map<String,Object>> findInvestProjectDataByParams(Map<String, Object> params);


    /**
     * 用于主键删除
     * @param id
     * @return
     */
    public boolean deleteInvestProjectListDataById(Integer id);


    /**
     * 修改年度项目清单状态
     * @param
     * @param
     * @return
     */
    boolean updateInvestProjectListData(InvestProjectList investProjectList);

    /**
     * 读取附件数据到数据库
     * @param attachment
     * @return
     */
    Map<String,Object> readInvestProjectListDataSource(Attachment attachment);

    /**
     * 新增年度投资项目
     * @param investProjectList 年度投资项目清单实体
     */
    boolean insertInvestProjectList(InvestProjectList investProjectList, HttpServletRequest request);

    /**
     * 提交年度投资项目附件上传
     *
     * @param param 参数map
     * @return 结果
     */
    boolean submitUploadFile(InvestProjectList investProjectList,Map<String, Object> param);
    /**
     * 修改年度项目清单信息
     * @param
     * @param
     * @return
     */
    boolean updateInvestProjectListData(InvestProjectList investProjectList, HttpServletRequest request);

    /**
     * 通过查询条件，查询数据(map)
     * @param params
     * @return
     */
    List<Map<String, Object>> findInvestProjectMapListDataByParams(Map<String, Object> params);



    /**
     * 通过查询条件 分页 查询列表(map)
     * @param params
     * @return
     */
    Page<Map<String, Object>> findInvestProjectListDataMapListPerPageByParams(Map<String, Object> params);
}
