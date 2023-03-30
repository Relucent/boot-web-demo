package yyl.demo.model.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(name = "功能权限节点_PermissionNodeVO")
@SuppressWarnings("serial")
@Data
public class PermissionNodeVO implements Serializable {

    @Schema(description = "主键")
    private String id;

    @Schema(description = "上级机构ID")
    private String parentId;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "编码")
    private String code;

    @Schema(description = "类别")
    private Integer type;

    @Schema(description = "访问路径")
    private String path;

    @Schema(description = "图标")
    private String icon;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "排序号")
    private String ordinal;

    @Schema(description = "子节点")
    private List<PermissionNodeVO> children;
}
