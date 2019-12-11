package yyl.demo.model.dto;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotEmpty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import yyl.demo.common.validation.Valids;

@ApiModel("用户_UserDTO")
@SuppressWarnings("serial")
@Data
public class UserDTO implements Serializable {

    @ApiModelProperty("ID")
    @NotEmpty(message = "ID不能为空", groups = Valids.Update.class)
    private String id;

    @ApiModelProperty("机构ID")
    private String organizationId;

    @NotEmpty(message = "用户名不能为空")
    @ApiModelProperty("用户名")
    private String username;

    @NotEmpty(message = "用户姓名不能为空")
    @ApiModelProperty("用户姓名")
    private String realname;

    @ApiModelProperty("性别")
    private String sex;

    @ApiModelProperty("电话")
    private String phone;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("是否可用")
    private Integer enabled;

    @ApiModelProperty("关联角色")
    private List<String> roleIds;
}
