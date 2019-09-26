package yyl.demo.service.support;

import com.github.relucent.base.util.tree.TreeUtil.IdAccess;

import yyl.demo.entity.Department;

public class DepartmentIdAccess implements IdAccess<Department, String> {

    public final static DepartmentIdAccess INSTANCE = new DepartmentIdAccess();

    @Override
    public String getId(Department model) {
        return model.getId();
    }

    @Override
    public String getParentId(Department model) {
        return model.getParentId();
    }

    @Override
    public void setIdPath(Department node, String idPath) {
        node.setIdPath(idPath);
    }
}
