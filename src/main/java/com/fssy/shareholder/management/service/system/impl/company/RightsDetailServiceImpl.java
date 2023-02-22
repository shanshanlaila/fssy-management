package com.fssy.shareholder.management.service.system.impl.company;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.mapper.manage.company.CompanyMapper;
import com.fssy.shareholder.management.mapper.system.company.ContributionsDetailMapper;
import com.fssy.shareholder.management.mapper.system.config.AttachmentMapper;
import com.fssy.shareholder.management.mapper.system.config.ProjectRelationAttachmentMapper;
import com.fssy.shareholder.management.pojo.manage.company.Company;
import com.fssy.shareholder.management.pojo.system.company.ContributionsDetail;
import com.fssy.shareholder.management.pojo.system.company.RightsDetail;
import com.fssy.shareholder.management.mapper.system.company.RightsDetailMapper;
import com.fssy.shareholder.management.pojo.system.config.Attachment;
import com.fssy.shareholder.management.pojo.system.config.ProjectRelationAttachment;
import com.fssy.shareholder.management.pojo.system.operate.invest.TechCapacityEvaluate;
import com.fssy.shareholder.management.service.system.company.RightsDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fssy.shareholder.management.tools.common.InstandTool;
import com.fssy.shareholder.management.tools.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 基础	股权明细 服务实现类
 * </p>
 *
 * @author Solomon
 * @since 2023-02-15
 */
@Service
public class RightsDetailServiceImpl extends ServiceImpl<RightsDetailMapper, RightsDetail> implements RightsDetailService {

    @Autowired
    private RightsDetailMapper rightsDetailMapper;



    @Autowired
    private CompanyMapper companyMapper;

    @Autowired
    AttachmentMapper attachmentMapper;

    @Autowired
    private ProjectRelationAttachmentMapper projectRelationAttachmentMapper;

    //查询 基础	股权明细 服务实现类信息
    @Override
    public List<RightsDetail> findRightsDetailDataByParams(Map<String, Object> params) {
        QueryWrapper<RightsDetail> queryWrapper = getQueryWrapper(params);
        return rightsDetailMapper.selectList(queryWrapper);
    }

    //分页查询 基础股权明细 服务实现类信息
    @Override
    public Page<Map<String, Object>> findRightsDetailDataListPerPageByParams(Map<String, Object> params) {
        QueryWrapper<RightsDetail> queryWrapper = getQueryWrapper(params);
        int limit = (int) params.get("limit");
        int page = (int) params.get("page");
        Page<Map<String, Object>> myPage = new Page<>(page,limit);
        myPage = rightsDetailMapper.selectMapsPage(myPage,queryWrapper);
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
            String stockRightsDocName = InstandTool.objectToString(item.get("stockRightsDocName"));
            QueryWrapper<RightsDetail> traceQueryWrapper = new QueryWrapper<>();
            traceQueryWrapper.eq("companyName", companyName).eq("stockRightsDocName", stockRightsDocName);
            traceQueryWrapper.orderByDesc("id");
            traceQueryWrapper.isNotNull("stockRightsDocId");
            List<RightsDetail> investProjectPlanTraces = rightsDetailMapper.selectList(traceQueryWrapper);
            if (!ObjectUtils.isEmpty(investProjectPlanTraces)){
                item.put("currentProjectStatus",investProjectPlanTraces.get(0).getStockRightsDocId());
            }else {
                item.put("currentProjectStatus","项目暂未开始");
            }

        });
        return myPage;

    }

    @Override
    public List<RightsDetail> findRightslDataByParams(Map<String, Object> params) {
        return null;
    }

    //通过主键删除 基础股权明细 服务实现类信息
    @Override
    public boolean delectRightsDetailDataById(Integer id) {
        QueryWrapper<RightsDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",id);
        RightsDetail rightsDetail = rightsDetailMapper.selectList(queryWrapper).get(0);
        String status = rightsDetail.getStatus();
        rightsDetail.setStatus("未使用");
        int result = rightsDetailMapper.updateById(rightsDetail);
        if (result>0){
            return true;
        }
        return false;
    }

    //修改基础股权信息
    @Override
    public boolean updateRightsDetailData(RightsDetail rightsDetail, HttpServletRequest request) {
        if (ObjectUtils.isEmpty(rightsDetail)){
            throw new ServiceException("股权信息不存在");
        }
        String investorId = request.getParameter("companyTowId");
        String companyId = request.getParameter("companyOneId");
        Company company = companyMapper.selectById(companyId);
        Company investorCompany = companyMapper.selectById(investorId);
        rightsDetail.setCompanyName(company.getName());
        rightsDetail.setCompanyId(companyId);
        rightsDetail.setInvestorName((investorCompany.getName()));
        rightsDetail.setInvestorId(Integer.valueOf(investorId));
        int result = rightsDetailMapper.updateById(rightsDetail);
        return result > 0;
    }

    //添加单条企业基础股权信息
    @Override
    public boolean insertRightsDetailData(RightsDetail rightsDetail,HttpServletRequest request) {
        if (ObjectUtils.isEmpty(rightsDetail)){
            throw new ServiceException("股权信息不存在");
        }
        String investorId = request.getParameter("companyTowId");
        String companyId = request.getParameter("companyOneId");
        Company company = companyMapper.selectById(companyId);
        Company investorCompany = companyMapper.selectById(investorId);
        rightsDetail.setCompanyName(company.getName());
        rightsDetail.setCompanyId(companyId);
        rightsDetail.setInvestorName((investorCompany.getName()));
        rightsDetail.setInvestorId(Integer.valueOf(investorId));
        int result = rightsDetailMapper.insert(rightsDetail);
        if (result > 0){
            return true;
        }
        return false;
    }

    @Override
    public boolean submitUploadFile(RightsDetail rightsDetail, Map<String, Object> param) {
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
                    projectRelationAttachment.setProjectListId(Long.valueOf(rightsDetail.getId()));
                    projectRelationAttachment.setConclusion(attachment.getConclusion());
                    projectRelationAttachment.setCompanyName(rightsDetail.getCompanyName());
                    projectRelationAttachment.setCompanyId(Long.valueOf(rightsDetail.getCompanyId()));
                    rightsDetail.setStockRightsDocId(Integer.valueOf(attachmentId));
                    rightsDetail.setStockRightsDocName(attachment.getFilename());


                    System.out.println(attachmentIds);

                    rightsDetailMapper.updateById(rightsDetail);
                    // 默认就是上载成功
                    result = projectRelationAttachmentMapper.insert(projectRelationAttachment);
                }
            }
        }
        return result > 0;
    }

    private QueryWrapper<RightsDetail> getQueryWrapper(Map<String,Object> params) {
        QueryWrapper<RightsDetail> queryWrapper = new QueryWrapper<>();
        if (params.containsKey("select")) {
            queryWrapper.select((String) params.get("select"));
        }
        if (params.containsKey("id")) {
            queryWrapper.eq("id", params.get("id"));
        }
        if (params.containsKey("stockRightsListId")) {
            queryWrapper.eq("stockRightsListId", params.get("stockRightsListId"));
        }
        if (params.containsKey("companyOneId")) {
            queryWrapper.eq("companyId", params.get("companyId"));
        }
        if (params.containsKey("companyTowId")) {
            queryWrapper.eq("companyId", params.get("companyId"));
        }
        if (params.containsKey("companyId")) {
            queryWrapper.eq("companyId", params.get("companyId"));
        }
        if (params.containsKey("companyName")) {
            queryWrapper.like("companyName", params.get("companyName"));
        }
        if (params.containsKey("stockRightsDocId")) {
            queryWrapper.eq("stockRightsDocId", params.get("stockRightsDocId"));
        }
        if (params.containsKey("stockRightsDocName")) {
            queryWrapper.like("stockRightsDocName", params.get("stockRightsDocName"));
        }
        if (params.containsKey("causeDesc")) {
            queryWrapper.like("causeDesc", params.get("causeDesc"));
        }
        if (params.containsKey("stockRightsType")) {
            queryWrapper.like("stockRightsType", params.get("stockRightsType"));
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
        if (params.containsKey("beforeRightsRatio")) {
            queryWrapper.like("beforeRightsRatio", params.get("beforeRightsRatio"));
        }
        if (params.containsKey("rightsRatio")) {
            queryWrapper.like("rightsRatio", params.get("rightsRatio"));
        }
        if (params.containsKey("changeTime")) {
            queryWrapper.like("changeTime", params.get("changeTime"));
        }

        // 变更时间开始查询
        if (params.containsKey("changeTimeStart")) {
            queryWrapper.ge("changeTime", params.get("changeTimeStart"));
        }
        if (params.containsKey("changeTimeEnd")) {
            queryWrapper.le("changeTime", params.get("changeTimeEnd"));
        }

        if (params.containsKey("sign")) {
            queryWrapper.eq("sign", params.get("sign"));
        }
        if (params.containsKey("status")) {
            queryWrapper.like("status", params.get("status"));
        }

        return queryWrapper;
    }
}
