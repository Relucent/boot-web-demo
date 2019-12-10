package yyl.demo.model.qo;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@ApiModel("角色的查询条件_RoleQO")
@Accessors(chain = true)
@Data
@SuppressWarnings("serial")
public class RoleQO implements Serializable {

    @ApiModelProperty("关键词")
    private String keyword;
}
