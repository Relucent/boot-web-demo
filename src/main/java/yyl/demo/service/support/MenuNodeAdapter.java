package yyl.demo.service.support;

import com.github.relucent.base.common.tree.TreeUtil.NodeAdapter;

import yyl.demo.common.model.BasicNodeVO;
import yyl.demo.entity.Permission;

public class MenuNodeAdapter implements NodeAdapter<Permission, BasicNodeVO> {

    public final static MenuNodeAdapter INSTANCE = new MenuNodeAdapter();

    @Override
    public BasicNodeVO adapte(Permission model) {
        BasicNodeVO node = new BasicNodeVO();
        node.setId(model.getId());
        node.setLabel(model.getName());
        node.setValue(model.getValue());
        node.setType(model.getType());
        return node;
    }
}
