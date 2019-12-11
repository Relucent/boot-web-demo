package yyl.demo.model.dto;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import yyl.demo.common.validation.Valids;

@ApiModel("功能权限_PermissionDTO")
@SuppressWarnings("serial")
@Data
public class PermissionDTO implements Serializable {

    @NotEmpty(message = "ID不能为空", groups = Valids.Update.class)
    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("上级ID")
    private String parentId;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("类别")
    private Integer type;

    @ApiModelProperty("编码")
    private String code;

    @ApiModelProperty("访问路径")
    private String path;

    @ApiModelProperty("图标")
    private String icon;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("排序号")
    private String ordinal;
}
