package yyl.demo.security;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.github.relucent.base.plugin.spring.context.WebContextHolder;

import yyl.demo.security.model.AuthenticatedUser;
import yyl.demo.security.model.UserPrincipal;
import yyl.demo.security.util.AccessTokenUtil;

/**
 * 用户信息提供者
 */
public class Securitys {

    /**
     * 获得当前登录用户ID
     * @return 当前登录用户ID
     */
    public static String getUserId() {
        return getPrincipal().getId();
    }

    /**
     * 获得当前登录用户
     * @return 当前登录用户
     */
    public static UserPrincipal getPrincipal() {
        return Optional.ofNullable(getAuthentication())//
                .map(Authentication::getPrincipal)//
                .filter(AuthenticatedUser.class::isInstance)//
                .map(AuthenticatedUser.class::cast)//
                .map(AuthenticatedUser::getPrincipal)//
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
     * 获得访问令牌
     * @return 访问令牌
     */
    public static String getAccessToken() {
        HttpServletRequest request = WebContextHolder.getRequest();
        return AccessTokenUtil.getAccessToken(request);
    }
}
