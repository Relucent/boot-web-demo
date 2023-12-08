package yyl.demo.model.dto;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(name = "认证令牌(用户名，密码)_UsernamePasswordDTO")
@Data
@SuppressWarnings("serial")
public class UsernamePasswordDTO implements Serializable {

    @Schema(description = "用户名")
    @NotEmpty(message = "用户名不能为空")
    private String username;

    @Schema(description = "密码")
    @NotEmpty(message = "密码不能为空")
    private String password;

    @Schema(description = "秘钥ID")
    private String rsaId;
}
