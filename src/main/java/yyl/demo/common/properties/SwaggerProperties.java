package yyl.demo.common.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@ConfigurationProperties(prefix = "global.swagger")
@Data
public class SwaggerProperties {

    boolean enabled = true;

    String title = "API";

    String version = "1.0.0";
}
