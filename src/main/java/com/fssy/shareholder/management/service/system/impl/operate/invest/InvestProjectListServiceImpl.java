package com.fssy.shareholder.management.service.system.impl.operate.invest;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fssy.shareholder.management.mapper.manage.company.CompanyMapper;
import com.fssy.shareholder.management.mapper.system.config.AttachmentMapper;
import com.fssy.shareholder.management.mapper.system.config.ProjectRelationAttachmentMapper;
import com.fssy.shareholder.management.mapper.system.operate.invest.InvestProjectListMapper;
import com.fssy.shareholder.management.mapper.system.operate.invest.InvestProjectPlanTraceDetailMapper;
import com.fssy.shareholder.management.mapper.system.operate.invest.InvestProjectPlanTraceMapper;
import com.fssy.shareholder.management.pojo.manage.company.Company;
import com.fssy.shareholder.management.pojo.manage.user.User;
import com.fssy.shareholder.management.pojo.system.config.Attachment;
import com.fssy.shareholder.management.pojo.system.config.ProjectRelationAttachment;
import com.fssy.shareholder.management.pojo.system.operate.invest.InvestProjectList;
import com.fssy.shareholder.management.pojo.system.operate.invest.InvestProjectPlanTrace;
import com.fssy.shareholder.management.service.common.SheetService;
import com.fssy.shareholder.management.service.system.operate.invest.InvestProjectListService;
import com.fssy.shareholder.management.tools.common.GetTool;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * <p>
 * **???????????????	bs_operate_invest_project_list	**?????????????????????	????????????????????????	**???????????????	???????????????	**??????????????????	?????? ??????????????????????????????	**????????????????????????	TerryZeng 2022-12-2 ???????????????
 * </p>
 *
 * @author Solomon
 * @since 2022-12-09
 */
@Service
public class InvestProjectListServiceImpl extends ServiceImpl<InvestProjectListMapper, InvestProjectList> implements InvestProjectListService {
    @Autowired
    private SheetService sheetService;

    @Autowired
    private InvestProjectListMapper investProjectListMapper;

    @Autowired
    private InvestProjectPlanTraceMapper investProjectPlanTraceMapper;

    @Autowired
    private InvestProjectPlanTraceDetailMapper investProjectPlanTraceDetailMapper;

    @Autowired
    private CompanyMapper companyMapper;

    @Autowired
    private AttachmentMapper attachmentMapper;

    @Autowired
    private ProjectRelationAttachmentMapper projectRelationAttachmentMapper;

    /**
     * ??????????????????????????????
     *
     * @param params
     * @return
     */
    @Override
    public List<InvestProjectList> findInvestProjectListDataByParams(Map<String, Object> params) {
        QueryWrapper<InvestProjectList> queryWrapper = getQueryWrapper(params);
        return investProjectListMapper.selectList(queryWrapper);

    }



    /**
     * ?????????????????????
     *
     * @param result ??????map
     * @param append ?????????????????????
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
     * ????????????????????????????????????
     *
     * @param params
     * @return
     */
    @Override
    public List<Map<String, Object>> findInvestProjectDataByParams(Map<String, Object> params) {
        QueryWrapper<InvestProjectList> queryWrapper = getQueryWrapper(params);
        return investProjectListMapper.selectMaps(queryWrapper);

    }

    @Override
    public boolean deleteInvestProjectListDataById(Integer id) {

        QueryWrapper<InvestProjectPlanTrace> queryWrapper = new QueryWrapper<>();
        String projectListId = String.valueOf(id);
        queryWrapper.eq("projectListId", projectListId);
        investProjectPlanTraceMapper.delete(queryWrapper);
//
//        QueryWrapper<InvestProjectPlanTraceDetail> queryWrapperTwo = new QueryWrapper<>();
//        queryWrapperTwo.eq("projectListId",projectListId);
//        investProjectPlanTraceDetailMapper.delete(queryWrapperTwo);


        int result = investProjectListMapper.deleteById(id);
        if (result > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean updateInvestProjectListData(InvestProjectList investProjectList) {
        int result = investProjectListMapper.updateById(investProjectList);
        if (result > 0) {
            return true;
        }
        return false;
    }

    @Override
    public Map<String, Object> readInvestProjectListDataSource(Attachment attachment) {
        // ????????????
        Map<String, Object> result = new HashMap<>();
        result.put("content", "");
        result.put("failed", false);
        StringBuffer sb = new StringBuffer();// ??????????????????
        //????????????
        sheetService.load(attachment.getPath(), attachment.getFilename());//?????????????????????????????????
        //????????????
        sheetService.readByName("????????????????????????");//???????????????????????????????????????
        //??????????????????
        Sheet sheet = sheetService.getSheet();
        if (ObjectUtils.isEmpty(sheet)) {
            throw new ServiceException("??????????????????????????????????????????????????????????????????????????????");
        }
        //??????????????????
        Date importDate = attachment.getImportDate();
        //??????????????????
        Row row;
        //??????????????????????????????????????????????????????
        // 2022-06-01 ????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
        short maxSize = sheet.getRow(0).getLastCellNum();//??????(????????????)
        // ???????????????(???????????????????????????2????????????)
        for (int j = 1; j <= sheet.getLastRowNum(); j++) {
            // getPhysicalNumberOfRows()???????????????????????????????????????
            //if (j==3)continue;// ????????????????????????2???3??????????????????
            List<String> cells = new ArrayList<>();//???????????????????????????list??????
            row = sheet.getRow(j);//?????????j???
            // ??????????????????????????? Row?????????cell??????
            // short lastCellNum = row.getLastCellNum();
            // ??????row???????????????????????????
            for (int k = 0; k < maxSize + 2; k++) {
                // ???????????????????????????????????????
                if (row.getCell(k) == null) {
                    cells.add("");
                    continue;
                }
                // ???????????????????????????????????????
                if (row.getCell(k).getCellType() == CellType.FORMULA) {
                    row.getCell(k).setCellType(CellType.STRING);
                    String res = row.getCell(k).getStringCellValue();
                    cells.add(res);
                    continue;
                }
                //Cannot get a STRING value from a NUMERIC cell ??????????????????????????????
                Cell cell = row.getCell(k);
                String res = sheetService.getValue(cell).trim();//?????????????????????
                cells.add(res);// ???????????????????????????
            }


            // ?????????????????????
            Cell cell = row.createCell(SheetService.columnToIndex("AD"));//?????????????????????excel AD???
            String year = cells.get(SheetService.columnToIndex("A"));
            //???????????????
            if (ObjectUtils.isEmpty(year)) {
                setFailedContent(result, String.format("???%s?????????????????????", j + 1));
                cell.setCellValue("???????????????");
                continue;
            }
            String month = cells.get(SheetService.columnToIndex("B"));
            if (ObjectUtils.isEmpty(month)) {
                setFailedContent(result, String.format("???%s?????????????????????", j + 1));
                cell.setCellValue("???????????????");
                continue;
            }
            String companyName = cells.get(SheetService.columnToIndex("C"));
            if (ObjectUtils.isEmpty(companyName)) {
                setFailedContent(result, String.format("???%s???????????????????????????", j + 1));
                cell.setCellValue("?????????????????????");
                continue;
            }
            String projectName = cells.get(SheetService.columnToIndex("D"));
            if (ObjectUtils.isEmpty(projectName)) {
                setFailedContent(result, String.format("???%s???????????????????????????", j + 1));
                cell.setCellValue("?????????????????????");
                continue;
            }
            String projectSource = cells.get(SheetService.columnToIndex("E"));
            if (ObjectUtils.isEmpty(projectSource)) {
                setFailedContent(result, String.format("???%s???????????????????????????", j + 1));
                cell.setCellValue("?????????????????????");
                continue;
            }
            String projectType = cells.get(SheetService.columnToIndex("F"));
            if (ObjectUtils.isEmpty(projectType)) {
                setFailedContent(result, String.format("???%s????????????????????????", j + 1));
                cell.setCellValue("?????????????????????");
                continue;
            }
            String projectClass = cells.get(SheetService.columnToIndex("G"));
            if (ObjectUtils.isEmpty(projectClass)) {
                setFailedContent(result, String.format("???%s??????????????????????????????", j + 1));
                cell.setCellValue("???????????????????????????");
                continue;
            }
            String projectAbstract = cells.get(SheetService.columnToIndex("H"));

            String investmentVolumePlan = cells.get(SheetService.columnToIndex("I"));
            if (ObjectUtils.isEmpty(investmentVolumePlan)) {
                setFailedContent(result, String.format("???%s???????????????????????????", j + 1));
                cell.setCellValue("????????????????????????");
                continue;
            }
            String respManager = cells.get(SheetService.columnToIndex("J"));
            if (ObjectUtils.isEmpty(respManager)) {
                setFailedContent(result, String.format("???%s???????????????????????????", j + 1));
                cell.setCellValue("????????????????????????");
                continue;
            }

            //????????????????????????????????????
            QueryWrapper<InvestProjectList> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("companyName",companyName).eq("year",year).eq("projectName",projectName).eq("month",month);
            List<InvestProjectList> investProjectLists = investProjectListMapper.selectList(queryWrapper);
            if (investProjectLists.size()>0){
                setFailedContent(result, String.format("???%s????????????????????????,???%s????????????????????????,???s%???????????????????????????,???s%?????????????????????", j + 1));
                cell.setCellValue("??????????????????");
                continue;
            }

            String projectContact = cells.get(SheetService.columnToIndex("K"));
            String projectSrartDatePlan = cells.get(SheetService.columnToIndex("L"));
            String projectEndDatePlan = cells.get(SheetService.columnToIndex("M"));
            String businessUnit = cells.get(SheetService.columnToIndex("N"));

            //???????????????
            InvestProjectList investProjectList = new InvestProjectList();
            investProjectList.setCompanyName(companyName);
            Date date = new Date();
            investProjectList.setYear(InstandTool.stringToInteger(year));
            investProjectList.setMonth(InstandTool.stringToInteger(month));
            investProjectList.setProjectName(projectName);
            investProjectList.setProjectAbstract(projectAbstract);
            investProjectList.setProjectSource(projectSource);
            investProjectList.setProjectType(projectType);
            investProjectList.setProjectClass(projectClass);
            investProjectList.setInvestmentVolumePlan(InstandTool.stringToDouble(investmentVolumePlan));
            investProjectList.setRespManager(respManager);
            investProjectList.setProjectContact(projectContact);
            //??????????????????????????????????????????????????????????????????
            LambdaQueryWrapper<Company> companyLambdaQueryWrapper = new LambdaQueryWrapper<>();
            companyLambdaQueryWrapper.eq(Company::getName,companyName);
            List<Company> companyList1 = companyMapper.selectList(companyLambdaQueryWrapper);
            if (ObjectUtils.isEmpty(companyList1)) {
                setFailedContent(result,String.format("??????%s????????????%s?????????????????????????????????????????????????????????", j + 1, companyName));
                cell.setCellValue(String.format("????????????%s??????????????????????????????????????????", j + 1));
                continue;
            }
            Company company = companyList1.get(0);
            investProjectList.setCompanyId(InstandTool.integerToLong(company.getId()));
            investProjectList.setCompanyShortName(company.getShortName());
            User user = GetTool.getUser();
            investProjectList.setCreatedAt(LocalDateTime.now());
            investProjectList.setCreatedId(user.getId());
            investProjectList.setCreatedName(user.getName());
            //????????????
            if (ObjectUtils.isEmpty(projectSrartDatePlan)) {
                investProjectList.setProjectSrartDatePlan(null);
            } else {
                investProjectList.setProjectSrartDatePlan(LocalDate.parse(projectSrartDatePlan));
            }
            if (ObjectUtils.isEmpty(projectEndDatePlan)) {
                investProjectList.setProjectEndDatePlan(null);

            } else {
                investProjectList.setProjectEndDatePlan(LocalDate.parse(projectEndDatePlan));
            }

            investProjectList.setBusinessUnit(businessUnit);
            investProjectListMapper.insert(investProjectList);
            cell.setCellValue("????????????");
        }
        sheetService.write(attachment.getPath(), attachment.getFilename());//??????excel???
        return result;
    }



    private QueryWrapper<InvestProjectList> getQueryWrapper(Map<String, Object> params) {
        QueryWrapper<InvestProjectList> queryWrapper = new QueryWrapper<>();
        if (params.containsKey("select")) {
            queryWrapper.select((String) params.get("select"));
        }
        if (params.containsKey("id")) {
            queryWrapper.eq("id", params.get("id"));
        }
        if (params.containsKey("projectName")) {
            queryWrapper.like("projectName", params.get("projectName"));
        }
        if (params.containsKey("year")) {
            queryWrapper.eq("year", params.get("year"));
        }
        if (params.containsKey("month")) {
            queryWrapper.eq("month", params.get("month"));
        }
        if (params.containsKey("companyName")) {
            queryWrapper.like("companyName", params.get("companyName"));
        }
        if (params.containsKey("companyId")) {
            queryWrapper.eq("companyId", params.get("companyId"));
        }
        if (params.containsKey("companyIdList")) {
            queryWrapper.in("companyId", (List<String>) params.get("companyIdList"));
        }
        if (params.containsKey("companyShortName")) {
            queryWrapper.like("companyShortName", params.get("companyShortName"));
        }
        if (params.containsKey("projectSource")) {
            queryWrapper.like("projectSource", params.get("projectSource"));
        }
        if (params.containsKey("projectType")) {
            queryWrapper.like("projectType", params.get("projectType"));
        }
        if (params.containsKey("projectClass")) {
            queryWrapper.like("projectClass", params.get("projectClass"));
        }
        if (params.containsKey("projectAbstract")) {
            queryWrapper.like("projectAbstract", params.get("projectAbstract"));
        }


        // ????????????????????????
        if (params.containsKey("investmentVolumePlanStart")) {
            queryWrapper.ge("investmentVolumePlan", params.get("investmentVolumePlanStart"));
        }
        if (params.containsKey("investmentVolumePlanEnd")) {
            queryWrapper.le("investmentVolumePlan", params.get("investmentVolumePlanEnd"));
        }

        // ????????????????????????
        if (params.containsKey("investmentVolumeActualStart")) {
            queryWrapper.ge("investmentVolumeActual", params.get("investmentVolumeActualStart"));
        }
        if (params.containsKey("investmentVolumeActualEnd")) {
            queryWrapper.le("investmentVolumeActual", params.get("investmentVolumeActualEnd"));
        }


        if (params.containsKey("respManager")) {
            queryWrapper.like("respManager", params.get("respManager"));
        }
        if (params.containsKey("projectContact")) {
            queryWrapper.like("projectContact", params.get("projectContact"));
        }
        if (params.containsKey("projectSrartDatePlan")) {
            queryWrapper.like("projectSrartDatePlan", params.get("projectSrartDatePlan"));
        }

        // ??????????????????????????????
        if (params.containsKey("projectSrartDateActualStart")) {
            queryWrapper.ge("projectSrartDateActual", params.get("projectSrartDateActualStart"));
        }
        if (params.containsKey("projectSrartDateActualEnd")) {
            queryWrapper.le("projectSrartDateActual", params.get("projectSrartDateActualEnd"));
        }

        // ??????????????????????????????
        if (params.containsKey("projectEndDateActualStart")) {
            queryWrapper.ge("projectEndDateActual", params.get("projectEndDateActualStart"));
        }
        if (params.containsKey("projectEndDateActualEnd")) {
            queryWrapper.le("projectEndDateActual", params.get("projectEndDateActualEnd"));
        }

        if (params.containsKey("projectEndDatePlan")) {
            queryWrapper.like("projectEndDatePlan", params.get("projectEndDatePlan"));
        }
        if (params.containsKey("currentProjectStatus")) {
            queryWrapper.like("currentProjectStatus", params.get("currentProjectStatus"));
        }
        if (params.containsKey("projectReportDate")) {
            queryWrapper.like("projectReportDate", params.get("projectReportDate"));
        }
        if (params.containsKey("projectFeasibilityStudyReport")) {
            queryWrapper.like("projectFeasibilityStudyReport", params.get("projectFeasibilityStudyReport"));
        }
        if (params.containsKey("projectFeasibilityStudyReportPlan")) {
            queryWrapper.like("projectFeasibilityStudyReportPlan", params.get("projectFeasibilityStudyReportPlan"));
        }
        if (params.containsKey("committeeAuditStatus")) {
            queryWrapper.like("committeeAuditStatus", params.get("committeeAuditStatus"));
        }
        if (params.containsKey("boardAuditStatus")) {
            queryWrapper.like("boardAuditStatus", params.get("boardAuditStatus"));
        }
        if (params.containsKey("projectFinishStatus")) {
            queryWrapper.like("projectFinishStatus", params.get("projectFinishStatus"));
        }
        if (params.containsKey("projectRiskStatus")) {
            queryWrapper.like("projectRiskStatus", params.get("projectRiskStatus"));
        }
        if (params.containsKey("projectEvalStatus")) {
            queryWrapper.like("projectEvalStatus", params.get("projectEvalStatus"));
        }
        if (params.containsKey("projectSelfEvalStatus")) {
            queryWrapper.like("projectSelfEvalStatus", params.get("projectSelfEvalStatus"));
        }
        if (params.containsKey("businessUnit")) {
            queryWrapper.like("businessUnit", params.get("businessUnit"));
        }
        if (params.containsKey("productLineName")) {
            queryWrapper.like("productLineName", params.get("productLineName"));
        }
        if (params.containsKey("productLineId")) {
            queryWrapper.eq("productLineId", params.get("productLineId"));
        }
        if (params.containsKey("idAsc"))
        {
            queryWrapper.orderByAsc("id");
        }
        else
        {
            queryWrapper.orderByDesc("id");
        }
        if (params.containsKey("select"))
        {
            queryWrapper.select(InstandTool.objectToString(params.get("select")));
        }
        if (params.containsKey("groupBy"))
        {
            queryWrapper.groupBy(InstandTool.objectToString(params.get("groupBy")));
        }
        return queryWrapper;
    }

    @Override
    @Transactional
    public boolean submitUploadFile(InvestProjectList investProjectList,Map<String, Object> param) {
        int result = 0;
        // ??????bs_operate_invest_project_relation_attachment???????????????????????????????????????
        ProjectRelationAttachment projectRelationAttachment;
        if (param.containsKey("attachmentIds")) {
            String attachmentIds = (String) param.get("attachmentIds");
            List<String> attachmentIdList = Arrays.asList(attachmentIds.split(","));
            if (!ObjectUtils.isEmpty(attachmentIdList)) {
                for (String attachmentId : attachmentIdList) {
                    projectRelationAttachment = new ProjectRelationAttachment();
                    Attachment attachment = attachmentMapper.selectById(attachmentId);
                    if (ObjectUtils.isEmpty(attachment)){
                        throw new ServiceException("??????????????????????????????");
                    }
                    projectRelationAttachment.setImportDate(attachment.getImportDate());
                    // ???????????????
                    projectRelationAttachment.setFilename(attachment.getFilename());
                    // ????????????????????????
                    projectRelationAttachment.setMd5Path(attachment.getMd5Path());
                    projectRelationAttachment.setPath(attachment.getPath());
                    projectRelationAttachment.setAttachmentId(attachment.getId());
                    projectRelationAttachment.setNote(attachment.getNote());
                    projectRelationAttachment.setProjectListId(investProjectList.getId());
                    projectRelationAttachment.setConclusion(attachment.getConclusion());
                    projectRelationAttachment.setProjectName(investProjectList.getProjectName());
                    projectRelationAttachment.setYear(investProjectList.getYear());
                    projectRelationAttachment.setCompanyName(investProjectList.getCompanyName());
                    projectRelationAttachment.setCompanyId(investProjectList.getCompanyId());
                    // ????????????????????????
                    result = projectRelationAttachmentMapper.insert(projectRelationAttachment);
                }
            }
        }
        return result > 0;
    }
    @Override
    public boolean insertInvestProjectList(InvestProjectList investProjectList, HttpServletRequest request) {
        Long companyId = Long.valueOf(request.getParameter("companyId"));
        if (ObjectUtils.isEmpty(companyId)) {
            throw new ServiceException("???????????????");
        }
        String year = request.getParameter("year");
        String month = request.getParameter("month");
        String projectName = request.getParameter("projectName");

        Company company = companyMapper.selectById(companyId);
        investProjectList.setCompanyName(company.getName());
        investProjectList.setCompanyId(companyId);
        investProjectList.setCompanyShortName(company.getShortName());
        User user = GetTool.getUser();
        investProjectList.setCreatedAt(LocalDateTime.now());
        investProjectList.setCreatedId(user.getId());
        investProjectList.setCreatedName(user.getName());
        //?????????????????????????????????????????????????????????????????????????????????????????????
        QueryWrapper<InvestProjectList> investProjectListQueryWrapper = new QueryWrapper<>();
        System.out.println(companyId);
        investProjectListQueryWrapper.eq("companyId",companyId).eq("year",year).eq("month", month).eq("projectName",projectName);
        List<InvestProjectList> investProjectLists = investProjectListMapper.selectList(investProjectListQueryWrapper);
        //????????????????????? ???????????????
        if (investProjectLists.size()>0){
            throw new ServiceException("????????????????????????????????????????????????");
        }
        investProjectListMapper.insert(investProjectList);//???????????????
        return true;
    }

    @Override
    public boolean updateInvestProjectListData(InvestProjectList investProjectList, HttpServletRequest request) {
        Long companyId = Long.valueOf(request.getParameter("companyId"));
        if (ObjectUtils.isEmpty(companyId)) {
            throw new ServiceException("???????????????");
        }
        Company company = companyMapper.selectById(companyId);
        investProjectList.setCompanyName(company.getName());
        investProjectList.setCompanyId(InstandTool.integerToLong(company.getId()));
        investProjectList.setCompanyShortName(company.getShortName());
        User user = GetTool.getUser();
        investProjectList.setUpdatedAt(LocalDateTime.now());
        investProjectList.setUpdatedId(user.getId());
        investProjectList.setUpdatedName(user.getName());

        Integer year = investProjectList.getYear();
        Integer month = investProjectList.getMonth();
        String projectName = investProjectList.getProjectName();
        int result = investProjectListMapper.updateById(investProjectList);

        //?????????????????????????????????
        Integer id = Math.toIntExact(investProjectList.getId());
        String companyName = investProjectList.getCompanyName();
        QueryWrapper<InvestProjectPlanTrace> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("projectListId",id);
        List<InvestProjectPlanTrace> investProjectPlanTraces = investProjectPlanTraceMapper.selectList(queryWrapper);
        for (InvestProjectPlanTrace projectPlanTrace : investProjectPlanTraces) {
            projectPlanTrace.setProjectName(projectName);
            projectPlanTrace.setYear(year);
            projectPlanTrace.setMonth(month);
            projectPlanTrace.setCompanyName(companyName);
            investProjectPlanTraceMapper.updateById(projectPlanTrace);
        }

        if (result > 0) {
            return true;
        }
        return false;
    }


    @Override
    public List<Map<String, Object>> findInvestProjectMapListDataByParams(Map<String, Object> params) {
        QueryWrapper<InvestProjectList> queryWrapper = getQueryWrapper(params);
        return investProjectListMapper.selectMaps(queryWrapper);
    }

    @Override
    public Page<Map<String, Object>> findInvestProjectListDataMapListPerPageByParams(Map<String, Object> params) {
        QueryWrapper<InvestProjectList> queryWrapper = getQueryWrapper(params);
        int limit = (int) params.get("limit");
        int page = (int) params.get("page");
        Page<Map<String, Object>> myPage = new Page<>(page, limit);
        myPage = investProjectListMapper.selectMapsPage(myPage, queryWrapper);
        // 2023-1-03 ??????????????????????????????
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
        List<InvestProjectList> investProjectLists = investProjectListMapper.selectList(queryWrapper);
        myPage.getRecords().stream().forEach(item -> {
            Integer year = (Integer) item.get("year");
            Integer month = (Integer) item.get("month");
            String companyName = InstandTool.objectToString(item.get("companyName"));
            String projectName = InstandTool.objectToString(item.get("projectName"));
            QueryWrapper<InvestProjectPlanTrace> traceQueryWrapper = new QueryWrapper<>();
            traceQueryWrapper.eq("year", year).eq("companyName", companyName).eq("month", month).eq("projectName", projectName);
            traceQueryWrapper.orderByDesc("id");
            traceQueryWrapper.isNotNull("projectIndicators");
            List<InvestProjectPlanTrace> investProjectPlanTraces = investProjectPlanTraceMapper.selectList(traceQueryWrapper);
            if (!ObjectUtils.isEmpty(investProjectPlanTraces)){
                item.put("currentProjectStatus",investProjectPlanTraces.get(0).getProjectIndicators());
            }else {
                item.put("currentProjectStatus","??????????????????");
            }

        });
        return myPage;
    }

    /**
     * ????????????????????????????????????
     *
     * @param params
     * @return
     */
    @Override
    public Page<Map<String, Object>> findInvestProjectListDataListPerPageByParams(Map<String, Object> params) {
        QueryWrapper<InvestProjectList> queryWrapper = getQueryWrapper(params);
        int limit = (int) params.get("limit");
        int page = (int) params.get("page");
        Page<Map<String, Object>> myPage = new Page<>(page, limit);
        myPage = investProjectListMapper.selectMapsPage(myPage, queryWrapper);
        // 2023-1-03 ??????????????????????????????
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
        return myPage;
    }
}
