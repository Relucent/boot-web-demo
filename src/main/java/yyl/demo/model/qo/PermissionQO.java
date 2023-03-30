package yyl.demo.model.qo;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@Schema(name = "功能权限查询条件_PermissionQO")
@Accessors(chain = true)
@Data
@SuppressWarnings("serial")
public class PermissionQO implements Serializable {

    @Schema(description = "关键词")
    private String keyword;

    @Schema(description = "上级ID")
    private String parentId;
}
