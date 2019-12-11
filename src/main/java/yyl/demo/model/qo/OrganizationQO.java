package yyl.demo.model.qo;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@ApiModel("机构查询条件(OrgQO)")
@Accessors(chain = true)
@Data
@SuppressWarnings("serial")
public class OrganizationQO implements Serializable {

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("上级机构ID")
    private String parentId;
}
