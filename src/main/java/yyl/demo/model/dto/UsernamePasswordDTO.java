package yyl.demo.model.dto;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("认证令牌(用户名，密码)_UsernamePasswordDTO")
@Data
@SuppressWarnings("serial")
public class UsernamePasswordDTO implements Serializable {

    @ApiModelProperty("用户名")
    @NotEmpty(message = "用户名不能为空")
    private String username;

    @ApiModelProperty("密码")
    @NotEmpty(message = "密码不能为空")
    private String password;

    @ApiModelProperty("秘钥ID")
    private String rsaId;
}
