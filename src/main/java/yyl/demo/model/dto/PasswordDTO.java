package yyl.demo.model.dto;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(name = "新旧密码_PasswordDTO")
@SuppressWarnings("serial")
@Data
public class PasswordDTO implements Serializable {
    @NotEmpty(message = "旧密码不能为空")
    @Schema(description = "旧密码")
    private String oldPassword;
    @Schema(description = "新密码")
    @NotEmpty(message = "新密码不能为空")
    private String newPassword;
    @Schema(description = "秘钥ID")
    private String rsaId;
}
