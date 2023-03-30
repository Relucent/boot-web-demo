package yyl.demo.model.qo;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Schema(name = "ID查询(IdQO)")
@SuppressWarnings("serial")
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class IdQO implements Serializable {
    @Schema(description = "ID")
    @NotNull(message = "参数 ID 不能为空")
    private String id;

}
