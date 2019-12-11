package yyl.demo.model.qo;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("用户ID_UserIdQO")
@SuppressWarnings("serial")
@Data
public class UserIdQO implements Serializable {

    @ApiModelProperty("用户ID")
    private String userId;
}
