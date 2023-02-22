package com.fssy.shareholder.management.service.system.impl.company;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.mapper.manage.company.CompanyMapper;
import com.fssy.shareholder.management.mapper.system.config.AttachmentMapper;
import com.fssy.shareholder.management.mapper.system.config.ProjectRelationAttachmentMapper;
import com.fssy.shareholder.management.pojo.manage.company.Company;
import com.fssy.shareholder.management.pojo.system.company.LicenseWarranty;
import com.fssy.shareholder.management.mapper.system.company.LicenseWarrantyMapper;
import com.fssy.shareholder.management.pojo.system.company.RightsDetail;
import com.fssy.shareholder.management.pojo.system.config.Attachment;
import com.fssy.shareholder.management.pojo.system.config.ProjectRelationAttachment;
import com.fssy.shareholder.management.service.system.company.LicenseWarrantyService;
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
 * 基础	营业执照	 服务实现类
 * </p>
 *
 * @author Solomon
 * @since 2023-02-16
 */
@Service
public class LicenseWarrantyServiceImpl extends ServiceImpl<LicenseWarrantyMapper, LicenseWarranty> implements LicenseWarrantyService {

    @Autowired
    private LicenseWarrantyMapper licenseWarrantyMapper;


    @Autowired
    private CompanyMapper companyMapper;

    @Autowired
    private AttachmentMapper attachmentMapper;

    @Autowired
    private ProjectRelationAttachmentMapper projectRelationAttachmentMapper;

    //分页查询企业营业执照信息
    @Override
    public Page<Map<String, Object>> findLicenseWarrantyPerPageByParams(Map<String, Object> params) {
        QueryWrapper<LicenseWarranty> queryWrapper = getQueryWrapper(params);
        int limit = (int) params.get("limit");
        int page = (int) params.get("page");
        Page<Map<String, Object>> myPage = new Page<>(page,limit);
        myPage = licenseWarrantyMapper.selectMapsPage(myPage,queryWrapper);
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
            String businessLicenseDocName = InstandTool.objectToString(item.get("businessLicenseDocName"));
            QueryWrapper<LicenseWarranty> traceQueryWrapper = new QueryWrapper<>();
            traceQueryWrapper.eq("companyName", companyName).eq("businessLicenseDocName", businessLicenseDocName);
            traceQueryWrapper.orderByDesc("id");
            traceQueryWrapper.isNotNull("businessLicenseDocId");
            List<LicenseWarranty> investProjectPlanTraces = licenseWarrantyMapper.selectList(traceQueryWrapper);
            if (!ObjectUtils.isEmpty(investProjectPlanTraces)){
                item.put("currentProjectStatus",investProjectPlanTraces.get(0).getBusinessLicenseDocId());
            }else {
                item.put("currentProjectStatus","项目暂未开始");
            }

        });
        return myPage;
    }

    //通过主键删除 企业营业执照信息
    @Override
    public boolean delectLicenseWarrantyById(Integer id) {
        QueryWrapper<LicenseWarranty> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",id);
        LicenseWarranty licenseWarranty = licenseWarrantyMapper.selectList(queryWrapper).get(0);
        licenseWarranty.setStatus("未使用");
        int result = licenseWarrantyMapper.updateById(licenseWarranty);
        if (result > 0 ){
            return true;
        }
        return false;
    }

    //修改企业营业执照信息
    @Override
    public boolean updateLicenseWarrantyData(LicenseWarranty licenseWarranty, HttpServletRequest request) {
        if (ObjectUtils.isEmpty(licenseWarranty)){
            throw new ServiceException("股权信息不存在");
        }
        String companyId = request.getParameter("companyId");
        Company company = companyMapper.selectById(companyId);
        licenseWarranty.setCompanyName(company.getName());
        licenseWarranty.setCompanyId(Integer.valueOf(companyId));



        int result = licenseWarrantyMapper.updateById(licenseWarranty);
        if (result > 0 ){
            return true;
        }
        return false;
    }

    //添加企业营业执照单条记录
    @Override
    public boolean insertLicenseWarrantyData(LicenseWarranty licenseWarranty,HttpServletRequest request) {
        if (ObjectUtils.isEmpty(licenseWarranty)){
            throw new ServiceException("信息不存在");
        }
        if (licenseWarranty.getCompanyId() == null){
            throw new ServiceException("请选择被投资公司");
        }
        String companyId = request.getParameter("companyId");
        Company company = companyMapper.selectById(companyId);
        licenseWarranty.setCompanyName(company.getName());
        licenseWarranty.setCompanyId(Integer.valueOf(companyId));

        int result = licenseWarrantyMapper.insert(licenseWarranty);
        if (result > 0 ){
            return true;
        }
        return false;
    }

    @Override
    public boolean submitUploadFile(LicenseWarranty licenseWarranty, Map<String, Object> param) {
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
                    projectRelationAttachment.setProjectListId(Long.valueOf(licenseWarranty.getId()));
                    projectRelationAttachment.setConclusion(attachment.getConclusion());
                    projectRelationAttachment.setCompanyName(licenseWarranty.getCompanyName());
                    projectRelationAttachment.setCompanyId(Long.valueOf(licenseWarranty.getCompanyId()));
                    licenseWarranty.setBusinessLicenseDocId(Integer.valueOf(attachmentId));
                    licenseWarranty.setBusinessLicenseDocName(attachment.getFilename());
                    licenseWarrantyMapper.updateById(licenseWarranty);
                    // 默认就是上载成功
                    result = projectRelationAttachmentMapper.insert(projectRelationAttachment);
                }
            }
        }
        return result > 0;
    }

    private QueryWrapper<LicenseWarranty> getQueryWrapper(Map<String, Object>params){
        QueryWrapper<LicenseWarranty> queryWrapper = new QueryWrapper<>();
        if (params.containsKey("select")) {
            queryWrapper.select((String) params.get("select"));
        }
        if (params.containsKey("id")) {
            queryWrapper.eq("id", params.get("id"));
        }
        if (params.containsKey("businessLicenseDocId")) {
            queryWrapper.eq("businessLicenseDocId", params.get("businessLicenseDocId"));
        }
        if (params.containsKey("companyId")) {
            queryWrapper.eq("companyId", params.get("companyId"));
        }
        if (params.containsKey("companyName")) {
            queryWrapper.like("companyName", params.get("companyName"));
        }
        if (params.containsKey("businessLicenseDocName")) {
            queryWrapper.like("businessLicenseDocName", params.get("businessLicenseDocName"));
        }

        // 更改时间查询
        if (params.containsKey("changeTimeStart")) {
            queryWrapper.ge("changeTime", params.get("changeTimeStart"));
        }
        if (params.containsKey("changeTimeEnd")) {
            queryWrapper.le("changeTime", params.get("changeTimeEnd"));
        }

        if (params.containsKey("changeProject")) {
            queryWrapper.like("changeProject", params.get("changeProject"));
        }
        if (params.containsKey("changeBefore")) {
            queryWrapper.like("changeBefore", params.get("changeBefore"));
        }
        if (params.containsKey("changeAfter")) {
            queryWrapper.like("changeAfter", params.get("changeAfter"));
        }
        return queryWrapper;
    }
}
