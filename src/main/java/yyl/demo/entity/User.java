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
 * 系统用户
 * @author YYL
 */
@SuppressWarnings("serial")
@Data
@TableName(value = "user", autoResultMap = true)
public class User implements Idable, Auditable, Serializable {

    /** 主键 */
    @TableId(value = "id", type = IdType.AUTO)
    private String id;
    /** 登录名 */
    @TableField("username")
    private String username;
    /** 登录密码 */
    @TableField("password")
    private String password;

    /** 所属部门 */
    @TableField("department_id")
    private String departmentId;
    /** 用户姓名 */
    @TableField("name")
    private String name;
    /** 备注 */
    @TableField("remark")
    private String remark;

    /** 是否可用 */
    @TableField("enabled")
    private Integer enabled;

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
