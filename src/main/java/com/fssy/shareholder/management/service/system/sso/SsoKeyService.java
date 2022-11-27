package com.fssy.shareholder.management.service.system.sso;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.pojo.system.sso.SsoKey;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * *****业务部门：	IT科		*****数据表中文名：	附件列表		*****数据表名：	bs_common_attachment		*****数据表作用：	各导入模块（场景）的附件物理保存		*****变更记录：	时间         	变更人		变更内容	20220915	兰宇铧           	初始设计		时间                    变更人                           变更内容	20221109          施镇脑                      更改note字段类型为longtext 服务类
 * </p>
 *
 * @author TerryZeng
 * @since 2022-11-27
 */
public interface SsoKeyService extends IService<SsoKey> {

    /**
     * 重新生成单点登录的密钥对
     * @param ssoKey 单点登录密钥对实体
     * @return 单点登录密钥对实体
     */
    public SsoKey insertSsoKey(SsoKey ssoKey);

    /**
     *更新 ssoKey
     * @param ssoKey
     * @return
     */
    public boolean updateSsoKey(SsoKey ssoKey);

    /**
     *通过查询条件，查询单点登录密钥 数据
     * @param params
     * @return 单点登录密钥数据
     */
    List<SsoKey> findSsoKeyDataParams(Map<String,Object> params);

    /**
     *删除单点登录密钥数据
     * @return
     */
    public boolean deleteSsoKeyById(int id);

    /**
     * 通过查询条件，查询 单点登录密钥 数据分页列表
     * @param params 查询条件
     * @return 单点登录密钥分页数据表
     */
    Page<SsoKey> findSsoKeyDataPerPageByParams(Map<String,Object> params);
}
