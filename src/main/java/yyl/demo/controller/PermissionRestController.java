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

import com.github.relucent.base.common.page.Page;
import com.github.relucent.base.common.page.Pagination;
import com.github.relucent.base.plugin.model.Result;

import yyl.demo.common.constant.Ids;
import yyl.demo.common.constant.PermissionTypes;
import yyl.demo.common.model.BasicNodeVO;
import yyl.demo.entity.Permission;
import yyl.demo.service.PermissionService;

/**
 * 功能权限管理
 * @author YYL
 */
@RestController
@RequestMapping("/rest/permission")
public class PermissionRestController {
    // ==============================Fields===========================================
    @Autowired
    private PermissionService permissionService;

    // ==============================Methods==========================================
    /**
     * [POST] /rest/user/save <br>
     * 保存数据(新增|更新)
     */
    @PostMapping("/save")
    public Result<?> save(@RequestBody Permission permission) {
        String id = permission.getId();
        if (id == null) {
            permissionService.insert(permission);
        } else {
            permissionService.update(permission);
        }
        return Result.ok();
    }

    /**
     * [DELETE] /rest/permission/{id} <br>
     * 删除数据
     */
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable("id") String id) {
        permissionService.deleteById(id);
        return Result.ok();
    }

    /**
     * [GET] /rest/permission/{id} <br>
     * 查询数据
     */
    @GetMapping("/{id}")
    public Result<?> getById(@PathVariable("id") String id) {
        Permission permission = permissionService.getById(id);
        return Result.ok(permission);
    }

    /**
     * [GET] /rest/permission/list <br>
     * 查询数据列表
     */
    @GetMapping("/list")
    public Result<?> pagedQuery(Pagination pagination, Permission condition) {
        condition = ObjectUtils.defaultIfNull(condition, new Permission());
        Page<Permission> page = permissionService.pagedQuery(pagination, condition);
        return Result.ok(page);
    }

    /**
     * [GET] /rest/permission/menu-tree/{rootId} <br>
     * 查询菜单树(只到菜单层级)
     */
    @GetMapping("/menu-tree/{rootId}")
    public Result<?> menuTree(@PathVariable("rootId") String rootId) {
        List<BasicNodeVO> menus = permissionService.getMenuTree(rootId, PermissionTypes.MENU);
        return Result.ok(menus);
    }

    /**
     * [GET] /rest/permission/menu-all-tree <br>
     * 查询菜单功能树(全部层级)
     */
    @GetMapping("/menu-all-tree")
    public Result<?> menuPermTree() {
        List<BasicNodeVO> nodes = permissionService.getMenuTree(Ids.ROOT_ID, PermissionTypes.BUTTON);
        return Result.ok(nodes);
    }
}
