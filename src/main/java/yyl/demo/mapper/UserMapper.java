package yyl.demo.mapper;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.relucent.base.common.collection.CollectionUtil;

import yyl.demo.common.enums.IntBoolEnum;
import yyl.demo.entity.UserEntity;
import yyl.demo.model.qo.UserQO;

/**
 * 系统用户_Mapper接口
 * @author _yyl
 */
@Mapper
public interface UserMapper extends BaseMapper<UserEntity> {

    // ==============================MapperMethods====================================
    // ...

    // ==============================DefaultMethods===================================
    /**
     * 查询全部用户
     * @return 全部用户列表
     */
    default List<UserEntity> findAll() {
        LambdaQueryWrapper<UserEntity> lqw = Wrappers.lambdaQuery();
        lqw.eq(UserEntity::getDeleted, IntBoolEnum.N.value());
        lqw.orderByDesc(UserEntity::getRealname);
        return selectList(lqw);
    }

    /**
     * 查询机构下的用户
     * @param orgId 机构ID
     * @return 用户列表
     */
    default List<UserEntity> findByOrganizationId(String organizationId) {
        LambdaQueryWrapper<UserEntity> lqw = Wrappers.lambdaQuery();
        lqw.eq(UserEntity::getOrganizationId, organizationId);
        lqw.eq(UserEntity::getDeleted, IntBoolEnum.N.value());
        lqw.orderByDesc(UserEntity::getRealname);
        return selectList(lqw);
    }

    /**
     * 查询全部用户ID
     * @return 用户ID列表
     */
    default List<String> findId() {
        LambdaQueryWrapper<UserEntity> lqw = Wrappers.lambdaQuery();
        lqw.select(UserEntity::getId);
        lqw.eq(UserEntity::getDeleted, IntBoolEnum.N.value());
        return CollectionUtil.map(selectList(lqw), UserEntity::getId);
    }

    /**
     * 查询用户
     * @param qo 查询条件
     * @return 用户列表
     */
    default List<UserEntity> findByCriteria(UserQO qo) {
        String keyword = qo.getKeyword();
        String organizationId = qo.getOrganizationId();
        LambdaQueryWrapper<UserEntity> lqw = Wrappers.lambdaQuery();
        if (StringUtils.isNoneEmpty(keyword)) {
            lqw.and(w -> w.likeLeft(UserEntity::getUsername, keyword)//
                    .or().likeLeft(UserEntity::getRealname, keyword));
        }
        if (StringUtils.isNoneEmpty(organizationId)) {
            lqw.eq(UserEntity::getOrganizationId, organizationId);
        }
        lqw.eq(UserEntity::getDeleted, IntBoolEnum.N.value());
        lqw.orderByAsc(UserEntity::getUsername);
        return selectList(lqw);
    }

    /**
     * 根据ID查询用户
     * @param id 用户ID
     * @return 用户信息
     */
    default UserEntity getById(String id) {
        LambdaQueryWrapper<UserEntity> lqw = Wrappers.lambdaQuery();
        lqw.eq(UserEntity::getId, id);
        lqw.eq(UserEntity::getDeleted, IntBoolEnum.N.value());
        return selectOne(lqw);
    }

    /**
     * 根据账号查询用户
     * @param username 用户名
     * @return 用户信息
     */
    default UserEntity getByUsername(String username) {
        LambdaQueryWrapper<UserEntity> lqw = Wrappers.lambdaQuery();
        lqw.eq(UserEntity::getUsername, username);
        lqw.eq(UserEntity::getDeleted, IntBoolEnum.N.value());
        return selectOne(lqw);
    }

    /**
     * 查询机构下的用户数
     * @param organizationId 机构ID
     * @return 用户数
     */
    default int countByOrganizationId(String organizationId) {
        LambdaQueryWrapper<UserEntity> lqw = Wrappers.lambdaQuery();
        lqw.eq(UserEntity::getOrganizationId, organizationId);
        lqw.eq(UserEntity::getDeleted, IntBoolEnum.N.value());
        return selectCount(lqw);
    }
}
