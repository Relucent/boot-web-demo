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
 * 功能权限(许可)
 * @author YYL
 */
@SuppressWarnings("serial")
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "permission", autoResultMap = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PermissionEntity extends BaseEntity {

    /** 主键 */
    @TableId("id")
    String id;

    /** 上级ID */
    @TableField("parent_id")
    String parentId;

    /** 类别 */
    @TableField("type")
    Integer type;

    /** 名称 */
    @TableField("name")
    String name;

    /** 编码 */
    @TableField("code")
    String code;

    /** 访问路径 */
    @TableField("path")
    String path;

    /** 图标 */
    @TableField("icon")
    String icon;

    /** 备注 */
    @TableField("remark")
    String remark;

    /** 排序号 */
    @TableField("ordinal")
    String ordinal;

    /** ID路径 */
    @TableField("id_path")
    String idPath;
}
