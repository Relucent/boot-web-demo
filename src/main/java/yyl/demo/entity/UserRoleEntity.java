package yyl.demo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;
import yyl.demo.entity.basic.BaseEntity;

/**
 * 用户角色关联
 * @author YYL
 */
@SuppressWarnings("serial")
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "user_role", autoResultMap = true)
public class UserRoleEntity extends BaseEntity {
    /** 主键 */
    @TableId("id")
    private String id;
    /** 用户主键id */
    @TableField("user_id")
    private String userId;
    /** 角色id */
    @TableField("role_id")
    private String roleId;
}
