package yyl.demo.model.dto;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotEmpty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import yyl.demo.common.validation.Valids;

@Schema(name = "用户_UserDTO")
@SuppressWarnings("serial")
@Data
public class UserDTO implements Serializable {

    @Schema(description = "ID")
    @NotEmpty(message = "ID不能为空", groups = Valids.Update.class)
    private String id;

    @Schema(description = "机构ID")
    private String organizationId;

    @NotEmpty(message = "用户名不能为空")
    @Schema(description = "用户名")
    private String username;

    @NotEmpty(message = "用户姓名不能为空")
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
