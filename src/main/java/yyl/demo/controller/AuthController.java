package yyl.demo.controller;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.relucent.base.plugin.model.Result;
import com.github.relucent.base.plugin.spring.context.WebContextHolder;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import yyl.demo.model.dto.UsernamePasswordDTO;
import yyl.demo.model.vo.UserInfoVO;
import yyl.demo.security.model.AccessToken;
import yyl.demo.service.AuthenticationTokenService;

@Api(tags = "认证")
@RestController
@RequestMapping("/rest/auth")
public class AuthController {

    // ==============================Fields===========================================
    @Autowired
    private AuthenticationTokenService authenticationTokenService;

    // ==============================Methods==========================================
    @ApiOperation("用户登录")
    @PostMapping("/login")
    @PermitAll
    public Result<AccessToken> doLogin(@RequestBody UsernamePasswordDTO dto) {
        AccessToken token = authenticationTokenService.login(dto);
        return Result.ok(token);
    }

    @ApiOperation(" 用户登出")
    @RequestMapping("/logout")
    @PermitAll
    public Result<?> logout() {
        authenticationTokenService.logout();
        WebContextHolder.invalidateSession();
        return Result.ok();
    }

    @ApiOperation("用户信息")
    @PostMapping("/info")
    public Result<UserInfoVO> info(HttpServletRequest request) {
        UserInfoVO info = authenticationTokenService.getCurrentUser();
        return Result.ok(info);
    }
}
