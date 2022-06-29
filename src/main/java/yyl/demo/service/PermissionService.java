package yyl.demo.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.relucent.base.common.exception.ExceptionHelper;
import com.github.relucent.base.common.identifier.IdUtil;
import com.github.relucent.base.common.tree.TreeUtil;
import com.github.relucent.base.common.tree.TreeUtil.NodeFilter;

import yyl.demo.common.constant.IdConstant;
import yyl.demo.common.constant.SymbolConstant;
import yyl.demo.common.enums.IntBoolEnum;
import yyl.demo.common.enums.PermissionTypeEnum;
import yyl.demo.common.standard.AuditableUtil;
import yyl.demo.entity.PermissionEntity;
import yyl.demo.kit.PermissionKit;
import yyl.demo.mapper.PermissionMapper;
import yyl.demo.mapper.RolePermissionMapper;
import yyl.demo.model.dto.PermissionDTO;
import yyl.demo.model.vo.PermissionNodeVO;
import yyl.demo.model.vo.PermissionVO;
import yyl.demo.security.Securitys;
import yyl.demo.security.model.UserPrincipal;

/**
 * 功能权限管理
 */
@Transactional
@Service
public class PermissionService {

	// ==============================Fields===========================================
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private PermissionMapper permissionMapper;

	@Autowired
	private RolePermissionMapper rolePermissionMapper;

	// ==============================Methods==========================================
	/**
	 * 新增功能权限
	 * @param dto 功能权限信息
	 */
	public void save(PermissionDTO dto) {
		dto.setId(null);
		validate(dto);
		PermissionEntity entity = new PermissionEntity();
		PermissionKit.copyProperties(dto, entity);
		entity.setId(IdUtil.timeId());
		if (entity.getOrdinal() == null) {
			entity.setOrdinal("1000");
		}
		entity.setIdPath(forceGetIdPath(entity));
		String identity = Securitys.getUserId();
		AuditableUtil.setCreated(entity, identity);
		permissionMapper.insert(entity);
	}

	/**
	 * 删除权限(标记删除)
	 * @param id 权限ID
	 */
	public void deleteById(String id) {
		if (permissionMapper.countByParentId(id) != 0) {
			throw ExceptionHelper.prompt("该功能存在子节点，不能被直接删除");
		}

		// 删除关联
		rolePermissionMapper.deleteByPermissionId(id);

		// 标记删除
		PermissionEntity updated = new PermissionEntity();
		updated.setId(id);
		updated.setDeleted(IntBoolEnum.Y.value());
		String identity = Securitys.getUserId();
		AuditableUtil.setUpdated(updated, identity);
		permissionMapper.updateById(updated);
	}

	/**
	 * 修改功能权限
	 * @param dto 功能权限信息
	 */
	public void update(PermissionDTO dto) {

		validate(dto);

		PermissionEntity entity = permissionMapper.selectById(dto.getId());

		if (entity == null) {
			throw ExceptionHelper.prompt("数据不存在");
		}

		String originalIdPath = entity.getIdPath();

		PermissionKit.copyProperties(dto, entity);

		entity.setIdPath(forceGetIdPath(entity));

		String identity = Securitys.getUserId();
		AuditableUtil.setUpdated(entity, identity);
		permissionMapper.updateById(entity);

		// IdPath发生更改, 更新子节点
		if (!Objects.equals(originalIdPath, entity.getIdPath())) {
			updateChildrenIdPath(entity);
		}
	}

	/**
	 * 查询功能权限
	 * @param id 功能权限ID
	 * @return 功能权限
	 */
	public PermissionVO getById(String id) {
		PermissionEntity entity = permissionMapper.selectById(id);
		if (entity == null) {
			throw ExceptionHelper.prompt("数据不存在或者已经失效");
		}
		PermissionVO vo = PermissionKit.toVO(entity);
		String parentId = entity.getParentId();
		if (IdConstant.ROOT_ID.equals(parentId)) {
			vo.setParentNamePath(StringUtils.EMPTY);
		} else {
			vo.setParentNamePath(permissionMapper.getNamePathById(parentId));
		}
		return vo;
	}

	/**
	 * 查询功能权限全名称
	 * @param id 功能权限ID
	 * @return 功能权限全名称
	 */
	public String getNamePathById(String id) {
		return permissionMapper.getNamePathById(id);
	}

	/**
	 * 获得功能权限树
	 * @param rootId 根节点
	 * @param type 类型级别
	 * @return 功能权限树
	 */
	public List<PermissionNodeVO> getPermissionTree(String rootId, PermissionTypeEnum type) {
		UserPrincipal principal = Securitys.getPrincipal();
		List<PermissionNodeVO> entities = permissionMapper.findAllNode();
		NodeFilter<PermissionNodeVO> filter = new PermissionTypeFilter(type);
		if (!IdConstant.ADMIN_ID.equals(principal.getId())) {
			String[] permissionIds = principal.getPermissionIds();
			filter = filter.and(new PermissionIdsFilter(permissionIds));
		}
		return TreeUtil.buildTree(//
				rootId, //
				entities, //
				n -> n, //
				filter, //
				PermissionNodeVO::getId, //
				PermissionNodeVO::getParentId, //
				PermissionNodeVO::setChildren//
		);
	}

	/**
	 * 强制刷新功能树索引(ID路径)
	 */
	public void forceRefreshIdPath() {
		List<PermissionEntity> entities = permissionMapper.findAll();
		// 递归设置ID路径
		TreeUtil.recursiveSetIdPath(entities, // 节点列表
				PermissionEntity::getId, // 获取ID的方法
				PermissionEntity::getParentId, // 获取父ID的方法
				PermissionEntity::setIdPath, // 设置ID路径的方法
				IdConstant.ROOT_ID, // 当前节点父ID
				SymbolConstant.SEPARATOR// 当前节点父ID路径
		);
		// 更新数据
		for (PermissionEntity entity : entities) {
			permissionMapper.updateById(entity);
		}
	}

	// ==============================ToolMethods======================================
	/**
	 * 校验
	 * @param dto 功能权限信息
	 */
	private void validate(PermissionDTO dto) {
		String parentId = dto.getParentId();
		String name = dto.getName();
		Integer type = dto.getType();
		if (StringUtils.isEmpty(name)) {
			throw ExceptionHelper.prompt("名称不能为空");
		}
		if (type == null) {
			throw ExceptionHelper.prompt("类别不能为空");
		}
		if (!IdConstant.ROOT_ID.equals(parentId)) {
			PermissionEntity parentEntity = permissionMapper.selectById(parentId);
			if (parentEntity == null) {
				throw ExceptionHelper.prompt("没有查询到对应的上级");
			}
		}
	}

	/**
	 * 构建ID路径
	 * @param entity 功能权限实体对象
	 * @return ID路径
	 */
	private String forceGetIdPath(PermissionEntity entity) {
		String id = entity.getId();
		String parentId = entity.getParentId();
		if (IdConstant.ROOT_ID.equals(parentId)) {
			return SymbolConstant.SEPARATOR + id + SymbolConstant.SEPARATOR;
		}
		List<String> idList = new ArrayList<>();
		idList.add(id);
		while (parentId != null && !IdConstant.ROOT_ID.equals(parentId)) {
			idList.add(parentId);
			PermissionEntity parentEntity = permissionMapper.selectById(parentId);
			if (parentEntity == null) {
				break;
			}
			parentId = parentEntity.getParentId();
		}
		StringBuilder idPathBuilder = new StringBuilder();
		idPathBuilder.append(SymbolConstant.SEPARATOR);
		for (int i = idList.size() - 1; i >= 0; i--) {
			idPathBuilder.append(idList.get(i)).append(SymbolConstant.SEPARATOR);
		}
		return idPathBuilder.toString();
	}

	/**
	 * 查询子孙功能权限(包含子孙级别功能权限)
	 * @param parentId 功能权限ID
	 * @return 子功能权限列表
	 */
	private List<PermissionEntity> findAllByParentId(String parentId) {
		List<PermissionEntity> entities = new ArrayList<>();
		Queue<String> idQueue = new LinkedList<>();// 待处理队列
		Set<String> idSet = new HashSet<>();// 已经处理集合
		idQueue.add(parentId);// QUEUE<-I
		for (; !idQueue.isEmpty();) {
			String id = idQueue.remove();// QUEUE->I
			idSet.add(id);//
			for (PermissionEntity entity : permissionMapper.findByParentId(id)) {
				if (!idSet.contains(entity.getId())) {
					entities.add(entity);
					idQueue.add(entity.getId());// QUEUE<-I
				}
			}
		}
		return entities;
	}

	/**
	 * 更新子节点IP路径
	 * @param parent 父节点信息
	 */
	private void updateChildrenIdPath(PermissionEntity parent) {
		Collection<PermissionEntity> entities = findAllByParentId(parent.getId());
		// 递归设置ID路径
		TreeUtil.recursiveSetIdPath(entities, // 节点列表
				PermissionEntity::getId, // 获取ID的方法
				PermissionEntity::getParentId, // 获取父ID的方法
				PermissionEntity::setIdPath, // 设置ID路径的方法
				parent.getId(), // 当前节点父ID
				parent.getIdPath()// 当前节点父ID路径
		);
		// 更新数据
		for (PermissionEntity entity : entities) {
			permissionMapper.updateById(entity);
		}
	}

	// ==============================InnerClass=======================================
	private static class PermissionIdsFilter implements NodeFilter<PermissionNodeVO> {
		private final String[] permissionIds;

		private PermissionIdsFilter(String[] permissionIds) {
			this.permissionIds = permissionIds;
		}

		@Override
		public boolean accept(PermissionNodeVO node, int depth, boolean leaf) {
			return (ArrayUtils.contains(permissionIds, node.getId()) || !leaf);
		}
	}

	private static class PermissionTypeFilter implements NodeFilter<PermissionNodeVO> {
		private final Integer type;

		public PermissionTypeFilter(PermissionTypeEnum type) {
			this.type = type.value();
		}

		@Override
		public boolean accept(PermissionNodeVO model, int depth, boolean leaf) {
			return model.getType().intValue() <= type.intValue();
		}
	}
}
