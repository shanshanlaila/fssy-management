package com.fssy.shareholder.management.mapper.system.operate.company;

import com.fssy.shareholder.management.pojo.system.operate.company.CompanyProfile;
import com.fssy.shareholder.management.mapper.manage.common.MyBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 部门：		时间：2022/7/15		表名：公司简介		表名：company_profile		用途：存储公司的基本信息和简介 Mapper 接口
 * </p>
 *
 * @author 农浩
 * @since 2022-12-13
 */
@Mapper
public interface CompanyProfileMapper extends MyBaseMapper<CompanyProfile> {

}
