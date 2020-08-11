package yyl.demo.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import yyl.demo.common.standard.Auditable;
import yyl.demo.common.standard.Idable;
import yyl.demo.common.standard.Ordinal;

/**
 * 功能权限
 * @author _yyl
 */
@SuppressWarnings("serial")
@Data
public class Permission implements Idable, Auditable, Ordinal, Serializable {

	/** 主键 */
	private String id;
	/** 上级功能权限ID */
	private String parentId;

	/** 名称 */
	private String name;
	/** 备注 */
	private String remark;

	/** 类别 */
	private Integer type;
	/** 内容(访问路径) */
	private String value;

	/** 图标 */
	private String icon;
	/** 排序 */
	private String ordinal;
	/** ID路径 */
	private String idPath;

	/** 版本号 */
	private Long version;
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

	/** ~上级功能权限名称 */
	private transient String parentName;
}
