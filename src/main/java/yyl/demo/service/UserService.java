package yyl.demo.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.relucent.base.plug.mybatis.MybatisHelper;
import com.github.relucent.base.plug.security.Principal;
import com.github.relucent.base.plug.security.Securitys;
import com.github.relucent.base.util.expection.ExceptionHelper;
import com.github.relucent.base.util.page.Page;
import com.github.relucent.base.util.page.Pagination;

import yyl.demo.common.BaseConstants.BoolInts;
import yyl.demo.common.BaseConstants.Ids;
import yyl.demo.common.BaseConstants.Symbols;
import yyl.demo.common.configuration.CustomProperties;
import yyl.demo.common.identifier.IdHelper;
import yyl.demo.common.standard.AuditableUtil;
import yyl.demo.entity.User;
import yyl.demo.entity.UserRole;
import yyl.demo.mapper.DepartmentMapper;
import yyl.demo.mapper.UserMapper;
import yyl.demo.mapper.UserRoleMapper;

/**
 * 系统用户
 */
@Transactional
@Service
public class UserService {

    // ==============================Fields===========================================
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private DepartmentMapper departmentMapper;

    @Autowired
    private Securitys securitys;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomProperties customProperties;

    // ==============================Methods==========================================
    /**
     * 新增用户
     * @param user 用户信息
     */
    public void insert(User user) {

        validate(user);

        Principal principal = securitys.getPrincipal();

        User entity = new User();

        IdHelper.setIfEmptyId(entity);
        entity.setDepartmentId(user.getDepartmentId());
        entity.setAccount(user.getAccount());

        String rawPassword = ObjectUtils.defaultIfNull(user.getPassword(), customProperties.getDefaultUserPassword());
        entity.setPassword(passwordEncoder.encode(rawPassword));

        entity.setName(user.getName());
        entity.setRemark(user.getRemark());
        entity.setEnabled(BoolInts.normalize(user.getEnabled()));

        AuditableUtil.setCreated(entity, principal);
        userMapper.insert(entity);
    }

    /**
     * 删除用户(标记删除)
     * @param id 用户ID
     */
    public void deleteById(String id) {
        if (Ids.ADMIN_ID.equals(id)) {
            throw ExceptionHelper.prompt("系统管理员不能被删除");
        }
        userMapper.deleteById(id);
        userRoleMapper.deleteByUserId(id);
    }

    /**
     * 更新用户
     * @param user 用户信息
     */
    public void update(User user) {
        validate(user);

        Principal principal = securitys.getPrincipal();

        User entity = userMapper.getById(user.getId());

        if (entity == null) {
            throw ExceptionHelper.prompt("用户不存在或者已经失效");
        }

        entity.setDepartmentId(user.getDepartmentId());
        entity.setAccount(user.getAccount());

        String rawPassword = user.getPassword();

        if (StringUtils.isNotEmpty(rawPassword) && !Symbols.PASSWORD_PLACEHOLDER.equals(rawPassword)) {
            entity.setPassword(passwordEncoder.encode(rawPassword));
        }

        entity.setName(user.getName());
        entity.setRemark(user.getRemark());
        entity.setEnabled(BoolInts.normalize(user.getEnabled()));

        AuditableUtil.setLastModified(entity, principal);
        userMapper.update(entity);
    }

    /**
     * 用户启用禁用
     * @param user 用户信息
     */
    public void enable(String id, Integer enabled) {
        Principal principal = securitys.getPrincipal();
        User entity = new User();
        entity.setId(id);
        entity.setEnabled(BoolInts.normalize(enabled));
        AuditableUtil.setLastModified(entity, principal);
        userMapper.update(entity);
    }

    /**
     * 查询用户
     * @param id 用户ID
     * @return 用户信息
     */
    public User getById(String id) {
        User entity = userMapper.getById(id);
        if (entity != null) {
            entity.setPassword(Symbols.PASSWORD_PLACEHOLDER);
        }
        return entity;
    }

    /**
     * 查询用户
     * @param account 账号
     * @return 用户信息
     */
    public User getByAccount(String account) {
        return userMapper.getByAccount(account);
    }

    /**
     * 分页查询
     * @param pagination 分页条件
     * @param condition 查询条件
     * @return 分页结果
     */
    public Page<User> pagedQuery(Pagination pagination, User condition) {
        return MybatisHelper.selectPage(pagination, () -> userMapper.findBy(condition));
    }

    /**
     * 查询用户的角色
     * @param userId 用户ID
     * @return 角色ID列表
     */
    public List<String> findRoleIdByUserId(String userId) {
        return userRoleMapper.findRoleIdByUserId(userId);
    }

    /**
     * 更新用户的角色
     * @param userId 用户ID
     * @param roleIds 角色 ID
     */
    public void updateUserRole(String userId, String[] roleIds) {
        Principal principal = securitys.getPrincipal();
        Map<String, UserRole> roleIdMap = new HashMap<>();
        for (UserRole entity : userRoleMapper.findByUserId(userId)) {
            roleIdMap.put(entity.getRoleId(), entity);
        }
        List<UserRole> newEntities = new ArrayList<>();
        for (String roleId : roleIds) {
            UserRole entity = roleIdMap.remove(roleId);
            if (entity == null) {
                entity = new UserRole();
                IdHelper.setIfEmptyId(entity);
                entity.setUserId(userId);
                entity.setRoleId(roleId);
                AuditableUtil.setCreated(entity, principal);
                newEntities.add(entity);
            }
        }
        for (UserRole entity : roleIdMap.values()) {
            userRoleMapper.deleteById(entity.getId());
        }
        for (UserRole entity : newEntities) {
            userRoleMapper.insert(entity);
        }
    }

    // ==============================ToolMethods======================================
    /** 验证数据 */
    private void validate(User user) {
        String id = user.getId();
        String account = user.getAccount();
        String name = user.getName();
        String departmentId = user.getDepartmentId();

        if (StringUtils.isEmpty(account)) {
            throw ExceptionHelper.prompt("账号不能为空");
        }
        if (StringUtils.isEmpty(name)) {
            throw ExceptionHelper.prompt("姓名不能为空");
        }
        User entity = userMapper.getByAccount(account);
        if (entity != null && !Objects.equals(entity.getId(), id)) {
            throw ExceptionHelper.prompt("已经存在相同账号");
        }

        if (!Ids.UNDEFINED_ID.equals(departmentId) && departmentMapper.getById(departmentId) == null) {
            throw ExceptionHelper.prompt("该组织机构无效");
        }
    }

}
