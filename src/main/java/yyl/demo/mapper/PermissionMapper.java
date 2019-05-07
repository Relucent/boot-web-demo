package yyl.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import yyl.demo.entity.Permission;
import yyl.demo.mapper.basic.BasicMapper;

/**
 * 功能权限_Mapper接口
 */
@Mapper
public interface PermissionMapper extends BasicMapper<Permission, String> {

    /**
     * 查询子功能权限
     * @param parentId 父功能权限ID
     * @return 子功能权限列表
     */
    List<Permission> findByParentId(String parentId);

    /**
     * 查询匹配条件的记录数
     * @param parentId 父功能权限ID
     * @return 记录数
     */
    Long countByParentId(String parentId);

    /**
     * 根据条件查询功能权限
     * @param condition 查询条件
     * @return 功能权限列表
     */
    List<Permission> findBy(Permission condition);
    

    /**
     * 查询模块级菜单
     * @return 模块级菜单列表
     */
    List<Permission> findModules();
}
