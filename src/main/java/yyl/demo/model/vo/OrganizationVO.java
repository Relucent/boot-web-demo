package yyl.demo.model.vo;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(name = "机构信息_OrganizationVO")
@SuppressWarnings("serial")
@Data
public class OrganizationVO implements Serializable {

    @Schema(description = "机构ID")
    private String id;

    @Schema(description = "上级机构ID")
    private String parentId;

    @Schema(description = "上级机构名称")
    private String parentName;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "排序号")
    private Integer ordinal;
}
