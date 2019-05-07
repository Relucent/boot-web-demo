package yyl.demo.controller;

import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.relucent.base.plug.model.Result;
import com.github.relucent.base.util.page.Page;
import com.github.relucent.base.util.page.Pagination;

import yyl.demo.entity.Role;
import yyl.demo.service.RoleService;

/**
 * 角色管理
 * @author YYL
 */
@RestController
@RequestMapping(value = "/rest/role")
public class RoleRestController {

    // ==============================Fields===========================================
    @Autowired
    private RoleService roleService;
    // ==============================Methods==========================================

    /**
     * [POST] /rest/role/save <br>
     * 保存数据(新增|更新)
     */
    @PostMapping(value = "/save")
    public Result<?> save(@RequestBody Role role) {
        String id = role.getId();
        if (StringUtils.isEmpty(id)) {
            roleService.insert(role);
        } else {
            roleService.update(role);
        }
        return Result.ok();
    }

    /**
     * [DELETE] /rest/role/{id} <br>
     * 保存数据(新增|更新)
     */
    @DeleteMapping(value = "/{id}")
    public Result<?> delete(@PathVariable("id") String id) {
        roleService.deleteById(id);
        return Result.ok();
    }

    /**
     * [GET] /rest/role/{id} <br>
     * 保存数据(新增|更新)
     */
    @GetMapping(value = "/{id}")
    public Result<?> getById(@PathVariable("id") String id) {
        Role role = roleService.getById(id);
        return Result.ok(role);
    }

    /**
     * [GET] /rest/role/list <br>
     * 查询数据列表
     */
    @GetMapping(value = "/list")
    public Result<?> pagedQuery(Pagination pagination, Role condition) {
        condition = ObjectUtils.defaultIfNull(condition, new Role());
        Page<Role> page = roleService.pagedQuery(pagination, condition);
        return Result.ok(page);
    }

    /**
     * [GET] /rest/role/all <br>
     * 查询数据列表
     */
    @GetMapping(value = "/all")
    public Result<?> find() {
        List<Role> records = roleService.findAll();
        return Result.ok(records);
    }

    /**
     * [GET] /rest/role/{id}/permission-ids <br>
     * 根据角色查询功能权限(ID)
     */
    @GetMapping(value = "/getAuthPerm/{id}")
    public Result<?> findIdByRoleId(@PathVariable("id") String roleId) {
        List<String> permissionIds = roleService.findPermissionIdByRoleId(roleId);
        return Result.ok(permissionIds);
    }

    /**
     * [POST] /rest/role/{id}/permission-ids <br>
     * 更新角色功能权限关联
     */
    @PostMapping(value = "/{id}/permission-ids")
    public Result<?> updateRolePermissions(@PathVariable("id") String roleId, @RequestBody String[] permissionIds) {
        roleService.updateRolePermissions(roleId, permissionIds);
        return Result.ok();
    }
}
