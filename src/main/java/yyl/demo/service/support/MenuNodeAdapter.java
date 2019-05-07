package yyl.demo.service.support;

import com.github.relucent.base.util.tree.TreeUtil.NodeAdapter;

import yyl.demo.entity.Permission;
import yyl.demo.service.model.BasicNode;

public class MenuNodeAdapter implements NodeAdapter<Permission, BasicNode> {

    public final static MenuNodeAdapter INSTANCE = new MenuNodeAdapter();

    @Override
    public BasicNode adapte(Permission model) {
        BasicNode node = new BasicNode();
        node.setId(model.getId());
        node.setLabel(model.getName());
        node.setValue(model.getValue());
        node.setType(model.getType());
        node.setIcon(model.getIcon());
        return node;
    }
}
