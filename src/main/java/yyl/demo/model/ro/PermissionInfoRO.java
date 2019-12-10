package yyl.demo.model.ro;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("许可信息_PermissionInfoRO")
@SuppressWarnings("serial")
@Data
public class PermissionInfoRO implements Serializable {
    @ApiModelProperty("主键")
    private String id;
    @ApiModelProperty("编码")
    private String code;
}