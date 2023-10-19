package yyl.demo.mapper;

import java.util.List;

import javax.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;

import yyl.demo.common.enums.IntBoolEnum;
import yyl.demo.common.model.PageVO;
import yyl.demo.common.model.PaginationQO;
import yyl.demo.common.mybatis.MyPageHelper;
import yyl.demo.entity.RoleEntity;
import yyl.demo.model.qo.RoleQO;
import yyl.demo.model.ro.RoleInfoRO;

/**
 * 系统角色_Mapper接口
 * @author _yyl
 */
@Mapper
public interface RoleMapper extends BaseMapper<RoleEntity> {

	// ==============================MapperMethods====================================
	/**
	 * 查询用户的角色
	 * @param userId 用户ID
	 * @return 角色信息列表
	 */
	List<RoleInfoRO> findRoleInfoByUserId(String userId);

	// ==============================DefaultMethods===================================

	/**
	 * 查询全部角色
	 * @return 角色列表
	 */
	default List<RoleEntity> findAll() {
		LambdaQueryWrapper<RoleEntity> lqw = Wrappers.lambdaQuery();
		lqw.eq(RoleEntity::getDeleted, IntBoolEnum.N.value());
		return selectList(lqw);
	}

	/**
	 * 根据名称查询角色
	 * @param name 角色名称
	 * @return 角色
	 */
	default RoleEntity getByName(String name) {
		LambdaQueryWrapper<RoleEntity> lqw = Wrappers.lambdaQuery();
		lqw.eq(RoleEntity::getName, name);
		lqw.eq(RoleEntity::getDeleted, IntBoolEnum.N.value());
		lqw.last("limit 1");
		return selectOne(lqw);
	}

	/**
	 * 根据条件查询角色
	 * @param pagination 分页条件
	 * @return 角色列表
	 */
	default PageVO<RoleEntity> findByCriteria(PaginationQO<RoleQO> pagination) {
		RoleQO qo = pagination.getFilter();
		String keyword = qo.getKeyword();
		LambdaQueryWrapper<RoleEntity> lqw = Wrappers.lambdaQuery();
		if (StringUtils.isNotEmpty(keyword)) {
			lqw.and(w -> w.likeLeft(RoleEntity::getName, keyword)//
					.or().likeLeft(RoleEntity::getCode, keyword));
		}
		lqw.eq(RoleEntity::getDeleted, IntBoolEnum.N.value());
		lqw.orderByAsc(RoleEntity::getCode);

		PageVO<RoleEntity> page = MyPageHelper.invoke(pagination, p -> selectPage(p, lqw));
		return page;
	}

	/**
	 * 验证是否存在相同编码
	 * @param code 编码
	 * @param id 排除的ID(可选)
	 * @return 是否存在相同名称
	 */
	default boolean existsByCodeAndNeId(String code, @Nullable String id) {
		LambdaQueryWrapper<RoleEntity> lqw = Wrappers.lambdaQuery();
		lqw.ne(StringUtils.isNotEmpty(id), RoleEntity::getId, id);
		lqw.eq(StringUtils.isNotEmpty(code), RoleEntity::getCode, code);
		lqw.eq(RoleEntity::getDeleted, IntBoolEnum.N.value());
		return selectCount(lqw) != 0;
	}

	/**
	 * 验证是否存在相同名称
	 * @param name 名称
	 * @param id 排除的ID(可选)
	 * @return 是否存在相同名称
	 */
	default boolean existsByNameAndNeId(String name, @Nullable String id) {
		LambdaQueryWrapper<RoleEntity> lqw = Wrappers.lambdaQuery();
		lqw.ne(StringUtils.isNotEmpty(id), RoleEntity::getId, id);
		lqw.eq(StringUtils.isNotEmpty(name), RoleEntity::getName, name);
		lqw.eq(RoleEntity::getDeleted, IntBoolEnum.N.value());
		return selectCount(lqw) != 0;
	}
}
