package yyl.demo.model.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "用户信息_UserVO")
@SuppressWarnings("serial")
@Data
public class UserVO implements Serializable {

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("机构ID")
    private String organizationId;

    @ApiModelProperty("机构名称")
    private String organizationName;

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

    @ApiModelProperty("关联角色")
    private List<String> roleIds;
}
