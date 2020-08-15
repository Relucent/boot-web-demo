package yyl.demo.service.support;

import com.github.relucent.base.common.tree.TreeUtil.NodeFilter;

import yyl.demo.common.constant.PermissionTypes;
import yyl.demo.entity.Permission;

/**
 * 功能权限类型层级过滤器
 */
public class PermissionTypeFilter implements NodeFilter<Permission> {

    public static final PermissionTypeFilter MODULE_TYPE_FILTER = new PermissionTypeFilter(PermissionTypes.MODULE);
    public static final PermissionTypeFilter MENU_TYPE_FILTER = new PermissionTypeFilter(PermissionTypes.MENU);
    public static final PermissionTypeFilter BUTTON_TYPE_FILTER = new PermissionTypeFilter(PermissionTypes.BUTTON);

    public static NodeFilter<Permission> getInstance(Integer typeLevel) {
        switch (typeLevel.intValue()) {
        case 1:
            return MODULE_TYPE_FILTER;
        case 2:
            return MENU_TYPE_FILTER;
        case 3:
            return BUTTON_TYPE_FILTER;
        default:
            return new PermissionTypeFilter(-1);
        }
    }

    private final Integer type;

    public PermissionTypeFilter(Integer type) {
        this.type = type;
    }

    @Override
    public boolean accept(Permission model, int depth, boolean leaf) {
        return model.getType().intValue() <= type.intValue();
    }
}
