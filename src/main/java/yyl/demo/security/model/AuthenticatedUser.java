package yyl.demo.security.model;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 认证用户对象
 */
@SuppressWarnings("serial")
public class AuthenticatedUser implements UserDetails {

    // ========================================Fields=========================================
    /** 用户信息 */
    private UserPrincipal principal;
    /** 帐户未过期 */
    private boolean accountNonExpired = true;
    /** 帐户未锁定 */
    private boolean accountNonLocked = true;
    /** 凭据未过期 */
    private boolean credentialsNonExpired = true;
    /** 状态启用 */
    private boolean enabled = true;
    /** 已经赋予的权限 */
    private Set<GrantedAuthority> authorities = null;

    // ========================================Constructors===================================、
    /**
     * 构造函数
     * @param principal {@link UserPrincipal}
     */
    public AuthenticatedUser(UserPrincipal principal) {
        this.principal = principal;
    }

    // ========================================Methods========================================
    /**
     * 获得用户信息
     * @return 用户信息
     */
    public UserPrincipal getPrincipal() {
        return principal;
    }

    // ========================================OverrideMethods================================
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = this.authorities;
        if (authorities == null) {
            authorities = Optional.ofNullable(principal.getAuthorities())
                    // 处理空的情况
                    .map(Stream::of).orElse(Stream.empty()).map(StringUtils::trim).filter(StringUtils::isNotEmpty)
                    // 去重，转换字符串为 GrantedAuthority
                    .distinct().map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
            this.authorities = authorities;
        }
        return authorities;
    }

    @Override
    public String getUsername() {
        return principal.getUsername();
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    // ========================================StaticMethods==================================
    /**
     * 获得认证用户对象
     * @param principal 用户信息
     * @return 认证用户对象
     */
    public static AuthenticatedUser of(UserPrincipal principal) {
        return new AuthenticatedUser(principal);
    }
}
