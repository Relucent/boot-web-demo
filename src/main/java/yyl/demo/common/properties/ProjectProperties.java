package yyl.demo.common.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * 自定义属性
 * @author YYL
 */
@Component
@ConfigurationProperties(prefix = "global.project")
@Data
public class ProjectProperties {
    /** 项目版本号 */
    private String version = "0.0.0";
}
