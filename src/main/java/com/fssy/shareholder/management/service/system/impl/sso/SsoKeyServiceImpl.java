package com.fssy.shareholder.management.service.system.impl.sso;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fssy.shareholder.management.pojo.system.sso.SsoKey;
import com.fssy.shareholder.management.mapper.system.sso.SsoKeyMapper;
import com.fssy.shareholder.management.service.system.sso.SsoKeyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * *****业务部门：	IT科		*****数据表中文名：	附件列表		*****数据表名：	bs_common_attachment		*****数据表作用：	各导入模块（场景）的附件物理保存		*****变更记录：	时间         	变更人		变更内容	20220915	兰宇铧           	初始设计		时间                    变更人                           变更内容	20221109          施镇脑                      更改note字段类型为longtext 服务实现类
 * </p>
 *
 * @author TerryZeng
 * @since 2022-11-27
 */
@Service
public class SsoKeyServiceImpl extends ServiceImpl<SsoKeyMapper, SsoKey> implements SsoKeyService {

    @Autowired
    private SsoKeyMapper ssoKeyMapper;

    //    传入参数查询
    private QueryWrapper<SsoKey> getQueryWrapper(Map<String, Object> params) {
        QueryWrapper<SsoKey> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id","publicKey");
        if (params.containsKey("publicKey")) {
            queryWrapper.like("publicKey", params.get("publicKey"));
        }
        return queryWrapper;
    }

    @Override
    public SsoKey insertSsoKey(SsoKey ssoKey) {
        ssoKeyMapper.insert(ssoKey);
        return ssoKey;
    }

    @Override
    public boolean updateSsoKey(SsoKey ssoKey) {
        int result = ssoKeyMapper.updateById(ssoKey);
        if (result > 0) {
            return true;
        }
        return false;
    }

    @Override
    public List<SsoKey> findSsoKeyDataParams(Map<String, Object> params) {
        QueryWrapper<SsoKey> queryWrapper = getQueryWrapper(params);
        List<SsoKey> ssoKeyList = ssoKeyMapper.selectList(queryWrapper);
        return ssoKeyList;
    }

    @Override
    public boolean deleteSsoKeyById(int id) {
        int result = ssoKeyMapper.deleteById(id);
        if (result > 0) {
            return true;
        }
        return false;
    }

    @Override
    public Page<SsoKey> findSsoKeyDataPerPageByParams(Map<String, Object> params) {
        QueryWrapper<SsoKey> queryWrapper = getQueryWrapper(params);
        int limit = (int) params.get("limit");
        int page = (int) params.get("page");
        Page<SsoKey> myPage=new Page<>(page,limit);
        Page<SsoKey> ssoKeyPage=ssoKeyMapper.selectPage(myPage,queryWrapper);
        return  ssoKeyPage;
    }
}
