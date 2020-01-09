package yyl.demo.common.security;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import com.github.relucent.base.plugin.security.AuthToken;
import com.github.relucent.base.plugin.security.Principal;
import com.github.relucent.base.plugin.security.Securitys;
import com.github.relucent.base.plugin.spring.context.WebContextHolder;

/**
 * 用户信息提供者
 */
public class SecurityImplementor implements Securitys {

    public static final String PRINCIPAL_NAME = "@principal";

    @Autowired
    private AuthRealm realm;


    @Override
    public void login(AuthToken token) {
        Principal principal = realm.doGetAuthenticationInfo(token);
        WebContextHolder.setSessionAttribute(PRINCIPAL_NAME, principal);
    }

    @Override
    public void logout() {
        WebContextHolder.invalidateSession();
    }

    @Override
    public Principal getPrincipal() {
        Principal principal = WebContextHolder.getSessionAttribute(PRINCIPAL_NAME);
        return principal == null ? Principal.NONE : principal;
    }

    /**
     * 获得当前登录用户
     * @param session 当前HTTP会话
     * @return 当前登录用户
     */
    protected static Principal getPrincipal(HttpSession session) {
        Principal principal = WebContextHolder.getSessionAttribute(session, PRINCIPAL_NAME);
        return principal == null ? Principal.NONE : principal;
    }
}
