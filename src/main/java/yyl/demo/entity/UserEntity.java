package yyl.demo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;
import yyl.demo.entity.basic.BaseEntity;

/**
 * 系统用户
 * @author YYL
 */
@SuppressWarnings("serial")
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "user", autoResultMap = true)
public class UserEntity extends BaseEntity {

    /** 主键 */
    @TableId("id")
    private String id;
    /** 登录名 */
    @TableField("username")
    private String username;
    /** 登录密码 */
    @TableField("password")
    private String password;

    /** 所属机构 */
    @TableField("organization_id")
    private String organizationId;
    /** 用户姓名 */
    @TableField("realname")
    private String realname;

    /** 性别 */
    @TableField("sex")
    private String sex;

    /** 电话 */
    @TableField("phone")
    private String phone;

    /** 备注 */
    @TableField("remark")
    private String remark;

    /** 是否可用 */
    @TableField("enabled")
    private Integer enabled;
}
