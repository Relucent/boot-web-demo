package yyl.demo.service.support;

import com.github.relucent.base.common.tree.TreeUtil.NodeAdapter;

import yyl.demo.common.model.BasicNodeVO;
import yyl.demo.entity.Department;

public class DepartmentNodeAdapter implements NodeAdapter<Department, BasicNodeVO> {

    public final static DepartmentNodeAdapter INSTANCE = new DepartmentNodeAdapter();

    @Override
    public BasicNodeVO adapte(Department model) {
        BasicNodeVO node = new BasicNodeVO();
        node.setId(model.getId());
        node.setLabel(model.getName());
        return node;
    }
}
