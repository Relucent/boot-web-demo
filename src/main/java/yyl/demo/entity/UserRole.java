package yyl.demo.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import yyl.demo.common.standard.Auditable;
import yyl.demo.common.standard.Idable;

/**
 * 用户角色关联
 * @author _yyl
 */
@SuppressWarnings("serial")
@Data
public class UserRole implements Idable, Auditable, Serializable {

    /** 主键 */
    private String id;
    /** 用户主键id */
    private String userId;
    /** 角色id */
    private String roleId;

    /** 创建者 */
    private String createdBy;
    /** 创建时间 */
    private Date createdAt;
    /** 修改者 */
    private String updatedBy;
    /** 修改时间 */
    private Date updatedAt;
}
