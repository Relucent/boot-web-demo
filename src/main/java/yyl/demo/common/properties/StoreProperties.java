package yyl.demo.common.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * 文件存储
 * @author YYL
 */
@Component
@ConfigurationProperties(prefix = "global.store")
@Data
public class StoreProperties {
    private String directory = "/data/boot-web-demo/";
}