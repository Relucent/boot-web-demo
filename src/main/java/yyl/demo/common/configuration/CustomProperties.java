package yyl.demo.common.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * 自定义属性
 * @author YYL
 */
@Component
@ConfigurationProperties(prefix = "custom.properties")
@Data
public class CustomProperties {

    /** 用户默认密码 */
    private String defaultUserPassword;

}
