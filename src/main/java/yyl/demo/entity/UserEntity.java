package yyl.demo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import yyl.demo.entity.basic.BaseEntity;

/**
 * 系统用户
 * @author YYL
 */
@SuppressWarnings("serial")
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "user", autoResultMap = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserEntity extends BaseEntity {

    /** 主键 */
    @TableId("id")
    String id;
    /** 登录名 */
    @TableField("username")
    String username;
    /** 登录密码 */
    @TableField("password")
    String password;

    /** 所属机构 */
    @TableField("organization_id")
    String organizationId;
    /** 用户姓名 */
    @TableField("realname")
    String realname;

    /** 性别 */
    @TableField("sex")
    String sex;

    /** 电话 */
    @TableField("phone")
    String phone;

    /** 备注 */
    @TableField("remark")
    String remark;

    /** 是否可用 */
    @TableField("enabled")
    Integer enabled;
}
