package yyl.demo.model.qo;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@ApiModel("用户的查询条件(UserQO)")
@Accessors(chain = true)
@Data
@SuppressWarnings("serial")
public class UserQO implements Serializable {

    @ApiModelProperty("姓名")
    private String keyword;

    @ApiModelProperty("机构ID")
    private String organizationId;
}
