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
 * 角色权限关联
 * @author YYL
 */
@SuppressWarnings("serial")
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "role_permission", autoResultMap = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RolePermissionEntity extends BaseEntity {

    /** 主键 */
    @TableId("id")
    String id;
    /** 角色组ID */
    @TableField("role_id")
    String roleId;
    /** 功能ID */
    @TableField("permission_id")
    String permissionId;
}
