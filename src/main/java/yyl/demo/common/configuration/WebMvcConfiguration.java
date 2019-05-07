package yyl.demo.common.configuration;

import java.util.Date;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.convert.converter.Converter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.relucent.base.plug.jackson.MyObjectMapper;
import com.github.relucent.base.util.lang.DateUtil;

import yyl.demo.common.resolver.MapReferenceArgumentResolver;
import yyl.demo.common.resolver.PaginationArgumentResolver;

@Configuration
public class WebMvcConfiguration extends WebMvcConfigurerAdapter {


    /**
     * <pre>
     *  <mvc:argument-resolvers>
     *      <bean class="yyl.common.resolver.PaginationMethodArgumentResolver" />
     *  </mvc:argument-resolvers>
     * </pre>
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        super.addArgumentResolvers(resolvers);
        resolvers.add(new MapReferenceArgumentResolver());
        resolvers.add(new PaginationArgumentResolver());
    }

    @Bean("objectMapper")
    @Primary
    public ObjectMapper objectMapper() {
        return MyObjectMapper.INSTANCE;
    }

    @Bean
    public Converter<String, Date> dateConverter() {
        return new Converter<String, Date>() {
            @Override
            public Date convert(String source) {
                return DateUtil.parse(source);
            }
        };
    }
}
