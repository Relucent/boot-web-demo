package yyl.demo.controller;

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
import yyl.demo.model.dto.IdEnableDTO;
import yyl.demo.model.dto.PasswordDTO;
import yyl.demo.model.dto.UserDTO;
import yyl.demo.model.qo.IdQO;
import yyl.demo.model.qo.UserQO;
import yyl.demo.model.ro.UserRO;
import yyl.demo.model.vo.UserVO;
import yyl.demo.service.UserService;

/**
 * 用户管理
 * @author YYL
 */
@Tag(name = "用户管理")
@RestController
@RequestMapping("/rest/user")
public class UserController {

    // ==============================Fields===========================================
    @Autowired
    private UserService userService;

    // ==============================Methods==========================================
    @Operation(summary = "新增")
    @PostMapping("/save")
    public Result<?> save(@RequestBody @Validated UserDTO dto) {
        userService.save(dto);
        return Result.ok();
    }

    @Operation(summary = "删除")
    @PostMapping("/deleteById")
    public Result<?> deleteById(@RequestBody @Validated IdQO qo) {
        userService.deleteById(qo.getId());
        return Result.ok();
    }

    @Operation(summary = "修改")
    @PostMapping("/update")
    public Result<?> update(@RequestBody @Validated(Valids.Update.class) UserDTO dto) {
        userService.update(dto);
        return Result.ok();
    }

    @Operation(summary = "启用禁用")
    @PostMapping("/enableById")
    public Result<?> enableById(@RequestBody @Validated IdEnableDTO dto) {
        userService.enableById(dto.getId(), dto.getEnabled());
        return Result.ok();
    }

    @Operation(summary = "查询")
    @PostMapping("/getById")
    public Result<?> getById(@RequestBody @Validated IdQO qo) {
        UserVO vo = userService.getById(qo.getId());
        return Result.ok(vo);
    }

    @Operation(summary = "查询列表")
    @PostMapping("/list")
    public Result<PageVO<UserRO>> list(@RequestBody @Validated PaginationQO<UserQO> qo) {
        PageVO<UserRO> page = userService.list(qo);
        return Result.ok(page);
    }

    @Operation(summary = "重置密码")
    @PostMapping("/resetPasswordById")
    public Result<?> resetPasswordById(@RequestBody @Validated IdQO qo) {
        userService.resetPasswordById(qo.getId());
        return Result.ok();
    }

    @Operation(summary = "修改当前用户密码")
    @PostMapping("/updateCurrentPassword")
    public Result<?> updatePasswordByCurrent(@RequestBody @Validated PasswordDTO dto) {
        userService.updatePasswordByCurrent(dto);
        return Result.ok();
    }
}
