package yyl.demo.mapper;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;

import yyl.demo.entity.User;

/**
 * 系统用户_Mapper接口
 * @author _yyl
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    // ==============================MapperMethods====================================
    // ...
    /**
     * 根据条件查询用户
     * @param criteria 查询条件
     * @return 用户列表
     */
    default List<User> findBy(User criteria) {
        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery();
        String realname = criteria.getRealname();
        String departmentId = criteria.getDepartmentId();
        if (StringUtils.isNotEmpty(realname)) {
            wrapper.eq(User::getRealname, realname);
        }
        if (StringUtils.isNotEmpty(departmentId)) {
            wrapper.eq(User::getDepartmentId, departmentId);
        }
        return selectList(wrapper);
    }

    // ==============================DefaultMethods===================================
    /**
     * 根据账号查询用户
     * @param username 用户名
     * @return 用户信息
     */
    default User getByUsername(String username) {
        return selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, username));
    }
}
