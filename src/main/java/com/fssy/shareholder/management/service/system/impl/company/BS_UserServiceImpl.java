package com.fssy.shareholder.management.service.system.impl.company;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fssy.shareholder.management.mapper.system.company.BS_UserMapper;
import com.fssy.shareholder.management.pojo.system.company.BS_User;
import com.fssy.shareholder.management.service.system.company.BS_UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * <p>
 * 基础-员工表	 服务实现类
 * </p>
 *
 * @author 农浩
 * @since 2023-02-16
 */
@Service
public class BS_UserServiceImpl extends ServiceImpl<BS_UserMapper, BS_User> implements BS_UserService {
    @Autowired
    private BS_UserMapper bs_userMapper;

    @Override
    public Page<BS_User> findDataListByParams(Map<String, Object> params) {
        QueryWrapper<BS_User> queryWrapper = getQueryWrapper(params).orderByDesc("id");
        Page<BS_User> myPage = new Page<>((int) params.get("page"), (int) params.get("limit"));
        return bs_userMapper.selectPage(myPage, queryWrapper);
    }
    private QueryWrapper<BS_User> getQueryWrapper(Map<String,Object> params) {
        QueryWrapper<BS_User> queryWrapper = new QueryWrapper<>();
        if (params.containsKey("id")) {
            queryWrapper.eq("id",params.get("id"));
        }
        return queryWrapper;
    }
}
