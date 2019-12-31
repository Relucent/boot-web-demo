package yyl.demo.common.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * 自定义属性
 * @author YYL
 */
@Component
@ConfigurationProperties(prefix = "global")
@Data
public class GlobalProperties {

    /** 安全信息 */
    private Security security = new Security();

    /** 项目信息 */
    @NestedConfigurationProperty
    private Project project = new Project();

    @Data
    public class Project {
        /** 项目版本号 */
        private String version = "0.0.0";
    }

    @Data
    public class Security {
        /** 用户默认密码 */
        private String defaultUserPassword;
    }
}
