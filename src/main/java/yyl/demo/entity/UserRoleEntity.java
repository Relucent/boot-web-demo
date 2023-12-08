package yyl.demo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import yyl.demo.entity.basic.BaseEntity;

/**
 * 用户角色关联
 * @author YYL
 */
@SuppressWarnings("serial")
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "user_role", autoResultMap = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRoleEntity extends BaseEntity {
    /** 主键 */
    @TableId("id")
    String id;
    /** 用户主键id */
    @TableField("user_id")
    String userId;
    /** 角色id */
    @TableField("role_id")
    String roleId;
}
