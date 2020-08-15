package yyl.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;

import yyl.demo.entity.Department;

/**
 * 部门_Mapper接口
 * @author YYL
 */
@Mapper
public interface DepartmentMapper extends BaseMapper<Department> {

    /**
     * 查询全部部门
     * @return 部门列表
     */
    default List<Department> selectAllList() {
        return selectList(Wrappers.emptyWrapper());
    }

    /**
     * 查询部门
     * @param parentId 父部门ID
     * @return 部门列表
     */
    default List<Department> selectListByParentId(String parentId) {
        return selectList(Wrappers.<Department>lambdaQuery().eq(Department::getParentId, parentId));
    }

    /**
     * 根据条件查询部门
     * @param condition 查询条件
     * @return 部门列表
     */
    default List<Department> selectListBy(Department condition) {
        return selectList(Wrappers.lambdaQuery(condition));
    }

    /**
     * 查询匹配条件的记录数
     * @param parentId 父部门ID
     * @return 记录数
     */
    default Integer selectCountByParentId(String parentId) {
        return selectCount(Wrappers.<Department>lambdaQuery().eq(Department::getParentId, parentId));
    }
}
