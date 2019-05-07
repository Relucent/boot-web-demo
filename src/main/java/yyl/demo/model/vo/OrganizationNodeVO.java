package yyl.demo.model.vo;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "机构节点_OrganizationNodeVO")
@SuppressWarnings("serial")
@Data
public class OrganizationNodeVO implements Serializable {

    @ApiModelProperty("机构ID")
    private String id;

    @ApiModelProperty("上级机构ID")
    private String parentId;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("排序号")
    private Integer ordinal;

    @ApiModelProperty("子节点")
    private List<OrganizationNodeVO> children;
}
