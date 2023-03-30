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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import yyl.demo.model.dto.UsernamePasswordDTO;
import yyl.demo.model.vo.RsaPublicKeyVO;
import yyl.demo.model.vo.UserInfoVO;
import yyl.demo.security.model.AccessToken;
import yyl.demo.service.AuthenticationTokenService;

@Tag(name = "认证")
@RestController
@RequestMapping("/rest/auth")
public class AuthController {

    // ==============================Fields===========================================
    @Autowired
    private AuthenticationTokenService authenticationTokenService;

    // ==============================Methods==========================================
    @Operation(summary = "获取加密公钥")
    @PostMapping("/getPublicKey")
    @PermitAll
    public Result<RsaPublicKeyVO> getPublicKey() {
        RsaPublicKeyVO vo = authenticationTokenService.getPublicKey();
        return Result.ok(vo);
    }

    @Operation(summary = "用户登录")
    @PostMapping("/login")
    @PermitAll
    public Result<AccessToken> doLogin(@RequestBody UsernamePasswordDTO dto) {
        AccessToken token = authenticationTokenService.login(dto);
        return Result.ok(token);
    }

    @Operation(summary = " 用户登出")
    @RequestMapping("/logout")
    @PermitAll
    public Result<?> logout() {
        authenticationTokenService.logout();
        WebContextHolder.invalidateSession();
        return Result.ok();
    }

    @Operation(summary = "用户信息")
    @PostMapping("/info")
    public Result<UserInfoVO> info(HttpServletRequest request) {
        UserInfoVO info = authenticationTokenService.getCurrentUser();
        return Result.ok(info);
    }
}
