package yyl.demo.service.support;

import com.github.relucent.base.util.tree.TreeUtil.NodeAdapter;

import yyl.demo.entity.Department;
import yyl.demo.service.model.BasicNode;

public class DeptNodeAdapter implements NodeAdapter<Department, BasicNode> {

    public final static DeptNodeAdapter INSTANCE = new DeptNodeAdapter();

    @Override
    public BasicNode adapte(Department model) {
        BasicNode node = new BasicNode();
        node.setId(model.getId());
        node.setLabel(model.getName());
        return node;
    }
}
