package yyl.demo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;
import yyl.demo.entity.basic.BaseEntity;

/**
 * 系统角色
 * @author YYL
 */
@SuppressWarnings("serial")
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "role", autoResultMap = true)
public class RoleEntity extends BaseEntity {

    /** 主键 */
    @TableId("id")
    private String id;
    /** 编码 */
    @TableField("code")
    private String code;
    /** 名称 */
    @TableField("name")
    private String name;
    /** 备注 */
    @TableField("remark")
    private String remark;
}
