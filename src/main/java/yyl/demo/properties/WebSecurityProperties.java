package yyl.demo.properties;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@ConfigurationProperties(prefix = "custom.security.web-security")
@Data
public class WebSecurityProperties {

    /** 不需要进行身份认证直接可以访问的URL */
    private String[] permitAllUrls = {};

    /** 需要进行身份认证才能访问的URL */
    private String[] authenticatedUrls = { "/rest/**" };

    /** 端点URL（不需要进行身份认证） */
    private String[] endpointUrls = {
            // 监控
            "/actuator/**", "/actuator/**/**",
            // 网关 Swagger
            "/doc.html", "/v2/api-docs/**", "/v3/api-docs/**", //
            "/swagger-resources/**", "/webjars/**", "/swagger/**", "/swagger-ui/**", //
            // 网站图标
            "/favicon.ico", //
            // 静态资源
            "/", "/index.html", "/__/**", "/views/**"//
    };

    /** 用户访问令牌有效期 （默认1天） */
    private Duration accessTokenTtl = Duration.parse("P1D");
    /** 用户访问令牌最长空闲时间 （默认1小时） */
    private Duration accessTokenMaxIdleTime = Duration.parse("PT10H");
}
