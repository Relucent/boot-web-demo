package yyl.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;

import yyl.demo.entity.RolePermission;

/**
 * 角色权限关联_Mapper接口
 * @author _yyl
 */
@Mapper
public interface RolePermissionMapper extends BaseMapper<RolePermission> {

    // ==============================MapperMethods====================================
    /**
     * 根据角色查询功能权限
     * @param roleId 角色ID
     * @return 功能权限ID列表
     */
    List<String> findPermissionIdByRoleId(String roleId);

    /**
     * 根据角色ID查询权限
     * @param roleIds
     * @return
     */
    List<String> findPermissionIdByRoleIds(String[] roleIds);

    // ==============================DefaultMethods===================================
    /**
     * 删除角色权限关联数据
     * @param roleId 角色ID
     */
    default int deleteByRoleId(String roleId) {
        return delete(Wrappers.<RolePermission>lambdaUpdate().eq(RolePermission::getRoleId, roleId));
    }

    /**
     * 删除角色权限关联数据
     * @param permissionId 权限ID
     */
    default int deleteByPermissionId(String permissionId) {
        return delete(Wrappers.<RolePermission>lambdaUpdate().eq(RolePermission::getPermissionId, permissionId));
    }

    /**
     * 根据角色查询角色功能权限关联
     * @param roleId 角色ID
     * @return 角色权限关联列表
     */
    default List<RolePermission> selectListByRoleId(String roleId) {
        return selectList(Wrappers.<RolePermission>lambdaQuery().eq(RolePermission::getRoleId, roleId));
    }
}