package yyl.demo.model.ro;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(name = "角色信息_RoleInfoRO")
@SuppressWarnings("serial")
@Data
public class RoleInfoRO implements Serializable {
    @Schema(description = "主键")
    private String id;
    @Schema(description = "编码")
    private String code;
}