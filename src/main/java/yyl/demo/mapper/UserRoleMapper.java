package yyl.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import yyl.demo.entity.UserRole;
import yyl.demo.mapper.basic.BasicMapper;

/**
 * 用户角色关联_Mapper接口
 * @author _yyl
 */
@Mapper
public interface UserRoleMapper extends BasicMapper<UserRole, String> {

    /**
     * 删除用户角色关联数据
     * @param userId 用户ID
     */
    void deleteByUserId(String userId);

    /**
     * 删除用户角色关联数据
     * @param roleId 角色ID
     */
    void deleteByRoleId(String roleId);

    /**
     * 根据用户查询用户角色关联
     * @param userId 用户ID
     * @return 角色用户角色关联列表
     */
    List<UserRole> findByUserId(String userId);

    /**
     * 根据用户查询角色
     * @param userId 用户ID
     * @return 角色ID列表
     */
    List<String> findRoleIdByUserId(String userId);
}

