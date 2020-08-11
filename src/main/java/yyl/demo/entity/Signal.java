package yyl.demo.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import yyl.demo.common.standard.Idable;

/**
 * 信号
 */
@SuppressWarnings("serial")
@Data
public class Signal implements Idable, Serializable {
	/** 主键(名称) */
	private String id;
	/** 计数 */
	private Long value;

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
}
