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

import com.github.relucent.base.common.exception.ExceptionHelper;
import com.github.relucent.base.common.page.Page;
import com.github.relucent.base.common.page.Pagination;
import com.github.relucent.base.plugin.mybatis.MybatisHelper;

import yyl.demo.common.security.Securitys;
import yyl.demo.common.security.UserPrincipal;
import yyl.demo.common.standard.AuditableUtil;
import yyl.demo.common.util.IdUtil;
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

    // ==============================Methods==========================================

    /**
     * 新增角色
     * @param role 角色数据
     */
    public void insert(Role role) {

        validate(role);

        UserPrincipal principal = Securitys.getPrincipal();

        Role entity = new Role();
        entity.setId(IdUtil.uuid32());
        entity.setCode(role.getCode());
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

        Role entity = roleMapper.selectById(role.getId());

        if (entity == null) {
            throw ExceptionHelper.prompt("角色不存在或者已经失效");
        }

        validate(role);

        UserPrincipal principal = Securitys.getPrincipal();

        entity.setCode(role.getCode());
        entity.setName(role.getName());
        entity.setRemark(role.getRemark());

        AuditableUtil.setUpdated(entity, principal);

        roleMapper.updateById(entity);
    }

    /**
     * 分页查询
     * @param pagination 分页条件
     * @param condition 查询条件
     * @return 查询结果
     */
    public Page<Role> pagedQuery(Pagination pagination, Role condition) {
        return MybatisHelper.selectPage(pagination, () -> roleMapper.selectListBy(condition));
    }

    /**
     * 得到所有角色
     * @return 角色列表
     */
    public List<Role> findAll() {
        return roleMapper.selectAllList();
    }

    /**
     * 查询角色
     * @param id 角色ID
     * @return 角色数据
     */
    public Role getById(String id) {
        Role entity = roleMapper.selectById(id);
        if (entity == null) {
            throw ExceptionHelper.prompt("角色不存在或者已经失效");
        }
        return entity;
    }

    /**
     * 查询角色的功能权限
     * @param roleId 角色ID
     * @return 功能权限ID列表
     */
    public List<String> findPermissionIdByRoleId(String roleId) {
        return rolePermissionMapper.selectPermissionIdListByRoleId(roleId);
    }

    /**
     * 查询角色的功能权限
     * @param roleIds 角色ID数组
     * @return 功能权限ID列表
     */
    public List<String> findPermissionIdByRoleIds(String[] roleIds) {
        return rolePermissionMapper.selectPermissionIdListByRoleIds(roleIds);
    }

    /**
     * 更新角色权限
     * @param roleId 角色ID
     * @param permissionIds 功能权限ID
     */
    public void updateRolePermissions(String roleId, String[] permissionIds) {
        UserPrincipal principal = Securitys.getPrincipal();
        Map<String, RolePermission> permissionIdMap = new HashMap<>();
        for (RolePermission entity : rolePermissionMapper.selectListByRoleId(roleId)) {
            permissionIdMap.put(entity.getPermissionId(), entity);
        }
        List<RolePermission> newEntities = new ArrayList<>();
        for (String permissionId : permissionIds) {
            RolePermission entity = permissionIdMap.remove(permissionId);
            if (entity == null) {
                entity = new RolePermission();
                entity.setId(IdUtil.uuid32());
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
        String code = role.getCode();
        String name = role.getName();
        if (StringUtils.isEmpty(code)) {
            throw ExceptionHelper.prompt("角色编码不能为空");
        }
        if (StringUtils.isEmpty(name)) {
            throw ExceptionHelper.prompt("角色名称不能为空");
        }
        Role entity = roleMapper.selectByName(name);
        if (entity != null && !Objects.equals(entity.getId(), role.getId())) {
            throw ExceptionHelper.prompt("已经存在相同名称的角色");
        }
    }
}
