package com.fssy.shareholder.management.service.system.config;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.pojo.system.config.AttachmentSecret;

import java.util.List;
import java.util.Map;

/**
 * @author TerryZeng
 * @title: AttachmentSecretService
 * @description: 销售合同等保密附件服务接口
 * @date 2021/10/30
 */

public interface AttachmentSecretService {
    /**
     * 通过查询条件查询附件列表
     *
     * @param params 查询条件
     * @return 附件列表
     */
    List<AttachmentSecret> findAttachmentSecretDataListByParams(Map<String, Object> params);
    /**
     * 通过查询条件分页查询附件列表
     *
     * @param params 查询条件
     * @return 附件分页数据
     */
    Page<AttachmentSecret> findAttachmentSecretDataListPerPageByParams(
            Map<String, Object> params);

    /**
     * 改变导入状态
     *
     * @param status 导入状态
     * @param id     附件表主键
     * @return
     */
    boolean changeImportStatus(int status, String id);

    /**
     * 改变导入状态并写入备注信息
     *
     * @param status 导入状态
     * @param id     附件表主键
     * @param msg    记录的信息
     * @return
     */
    boolean changeImportStatus(int status, String id, String msg);

    /**
     *
     * @param status 导入状态
     * @param id 附件表主键
     * @param msg 记录的信息
     * @param conclusion 导入情况总述
     * @return
     */
    boolean changeImportStatus(int status,String id,String msg,String conclusion);

    /**
     * 根据附件表主键删除附件
     *
     * @param id 附件表主键
     * @return
     */
    boolean deleteAttachmentSecretById(int id);
}
