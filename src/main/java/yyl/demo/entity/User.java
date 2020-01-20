package yyl.demo.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import yyl.demo.common.standard.Auditable;
import yyl.demo.common.standard.Idable;

/**
 * 系统用户
 * @author _yyl
 */
@SuppressWarnings("serial")
@Data
public class User implements Idable, Auditable, Serializable {

    /** 主键 */
    private String id;
    /** 登录名 */
    private String username;
    /** 登录密码 */
    private String password;

    /** 所属机构 */
    private String departmentId;
    /** 所属机构名称 */
    private String departmentName;
    /** 用户姓名 */
    private String name;
    /** 备注 */
    private String remark;

    /** 是否可用 */
    private Integer enabled;
    /** 是否删除的 */
    private Integer deleted;

    /** 创建者 */
    private String createdBy;
    /** 创建时间 */
    private Date createdAt;
    /** 最后修改者 */
    private String updatedBy;
    /** 最后修改时间 */
    private Date updatedAt;
}
