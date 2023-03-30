package yyl.demo.model.dto;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import yyl.demo.common.validation.Valids;

@Schema(name = "功能权限_PermissionDTO")
@SuppressWarnings("serial")
@Data
public class PermissionDTO implements Serializable {

    @NotEmpty(message = "ID不能为空", groups = Valids.Update.class)
    @Schema(description = "主键")
    private String id;

    @Schema(description = "上级ID")
    private String parentId;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "类别")
    private Integer type;

    @Schema(description = "编码")
    private String code;

    @Schema(description = "访问路径")
    private String path;

    @Schema(description = "图标")
    private String icon;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "排序号")
    private String ordinal;
}
