package yyl.demo.common.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * 自定义属性
 */
@ConfigurationProperties(prefix = "global.project")
@Data
public class ProjectProperties {
    /** 项目版本号 */
    private String version = "0.0.0";
}
