package yyl.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.relucent.base.plugin.model.Result;

import yyl.demo.common.security.Securitys;
import yyl.demo.common.security.UserPrincipal;
import yyl.demo.common.security.UsernamePasswordToken;
import yyl.demo.service.AuthRealmService;

/**
 * 主页/登录/注销 视图类
 */
@RestController
@RequestMapping("/rest/passport")
public class IndexRestController {

    // ==============================Fields===========================================
    @Autowired
    private AuthRealmService authRealmService;

    // ==============================Methods==========================================
    /**
     * [POST] | /rest/login <br>
     * 用户登录
     */
    @PostMapping("/login")
    public Result<?> doLogin(@RequestBody UsernamePasswordToken token) {
        UserPrincipal principal = authRealmService.doGetAuthenticationInfo(token);
        Securitys.login(principal);
        return Result.ok();
    }

    /**
     * [POST] | /rest/logout <br>
     * 用户登出
     */
    @RequestMapping("/logout")
    public Result<?> logout() {
        Securitys.logout();
        return Result.ok();
    }
}
