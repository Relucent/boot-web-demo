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
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.github.relucent.base.common.json.JsonUtil;
import com.github.relucent.base.common.web.WebUtil;
import com.github.relucent.base.plugin.model.Result;

import yyl.demo.common.matcher.PermitUrlMatcher;
import yyl.demo.properties.WebSecurityProperties;
import yyl.demo.security.filter.AuthenticationTokenFilter;
import yyl.demo.security.store.AuthenticationTokenStore;
import yyl.demo.security.store.RsaKeyPairStore;

/**
 * 安全相关配置
 */
@Configuration
@EnableConfigurationProperties(WebSecurityProperties.class)
public class WebSecurityConfiguration {

	// =======================Fields==================================================
	@Autowired
	private ApplicationContext applicationContext;
	@Autowired
	private WebSecurityProperties properties;

	// =======================Constructors============================================
	protected WebSecurityConfiguration() {
	}

	// =======================Methods=================================================
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		PermitUrlMatcher permitUrlMatcher = permitUrlMatcher();
		return http// ->
				.authorizeRequests(scope -> scope
						// 不需要身份认证的资源
						.antMatchers(permitUrlMatcher.getPermitAllUrls()).permitAll()
						// 需要进行身份认证才能访问的资源
						.antMatchers(permitUrlMatcher.getAuthenticatedUrls()).authenticated()
						// 其余资源放行
						.anyRequest().permitAll()//
				)
				// 开启跨域访问
				.cors(scope -> scope.disable())
				// 关闭跨域请求防护
				.csrf(scope -> scope.disable())
				// SESSION
				.sessionManagement(scope -> scope.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				// 自定义权限拦截器
				.addFilterBefore(authenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class)
				// 异常处理
				.exceptionHandling(scope -> scope
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
						})//
				)
				// 禁用缓存 | 允许 IFrame 嵌套
				.headers(scope -> scope.cacheControl().and().frameOptions().disable())
				// 构建 SecurityFilterChain
				.build();
	}

	@Bean
	WebSecurityCustomizer webSecurityCustomizer() {
		return new WebSecurityCustomizer() {
			@Override
			public void customize(WebSecurity web) {
				// 放行所有OPTIONS请求
				web.ignoring().antMatchers(HttpMethod.OPTIONS, "/**");

			}
		};
	}

	/**
	 * 身份验证过滤器
	 * @return 身份验证过滤器
	 */
	@Bean
	AuthenticationTokenFilter authenticationTokenFilter() {
		return new AuthenticationTokenFilter(authenticationTokenStore());
	}

	/**
	 * 身份验证令牌存储器
	 * @return 身份验证令牌存储器
	 */
	@Primary
	@Bean
	AuthenticationTokenStore authenticationTokenStore() {
		return new AuthenticationTokenStore(properties);
	}

	/**
	 * RSA密钥对存储器
	 * @return RSA密钥对存储器
	 */
	@Primary
	@Bean
	RsaKeyPairStore rsaKeyPairStore() {
		return new RsaKeyPairStore();
	}

	/** 密码编码器 */
	@Primary
	@Bean
	PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	@Bean
	@ConditionalOnMissingBean(UserDetailsService.class)
	UserDetailsService userDetailsService() {
		return username -> null;
	}

	/**
	 * 许可URL匹配
	 * @return 许可URL匹配器
	 */
	@Bean
	PermitUrlMatcher permitUrlMatcher() {
		return new PermitUrlMatcher(applicationContext, properties);
	}
}