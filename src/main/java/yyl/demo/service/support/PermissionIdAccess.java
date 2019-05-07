package yyl.demo.service.support;

import com.github.relucent.base.util.tree.TreeUtil.IdAccess;

import yyl.demo.entity.Permission;

public class PermissionIdAccess implements IdAccess<Permission, String> {

    public final static PermissionIdAccess INSTANCE = new PermissionIdAccess();

    @Override
    public String getId(Permission model) {
        return model.getId();
    }

    @Override
    public String getParentId(Permission model) {
        return model.getParentId();
    }
}
