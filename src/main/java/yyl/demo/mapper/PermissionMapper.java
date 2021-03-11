package yyl.demo.mapper;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;

import yyl.demo.entity.Permission;

/**
 * 功能权限_Mapper接口
 * @author YYL
 */
@Mapper
public interface PermissionMapper extends BaseMapper<Permission> {

    // ==============================MapperMethods====================================
    // ...

    // ==============================DefaultMethods===================================
    /**
     * 查询全部功能权限
     * @return 功能权限列表
     */
    default List<Permission> findAll() {
        return selectList(Wrappers.emptyWrapper());
    }

    /**
     * 查询子功能权限
     * @param parentId 父功能权限ID
     * @return 子功能权限列表
     */
    default List<Permission> findByParentId(String parentId) {
        return selectList(Wrappers.<Permission>lambdaQuery().eq(Permission::getParentId, parentId));
    }

    /**
     * 根据条件查询功能权限
     * @param criteria 查询条件
     * @return 功能权限列表
     */
    default List<Permission> findBy(Permission criteria) {
        String name = criteria.getName();
        LambdaQueryWrapper<Permission> wrapper = Wrappers.lambdaQuery();
        if (StringUtils.isNotEmpty(name)) {
            wrapper.eq(Permission::getName, name);
        }
        return selectList(wrapper);
    }

    /**
     * 查询匹配条件的记录数
     * @param parentId 父功能权限ID
     * @return 记录数
     */
    default Integer countByParentId(String parentId) {
        return selectCount(Wrappers.<Permission>lambdaQuery().eq(Permission::getParentId, parentId));
    }

}
