package yyl.demo.common.security;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import com.github.relucent.base.common.exception.GeneralException;
import com.github.relucent.base.plugin.spring.context.WebContextHolder;

/**
 * 用户信息提供者
 */
public class Securitys {

    public static final String PRINCIPAL_NAME = "@principal";

    @Autowired
    private AuthRealm realm;

    /**
     * 用户登录
     * @param token 登录凭证
     * @throws GeneralException 登录失败抛出认证异常
     */
    public void login(UsernamePasswordToken token) {
        UserDetails principal = realm.doGetAuthenticationInfo(token);
        WebContextHolder.setSessionAttribute(PRINCIPAL_NAME, principal);
    }

    /**
     * 用户登出
     */
    public void logout() {
        WebContextHolder.invalidateSession();
    }

    /**
     * 获得当前登录用户
     * @return 当前登录用户
     */
    public UserDetails getPrincipal() {
        return getPrincipal(WebContextHolder.getSession());
    }

    /**
     * 获得当前登录用户
     * @param session 当前HTTP会话
     * @return 当前登录用户
     */
    protected static UserDetails getPrincipal(HttpSession session) {
        UserDetails principal = WebContextHolder.getSessionAttribute(session, PRINCIPAL_NAME);
        return principal == null ? UserDetails.NONE : principal;
    }
}
