package yyl.demo.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import yyl.demo.common.standard.Auditable;
import yyl.demo.common.standard.Idable;

/**
 * 角色权限关联
 * @author _yyl
 */
@SuppressWarnings("serial")
@Data
public class RolePermission implements Idable, Auditable, Serializable {

    /** 主键 */
    private String id;
    /** 角色组ID */
    private String roleId;
    /** 功能ID */
    private String permissionId;

    /** 创建者 */
    private String createdBy;
    /** 创建时间 */
    private Date createdDate;
    /** 最后修改者 */
    private String lastModifiedBy;
    /** 最后修改时间 */
    private Date lastModifiedDate;
}
