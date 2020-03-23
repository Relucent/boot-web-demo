package yyl.demo.common.configuration;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * _Mybatis 配置
 */
@Configuration
@MapperScan(basePackages = "yyl.demo", sqlSessionTemplateRef = "sqlSessionTemplate", annotationClass = Mapper.class)
public class MybatisConfiguration {
}
