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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.relucent.base.common.exception.ExceptionHelper;
import com.github.relucent.base.common.page.Page;
import com.github.relucent.base.common.page.Pagination;
import com.github.relucent.base.common.tree.TreeUtil;
import com.github.relucent.base.plugin.mybatis.MybatisHelper;

import yyl.demo.common.constant.Ids;
import yyl.demo.common.constant.Symbols;
import yyl.demo.common.model.BasicNodeVO;
import yyl.demo.common.security.Securitys;
import yyl.demo.common.security.UserPrincipal;
import yyl.demo.common.standard.AuditableUtil;
import yyl.demo.common.util.IdUtil;
import yyl.demo.entity.Department;
import yyl.demo.mapper.DepartmentMapper;
import yyl.demo.service.support.DepartmentNodeAdapter;

/**
 * 部门管理
 */
@Transactional
@Service
public class DepartmentService {

    // ==============================Fields===========================================
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private DepartmentMapper departmentMapper;

    // ==============================Methods==========================================
    /**
     * 新增部门
     * @param organization 部门
     */
    public void insert(Department department) {

        validate(department);

        UserPrincipal principal = Securitys.getPrincipal();

        Department entity = new Department();

        entity.setId(IdUtil.uuid32());
        entity.setName(department.getName());
        entity.setRemark(department.getRemark());
        entity.setIdPath(forceGetIdPath(entity));

        AuditableUtil.setCreated(entity, principal);

        departmentMapper.insert(entity);
    }

    /**
     * 删除部门
     * @param id 部门ID
     */
    public void deleteById(String id) {
        if (departmentMapper.countByParentId(id) != 0) {
            throw ExceptionHelper.prompt("存在子机构，不能被直接删除");
        }
        departmentMapper.deleteById(id);
    }

    /**
     * 修改部门
     * @param organization 部门
     */
    public void update(Department department) {

        validate(department);

        UserPrincipal principal = Securitys.getPrincipal();

        Department entity = departmentMapper.selectById(department.getId());

        if (entity == null) {
            throw ExceptionHelper.prompt("数据不存在");
        }

        String originalIdPath = entity.getIdPath();

        entity.setName(department.getName());
        entity.setRemark(department.getRemark());
        entity.setIdPath(forceGetIdPath(entity));
        AuditableUtil.setUpdated(entity, principal);

        departmentMapper.updateById(entity);

        // IdPath发生更改, 更新子节点
        if (!Objects.equals(originalIdPath, entity.getIdPath())) {
            updateChildrenIdPath(entity);
        }
    }

    /**
     * 查询部门
     * @param id 部门ID
     * @return 部门
     */
    public Department getById(String id) {
        Department entity = departmentMapper.selectById(id);
        if (entity == null) {
            throw ExceptionHelper.prompt("数据不存在或者已经失效");
        }
        String parentId = entity.getParentId();
        if (Ids.ROOT_ID.equals(parentId)) {
            entity.setParentName("/");
        } else {
            Department parentEntity = departmentMapper.selectById(parentId);
            entity.setParentName(parentEntity == null ? "[未知]" : parentEntity.getName());
        }
        return entity;
    }

    /**
     * 分页查询
     * @param pagination 分页条件
     * @param condition 查询条件
     * @return 查询结果
     */
    public Page<Department> pagedQuery(Pagination pagination, Department condition) {
        return MybatisHelper.selectPage(pagination, () -> departmentMapper.findBy(condition));
    }

    /**
     * 查询部门树
     * @return 部门树
     */
    public List<BasicNodeVO> getTree() {
        List<Department> entities = departmentMapper.findAll();
        List<BasicNodeVO> nodes = TreeUtil.buildTree(//
                Ids.DEPT_ROOT_ID, //
                entities, //
                DepartmentNodeAdapter.INSTANCE, //
                Department::getId, //
                Department::getParentId, //
                BasicNodeVO::setChildren//
        );
        return nodes;
    }

    /**
     * 强制刷新机构树索引(ID路径)
     */
    public void forceRefreshIndexes() {
        List<Department> entities = departmentMapper.findAll();
        TreeUtil.recursiveSetIdPath(entities, Department::getId, Department::getParentId, Department::setIdPath, Ids.ROOT_ID, Symbols.SEPARATOR);
        for (Department entity : entities) {
            departmentMapper.updateById(entity);
        }
    }

    /** 更新子节点IP路径 */
    private void updateChildrenIdPath(Department parent) {
        Collection<Department> entities = findAllByParentId(parent.getId());
        TreeUtil.recursiveSetIdPath(entities, Department::getId, Department::getParentId, Department::setIdPath, parent.getId(), parent.getIdPath());
        for (Department entity : entities) {
            departmentMapper.updateById(entity);
        }
    }

    /** 保存时候校验 */
    private void validate(Department department) {
        String parentId = department.getParentId();
        String name = department.getName();
        if (StringUtils.isEmpty(name)) {
            throw ExceptionHelper.prompt("名称不能为空");
        }
        if (!Ids.ROOT_ID.equals(parentId)) {
            Department parentEntity = departmentMapper.selectById(parentId);
            if (parentEntity == null) {
                throw ExceptionHelper.prompt("没有查询到对应的上级");
            }
        }
    }

    /** 构建Id路径 */
    private String forceGetIdPath(Department department) {
        String id = department.getId();
        String parentId = department.getParentId();
        if (Ids.ROOT_ID.equals(parentId)) {
            return Symbols.SEPARATOR + id + Symbols.SEPARATOR;
        }
        List<String> idList = new ArrayList<>();
        idList.add(id);
        while (parentId != null && !Ids.ROOT_ID.equals(parentId)) {
            idList.add(parentId);
            Department parentEntity = departmentMapper.selectById(parentId);
            if (parentEntity == null) {
                break;
            }
            parentId = parentEntity.getParentId();
        }
        StringBuilder idPathBuilder = new StringBuilder();
        idPathBuilder.append(Symbols.SEPARATOR);
        for (int i = idList.size() - 1; i >= 0; i--) {
            idPathBuilder.append(idList.get(i)).append(Symbols.SEPARATOR);
        }
        return idPathBuilder.toString();
    }

    /**
     * 查询子孙部门(包含子孙级别部门)
     * @param parentId 部门ID
     * @return 子部门列表
     */
    private List<Department> findAllByParentId(String parentId) {
        List<Department> entities = new ArrayList<>();
        Queue<String> idQueue = new LinkedList<>();// 待处理队列
        Set<String> idSet = new HashSet<>();// 已经处理集合
        idQueue.add(parentId);// QUEUE<-I
        for (; !idQueue.isEmpty();) {
            String id = idQueue.remove();// QUEUE->I
            idSet.add(id);//
            for (Department entity : departmentMapper.findByParentId(id)) {
                if (!idSet.contains(entity.getId())) {
                    entities.add(entity);
                    idQueue.add(entity.getId());// QUEUE<-I
                }
            }
        }
        return entities;
    }
}
