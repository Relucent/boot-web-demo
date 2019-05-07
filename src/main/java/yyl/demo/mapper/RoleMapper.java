package yyl.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import yyl.demo.entity.Role;
import yyl.demo.mapper.basic.BasicMapper;

/**
 * 系统角色_Mapper接口
 * @author _yyl
 */
@Mapper
public interface RoleMapper extends BasicMapper<Role, String> {

    /**
     * 根据名称查询角色
     * @param name 角色名称
     * @return 角色
     */
    Role getByName(String name);

    /**
     * 根据条件查询角色
     * @param condition 查询条件
     * @return 角色列表
     */
    List<Role> findBy(Role condition);
}
