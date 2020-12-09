package yyl.demo.common.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.github.relucent.base.common.json.JsonUtil;
import com.github.relucent.base.common.web.WebUtil;
import com.github.relucent.base.plugin.model.Result;

import yyl.demo.common.annotations.PermissionAx;
import yyl.demo.common.constant.Ids;

public class SecurityHandlerInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod hm = (HandlerMethod) handler;
            PermissionAx ax = hm.getMethodAnnotation(PermissionAx.class);
            if (ax != null) {
                UserDetails principal = Securitys.getPrincipal(request.getSession());
                String userId = principal.getUserId();
                String[] permissionIds = principal.getPermissionIds();
                if (!Ids.ADMIN_ID.equals(userId) && !intersect(ax.value(), permissionIds)) {
                    response.setHeader("unauthorized-signal", "403");
                    Class<?> returnType = (Class<?>) hm.getReturnType().getGenericParameterType();
                    if (Result.class.isAssignableFrom(returnType)) {
                        Result<?> result = Result.ofMessage(403, "没有访问该资源的权限");
                        String json = JsonUtil.encode(result);
                        WebUtil.writeJson(json, request, response);
                    }
                    return false;
                }
            }
        }
        return true;
    }

    private boolean intersect(String[] array, String[] valueToFinds) {
        for (String value : array) {
            for (String valueToFind : valueToFinds) {
                if (valueToFind.equals(value)) {
                    return false;
                }
            }
        }
        return false;
    }
}
