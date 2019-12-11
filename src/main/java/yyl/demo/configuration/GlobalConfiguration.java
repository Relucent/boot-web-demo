package yyl.demo.configuration;

import javax.annotation.PostConstruct;

import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.github.relucent.base.common.json.JsonUtil;
import com.github.relucent.base.plugin.jackson.JacksonHandler;

import yyl.demo.common.jackson.Jackson2ObjectMapperBuilderCustomizerImplement;

/**
 * 项目公用配置
 * @author YYL
 */
@Configuration
public class GlobalConfiguration {

    /**
     * _Jackson 定制器
     * @return _Jackson 定制器
     */
    @Primary
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer Jackson2ObjectMapperBuilderCustomizer() {
        return new Jackson2ObjectMapperBuilderCustomizerImplement();
    }

    @PostConstruct
    public void initialize() {
        JsonUtil.setHandler(JacksonHandler.INSTANCE);
    }
}
