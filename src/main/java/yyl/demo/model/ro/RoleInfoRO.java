package yyl.demo.model.ro;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("角色信息_RoleInfoRO")
@SuppressWarnings("serial")
@Data
public class RoleInfoRO implements Serializable {
    @ApiModelProperty("主键")
    private String id;
    @ApiModelProperty("编码")
    private String code;
}