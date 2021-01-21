package yyl.demo.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.relucent.base.common.exception.ExceptionHelper;
import com.github.relucent.base.common.page.Page;
import com.github.relucent.base.common.page.Pagination;
import com.github.relucent.base.plugin.mybatis.MybatisHelper;

import yyl.demo.common.configuration.properties.SecurityProperties;
import yyl.demo.common.constant.BoolInts;
import yyl.demo.common.constant.Ids;
import yyl.demo.common.constant.Symbols;
import yyl.demo.common.security.Securitys;
import yyl.demo.common.security.UserPrincipal;
import yyl.demo.common.standard.AuditableUtil;
import yyl.demo.common.util.IdUtil;
import yyl.demo.entity.User;
import yyl.demo.entity.UserRole;
import yyl.demo.mapper.DepartmentMapper;
import yyl.demo.mapper.UserMapper;
import yyl.demo.mapper.UserRoleMapper;
import yyl.demo.service.dto.PasswordDTO;

/**
 * 系统用户
 */
@Transactional
@Service
@EnableConfigurationProperties(SecurityProperties.class)
public class UserService {

    // ==============================Fields===========================================
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private DepartmentMapper departmentMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SecurityProperties securityProperties;

    // ==============================Methods==========================================
    /**
     * 新增用户
     * @param user 用户信息
     */
    public void insert(User user) {

        validate(user);

        UserPrincipal principal = Securitys.getPrincipal();

        User entity = new User();

        entity.setId(IdUtil.uuid32());
        entity.setDepartmentId(user.getDepartmentId());
        entity.setUsername(user.getUsername());

        String rawPassword = ObjectUtils.defaultIfNull(user.getPassword(), securityProperties.getDefaultUserPassword());
        entity.setPassword(passwordEncoder.encode(rawPassword));

        entity.setRealname(user.getRealname());
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

        UserPrincipal principal = Securitys.getPrincipal();

        User entity = userMapper.selectById(user.getId());

        if (entity == null) {
            throw ExceptionHelper.prompt("用户不存在或者已经失效");
        }

        entity.setDepartmentId(user.getDepartmentId());
        entity.setUsername(user.getUsername());

        String rawPassword = user.getPassword();

        if (StringUtils.isNotEmpty(rawPassword) && !Symbols.PASSWORD_PLACEHOLDER.equals(rawPassword)) {
            entity.setPassword(passwordEncoder.encode(rawPassword));
        }

        entity.setRealname(user.getRealname());
        entity.setRemark(user.getRemark());
        entity.setEnabled(BoolInts.normalize(user.getEnabled()));

        AuditableUtil.setUpdated(entity, principal);
        userMapper.updateById(entity);
    }

    /**
     * 用户启用禁用
     * @param id 用户ID
     * @param enabled 是否启用
     */
    public void enable(String id, Integer enabled) {
        UserPrincipal principal = Securitys.getPrincipal();
        User entity = new User();
        entity.setId(id);
        entity.setEnabled(BoolInts.normalize(enabled));
        AuditableUtil.setUpdated(entity, principal);
        userMapper.updateById(entity);
    }

    /**
     * 重置用户密码
     * @param id 用户ID
     */
    public void resetPassword(String id) {
        UserPrincipal principal = Securitys.getPrincipal();
        User entity = userMapper.selectById(id);
        if (entity == null) {
            throw ExceptionHelper.prompt("用户不存在或者已经失效");
        }
        String password = securityProperties.getDefaultUserPassword();
        entity.setPassword(passwordEncoder.encode(password));
        AuditableUtil.setUpdated(entity, principal);
        userMapper.updateById(entity);
    }

    /**
     * 修改当前用户密码
     * @param passwordDto 新旧密码
     */
    public void updateCurrentPassword(PasswordDTO passwordDto) {

        String oldPassword = passwordDto.getOldPassword();
        String newPassword = passwordDto.getNewPassword();

        if (StringUtils.isEmpty(oldPassword)) {
            throw ExceptionHelper.prompt("请输入旧密码");
        }
        if (StringUtils.isEmpty(newPassword)) {
            throw ExceptionHelper.prompt("请输入新密码");
        }

        UserPrincipal principal = Securitys.getPrincipal();
        String userId = principal.getId();
        User entity = userMapper.selectById(userId);
        if (entity == null) {
            throw ExceptionHelper.prompt("用户未登录");
        }
        if (!passwordEncoder.matches(passwordDto.getOldPassword(), entity.getPassword())) {
            throw ExceptionHelper.prompt("旧密码输入错误");
        }
        entity.setPassword(passwordEncoder.encode(newPassword));
        AuditableUtil.setUpdated(entity, principal);
        userMapper.updateById(entity);
    }

    /**
     * 查询用户
     * @param id 用户ID
     * @return 用户信息
     */
    public User getById(String id) {
        User entity = userMapper.selectById(id);
        if (entity != null) {
            entity.setPassword(Symbols.PASSWORD_PLACEHOLDER);
        }
        return entity;
    }

    /**
     * 查询用户
     * @param username 用户名
     * @return 用户信息
     */
    public User getByUsername(String username) {
        return userMapper.selectByUsername(username);
    }

    /**
     * 分页查询
     * @param pagination 分页条件
     * @param condition 查询条件
     * @return 分页结果
     */
    public Page<User> pagedQuery(Pagination pagination, User condition) {
        return MybatisHelper.selectPage(pagination, () -> userMapper.selectListBy(condition));
    }

    /**
     * 查询用户的角色
     * @param userId 用户ID
     * @return 角色ID列表
     */
    public List<String> findRoleIdByUserId(String userId) {
        return userRoleMapper.selectRoleIdListByUserId(userId);
    }

    /**
     * 更新用户的角色
     * @param userId 用户ID
     * @param roleIds 角色 ID
     */
    public void updateUserRole(String userId, String[] roleIds) {
        UserPrincipal principal = Securitys.getPrincipal();
        Map<String, UserRole> roleIdMap = new HashMap<>();
        for (UserRole entity : userRoleMapper.selectListByUserId(userId)) {
            roleIdMap.put(entity.getRoleId(), entity);
        }
        List<UserRole> newEntities = new ArrayList<>();
        for (String roleId : roleIds) {
            UserRole entity = roleIdMap.remove(roleId);
            if (entity == null) {
                entity = new UserRole();
                entity.setId(IdUtil.uuid32());
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
        String username = user.getUsername();
        String realname = user.getRealname();
        String departmentId = user.getDepartmentId();

        if (StringUtils.isEmpty(username)) {
            throw ExceptionHelper.prompt("用户名不能为空");
        }
        if (StringUtils.isEmpty(realname)) {
            throw ExceptionHelper.prompt("姓名不能为空");
        }
        User entity = userMapper.selectByUsername(username);
        if (entity != null && !Objects.equals(entity.getId(), id)) {
            throw ExceptionHelper.prompt("已经存在相同账号");
        }

        if (!Ids.UNDEFINED_ID.equals(departmentId) && departmentMapper.selectById(departmentId) == null) {
            throw ExceptionHelper.prompt("该组织机构无效");
        }
    }
}
