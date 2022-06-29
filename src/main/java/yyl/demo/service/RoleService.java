package yyl.demo.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.relucent.base.common.collection.CollectionUtil;
import com.github.relucent.base.common.exception.ExceptionHelper;
import com.github.relucent.base.common.identifier.IdUtil;

import yyl.demo.common.enums.IntBoolEnum;
import yyl.demo.common.model.PageVO;
import yyl.demo.common.model.PaginationQO;
import yyl.demo.common.mybatis.MyPageHelper;
import yyl.demo.common.standard.AuditableUtil;
import yyl.demo.entity.RoleEntity;
import yyl.demo.entity.RolePermissionEntity;
import yyl.demo.kit.RoleKit;
import yyl.demo.mapper.RoleMapper;
import yyl.demo.mapper.RolePermissionMapper;
import yyl.demo.mapper.UserRoleMapper;
import yyl.demo.model.dto.RoleDTO;
import yyl.demo.model.qo.RoleQO;
import yyl.demo.model.ro.RoleRO;
import yyl.demo.model.vo.RoleVO;
import yyl.demo.security.Securitys;

/**
 * 角色管理
 */
@Transactional
@Service
public class RoleService {

	// ==============================Fields===========================================

	@Autowired
	private RoleMapper roleMapper;

	@Autowired
	private UserRoleMapper userRoleMapper;

	@Autowired
	private RolePermissionMapper rolePermissionMapper;

	// ==============================Methods==========================================
	/**
	 * 新增角色
	 * @param dto 角色信息
	 */
	public void save(RoleDTO dto) {
		dto.setId(null);
		validate(dto);
		RoleEntity entity = new RoleEntity();
		RoleKit.copyProperties(dto, entity);
		entity.setId(IdUtil.timeId());
		String identity = Securitys.getUserId();
		AuditableUtil.setCreated(entity, identity);
		roleMapper.insert(entity);

		// 更新角色功能权限
		updateRolePermissions(entity.getId(), dto.getPermissionIds());
	}

	/**
	 * 删除角色(标记删除)
	 * @param id 角色ID
	 */
	public void deleteById(String id) {
		// 删除用户角色关联数据
		userRoleMapper.deleteByRoleId(id);
		// 删除角色权限关联数据
		rolePermissionMapper.deleteByRoleId(id);

		// 删除角色数据(标记删除)
		RoleEntity updated = new RoleEntity();
		updated.setId(id);
		updated.setDeleted(IntBoolEnum.Y.value());
		String identity = Securitys.getUserId();
		AuditableUtil.setUpdated(updated, identity);
		roleMapper.updateById(updated);
	}

	/**
	 * 更新角色
	 * @param dto 角色信息
	 */
	public void update(RoleDTO dto) {
		validate(dto);
		RoleEntity entity = roleMapper.selectById(dto.getId());
		if (entity == null) {
			throw ExceptionHelper.prompt("角色不存在或者已经失效");
		}
		RoleKit.copyProperties(dto, entity);
		String identity = Securitys.getUserId();
		AuditableUtil.setUpdated(entity, identity);
		roleMapper.updateById(entity);

		// 更新角色功能权限
		updateRolePermissions(entity.getId(), dto.getPermissionIds());
	}

	/**
	 * 查询角色
	 * @param id 角色ID
	 * @return 角色信息
	 */
	public RoleVO getById(String id) {
		RoleEntity entity = roleMapper.selectById(id);
		if (entity == null) {
			throw ExceptionHelper.prompt("角色不存在或者已经失效");
		}
		RoleVO vo = RoleKit.toVO(entity);

		// 关联的功能权限
		List<String> permissionIds = rolePermissionMapper.findPermissionIdByRoleId(id);
		vo.setPermissionIds(permissionIds);
		return vo;
	}

	/**
	 * 分页查询
	 * @param pagination 分页条件
	 * @return 分页结果
	 */
	public PageVO<RoleRO> list(PaginationQO<RoleQO> pagination) {
		RoleQO qo = pagination.getFilter();
		PageVO<RoleEntity> page = MyPageHelper.invoke(pagination, () -> roleMapper.findByCriteria(qo));
		return page.mapRecords(RoleKit::toRO);
	}

	/**
	 * 得到所有角色
	 * @return 角色列表
	 */
	public List<RoleRO> findAll() {
		List<RoleEntity> entities = roleMapper.findAll();
		return CollectionUtil.map(entities, RoleKit::toRO);
	}

	/**
	 * 查询角色的功能权限
	 * @param roleIds 角色ID列表
	 * @return 功能权限ID列表
	 */
	public List<String> findPermissionIdByRoleIds(Collection<String> roleIds) {
		return rolePermissionMapper.findPermissionIdByRoleIds(roleIds);
	}

	/**
	 * 更新角色权限
	 * @param roleId 角色ID
	 * @param permissionIds 功能权限ID
	 */
	private void updateRolePermissions(String roleId, List<String> permissionIds) {
		if (permissionIds == null) {
			return;
		}
		String identity = Securitys.getUserId();
		Map<String, RolePermissionEntity> permissionIdMap = new HashMap<>();
		for (RolePermissionEntity entity : rolePermissionMapper.selectListByRoleId(roleId)) {
			permissionIdMap.put(entity.getPermissionId(), entity);
		}
		List<RolePermissionEntity> newEntities = new ArrayList<>();
		for (String permissionId : permissionIds) {
			RolePermissionEntity entity = permissionIdMap.remove(permissionId);
			if (entity == null) {
				entity = new RolePermissionEntity();
				entity.setId(IdUtil.uuid32());
				entity.setRoleId(roleId);
				entity.setPermissionId(permissionId);
				AuditableUtil.setCreated(entity, identity);
				newEntities.add(entity);
			}
		}
		for (RolePermissionEntity entity : permissionIdMap.values()) {
			rolePermissionMapper.deleteById(entity.getId());
		}
		for (RolePermissionEntity entity : newEntities) {
			rolePermissionMapper.insert(entity);
		}
	}

	// ==============================ToolMethods======================================
	/**
	 * 验证数据
	 * @param dto 角色数据
	 */
	private void validate(RoleDTO dto) {
		String id = dto.getId();
		String code = dto.getCode();
		String name = dto.getName();
		if (StringUtils.isEmpty(code)) {
			throw ExceptionHelper.prompt("角色编码不能为空");
		}
		if (StringUtils.isEmpty(name)) {
			throw ExceptionHelper.prompt("角色名称不能为空");
		}
		if (roleMapper.existsByCodeAndNeId(code, id)) {
			throw ExceptionHelper.prompt("该角色编码已经被使用，角色编码不能重复");
		}
		if (roleMapper.existsByNameAndNeId(name, id)) {
			throw ExceptionHelper.prompt("该角色名称已经被使用，角色名称不能重复");
		}
	}
}
