package yyl.demo.mapper;



import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import yyl.demo.entity.Department;
import yyl.demo.mapper.basic.BasicMapper;

/**
 * 部门_Mapper接口
 * @author _yyl
 */
@Mapper
public interface DepartmentMapper extends BasicMapper<Department, String> {

    /**
     * 查询部门
     * @param parentId 父部门ID
     * @return 部门列表
     */
    List<Department> findByParentId(String parentId);

    /**
     * 查询匹配条件的记录数
     * @param parentId 父部门ID
     * @return 记录数
     */
    Long countByParentId(String parentId);

    /**
     * 根据条件查询部门
     * @param condition 查询条件
     * @return 部门列表
     */
    List<Department> findBy(Department condition);
}
