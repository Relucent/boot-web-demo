package yyl.demo.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.relucent.base.plug.mybatis.MybatisHelper;
import com.github.relucent.base.plug.security.Principal;
import com.github.relucent.base.plug.security.Securitys;
import com.github.relucent.base.util.expection.ExceptionHelper;
import com.github.relucent.base.util.page.Page;
import com.github.relucent.base.util.page.Pagination;
import com.github.relucent.base.util.tree.TreeUtil;
import com.github.relucent.base.util.tree.TreeUtil.NodeFilter;

import yyl.demo.common.BaseConstants.Ids;
import yyl.demo.common.BaseConstants.Symbols;
import yyl.demo.common.identifier.IdHelper;
import yyl.demo.common.standard.AuditableUtil;
import yyl.demo.entity.Permission;
import yyl.demo.mapper.PermissionMapper;
import yyl.demo.mapper.RolePermissionMapper;
import yyl.demo.service.model.BasicNode;
import yyl.demo.service.support.MenuNodeAdapter;
import yyl.demo.service.support.PermissionIdAccess;
import yyl.demo.service.support.PermissionTypeFilter;

/**
 * 功能权限管理
 */
@Transactional
@Service
public class PermissionService {

    // ==============================Fields===========================================
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private PermissionMapper permissionMapper;

    @Autowired
    private RolePermissionMapper rolePermissionMapper;

    @Autowired
    private Securitys securitys;

    // ==============================Methods==========================================
    /**
     * 新增功能权限
     * @param permission 功能权限
     */
    public void insert(Permission permission) {

        validate(permission);

        Principal principal = securitys.getPrincipal();

        Permission entity = new Permission();

        IdHelper.setIfEmptyId(entity);
        entity.setName(permission.getName());
        entity.setRemark(permission.getRemark());
        entity.setType(permission.getType());
        entity.setValue(permission.getValue());
        entity.setOrdinal(ObjectUtils.defaultIfNull(permission.getOrdinal(), "9999"));
        entity.setIdPath(forceGetIdPath(entity));

        AuditableUtil.setCreated(entity, principal);

        permissionMapper.insert(entity);
    }

    /**
     * 删除权限
     * @param id 权限ID
     */
    public void deleteById(String id) {
        if (permissionMapper.countByParentId(id) != 0) {
            throw ExceptionHelper.prompt("该功能存在子节点，不能被直接删除");
        }
        permissionMapper.deleteById(id);
        rolePermissionMapper.deleteByPermissionId(id);
    }

    /**
     * 修改功能权限
     * @param permission 功能权限
     */
    public void update(Permission permission) {

        validate(permission);

        Principal principal = securitys.getPrincipal();

        Permission entity = permissionMapper.getById(permission.getId());

        if (entity == null) {
            throw ExceptionHelper.prompt("数据不存在");
        }

        String originalIdPath = entity.getIdPath();

        entity.setName(permission.getName());
        entity.setRemark(permission.getRemark());
        entity.setType(permission.getType());
        entity.setValue(permission.getValue());
        entity.setOrdinal(ObjectUtils.defaultIfNull(permission.getOrdinal(), "9999"));
        entity.setIdPath(forceGetIdPath(entity));
        AuditableUtil.setLastModified(entity, principal);

        permissionMapper.update(entity);

        // IdPath发生更改, 更新子节点
        if (!Objects.equals(originalIdPath, entity.getIdPath())) {
            updateChildrenIdPath(entity);
        }
    }


    /**
     * 查询功能权限
     * @param id 功能权限ID
     * @return 功能权限
     */
    public Permission getById(String id) {
        Permission entity = permissionMapper.getById(id);
        if (entity == null) {
            throw ExceptionHelper.prompt("数据不存在或者已经失效");
        }
        String parentId = entity.getParentId();
        if (Ids.ROOT_ID.equals(parentId)) {
            entity.setParentName("/");
        } else {
            Permission parentEntity = permissionMapper.getById(parentId);
            entity.setParentName(parentEntity == null ? "[未知]" : parentEntity.getName());
        }
        return entity;
    }

    /**
     * 分页查询
     * @param pagination 分页条件
     * @param condition 查询条件
     * @return 查询结果
     */
    public Page<Permission> pagedQuery(Pagination pagination, Permission condition) {
        return MybatisHelper.selectPage(pagination, () -> permissionMapper.findBy(condition));
    }


    /**
     * 获得菜单树
     * @param rootId 根节点
     * @param typeLevel 类型级别
     * @return 菜单树
     */
    public List<BasicNode> getMenuTree(String rootId, Integer typeLevel) {
        Principal principal = securitys.getPrincipal();
        List<Permission> entities = permissionMapper.findAll();
        NodeFilter<Permission> filter = PermissionTypeFilter.getInstance(typeLevel);
        if (!Ids.ADMIN_ID.equals(principal.getUserId())) {
            String[] permissionIds = principal.getPermissionIds();
            filter = filter.and(new PermissionIdsFilter(permissionIds));
        }
        return TreeUtil.buildTree(rootId, entities, MenuNodeAdapter.INSTANCE, filter, PermissionIdAccess.INSTANCE);
    }

    /**
     * 获得TOP菜单树
     * @return 菜单树(仅一层)
     */
    public List<BasicNode> findModules() {
        Principal principal = securitys.getPrincipal();
        List<Permission> entities = permissionMapper.findModules();
        Stream<Permission> stream = entities.stream();
        if (!Ids.ADMIN_ID.equals(principal.getUserId())) {
            String[] permissionIds = principal.getPermissionIds();
            stream = stream.filter((e) -> ArrayUtils.contains(permissionIds, e.getId()));
        }
        List<BasicNode> nodes = stream.map(MenuNodeAdapter.INSTANCE::adapte).collect(Collectors.toList());
        return nodes;
    }

    /**
     * 强制刷新功能树索引(ID路径)
     */
    public void forceRefreshIndexes() {
        List<Permission> entities = permissionMapper.findAll();
        TreeUtil.recursiveSetIdPath(entities, PermissionIdAccess.INSTANCE, Ids.ROOT_ID, Symbols.SEPARATOR);
        for (Permission entity : entities) {
            permissionMapper.update(entity);
        }
    }

    /** 更新子节点IP路径 */
    private void updateChildrenIdPath(Permission parent) {
        Collection<Permission> entities = findAllByParentId(parent.getId());
        TreeUtil.recursiveSetIdPath(entities, PermissionIdAccess.INSTANCE, parent.getId(), parent.getIdPath());
        for (Permission entity : entities) {
            permissionMapper.update(entity);
        }
    }

    /** 保存时候校验 */
    private void validate(Permission permission) {
        String parentId = permission.getParentId();
        String name = permission.getName();
        Integer type = permission.getType();
        if (StringUtils.isEmpty(name)) {
            throw ExceptionHelper.prompt("名称不能为空");
        }
        if (type == null) {
            throw ExceptionHelper.prompt("类别不能为空");
        }
        if (!Ids.ROOT_ID.equals(parentId)) {
            Permission parentEntity = permissionMapper.getById(parentId);
            if (parentEntity == null) {
                throw ExceptionHelper.prompt("没有查询到对应的上级");
            }
        }
    }

    /** 构建Id路径 */
    private String forceGetIdPath(Permission permission) {
        String id = permission.getId();
        String parentId = permission.getParentId();
        if (Ids.ROOT_ID.equals(parentId)) {
            return Symbols.SEPARATOR + id + Symbols.SEPARATOR;
        }
        List<String> idList = new ArrayList<>();
        idList.add(id);
        while (parentId != null && !Ids.ROOT_ID.equals(parentId)) {
            idList.add(parentId);
            Permission parentEntity = permissionMapper.getById(parentId);
            if (parentEntity == null) {
                break;
            }
            parentId = parentEntity.getParentId();
        }
        StringBuilder idPathBuilder = new StringBuilder();
        idPathBuilder.append(Symbols.SEPARATOR);
        for (int i = idList.size() - 1; i >= 0; i--) {
            idPathBuilder.append(idList.get(i)).append(Symbols.SEPARATOR);
        }
        return idPathBuilder.toString();
    }

    /**
     * 查询子孙功能权限(包含子孙级别功能权限)
     * @param parentId 功能权限ID
     * @return 子功能权限列表
     */
    private List<Permission> findAllByParentId(String parentId) {
        List<Permission> entities = new ArrayList<>();
        Queue<String> idQueue = new LinkedList<>();// 待处理队列
        Set<String> idSet = new HashSet<>();// 已经处理集合
        idQueue.add(parentId);// QUEUE<-I
        for (; !idQueue.isEmpty();) {
            String id = idQueue.remove();// QUEUE->I
            idSet.add(id);//
            for (Permission entity : permissionMapper.findByParentId(id)) {
                if (!idSet.contains(entity.getId())) {
                    entities.add(entity);
                    idQueue.add(entity.getId());// QUEUE<-I
                }
            }
        }
        return entities;
    }

    // ==============================InnerClass=======================================
    private static class PermissionIdsFilter implements NodeFilter<Permission> {
        private final String[] permissionIds;

        private PermissionIdsFilter(String[] permissionIds) {
            this.permissionIds = permissionIds;
        }

        @Override
        public boolean accept(Permission model, int depth, boolean leaf) {
            return (ArrayUtils.contains(permissionIds, model.getId()) || !leaf);
        }
    }
}
