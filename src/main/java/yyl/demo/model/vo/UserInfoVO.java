package yyl.demo.model.vo;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(name = "用户信息_UserInfoVO")
@SuppressWarnings("serial")
@Data
public class UserInfoVO implements Serializable {
    @Schema(description = "用户ID")
    private String id;
    @Schema(description = "用户名")
    private String username;
    @Schema(description = "真实姓名")
    private String realname;
}
