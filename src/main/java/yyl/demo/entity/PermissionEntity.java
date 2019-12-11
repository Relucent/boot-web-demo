package yyl.demo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;
import yyl.demo.entity.basic.BaseEntity;

/**
 * 功能权限(许可)
 * @author YYL
 */
@SuppressWarnings("serial")
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "permission", autoResultMap = true)
public class PermissionEntity extends BaseEntity {

    /** 主键 */
    @TableId("id")
    private String id;

    /** 上级ID */
    @TableField("parent_id")
    private String parentId;

    /** 类别 */
    @TableField("type")
    private Integer type;

    /** 名称 */
    @TableField("name")
    private String name;

    /** 编码 */
    @TableField("code")
    private String code;

    /** 访问路径 */
    @TableField("path")
    private String path;

    /** 图标 */
    @TableField("icon")
    private String icon;

    /** 备注 */
    @TableField("remark")
    private String remark;

    /** 排序号 */
    @TableField("ordinal")
    private String ordinal;

    /** ID路径 */
    @TableField("id_path")
    private String idPath;
}
