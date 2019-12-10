package yyl.demo.model.qo;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@ApiModel("功能权限查询条件_PermissionQO")
@Accessors(chain = true)
@Data
@SuppressWarnings("serial")
public class PermissionQO implements Serializable {

    @ApiModelProperty("关键词")
    private String keyword;

    @ApiModelProperty("上级ID")
    private String parentId;
}
