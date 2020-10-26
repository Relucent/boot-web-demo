package yyl.demo.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import yyl.demo.common.standard.Auditable;
import yyl.demo.common.standard.Idable;

/**
 * 用户角色关联
 * @author YYL
 */
@SuppressWarnings("serial")
@Data
@TableName(value = "user_role", autoResultMap = true)
public class UserRole implements Idable, Auditable, Serializable {

    /** 主键 */
    @TableId(value = "id", type = IdType.INPUT)
    private String id;
    /** 用户主键id */
    @TableField("user_id")
    private String userId;
    /** 角色id */
    @TableField("role_id")
    private String roleId;

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
}
