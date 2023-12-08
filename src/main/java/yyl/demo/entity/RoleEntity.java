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
 * 系统角色
 * @author YYL
 */
@SuppressWarnings("serial")
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "role", autoResultMap = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleEntity extends BaseEntity {

    /** 主键 */
    @TableId("id")
    String id;
    /** 编码 */
    @TableField("code")
    String code;
    /** 名称 */
    @TableField("name")
    String name;
    /** 备注 */
    @TableField("remark")
    String remark;
}
