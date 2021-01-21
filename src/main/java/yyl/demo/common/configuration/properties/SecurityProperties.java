package yyl.demo.common.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@ConfigurationProperties(prefix = "global.security")
@Data
public class SecurityProperties {

    private String defaultUserPassword;

    private String[] ignoreUrls = { "/rest/auth/index/**" };
}
