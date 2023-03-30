package yyl.demo.common.model;

import com.github.relucent.base.common.page.Pagination;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(name = "分页查询对象(PaginationQO)")
@Data
@SuppressWarnings("serial")
public class PaginationQO<T> implements Pagination {
    @Schema(description = "查询的偏移量，从0开始", example = "0")
    private long offset = 1;
    @Schema(description = "每页记录条数", example = "20")
    private long limit = 20;
    @Schema(description = "查询条件")
    private T filter;
}