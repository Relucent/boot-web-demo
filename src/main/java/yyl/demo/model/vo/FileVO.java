package yyl.demo.model.vo;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(name = "文件信息_FileVO")
@SuppressWarnings("serial")
@Data
public class FileVO implements Serializable {
    @Schema(description = "文件ID")
    private String id;
    @Schema(description = "文件名称")
    private String name;
    @Schema(description = "文件扩展名")
    private String extension;
    @Schema(description = "文件尺寸")
    private Long length;
    @Schema(description = "内容类型")
    private String contentType;
    @Schema(description = "存储路径")
    private String path;
}
