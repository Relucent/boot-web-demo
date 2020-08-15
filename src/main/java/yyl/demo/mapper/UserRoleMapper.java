package yyl.demo.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;

import yyl.demo.entity.UserRole;

/**
 * 用户角色关联_Mapper接口
 * @author _yyl
 */
@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {

    /**
     * 删除用户角色关联数据
     * @param userId 用户ID
     */
    default int deleteByUserId(String userId) {
        return delete(Wrappers.<UserRole>lambdaUpdate().eq(UserRole::getUserId, userId));
    }

    /**
     * 删除用户角色关联数据
     * @param roleId 角色ID
     */
    default int deleteByRoleId(String roleId) {
        return delete(Wrappers.<UserRole>lambdaUpdate().eq(UserRole::getRoleId, roleId));
    }

    /**
     * 根据用户查询用户角色关联
     * @param userId 用户ID
     * @return 角色用户角色关联列表
     */
    default List<UserRole> selectListByUserId(String userId) {
        return selectList(Wrappers.<UserRole>lambdaQuery().eq(UserRole::getUserId, userId));
    }

    /**
     * 根据用户查询角色
     * @param userId 用户ID
     * @return 角色ID列表
     */
    default List<String> selectRoleIdListByUserId(String userId) {
        return selectList(Wrappers.<UserRole>lambdaQuery()//
                .select(UserRole::getRoleId)//
                .eq(UserRole::getUserId, userId))//
                        .stream()//
                        .map(UserRole::getRoleId)//
                        .distinct()//
                        .collect(Collectors.toList());
    }
}
