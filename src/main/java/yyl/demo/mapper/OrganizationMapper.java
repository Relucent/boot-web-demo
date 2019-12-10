package yyl.demo.mapper;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.relucent.base.common.collection.CollectionUtil;

import yyl.demo.common.constant.IdConstant;
import yyl.demo.common.enums.IntBoolEnum;
import yyl.demo.entity.OrganizationEntity;
import yyl.demo.kit.OrganizationKit;
import yyl.demo.model.qo.OrganizationQO;
import yyl.demo.model.vo.OrganizationNodeVO;

/**
 * 机构_Mapper接口
 */
@Mapper
public interface OrganizationMapper extends BaseMapper<OrganizationEntity> {

    // ====================MapperMethods=====================================

    // ====================DefaultMethods=====================================
    /**
     * 查询机构
     * @param id 机构ID
     * @return 机构信息
     */
    default OrganizationEntity getById(String id) {
        LambdaQueryWrapper<OrganizationEntity> lqw = Wrappers.lambdaQuery();
        lqw.eq(OrganizationEntity::getId, id);
        lqw.eq(OrganizationEntity::getDeleted, IntBoolEnum.N.value());
        return selectOne(lqw);
    }

    /**
     * 查询机构
     * @param name 机构名称
     * @return 机构信息
     */
    default OrganizationEntity getByName(String name) {
        LambdaQueryWrapper<OrganizationEntity> lqw = Wrappers.lambdaQuery();
        lqw.eq(OrganizationEntity::getName, name);
        lqw.eq(OrganizationEntity::getDeleted, IntBoolEnum.N.value());
        return selectOne(lqw);
    }

    /**
     * 查询机构名称
     * @param id 机构ID
     * @return 机构名称
     */
    default String getNameById(String id) {
        if (StringUtils.isEmpty(id) || IdConstant.ROOT_ID.equals(id)) {
            return StringUtils.EMPTY;
        }
        LambdaQueryWrapper<OrganizationEntity> lqw = Wrappers.lambdaQuery();
        lqw.eq(OrganizationEntity::getId, id);
        lqw.eq(OrganizationEntity::getDeleted, IntBoolEnum.N.value());
        lqw.select(OrganizationEntity::getName);
        return Optional.ofNullable(selectOne(lqw)).map(OrganizationEntity::getName).orElse(StringUtils.EMPTY);
    }

    /**
     * 查询
     * @param qo 查询条件
     * @return 列表
     */
    default List<OrganizationEntity> findByCriteria(OrganizationQO qo) {
        String name = qo.getName();
        LambdaQueryWrapper<OrganizationEntity> lqw = Wrappers.lambdaQuery();
        if (StringUtils.isEmpty(name)) {
            lqw.likeLeft(OrganizationEntity::getName, name);
        }
        lqw.eq(OrganizationEntity::getDeleted, IntBoolEnum.N.value());
        lqw.orderByAsc(OrganizationEntity::getOrdinal);
        return selectList(lqw);
    }

    /**
     * 查询
     * @param qo 查询条件
     * @return 列表
     */
    default List<OrganizationEntity> findAll() {
        LambdaQueryWrapper<OrganizationEntity> lqw = Wrappers.lambdaQuery();
        lqw.eq(OrganizationEntity::getDeleted, IntBoolEnum.N.value());
        lqw.orderByDesc(OrganizationEntity::getOrdinal);
        return selectList(lqw);
    }

    /**
     * 查询机构
     * @param parentId 父机构ID
     * @return 机构列表
     */
    default List<OrganizationEntity> findByParentId(String parentId) {
        LambdaQueryWrapper<OrganizationEntity> lqw = Wrappers.lambdaQuery();
        lqw.eq(OrganizationEntity::getParentId, parentId);
        lqw.eq(OrganizationEntity::getDeleted, IntBoolEnum.N.value());
        lqw.orderByDesc(OrganizationEntity::getOrdinal);
        return selectList(lqw);
    }

    /**
     * 查询机构节点列表
     * @return 机构节点列表
     */
    default List<OrganizationNodeVO> findAllNode() {
        LambdaQueryWrapper<OrganizationEntity> lqw = Wrappers.lambdaQuery();
        lqw.eq(OrganizationEntity::getDeleted, IntBoolEnum.N.value());
        lqw.orderByDesc(OrganizationEntity::getOrdinal);
        lqw.select(//
                OrganizationEntity::getId, //
                OrganizationEntity::getParentId, //
                OrganizationEntity::getName, //
                OrganizationEntity::getRemark, //
                OrganizationEntity::getOrdinal//
        );
        return CollectionUtil.map(selectList(lqw), OrganizationKit::toNodeVO);
    }
}
