package yyl.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.relucent.base.util.model.Result;

import yyl.demo.service.DepartmentService;
import yyl.demo.service.model.BasicNode;

/**
 * 部门管理
 */
@RestController
@RequestMapping(value = "/rest/department")
public class DepartmentRestController {

    // ==============================Fields===========================================
    @Autowired
    private DepartmentService departmentService;

    // ==============================Methods==========================================
    /**
     * [GET] /rest/department/tree <br>
     * 查询部门数据(新增|更新)
     */
    @GetMapping(value = "/tree")
    public Result<?> getById() {
        List<BasicNode> nodes = departmentService.getDeptTree();
        return Result.ok(nodes);
    }
}
