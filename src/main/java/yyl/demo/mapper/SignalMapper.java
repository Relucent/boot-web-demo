package yyl.demo.mapper;

import org.apache.ibatis.annotations.Mapper;

import yyl.demo.entity.Signal;
import yyl.demo.mapper.basic.BasicMapper;

/**
 * 信号
 */
@Mapper
public interface SignalMapper extends BasicMapper<Signal, String> {

    /**
     * 变更信号
     * @param ID
     */
    void emit(String id);
}
