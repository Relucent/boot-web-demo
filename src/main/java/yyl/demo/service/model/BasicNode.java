package yyl.demo.service.model;

import java.io.Serializable;
import java.util.List;

import com.github.relucent.base.util.tree.Node;

import lombok.Data;

/**
 * 菜单树节点模型
 */
@SuppressWarnings("serial")
@Data
public class BasicNode implements Node<BasicNode>, Serializable {
    /** 主键 */
    private String id;
    /** 名称 */
    private String label;
    /** 类别 */
    private Integer type;
    /** 内容(访问路径) */
    private String value;
    /** 图标 */
    private String icon;
    /** 子节点 */
    private List<BasicNode> children;
}
