package yyl.demo.model.ro;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "用户信息.UserRO")
@SuppressWarnings("serial")
@Data
public class UserRO implements Serializable {

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("机构ID")
    private String organizationId;

    @ApiModelProperty("登录账号")
    private String username;

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
}
