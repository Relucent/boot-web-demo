package yyl.demo.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;
import lombok.experimental.Accessors;

@ConfigurationProperties(prefix = "custom.swagger")
@Data
public class SwaggerProperties {

    private boolean enabled = true;
    private String title = "API";
    private String version = "1.0.0";
    private String description = "接口文档";

    private GlobalHeaderInfo[] globalHeaders = { new GlobalHeaderInfo().setName("Access-Token").setDescription("访问令牌") };

    @Data
    @Accessors(chain = true)
    public static class GlobalHeaderInfo {
        private String name;
        private String description;
    }
}
