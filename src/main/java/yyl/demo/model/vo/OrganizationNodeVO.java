package yyl.demo.model.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(name = "机构节点_OrganizationNodeVO")
@SuppressWarnings("serial")
@Data
public class OrganizationNodeVO implements Serializable {

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

    @Schema(description = "子节点")
    private List<OrganizationNodeVO> children;
}
