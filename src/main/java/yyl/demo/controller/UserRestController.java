package yyl.demo.controller;

import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.relucent.base.common.collection.Mapx;
import com.github.relucent.base.common.page.Page;
import com.github.relucent.base.common.page.Pagination;
import com.github.relucent.base.plugin.model.Result;

import yyl.demo.entity.User;
import yyl.demo.service.UserService;

/**
 * 用户管理
 * @author YYL
 */
@RestController
@RequestMapping(value = "/rest/user")
public class UserRestController {

    // ==============================Fields===========================================
    @Autowired
    private UserService userService;

    // ==============================Methods==========================================
    /**
     * [POST] /rest/user/save <br>
     * 保存数据(新增|更新)
     */
    @PostMapping(value = "/save")
    public Result<?> save(@RequestBody User user) {
        String id = user.getId();
        if (id == null) {
            userService.insert(user);
        } else {
            userService.update(user);
        }
        return Result.ok();
    }

    /**
     * [POST] /rest/user/enable <br>
     * [BODY] {id:?,enabled:?}<br>
     * 启用禁用
     */
    @PostMapping(value = "/enable")
    public Result<?> enable(@RequestBody Mapx params) {
        String id = params.getString("id");
        Integer enabled = params.getInteger("enabled");
        userService.enable(id, enabled);
        return Result.ok();
    }

    /**
     * [DELETE] /rest/user/{id} <br>
     * 根据编号删除用户
     */
    @DeleteMapping(value = "/{id}")
    public Result<?> delete(@PathVariable("id") String id) {
        userService.deleteById(id);
        return Result.ok();
    }

    /**
     * [GET] /rest/user/{id} <br>
     * 保存数据(新增|更新)
     */
    // @GetMapping(value = "/{id}")
    public Result<?> getById(@PathVariable("id") String id) {
        User user = userService.getById(id);
        return Result.ok(user);
    }

    /**
     * [GET] /rest/user/list <br>
     * 查询数据列表
     */
    @GetMapping(value = "/list")
    public Result<?> pagedQuery(Pagination pagination, User condition) {
        condition = ObjectUtils.defaultIfNull(condition, new User());
        Page<User> page = userService.pagedQuery(pagination, condition);
        return Result.ok(page);
    }

    /**
     * [GET] /rest/user/{id}/role-ids <br>
     * 查询用户的角色(ID)
     */
    @GetMapping(value = "/{id}/role-ids")
    public Result<?> findRoleIdByUserId(@PathVariable("id") String userId) {
        List<String> roleIds = userService.findRoleIdByUserId(userId);
        return Result.ok(roleIds);
    }

    /**
     * [POST] /rest/user/{id}/role-ids <br>
     * 更新用户关联的角色
     */
    @PostMapping(value = "/{id}/role-ids")
    public Result<?> updateUserRole(@PathVariable("id") String userId, @RequestBody String[] roleIds) {
        userService.updateUserRole(userId, roleIds);
        return Result.ok();
    }

}
