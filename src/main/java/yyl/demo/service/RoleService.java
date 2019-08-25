package yyl.demo.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.relucent.base.plug.mybatis.MybatisHelper;
import com.github.relucent.base.plug.security.Principal;
import com.github.relucent.base.plug.security.Securitys;
import com.github.relucent.base.util.expection.ExceptionHelper;
import com.github.relucent.base.util.page.Page;
import com.github.relucent.base.util.page.Pagination;

import yyl.demo.common.identifier.IdHelper;
import yyl.demo.common.standard.AuditableUtil;
import yyl.demo.entity.Role;
import yyl.demo.entity.RolePermission;
import yyl.demo.mapper.RoleMapper;
import yyl.demo.mapper.RolePermissionMapper;
import yyl.demo.mapper.UserRoleMapper;

/**
 * 角色管理
 */
@Transactional
@Service
public class RoleService {

    // ==============================Fields===========================================

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private RolePermissionMapper rolePermissionMapper;

    @Autowired
    private Securitys securitys;
    // ==============================Methods==========================================

    /**
     * 新增角色
     * @param role 角色数据
     */
    public void insert(Role role) {

        validate(role);

        Principal principal = securitys.getPrincipal();

        Role entity = new Role();
        IdHelper.setIfEmptyId(entity);
        entity.setName(role.getName());
        entity.setRemark(role.getRemark());

        AuditableUtil.setCreated(entity, principal);

        roleMapper.insert(entity);
    }

    /**
     * 删除角色
     * @param id 角色ID
     */
    public void deleteById(String id) {
        // 删除角色数据
        roleMapper.deleteById(id);
        // 删除用户角色关联数据
        userRoleMapper.deleteByRoleId(id);
        // 删除角色权限关联数据
        rolePermissionMapper.deleteByRoleId(id);
    }

    /**
     * 修改角色
     * @param vo 角色数据
     */
    public void update(Role role) {

        Role entity = roleMapper.getById(role.getId());

        if (entity == null) {
            throw ExceptionHelper.prompt("角色不存在或者已经失效");
        }

        validate(role);

        Principal principal = securitys.getPrincipal();

        entity.setName(role.getName());
        entity.setRemark(role.getRemark());

        AuditableUtil.setLastModified(entity, principal);

        roleMapper.update(entity);
    }

    /**
     * 分页查询
     * @param pagination 分页条件
     * @param condition 查询条件
     * @return 查询结果
     */
    public Page<Role> pagedQuery(Pagination pagination, Role condition) {
        return MybatisHelper.selectPage(pagination, () -> roleMapper.findBy(condition));
    }

    /**
     * 得到所有角色
     * @return 角色列表
     */
    public List<Role> findAll() {
        return roleMapper.findAll();
    }

    /**
     * 查询角色
     * @param id 角色ID
     * @return 角色数据
     */
    public Role getById(String id) {
        Role entity = roleMapper.getById(id);
        if (entity == null) {
            throw ExceptionHelper.prompt("角色不存在或者已经失效");
        }
        rolePermissionMapper.deleteByRoleId(id);
        return entity;
    }

    /**
     * 查询角色的功能权限
     * @param roleId 角色ID
     * @return 功能权限ID列表
     */
    public List<String> findPermissionIdByRoleId(String roleId) {
        return rolePermissionMapper.findPermissionIdByRoleId(roleId);
    }

    /**
     * 查询角色的功能权限
     * @param roleIds 角色ID数组
     * @return 功能权限ID列表
     */
    public List<String> findPermissionIdByRoleIds(String[] roleIds) {
        return rolePermissionMapper.findPermissionIdByRoleIds(roleIds);
    }

    /**
     * 更新角色权限
     * @param roleId 角色ID
     * @param permissionIds 功能权限ID
     */
    public void updateRolePermissions(String roleId, String[] permissionIds) {
        Principal principal = securitys.getPrincipal();
        Map<String, RolePermission> permissionIdMap = new HashMap<>();
        for (RolePermission entity : rolePermissionMapper.findByRoleId(roleId)) {
            permissionIdMap.put(entity.getPermissionId(), entity);
        }
        List<RolePermission> newEntities = new ArrayList<>();
        for (String permissionId : permissionIds) {
            RolePermission entity = permissionIdMap.remove(permissionId);
            if (entity == null) {
                entity = new RolePermission();
                IdHelper.setIfEmptyId(entity);
                entity.setRoleId(roleId);
                entity.setPermissionId(permissionId);
                AuditableUtil.setCreated(entity, principal);
                newEntities.add(entity);
            }
        }
        for (RolePermission entity : permissionIdMap.values()) {
            rolePermissionMapper.deleteById(entity.getId());
        }
        for (RolePermission entity : newEntities) {
            rolePermissionMapper.insert(entity);
        }
    }

    // ==============================ToolMethods======================================
    /** 验证数据 */
    private void validate(Role role) {
        String name = role.getName();
        if (StringUtils.isEmpty(name)) {
            throw ExceptionHelper.prompt("角色名称不能为空");
        }
        Role entity = roleMapper.getByName(name);
        if (entity != null && !Objects.equals(entity.getId(), role.getId())) {
            throw ExceptionHelper.prompt("已经存在相同名称的角色");
        }
    }
}
