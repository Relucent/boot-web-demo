package yyl.demo.mapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.relucent.base.common.collection.CollectionUtil;

import yyl.demo.common.enums.IntBoolEnum;
import yyl.demo.entity.RolePermissionEntity;

/**
 * 角色权限关联_Mapper接口
 * @author _yyl
 */
@Mapper
public interface RolePermissionMapper extends BaseMapper<RolePermissionEntity> {

    // ==============================MapperMethods====================================
    // ...

    // ==============================DefaultMethods===================================
    /**
     * 删除角色权限关联数据
     * @param roleId 角色ID
     */
    default int deleteByRoleId(String roleId) {
        LambdaQueryWrapper<RolePermissionEntity> lqw = Wrappers.lambdaQuery();
        lqw.eq(RolePermissionEntity::getRoleId, roleId);
        lqw.eq(RolePermissionEntity::getDeleted, IntBoolEnum.N.value());
        return delete(lqw);
    }

    /**
     * 删除角色权限关联数据
     * @param permissionId 权限ID
     */
    default int deleteByPermissionId(String permissionId) {
        LambdaQueryWrapper<RolePermissionEntity> lqw = Wrappers.lambdaQuery();
        lqw.eq(RolePermissionEntity::getPermissionId, permissionId);
        lqw.eq(RolePermissionEntity::getDeleted, IntBoolEnum.N.value());
        return delete(lqw);
    }

    /**
     * 根据角色查询功能权限
     * @param roleId 角色ID
     * @return 功能权限信息ID列表
     */
    default List<String> findPermissionIdByRoleId(String roleId) {
        LambdaQueryWrapper<RolePermissionEntity> lqw = Wrappers.lambdaQuery();
        lqw.eq(RolePermissionEntity::getRoleId, roleId);
        lqw.eq(RolePermissionEntity::getDeleted, IntBoolEnum.N.value());
        lqw.select(RolePermissionEntity::getPermissionId);
        return CollectionUtil.map(selectList(lqw), RolePermissionEntity::getPermissionId);
    }

    /**
     * 根据角色ID查询权限
     * @param roleIds
     * @return 功能权限信息ID列表
     */
    default List<String> findPermissionIdByRoleIds(Collection<String> roleIds) {
        if (CollectionUtils.isEmpty(roleIds)) {
            return new ArrayList<>();
        }
        LambdaQueryWrapper<RolePermissionEntity> lqw = Wrappers.lambdaQuery();
        lqw.in(RolePermissionEntity::getRoleId, roleIds);
        lqw.eq(RolePermissionEntity::getDeleted, IntBoolEnum.N.value());
        lqw.select(RolePermissionEntity::getPermissionId);
        return CollectionUtil.map(selectList(lqw), RolePermissionEntity::getPermissionId);
    }

    /**
     * 根据角色查询角色功能权限关联
     * @param roleId 角色ID
     * @return 角色权限关联列表
     */
    default List<RolePermissionEntity> selectListByRoleId(String roleId) {
        LambdaQueryWrapper<RolePermissionEntity> lqw = Wrappers.lambdaQuery();
        lqw.eq(RolePermissionEntity::getRoleId, roleId);
        lqw.eq(RolePermissionEntity::getDeleted, IntBoolEnum.N.value());
        return selectList(lqw);
    }
}