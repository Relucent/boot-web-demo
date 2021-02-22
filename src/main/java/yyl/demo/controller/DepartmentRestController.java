package yyl.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.relucent.base.plugin.model.Result;

import yyl.demo.common.model.BasicNodeVO;
import yyl.demo.service.DepartmentService;

/**
 * 部门管理
 */
@RestController
@RequestMapping("/rest/department")
public class DepartmentRestController {

    // ==============================Fields===========================================
    @Autowired
    private DepartmentService departmentService;

    // ==============================Methods==========================================
    /**
     * [GET] /rest/department/tree <br>
     * 查询部门数据(新增|更新)
     */
    @GetMapping("/tree")
    public Result<?> getTree() {
        List<BasicNodeVO> nodes = departmentService.getTree();
        return Result.ok(nodes);
    }
}
