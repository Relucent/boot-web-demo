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
import yyl.demo.common.model.PageVO;
import yyl.demo.common.model.PaginationQO;
import yyl.demo.common.validation.Valids;
import yyl.demo.model.dto.RoleDTO;
import yyl.demo.model.qo.IdQO;
import yyl.demo.model.qo.RoleQO;
import yyl.demo.model.ro.RoleRO;
import yyl.demo.model.vo.RoleVO;
import yyl.demo.service.RoleService;

/**
 * 角色管理
 * @author YYL
 */
@Tag(name = "角色管理")
@RestController
@RequestMapping("/rest/role")
public class RoleController {

    // ==============================Fields===========================================
    @Autowired
    private RoleService roleService;

    // ==============================Methods==========================================
    @Operation(summary = "新增")
    @PostMapping("/save")
    public Result<?> save(@RequestBody @Validated RoleDTO dto) {
        roleService.save(dto);
        return Result.ok();
    }

    @Operation(summary = "删除")
    @PostMapping("/deleteById")
    public Result<?> deleteById(@RequestBody @Validated IdQO qo) {
        roleService.deleteById(qo.getId());
        return Result.ok();
    }

    @Operation(summary = "修改")
    @PostMapping("/update")
    public Result<?> update(@RequestBody @Validated(Valids.Update.class) RoleDTO dto) {
        roleService.update(dto);
        return Result.ok();
    }

    @Operation(summary = "查询")
    @PostMapping("/getById")
    public Result<RoleVO> getById(@RequestBody @Validated IdQO qo) {
        RoleVO vo = roleService.getById(qo.getId());
        return Result.ok(vo);
    }

    @Operation(summary = "查询列表")
    @PostMapping("/list")
    public Result<PageVO<RoleRO>> list(@RequestBody @Validated PaginationQO<RoleQO> qo) {
        PageVO<RoleRO> page = roleService.list(qo);
        return Result.ok(page);
    }

    @Operation(summary = "全部角色")
    @PostMapping("/findAll")
    public Result<List<RoleRO>> findAll() {
        List<RoleRO> records = roleService.findAll();
        return Result.ok(records);
    }
}
