package yyl.demo.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@ConfigurationProperties(prefix = "custom.encryptor")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EncryptorProperties {
	String password;
	Boolean banner;
}
