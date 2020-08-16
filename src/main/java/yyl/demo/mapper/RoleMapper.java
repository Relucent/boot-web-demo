package yyl.demo.mapper;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;

import yyl.demo.entity.Role;

/**
 * 系统角色_Mapper接口
 * @author _yyl
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * 查询全部角色
     * @return 角色列表
     */
    default List<Role> selectAllList() {
        return selectList(Wrappers.emptyWrapper());
    }

    /**
     * 根据名称查询角色
     * @param name 角色名称
     * @return 角色
     */
    default Role selectByName(String name) {
        return selectOne(Wrappers.<Role>lambdaQuery().eq(Role::getName, name).last("limit 1"));
    }

    /**
     * 根据条件查询角色
     * @param condition 查询条件
     * @return 角色列表
     */
    default List<Role> selectListBy(Role condition) {
        LambdaQueryWrapper<Role> wrapper = Wrappers.lambdaQuery();
        String name = condition.getName();
        if (StringUtils.isNotEmpty(name)) {
            wrapper.eq(Role::getName, name);
        }
        return selectList(wrapper);
    }
}
