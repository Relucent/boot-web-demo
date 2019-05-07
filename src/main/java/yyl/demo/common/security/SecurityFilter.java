package yyl.demo.common.security;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.relucent.base.plug.security.Principal;
import com.github.relucent.base.util.json.JsonUtil;
import com.github.relucent.base.util.model.Result;
import com.github.relucent.base.util.web.WebUtil;

/**
 * 安全控制过滤器
 */
public class SecurityFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String path = WebUtil.getPathWithinApplication(request);

        if (!authc(path)) {
            chain.doFilter(request, response);

            return;
        }
        Principal principal = SecurityImplementor.getPrincipal(request.getSession());
        if (!Principal.NONE.equals(principal)) {
            chain.doFilter(request, response);
            return;
        }

        if (isRest(path)) {
            response.setHeader("session-timeout-signal", "is-timeout");
            Result<?> result = Result.ofMessage(403, "SESSION超时");
            String json = JsonUtil.encode(result);
            WebUtil.writeJson(json, request, response);
            return;
        }

        String ctx = WebUtil.getContextPath(request);
        response.sendRedirect(ctx + "/login.html");
    }


    private boolean authc(String path) {
        if (path.startsWith("/s/")//
                || path.startsWith("/login.html") //
                || path.startsWith("/rest/login") //
                || path.startsWith("/rest/logout")) {
            return false;
        }
        return true;
    }

    private boolean isRest(String path) {
        return path.startsWith("/rest/");
    }

    @Override
    public void init(FilterConfig config) throws ServletException {}

    @Override
    public void destroy() {}

}
