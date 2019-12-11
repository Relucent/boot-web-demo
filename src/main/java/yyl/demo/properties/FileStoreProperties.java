package yyl.demo.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * 文件存储
 * @author YYL
 */
@Component
@ConfigurationProperties(prefix = "custom.file-store")
@Data
public class FileStoreProperties {
    private String directory = "/data/boot-web-demo/";
}