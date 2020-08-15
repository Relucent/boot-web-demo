package yyl.demo.common.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@ConfigurationProperties(prefix = "global.security")
@Data
public class SecurityProperties {
    private String defaultUserPassword;
}
