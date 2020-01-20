package yyl.demo.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import yyl.demo.common.standard.Auditable;
import yyl.demo.common.standard.Idable;

/**
 * 系统角色
 * @author _yyl
 */
@SuppressWarnings("serial")
@Data
public class Role implements Idable, Auditable, Serializable {

    /** 主键 */
    private String id;
    /** 名称 */
    private String name;
    /** 备注 */
    private String remark;

    /** 创建者 */
    private String createdBy;
    /** 创建时间 */
    private Date createdAt;
    /** 最后修改者 */
    private String updatedBy;
    /** 最后修改时间 */
    private Date updatedAt;
}
