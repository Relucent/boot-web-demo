package yyl.demo.service;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.relucent.base.common.exception.ExceptionUtil;
import com.github.relucent.base.common.identifier.IdUtil;

import yyl.demo.common.constant.IdConstant;
import yyl.demo.common.constant.SymbolConstant;
import yyl.demo.common.enums.IntBoolEnum;
import yyl.demo.common.model.PageVO;
import yyl.demo.common.model.PaginationQO;
import yyl.demo.common.standard.AuditableUtil;
import yyl.demo.common.util.RsaUtil;
import yyl.demo.entity.UserEntity;
import yyl.demo.entity.UserRoleEntity;
import yyl.demo.kit.UserKit;
import yyl.demo.mapper.OrganizationMapper;
import yyl.demo.mapper.UserMapper;
import yyl.demo.mapper.UserRoleMapper;
import yyl.demo.model.dto.PasswordDTO;
import yyl.demo.model.dto.UserDTO;
import yyl.demo.model.qo.UserQO;
import yyl.demo.model.ro.UserRO;
import yyl.demo.model.vo.UserVO;
import yyl.demo.security.Securitys;
import yyl.demo.security.store.RsaKeyPairStore;

/**
 * 系统用户
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class UserService {

    // ==============================Fields===========================================
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private OrganizationMapper organizationMapper;

    @Autowired
    private RsaKeyPairStore rsaKeyPairStore;
    @Autowired
    private PasswordEncoder passwordEncoder;

    // ==============================Methods==========================================
    /**
     * 新增用户
     * @param dto 用户信息
     */
    public void save(UserDTO dto) {

        dto.setId(null);
        validate(dto);

        UserEntity entity = new UserEntity();
        UserKit.copyProperties(dto, entity);

        entity.setId(IdUtil.uuid32());

        // 初始密码和用户名一致
        entity.setPassword(passwordEncoder.encode(entity.getUsername()));

        if (entity.getEnabled() == null) {
            entity.setEnabled(IntBoolEnum.N.value());
        }

        String identity = Securitys.getUserId();
        AuditableUtil.setCreated(entity, identity);
        userMapper.insert(entity);

        // 更新用户角色
        updateUserRole(entity.getId(), dto.getRoleIds());
    }

    /**
     * 删除用户(标记删除)
     * @param id 用户ID
     */
    public void deleteById(String id) {
        if (IdConstant.ADMIN_ID.equals(id)) {
            throw ExceptionUtil.prompt("系统管理员不能被删除");
        }
        userRoleMapper.deleteByUserId(id);

        // 删除用户数据(标记删除)
        UserEntity updated = new UserEntity();
        updated.setId(id);
        updated.setDeleted(IntBoolEnum.Y.value());
        String identity = Securitys.getUserId();
        AuditableUtil.setUpdated(updated, identity);
        userMapper.updateById(updated);
    }

    /**
     * 更新用户
     * @param dto 用户信息
     */
    public void update(UserDTO dto) {
        validate(dto);

        UserEntity entity = userMapper.selectById(dto.getId());
        if (entity == null) {
            throw ExceptionUtil.prompt("用户不存在或者已经失效");
        }
        UserKit.copyProperties(dto, entity);

        // 不修改密码
        entity.setPassword(null);

        String identity = Securitys.getUserId();
        AuditableUtil.setUpdated(entity, identity);
        userMapper.updateById(entity);

        // 更新用户角色
        updateUserRole(entity.getId(), dto.getRoleIds());
    }

    /**
     * 用户启用禁用
     * @param id      用户ID
     * @param enabled 是否启用
     */
    public void enableById(String id, Integer enabled) {
        if (IdConstant.ADMIN_ID.equals(id)) {
            throw ExceptionUtil.prompt("内置账号不能被禁用");
        }
        IntBoolEnum bool = IntBoolEnum.of(enabled, null);
        if (bool != null && StringUtils.isNoneEmpty(id)) {
            UserEntity updated = new UserEntity();
            updated.setId(id);
            updated.setEnabled(bool.value());
            String identity = Securitys.getUserId();
            AuditableUtil.setCreated(updated, identity);
            userMapper.updateById(updated);
        }
    }

    /**
     * 重置用户密码
     * @param id 用户ID
     */
    public void resetPasswordById(String id) {
        String identity = Securitys.getUserId();
        UserEntity entity = userMapper.selectById(id);
        if (entity == null) {
            throw ExceptionUtil.prompt("用户不存在或者已经失效");
        }
        String password = entity.getUsername();
        entity.setPassword(passwordEncoder.encode(password));
        AuditableUtil.setUpdated(entity, identity);
        userMapper.updateById(entity);
    }

    /**
     * 修改当前用户密码
     * @param passwordDto 新旧密码
     */
    public void updatePasswordByCurrent(PasswordDTO passwordDto) {

        String identity = Securitys.getUserId();
        String oldPassword = passwordDto.getOldPassword();
        String newPassword = passwordDto.getNewPassword();
        String rsaId = passwordDto.getRsaId();

        if (StringUtils.isEmpty(oldPassword)) {
            throw ExceptionUtil.prompt("请输入旧密码");
        }
        if (StringUtils.isEmpty(newPassword)) {
            throw ExceptionUtil.prompt("请输入新密码");
        }

        // 使用RSA加密
        if (StringUtils.isNotEmpty(rsaId)) {
            KeyPair keyPair = rsaKeyPairStore.get(rsaId);
            if (keyPair == null) {
                throw ExceptionUtil.prompt("客户端秘钥失效！");
            }
            try {
                PrivateKey privateKey = keyPair.getPrivate();
                oldPassword = RsaUtil.decryptBase64(oldPassword, privateKey);
                newPassword = RsaUtil.decryptBase64(newPassword, privateKey);
                rsaKeyPairStore.remove(rsaId);
            } catch (Exception e) {
                throw ExceptionUtil.prompt("客户端秘钥无效!");
            }
        }

        String userId = identity;
        UserEntity entity = userMapper.selectById(userId);
        if (entity == null) {
            throw ExceptionUtil.prompt("用户未登录");
        }
        if (!passwordEncoder.matches(oldPassword, entity.getPassword())) {
            throw ExceptionUtil.prompt("旧密码输入错误");
        }
        entity.setPassword(passwordEncoder.encode(newPassword));
        AuditableUtil.setUpdated(entity, identity);
        userMapper.updateById(entity);
    }

    /**
     * 查询用户
     * @param id 用户ID
     * @return 用户信息
     */
    public UserVO getById(String id) {
        UserEntity entity = userMapper.selectById(id);
        if (entity != null) {
            entity.setPassword(SymbolConstant.PASSWORD_PLACEHOLDER);
        }
        UserVO vo = UserKit.toVO(entity);
        vo.setOrganizationName(organizationMapper.getNameById(entity.getOrganizationId()));

        List<String> roleIds = userRoleMapper.findRoleIdListByUserId(id);
        vo.setRoleIds(roleIds);
        return vo;
    }

    /**
     * 查询用户
     * @param username 用户名
     * @return 用户信息
     */
    public UserEntity getByUsername(String username) {
        return userMapper.getByUsername(username);
    }

    /**
     * 分页查询
     * @param pagination 分页条件
     * @return 分页结果
     */
    public PageVO<UserRO> list(PaginationQO<UserQO> pagination) {
        PageVO<UserEntity> page = userMapper.findByCriteria(pagination);
        return page.mapRecords(UserKit::toRO);
    }

    /**
     * 更新用户的角色
     * @param userId  用户ID
     * @param roleIds 角色 ID
     */
    private void updateUserRole(String userId, Collection<String> roleIds) {
        if (roleIds == null) {
            return;
        }
        String identity = Securitys.getUserId();
        Map<String, UserRoleEntity> roleIdMap = new HashMap<>();
        for (UserRoleEntity entity : userRoleMapper.findByUserId(userId)) {
            roleIdMap.put(entity.getRoleId(), entity);
        }
        List<UserRoleEntity> newEntities = new ArrayList<>();
        for (String roleId : roleIds) {
            UserRoleEntity entity = roleIdMap.remove(roleId);
            if (entity == null) {
                entity = new UserRoleEntity();
                entity.setId(IdUtil.uuid32());
                entity.setUserId(userId);
                entity.setRoleId(roleId);
                AuditableUtil.setCreated(entity, identity);
                newEntities.add(entity);
            }
        }
        for (UserRoleEntity entity : roleIdMap.values()) {
            userRoleMapper.deleteById(entity.getId());
        }
        for (UserRoleEntity entity : newEntities) {
            userRoleMapper.insert(entity);
        }
    }

    // ==============================ToolMethods======================================
    /**
     * 验证数据
     * @param dto 用户数据
     */
    private void validate(UserDTO dto) {
        String id = dto.getId();
        String username = dto.getUsername();
        String realname = dto.getRealname();
        String organizationId = dto.getOrganizationId();

        if (StringUtils.isEmpty(username)) {
            throw ExceptionUtil.prompt("用户名不能为空");
        }
        if (StringUtils.isEmpty(realname)) {
            throw ExceptionUtil.prompt("姓名不能为空");
        }
        UserEntity entity = userMapper.getByUsername(username);
        if (entity != null && !Objects.equals(entity.getId(), id)) {
            throw ExceptionUtil.prompt("已经存在相同账号");
        }
        if (IdConstant.ADMIN_ID.equals(id) && organizationMapper.getById(organizationId) == null) {
            throw ExceptionUtil.prompt("该组织机构无效");
        }
    }
}
