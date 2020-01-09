package yyl.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.relucent.base.plugin.model.Result;
import com.github.relucent.base.plugin.security.AuthToken;
import com.github.relucent.base.plugin.security.Securitys;



/**
 * 主页/登录/注销 视图类
 */
@RestController
@RequestMapping(value = "/rest")
public class IndexRestController {

    // ==============================Fields===========================================
    @Autowired
    private Securitys securitys;

    // ==============================Methods==========================================
    /**
     * [POST] | /rest/login <br>
     * 用户登录
     */
    @PostMapping(value = "/login")
    public Result<?> doLogin(@RequestBody AuthToken token) {
        securitys.login(token);
        return Result.ok();
    }

    /**
     * [POST] | /rest/logout <br>
     * 用户登出
     */
    @RequestMapping(value = "/logout")
    public Result<?> logout() {
        securitys.logout();
        return Result.ok();
    }
}
