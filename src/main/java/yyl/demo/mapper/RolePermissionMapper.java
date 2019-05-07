package yyl.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import yyl.demo.entity.RolePermission;
import yyl.demo.mapper.basic.BasicMapper;

/**
 * 角色权限关联_Mapper接口
 * @author _yyl
 */
@Mapper
public interface RolePermissionMapper extends BasicMapper<RolePermission, String> {

    /**
     * 删除角色权限关联数据
     * @param roleId 角色ID
     */
    void deleteByRoleId(String roleId);

    /**
     * 删除角色权限关联数据
     * @param permissionId 权限ID
     */
    void deleteByPermissionId(String permissionId);

    /**
     * 根据角色查询角色功能权限关联
     * @param roleId 角色ID
     * @return 角色权限关联列表
     */
    List<RolePermission> findByRoleId(String roleId);

    /**
     * 根据角色查询功能权限
     * @param roleId 角色ID
     * @return 功能权限ID列表
     */
    List<String> findPermissionIdByRoleId(String roleId);

    /**
     * 根据角色查询功能权限
     * @param roleIds 角色ID数组
     * @return 功能权限ID列表
     */
    List<String> findPermissionIdByRoleIds(String[] roleIds);
}
