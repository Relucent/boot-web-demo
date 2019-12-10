package yyl.demo.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;

import yyl.demo.common.enums.IntBoolEnum;
import yyl.demo.entity.FileEntity;

/**
 * 文件信息_Mapper接口
 */
@Mapper
public interface FileMapper extends BaseMapper<FileEntity> {

    // ====================MapperMethods=====================================

    // ====================DefaultMethods=====================================
    /**
     * 查询文件信息
     * @param id 文件ID
     * @return 文件信息
     */
    default FileEntity getById(String id) {
        LambdaQueryWrapper<FileEntity> lqw = Wrappers.lambdaQuery();
        lqw.eq(FileEntity::getId, id);
        lqw.eq(FileEntity::getDeleted, IntBoolEnum.N.value());
        return selectOne(lqw);
    }
}
