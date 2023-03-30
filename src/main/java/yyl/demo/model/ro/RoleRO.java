package yyl.demo.model.ro;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(name = "角色信息_RoleRO")
@SuppressWarnings("serial")
@Data
public class RoleRO implements Serializable {

    @Schema(description = "主键")
    private String id;
    @Schema(description = "编码")
    private String code;
    @Schema(description = "名称")
    private String name;
    @Schema(description = "备注")
    private String remark;
}
