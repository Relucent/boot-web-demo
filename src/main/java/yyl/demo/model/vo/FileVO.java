package yyl.demo.model.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("文件信息_FileVO")
@SuppressWarnings("serial")
@Data
public class FileVO implements Serializable {
    @ApiModelProperty("文件ID")
    private String id;
    @ApiModelProperty("文件名称")
    private String name;
    @ApiModelProperty("文件扩展名")
    private String extension;
    @ApiModelProperty("文件尺寸")
    private Long length;
    @ApiModelProperty("内容类型")
    private String contentType;
    @ApiModelProperty("存储路径")
    private String path;
}
