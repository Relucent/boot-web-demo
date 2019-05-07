package yyl.demo.model.ro;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(name = "机构信息_OrgRO")
@SuppressWarnings("serial")
@Data
public class OrganizationRO implements Serializable {

    @Schema(description = "机构ID")
    private String id;

    @Schema(description = "上级机构ID")
    private String parentId;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "排序号")
    private Integer ordinal;
}
