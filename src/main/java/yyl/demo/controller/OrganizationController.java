package yyl.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.relucent.base.plugin.model.Result;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import yyl.demo.common.validation.Valids;
import yyl.demo.model.dto.OrganizationDTO;
import yyl.demo.model.qo.IdQO;
import yyl.demo.model.vo.OrganizationNodeVO;
import yyl.demo.model.vo.OrganizationVO;
import yyl.demo.service.OrganizationService;

@RestController
@RequestMapping("/rest/organization")
@Api(tags = "组织机构")
public class OrganizationController {

    // ==============================Fields===========================================
    @Autowired
    private OrganizationService organizationService;

    // ==============================Methods==========================================
    @ApiOperation("新增")
    @PostMapping("/save")
    public Result<?> save(@RequestBody @Validated OrganizationDTO dto) {
        organizationService.save(dto);
        return Result.ok();
    }

    @ApiOperation("删除")
    @PostMapping("/deleteById")
    public Result<?> deleteById(@RequestBody @Validated IdQO qo) {
        organizationService.deleteById(qo.getId());
        return Result.ok();
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public Result<?> update(@RequestBody @Validated(Valids.Update.class) OrganizationDTO dto) {
        organizationService.update(dto);
        return Result.ok();
    }

    @ApiOperation("查询")
    @PostMapping("/getById")
    public Result<OrganizationVO> getById(@RequestBody @Validated IdQO qo) {
        OrganizationVO vo = organizationService.getById(qo.getId());
        return Result.ok(vo);
    }

    @ApiOperation("查询机构名称")
    @PostMapping("/getNameById")
    public Result<String> getNameById(@RequestBody @Validated IdQO qo) {
        String name = organizationService.getNameById(qo.getId());
        return Result.ok(name);
    }

    @ApiOperation("查询机构树")
    @PostMapping("/getOrganizationTree")
    public Result<List<OrganizationNodeVO>> getOrganizationTree() {
        List<OrganizationNodeVO> nodes = organizationService.getOrganizationTree();
        return Result.ok(nodes);
    }

    @ApiOperation("刷新机构树索引(ID路径)")
    @PostMapping("/forceRefreshIdPath")
    public Result<?> forceRefreshIdPath() {
        organizationService.forceRefreshIdPath();
        return Result.ok();
    }
}