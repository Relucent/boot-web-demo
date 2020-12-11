package yyl.demo.common.security;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.github.relucent.base.common.collection.CollectionUtil;
import com.github.relucent.base.common.exception.ExceptionHelper;

import yyl.demo.entity.User;
import yyl.demo.service.RoleService;
import yyl.demo.service.UserService;

/**
 * 权限验证类<br>
 * @author YYL
 */
public class AuthRealm {

    // ==============================Fields===========================================
    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    // ==============================Methods==========================================
    /**
     * (登陆验证)认证回调函数,登录时调用.
     * @param token 认证凭据
     * @return 认证信息
     */
    protected UserDetails doGetAuthenticationInfo(UsernamePasswordToken token) {

        String username = token.getUsername();
        String password = token.getPassword();

        if (StringUtils.isEmpty(username)) {
            throw ExceptionHelper.prompt("用户名不能为空");
        }
        if (StringUtils.isEmpty(password)) {
            throw ExceptionHelper.prompt("密码不能为空");
        }

        User user = userService.getByUsername(username);

        if (user == null) {
            throw ExceptionHelper.prompt("用户不存在");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw ExceptionHelper.prompt("用户名或密码错误");
        }

        String id = user.getId();

        List<String> roldIdList = userService.findRoleIdByUserId(id);
        String[] roleIds = CollectionUtil.toArray(roldIdList, String.class);

        List<String> permissionIdList = roleService.findPermissionIdByRoleIds(roleIds);
        String[] permissionIds = CollectionUtil.toArray(permissionIdList, String.class);

        UserDetails principal = new UserDetails();

        principal.setId(id);
        principal.setUsername(user.getUsername());
        principal.setRealname(user.getRealname());
        principal.setDepartmentId(user.getDepartmentId());
        principal.setRoleIds(roleIds);
        principal.setPermissionIds(permissionIds);

        return principal;
    }
}
