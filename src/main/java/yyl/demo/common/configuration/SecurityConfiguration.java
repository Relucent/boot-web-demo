package yyl.demo.common.configuration;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.github.relucent.base.common.json.JsonUtil;
import com.github.relucent.base.common.web.WebUtil;
import com.github.relucent.base.plugin.model.Result;

import yyl.demo.common.properties.SecurityProperties;
import yyl.demo.common.security.SecurityFilter;

/**
 * 安全相关配置
 */
@Configuration
@EnableConfigurationProperties(SecurityProperties.class)
@EnableAutoConfiguration(exclude = UserDetailsServiceAutoConfiguration.class)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private SecurityProperties securityProperties;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                // 不需要身份认证的资源
                .antMatchers(securityProperties.getIgnoreUrls()).permitAll()
                // 需要进行身份认证才能访问的资源
                .antMatchers("/rest/**").authenticated()
                // 其余资源放行
                .anyRequest().permitAll().and()
                // 开启跨域访问，关闭跨域请求防护
                .cors().disable().csrf().disable()
                // SESSION
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED).and()//
                // 自定义权限拦截器
                .addFilterBefore(securityFilter(), UsernamePasswordAuthenticationFilter.class)
                // 异常处理
                .exceptionHandling()
                // 未登录时
                .authenticationEntryPoint((request, response, authException) -> {
                    Result<?> result = Result.ofMessage(HttpServletResponse.SC_UNAUTHORIZED, "未登录");
                    WebUtil.writeJson(JsonUtil.encode(result), request, response);
                })
                // 未授权时
                .accessDeniedHandler((request, response, ex) -> {
                    Result<?> result = Result.ofMessage(HttpServletResponse.SC_FORBIDDEN, "未授权");
                    WebUtil.writeJson(JsonUtil.encode(result), request, response);
                }).and()
                // 禁用缓存
                .headers().cacheControl();
    }

    @Override
    public void configure(WebSecurity web) {
        // 放行所有OPTIONS请求
        web.ignoring().antMatchers(HttpMethod.OPTIONS, "/**");
    }

    @Bean
    public SecurityFilter securityFilter() {
        return new SecurityFilter();
    }

    /**
     * 初始密码编码器
     * @return 密码编码器
     */
    @Primary
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
