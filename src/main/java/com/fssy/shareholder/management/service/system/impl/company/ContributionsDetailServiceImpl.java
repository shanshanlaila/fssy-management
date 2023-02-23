package com.fssy.shareholder.management.service.system.impl.company;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.mapper.manage.company.CompanyMapper;
import com.fssy.shareholder.management.mapper.system.config.AttachmentMapper;
import com.fssy.shareholder.management.mapper.system.config.ProjectRelationAttachmentMapper;
import com.fssy.shareholder.management.pojo.manage.company.Company;
import com.fssy.shareholder.management.pojo.system.company.ContributionsDetail;
import com.fssy.shareholder.management.mapper.system.company.ContributionsDetailMapper;
import com.fssy.shareholder.management.pojo.system.config.Attachment;
import com.fssy.shareholder.management.pojo.system.config.ProjectRelationAttachment;
import com.fssy.shareholder.management.pojo.system.operate.invest.InvestProjectList;
import com.fssy.shareholder.management.pojo.system.operate.invest.InvestProjectPlanTrace;
import com.fssy.shareholder.management.pojo.system.operate.invest.TechCapacityEvaluate;
import com.fssy.shareholder.management.service.system.company.ContributionsDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fssy.shareholder.management.tools.common.InstandTool;
import com.fssy.shareholder.management.tools.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * <p>
 * 基础	出资方明细	 服务实现类
 * </p>
 *
 * @author Solomon
 * @since 2023-02-15
 */
@Service
public class ContributionsDetailServiceImpl extends ServiceImpl<ContributionsDetailMapper, ContributionsDetail> implements ContributionsDetailService {

    @Autowired
    private ContributionsDetailMapper contributionsDetailMapper;

    @Autowired
    private CompanyMapper companyMapper;


    @Autowired
    private AttachmentMapper attachmentMapper;

    @Autowired
    private ProjectRelationAttachmentMapper projectRelationAttachmentMapper;


    private QueryWrapper<ContributionsDetail> getQueryWrapper(Map<String,Object> params) {
        QueryWrapper<ContributionsDetail> queryWrapper = new QueryWrapper<>();
        if (params.containsKey("select")) {
            queryWrapper.select((String) params.get("select"));
        }
        if (params.containsKey("id")) {
            queryWrapper.eq("id", params.get("id"));
        }
        if (params.containsKey("contributionsListId")) {
            queryWrapper.eq("contributionsListId", params.get("contributionsListId"));
        }
        if (params.containsKey("companyId")) {
            queryWrapper.eq("companyId", params.get("companyId"));
        }
        if (params.containsKey("companyName")) {
            queryWrapper.like("companyName", params.get("companyName"));
        }
        if (params.containsKey("contributionsDocId")) {
            queryWrapper.eq("contributionsDocId", params.get("contributionsDocId"));
        }
        if (params.containsKey("contributionsDocName")) {
            queryWrapper.like("contributionsDocName", params.get("contributionsDocName"));
        }
        if (params.containsKey("causeDesc")) {
            queryWrapper.like("causeDesc", params.get("causeDesc"));
        }
        if (params.containsKey("investorType")) {
            queryWrapper.like("investorType", params.get("investorType"));
        }
        if (params.containsKey("investorId")) {
            queryWrapper.eq("investorId", params.get("investorId"));
        }
        if (params.containsKey("investorName")) {
            queryWrapper.like("investorName", params.get("investorName"));
        }
        if (params.containsKey("contributionsMode")) {
            queryWrapper.like("contributionsMode", params.get("contributionsMode"));
        }
        if (params.containsKey("registeredCapital")) {
            queryWrapper.eq("registeredCapital", params.get("registeredCapital"));
        }
        if (params.containsKey("contributedCapital")) {
            queryWrapper.like("contributedCapital", params.get("contributedCapital"));
        }

        // 计划到账时间查询
        if (params.containsKey("planArrivalTimeStart")) {
            queryWrapper.ge("planArrivalTime", params.get("planArrivalTimeStart"));
        }
        if (params.containsKey("planArrivalTimeEnd")) {
            queryWrapper.le("planArrivalTime", params.get("planArrivalTimeEnd"));
        }

        // 实际到账时间查询
        if (params.containsKey("actualArrivalTimeStart")) {
            queryWrapper.ge("actualArrivalTime", params.get("actualArrivalTimeStart"));
        }
        if (params.containsKey("actualArrivalTimeEnd")) {
            queryWrapper.le("actualArrivalTime", params.get("actualArrivalTimeEnd"));
        }


        if (params.containsKey("sign")) {
            queryWrapper.eq("sign", params.get("sign"));
        }

        return queryWrapper;
    }

    //分页查询基础 投资方信息
    @Override
    public Page<Map<String, Object>> findContributionsDetailPerPageByParams(Map<String, Object> params) {
        int limit = (int) params.get("limit");
        int page = (int) params.get("page");
        QueryWrapper<ContributionsDetail> queryWrapper = getQueryWrapper(params);
        Page<Map<String, Object>> myPage = new Page<>(page,limit);
        myPage = contributionsDetailMapper.selectMapsPage(myPage,queryWrapper);
        // 2023-1-03 添加评优材料附件查询
        myPage.getRecords().forEach(item -> {
            LambdaQueryWrapper<ProjectRelationAttachment> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(ProjectRelationAttachment::getProjectListId,item.get("id"))
                    .select(ProjectRelationAttachment::getProjectListId,
                            ProjectRelationAttachment::getAttachmentId,
                            ProjectRelationAttachment::getMd5Path,
                            ProjectRelationAttachment::getFilename);
            List<ProjectRelationAttachment> attachments = projectRelationAttachmentMapper.selectList(wrapper);
            item.put("attachmentList", attachments);
        });

        myPage.getRecords().stream().forEach(item -> {
            String companyName = InstandTool.objectToString(item.get("companyName"));
            String contributionsDocName = InstandTool.objectToString(item.get("contributionsDocName"));
            QueryWrapper<ContributionsDetail> traceQueryWrapper = new QueryWrapper<>();
            traceQueryWrapper.eq("companyName", companyName).eq("contributionsDocName", contributionsDocName);
            traceQueryWrapper.orderByDesc("id");
            traceQueryWrapper.isNotNull("contributionsDocId");
            List<ContributionsDetail> investProjectPlanTraces = contributionsDetailMapper.selectList(traceQueryWrapper);
            if (!ObjectUtils.isEmpty(investProjectPlanTraces)){
                item.put("currentProjectStatus",investProjectPlanTraces.get(0).getContributionsDocId());
            }else {
                item.put("currentProjectStatus","项目暂未开始");
            }

        });
        return myPage;

    }

    /*@Override
    public List<Map<String, Object>> findContributionsDetailByParams(Map<String, Object> params, List<String> selectedIds) {
        QueryWrapper<ContributionsDetail> queryWrapper = getQueryWrapper(params);
        List<ContributionsDetail> contributionsDetails = contributionsDetailMapper.selectList(queryWrapper);
        List<Map<String, Object>> resultList = new ArrayList<>();
        for (ContributionsDetail contributionsDetail : contributionsDetails) {

            Map<String, Object> result = new HashMap<String, Object>();
            result.put("name", contributionsDetail.getCompanyId() + "|" +contributionsDetail.getCompanyName());
            result.put("value", contributionsDetail.getCompanyId());
            result.put("id", contributionsDetail.getCompanyId());
            boolean selected = false;
            for (String selectedId : selectedIds) {

                if (selectedId.equals(contributionsDetail.getCompanyId().toString())) {
                    selected = true;
                    break;
                }
            }
            result.put("selected", selected);
            resultList.add(result);
        }
        return resultList;
    }*/

    //删除 基础 投资方信息
    @Override
    public boolean delectContributionsDetailByid(Integer id) {
        QueryWrapper<ContributionsDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",id);
        ContributionsDetail contributionsDetail = contributionsDetailMapper.selectList(queryWrapper).get(0);
        contributionsDetail.setStatus("未开业");
        int result = contributionsDetailMapper.updateById(contributionsDetail);
        if (result > 0){
            return true;
        }
        return false;
    }

    //修改基础 投资方信息
    @Override
    public boolean updateContributionsDetailData(ContributionsDetail contributionsDetail, HttpServletRequest request) {
        if (ObjectUtils.isEmpty(contributionsDetail)){
            throw new ServiceException("股权信息不存在");
        }

        String investorId = request.getParameter("companyTowId");
        String companyId = request.getParameter("companyOneId");
        Company company = companyMapper.selectById(companyId);
        Company investorCompany = companyMapper.selectById(investorId);
        contributionsDetail.setCompanyName(company.getName());
        contributionsDetail.setCompanyId(Integer.valueOf(companyId));
        contributionsDetail.setInvestorName((investorCompany.getName()));
        contributionsDetail.setInvestorId(investorId);

        int result = contributionsDetailMapper.updateById(contributionsDetail);
        if (result>0){
            return true;
        }
        return false;
    }

    //插入基础 投资方信息
    @Override
    public boolean insertContributionsDetailData(ContributionsDetail contributionsDetail,HttpServletRequest request) {

        if (ObjectUtils.isEmpty(request.getParameter("companyTowId"))){
            throw new ServiceException("请选择投资方名称");
        }
        if (ObjectUtils.isEmpty(request.getParameter("companyOneId"))){
            throw new ServiceException("请选择被投资公司名称");
        }


        String investorId = request.getParameter("companyTowId");
        String companyId = request.getParameter("companyOneId");
        Company company = companyMapper.selectById(companyId);
        Company investorCompany = companyMapper.selectById(investorId);
        contributionsDetail.setCompanyName(company.getName());
        contributionsDetail.setCompanyId(Integer.valueOf(companyId));
        contributionsDetail.setInvestorName((investorCompany.getName()));
        contributionsDetail.setInvestorId(investorId);
        int result = contributionsDetailMapper.insert(contributionsDetail);
        if (result > 0 ){
            return true;
        }
        return false;
    }

    @Override
    public boolean submitUploadFile(ContributionsDetail contributionsDetail, Map<String, Object> param) {
        int result = 0;
        // 维护bs_operate_invest_project_relation_attachment（投资项目清单附件关联表）

        ProjectRelationAttachment projectRelationAttachment;
        if (param.containsKey("attachmentIds")) {
            String attachmentIds = (String) param.get("attachmentIds");
            List<String> attachmentIdList = Arrays.asList(attachmentIds.split(","));
            if (!ObjectUtils.isEmpty(attachmentIdList)) {
                for (String attachmentId : attachmentIdList) {
                    projectRelationAttachment = new ProjectRelationAttachment();
                    Attachment attachment = attachmentMapper.selectById(attachmentId);
                    if (ObjectUtils.isEmpty(attachment)){
                        throw new ServiceException("未选择附件，提交失败");
                    }
                    projectRelationAttachment.setImportDate(attachment.getImportDate());
                    // 保存附件表
                    projectRelationAttachment.setFilename(attachment.getFilename());
                    // 默认就是正在导入
                    projectRelationAttachment.setMd5Path(attachment.getMd5Path());
                    projectRelationAttachment.setPath(attachment.getPath());
                    projectRelationAttachment.setAttachmentId(attachment.getId());
                    projectRelationAttachment.setNote(attachment.getNote());
                    projectRelationAttachment.setProjectListId(Long.valueOf(contributionsDetail.getId()));
                    projectRelationAttachment.setConclusion(attachment.getConclusion());
                    projectRelationAttachment.setCompanyName(contributionsDetail.getCompanyName());
                    projectRelationAttachment.setCompanyId(Long.valueOf(contributionsDetail.getCompanyId()));
                    contributionsDetail.setContributionsDocId(Integer.valueOf(attachmentId));
                    contributionsDetail.setContributionsDocName(attachment.getFilename());
                    contributionsDetailMapper.updateById(contributionsDetail);
                    // 默认就是上载成功
                    result = projectRelationAttachmentMapper.insert(projectRelationAttachment);
                }
            }
        }
        return result > 0;
    }
}
