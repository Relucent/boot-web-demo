package yyl.demo.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import yyl.demo.common.standard.Auditable;
import yyl.demo.common.standard.Idable;
import yyl.demo.common.standard.Ordinal;

/**
 * 组织机构
 * @author _yyl
 */
@SuppressWarnings("serial")
@Data
public class Department implements Idable, Auditable, Ordinal, Serializable {

    /** 主键 */
    private String id;
    /** 上级ID */
    private String parentId;

    /** 名称 */
    private String name;
    /** 备注 */
    private String remark;

    /** 排序 */
    private String ordinal;
    /** 是否删除的 */
    private Integer deleted;

    /** ID路径 */
    private String idPath;

    /** 创建者 */
    private String createdBy;
    /** 创建时间 */
    private Date createdAt;
    /** 最后修改者 */
    private String updatedBy;
    /** 最后修改时间 */
    private Date updatedAt;

    /** ~上级功能权限名称 */
    private transient String parentName;
}
