package com.fssy.shareholder.management.pojo.system.sso;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fssy.shareholder.management.pojo.common.BaseModel;
import java.io.Serializable;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * *****业务部门：	IT科		*****数据表中文名：	附件列表		*****数据表名：	bs_common_attachment		*****数据表作用：	各导入模块（场景）的附件物理保存		*****变更记录：	时间         	变更人		变更内容	20220915	兰宇铧           	初始设计		时间                    变更人                           变更内容	20221109          施镇脑                      更改note字段类型为longtext
 * </p>
 *
 * @author TerryZeng
 * @since 2022-11-27
 */
@Data
@TableName("bs_sso_key")
public class SsoKey extends BaseModel {
    private static final long serialVersionUID = 4244662547984740203L;


    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 备注
     */
    @TableField("note")
    private String note;

    /**
     * 文件状态 0：失效 1:有效可以下载，默认为1
     */
    @TableField("status")
    private Integer status;

    /**
     * 公钥
     */
    @TableField("publicKey")
    private String publicKey;

    /**
     * 私钥
     */
    @TableField("privateKey")
    private String privateKey;


}
