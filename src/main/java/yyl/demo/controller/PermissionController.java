package yyl.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.relucent.base.plugin.model.Result;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import yyl.demo.common.constant.IdConstant;
import yyl.demo.common.enums.PermissionTypeEnum;
import yyl.demo.common.validation.Valids;
import yyl.demo.model.dto.PermissionDTO;
import yyl.demo.model.qo.IdQO;
import yyl.demo.model.vo.PermissionNodeVO;
import yyl.demo.model.vo.PermissionVO;
import yyl.demo.service.PermissionService;

/**
 * 功能权限管理
 * @author YYL
 */
@Tag(name = "功能权限")
@RestController
@RequestMapping("/rest/permission")
public class PermissionController {
    // ==============================Fields===========================================
    @Autowired
    private PermissionService permissionService;

    // ==============================Methods==========================================
    @Operation(summary = "新增")
    @PostMapping("/save")
    public Result<?> save(@RequestBody @Validated PermissionDTO dto) {
        permissionService.save(dto);
        return Result.ok();
    }

    @Operation(summary = "删除")
    @PostMapping("/deleteById")
    public Result<?> deleteById(@RequestBody @Validated IdQO qo) {
        permissionService.deleteById(qo.getId());
        return Result.ok();
    }

    @Operation(summary = "修改")
    @PostMapping("/update")
    public Result<?> update(@RequestBody @Validated(Valids.Update.class) PermissionDTO dto) {
        permissionService.update(dto);
        return Result.ok();
    }

    @Operation(summary = "查询")
    @PostMapping("/getById")
    public Result<PermissionVO> getById(@RequestBody @Validated IdQO qo) {
        PermissionVO vo = permissionService.getById(qo.getId());
        return Result.ok(vo);
    }

    @Operation(summary = "查询功能全名称")
    @PostMapping("/getNamePathById")
    public Result<String> getNamePathById(@RequestBody @Validated IdQO qo) {
        String name = permissionService.getNamePathById(qo.getId());
        return Result.ok(name);
    }

    @Operation(summary = "查询菜单树")
    @PostMapping("/getMenuTree")
    public Result<List<PermissionNodeVO>> getMenuTree() {
        List<PermissionNodeVO> menus = permissionService.getPermissionTree(IdConstant.ROOT_ID, PermissionTypeEnum.MENU);
        return Result.ok(menus);
    }

    @Operation(summary = "查询功能树(全部层级)")
    @PostMapping("/getPermissionTree")
    public Result<List<PermissionNodeVO>> getPermissionTree() {
        List<PermissionNodeVO> nodes = permissionService.getPermissionTree(IdConstant.ROOT_ID, PermissionTypeEnum.BUTTON);
        return Result.ok(nodes);
    }

    @Operation(summary = "刷新功能树索引(ID路径)")
    @PostMapping("/forceRefreshIdPath")
    public Result<?> forceRefreshIdPath() {
        permissionService.forceRefreshIdPath();
        return Result.ok();
    }
}
