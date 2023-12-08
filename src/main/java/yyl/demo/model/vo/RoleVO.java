package yyl.demo.model.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(name = "角色信息_RoleVO")
@SuppressWarnings("serial")
@Data
public class RoleVO implements Serializable {

    @Schema(description = "主键")
    private String id;
    @Schema(description = "编码")
    private String code;
    @Schema(description = "名称")
    private String name;
    @Schema(description = "备注")
    private String remark;

    @Schema(description = "关联权限")
    private List<String> permissionIds;
}
