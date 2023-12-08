package yyl.demo.model.ro;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(name = "许可信息_PermissionInfoRO")
@SuppressWarnings("serial")
@Data
public class PermissionInfoRO implements Serializable {
    @Schema(description = "主键")
    private String id;
    @Schema(description = "编码")
    private String code;
}