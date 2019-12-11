package yyl.demo.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.relucent.base.common.exception.ExceptionHelper;
import com.github.relucent.base.common.tree.TreeUtil;

import yyl.demo.common.constant.IdConstant;
import yyl.demo.common.constant.SymbolConstant;
import yyl.demo.common.enums.IntBoolEnum;
import yyl.demo.common.standard.AuditableUtil;
import yyl.demo.common.util.IdUtil;
import yyl.demo.entity.OrganizationEntity;
import yyl.demo.kit.OrganizationKit;
import yyl.demo.mapper.OrganizationMapper;
import yyl.demo.mapper.UserMapper;
import yyl.demo.model.dto.OrganizationDTO;
import yyl.demo.model.vo.OrganizationNodeVO;
import yyl.demo.model.vo.OrganizationVO;
import yyl.demo.security.Securitys;

/**
 * 机构管理
 */
@Transactional
@Service
public class OrganizationService {

    // ==============================Fields===========================================
    @Autowired
    private OrganizationMapper organizationMapper;
    @Autowired
    private UserMapper userMapper;

    // ==============================Methods==========================================
    /**
     * 新增机构
     * @param dto 机构信息
     */
    public void save(OrganizationDTO dto) {
        dto.setId(null);
        validate(dto);
        OrganizationEntity entity = new OrganizationEntity();
        OrganizationKit.copyProperties(dto, entity);
        entity.setId(IdUtil.timeId());
        entity.setIdPath(forceGetIdPath(entity));
        String identity = Securitys.getUserId();
        AuditableUtil.setCreated(entity, identity);
        organizationMapper.insert(entity);
    }

    /**
     * 删除机构(标记删除)
     * @param id 机构ID
     */
    public void deleteById(String id) {
        if (userMapper.countByOrganizationId(id) > 0) {
            throw ExceptionHelper.prompt("该机构下存在用户，不能被直接删除！");
        }
        OrganizationEntity updated = new OrganizationEntity();
        updated.setId(id);
        updated.setDeleted(IntBoolEnum.Y.value());
        String identity = Securitys.getUserId();
        AuditableUtil.setUpdated(updated, identity);
        organizationMapper.updateById(updated);
    }

    /**
     * 更新机构
     * @param dto 机构信息
     */
    public void update(OrganizationDTO dto) {
        validate(dto);
        OrganizationEntity entity = organizationMapper.getById(dto.getId());
        if (entity == null) {
            throw ExceptionHelper.prompt("机构不存在或者已经失效");
        }
        OrganizationKit.copyProperties(dto, entity);

        String originalIdPath = entity.getIdPath();

        entity.setIdPath(forceGetIdPath(entity));

        String identity = Securitys.getUserId();
        AuditableUtil.setUpdated(entity, identity);
        organizationMapper.updateById(entity);
        // IdPath发生更改, 更新子节点
        if (!Objects.equals(originalIdPath, entity.getIdPath())) {
            updateChildrenIdPath(entity);
        }
    }

    /**
     * 查询机构
     * @param id 机构ID
     * @return 机构信息
     */
    public OrganizationVO getById(String id) {
        OrganizationEntity entity = organizationMapper.getById(id);
        if (entity == null) {
            throw ExceptionHelper.prompt("机构不存在或者已经失效");
        }
        OrganizationVO vo = OrganizationKit.toVO(entity);
        String parentId = entity.getParentId();
        if (IdConstant.ROOT_ID.equals(parentId)) {
            vo.setParentName("/");
        } else {
            OrganizationEntity parentEntity = organizationMapper.getById(parentId);
            vo.setParentName(parentEntity == null ? "[未知]" : parentEntity.getName());
        }
        return vo;
    }

    /**
     * 查询机构名称
     * @param id 机构ID
     * @return 机构名称
     */
    public String getNameById(String id) {
        return organizationMapper.getNameById(id);
    }

    /**
     * 获得机构树
     * @return 机构树
     */
    public List<OrganizationNodeVO> getOrganizationTree() {
        List<OrganizationNodeVO> nodes = organizationMapper.findAllNode();
        return TreeUtil.buildTree(//
                IdConstant.ROOT_ID, //
                nodes, //
                n -> n, //
                OrganizationNodeVO::getId, //
                OrganizationNodeVO::getParentId, //
                OrganizationNodeVO::setChildren//
        );
    }

    /**
     * 强制刷新机构树索引(ID路径)
     */
    public void forceRefreshIdPath() {
        List<OrganizationEntity> entities = organizationMapper.findAll();
        // 递归设置ID路径
        TreeUtil.recursiveSetIdPath(entities, // 节点列表
                OrganizationEntity::getId, // 获取ID的方法
                OrganizationEntity::getParentId, // 获取父ID的方法
                OrganizationEntity::setIdPath, // 设置ID路径的方法
                IdConstant.ROOT_ID, // 当前节点父ID
                SymbolConstant.SEPARATOR// 当前节点父ID路径
        );
        // 更新数据
        for (OrganizationEntity entity : entities) {
            organizationMapper.updateById(entity);
        }
    }

    // ==============================ToolMethods======================================
    private void validate(OrganizationDTO dto) {
        String id = dto.getId();
        String name = dto.getName();
        if (StringUtils.isEmpty(name)) {
            throw ExceptionHelper.prompt("机构名不能为空");
        }
        OrganizationEntity entity = organizationMapper.getByName(name);
        if (entity != null && !Objects.equals(entity.getId(), id)) {
            throw ExceptionHelper.prompt("已经存在相同的机构名");
        }
    }

    /**
     * 构建Id路径
     * @param entity 机构实体
     * @return 机构的ID路径
     */
    private String forceGetIdPath(OrganizationEntity entity) {
        String id = entity.getId();
        String parentId = entity.getParentId();
        if (IdConstant.ROOT_ID.equals(parentId)) {
            return SymbolConstant.SEPARATOR + id + SymbolConstant.SEPARATOR;
        }
        List<String> idList = new ArrayList<>();
        idList.add(id);
        while (parentId != null && !IdConstant.ROOT_ID.equals(parentId)) {
            idList.add(parentId);
            OrganizationEntity parentEntity = organizationMapper.selectById(parentId);
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
     * 查询子孙机构(包含子孙级别机构)
     * @param parentId 父机构ID
     * @return 机构列表
     */
    private List<OrganizationEntity> findAllByParentId(String parentId) {
        List<OrganizationEntity> entities = new ArrayList<>();
        Queue<String> idQueue = new LinkedList<>();// 待处理队列
        Set<String> idSet = new HashSet<>();// 已经处理集合
        idQueue.add(parentId);// QUEUE<-I
        for (; !idQueue.isEmpty();) {
            String id = idQueue.remove();// QUEUE->I
            idSet.add(id);//
            for (OrganizationEntity entity : organizationMapper.findByParentId(id)) {
                if (!idSet.contains(entity.getId())) {
                    entities.add(entity);
                    idQueue.add(entity.getId());// QUEUE<-I
                }
            }
        }
        return entities;
    }

    /**
     * 更新子节点ID路径
     * @param parent 父节点
     */
    private void updateChildrenIdPath(OrganizationEntity parent) {
        Collection<OrganizationEntity> entities = findAllByParentId(parent.getId());
        // 递归设置ID路径
        TreeUtil.recursiveSetIdPath(entities, // 节点列表
                OrganizationEntity::getId, // 获取ID的方法
                OrganizationEntity::getParentId, // 获取父ID的方法
                OrganizationEntity::setIdPath, // 设置ID路径的方法
                parent.getId(), // 当前节点父ID
                parent.getIdPath()// 当前节点父ID路径
        );
        // 更新数据
        for (OrganizationEntity entity : entities) {
            organizationMapper.updateById(entity);
        }
    }
}
