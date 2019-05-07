package yyl.demo.model.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("角色信息_RoleVO")
@SuppressWarnings("serial")
@Data
public class RoleVO implements Serializable {

    @ApiModelProperty("主键")
    private String id;
    @ApiModelProperty("编码")
    private String code;
    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("关联权限")
    private List<String> permissionIds;
}
