package yyl.demo.security.model;

import java.beans.Transient;
import java.io.Serializable;
import java.security.Principal;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.github.relucent.base.common.constant.StringConstants;

import lombok.Data;
import yyl.demo.common.constant.IdConstant;

/**
 * 登录用户信息对象
 */
@Data
@SuppressWarnings("serial")
public class UserPrincipal implements Principal, Serializable {

    // ========================================Fields=========================================
    /** 用户ID */
    private String id;
    /** 用户名 */
    private String username;
    /** 用户密码 */
    private String password;

    /** 真实姓名 */
    private String realname;
    /** 所属机构 */
    private String organizationId;

    /** 角色 */
    private String[] roleIds = {};
    /** 授权 */
    private String[] permissionIds = {};

    /** 帐户未过期 */
    private boolean accountNonExpired = true;
    /** 帐户未锁定 */
    private boolean accountNonLocked = true;
    /** 凭据未过期 */
    private boolean credentialsNonExpired = true;
    /** 状态启用 */
    private boolean enabled = true;
    /** 已经赋予的权限 */
    private String[] authorities = {};

    // ==============================OverrideMethods==========================================
    @Override
    @Transient
    public String getName() {
        return id + "@" + username;
    }

    // ========================================Methods========================================
    /**
     * 用户是否有该角色
     * @param roleId 角色
     * @return 拥有该角色返回true，否则返回false
     */
    @Transient
    public boolean hasRole(String roleId) {
        return ArrayUtils.contains(roleIds, roleId);
    }

    // ========================================StaticMethods==================================
    /**
     * 是否匿名用户
     * @param principal 用户信息
     * @return 匿名用户返回真
     */
    public static boolean isAnonymousUser(UserPrincipal principal) {
        return principal == null || ANONYMOUS.equals(principal) || IdConstant.isNonId(principal.getId());
    }

    // ========================================Constants======================================
    /** 未登录用户 */
    public static final UserPrincipal ANONYMOUS;
    static {
        ANONYMOUS = new UserPrincipal();
        ANONYMOUS.setId(StringUtils.EMPTY);
        ANONYMOUS.setUsername(StringUtils.EMPTY);
        ANONYMOUS.setRealname(StringConstants.EMPTY);
        ANONYMOUS.setOrganizationId(StringConstants.EMPTY);
    }
}
