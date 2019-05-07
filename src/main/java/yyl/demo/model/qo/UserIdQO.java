package yyl.demo.model.qo;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(name = "用户ID_UserIdQO")
@SuppressWarnings("serial")
@Data
public class UserIdQO implements Serializable {

    @Schema(description = "用户ID")
    private String userId;
}
