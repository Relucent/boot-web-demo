package yyl.demo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;
import yyl.demo.entity.basic.BaseEntity;

/** 文件信息 */
@SuppressWarnings("serial")
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "file", autoResultMap = true)
public class FileEntity extends BaseEntity {

    /** 文件ID */
    @TableId("id")
    private String id;

    /** 文件名 */
    @TableField("name")
    private String name;

    /** 文件扩展名 */
    @TableField("extension")
    private String extension;

    /** 文件大小 */
    @TableField("length")
    private Long length;

    /** 文件内容类型 */
    @TableField("content_type")
    private String contentType;

    /** 文件存储路径 */
    @TableField("path")
    private String path;

    /** 文件存储库 */
    @TableField("store")
    private String store;
}
