package yyl.demo.model.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("功能权限节点_PermissionNodeVO")
@SuppressWarnings("serial")
@Data
public class PermissionNodeVO implements Serializable {

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("上级机构ID")
    private String parentId;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("编码")
    private String code;

    @ApiModelProperty("类别")
    private Integer type;

    @ApiModelProperty("访问路径")
    private String path;

    @ApiModelProperty("图标")
    private String icon;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("排序号")
    private String ordinal;

    @ApiModelProperty("子节点")
    private List<PermissionNodeVO> children;
}
