package yyl.demo.entity;

import java.io.Serializable;

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
    /** 更新时间 */
    private String updatedAt;
}
