package com.fssy.shareholder.management.mapper.system.sso;

import com.fssy.shareholder.management.pojo.system.sso.SsoKey;
import com.fssy.shareholder.management.mapper.manage.common.MyBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * *****业务部门：	IT科		*****数据表中文名：	附件列表		*****数据表名：	bs_common_attachment		*****数据表作用：	各导入模块（场景）的附件物理保存		*****变更记录：	时间         	变更人		变更内容	20220915	兰宇铧           	初始设计		时间                    变更人                           变更内容	20221109          施镇脑                      更改note字段类型为longtext Mapper 接口
 * </p>
 *
 * @author TerryZeng
 * @since 2022-11-27
 */
@Mapper
public interface SsoKeyMapper extends MyBaseMapper<SsoKey> {

}
