package yyl.demo.model.dto;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("启用禁用_IdEnableDTO")
@SuppressWarnings("serial")
@Data
public class IdEnableDTO implements Serializable {

    @ApiModelProperty("ID主键")
    @NotEmpty(message = "ID不能为空")
    private String id;

    @ApiModelProperty("是否可用(0禁用,1启用)")
    @NotEmpty(message = "是否可用标志不能为空")
    private Integer enabled;

}
