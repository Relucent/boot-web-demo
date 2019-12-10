package yyl.demo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;
import yyl.demo.entity.basic.BaseEntity;

/**
 * 角色权限关联
 * @author YYL
 */
@SuppressWarnings("serial")
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "role_permission", autoResultMap = true)
public class RolePermissionEntity extends BaseEntity {

    /** 主键 */
    @TableId("id")
    private String id;
    /** 角色组ID */
    @TableField("role_id")
    private String roleId;
    /** 功能ID */
    @TableField("permission_id")
    private String permissionId;
}
