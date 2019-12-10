package yyl.demo.security.util;

import java.util.Optional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.util.WebUtils;

import yyl.demo.common.constant.SecurityConstant;

public class AccessTokenUtil {

    /**
     * 从请求中获取访问令牌
     * @param request HTTP 请求
     * @return 访问令牌
     */
    public static String getAccessToken(HttpServletRequest request) {

        // 无效请求
        if (request == null) {
            return null;
        }

        // 从请求头获取访问令牌
        String token = request.getHeader(SecurityConstant.ACCESS_TOKEN_HEADER);

        // 从Cookie中获取访问令牌
        if (StringUtils.isEmpty(token)) {
            token = Optional.ofNullable(WebUtils.getCookie(request, SecurityConstant.ACCESS_TOKEN_HEADER)).map(Cookie::getValue).orElse(null);
        }

        // 从请求参数中获取访问令牌
        if (StringUtils.isEmpty(token)) {
            token = request.getParameter(SecurityConstant.ACCESS_TOKEN_HEADER);
        }

        return token;
    }

}
