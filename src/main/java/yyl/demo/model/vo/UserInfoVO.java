package yyl.demo.model.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("用户信息_UserInfoVO")
@SuppressWarnings("serial")
@Data
public class UserInfoVO implements Serializable {
    @ApiModelProperty("用户ID")
    private String id;
    @ApiModelProperty("用户名")
    private String username;
    @ApiModelProperty("真实姓名")
    private String realname;
}
