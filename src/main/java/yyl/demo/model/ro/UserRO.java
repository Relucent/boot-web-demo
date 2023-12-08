package yyl.demo.model.ro;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(name = "用户信息.UserRO")
@SuppressWarnings("serial")
@Data
public class UserRO implements Serializable {

    @Schema(description = "主键")
    private String id;

    @Schema(description = "机构ID")
    private String organizationId;

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
}
