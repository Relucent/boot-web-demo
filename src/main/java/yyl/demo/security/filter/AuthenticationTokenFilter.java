package yyl.demo.security.filter;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import yyl.demo.security.model.AuthenticatedUser;
import yyl.demo.security.model.UserPrincipal;
import yyl.demo.security.store.AuthenticationTokenStore;
import yyl.demo.security.util.AccessTokenUtil;

/**
 * 身份验证过滤器
 */
public class AuthenticationTokenFilter extends OncePerRequestFilter {

    // ==============================Fields===========================================
    private final AuthenticationTokenStore authenticationTokenStore;

    // ==============================Constructors=====================================
    /**
     * 构造函数
     * @param authenticationTokenStore 身份验证令牌存储器
     */
    public AuthenticationTokenFilter(AuthenticationTokenStore authenticationTokenStore) {
        super();
        this.authenticationTokenStore = authenticationTokenStore;
    }

    // ==============================Methods==========================================
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        initializeContext(request);
        try {
            filterChain.doFilter(request, response);
        } finally {
            clearContext();
        }
    }

    /**
     * 初始化上下文
     * @param request HTTP 请求
     */
    protected void initializeContext(HttpServletRequest request) {

        // 从请求中获取访问令牌
        String token = AccessTokenUtil.getAccessToken(request);

        // 没有获取到访问令牌
        if (StringUtils.isEmpty(token)) {
            return;
        }

        // 获取当前用户信息
        UserPrincipal principal = authenticationTokenStore.getPrincipal(token);

        // 用户未登录或者用户
        if (UserPrincipal.isAnonymousUser(principal)) {
            return;
        }

        AuthenticatedUser authenticated = AuthenticatedUser.of(principal);
        Collection<? extends GrantedAuthority> authorities = authenticated.getAuthorities();

        // 保存登录信息到安全上下文
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(authenticated, null, authorities);
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    /**
     * 清理上下文
     */
    protected void clearContext() {
        SecurityContextHolder.clearContext();
    }
}
