package yyl.demo.mapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ArrayUtils;
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

    /**
     * 根据角色查询功能权限
     * @param roleId 角色ID
     * @return 功能权限ID列表
     */
    default List<String> selectPermissionIdListByRoleId(String roleId) {
        return selectList(Wrappers.<RolePermission>lambdaQuery()//
                .select(RolePermission::getPermissionId)//
                .eq(RolePermission::getRoleId, roleId))//
                        .stream()//
                        .map(RolePermission::getPermissionId)//
                        .distinct()//
                        .collect(Collectors.toList());
    }

    /**
     * 根据角色查询功能权限
     * @param roleIds 角色ID数组
     * @return 功能权限ID列表
     */
    default List<String> selectPermissionIdListByRoleIds(String[] roleIds) {
        if (ArrayUtils.isEmpty(roleIds)) {
            return new ArrayList<>();
        }
        return selectList(Wrappers.<RolePermission>lambdaQuery()//
                .select(RolePermission::getPermissionId)//
                .in(RolePermission::getRoleId, Arrays.asList(roleIds)))//
                        .stream()//
                        .map(RolePermission::getPermissionId)//
                        .distinct()//
                        .collect(Collectors.toList());
    }
}