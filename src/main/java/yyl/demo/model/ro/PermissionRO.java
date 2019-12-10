package yyl.demo.model.ro;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("许可信息_PermissionRO")
@SuppressWarnings("serial")
@Data
public class PermissionRO implements Serializable {

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("上级ID")
    private String parentId;

    @ApiModelProperty("上级名称")
    private String parentName;

    @ApiModelProperty("类别")
    private Integer type;

    @ApiModelProperty("名称")
    private String name;

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
