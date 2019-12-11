package yyl.demo.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;

import yyl.demo.common.enums.IntBoolEnum;
import yyl.demo.entity.UserRoleEntity;

/**
 * 用户角色关联_Mapper接口
 * @author _yyl
 */
@Mapper
public interface UserRoleMapper extends BaseMapper<UserRoleEntity> {

    // ==============================MapperMethods====================================
    // ...

    // ==============================DefaultMethods===================================
    /**
     * 删除用户角色关联数据
     * @param userId 用户ID
     */
    default int deleteByUserId(String userId) {
        LambdaQueryWrapper<UserRoleEntity> lqw = Wrappers.lambdaQuery();
        lqw.eq(UserRoleEntity::getUserId, userId);
        return delete(lqw);
    }

    /**
     * 删除用户角色关联数据
     * @param roleId 角色ID
     */
    default int deleteByRoleId(String roleId) {
        LambdaQueryWrapper<UserRoleEntity> lqw = Wrappers.lambdaQuery();
        lqw.eq(UserRoleEntity::getRoleId, roleId);
        return delete(lqw);
    }

    /**
     * 根据用户查询用户角色关联
     * @param userId 用户ID
     * @return 角色用户角色关联列表
     */
    default List<UserRoleEntity> findByUserId(String userId) {
        LambdaQueryWrapper<UserRoleEntity> lqw = Wrappers.lambdaQuery();
        lqw.eq(UserRoleEntity::getUserId, userId);
        lqw.eq(UserRoleEntity::getDeleted, IntBoolEnum.N.value());
        return selectList(lqw);
    }

    /**
     * 根据用户查询角色
     * @param userId 用户ID
     * @return 角色ID列表
     */
    default List<String> findRoleIdListByUserId(String userId) {
        return selectList(Wrappers.<UserRoleEntity>lambdaQuery()//
                .select(UserRoleEntity::getRoleId)//
                .eq(UserRoleEntity::getUserId, userId))//
                        .stream()//
                        .map(UserRoleEntity::getRoleId)//
                        .distinct()//
                        .collect(Collectors.toList());
    }
}
