package yyl.demo.service;

import java.security.KeyPair;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.relucent.base.common.collection.CollectionUtil;
import com.github.relucent.base.common.exception.ExceptionHelper;

import lombok.extern.slf4j.Slf4j;
import yyl.demo.common.constant.SecurityConstant;
import yyl.demo.common.enums.IntBoolEnum;
import yyl.demo.common.util.RsaUtil;
import yyl.demo.entity.UserEntity;
import yyl.demo.mapper.PermissionMapper;
import yyl.demo.mapper.RoleMapper;
import yyl.demo.mapper.UserMapper;
import yyl.demo.model.dto.UsernamePasswordDTO;
import yyl.demo.model.ro.PermissionInfoRO;
import yyl.demo.model.ro.RoleInfoRO;
import yyl.demo.model.vo.RsaPublicKeyVO;
import yyl.demo.model.vo.UserInfoVO;
import yyl.demo.security.Securitys;
import yyl.demo.security.model.AccessToken;
import yyl.demo.security.model.UserPrincipal;
import yyl.demo.security.store.AuthenticationTokenStore;
import yyl.demo.security.store.RsaKeyPairStore;

/**
 * 身份验证令牌服务
 */
@Transactional(rollbackFor = Exception.class)
@Service
@Slf4j
public class AuthenticationTokenService {

    // ==============================Fields===========================================
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private PermissionMapper permissionMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationTokenStore authenticationTokenStore;

    @Autowired
    private RsaKeyPairStore rsaKeyPairStore;

    // ==============================Methods==========================================
    /**
     * 获取加密公钥
     * @return 加密公钥
     */
    public RsaPublicKeyVO getPublicKey() {
        KeyPair keyPair = RsaUtil.generateKeyPait();
        String id = rsaKeyPairStore.put(keyPair);
        RsaPublicKeyVO vo = new RsaPublicKeyVO();
        vo.setRsaId(id);
        vo.setPublicKey(RsaUtil.encodeBase64String(keyPair.getPublic()));
        return vo;
    }

    /**
     * 用户登录(用户名密码方式获得用户令牌信息)
     * @param dto 用户名密码
     * @return 用户令牌信息
     */
    public AccessToken login(UsernamePasswordDTO dto) {
        String username = dto.getUsername();
        String password = dto.getPassword();
        String rsaId = dto.getRsaId();

        if (StringUtils.isEmpty(username)) {
            throw ExceptionHelper.prompt("请输入用户名！");
        }
        if (StringUtils.isEmpty(password)) {
            throw ExceptionHelper.prompt("请输入密码！");
        }

        if (StringUtils.isNotEmpty(rsaId)) {
            KeyPair keyPair = rsaKeyPairStore.get(rsaId);
            if (keyPair == null) {
                throw ExceptionHelper.prompt("客户端凭证无效！");
            }
            try {
                password = RsaUtil.decryptBase64(password, keyPair.getPrivate());
            } catch (Exception e) {
                log.error("!", e);
                throw ExceptionHelper.prompt("客户端凭证失效!");
            }
        }
        UserEntity entity = userMapper.getByUsername(username);
        if (entity == null) {
            throw ExceptionHelper.prompt("用户名错误，请检查用户名！");
        }
        if (!passwordEncoder.matches(password, entity.getPassword())) {
            throw ExceptionHelper.prompt("用户名或密码错误！");
        }
        if (!IntBoolEnum.Y.is(entity.getEnabled())) {
            throw ExceptionHelper.prompt("用户已被禁用，请联系管理员！");
        }

        UserPrincipal principal = buildPrincipal(entity);
        AccessToken token = authenticationTokenStore.putPrincipal(principal);
        return token;
    }

    /**
     * 用户注销
     */
    public void logout() {
        String token = Securitys.getAccessToken();
        authenticationTokenStore.removeToken(token);
    }

    /**
     * 获得当前登录用户信息
     * @return 前登录用户信息
     */
    public UserInfoVO getCurrentUser() {
        UserPrincipal principal = Securitys.getPrincipal();
        UserInfoVO vo = new UserInfoVO();
        vo.setId(principal.getId());
        vo.setUsername(principal.getUsername());
        vo.setRealname(principal.getRealname());
        return vo;
    }

    /**
     * 构建 {@code UserPrincipal}
     * @param entity 用户实体
     * @return 用户信息
     */
    private UserPrincipal buildPrincipal(UserEntity entity) {

        String userId = entity.getId();

        Set<String> authoritySet = new HashSet<>();
        List<String> roldIdList = new ArrayList<>();
        List<String> permissionIdList = new ArrayList<>();

        for (RoleInfoRO roleInfo : roleMapper.findRoleInfoByUserId(userId)) {
            roldIdList.add(roleInfo.getId());
            authoritySet.add(SecurityConstant.ROLE_PREFIX + roleInfo.getCode());
        }
        String[] roleIds = CollectionUtil.toArray(roldIdList, String.class);

        for (PermissionInfoRO permissionInfo : permissionMapper.findPermissionInfoByRoleIds(roleIds)) {
            permissionIdList.add(permissionInfo.getId());
            authoritySet.add(permissionInfo.getCode());
        }
        String[] permissionIds = CollectionUtil.toArray(permissionIdList, String.class);

        UserPrincipal principal = new UserPrincipal();
        principal.setId(userId);
        principal.setOrganizationId(entity.getOrganizationId());
        principal.setUsername(entity.getUsername());
        principal.setRealname(entity.getRealname());
        principal.setRoleIds(roleIds);
        principal.setPermissionIds(permissionIds);
        principal.setAuthorities(CollectionUtil.toArray(authoritySet, String.class));
        return principal;
    }

}