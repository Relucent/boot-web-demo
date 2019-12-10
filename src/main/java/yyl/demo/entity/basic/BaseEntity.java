package yyl.demo.entity.basic;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;

import lombok.Data;
import yyl.demo.common.standard.Auditable;

/**
 * 实体模型基类
 */
@SuppressWarnings("serial")
@Data
public abstract class BaseEntity implements Auditable, Serializable {
    /** 版本号 */
    @TableField("version")
    private Long version;
    /** 删除标记 */
    @TableField("deleted")
    private Integer deleted;
    /** 创建者 */
    @TableField("created_by")
    private String createdBy;
    /** 创建时间 */
    @TableField("created_at")
    private Date createdAt;
    /** 最后修改者 */
    @TableField("updated_by")
    private String updatedBy;
    /** 最后修改时间 */
    @TableField("updated_at")
    private Date updatedAt;
}