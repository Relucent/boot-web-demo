package yyl.demo.configuration;

import java.util.Date;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.convert.converter.Converter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.relucent.base.common.time.DateUtil;
import com.github.relucent.base.plugin.jackson.MyObjectMapper;

import yyl.demo.common.resolver.PaginationArgumentResolver;
import yyl.demo.security.interceptor.SecurityHandlerInterceptor;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    /**
     * 设置拦截器
     * @param registry 拦截器注册器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SecurityHandlerInterceptor()).addPathPatterns("/rest/**");
    }

    /**
     * <pre>
     *  <mvc:argument-resolvers>
     *      <bean class="edmp.common.resolver.PaginationMethodArgumentResolver" />
     *  </mvc:argument-resolvers>
     * </pre>
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new PaginationArgumentResolver());
    }

    /**
     * JSON ObjectMapper
     * @return ObjectMapper
     */
    @Bean("objectMapper")
    @Primary
    public ObjectMapper objectMapper() {
        return MyObjectMapper.INSTANCE;
    }

    /**
     * 设置转换器
     * @return 日期类型转换器
     */
    @Bean
    public Converter<String, Date> dateConverter() {
        return new Converter<String, Date>() {
            @Override
            public Date convert(String source) {
                return DateUtil.parseDate(source);
            }
        };
    }

    /**
     * 设置跨域访问
     * @param registry 跨域资源共享注册器
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")//
                .allowedOrigins("*")//
                .allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE")//
                .maxAge(3600)//
                .allowCredentials(true);
    }
}
