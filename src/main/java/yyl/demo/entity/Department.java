package yyl.demo.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import yyl.demo.common.standard.IdEntity;
import yyl.demo.common.standard.Ordinal;

/**
 * 组织机构
 * @author YYL
 */
@SuppressWarnings("serial")
@Data
@TableName(value = "department", autoResultMap = true)
public class Department implements IdEntity, Ordinal {

    /** 主键 */
    @TableId(value = "id", type = IdType.INPUT)
    private String id;
    /** 上级ID */
    @TableField("parent_id")
    private String parentId;

    /** 名称 */
    @TableField("name")
    private String name;
    /** 备注 */
    @TableField("remark")
    private String remark;

    /** 排序 */
    @TableField("ordinal")
    private String ordinal;

    /** ID路径 */
    @TableField("id_path")
    private String idPath;

    /** 版本号 */
    @TableField("version")
    private Long version;
    /** 删除标记 */
    @TableField("deleted")
    @TableLogic
    private Integer deleted;
    /** 创建者 */
    @TableField("created_by")
    private String createdBy;
    /** 创建时间 */
    @TableField("created_date")
    private Date createdDate;
    /** 最后修改者 */
    @TableField("modified_by")
    private String modifiedBy;
    /** 最后修改时间 */
    @TableField("modified_date")
    private Date modifiedDate;

    /** ~上级功能权限名称 */
    @TableField(exist = false)
    private String parentName;
}
