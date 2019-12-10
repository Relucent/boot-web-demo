package yyl.demo.model.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "机构信息_OrganizationVO")
@SuppressWarnings("serial")
@Data
public class OrganizationVO implements Serializable {

    @ApiModelProperty("机构ID")
    private String id;

    @ApiModelProperty("上级机构ID")
    private String parentId;

    @ApiModelProperty("上级机构名称")
    private String parentName;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("排序号")
    private Integer ordinal;
}
