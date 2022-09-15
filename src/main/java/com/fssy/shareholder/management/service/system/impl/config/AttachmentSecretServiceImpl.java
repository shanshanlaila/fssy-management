package com.fssy.shareholder.management.service.system.impl.config;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.mapper.system.config.AttachmentSecretMapper;
import com.fssy.shareholder.management.pojo.manage.user.User;
import com.fssy.shareholder.management.pojo.system.config.AttachmentSecret;
import com.fssy.shareholder.management.service.system.config.AttachmentSecretService;
import com.fssy.shareholder.management.tools.common.FileAttachmentTool;
import com.fssy.shareholder.management.tools.common.InstandTool;
import com.fssy.shareholder.management.tools.constant.CommonConstant;
import com.fssy.shareholder.management.tools.exception.ServiceException;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author TerryZeng
 * @title: AttachmentSecretServiceImpl
 * @description: 销售等保密附件实现类
 * @date 2021/10/30
 */
@Service
public class AttachmentSecretServiceImpl implements AttachmentSecretService {
    /**
     * 文件附件工具类
     */
    @Autowired
    private FileAttachmentTool fileAttachmentTool;
    @Autowired
    private AttachmentSecretMapper attachmentSecretMapper;
    @Override
    public List<AttachmentSecret> findAttachmentSecretDataListByParams(Map<String, Object> params) {
        QueryWrapper<AttachmentSecret> queryWrapper = getQueryWrapper(params);
        List<AttachmentSecret> attachmentSecrets = attachmentSecretMapper
                .selectList(queryWrapper);
        return attachmentSecrets;
    }

    @Override
    public Page<AttachmentSecret> findAttachmentSecretDataListPerPageByParams(Map<String, Object> params) {
        QueryWrapper<AttachmentSecret> queryWrapper = getQueryWrapper(params).orderByDesc("createdAt");
        int limit = (int) params.get("limit");
        int page = (int) params.get("page");
        Page<AttachmentSecret> myPage = new Page<>(page, limit);
        Page<AttachmentSecret> attachmentSecretPage = attachmentSecretMapper.selectPage(myPage,
                queryWrapper);
        return attachmentSecretPage;
    }
    private QueryWrapper<AttachmentSecret> getQueryWrapper(Map<String, Object> params)
    {
        QueryWrapper<AttachmentSecret> queryWrapper = new QueryWrapper<>();
        // 附件id查询
        if (params.containsKey("id"))
        {
            queryWrapper.eq("id", params.get("id"));
        }
        // 2022-3-9 添加创建人查询
        if (params.containsKey("createdName"))
        {
            queryWrapper.like("createdName",params.get("createdName"));
        }
        // md5检查查询
        if (params.containsKey("md5Path"))
        {
            queryWrapper.eq("md5Path", params.get("md5Path"));
        }
        // 附件名称查询
        if (params.containsKey("filename"))
        {
            queryWrapper.like("filename", params.get("filename"));
        }
        // 附件状态查询
        if (params.containsKey("status"))
        {
            queryWrapper.like("status", params.get("status"));
        }
        // 附件导入状态状态查询
        if (params.containsKey("importStatus")
                && !ObjectUtils.isEmpty(params.get("importStatus")))
        {
            queryWrapper.eq("importStatus", params.get("importStatus"));
        }
        // 导入日期查询
        if (params.containsKey("importDateStart"))
        {
            queryWrapper.ge("importDate", params.get("importDateStart"));
        }
        if (params.containsKey("importDateEnd"))
        {
            queryWrapper.le("importDate", params.get("importDateEnd"));
        }
        if (params.containsKey("module"))
        {
            queryWrapper.eq("module", params.get("module"));
        }
        if(params.containsKey("ids")){
            queryWrapper.in("id", (String[]) params.get("ids"));
        }
        if (params.containsKey("createdId")) {
            queryWrapper.eq("createdId", params.get("createdId"));
        }
        if (params.containsKey("contractAttachmentIds"))
        {
            List<String> contractAttachmentIds = Arrays.asList(InstandTool.objectToString(params.get("contractAttachmentIds")).split(","));
            queryWrapper.in("id", contractAttachmentIds);
        }
        if (params.containsKey("conclusionEq"))
        {
            queryWrapper.eq("conclusion", params.get("conclusionEq"));
        }
        if (params.containsKey("orderByDesc"))
        {
            queryWrapper.orderByDesc("id");
        }
        return queryWrapper;
    }
    @Override
//    @Transactional(propagation = Propagation.NOT_SUPPORTED) // 这里不需要事务
    public boolean changeImportStatus(int status, String id) {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        UpdateWrapper<AttachmentSecret> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", id).set("importStatus", status)
                .set("updatedAt", LocalDateTime.now())
                .set("updatedId", user.getId())
                .set("updatedName", user.getName());
        int num = attachmentSecretMapper.update(null, updateWrapper);
        if (num > 0)
        {
            return true;
        }
        return false;
    }

    @Override
    public boolean changeImportStatus(int status, String id, String msg) {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        UpdateWrapper<AttachmentSecret> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", id).set("importStatus", status)
                .set("note", msg)
                .set("updatedAt", LocalDateTime.now())
                .set("updatedId", user.getId())
                .set("updatedName", user.getName());
        int num = attachmentSecretMapper.update(null, updateWrapper);
        if (num > 0)
        {
            return true;
        }
        return false;
    }

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public boolean changeImportStatus(int status, String id, String msg, String conclusion)
    {

        User user = (User) SecurityUtils.getSubject().getPrincipal();
        UpdateWrapper<AttachmentSecret> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", id).set("importStatus", status)
                .set("note", msg)
                .set("conclusion", conclusion)
                .set("updatedAt", LocalDateTime.now())
                .set("updatedId", user.getId())
                .set("updatedName", user.getName());
        int num = attachmentSecretMapper.update(null, updateWrapper);
        if (num > 0)
        {
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteAttachmentSecretById(int id) {
        AttachmentSecret deleteAttachmentSecret = attachmentSecretMapper.selectById(id);
        // 业务判断
        if (deleteAttachmentSecret
                .getImportStatus() == CommonConstant.IMPORT_RESULT_SUCCESS)
        {
            throw new ServiceException("附件已经导入，不允许删除");
        }

        // 删除附件文件
        fileAttachmentTool.deleteFileAsResource(deleteAttachmentSecret.getPath(),
                deleteAttachmentSecret.getFilename());

        int num = attachmentSecretMapper.deleteById(id);

        if (num > 0)
        {
            return true;
        }
        return false;
    }
}
