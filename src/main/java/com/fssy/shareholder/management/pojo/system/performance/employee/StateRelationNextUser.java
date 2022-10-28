package com.fssy.shareholder.management.pojo.system.performance.employee;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fssy.shareholder.management.pojo.common.BaseModel;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * *****业务部门：	绩效科		*****数据表中文名：	员工月度评价情况关联次担用户关系表		*****数据表名：	bs_performance_state_relation_next_user		*****数据表作用：	员工月度评价情况关联次担用户关系		*****变更记录：	时间         	变更人		变更内容	20220915	兰宇铧           	初始设计
 * </p>
 *
 * @author shanshan
 * @since 2022-10-28
 */
@Getter
@Setter
@TableName("bs_performance_state_relation_next_user")
public class StateRelationNextUser extends BaseModel {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 备注
     */
    @TableField("note")
    private String note;

    /**
     * 主担岗位名称
     */
    @TableField("nextRoleName")
    private String nextRoleName;

    /**
     * 主担岗位主键
     */
    @TableField("nextRoleId")
    private Long nextRoleId;

    /**
     * 主担用户名称
     */
    @TableField("nextUserName")
    private String nextUserName;

    /**
     * 主担用户主键
     */
    @TableField("nextUserId")
    private Long nextUserId;

    /**
     * 主担部门名称
     */
    @TableField("nextDepartmentName")
    private String nextDepartmentName;

    /**
     * 主担部门主键
     */
    @TableField("nextDepartmentId")
    private Long nextDepartmentId;

    /**
     * 评价说明表明细主键
     */
    @TableField("stateId")
    private Long stateId;


}
