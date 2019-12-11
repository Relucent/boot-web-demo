package yyl.demo.configuration;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.github.relucent.base.common.json.JsonUtil;
import com.github.relucent.base.common.web.WebUtil;
import com.github.relucent.base.plugin.model.Result;

import yyl.demo.common.matcher.PermitUrlMatcher;
import yyl.demo.properties.WebSecurityProperties;
import yyl.demo.security.filter.AuthenticationTokenFilter;
import yyl.demo.security.store.AuthenticationTokenStore;

/**
 * 安全相关配置
 */
@Configuration
@EnableConfigurationProperties(WebSecurityProperties.class)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    // =======================Fields==================================================
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private WebSecurityProperties properties;

    // =======================Constructors============================================
    protected WebSecurityConfiguration() {
    }

    // =======================Methods=================================================
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        PermitUrlMatcher permitUrlMatcher = permitUrlMatcher();
        http.authorizeRequests()
                // 不需要身份认证的资源
                .antMatchers(permitUrlMatcher.getPermitAllUrls()).permitAll()
                // 需要进行身份认证才能访问的资源
                .antMatchers(permitUrlMatcher.getAuthenticatedUrls()).authenticated()
                // 其余资源放行
                .anyRequest().permitAll().and()
                // 开启跨域访问，关闭跨域请求防护
                .cors().disable().csrf().disable()
                // SESSION
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()//
                // 自定义权限拦截器
                .addFilterBefore(authenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class)
                // 异常处理
                .exceptionHandling()
                // 未登录时
                .authenticationEntryPoint((request, response, authException) -> {
                    response.setStatus(HttpServletResponse.SC_OK);
                    Result<?> result = Result.ofMessage(HttpServletResponse.SC_UNAUTHORIZED, "未登录");
                    String json = JsonUtil.encode(result);
                    WebUtil.writeJson(json, request, response);
                })
                // 未授权时
                .accessDeniedHandler((request, response, ex) -> {
                    response.setStatus(HttpServletResponse.SC_OK);
                    Result<?> result = Result.ofMessage(HttpServletResponse.SC_FORBIDDEN, "未授权");
                    String json = JsonUtil.encode(result);
                    WebUtil.writeJson(json, request, response);
                }).and()
                // 禁用缓存
                .headers().cacheControl().and()
                // 允许 IFrame 嵌套
                .frameOptions().disable();
    }

    @Override
    public void configure(WebSecurity web) {
        // 放行所有OPTIONS请求
        web.ignoring().antMatchers(HttpMethod.OPTIONS, "/**");
    }

    /**
     * 身份验证过滤器
     * @return 身份验证过滤器
     */
    @Bean
    public AuthenticationTokenFilter authenticationTokenFilter() {
        return new AuthenticationTokenFilter(authenticationTokenStore());
    }

    /**
     * 身份验证令牌存储器
     * @return 身份验证令牌存储器
     */
    @Primary
    @Bean
    public AuthenticationTokenStore authenticationTokenStore() {
        return new AuthenticationTokenStore(properties);
    }

    /** 密码编码器 */
    @Primary
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    @ConditionalOnMissingBean(UserDetailsService.class)
    public UserDetailsService userDetailsService() {
        return username -> null;
    }

    /**
     * 许可URL匹配
     * @return 许可URL匹配器
     */
    @Bean
    public PermitUrlMatcher permitUrlMatcher() {
        return new PermitUrlMatcher(applicationContext, properties);
    }

}