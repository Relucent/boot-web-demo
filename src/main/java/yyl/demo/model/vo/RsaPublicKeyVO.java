package yyl.demo.model.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("RSA加密秘钥_RsaPublicKeyVO")
@SuppressWarnings("serial")
@Data
public class RsaPublicKeyVO implements Serializable {
    @ApiModelProperty("秘钥ID")
    private String rsaId;
    @ApiModelProperty("公钥串")
    private String publicKey;
}