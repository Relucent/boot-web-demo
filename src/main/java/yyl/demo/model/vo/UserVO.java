package yyl.demo.model.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(name = "用户信息_UserVO")
@SuppressWarnings("serial")
@Data
public class UserVO implements Serializable {

    @Schema(description = "主键")
    private String id;

    @Schema(description = "机构ID")
    private String organizationId;

    @Schema(description = "机构名称")
    private String organizationName;

    @Schema(description = "登录账号")
    private String username;

    @Schema(description = "用户姓名")
    private String realname;

    @Schema(description = "性别")
    private String sex;

    @Schema(description = "电话")
    private String phone;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "是否可用")
    private Integer enabled;

    @Schema(description = "关联角色")
    private List<String> roleIds;
}
