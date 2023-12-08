package yyl.demo.model.dto;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotEmpty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import yyl.demo.common.validation.Valids;

@Schema(name = "角色_RoleDTO")
@SuppressWarnings("serial")
@Data
public class RoleDTO implements Serializable {

    @NotEmpty(message = "ID不能为空", groups = Valids.Update.class)
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
