package yyl.demo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;
import yyl.demo.entity.basic.BaseEntity;

/** 组织机构 */
@SuppressWarnings("serial")
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "organization", autoResultMap = true)
public class OrganizationEntity extends BaseEntity {

    /** 主键 */
    @TableId("id")
    private String id;

    /** 上级机构ID */
    @TableField("parent_id")
    private String parentId;

    /** 机构名称 */
    @TableField("name")
    private String name;

    /** 备注 */
    @TableField("remark")
    private String remark;

    /** 排序号 */
    @TableField("ordinal")
    private Integer ordinal;

    /** 节点路径 */
    @TableField("id_path")
    private String idPath;
}