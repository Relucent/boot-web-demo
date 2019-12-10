package yyl.demo.model.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("机构_OrganizationDTO")
@SuppressWarnings("serial")
@Data
public class OrganizationDTO implements Serializable {

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
}
