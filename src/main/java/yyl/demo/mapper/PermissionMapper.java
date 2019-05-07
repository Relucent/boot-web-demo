package yyl.demo.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.relucent.base.common.collection.CollectionUtil;
import com.github.relucent.base.common.lang.StringUtil;

import yyl.demo.common.constant.SymbolConstant;
import yyl.demo.common.enums.IntBoolEnum;
import yyl.demo.entity.PermissionEntity;
import yyl.demo.kit.PermissionKit;
import yyl.demo.model.qo.PermissionQO;
import yyl.demo.model.ro.PermissionInfoRO;
import yyl.demo.model.vo.PermissionNodeVO;

/**
 * 功能权限_Mapper接口
 * @author YYL
 */
@Mapper
public interface PermissionMapper extends BaseMapper<PermissionEntity> {

    // ==============================MapperMethods====================================
    /**
     * 根据角色查询功能权限
     * @param roleId 角色ID
     * @return 功能权限信息列表
     */
    List<PermissionInfoRO> findPermissionInfoByRoleId(String roleId);

    /**
     * 根据角色ID查询权限
     * @param roleIds
     * @return 功能权限信息列表
     */
    List<PermissionInfoRO> findPermissionInfoByRoleIds(String[] roleIds);

    // ==============================DefaultMethods===================================
    /**
     * 查询功能权限
     * @param id 功能权限ID
     * @return 功能权限信息
     */
    default PermissionEntity getById(String id) {
        LambdaQueryWrapper<PermissionEntity> lqw = Wrappers.lambdaQuery();
        lqw.eq(PermissionEntity::getId, id);
        lqw.eq(PermissionEntity::getDeleted, IntBoolEnum.N.value());
        return selectOne(lqw);
    }

    /**
     * 查询全部功能权限
     * @return 功能权限列表
     */
    default List<PermissionEntity> findAll() {
        return selectList(Wrappers.emptyWrapper());
    }

    /**
     * 查询子功能权限
     * @param parentId 父功能权限ID
     * @return 子功能权限列表
     */
    default List<PermissionEntity> findByParentId(String parentId) {
        return selectList(Wrappers.<PermissionEntity>lambdaQuery().eq(PermissionEntity::getParentId, parentId));
    }

    /**
     * 查询功能权限全名称
     * @param id 功能权限ID
     * @return 功能权限全名称
     */
    default String getNamePathById(String id) {
        PermissionEntity entity = this.selectById(id);
        if (entity == null) {
            return StringUtils.EMPTY;
        }
        String[] ids = StringUtil.splitPurify(entity.getIdPath(), SymbolConstant.SEPARATOR);
        if (ArrayUtils.isEmpty(ids)) {
            return StringUtils.EMPTY;
        }
        Map<String, String> idNameMap = getIdNameMapByIds(ids);
        return Stream.of(ids)//
                .map(idNameMap::get)//
                .filter(StringUtils::isNotEmpty)//
                .collect(Collectors.joining("/"));
    }

    /**
     * 查询功能权限ID和名称的映射表
     * @return ID和名称的映射表
     */
    default Map<String, String> getIdNameMapByIds(String[] ids) {
        Map<String, String> idNameMap = new HashMap<>();
        LambdaQueryWrapper<PermissionEntity> lqw = Wrappers.lambdaQuery();
        lqw.in(PermissionEntity::getId, (Object[]) ids);
        lqw.eq(PermissionEntity::getDeleted, IntBoolEnum.N.value());
        lqw.select(PermissionEntity::getId, PermissionEntity::getName);
        for (PermissionEntity entity : selectList(lqw)) {
            idNameMap.put(entity.getId(), entity.getName());
        }
        return idNameMap;
    }

    /**
     * 根据条件查询功能权限
     * @param criteria 查询条件
     * @return 功能权限列表
     */
    default List<PermissionEntity> findByCriteria(PermissionQO qo) {
        String keyword = qo.getKeyword();
        LambdaQueryWrapper<PermissionEntity> lqw = Wrappers.lambdaQuery();
        if (StringUtils.isEmpty(keyword)) {
            lqw.likeLeft(PermissionEntity::getName, keyword);
        }
        lqw.eq(PermissionEntity::getDeleted, IntBoolEnum.N.value());
        lqw.orderByAsc(PermissionEntity::getOrdinal);
        return selectList(lqw);
    }

    /**
     * 查询匹配条件的记录数
     * @param parentId 父功能权限ID
     * @return 记录数
     */
    default Long countByParentId(String parentId) {
        LambdaQueryWrapper<PermissionEntity> lqw = Wrappers.lambdaQuery();
        lqw.eq(PermissionEntity::getParentId, parentId);
        lqw.eq(PermissionEntity::getDeleted, IntBoolEnum.N.value());
        return selectCount(lqw);
    }

    /**
     * 查询功能权限节点列表
     * @return 功能权限节点列表
     */
    default List<PermissionNodeVO> findAllNode() {
        LambdaQueryWrapper<PermissionEntity> lqw = Wrappers.lambdaQuery();
        lqw.eq(PermissionEntity::getDeleted, IntBoolEnum.N.value());
        lqw.orderByDesc(PermissionEntity::getOrdinal);
        lqw.select(//
                PermissionEntity::getId, //
                PermissionEntity::getParentId, //
                PermissionEntity::getName, //
                PermissionEntity::getCode, //
                PermissionEntity::getType, //
                PermissionEntity::getPath, //
                PermissionEntity::getIcon, //
                PermissionEntity::getRemark, //
                PermissionEntity::getOrdinal//
        );
        return CollectionUtil.map(selectList(lqw), PermissionKit::toNodeVO);
    }
}
