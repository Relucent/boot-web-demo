package yyl.demo.model.vo;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(name = "RSA加密秘钥_RsaPublicKeyVO")
@SuppressWarnings("serial")
@Data
public class RsaPublicKeyVO implements Serializable {
    @Schema(description = "秘钥ID")
    private String rsaId;
    @Schema(description = "公钥串")
    private String publicKey;
}