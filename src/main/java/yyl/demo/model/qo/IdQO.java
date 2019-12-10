package yyl.demo.model.qo;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@ApiModel("ID查询(IdQO)")
@SuppressWarnings("serial")
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class IdQO implements Serializable {
    @ApiModelProperty("ID")
    @NotNull(message = "参数 ID 不能为空")
    private String id;

}
