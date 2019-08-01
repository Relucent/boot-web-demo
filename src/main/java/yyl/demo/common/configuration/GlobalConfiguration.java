package yyl.demo.common.configuration;

import javax.annotation.PostConstruct;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.github.relucent.base.plug.security.Securitys;
import com.github.relucent.base.util.json.JsonUtil;

import yyl.demo.common.json.JacksonHandler;
import yyl.demo.common.security.AuthRealm;
import yyl.demo.common.security.SecurityFilter;
import yyl.demo.common.security.SecurityImplementor;
import yyl.demo.common.thymeleaf.CustomThymeleafDialect;

/**
 * 项目公用配置
 * 
 * @author YYL
 */
@Configuration
public class GlobalConfiguration {

    /** 权限信息工具 */
    @Primary
    @Bean
    public Securitys securitys() {
        return new SecurityImplementor();
    }

    /** 密码编码器 */
    @Primary
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    /** 安全过滤器(注册) */
    @Bean
    public FilterRegistrationBean<SecurityFilter> securityFilterRegistration() {
        FilterRegistrationBean<SecurityFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(securityFilter());
        registration.addUrlPatterns("/*");
        registration.setName("security_filter");
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registration;
    }

    /** 安全过滤器 */
    @Primary
    @Bean
    public SecurityFilter securityFilter() {
        return new SecurityFilter();
    }

    /** 认证领域类 */
    @Bean
    public AuthRealm authRealm() {
        return new AuthRealm();
    }

    /** 自定义_Thymeleaf标签 */
    @Bean
    public CustomThymeleafDialect customThymeleafDialect() {
        return new CustomThymeleafDialect(securitys());
    }

    @PostConstruct
    public void initialize() {
        JsonUtil.setHANDLER(JacksonHandler.INSTANCE);
    }

}
