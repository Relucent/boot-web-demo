package yyl.demo.model.dto;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotEmpty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import yyl.demo.common.validation.Valids;

@ApiModel("角色_RoleDTO")
@SuppressWarnings("serial")
@Data
public class RoleDTO implements Serializable {

    @NotEmpty(message = "ID不能为空", groups = Valids.Update.class)
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
