package yyl.demo.model.qo;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@Schema(name = "角色的查询条件_RoleQO")
@Accessors(chain = true)
@Data
@SuppressWarnings("serial")
public class RoleQO implements Serializable {

    @Schema(description = "关键词")
    private String keyword;
}
