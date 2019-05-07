package yyl.demo.mapper.basic;

import java.io.Serializable;
import java.util.List;

/**
 * _Mapper 基础接口, 定义该接口是为了规范化, 方法不需要完全实现
 * @param <T> 实体类型
 * @param <I> 实体ID类型
 * @author _yyl
 */
public interface BasicMapper<T, I extends Serializable> {
    
    /**
     * 保存记录(等同于 insert or update)
     * @param record 记录
     */
    void save(T record);

    /**
     * 插入记录
     * @param record 记录
     */
    void insert(T record);

    /**
     * 刪除记录
     * @param id 记录ID
     */
    void deleteById(I id);

    /**
     * 删除全部记录
     */
    void deleteAll();

    /**
     * 更新记录
     * @param record 更新
     */
    void update(T record);

    /**
     * 查询记录
     * @param id 记录ID
     * @return 记录
     */
    T getById(I id);

    /**
     * 查询全部记录
     * @param id 记录ID
     * @return 记录列表
     */
    List<T> findAll();
}
