package yyl.demo.model.qo;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

@Schema(name = "机构查询条件(OrgQO)")
@Accessors(chain = true)
@Data
@SuppressWarnings("serial")
public class OrganizationQO implements Serializable {

    @Schema(description = "名称")
    private String name;

    @Schema(description = "上级机构ID")
    private String parentId;
}
