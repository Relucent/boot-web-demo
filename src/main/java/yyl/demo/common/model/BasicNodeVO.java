package yyl.demo.common.model;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * 菜单树节点模型
 */
@SuppressWarnings("serial")
@Data
public class BasicNodeVO implements Serializable {
    /** 主键 */
    private String id;
    /** 名称 */
    private String label;
    /** 类别 */
    private Integer type;
    /** 内容(访问路径) */
    private String value;
    /** 子节点 */
    private List<BasicNodeVO> children;
}
