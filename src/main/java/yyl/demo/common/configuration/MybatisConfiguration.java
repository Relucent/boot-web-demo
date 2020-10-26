package yyl.demo.common.configuration;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.relucent.base.plugin.mybatis.MybatisPaginationInterceptor;

/**
 * _Mybatis 配置
 */
@Configuration
@MapperScan(basePackages = "yyl.demo", sqlSessionTemplateRef = "sqlSessionTemplate", annotationClass = Mapper.class)
public class MybatisConfiguration {
    /**
     * 定义基础分页插件
     * @return 基础分页插件
     */
    @Bean
    public MybatisPaginationInterceptor mybatisPaginationInterceptor() {
        return new MybatisPaginationInterceptor();
    }
}
