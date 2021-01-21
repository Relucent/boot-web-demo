package yyl.demo.common.security;

import java.util.Optional;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.github.relucent.base.plugin.spring.context.WebContextHolder;

/**
 * 用户信息提供者
 */
public class Securitys {

    /**
     * 获得当前登录用户
     * @return 当前登录用户
     */
    public static UserPrincipal getPrincipal() {
        return Optional.ofNullable(getAuthentication())//
                .map(Authentication::getPrincipal)//
                .filter(UserPrincipal.class::isInstance)//
                .map(UserPrincipal.class::cast)//
                .orElse(UserPrincipal.ANONYMOUS);
    }

    /**
     * 获得当前身份验证
     * @return 身份验证
     */
    public static Authentication getAuthentication() {
        SecurityContext context = SecurityContextHolder.getContext();
        return context != null ? context.getAuthentication() : null;
    }

    /**
     * 用户登录
     * @param principal 用户信息
     */
    public static void login(UserPrincipal principal) {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
        context.setAuthentication(authentication);
    }

    /**
     * 用户注销
     */
    public static void logout() {
        Authentication authentication = getAuthentication();
        if (authentication != null) {
            SecurityContext context = SecurityContextHolder.getContext();
            context.setAuthentication(null);
            WebContextHolder.invalidateSession();
            SecurityContextHolder.clearContext();
        }
    }
}
