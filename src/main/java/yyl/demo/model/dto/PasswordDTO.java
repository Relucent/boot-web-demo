package yyl.demo.model.dto;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("新旧密码_PasswordDTO")
@SuppressWarnings("serial")
@Data
public class PasswordDTO implements Serializable {
    @NotEmpty(message = "旧密码不能为空")
    @ApiModelProperty("旧密码")
    private String oldPassword;
    @ApiModelProperty("新密码")
    @NotEmpty(message = "新密码不能为空")
    private String newPassword;
}
