package yyl.demo.model.qo;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@Schema(name = "用户的查询条件(UserQO)")
@Accessors(chain = true)
@Data
@SuppressWarnings("serial")
public class UserQO implements Serializable {

    @Schema(description = "姓名")
    private String keyword;

    @Schema(description = "机构ID")
    private String organizationId;
}
