package yyl.demo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import yyl.demo.entity.basic.BaseEntity;

/** 组织机构 */
@SuppressWarnings("serial")
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "organization", autoResultMap = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrganizationEntity extends BaseEntity {

    /** 主键 */
    @TableId("id")
    String id;

    /** 上级机构ID */
    @TableField("parent_id")
    String parentId;

    /** 机构名称 */
    @TableField("name")
    String name;

    /** 备注 */
    @TableField("remark")
    String remark;

    /** 排序号 */
    @TableField("ordinal")
    Integer ordinal;

    /** 节点路径 */
    @TableField("id_path")
    String idPath;
}